package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type

@Composable
fun CreateDinoScreen() {
    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            SuccessDinoScreen(
                Dino(
                    0,
                    "ALBINOSAURE",
                    Gender.NO,
                    Type.AERIEN,
                    Regime.CARNIVORE,
                    Apprivoiser.INCONNU
                )
            )
        }
    }
}
