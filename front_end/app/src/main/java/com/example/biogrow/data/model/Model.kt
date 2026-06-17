package com.example.biogrow.data.model

import com.google.gson.annotations.SerializedName

// Modelo para a Análise Não Destrutiva
data class EntradaNaoDestrutiva(
    @SerializedName("organismo") val organismo: String,
    @SerializedName("cidade") val cidade: String,
    @SerializedName("cultivo") val cultivo: String,
    @SerializedName("pa_cm") val paCm: Double,
    @SerializedName("diametro_coleto_mm") val diametroColetoMm: Double
)

// Modelo para a Análise Destrutiva
data class EntradaDestrutiva(
    @SerializedName("organismo") val organismo: String,
    @SerializedName("cidade") val cidade: String,
    @SerializedName("cultivo") val cultivo: String,
    @SerializedName("pa_cm") val paCm: Double,
    @SerializedName("diametro_coleto_mm") val diametroColetoMm: Double,
    @SerializedName("pst_mg") val pstMg: Double,
    @SerializedName("psr_mg") val psrMg: Double,
    @SerializedName("psa_mg") val psaMg: Double,
    @SerializedName("comprimento_raiz_cm") val comprimentoRaizCm: Double
)

// Saída de dados
data class SaidaPrevisao(
    @SerializedName("nivelImpacto") val nivelImpacto: String,
    @SerializedName("confiancaModelo") val confiancaModelo: Double,
    @SerializedName("recomendacao") val recomendacao: String
)