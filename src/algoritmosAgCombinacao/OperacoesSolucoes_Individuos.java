package algoritmosAgCombinacao;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;
import Heuristicas.Memoria;
import Heuristicas.Individuo;
import Heuristicas.Solucao;
import Util.Funcoes;

public class OperacoesSolucoes_Individuos{

    Solucao solucao = new Solucao();
 
    public Solucao retornaSolucaoAleatoria(LinkedList<Solucao> solucoes_memoria){
		
        int sol_aleatorio;

        LinkedList<Solucao> list_solucao;
        SecureRandom random = new SecureRandom();

        list_solucao =(LinkedList<Solucao>) solucoes_memoria.clone();

        sol_aleatorio = random.nextInt(list_solucao.size());

        solucao = list_solucao.get(sol_aleatorio);

      //  solucao.setSolucao(solucao);

        return new Solucao(true, solucao.getLista(), solucao.getFitness());
    //return solucao;
    }

    public Solucao retornaMelhorSolucao(LinkedList<Solucao> solucoes_memoria){

        LinkedList<Solucao> list_solucao;
        list_solucao = (LinkedList<Solucao>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesDecrescente(list_solucao);
        solucao = list_solucao.getFirst();
        //solucao.setSolucao(solucao);

        return new Solucao(true, solucao.getLista(), solucao.getFitness());
    }

    
    public Solucao retornaPiorSolucao(LinkedList<Solucao> solucoes_memoria){

        LinkedList<Solucao> list_solucao;
        list_solucao =(LinkedList<Solucao>) solucoes_memoria.clone();

        Funcoes.ordenaSolucoesDecrescente(list_solucao);
        solucao = list_solucao.getLast();
        //solucao.setSolucao(solucao);
        
        return new Solucao(true, solucao.getLista(), solucao.getFitness());
      //return solucao;
    }	
		
    // OPERAÇÕES SOBRE OS INDIVÍDUOS

    
    //Retorna a maior/pior Lista/Individuo extrapolada/ocupada dada uma Solução ordenada !!! 
    //Lista de Maior Ocupação !!!
    public static Individuo selecionaMaiorIndividuo(Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLista());

        individuo = solucao.getLista().getFirst();

        return individuo;
    }
    
    //Lista de menor ocupação
    public static Individuo selecionaMenorIndividuo(Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLista());

        individuo = solucao.getLista().getLast();

        return individuo;
    }

    //Lista de Ocupação Aleatória
    public static Individuo selecionaAleatorioIndividuo(Solucao solucao){

        int indv;
        Individuo individuo ;
        Random random = new Random();

        indv = random.nextInt(solucao.size());
        individuo = solucao.getLista().get(indv);

        return individuo;
    }

    //Lista de maior ocupação
    public static Individuo selecionaListaMaiorOcupacao(Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLista(),"");

        individuo = solucao.getLista().getFirst();

        return individuo;
    }
    
    //Lista de menor ocupação
    public static Individuo selecionaListaMenorOcupacao(Solucao solucao){

        Individuo individuo;

        Funcoes.ordenaIndividuosDecrescente(solucao.getLista(),"");

        individuo = solucao.getLista().getLast();

        return individuo;
    }

    
    public void atualizaSolucoes(Memoria memoria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
