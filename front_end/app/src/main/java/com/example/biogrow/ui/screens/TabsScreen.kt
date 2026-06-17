package com.example.biogrow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.biogrow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsScreen(
    onNavigateToFormNaoDestrutivo: () -> Unit,
    onNavigateToFormDestrutivo: () -> Unit
) {
    val cor1 = Color(0xFFCDCA00)
    val cor2 = Color(0xFF846E00)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cor1
                ),
                title = {
                    Box(modifier = Modifier.padding(top = 15.dp)) {
                        Text(
                            "TIPOS DE ANÁLISE",
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
                            "TIPOS DE ANÁLISE",
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
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Botão 1: Análise Não Destrutiva
                Button(
                    onClick = onNavigateToFormNaoDestrutivo,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cor1
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "ANÁLISE NÃO DESTRUTIVA", color = cor2,
                            fontFamily = FreemanFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp
                        )
                        Text(
                            "(Biometria Básica)", color = cor2,
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Botão 2: Análise Destrutiva
                Button(
                    onClick = onNavigateToFormDestrutivo,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cor1
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "ANÁLISE DESTRUTIVA", color = cor2,
                            fontFamily = FreemanFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp
                        )
                        Text(
                            "(Biomassa Seca e Raiz)", color = cor2,
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}