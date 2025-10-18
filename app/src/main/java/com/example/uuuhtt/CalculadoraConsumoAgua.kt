
package com.example.uuuhtt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraConsumoAgua(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nombre by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    val generos = listOf("Masculino", "Femenino", "Sin especificar")
    var generoSeleccionado by rememberSaveable { mutableStateOf(generos[0]) }
    var resultado by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Calculadora de Agua") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
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
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                label = { Text("Peso corporal (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de Género
            Column(Modifier.fillMaxWidth()) {
                Text("Género:", style = MaterialTheme.typography.bodyLarge)
                Row {
                    generos.forEach { genero ->
                        Row(
                            Modifier
                                .selectable(
                                    selected = (genero == generoSeleccionado),
                                    onClick = { generoSeleccionado = genero },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (genero == generoSeleccionado),
                                onClick = null
                            )
                            Text(text = genero, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            }


            Button(
                onClick = {
                    val pesoDouble = peso.toDoubleOrNull()
                    when {
                        nombre.isBlank() || peso.isBlank() -> {
                            scope.launch { snackbarHostState.showSnackbar("Por favor, completa todos los campos.") }
                        }
                        pesoDouble == null || pesoDouble <= 5 || pesoDouble > 200 -> {
                            scope.launch { snackbarHostState.showSnackbar("El peso debe ser un número válido entre 5 y 200 kg.") }
                        }
                        else -> {
                            val factorGenero = when (generoSeleccionado) {
                                "Masculino" -> 1.02
                                "Femenino" -> 1.01
                                else -> 1.00
                            }
                            val litros = pesoDouble * 0.035 * factorGenero
                            resultado = "$nombre debe beber aproximadamente %.2f litros de agua al día.".format(litros)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular consumo de agua")
            }

            if (resultado.isNotEmpty()) {
                Text(
                    text = resultado,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
