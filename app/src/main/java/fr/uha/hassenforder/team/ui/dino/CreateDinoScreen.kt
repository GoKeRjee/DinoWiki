package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.database.TeamDatabase
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type
import fr.uha.hassenforder.team.repository.DinoRepository
import fr.uha.hassenforder.team.ui.dino.DinoViewModel


@Composable
fun CreateDinoScreen(
    vm: DinoViewModel = DinoViewModel(DinoRepository(TeamDatabase.get().dinoDAO))
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

    Scaffold {
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
