package algoritmosAgCombinacao;

import AgCombinacao.AgentesOperadores;
import Heuristicas.Memoria;
import Heuristicas.Individuo;
import Heuristicas.Solucao;

public class AgenteCombinacaoMelhorS2 {

	Solucao solucao;
	Individuo individuo1, individuo2;
	
	Filhos filhos = new Filhos();
	OperacoesSolucoes_Individuos opSolucao = new OperacoesSolucoes_Individuos();
	
	public Solucao melhorSol_MelhorIndividuo(Memoria memoria){
		
		//Retorna a Melhor Solucao
		solucao = opSolucao.retornaMelhorSolucao(memoria.getLista_solucoes());
				
		//Selecionar Indivíduo aleatório
		individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solucao);
				
		//Selecionar Pior Indivíduo
		individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(solucao);
				
		while (individuo1.equals(individuo2)){
					
				individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(solucao);
		}
				
		System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
		System.out.println("Tamanho Indv2 --> "+individuo2.getSize());
				
		//filhos = AgentesOperadores.operadorCrossoverPonto1(individuo1, individuo2);
		filhos = AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2);
						
		System.out.println("Saindo normalmente !!!");
				
		return solucao;
		
		
	}
	
}
