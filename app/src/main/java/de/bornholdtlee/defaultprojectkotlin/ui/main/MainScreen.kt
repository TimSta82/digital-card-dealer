package de.bornholdtlee.defaultprojectkotlin.ui.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import de.bornholdtlee.defaultprojectkotlin.core.utils.Logger
import de.bornholdtlee.defaultprojectkotlin.data.database.model.QuestionEntity
import de.bornholdtlee.defaultprojectkotlin.ui.extensions.collectAsStateLifecycleAware
import de.bornholdtlee.defaultprojectkotlin.ui.main.composables.TriggerButton
import de.bornholdtlee.defaultprojectkotlin.ui.theme.two_GU
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel, scaffoldState: ScaffoldState) {

    val context = LocalContext.current
    val counter: Int by viewModel.counter.collectAsStateLifecycleAware(0)
    val questions by viewModel.questionEntities.collectAsStateLifecycleAware(emptyList())

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.downloadErrorStringRes.collectLatest { stringRes ->
                scaffoldState.snackbarHostState.showSnackbar(context.resources.getString(stringRes))
            }
        }
        launch {
            viewModel.downloadSuccess.collectLatest {
                scaffoldState.snackbarHostState.showSnackbar("Erfolg")
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            Logger.error("Wuhu success, could process the bitmap now")
        } else {
            Logger.error("Error while taking picture")
        }
    }

    MainContent(
        counter = counter,
        questions = questions,
        incrementCounter = viewModel::onIncrementCounter,
        takePhoto = { launcher.launch() },
        loadQuestions = viewModel::makeApiCall,
    )
}

@Composable
private fun MainContent(
    counter: Int,
    questions: List<QuestionEntity>,
    incrementCounter: () -> Unit,
    takePhoto: () -> Unit,
    loadQuestions: () -> Unit,
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val modifier = Modifier.padding(two_GU)

        item {
            Text(modifier = modifier, text = counter.toString())

            TriggerButton(modifier = modifier, text = "Counter", onClick = incrementCounter)
            TriggerButton(modifier = modifier, text = "Foto aufnehmen", onClick = takePhoto)
            TriggerButton(modifier = modifier, text = "Fragen laden", onClick = loadQuestions)

            Text(modifier = modifier, text = questions.toString())
        }
    }
}

@Composable
@Preview
private fun Preview_MainContent() {
    MainContent(
        counter = 0,
        questions = emptyList(),
        incrementCounter = {},
        takePhoto = {},
        loadQuestions = {},
    )
}
