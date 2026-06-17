package com.example.biogrow.data.network

import retrofit2.http.Body
import retrofit2.http.POST
import com.example.biogrow.data.model.EntradaNaoDestrutiva
import com.example.biogrow.data.model.EntradaDestrutiva
import com.example.biogrow.data.model.SaidaPrevisao

interface API {
    @POST("/predict/nao-destrutivo")
    suspend fun classificarImpactoNaoDestrutivo(@Body dados: EntradaNaoDestrutiva): SaidaPrevisao

    @POST("/predict/destrutivo")
    suspend fun classificarImpactoDestrutivo(@Body dados: EntradaDestrutiva): SaidaPrevisao
}