package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AirportShuttle
import androidx.compose.material.icons.outlined.ConnectingAirports
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Capacity
import fr.uha.hassenforder.team.model.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTeamsScreen(
    vm: ListTeamsViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (p: Team) -> Unit,
) {
    val teams = vm.teams.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = { vm.feed() }),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = { vm.clean() })
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.team_list)
                },
                actions = { AppMenu(entries = menuEntries) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(
                items = teams.value,
                key = { team -> team.tid }
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item) },
                    onDelete = { vm.delete(item) },
                ) {
                    teamItem(item)
                }
            }
        }
    }

}

@Composable
fun teamItem(team: Team) {

    val capacityIcon = when (team.capacity) {
        Capacity.DEFEND -> Icons.Outlined.Shield
        Capacity.ATTACK -> Icons.Outlined.FitnessCenter
        Capacity.TRANSPORT -> Icons.Outlined.AirportShuttle
        Capacity.TRAVEL -> Icons.Outlined.ConnectingAirports
        Capacity.FARM -> Icons.Outlined.ElectricBolt
    }
    ListItem(
        headlineContent = {
            Text(
                text = team.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        supportingContent = {
            Column {
                Row {
                    Icon(imageVector = Icons.Outlined.Start, contentDescription = null)
                    Text(
                        text = UIConverter.convert(team.startDay),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                Text(
                    text = "${team.duration} days",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        },
        leadingContent = {
            Icon(
                imageVector = capacityIcon,
                contentDescription = "Capacity",
                modifier = Modifier.size(48.dp)
            )
        }
    )
}
