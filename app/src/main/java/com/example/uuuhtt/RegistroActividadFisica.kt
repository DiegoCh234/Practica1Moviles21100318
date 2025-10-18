package com.example.uuuhtt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroActividadFisica(navController: NavController) {
    val activities = listOf("Correr", "Caminar", "Nadar", "Ciclismo", "Yoga")
    val intensities = listOf("Baja", "Media", "Alta")

    var selectedActivity by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var selectedIntensity by rememberSaveable { mutableStateOf("") }
    var caloriesResult by rememberSaveable { mutableStateOf("") }

    var isActivityDropdownExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Actividad") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Activity Dropdown
            ExposedDropdownMenuBox(
                expanded = isActivityDropdownExpanded,
                onExpandedChange = { isActivityDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedActivity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Actividad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isActivityDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isActivityDropdownExpanded,
                    onDismissRequest = { isActivityDropdownExpanded = false }
                ) {
                    activities.forEach { activity ->
                        DropdownMenuItem(
                            text = { Text(activity) },
                            onClick = {
                                selectedActivity = activity
                                isActivityDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Duration TextField
            OutlinedTextField(
                value = duration,
                onValueChange = { if (it.all { char -> char.isDigit() }) duration = it },
                label = { Text("Duración (minutos)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Intensity RadioButtons
            Column(Modifier.fillMaxWidth()) {
                Text("Intensidad", style = MaterialTheme.typography.bodyLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    intensities.forEach { intensity ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (selectedIntensity == intensity),
                                onClick = { selectedIntensity = intensity }
                            )
                            Text(text = intensity)
                        }
                    }
                }
            }


            // Calculate Button
            Button(
                onClick = {
                    val durationInt = duration.toIntOrNull()
                    when {
                        selectedActivity.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Por favor, selecciona una actividad.") }
                        }
                        durationInt == null || durationInt <= 0 -> {
                            scope.launch { snackbarHostState.showSnackbar("Por favor, introduce una duración válida.") }
                        }
                        selectedIntensity.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Por favor, selecciona una intensidad.") }
                        }
                        else -> {
                            val caloriesPerMinute = when (selectedActivity) {
                                "Correr" -> 10
                                "Caminar" -> 5
                                "Nadar" -> 8
                                "Ciclismo" -> 7
                                "Yoga" -> 4
                                else -> 0
                            }
                            val intensityFactor = when (selectedIntensity) {
                                "Baja" -> 0.8
                                "Media" -> 1.0
                                "Alta" -> 1.2
                                else -> 1.0
                            }
                            val totalCalories = caloriesPerMinute * durationInt * intensityFactor
                            caloriesResult = "Has quemado aproximadamente %.1f calorías.".format(totalCalories)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular Calorías Quemadas")
            }

            // Result Text
            if (caloriesResult.isNotEmpty()) {
                Text(
                    text = caloriesResult,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
