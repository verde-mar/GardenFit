package it.unipi.gardenfit.screen.plantlist

//NON SO SE MI SERVE
/*
@Composable
fun PlantList(
    @StringRes title: Int,
    plants: List<Plant>,
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    Card(modifier = modifier, backgroundColor = MaterialTheme.colors.primary) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            PlantRow(plants = plants, navigateTo = navigateTo)
        }
    }
}

@Composable
fun PlantRow(
    plants: List<Plant>,
    navigateTo: (String) -> Unit
) {
    val lastIndex = plants.size - 1
    LazyRow(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        itemsIndexed(items = plants) { index: Int, item: Plant ->
            PlantItem(plant = item, navigateTo = navigateTo)
            if (index < lastIndex) Spacer(Modifier.width(16.dp))
        }
    }
}


@Composable
fun PlantItem (
    plant: Plant,
    navigateTo: (String) -> Unit
    ) {
        Surface(shape = RoundedCornerShape(10.dp)) {
            Card(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .clickable(onClick = {
                        plant.plantId
                            ?.let { Screen.Plant.route(it) }
                            ?.let { navigateTo(it) }

                    })
            ) {
                Column(
                    Modifier
                        .padding(8.dp)
                        .wrapContentSize(Alignment.Center)


                ) {
                    plant.name?.let {
                        Text(
                            text = plant.name,
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = Color.Black
                        )
                    }
                }
            }
        }
}
*/