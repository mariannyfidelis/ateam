package SimulaGenetico;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;
import Heuristicas.Memoria;
import Heuristicas.Individuo;
import Heuristicas.Solucao;
import Util.Funcoes;

public class OperacoesSolucoes_Individuos{

    HeuristicaConstrutivaInicial.Solucao solucao = new HeuristicaConstrutivaInicial.Solucao();
 
    public HeuristicaConstrutivaInicial.Solucao retornaSolucaoAleatoria(LinkedList<HeuristicaConstrutivaInicial.Solucao> solucoes_memoria){
		
        int sol_aleatorio;

        LinkedList<HeuristicaConstrutivaInicial.Solucao> list_solucao;
        SecureRandom random = new SecureRandom();

        list_solucao =(LinkedList<HeuristicaConstrutivaInicial.Solucao>) solucoes_memoria.clone();

        sol_aleatorio = random.nextInt(list_solucao.size());

        solucao = list_solucao.get(sol_aleatorio);

      //  solucao.setSolucao(solucao);

        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new HeuristicaConstrutivaInicial.Solucao(solucao);
    //return solucao;
    }

    public HeuristicaConstrutivaInicial.Solucao retornaMelhorSolucao(LinkedList<HeuristicaConstrutivaInicial.Solucao> solucoes_memoria){

        LinkedList<HeuristicaConstrutivaInicial.Solucao> list_solucao;
        list_solucao = (LinkedList<HeuristicaConstrutivaInicial.Solucao>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasDecrescente(list_solucao);
        solucao = list_solucao.getFirst();
        //solucao.setSolucao(solucao);

        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new HeuristicaConstrutivaInicial.Solucao(solucao);
    }

    
    public HeuristicaConstrutivaInicial.Solucao retornaPiorSolucao(LinkedList<HeuristicaConstrutivaInicial.Solucao> solucoes_memoria){

        LinkedList<HeuristicaConstrutivaInicial.Solucao> list_solucao;
        list_solucao =(LinkedList<HeuristicaConstrutivaInicial.Solucao>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesHeuristicasDecrescente(list_solucao);
        solucao = list_solucao.getLast();
        //solucao.setSolucao(solucao);
        
        //return new Solucao(true, solucao.getLista(), solucao.getFitness());
        return new HeuristicaConstrutivaInicial.Solucao(solucao);
      //return solucao;
    }	
		
    // OPERAÇÕES SOBRE OS INDIVÍDUOS

    
    //Retorna a maior/pior Lista/Individuo extrapolada/ocupada dada uma Solução ordenada !!! 
    //Lista de Maior Ocupação !!!
    public static Individuo selecionaMaiorIndividuo(HeuristicaConstrutivaInicial.Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos());

        individuo = solucao.getLinkedListIndividuos().getFirst();

        return individuo;
    }
    
    //Lista de menor ocupação
    public static Individuo selecionaMenorIndividuo(HeuristicaConstrutivaInicial.Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos());

        individuo = solucao.getLinkedListIndividuos().getLast();

        return individuo;
    }

    //Lista de Ocupação Aleatória
    public static Individuo selecionaAleatorioIndividuo(HeuristicaConstrutivaInicial.Solucao solucao){

        int indv;
        Individuo individuo ;
        Random random = new Random();

        indv = random.nextInt(solucao.getLinkedListIndividuos().size());
        individuo = solucao.getLinkedListIndividuos().get(indv);

        return individuo;
    }

    //Lista de maior ocupação
    public static Individuo selecionaListaMaiorOcupacao(HeuristicaConstrutivaInicial.Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos(),"");

        individuo = solucao.getLinkedListIndividuos().getFirst();

        return individuo;
    }
    
    //Lista de menor ocupação
    public static Individuo selecionaListaMenorOcupacao(HeuristicaConstrutivaInicial.Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLinkedListIndividuos(),"");

        individuo = solucao.getLinkedListIndividuos().getLast();

        return individuo;
    }

    
    public void atualizaSolucoes(Memoria memoria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
