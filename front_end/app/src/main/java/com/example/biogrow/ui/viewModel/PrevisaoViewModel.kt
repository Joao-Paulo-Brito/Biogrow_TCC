package com.example.biogrow.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biogrow.data.model.EntradaDestrutiva
import com.example.biogrow.data.model.EntradaNaoDestrutiva
import com.example.biogrow.data.model.SaidaPrevisao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estados da Tela
sealed class PrevisaoUiState {
    object Ocioso : PrevisaoUiState()
    object Carregando : PrevisaoUiState()
    data class Sucesso(val resultado: SaidaPrevisao) : PrevisaoUiState()
    data class Erro(val mensagem: String) : PrevisaoUiState()
}

// O ViewModel
class PrevisaoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PrevisaoUiState>(PrevisaoUiState.Ocioso)
    val uiState: StateFlow<PrevisaoUiState> = _uiState.asStateFlow()

    // Análise Não Destrutiva
    fun buscarPrevisaoNaoDestrutiva(
        organismo: String,
        cidade: String,
        cultivo: String,
        paCm: Double,
        diametroColetoMm: Double
    ) {
        viewModelScope.launch {
            _uiState.value = PrevisaoUiState.Carregando

            try {
                val entrada = EntradaNaoDestrutiva(
                    organismo = organismo,
                    cidade = cidade,
                    cultivo = cultivo,
                    paCm = paCm,
                    diametroColetoMm = diametroColetoMm
                )

                val resposta = com.example.biogrow.data.network.RetrofitClient.api.classificarImpactoNaoDestrutivo(entrada)
                _uiState.value = PrevisaoUiState.Sucesso(resposta)

            } catch (e: Exception) {
                _uiState.value = PrevisaoUiState.Erro("Erro na comunicação: ${e.localizedMessage}")
            }
        }
    }

    // Análise Destrutiva
    fun buscarPrevisaoDestrutiva(
        organismo: String,
        cidade: String,
        cultivo: String,
        paCm: Double,
        diametroColetoMm: Double,
        pstMg: Double,
        psrMg: Double,
        psaMg: Double,
        comprimentoRaizCm: Double
    ) {
        viewModelScope.launch {
            _uiState.value = PrevisaoUiState.Carregando

            try {
                val entrada = EntradaDestrutiva(
                    organismo = organismo,
                    cidade = cidade,
                    cultivo = cultivo,
                    paCm = paCm,
                    diametroColetoMm = diametroColetoMm,
                    pstMg = pstMg,
                    psrMg = psrMg,
                    psaMg = psaMg,
                    comprimentoRaizCm = comprimentoRaizCm
                )

                val resposta = com.example.biogrow.data.network.RetrofitClient.api.classificarImpactoDestrutivo(entrada)
                _uiState.value = PrevisaoUiState.Sucesso(resposta)

            } catch (e: Exception) {
                _uiState.value = PrevisaoUiState.Erro("Erro na comunicação: ${e.localizedMessage}")
            }
        }
    }

    fun resetarTela() {
        _uiState.value = PrevisaoUiState.Ocioso
    }
}