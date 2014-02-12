package ComunicaoConcorrenteParalela;

import AgCombinacao.ETiposServicosAgentes;
import Heuristicas.Solucao;
import java.io.Serializable;

public class ObjetoComunicacao implements Serializable{

    private Solucao solucao;
    private String servico_agente;
    private ETiposServicosAgentes tipo_servico_agente;
    private ETiposServicosServidor tipo_servico_servidor;
        
    public ObjetoComunicacao(){}
    public ObjetoComunicacao(Solucao solucao, String servico, ETiposServicosAgentes tipo_A, ETiposServicosServidor tipo_S){
    
        this.solucao = solucao;
        this.servico_agente = servico;
        this.tipo_servico_agente = tipo_A;
        this.tipo_servico_servidor = tipo_S;
    }
    
    public Solucao getSolucao(){
        return this.solucao;    
    }
    
    public String getServicoAgente(){
    
        return this.servico_agente;
    }
    
    public void setSolucao(Solucao solucao){
        this.solucao = solucao;
    }
    
    public String getTipoServicoAgente(){
        
        return this.tipo_servico_agente.toString();
    }
    
    public ETiposServicosAgentes getTipoServicoAgente2(){
        return this.tipo_servico_agente;
    }
    
    public void setTipoServicoAgente(ETiposServicosAgentes tipo_servico){
        this.tipo_servico_agente = tipo_servico;
    }
    
    public ETiposServicosServidor getTipoServicoServidor(){
    
        return this.tipo_servico_servidor;
    }
    
    public void setTipoServicoServidor(ETiposServicosServidor tipo_S){
    
        this.tipo_servico_servidor = tipo_S;
    }
}