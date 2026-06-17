import pytest
from fastapi.testclient import TestClient
from unittest.mock import patch, MagicMock
from main import app

# Cria um cliente de teste usando a instância da sua aplicação FastAPI
client = TestClient(app)

# Testes da rota raiz
def test_root_status():
    # Testa se a API está online e retornando a mensagem raiz atualizada corretamente.
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {
        "status": "Online", 
        "projeto": "BioGrow API - Upgrade Multimodal (TCC)",
        "modos_suportados": ["Não Destrutivo (/predict/nao-destrutivo)", "Destrutivo (/predict/destrutivo)"]
    }

# Testes da análise não destrutiva
def test_predict_nd_dados_invalidos():
    # Testa se a rota Não Destrutiva recusa requisições com dados faltando (Erro 422).
    dados_incompletos = {
        "organismo": "T4",
        "cidade": "Castanhal",
        "cultivo": "Agulhinha"
        # Faltam pa_cm e diametro_coleto_mm
    }
    
    response = client.post("/predict/nao-destrutivo", json=dados_incompletos)
    assert response.status_code == 422

@patch("main.ohe_nd")
@patch("main.le_nd")
@patch("main.modelo_xgb_nd")
def test_predict_nd_impacto_alto_sucesso(mock_modelo_nd, mock_le_nd, mock_ohe_nd):
    """Testa o fluxo de sucesso da predição Não Destrutiva simulando ALTO IMPACTO."""
    
    mock_ohe_nd.transform.return_value = MagicMock()
    mock_modelo_nd.predict.return_value = [1] 
    mock_modelo_nd.predict_proba.return_value = [[0.05, 0.95]] # Finge 95% de confiança
    mock_le_nd.inverse_transform.return_value = ["ALTO_IMPACTO"] 
    
    dados_validos = {
        "organismo": "T4",
        "cidade": "Castanhal",
        "cultivo": "Agulhinha",
        "pa_cm": 35.0,
        "diametro_coleto_mm": 1.24
    }
    
    response = client.post("/predict/nao-destrutivo", json=dados_validos)
    
    assert response.status_code == 200
    resultado = response.json()
    assert resultado["nivelImpacto"] == "ALTO"
    assert resultado["confiancaModelo"] == 0.95
    assert "Alto impacto detectado" in resultado["recomendacao"]

@patch("main.modelo_xgb_nd", None)
def test_predict_nd_modelos_nao_carregados():
    """Testa se a rota Não Destrutiva retorna erro 503 caso os artefatos não estejam carregados."""
    dados_validos = {
        "organismo": "T4",
        "cidade": "Castanhal",
        "cultivo": "Agulhinha",
        "pa_cm": 35.0,
        "diametro_coleto_mm": 1.24
    }
    
    response = client.post("/predict/nao-destrutivo", json=dados_validos)
    assert response.status_code == 503
    assert response.json()["detail"] == "O modelo não destrutivo não está pronto."

# Testes da análise destrutiva
def test_predict_destrutivo_dados_invalidos():
    """Testa se a rota Destrutiva recusa requisições faltando as novas features de biomassa (Erro 422)."""
    dados_incompletos = {
        "organismo": "T4",
        "cidade": "Castanhal",
        "cultivo": "Agulhinha",
        "pa_cm": 35.0,
        "diametro_coleto_mm": 1.24
        # Faltam pst_mg, psr_mg, psa_mg e comprimento_raiz_cm
    }
    
    response = client.post("/predict/destrutivo", json=dados_incompletos)
    assert response.status_code == 422

@patch("main.ohe_d")
@patch("main.le_d")
@patch("main.modelo_xgb_d")
def test_predict_destrutivo_impacto_padrao_sucesso(mock_modelo_d, mock_le_d, mock_ohe_d):
    """Testa o fluxo de sucesso da predição Destrutiva simulando IMPACTO PADRÃO."""
    
    mock_ohe_d.transform.return_value = MagicMock()
    mock_modelo_d.predict.return_value = [0]
    mock_modelo_d.predict_proba.return_value = [[0.88, 0.12]] # Finge 88% de confiança para Padrão
    mock_le_d.inverse_transform.return_value = ["PADRAO"]
    
    dados_validos = {
        "organismo": "Nenhum",
        "cidade": "Castanhal",
        "cultivo": "Agulhinha",
        "pa_cm": 20.0,
        "diametro_coleto_mm": 0.90,
        "pst_mg": 850.0,
        "psr_mg": 200.0,
        "psa_mg": 650.0,
        "comprimento_raiz_cm": 8.0
    }
    
    response = client.post("/predict/destrutivo", json=dados_validos)
    
    assert response.status_code == 200
    resultado = response.json()
    assert resultado["nivelImpacto"] == "PADRÃO"
    assert resultado["confiancaModelo"] == 0.88
    assert "Impacto padrão verificado" in resultado["recomendacao"]

@patch("main.modelo_xgb_d", None)
def test_predict_destrutivo_modelos_nao_carregados():
    """Testa se a rota Destrutiva retorna erro 503 caso os artefatos não estejam carregados."""
    dados_validos = {
        "organismo": "T4",
        "cidade": "Castanhal",
        "cultivo": "Agulhinha",
        "pa_cm": 35.0,
        "diametro_coleto_mm": 1.24,
        "pst_mg": 1570.0,
        "psr_mg": 450.0,
        "psa_mg": 1120.0,
        "comprimento_raiz_cm": 12.5
    }
    
    response = client.post("/predict/destrutivo", json=dados_validos)
    assert response.status_code == 503
    assert response.json()["detail"] == "O modelo destrutivo não está pronto."

# Para rodar o teste: pytest -v test_main.py