package algoritmosAgCombinacao;

import AgCombinacao.AgentesOperadores;
import Heuristicas.Individuo;
import Heuristicas.Solucao;
import Heuristicas.Memoria;

public class AgenteCombinacaoAleat1{

	Solucao solucao;
	Individuo individuo1, individuo2;
	
	Filhos filhos = new Filhos();
	OperacoesSolucoes_Individuos opSolucao = new OperacoesSolucoes_Individuos();
	
                
        
        //Deve ter um método serializa e deserializa aqui !!!!!!!!!!!!!!!!!!
        
	public Solucao totalmenteAleatorioSolucao(Memoria memoria){
		
		//Seleciona Solução Aleatória
		solucao = opSolucao.retornaSolucaoAleatoria(memoria.getLista_solucoes()).clone();

		//Seleciona Indivíduos Aleatórios
		individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solucao);
		individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solucao);
		
		while (individuo1.equals(individuo2)){
			
			individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solucao);	
		}
		
		System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
		System.out.println("Tamanho Indv2 --> "+individuo2.getSize());
		
		//filhos = AgentesOperadores.operadorCrossoverPonto1(individuo1, individuo2);
		filhos = AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2);
		
		System.out.println("Saindo normalmente !!!");
		
		return solucao;
	}
	
}