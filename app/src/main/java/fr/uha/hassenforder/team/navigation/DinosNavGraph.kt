package fr.uha.hassenforder.team.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.ui.dino.CreateDinoScreen
import fr.uha.hassenforder.team.ui.dino.EditDinoScreen
import fr.uha.hassenforder.team.ui.dino.ListDinosScreen
import fr.uha.hassenforder.team.ui.dino.ListDinosViewModel

private sealed class DinoNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all dinos
    object Dinos : DinoNavGraphEntry(
        route = "dinos",
        title = R.string.action_dinos,
    )

    // to create a dino
    object Create : DinoNavGraphEntry(
        route = "dino",
        title = R.string.action_dino_create,
    )

    // to edit a dino
    object Edit : DinoNavGraphEntry(
        route = "dino/{pid}",
        title = R.string.action_dino_edit,
    ) {
        fun to(pid: Long): String {
            return route.replace("{pid}", pid.toString())
        }
    }

}

fun NavGraphBuilder.DinosNavGraph(
    navController: NavHostController
) {
    navigation(DinoNavGraphEntry.Dinos.route, BottomBarNavGraphEntry.Dinos.route) {
        composable(route = DinoNavGraphEntry.Dinos.route) {
            val listDinosViewModel: ListDinosViewModel = hiltViewModel()

            ListDinosScreen(
                onCreate = { navController.navigate(DinoNavGraphEntry.Create.route) },
                onEdit = { p: Dino -> navController.navigate(DinoNavGraphEntry.Edit.to(p.pid)) },
                onDelete = { dino -> listDinosViewModel.delete(dino) }
            )
        }
        composable(route = DinoNavGraphEntry.Create.route) {
            CreateDinoScreen(back = { navController.popBackStack() })
        }
        composable(
            route = DinoNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("pid") { type = NavType.LongType })
        ) { backStackEntry ->
            EditDinoScreen(
                pid = backStackEntry.arguments?.getLong("pid")!!,
                back = { navController.popBackStack() })
        }
    }
}
