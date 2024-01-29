package fr.uha.hassenforder.team.ui.dino

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDinoScreen(
    vm: DinoViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = vm.isLaunched) {
        if (!vm.isLaunched) {
            val dino = Dino(
                0,
                "ALBINOSAURE",
                Gender.NO,
                Type.AERIEN,
                Regime.CARNIVORE,
                Apprivoiser.INCONNU
            )
            vm.create(dino)
            vm.isLaunched = true
        }
    }

    val menuEntries = listOf(
        AppMenuEntry.ActionEntry(
            title = R.string.save,
            icon = Icons.Filled.Save,
            enabled = state.isSavable(),
            listener = { vm.save() }
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(
                        appNameId = R.string.app_name,
                        pageTitleId = R.string.dino_create,
                        isModified = state.isModified()
                    )
                },
                actions = { AppMenu(entries = menuEntries) }
            )
        }
    )
    {
        Column(
            modifier = Modifier.padding(it)
        ) {
            when (state.initialState) {
                DinoViewModel.DinoState.Loading ->
                    LoadingScreen(text = stringResource(id = R.string.loading))

                DinoViewModel.DinoState.Error ->
                    ErrorScreen(text = stringResource(id = R.string.error))

                is DinoViewModel.DinoState.Success ->
                    SuccessDinoScreen(state, vm.uiCallback)

            }
        }
    }
}
