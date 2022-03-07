@file:OptIn(ExperimentalCoroutinesApi::class)

package de.bornholdtlee.defaultprojectkotlin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.bornholdtlee.defaultprojectkotlin.core.android.app_startup.KoinInitializer
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import java.lang.reflect.Field
import java.lang.reflect.Modifier

open class BaseTest : KoinTest {

    // THIS TEST RULE IS NEEDED FOR RUNNING COROUTINE TESTS
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // THIS TEST RULE CREATES MOCKS FROM MOCKK FOR EVERY INJECT WHICH IS DECLARED BY declareMock FROM KOIN
    @get:Rule
    val mockProvider = MockProviderRule.create { mockkClass(it) }

    @MockK(relaxed = true)
    lateinit var mockedAndroidContext: BaseApplication

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        stopKoin()
        startKoin {
            androidContext(mockedAndroidContext)
            modules(KoinInitializer.modules)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // setStaticFieldViaReflection(Class::class.java.getField("<STATIC_FIELD>"), value)
    protected fun setStaticFieldViaReflection(field: Field, value: Any) {
        field.isAccessible = true
        Field::class.java.getDeclaredField("modifiers").apply {
            isAccessible = true
            setInt(field, field.modifiers and Modifier.FINAL.inv())
        }
        field.set(null, value)
    }
}
