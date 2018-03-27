package ComunicaoConcorrenteParalela;

import java.io.Serializable;
import SimulaGenetico.ETiposServicosAgentes;
import j_HeuristicaArvoreNAria.SolucaoNAria;

public class ObjetoComunicacaoTree implements Serializable {
    
    private SolucaoNAria solucao;
    private String mensagem_servico_agente;
    private ETiposServicosAgentes  tipo_servico_agente;
    private ETiposServicosServidor tipo_servico_servidor;
    
    public ObjetoComunicacaoTree(){}
    
    public ObjetoComunicacaoTree(SolucaoNAria solucao, String MSGservico, ETiposServicosAgentes tipo_A, ETiposServicosServidor tipo_S){
    
        this.solucao = solucao;
        this.mensagem_servico_agente = MSGservico;
        this.tipo_servico_agente = tipo_A;
        this.tipo_servico_servidor = tipo_S;
    }
    
    public SolucaoNAria getSolucao(){
        return this.solucao;    
    }
    
    public String getMSGServicoAgente(){
    
        return this.mensagem_servico_agente;
    }
    
    public void setMSGServicoAgente(String mSGServico){
    
        this.mensagem_servico_agente = mSGServico;
    }
    
    public void setSolucao(SolucaoNAria solucao){
        
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
