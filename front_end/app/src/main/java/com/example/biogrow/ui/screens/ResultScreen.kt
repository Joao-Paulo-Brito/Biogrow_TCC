package com.example.biogrow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.biogrow.R
import com.example.biogrow.data.model.SaidaPrevisao

@Composable
fun ResultScreen(
    resultado: SaidaPrevisao,
    onNavigateToForm: () -> Unit
) {
    // Cores do Botão
    val cor1 = Color(0xFFCDCA00)
    val cor2 = Color(0xFF846E00)

    // Cor ajustada para os retornos do backend (ALTO e PADRÃO)
    val corDoImpacto = when (resultado.nivelImpacto.uppercase()) {
        "ALTO" -> Color.Green
        "PADRÃO" -> Color(0xFFFFA500)
        "MÉDIO", "MEDIO" -> Color(0xFFFFA500)
        "BAIXO" -> Color.Red
        else -> Color.Gray
    }

    val tituloImpacto = "IMPACTO ${resultado.nivelImpacto.uppercase()}"

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.rice_2),
                contentDescription = "Rice",
                modifier = Modifier.size(220.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box{
                Text(
                    text = tituloImpacto,
                    color = corDoImpacto,
                    fontWeight = FontWeight.Bold,
                    fontFamily = QuicksandFontFamily,
                    fontSize = 50.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(8f, 8f),
                            blurRadius = 4f
                        )
                    )
                )

                Text(
                    text = tituloImpacto,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = QuicksandFontFamily,
                    fontSize = 50.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        drawStyle = Stroke(width = 4f),
                    ),
                    modifier = Modifier.offset(x = 1.dp, y = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = resultado.recomendacao,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontFamily = FreemanFontFamily,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            val porcentagemConfianca = (resultado.confiancaModelo * 100).toInt()
            Text(
                text = "Confiança do Modelo: $porcentagemConfianca%",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FreemanFontFamily,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onNavigateToForm,
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
                    text = "PRÓXIMA ANÁLISE",
                    color = cor2,
                    fontFamily = FreemanFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}