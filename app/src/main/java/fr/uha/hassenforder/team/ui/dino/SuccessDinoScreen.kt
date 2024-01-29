package fr.uha.hassenforder.team.ui.dino

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedEnumRadioGroup
import fr.uha.hassenforder.android.ui.PictureField
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.TeamFileProvider
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type

@Composable
fun SuccessDinoScreen(
    dino: DinoViewModel.DinoUIState,
    uiCB: DinoViewModel.DinoUICallback
) {
    val context = LocalContext.current

    Column(
    ) {
        OutlinedTextField(
            value = dino.nameState.current ?: "",
            onValueChange = { uiCB.onEvent(DinoViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.name)) },
            supportingText = { if (dino.nameState.errorId != null) Text(stringResource(id = dino.nameState.errorId)) },
            isError = dino.nameState.errorId != null,

            )
        OutlinedEnumRadioGroup(
            value = dino.genderState.current,
            onValueChange = { uiCB.onEvent(DinoViewModel.UIEvent.GenderChanged(Gender.valueOf(it))) },
            modifier = Modifier.fillMaxWidth(),
            items = Gender.values(),
            labelId = R.string.gender,
            errorId = dino.genderState.errorId,
        )
        OutlinedEnumRadioGroup(
            value = dino.typeState.current,
            onValueChange = { uiCB.onEvent(DinoViewModel.UIEvent.TypeChanged(Type.valueOf(it))) },
            modifier = Modifier.fillMaxWidth(),
            items = Type.values(),
            labelId = R.string.type,
            errorId = dino.typeState.errorId,
        )
        OutlinedEnumRadioGroup(
            value = dino.regimeState.current,
            onValueChange = { uiCB.onEvent(DinoViewModel.UIEvent.RegimeChanged(Regime.valueOf(it))) },
            modifier = Modifier.fillMaxWidth(),
            items = Regime.values(),
            labelId = R.string.regime,
            errorId = dino.regimeState.errorId,
        )
        OutlinedEnumRadioGroup(
            value = dino.apprivoiserState.current,
            onValueChange = {
                uiCB.onEvent(
                    DinoViewModel.UIEvent.ApprivoiserChanged(
                        Apprivoiser.valueOf(
                            it
                        )
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            items = Apprivoiser.values(),
            labelId = R.string.apprivoiser,
            errorId = dino.apprivoiserState.errorId,
        )
        PictureField(
            value = dino.pictureState.current,
            onValueChange = { uiCB.onEvent(DinoViewModel.UIEvent.PictureChanged(it)) },
            newImageUriProvider = { TeamFileProvider.getImageUri(context) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.picture,
            errorId = dino.pictureState.errorId
        )

    }
}