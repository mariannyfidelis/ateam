package SimulaGenetico;

import java.util.List;
import java.util.Random;
import HHDInterfaces.IBin;
import Utilidades.Funcoes;
import Heuristicas.Memoria;
import java.util.LinkedList;
import Heuristicas.Individuo;
import java.security.SecureRandom;
import HHDInternal.SolucaoHeuristica;
import HeuristicaConstrutivaInicial.Bin;
import HHDBinPackingTree.BinPackTreeForest;
import j_HeuristicaArvoreNAria.SolucaoNAria;

public class OperacoesSolucoes_Individuos{

    SolucaoHeuristica solucao = new SolucaoHeuristica();
    SolucaoNAria solucaoNaria = new SolucaoNAria();
    
//##################################### OPERAÇÕES SOBRE SOLUÇÃO N-ÁRIA #################################

    public SolucaoNAria retornaSolucaoAleatoriaNaria(LinkedList<SolucaoNAria> solucoes_memoria){
		
        int sol_aleatorio;

        LinkedList<SolucaoNAria> list_solucao;
        SecureRandom random = new SecureRandom();

        list_solucao =(LinkedList<SolucaoNAria>) solucoes_memoria.clone();

        sol_aleatorio = random.nextInt(list_solucao.size());

        solucaoNaria = list_solucao.get(sol_aleatorio);

        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoNAria(solucaoNaria);
    }

    public SolucaoNAria retornaMelhorSolucaoNaria(LinkedList<SolucaoNAria> solucoes_memoria){

        LinkedList<SolucaoNAria> list_solucao;
        list_solucao = (LinkedList<SolucaoNAria>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasNariaDecrescente(list_solucao);
        solucaoNaria = list_solucao.getFirst();
        //solucao.setSolucao(solucao);

        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoNAria(solucaoNaria);
    }

    
    public SolucaoNAria retornaPiorSolucaoNaria(LinkedList<SolucaoNAria> solucoes_memoria){

        LinkedList<SolucaoNAria> list_solucao;
        list_solucao =(LinkedList<SolucaoNAria>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasNariaDecrescente(list_solucao);
        solucaoNaria = list_solucao.getLast();
        //solucao.setSolucao(solucao);
        
        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoNAria(solucaoNaria);
    }
    
    
//##################################### OPERAÇÕES SOBRE SOLUÇÃO HEURÍSTICA #################################
    
    public SolucaoHeuristica retornaSolucaoAleatoria(LinkedList<SolucaoHeuristica> solucoes_memoria){
		
        int sol_aleatorio;

        LinkedList<SolucaoHeuristica> list_solucao;
        SecureRandom random = new SecureRandom();

        list_solucao =(LinkedList<SolucaoHeuristica>) solucoes_memoria.clone();

        sol_aleatorio = random.nextInt(list_solucao.size());

        solucao = list_solucao.get(sol_aleatorio);

      //  solucao.setSolucao(solucao);

        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoHeuristica(solucao);
    //return solucao;
    }

    public SolucaoHeuristica retornaMelhorSolucao(LinkedList<SolucaoHeuristica> solucoes_memoria){

        LinkedList<SolucaoHeuristica> list_solucao;
        list_solucao = (LinkedList<SolucaoHeuristica>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasDecrescente(list_solucao);
        solucao = list_solucao.getFirst();
        //solucao.setSolucao(solucao);

        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoHeuristica(solucao);
    }

    
    public SolucaoHeuristica retornaPiorSolucao(LinkedList<SolucaoHeuristica> solucoes_memoria){

        LinkedList<SolucaoHeuristica> list_solucao;
        list_solucao =(LinkedList<SolucaoHeuristica>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasDecrescente(list_solucao);
        solucao = list_solucao.getLast();
        //solucao.setSolucao(solucao);
        
        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoHeuristica(solucao);
      //return solucao;
    }	
    
    public SolucaoHeuristica retorna_linearMelhor_Solucao(LinkedList<SolucaoHeuristica> solucoes_memoria){

        LinkedList<SolucaoHeuristica> list_solucao;
        list_solucao =(LinkedList<SolucaoHeuristica>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasDecrescente(list_solucao);
        
        Funcoes f = new Funcoes();
        int s = f.linearRand(false, solucoes_memoria.size());
        
        solucao = list_solucao.get(s);
        //solucao.setSolucao(solucao);
        
        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoHeuristica(solucao);
      //return solucao;
    }
    
    public SolucaoHeuristica retorna_linearPior_Solucao(LinkedList<SolucaoHeuristica> solucoes_memoria){

        LinkedList<SolucaoHeuristica> list_solucao;
        list_solucao =(LinkedList<SolucaoHeuristica>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasDecrescente(list_solucao);
        
        Funcoes f = new Funcoes();
        int s = f.linearRand(true, solucoes_memoria.size());
        
        solucao = list_solucao.get(s);
        //solucao.setSolucao(solucao);
        
        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoHeuristica(solucao);
      //return solucao;
    }
    
    public SolucaoHeuristica retorna_Triangular_Solucao(LinkedList<SolucaoHeuristica> solucoes_memoria){

        LinkedList<SolucaoHeuristica> list_solucao;
        list_solucao =(LinkedList<SolucaoHeuristica>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasDecrescente(list_solucao);
        
        Funcoes f = new Funcoes();
        int s = f.triangularRand(solucoes_memoria.size());
        
        solucao = list_solucao.get(s);
        //solucao.setSolucao(solucao);
        
        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new SolucaoHeuristica(solucao);
      //return solucao;
    }
		
    // OPERAÇÕES SOBRE OS INDIVÍDUOS

    
    //Retorna a maior/pior Lista/Individuo extrapolada/ocupada dada uma Solução ordenada !!! 
    //Lista de Maior Ocupação !!!
    public static Individuo selecionaMaiorIndividuo(SolucaoHeuristica solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos());

        individuo = solucao.getLinkedListIndividuos().getFirst();

        return individuo;
    }
    
    //Lista de menor ocupação
    public static Individuo selecionaMenorIndividuo(SolucaoHeuristica solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos());

        individuo = solucao.getLinkedListIndividuos().getLast();

        return individuo;
    }

    //Lista de Ocupação Aleatória
    public static Individuo selecionaAleatorioIndividuo(SolucaoHeuristica solucao){

        int indv;
        Individuo individuo ;
        Random random = new Random();

        indv = random.nextInt(solucao.getLinkedListIndividuos().size());
        individuo = solucao.getLinkedListIndividuos().get(indv);

        return individuo;
    }

    //Lista de maior ocupação
    public static Individuo selecionaListaMaiorOcupacao(SolucaoHeuristica solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos(),"");

        individuo = solucao.getLinkedListIndividuos().getFirst();

        return individuo;
    }
    
    //Lista de menor ocupação
    public static Individuo selecionaListaMenorOcupacao(SolucaoHeuristica solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos(),"");

        individuo = solucao.getLinkedListIndividuos().getLast();

        return individuo;
    }

    /*########### AQUI SERÁ ESCOLHIDO AS PLACAS PARA DESMONTE E/OU OPERAÇÕES###############*/
    public static IBin selecionaListaMaiorOcupacaoG(SolucaoHeuristica solucao){

        HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solucao);
        
        return solucao.getObjetos().get(0);
    }
    
    //Lista de menor ocupação
    public static IBin selecionaListaMenorOcupacaoG(SolucaoHeuristica solucao){

       List<Bin> objetos;
       
       HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solucao);
       
       objetos = solucao.getObjetos();
       
       
       return objetos.get(objetos.size() - 1);
    }
    
    public static IBin selecionaListaAleatoriaOcupacaoG(SolucaoHeuristica solucao){

       List<Bin> objetos;
       Random random = new Random();

       int indv = random.nextInt(solucao.getObjetos().size());
        
       HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solucao);
       
       objetos = solucao.getObjetos();
       
       return objetos.get(indv);
    }
    /*########### AQUI SERÁ ESCOLHIDO AS PLACAS PARA DESMONTE E/OU OPERAÇÕES###############*/
    public static IBin selecionaListaMaiorOcupacaoBPTree(BinPackTreeForest solucao){
    
        List<IBin> bPackTree;
        HHDInternal.FuncoesHDInternal.ordenaSolucaoDecrescente(solucao);
        
        bPackTree = solucao.getListaBinPackTrees();
        
        return bPackTree.get(0);
    }
    
    public static IBin selecionaListaMenorOcupacaoBPTree(BinPackTreeForest solucao){
    
       List<IBin> bPackTree;
       HHDInternal.FuncoesHDInternal.ordenaSolucaoDecrescente(solucao);
       
       bPackTree = solucao.getListaBinPackTrees(); 
       
       return bPackTree.get(bPackTree.size()-1);
    }
    
    public static IBin selecionaListaAleatoriaOcupacaoBPTree(BinPackTreeForest solucao){
    
        List<IBin> bPackTree;
        SecureRandom randomico = new SecureRandom();
        
        HHDInternal.FuncoesHDInternal.ordenaSolucaoDecrescente(solucao);
        
        bPackTree = solucao.getListaBinPackTrees(); 
        int indv = randomico.nextInt(solucao.getListaBinPackTrees().size());
        
        return bPackTree.get(randomico.nextInt());
    }
        
    
    public void atualizaSolucoes(Memoria memoria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
