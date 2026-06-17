import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from contextlib import asynccontextmanager
import pickle
import numpy as np
import os
import uvicorn

# Mapeamento da pasta do arquivo main e da pasta Pickle
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PASTA_PICKLE = os.path.join(BASE_DIR, "Pickle")

# Artefatos da Análise NÃO DESTRUTIVA
CAMINHO_OHE_ND = os.path.join(PASTA_PICKLE, "OHE_biogrow.pkl")
CAMINHO_LE_ND = os.path.join(PASTA_PICKLE, "LE_biogrow.pkl")
CAMINHO_MODELO_ND = os.path.join(PASTA_PICKLE, "modelo_xgb.pkl")

# Artefatos da Análise DESTRUTIVA (Upgrade)
CAMINHO_OHE_D = os.path.join(PASTA_PICKLE, "OHE_biogrow_destrutivo.pkl")
CAMINHO_LE_D = os.path.join(PASTA_PICKLE, "LE_biogrow_destrutivo.pkl")
CAMINHO_MODELO_D = os.path.join(PASTA_PICKLE, "modelo_xgb_destrutivo.pkl")

# Carregamento global dos artefatos dos modelos
ohe_nd, le_nd, modelo_xgb_nd = None, None, None
ohe_d, le_d, modelo_xgb_d = None, None, None

@asynccontextmanager
async def lifespan(app: FastAPI):
    global ohe_nd, le_nd, modelo_xgb_nd, ohe_d, le_d, modelo_xgb_d
    try:
        # Verificação de arquivos Não Destrutivos
        if not all([os.path.exists(CAMINHO_OHE_ND), os.path.exists(CAMINHO_LE_ND), os.path.exists(CAMINHO_MODELO_ND)]):
            raise FileNotFoundError("Arquivos .pkl da análise NÃO DESTRUTIVA não foram encontrados.")
        
        # Verificação de arquivos Destrutivos
        if not all([os.path.exists(CAMINHO_OHE_D), os.path.exists(CAMINHO_LE_D), os.path.exists(CAMINHO_MODELO_D)]):
            raise FileNotFoundError("Arquivos .pkl da análise DESTRUTIVA não foram encontrados.")

        # Carregando Célula Não Destrutiva
        with open(CAMINHO_OHE_ND, "rb") as f: ohe_nd = pickle.load(f)
        with open(CAMINHO_LE_ND, "rb") as f: le_nd = pickle.load(f)
        with open(CAMINHO_MODELO_ND, "rb") as f: modelo_xgb_nd = pickle.load(f)
            
        # Carregando Célula Destrutiva
        with open(CAMINHO_OHE_D, "rb") as f: ohe_d = pickle.load(f)
        with open(CAMINHO_LE_D, "rb") as f: le_d = pickle.load(f)
        with open(CAMINHO_MODELO_D, "rb") as f: modelo_xgb_d = pickle.load(f)
            
        print("Todos os artefatos de Machine Learning (ND e Destrutivo) foram carregados com sucesso!")
    except Exception as e:
        print(f"ERRO CRÍTICO AO CARREGAR OS MODELOS: {e}")
        
    yield 
    
    # Limpeza de memória
    ohe_nd, le_nd, modelo_xgb_nd = None, None, None
    ohe_d, le_d, modelo_xgb_d = None, None, None

app = FastAPI(
    title="API BioGrow Pro",
    description="Previsão de impacto de microrganismos no arroz (Não Destrutiva e Destrutiva).",
    lifespan=lifespan
)

# Schemas de Entrada e Saída 
class EntradaNaoDestrutiva(BaseModel):
    organismo: str = Field(..., json_schema_extra={"examples": ["T4"]})
    cidade: str = Field(..., json_schema_extra={"examples": ["Castanhal"]})
    cultivo: str = Field(..., json_schema_extra={"examples": ["Agulhinha"]})
    pa_cm: float = Field(..., description="Comprimento da Parte Aérea em centímetros", json_schema_extra={"examples": [35.0]})
    diametro_coleto_mm: float = Field(..., description="Diâmetro do Coleto em milímetros", json_schema_extra={"examples": [1.24]})

class EntradaDestrutiva(BaseModel):
    organismo: str = Field(..., json_schema_extra={"examples": ["T4"]})
    cidade: str = Field(..., json_schema_extra={"examples": ["Castanhal"]})
    cultivo: str = Field(..., json_schema_extra={"examples": ["Agulhinha"]})
    pa_cm: float = Field(..., description="Comprimento da Parte Aérea em centímetros", json_schema_extra={"examples": [35.0]})
    diametro_coleto_mm: float = Field(..., description="Diâmetro do Coleto em milímetros", json_schema_extra={"examples": [1.24]})
    
    # Novas features ajustadas
    pst_mg: float = Field(..., description="Peso Seco Total em miligramas", json_schema_extra={"examples": [1570.0]})
    psr_mg: float = Field(..., description="Peso Seco da Raiz em miligramas", json_schema_extra={"examples": [450.0]})
    psa_mg: float = Field(..., description="Peso Seco da Parte Aérea em miligramas", json_schema_extra={"examples": [1120.0]})
    comprimento_raiz_cm: float = Field(..., description="Comprimento da Raiz em centímetros", json_schema_extra={"examples": [12.5]})

class SaidaPrevisao(BaseModel):
    nivelImpacto: str = Field(...)
    confiancaModelo: float = Field(...)
    recomendacao: str = Field(...)


# Rotas de Predição
# Previsão não destrutiva
@app.post("/predict/nao-destrutivo", response_model=SaidaPrevisao)
async def classificar_impacto_nao_destrutivo(dados: EntradaNaoDestrutiva):
    if ohe_nd is None or le_nd is None or modelo_xgb_nd is None:
        raise HTTPException(status_code=503, detail="O modelo não destrutivo não está pronto.")
        
    try:
        dados_df = pd.DataFrame([{
            'TRAT': dados.organismo, 
            'Cidade': dados.cidade, 
            'Plantação': dados.cultivo, 
            'PA_(CM)': dados.pa_cm, 
            'Diametro_do_coleto_(mm)': dados.diametro_coleto_mm
        }])
        
        dados_transformados = ohe_nd.transform(dados_df)
        predicao_num = modelo_xgb_nd.predict(dados_transformados)
        
        probabilidades = modelo_xgb_nd.predict_proba(dados_transformados)[0]
        indice_predito = predicao_num[0]
        confianca = probabilidades[indice_predito]
        
        classe_texto = le_nd.inverse_transform(predicao_num)[0]
        classe_exibicao = "ALTO" if "ALTO" in classe_texto.upper() else "PADRÃO"
        
        if classe_exibicao == "ALTO":
            msg = (f"Alto impacto detectado (Confiança: {confianca:.0%}). "
                   f"A muda avaliada apresenta ótimo potencial de desenvolvimento inicial.")
        else:
            msg = (f"Impacto padrão detectado (Confiança: {confianca:.0%}). "
                   f"As medições biométricas indicam um crescimento basal esperado.")

        return SaidaPrevisao(
            nivelImpacto=classe_exibicao,
            confiancaModelo=float(round(confianca, 2)),
            recomendacao=msg
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erro no processamento ND: {str(e)}")


# Previsão destrutiva
@app.post("/predict/destrutivo", response_model=SaidaPrevisao)
async def classificar_impacto_destrutivo(dados: EntradaDestrutiva):
    if ohe_d is None or le_d is None or modelo_xgb_d is None:
        raise HTTPException(status_code=503, detail="O modelo destrutivo não está pronto.")
        
    try:
        dados_df = pd.DataFrame([{
            'TRAT': dados.organismo, 
            'Cidade': dados.cidade, 
            'Plantação': dados.cultivo, 
            'PA_(CM)': dados.pa_cm, 
            'Diametro_do_coleto_(mm)': dados.diametro_coleto_mm,
            'PST_(mg)': dados.pst_mg,
            'PSR_(mg)': dados.psr_mg,
            'PSA_(mg)': dados.psa_mg,
            'Comprimento_de_raiz_(CM)': dados.comprimento_raiz_cm
        }])
        
        dados_transformados = ohe_d.transform(dados_df)
        predicao_num = modelo_xgb_d.predict(dados_transformados)
        
        probabilidades = modelo_xgb_d.predict_proba(dados_transformados)[0]
        indice_predito = predicao_num[0]
        confianca = probabilidades[indice_predito]
        
        classe_texto = le_d.inverse_transform(predicao_num)[0]
        classe_exibicao = "ALTO" if "ALTO" in classe_texto.upper() else "PADRÃO"
        
        if classe_exibicao == "ALTO":
            msg = (f"Análise Destrutiva concluída: Alto impacto verificado (Confiança: {confianca:.0%}). "
                   f"Os dados de biomassa seca e comprimento de raiz coletados confirmam uma alta correlação "
                   f"com o Índice de Qualidade de Dickson (IQD).")
        else:
            msg = (f"Análise Destrutiva concluída: Impacto padrão verificado (Confiança: {confianca:.0%}). "
                   f"O perfil morfológico e de biomassa coletado indica uma performance dentro da média esperada.")

        return SaidaPrevisao(
            nivelImpacto=classe_exibicao,
            confiancaModelo=float(round(confianca, 2)),
            recomendacao=msg
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erro no processamento destrutivo: {str(e)}")


@app.get("/")
async def root():
    return {
        "status": "Online", 
        "projeto": "BioGrow API(TCC)",
        "modos_suportados": ["Não Destrutivo (/predict/nao-destrutivo)", "Destrutivo (/predict/destrutivo)"]
    }

if __name__ == '__main__':
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)