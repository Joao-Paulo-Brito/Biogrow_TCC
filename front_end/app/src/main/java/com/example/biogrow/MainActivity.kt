package com.example.biogrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biogrow.ui.theme.BIOGROWTheme
import com.example.biogrow.ui.screens.TabsScreen
import com.example.biogrow.ui.screens.FormScreen
import com.example.biogrow.ui.screens.FormScreenDestrutivo
import com.example.biogrow.ui.screens.HomeScreen
import com.example.biogrow.ui.screens.ResultScreen
import com.example.biogrow.ui.viewModel.PrevisaoUiState
import com.example.biogrow.ui.viewModel.PrevisaoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BIOGROWTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: PrevisaoViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "home") {

        // Rota: Home
        composable("home") {
            HomeScreen(
                onNavigateToForm = {
                    navController.navigate("tabs")
                }
            )
        }

        // Rota: Tela de Abas (Tipos de Análise)
        composable("tabs") {
            TabsScreen(
                onNavigateToFormNaoDestrutivo = {
                    navController.navigate("form_nao_destrutivo")
                },
                onNavigateToFormDestrutivo = {
                    navController.navigate("form_destrutivo")
                }
            )
        }

        // Rota: Formulário Não Destrutivo
        composable("form_nao_destrutivo") {
            FormScreen(
                onNavigateToResult = {
                    navController.navigate("result")
                },
                onBuscarPrevisao = { organismo, cidade, cultivo, pa_cm, diametro_coleto_mm ->
                    viewModel.buscarPrevisaoNaoDestrutiva(organismo, cidade, cultivo, pa_cm, diametro_coleto_mm)
                },

                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ROTA: Formulário Destrutivo
        composable("form_destrutivo") {
            FormScreenDestrutivo(
                onNavigateToResult = {
                    navController.navigate("result")
                },
                onBuscarPrevisaoDestrutiva = { organismo, cidade, cultivo, pa_cm, diametro_coleto_mm, pst_mg, psr_mg, psa_mg, comprimento_raiz_cm ->
                    viewModel.buscarPrevisaoDestrutiva(
                        organismo, cidade, cultivo, pa_cm, diametro_coleto_mm,
                        pst_mg, psr_mg, psa_mg, comprimento_raiz_cm
                    )
                },

                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Rota: Resultado (Compartilhada por ambas as análises)
        composable("result") {
            when (uiState) {
                is PrevisaoUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color(0xFFCDCA00))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Analisando dados biológicos...",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF846E00)
                            )
                        }
                    }
                }

                is PrevisaoUiState.Sucesso -> {
                    val dadosDoResultado = (uiState as PrevisaoUiState.Sucesso).resultado

                    ResultScreen(
                        resultado = dadosDoResultado,
                        onNavigateToForm = {
                            viewModel.resetarTela()
                            navController.popBackStack("tabs", inclusive = false)
                        }
                    )
                }

                is PrevisaoUiState.Erro -> {
                    val mensagemErro = (uiState as PrevisaoUiState.Erro).mensagem
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = mensagemErro, color = Color.Red, fontSize = 18.sp)
                    }
                }

                else -> {}
            }
        }
    }
}