package de.bornholdtlee.defaultprojectkotlin.core.utils

import android.util.Log
import de.bornholdtlee.defaultprojectkotlin.BuildConfig.DEBUG

object Logger {

    private const val KOTLIN_EXTENSION = ".kt"
    private val isAllowedToLog = DEBUG

    @Suppress("ReturnCount")
    private fun getCallingClass(): String {
        val isLoggerClass = fun(stackTraceElement: StackTraceElement): Boolean = stackTraceElement.className == Logger::class.java.canonicalName

        val stackTraceElements = Thread.currentThread().stackTrace
        stackTraceElements.forEachIndexed { index, stackTraceElement ->
            if (isLoggerClass(stackTraceElement)) {
                try {
                    for (searchNextNotLoggerClassIndex in index until stackTraceElements.size) {
                        val callingClass = stackTraceElements[searchNextNotLoggerClassIndex]
                        if (!isLoggerClass(callingClass)) {
                            val split = callingClass.className.split("\\.".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                            var className = split.last()

                            if (className.contains("$")) {
                                className = className.split("\\$".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()[0]
                            }
                            return "($className$KOTLIN_EXTENSION:${callingClass.lineNumber})"
                        }
                    }
                } catch (exception: Exception) {
                    return Logger::class.java.simpleName
                }
            }
        }
        return Logger::class.java.simpleName
    }

    private fun createMessage(message: String, printThread: Boolean = false): String {
        val threadString = if (printThread) " (THREAD: ${Thread.currentThread().name}" else ""
        return "$message --> $threadString)"
    }

    fun error(message: String, printThread: Boolean = true) {
        if (isAllowedToLog) Log.e(getCallingClass(), createMessage(message, printThread))
    }

    fun debug(message: String) {
        if (isAllowedToLog) Log.d(getCallingClass(), createMessage(message))
    }

    fun info(message: String) {
        if (isAllowedToLog) Log.i(getCallingClass(), createMessage(message))
    }

    fun verbose(message: String) {
        if (isAllowedToLog) Log.v(getCallingClass(), createMessage(message))
    }

    fun warn(message: String) {
        if (isAllowedToLog) Log.w(getCallingClass(), createMessage(message))
    }

    fun wtf(message: String) {
        if (isAllowedToLog) Log.wtf(getCallingClass(), createMessage(message))
    }
}
