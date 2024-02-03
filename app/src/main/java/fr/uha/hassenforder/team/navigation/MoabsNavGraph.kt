package fr.uha.hassenforder.team.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.ui.moab.CreateMoabScreen
import fr.uha.hassenforder.team.ui.moab.EditMoabScreen
import fr.uha.hassenforder.team.ui.moab.ListMoabsScreen

private sealed class MoabNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all moabs
    object Moab : MoabNavGraphEntry(
        route = "moabs",
        title = R.string.action_moabs,
    )

    // to create a moab
    object Create : MoabNavGraphEntry(
        route = "moab",
        title = R.string.action_moab_create,
    )

    // to edit a moab
    object Edit : MoabNavGraphEntry(
        route = "moab/{mid}",
        title = R.string.action_moab_edit,
    ) {
        fun to(mid: Long): String {
            return route.replace("{mid}", mid.toString())
        }
    }

}

fun NavGraphBuilder.moabsNavGraph(
    navController: NavHostController
) {
    navigation(MoabNavGraphEntry.Moab.route, BottomBarNavGraphEntry.Moabs.route) {
        composable(route = MoabNavGraphEntry.Moab.route) {
            ListMoabsScreen(
                onCreate = { navController.navigate(MoabNavGraphEntry.Create.route) },
                onEdit = { m: Moab -> navController.navigate(MoabNavGraphEntry.Edit.to(m.mid)) }
            )
        }
        composable(route = MoabNavGraphEntry.Create.route) {
            CreateMoabScreen(back = { navController.popBackStack() })
        }
        composable(
            route = MoabNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("mid") { type = NavType.LongType })
        ) { backStackEntry ->
            EditMoabScreen(
                mid = backStackEntry.arguments?.getLong("mid")!!,
                back = { navController.popBackStack() })
        }
    }
}

