package com.muthia0027.mobpro1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.muthia0027.mobpro1.R
import com.muthia0027.mobpro1.model.Financial
import com.muthia0027.mobpro1.navigation.Screen
import com.muthia0027.mobpro1.ui.theme.Mobpro1Theme
import com.muthia0027.mobpro1.util.SettingsDataStore
import com.muthia0027.mobpro1.util.ViewModelFactory
import kotlinx.coroutines.*
import java.util.*

data class ThemeColorOption(
    val name: String,
    val color: Color
)

val pastelColors = listOf(
    ThemeColorOption("Default Blue", Color(0xFF4A90E2)),
    ThemeColorOption("Pastel Pink", Color(0xFFF8BBD0)),
    ThemeColorOption("Pastel Purple", Color(0xFFD1C4E9)),
    ThemeColorOption("Pastel Green", Color(0xFFC8E6C9)),
    ThemeColorOption("Pastel Blue Soft", Color(0xFFBBDEFB))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current
    val dataStore = SettingsDataStore(context)
    val showList by dataStore.layoutFlow.collectAsState(true)

    var selectedDate by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var selectedColor by remember {
        mutableStateOf(pastelColors.first())
    }

    var expandedColorMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF5F7FA),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedDate == null)
                            stringResource(R.string.app_name)
                        else
                            stringResource(R.string.tanggal_filter, selectedDate ?: ""),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = selectedColor.color
                ),
                actions = {

                    ExposedDropdownMenuBox(
                        expanded = expandedColorMenu,
                        onExpandedChange = { expandedColorMenu = it }
                    ) {
                        IconButton(onClick = { expandedColorMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = expandedColorMenu,
                            onDismissRequest = { expandedColorMenu = false },
                            modifier = Modifier.width(200.dp)
                        ) {
                            pastelColors.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            item.name,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    },
                                    onClick = {
                                        selectedColor = item
                                        expandedColorMenu = false
                                    }
                                )
                            }
                        }
                    }

                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, null, tint = Color.White)
                    }

                    IconButton(onClick = { selectedDate = null }) {
                        Icon(Icons.Default.Clear, null, tint = Color.White)
                    }

                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            imageVector = if (showList)
                                Icons.Default.GridView
                            else
                                Icons.Default.ViewList,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                containerColor = selectedColor.color,
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    ) { innerPadding ->

        ScreenContent(
            showList,
            Modifier.padding(innerPadding),
            navController,
            selectedDate,
            selectedColor.color
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val cal = Calendar.getInstance()
                            cal.timeInMillis = millis

                            selectedDate = "%04d-%02d-%02d".format(
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH) + 1,
                                cal.get(Calendar.DAY_OF_MONTH)
                            )
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(text = stringResource(R.string.btn_batal))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun ScreenContent(
    showList: Boolean,
    modifier: Modifier,
    navController: NavHostController,
    selectedDate: String?,
    themeColor: Color
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)

    val allData by viewModel.data.collectAsState()

    val data = if (selectedDate == null) {
        allData
    } else {
        allData.filter { it.date == selectedDate }
    }

    val total = data.sumOf { it.amount }

    if (data.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.empty_data))
        }
    } else {

        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {

                item {
                    TotalCard(total, themeColor)
                }

                items(data) {
                    ListItem(it, themeColor) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {

                item(span = StaggeredGridItemSpan.FullLine) {
                    TotalCard(total, themeColor)
                }

                items(data) {
                    GridItem(it, themeColor) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
    }
}

@Composable
fun TotalCard(total: Int, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.pengeluaran_total), color = Color.White)
            Text(
                text = "Rp $total",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ListItem(financial: Financial, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(financial.title, fontWeight = FontWeight.Bold)
            Text("Rp ${financial.amount}", color = color)
            Text(financial.category, color = Color.Gray)
            Text(financial.date, color = Color.Gray)
        }
    }
}

@Composable
fun GridItem(financial: Financial, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(financial.title, fontWeight = FontWeight.Bold)
            Text("Rp ${financial.amount}", color = color)
            Text(financial.category, color = Color.Gray)
            Text(financial.date, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMain() {
    Mobpro1Theme {
        MainScreen(rememberNavController())
    }
}