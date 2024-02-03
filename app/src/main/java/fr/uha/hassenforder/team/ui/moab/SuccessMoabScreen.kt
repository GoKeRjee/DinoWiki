package fr.uha.hassenforder.team.ui.moab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedDateField
import fr.uha.hassenforder.android.ui.OutlinedSpinnerFieldEnum
import fr.uha.hassenforder.android.ui.PictureField
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.TeamFileProvider
import fr.uha.hassenforder.team.model.Community

@Composable
fun SuccessMoabScreen(
    moab: MoabViewModel.MoabUIState,
    uiCB: MoabViewModel.MoabUICallback
) {
    val context = LocalContext.current

    Column {
        OutlinedTextField(
            value = moab.name.current ?: "",
            onValueChange = { uiCB.onEvent(MoabViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.moabname)) },
            supportingText = { if (moab.name.errorId != null) Text(stringResource(id = moab.name.errorId)) },
            isError = moab.name.errorId != null,
        )
        PictureField(
            value = moab.pictureState.current,
            onValueChange = { uiCB.onEvent(MoabViewModel.UIEvent.PictureChanged(it)) },
            newImageUriProvider = { TeamFileProvider.getImageUri(context) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.picture,
            errorId = moab.pictureState.errorId
        )
        OutlinedDateField(
            value = moab.startDay.current,
            onValueChange = { uiCB.onEvent(MoabViewModel.UIEvent.StartDayChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = R.string.start_day,
            errorId = moab.startDay.errorId
        )
        OutlinedSpinnerFieldEnum(
            value = moab.community.current,
            onValueChange = { uiCB.onEvent(MoabViewModel.UIEvent.CommunityChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            enumValues = Community.values(),
            labelId = R.string.community,
            errorId = moab.community.errorId,
        )
        ListMembersField(
            value = moab.members.current,
            onAdd = { uiCB.onEvent(MoabViewModel.UIEvent.MemberAdded(it)) },
            onDelete = { uiCB.onEvent(MoabViewModel.UIEvent.MemberDeleted(it)) },
            errorId = moab.members.errorId
        )
    }
}
