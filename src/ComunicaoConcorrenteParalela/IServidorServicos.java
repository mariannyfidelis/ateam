package ComunicaoConcorrenteParalela;

import Heuristicas.Memoria;
import Heuristicas.Solucao;

public interface IServidorServicos {
    
    public Solucao retornaSolucaoAleatoria(Memoria memoria);
    public Solucao retornaMelhorSolucao(Memoria memoria);
    public Solucao retornaPiorSolucao(Memoria memoria); 
    public void atualizaSolucoes(Memoria memoria);
    
    //public void adicionaSolucao(Solucao solucao);
    //public void removeSolucao(Solucao solucao);
    //public Solucao retorna_pior_Solucao(LinkedList<Solucao> list_solucao);
    //public Solucao retorna_melhor_Solucao(LinkedList<Solucao> list_solucao);
    //public Solucao retorna_aleatoria_Solucao(LinkedList<Solucao> list_solucao);
}
