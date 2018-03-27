package ComunicaoConcorrenteParalela;

import HHDBinPackingTree.BinPackTreeForest;
import java.io.Serializable;
import HHDInternal.SolucaoHeuristica;
import SimulaGenetico.ETiposServicosAgentes;

public class ObjetoComunicacaoMelhorado implements Serializable {
    
    private SolucaoHeuristica solucao;
    private BinPackTreeForest treeSolution;
    private String mensagem_servico_agente;
    private ETiposServicosAgentes tipo_servico_agente;
    private ETiposServicosServidor tipo_servico_servidor;

    public ObjetoComunicacaoMelhorado(){
    
        mensagem_servico_agente =  "Não tenho nada";
    }
    
    public ObjetoComunicacaoMelhorado(SolucaoHeuristica solucao, String MSGservico, ETiposServicosAgentes tipo_A, ETiposServicosServidor tipo_S){
    
        this.solucao = solucao;
        this.treeSolution = null;
        this.mensagem_servico_agente = MSGservico;
        this.tipo_servico_agente = tipo_A;
        this.tipo_servico_servidor = tipo_S;
    }
    
    public SolucaoHeuristica getSolucao(){
        return this.solucao;    
    }
    
    public BinPackTreeForest getTreeSolucao(){
        return this.treeSolution;    
    }
    
    public String getMSGServicoAgente(){
    
        return this.mensagem_servico_agente;
    }
    
    public void setMSGServicoAgente(String mSGServico){
    
        this.mensagem_servico_agente = mSGServico;
    }
    
    public void setSolucao(SolucaoHeuristica solucao){
        
        this.solucao = solucao;
    }
    
    public void setTreeSolucao(BinPackTreeForest treeSolution){
        
        this.treeSolution = treeSolution;    
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
