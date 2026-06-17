package com.example.biogrow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.biogrow.R

// Fontes de Texto
val Quicksand_Bold = Font(R.font.quicksand_bold, FontWeight.Bold)
val Quicksand_Regular = Font(R.font.quicksand_regular, FontWeight.Normal)
val Freeman_Regular = Font(R.font.freeman_regular, weight = FontWeight.Normal)
val FreemanFontFamily = FontFamily(Freeman_Regular)
val QuicksandFontFamily = FontFamily(Quicksand_Regular, Quicksand_Bold)

@Composable
fun HomeScreen(
    onNavigateToForm: () -> Unit
) {
    // Cores
    val cor1 = Color(0xFFCDCA00)
    val cor2 = Color(0xFF846E00)

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column (
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(140.dp))

            // Título Biogrow
            Box{

                Text(
                    text = "BIOGROW",
                    fontFamily = QuicksandFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center,
                    color = cor1,
                    style = TextStyle(
                        shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(8f, 8f),
                                blurRadius = 4f
                        )
                    )
                )

                Text(
                    text = "BIOGROW",
                    fontFamily = QuicksandFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        drawStyle = Stroke(width = 4f),
                    ),
                    color = Color.Black, // cor do contorno
                    modifier = Modifier.offset(x = 1.dp, y = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Aplicativo preditivo para análise\n" +
                        "de impacto de microrganismos \n" +
                        "(como rizobactérias)\n" +
                        "no crescimento do arroz via\n" +
                        "Machine Learning.",
                fontFamily = QuicksandFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.3.sp,
                color = Color(0xFF2C2C2C),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.15f),
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )
                )
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Botão
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
                        "INICIAR", color = cor2,
                        fontFamily = FreemanFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp
                    )
                }

        }
    }

}
