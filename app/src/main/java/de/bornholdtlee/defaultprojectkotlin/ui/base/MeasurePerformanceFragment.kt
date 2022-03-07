package de.bornholdtlee.defaultprojectkotlin.ui.base

import android.os.Handler
import android.os.Looper
import android.view.FrameMetrics
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import de.bornholdtlee.defaultprojectkotlin.core.utils.Logger
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToLong

/**
 * This fragment can be used to measure the performance of the layout inflation. The measurement only takes place in DEBUG mode.
 * It starts counting the draw time and measure time of each frame until no more rendering is done for one second.
 * Afterwards the FrameMetricsListener is de-registered until the fragment is loaded again from the backstack.
 */
open class MeasurePerformanceFragment(@LayoutRes resId: Int) : Fragment(resId) {

    private companion object {
        const val MEASURE_LENGTH_IN_FRAMES = 100
        const val POST_RESULT_DELAY = 1000L
    }

    private var counter: AtomicInteger = AtomicInteger(0)
    private var resultPosted: Boolean = false

    private var measure: MutableList<Long> = mutableListOf()
    private var draw: MutableList<Long> = mutableListOf()

    private val resultHandler = Handler(Looper.getMainLooper())
    private val frameMetricsHandler = Handler(Looper.getMainLooper())

    private val frameMetricsAvailableListener = Window.OnFrameMetricsAvailableListener { _, frameMetrics, _ ->
        if (!resultPosted) {
            val frameMetricsCopy = FrameMetrics(frameMetrics)

            if (counter.addAndGet(1) <= MEASURE_LENGTH_IN_FRAMES) {
                measure.add(frameMetricsCopy.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION))
                draw.add(frameMetricsCopy.getMetric(FrameMetrics.DRAW_DURATION))
            }

            resultHandler.removeCallbacks(resultRunnable)
            resultHandler.postDelayed(resultRunnable, POST_RESULT_DELAY)
        }
    }

    private val resultRunnable = Runnable {
        Logger.debug("FRAMES: ${counter.get()}")
        Logger.debug("MEASUREMENT: Max: ${TimeUnit.NANOSECONDS.toMillis(measure.maxOrNull() ?: 0L)}")
        Logger.debug("MEASUREMENT: Sum: ${TimeUnit.NANOSECONDS.toMillis(measure.sum())}")
        Logger.debug("MEASUREMENT: Average: ${TimeUnit.NANOSECONDS.toMillis(measure.average().roundToLong())}")
        Logger.debug("DRAW: Max: ${TimeUnit.NANOSECONDS.toMillis(draw.maxOrNull() ?: 0L)}")
        Logger.debug("DRAW: Sum: ${TimeUnit.NANOSECONDS.toMillis(draw.sum())}")
        Logger.debug("DRAW: Average: ${TimeUnit.NANOSECONDS.toMillis(draw.average().roundToLong())}")
        measure = mutableListOf()
        draw = mutableListOf()
        resultPosted = true
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.addOnFrameMetricsAvailableListener(frameMetricsAvailableListener, frameMetricsHandler)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.removeOnFrameMetricsAvailableListener(frameMetricsAvailableListener)
        counter = AtomicInteger(0)
        resultPosted = false
    }
}
