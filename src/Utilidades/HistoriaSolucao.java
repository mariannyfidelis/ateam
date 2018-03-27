package Utilidades;

import Heuristicas.Memoria;

public class HistoriaSolucao {

    //TipoAgente ou o Nome do Agente
    private Double FAV;
    private Double FAV2;
    private int quantidadeChapa;
    private int quantidadeCortes;
    private float somatorioSobra;
    private float inverso_mediaAreaSobras;
    private Memoria memoria_escrita;

    public HistoriaSolucao(){}
    
    public Double getFAV() {
        return FAV;
    }

    public void setFAV(Double FAV) {
        this.FAV = FAV;
    }

    public Double getFAV2() {
        return FAV2;
    }

    public void setFAV2(Double FAV2) {
        this.FAV2 = FAV2;
    }

    public float getInverso_mediaAreaSobras() {
        return inverso_mediaAreaSobras;
    }

    public void setInverso_mediaAreaSobras(float inverso_mediaAreaSobras) {
        this.inverso_mediaAreaSobras = inverso_mediaAreaSobras;
    }

    public Memoria getMemoria_escrita() {
        return memoria_escrita;
    }

    public void setMemoria_escrita(Memoria memoria_escrita) {
        this.memoria_escrita = memoria_escrita;
    }

    public int getQuantidadeChapa() {
        return quantidadeChapa;
    }

    public void setQuantidadeChapa(int quantidadeChapa) {
        this.quantidadeChapa = quantidadeChapa;
    }

    public int getQuantidadeCortes() {
        return quantidadeCortes;
    }

    public void setQuantidadeCortes(int quantidadeCortes) {
        this.quantidadeCortes = quantidadeCortes;
    }

    public float getSomatorioSobra() {
        return somatorioSobra;
    }

    public void setSomatorioSobra(float somatorioSobra) {
        this.somatorioSobra = somatorioSobra;
    }
    
    
}
