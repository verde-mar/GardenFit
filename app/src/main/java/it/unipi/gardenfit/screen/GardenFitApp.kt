package it.unipi.gardenfit.screen

/*
@RequiresApi(Build.VERSION_CODES.N)
@Composable
@ExperimentalMaterialApi
@Preview
fun GardenFitApp() {
    it.unipi.gardenfit.ui.theme.GardenFitTheme {
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    HomeScreen(viewModel = it.hiltNavGraphViewModel(), modifier = Modifier)
                }
                composable(Screen.Plant.route) {
                    val plantId = it.arguments?.getString(Screen.Plant.ARG_PLANT_ID).orEmpty()
                    PlantScreen(
                        viewModel = it.hiltNavGraphViewModel(),
                        plantId = plantId,
                        onBackPressed = { navController.navigateUp() }
                    )
                }
                composable(Screen.Zone.route) {
                    val zoneId = it.arguments?.getString(Screen.Zone.ARG_ZONE_ID).orEmpty()
                    PlantListScreen(
                        viewModel = it.hiltNavGraphViewModel(),
                        zoneId = zoneId,
                        onBackPressed = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}*/