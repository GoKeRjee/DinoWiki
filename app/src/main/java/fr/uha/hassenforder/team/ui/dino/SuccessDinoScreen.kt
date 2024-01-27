package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedEnumRadioGroup
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type

@Composable
fun SuccessDinoScreen(
    dino: Dino
) {
    Column {
        OutlinedTextField(
            value = dino.name,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.name)) }
        )

        OutlinedEnumRadioGroup(
            value = dino.gender,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            items = Gender.values(),
            labelId = R.string.gender
        )
        OutlinedEnumRadioGroup(
            value = dino.type,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            items = Type.values(),
            labelId = R.string.type
        )
        OutlinedEnumRadioGroup(
            value = dino.regime,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            items = Regime.values(),
            labelId = R.string.regime
        )
        OutlinedEnumRadioGroup(
            value = dino.apprivoiser,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            items = Apprivoiser.values(),
            labelId = R.string.apprivoiser
        )
        /*
        Image(
            painter = rememberAsyncImagePainter(dino.picture),
            contentDescription = stringResource(R.string.picture)
        )
         */
    }
}
