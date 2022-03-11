package de.digitaldealer.cardsplease.ui.main

import androidx.lifecycle.ViewModel
import de.digitaldealer.cardsplease.data.database.KeyValueStore
import de.digitaldealer.cardsplease.domain.usecases.RefreshQuestionsFromApiUseCase
import de.digitaldealer.cardsplease.domain.usecases.WatchQuestionsFromDbUseCase
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val refreshQuestionsFromApiUseCase by inject<RefreshQuestionsFromApiUseCase>()
    private val watchQuestionsFromDbUseCase by inject<WatchQuestionsFromDbUseCase>()
    private val keyValueStore by inject<KeyValueStore>()

    private val _downloadError = MutableSharedFlow<Int>()
    val downloadErrorStringRes = _downloadError.asSharedFlow()

    private val _downloadSuccess = MutableSharedFlow<Any>()
    val downloadSuccess = _downloadSuccess.asSharedFlow()

    private val _counter = MutableStateFlow(0)
    val counter = _counter.asStateFlow()

//    val questionEntities: Flow<List<QuestionEntity>> = watchQuestionsFromDbUseCase.call()

    init {
        readPreferences()
    }

    fun onIncrementCounter() {
        _counter.value = _counter.value.plus(1)
    }

    private fun readPreferences() {
//        launch {
//            keyValueStore.watchExampleStringSet().collectLatest { exampleStringSet ->
//                Logger.error("preferences: ${exampleStringSet.firstOrNull() ?: ""}")
//            }
//        }
    }

    fun makeApiCall() {
//        launch {
//            when (refreshQuestionsFromApiUseCase.call()) {
//                is Success -> _downloadSuccess.emit(Any())
//                is Failure -> _downloadError.emit(R.string.error_load_questions)
//            }
//        }
    }
}
