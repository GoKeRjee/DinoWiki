package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedDateField
import fr.uha.hassenforder.android.ui.OutlinedSpinnerField
import fr.uha.hassenforder.android.ui.OutlinedSpinnerFieldEnum
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Capacity

@Composable
fun SuccessTeamScreen(
    team: TeamViewModel.TeamUIState,
    uiCB: TeamViewModel.TeamUICallback
) {
    val showDialog = remember { mutableStateOf(false) }

    Column(
    ) {
        OutlinedTextField(
            value = team.name.current ?: "",
            onValueChange = { uiCB.onEvent(TeamViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.teamname)) },
            supportingText = { if (team.name.errorId != null) Text(stringResource(id = team.name.errorId)) },
            isError = team.name.errorId != null,
        )
        OutlinedDateField(
            value = team.startDay.current,
            onValueChange = { uiCB.onEvent(TeamViewModel.UIEvent.StartDayChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = R.string.start_day,
            errorId = team.startDay.errorId
        )
        OutlinedSpinnerFieldEnum(
            value = team.capacity.current,
            onValueChange = { uiCB.onEvent(TeamViewModel.UIEvent.CapacityChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            enumValues = Capacity.values(),
            labelId = R.string.capacity,
            errorId = team.capacity.errorId,
        )
        OutlinedSpinnerField(
            value = team.duration.current.toString(),
            onValueChange = {
                try {
                    val v: Int = it.toInt(); uiCB.onEvent(TeamViewModel.UIEvent.DurationChanged(v))
                } catch (e: Exception) {
                }
            },
            label = R.string.duration,
            option_views = stringArrayResource(id = R.array.durations),
            option_values = stringArrayResource(id = R.array.durations),
            errorId = team.duration.errorId,
        )
        ListMembersField(
            value = team.members.current,
            onAdd = { uiCB.onEvent(TeamViewModel.UIEvent.MemberAdded(it)) },
            onDelete = { uiCB.onEvent(TeamViewModel.UIEvent.MemberDeleted(it)) },
            errorId = team.members.errorId
        )
    }

}
