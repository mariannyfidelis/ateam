package Util;

import java.io.IOException;
import java.util.LinkedList;
import Heuristicas.Memoria;
import AgCombinacao.AgentesCombinacao;
import AgCombinacao.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ServicoAgente;
import Heuristicas.FirstBestFit;
import Heuristicas.Individuo;
import Heuristicas.Solucao;
import Heuristicas.Util;
import algoritmosAgCombinacao.OperacoesSolucoes_Individuos;
import java.security.SecureRandom;

public class PrincipalTeste {

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws IOException{

        LinkedList<Pedidos> pedidos;
        LinkedList<Individuo> list_indiv;

        Memoria memoria_capacidade_respeitada = new Memoria(10);
        Memoria memoria_capacidade_NRespeitada = new Memoria(10);

        Solucao solucao = new Solucao();
        Solucao solucao_aux = new Solucao();

        pedidos = Funcoes.lerArquivo();

        //Funcoes.imprimeListaPedidos(pedidos);

        FirstBestFit first = new FirstBestFit();

        Funcoes.ordenaPedidosDecrescente(pedidos);
        list_indiv = first.encaixe_itens(pedidos,Funcoes.getChapa(), true);

        solucao.setLista(list_indiv);
        solucao.calculaFitness();

        memoria_capacidade_respeitada.adiciona_solucao(new Solucao(solucao.isCapacidadeRespeitada(),
                            solucao.getLista(), solucao.getFitness()));
        System.out.println("Número de soluçoes na memória --> "+memoria_capacidade_respeitada.getNum_solucoes());
        
        Util.imprimeListaIndividuos(list_indiv);
        memoria_capacidade_respeitada.imprimeSolucoes();
 
        int opc = 1, pcv;
        OperacoesSolucoes_Individuos op = new OperacoesSolucoes_Individuos();

        AgentesCombinacao agente;
      
        while(memoria_capacidade_respeitada.getNum_solucoes() < memoria_capacidade_respeitada.getTamanho_memoria()){

            SecureRandom random = new SecureRandom();
            pcv = random.nextInt(10);

            agente = new AgentesCombinacao(0, "Agente "+opc+"", ServicoAgente.Combinacao, ETiposServicosAgentes.AleatorioPior);
            
            System.out.println("Criei um agente combinação .........");
            switch(pcv){
                case 0:
                                      
                    System.out.println("Caso 1");
                    solucao.setSolucao(op.retornaMelhorSolucao(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.AleatorioMelhor(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 1:
                    solucao.setSolucao(op.retornaMelhorSolucao(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.AleatorioMelhorS(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 2:
                    System.out.println("Caso 3");
                    solucao.setSolucao(op.retornaMelhorSolucao(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.AleatorioPior(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 3:
                    System.out.println("Caso 4");
                    solucao.setSolucao(op.retornaMelhorSolucao(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.MelhorSol_Aleatorio(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 4:
                    System.out.println("Caso 5");
                    solucao.setSolucao(op.retornaMelhorSolucao(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.MelhorSol_Maior_MenorIndv(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 5:
                    System.out.println("Caso 6");
                    solucao.setSolucao(op.retornaMelhorSolucao(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.MelhorSol_MelhorIndividuo(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 6:
                    System.out.println("Caso 7");
                    solucao.setSolucao(op.retornaMelhorSolucao(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.MelhorSol_Melhor_DoisIndv(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 7:
                    System.out.println("Caso 8");
                    solucao.setSolucao(op.retornaSolucaoAleatoria(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.Melhor_Dois(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;
                    
                case 8:
                    System.out.println("Caso 9");
                    solucao.setSolucao(op.retornaSolucaoAleatoria(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.Melhor_Pior(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;

                case 9:
                    System.out.println("Caso 10");
                    solucao.setSolucao(op.retornaSolucaoAleatoria(memoria_capacidade_respeitada.getLista_solucoes()));
                    solucao_aux.setSolucao(agente.TotalmenteAleatorioSolucao(solucao));
                    
                    if((solucao == solucao_aux) || (solucao.equals(solucao_aux))){
                        System.out.println("\nEita pode haver erro !!!\n\n");
                    }
                break;
                
                    default: break;
            }
            try{
                
                System.out.println("Vou adicionar uma nova solução na memória !");
                
                if(solucao_aux.isCapacidadeRespeitada() == false){
                    memoria_capacidade_NRespeitada.adiciona_solucao(new Solucao(solucao_aux.isCapacidadeRespeitada(),
                            solucao_aux.getLista(), solucao_aux.getFitness()));
                    System.out.println("Número Soluçoes na Memória Não-Respeitada--> "+memoria_capacidade_NRespeitada.getNum_solucoes());
                }
                else{
                    memoria_capacidade_respeitada.adiciona_solucao(new Solucao(solucao_aux.isCapacidadeRespeitada(),
                            solucao_aux.getLista(), solucao_aux.getFitness()));
                    System.out.println("Número Soluçoes na Memória Respeitada --> "+memoria_capacidade_respeitada.getNum_solucoes());
                }
                
            }
            catch(NullPointerException erro){
            
                System.out.println("Erro de ponteiro Nulo deve ser verificado !!!");
            }
            
            memoria_capacidade_respeitada.imprimeSolucoes();
        opc++;
      }
    }
}