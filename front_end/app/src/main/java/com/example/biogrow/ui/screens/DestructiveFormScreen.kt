package com.example.biogrow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import com.example.biogrow.R

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreenDestrutivo(
    onNavigateToResult: () -> Unit,
    onNavigateBack: () -> Unit,
    onBuscarPrevisaoDestrutiva: (String, String, String, Double, Double, Double, Double, Double, Double) -> Unit
) {
    val cor1 = Color(0xFFCDCA00)
    val cor2 = Color(0xFF846E00)

    var organismo by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var cultivo by remember { mutableStateOf("") }
    var paCm by remember { mutableStateOf("") }
    var diametroColetoMm by remember { mutableStateOf("") }

    var pstMg by remember { mutableStateOf("") }
    var psrMg by remember { mutableStateOf("") }
    var psaMg by remember { mutableStateOf("") }
    var comprimentoRaizCm by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cor1
                ),
                title = {
                    Box(modifier = Modifier.padding(top = 15.dp)) {
                        Text(
                            "INSIRA OS DADOS",
                            color = cor2,
                            fontWeight = FontWeight.Bold,
                            fontFamily = QuicksandFontFamily,
                            fontSize = 40.sp,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(8f, 8f),
                                    blurRadius = 4f
                                )
                            )
                        )
                        Text(
                            "INSIRA OS DADOS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = QuicksandFontFamily,
                            fontSize = 40.sp,
                            style = TextStyle(drawStyle = Stroke(width = 4f)),
                            modifier = Modifier.offset(x = 1.dp, y = 1.dp)
                        )
                    }
                },
                modifier = Modifier.height(120.dp)
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(130.dp))

                CustomTextField(
                    value = organismo,
                    onValueChange = { organismo = it },
                    label = "Microrganismo:"
                )

                CustomTextField(
                    value = cidade,
                    onValueChange = { cidade = it },
                    label = "Cidade (Localização):"
                )

                CustomTextField(
                    value = cultivo,
                    onValueChange = { cultivo = it },
                    label = "Cultivo (Tipo de Arroz):"
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomTextField(
                        value = paCm,
                        onValueChange = { paCm = it },
                        label = "PA (cm):",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )

                    CustomTextField(
                        value = diametroColetoMm,
                        onValueChange = { diametroColetoMm = it },
                        label = "Coleto (mm):",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomTextField(
                        value = pstMg,
                        onValueChange = { pstMg = it },
                        label = "PST (mg):",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )

                    CustomTextField(
                        value = comprimentoRaizCm,
                        onValueChange = { comprimentoRaizCm = it },
                        label = "Comp. Raiz (cm):",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomTextField(
                        value = psrMg,
                        onValueChange = { psrMg = it },
                        label = "PSR (mg):",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )

                    CustomTextField(
                        value = psaMg,
                        onValueChange = { psaMg = it },
                        label = "PSA (mg):",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Botão Enviar Dados
                Button(
                    onClick = {
                        val paDouble = paCm.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val diametroDouble = diametroColetoMm.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val pstDouble = pstMg.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val psrDouble = psrMg.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val psaDouble = psaMg.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val compRaizDouble = comprimentoRaizCm.replace(",", ".").toDoubleOrNull() ?: 0.0

                        onBuscarPrevisaoDestrutiva(
                            organismo, cidade, cultivo, paDouble, diametroDouble,
                            pstDouble, psrDouble, psaDouble, compRaizDouble
                        )
                        onNavigateToResult()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cor1
                    ),
                    modifier = Modifier
                        .width(300.dp)
                        .height(70.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(
                        "INSERIR DADOS", color = cor2,
                        fontFamily = FreemanFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão Voltar
                Button(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cor1
                    ),
                    modifier = Modifier
                        .width(300.dp)
                        .height(70.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(
                        "VOLTAR", color = cor2,
                        fontFamily = FreemanFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}