
package com.example.uuuhtt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uuuhtt.ui.theme.UuuhttTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UuuhttTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "menuPrincipal",
        modifier = modifier
    ) {
        composable("menuPrincipal") {
            MenuPrincipal(navController = navController)
        }
        composable("calculadoraConsumoAgua") {
            CalculadoraConsumoAgua(navController = navController)
        }
        composable("registroActividadFisica") {
            RegistroActividadFisica(navController = navController)
        }
        composable("catalogoAutos") {
            CatalogoAutos(navController = navController)
        }
    }
}

@Composable
fun MenuPrincipal(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Menú Principal", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController.navigate("calculadoraConsumoAgua") }) {
            Text(text = "Calculadora de consumo de agua")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("registroActividadFisica") }) {
            Text(text = "Registro de actividad física")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("catalogoAutos") }) {
            Text(text = "Catálogo de autos deportivos")
        }
    }
}
