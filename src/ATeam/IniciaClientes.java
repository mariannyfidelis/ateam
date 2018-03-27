package ATeam;

import java.util.Scanner;
import java.io.IOException;
import HHDInternal.HHDHeuristic;
import Utilidades.PoliticaSelecao;
import java.io.FileNotFoundException;
import SimulaGenetico.AgentesCombinacao;
import HHD_Exception.PecaInvalidaException;
import SimulaGenetico.ETiposServicosAgentes;
import j_HeuristicaArvoreNAria.AgentBasedTree;
import ComunicaoConcorrenteParalela.ServicoAgente;
import SimulaGenetico.AgentesAlocacao_E_Permutacao;
import HeuristicaConstrutivaInicial.HeuristicaGRASP;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import Heuristicas.EstrategiaBinPackTree;
import java.io.File;

public class IniciaClientes {

    public IniciaClientes(){}
    
    //###########################  AGENTES RELACIONADOS A ESTRUTURA DE ÁRVORE BINÁRIA HHDHEURISTIC ##############################
    
    public void iniciaClienteHHDHeuristic( ETipoHeuristicas tipoHeuristica, ServicoAgente tipo_agente, 
                                              ETiposServicosAgentes operacao_agente, ETiposServicosServidor operacao_servidor,
                                              int porta, PoliticaSelecao polSolucao, PoliticaSelecao polPlacas, double aprov_tree,
                                              float aproveitamento, int typeGrasp, EstrategiaBinPackTree estrategia) throws IOException,
                                                                                                                            Exception{
        EstrategiaBinPackTree estrategiaTree = estrategia;
        PoliticaSelecao polSol = polSolucao, polPlac = polPlacas;
        double aproveitment = aprov_tree;
        int tipoGrasp = 0, tipoAgente = 0, opAgente = 0, opServidor = 0, portaCom = porta, tipo_heuristica = 0, 
                                                               estragTree = 0, politicSol = 0, politicPlac = 0;
        float aproveit = 0;
                                    //Tipos de Heurísticas
        if(tipoHeuristica == ETipoHeuristicas.HHDHeuristic){                     tipo_heuristica = 1;  }
        if(tipoHeuristica == ETipoHeuristicas.HHDHeuristic_Melhoria){            tipo_heuristica = 2;  }
        if(tipoHeuristica == ETipoHeuristicas.HHDHeuristic_Melhoria_Tree){       tipo_heuristica = 3;  }
        
                                    //Tipo de Serviço
        if(tipo_agente == ServicoAgente.Inicializacao){                          tipoAgente = 1; }
        if(tipo_agente == ServicoAgente.HHDHeuristic_Melhoria){                  tipoAgente = 2; }

                                  //Serviços de Inicialização, Escrita e Consulta
        if(operacao_agente == ETiposServicosAgentes.Inicializacao_HHDHeuristic){ opAgente = 1; }
        if(operacao_agente == ETiposServicosAgentes.Consulta_Solucao){           opAgente = 2; }
        if(operacao_agente == ETiposServicosAgentes.Escrever_Solucacao){         opAgente = 3; }
                                           
                                  //Serviços do Servidor
        if(operacao_servidor == ETiposServicosServidor.Inserir_Solucao){         opServidor = 1; }
        if(operacao_servidor == ETiposServicosServidor.AtualizaSolucao){         opServidor = 2; }
        if(operacao_servidor == ETiposServicosServidor.MelhorSolucao)  {         opServidor = 3; }
        if(operacao_servidor == ETiposServicosServidor.PiorSolucao)    {         opServidor = 4; }
        if(operacao_servidor == ETiposServicosServidor.Solucao_Aleatoria){       opServidor = 5; }
        if(operacao_servidor == ETiposServicosServidor.Roleta){                  opServidor = 6; }
        
                                //Serviços de Seleção das Soluções
        if(polSol == null){                                                      politicSol = 0;}
        if(polSol == PoliticaSelecao.Melhor){                                    politicSol = 1;}
        if(polSol == PoliticaSelecao.ProbabilidadeMelhor){                       politicSol = 2;}
        if(polSol == PoliticaSelecao.Aleatorio){                                 politicSol = 3;}
        if(polSol == PoliticaSelecao.Pior){                                      politicSol = 4;}
        if(polSol == PoliticaSelecao.ProbabilidadeMenor){                        politicSol = 5;}
                                
                                //Serviços de Seleção das Placas
        if(polPlac == null){                                                     politicPlac = 0;}
        if(polPlac == PoliticaSelecao.MelhorMelhor){                             politicPlac = 1;}
        if(polPlac == PoliticaSelecao.MelhorPior){                               politicPlac = 2;}
        if(polPlac == PoliticaSelecao.MelhorAleatorio){                          politicPlac = 3;}
        if(polPlac == PoliticaSelecao.PiorAleatorio){                            politicPlac = 4;}
        if(polPlac == PoliticaSelecao.PiorPior){                                 politicPlac = 5;}
        
        aproveit   = aproveitamento;
        tipoGrasp  = typeGrasp;
        
        if(estrategiaTree == EstrategiaBinPackTree.OneSolutionHHD   ){                                 estragTree = 1;}
        if(estrategiaTree == EstrategiaBinPackTree.TwoSolutionsHHD  ){                                 estragTree = 2;}
        if(estrategiaTree == EstrategiaBinPackTree.OneSolutionGRASP ){                                 estragTree = 3;}        
        if(estrategiaTree == EstrategiaBinPackTree.TwoSolutionsGRASP){                                 estragTree = 4;}
        
        String args[] = {Integer.toString(tipo_heuristica),Integer.toString(tipoAgente),Integer.toString(opAgente),
                         Integer.toString(opServidor), Integer.toString(porta),Integer.toString(politicSol), 
                         Integer.toString(politicPlac), Double.toString(aproveitment), Float.toString(aproveit),
                         Integer.toString(tipoGrasp),Integer.toString(estragTree)};

        /************************ HEURÍSTICAS HHDHEURISTICA ************************************************************************/
        
        if(tipoHeuristica == ETipoHeuristicas.HHDHeuristic){
            HHDHeuristic.main(args);
        }
        if(tipoHeuristica == ETipoHeuristicas.HHDHeuristic_Melhoria){
            HHDHeuristic.main(args);
        }
        if(tipoHeuristica == ETipoHeuristicas.HHDHeuristic_Melhoria_Tree){
            HHDHeuristic.main(args);
        }      
    }
    
    //###########################  AGENTES RELACIONADOS A ESTRUTURA DE ÁRVORE ###################################################
        
    public void iniciaClienteTreeInsertion(ETipoHeuristicas tipoHeuristica, ServicoAgente tipo_agente, 
                                              ETiposServicosAgentes operacao_agente, ETiposServicosServidor operacao_servidor,
                                              int porta, boolean rotaciona, boolean firstfit) throws IOException, Exception{
        
        int tipoAgente = 0, opAgente = 0, opServidor = 0, portaCom = porta, tipo_heuristica = 0;
        int rotacion, firstFit;
                                    //Tipos de Heurísticas
        if(tipoHeuristica == ETipoHeuristicas.FirstFit){                         tipo_heuristica = 1;  }
        if(tipoHeuristica == ETipoHeuristicas.BestFit){                          tipo_heuristica = 2;  }
        if(tipoHeuristica == ETipoHeuristicas.Justification){                    tipo_heuristica = 3;  }
        
                          //Serviços de Inicialização, Escrita e Consulta
        if(tipo_agente == ServicoAgente.Inicializacao){                          tipoAgente = 1; }
        if(tipo_agente == ServicoAgente.Tree){                                   tipoAgente = 2; }
        
        if(operacao_agente == ETiposServicosAgentes.Consulta_Solucao){           opAgente = 1;   }
        if(operacao_agente == ETiposServicosAgentes.Escrever_Solucacao){         opAgente = 2;   }
        
        if(operacao_servidor == ETiposServicosServidor.Inserir_Solucao){         opServidor = 1; }
        if(operacao_servidor == ETiposServicosServidor.AtualizaSolucao){         opServidor = 2; }
        if(operacao_servidor == ETiposServicosServidor.MelhorSolucao){           opServidor = 3; }
        if(operacao_servidor == ETiposServicosServidor.PiorSolucao){             opServidor = 4; }
        if(operacao_servidor == ETiposServicosServidor.Solucao_Aleatoria){       opServidor = 5; }
        
        if(rotaciona)
            rotacion = 1;
        else 
            rotacion = 0;
        
        if(firstfit)
            firstFit = 1;
        else 
            firstFit = 0;
        
        String args[] = {Integer.toString(tipo_heuristica),Integer.toString(tipoAgente),Integer.toString(opAgente),
                         Integer.toString(opServidor), Integer.toString(porta),Integer.toString(rotacion), Integer.toString(firstFit)};
        
        /************************ HEURÍSTICAS ÁRVORE NÁRIA ************************************/
        if(tipoHeuristica == ETipoHeuristicas.FirstFit){
            AgentBasedTree.main(args);
        }
        if(tipoHeuristica == ETipoHeuristicas.BestFit){
            AgentBasedTree.main(args);
        }
        if(tipoHeuristica == ETipoHeuristicas.Justification){
            AgentBasedTree.main(args);
        }
    }
    
    //######################################### AGENTE RELACIONADO AO GRASP ############################################################### 
    
    public void iniciaClientesGRASP(ETipoHeuristicas tipoHeuristica, ServicoAgente tipo_agente, ETiposServicosAgentes operacao_agente, 
                                                      ETiposServicosServidor operacao_servidor, int porta, double alpha,int maxit,
                                                      PoliticaSelecao politicaSelecaoSolucao, PoliticaSelecao polSelecaoplaca,
                                                      float aproveitamento, int typeGrasp) 
                                                      throws FileNotFoundException, IOException, PecaInvalidaException,Exception{
         
        int tipoGrasp = 0, tipoAgente = 0, opAgente = 0, opServidor = 0, portaCom = porta, tipo_heuristica = 0;
        int polSelecao_Solucao = 0, polSelecao_Placa = 0;
        float aproveit = 0;
        
        
                           //Tipos de Heurísticas
        if(tipoHeuristica == ETipoHeuristicas.GRASP_2D_CONSTRUTIVO ){            tipo_heuristica = 4;  }
        if(tipoHeuristica == ETipoHeuristicas.Grasp2d_Melhoria ){                tipo_heuristica = 5;  }
        if(tipoHeuristica == ETipoHeuristicas.Grasp2dTree_Melhoria){             tipo_heuristica = 6;  }
                                   
                          //Tipo de Serviço
        if(tipo_agente == ServicoAgente.Inicializacao){                          tipoAgente = 5; }
        if(tipo_agente == ServicoAgente.GRASP_Melhoria){                         tipoAgente = 6; }
                
                          //Serviços de Inicialização, Escrita e Consulta
        if(operacao_agente == ETiposServicosAgentes.Inicializacao_Grasp){        opAgente = 1; }
        if(operacao_agente == ETiposServicosAgentes.Inicializacao_Aleatoria){    opAgente = 3; }
        if(operacao_agente == ETiposServicosAgentes.Consulta_Solucao){           opAgente = 4; }
        if(operacao_agente == ETiposServicosAgentes.Escrever_Solucacao){         opAgente = 5; }
                  
                         
                        //Serviços do Servidor
        if(operacao_servidor == ETiposServicosServidor.Inserir_Solucao){         opServidor = 1; }
        if(operacao_servidor == ETiposServicosServidor.AtualizaSolucao){         opServidor = 2; }
        if(operacao_servidor == ETiposServicosServidor.MelhorSolucao)  {         opServidor = 3; }
        if(operacao_servidor == ETiposServicosServidor.PiorSolucao)    {         opServidor = 4; }
        if(operacao_servidor == ETiposServicosServidor.Solucao_Aleatoria){       opServidor = 5; }
        if(operacao_servidor == ETiposServicosServidor.Roleta){                  opServidor = 6; }
        
                        //Serviços de Seleção das Soluções
        if(politicaSelecaoSolucao == null){                                      polSelecao_Solucao = 0;}
        if(politicaSelecaoSolucao == PoliticaSelecao.Melhor){                    polSelecao_Solucao = 1;}
        if(politicaSelecaoSolucao == PoliticaSelecao.ProbabilidadeMelhor){       polSelecao_Solucao = 2;}
        if(politicaSelecaoSolucao == PoliticaSelecao.Aleatorio){                 polSelecao_Solucao = 3;}
        if(politicaSelecaoSolucao == PoliticaSelecao.Pior){                      polSelecao_Solucao = 4;}
        if(politicaSelecaoSolucao == PoliticaSelecao.ProbabilidadeMenor){        polSelecao_Solucao = 5;}
                                
                       //Serviços de Seleção das Placas
        if(politicaSelecaoSolucao == null){                                      polSelecao_Placa = 0;}
        if(politicaSelecaoSolucao == PoliticaSelecao.MelhorMelhor){              polSelecao_Placa = 1;}
        if(politicaSelecaoSolucao == PoliticaSelecao.MelhorPior){                polSelecao_Placa = 2;}
        if(politicaSelecaoSolucao == PoliticaSelecao.MelhorAleatorio){           polSelecao_Placa = 3;}
        if(politicaSelecaoSolucao == PoliticaSelecao.PiorAleatorio){             polSelecao_Placa = 4;}
        
        aproveit   = aproveitamento;
        tipoGrasp  = typeGrasp;
        
        String args[] = {Integer.toString(tipo_heuristica),Integer.toString(tipoAgente),Integer.toString(opAgente),
                         Integer.toString(opServidor), Integer.toString(porta),Double.toString(alpha), Integer.toString(maxit),
                         Integer.toString(polSelecao_Solucao), Integer.toString(polSelecao_Placa), Float.toString(aproveit),
                         Integer.toString(tipoGrasp)};
        
        /*************************** HEURÍSTICAS GRASP *****************************************************************************/
        
        if(tipoHeuristica == ETipoHeuristicas.GRASP_2D_CONSTRUTIVO){
            HeuristicaGRASP.main(args);
        }
        if(tipoHeuristica == ETipoHeuristicas.Grasp2d_Melhoria){
            HeuristicaGRASP.main(args);
        }
        
        if(tipoHeuristica == ETipoHeuristicas.Grasp2dTree_Melhoria){       
            HeuristicaGRASP.main(args);
        }
    }
    
    //######################################### AGENTE RELACIONADO AO SIMULA GENÉTICO #####################################################
    
    public void iniciaClientesSimulaGenetico(ETipoHeuristicas tipoHeuristica, ServicoAgente tipo_agente, 
                                                                      ETiposServicosAgentes operacao_agente, 
                                                                      ETiposServicosServidor operacao_servidor,
                                                                      ETiposServicosAgentes operacao_agenteM2,
                                                                      PoliticaSelecao politicaSelecaoSolucao,
                                                                      int portaM1, int portaM2, boolean rotaciona, boolean firstfit)
                                                                      throws FileNotFoundException, IOException, PecaInvalidaException,Exception{
    
        int tipo_heuristica = 0, tipoAgente = 0, opAgente = 0, opAgente2 = 0, opServidor = 0, porta1 = portaM1,porta2 = portaM2;
        int polSelecao_Solucao  = 0;
        int rotacione,eFirstFit = 0;
                                        
                                //Tipos de Heurísticas
        if(tipoHeuristica == ETipoHeuristicas.SimulaGenetico){                   tipo_heuristica = 1;  }
    
                                //Tipo de Serviço
        if(tipo_agente == ServicoAgente.Alocacao){                               tipoAgente = 1; }
        if(tipo_agente == ServicoAgente.Combinacao){                             tipoAgente = 2; }
        if(tipo_agente == ServicoAgente.Permutacao){                             tipoAgente = 3; }
        
                                //Serviços de Inicialização, Escrita e Consulta
        if(operacao_agente == ETiposServicosAgentes.Consulta_Solucao){              opAgente = 1; }
        if(operacao_agente == ETiposServicosAgentes.Escrever_Solucacao){            opAgente = 2; }
        
                                //Serviços do Simula Genético Permutação 
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_1){        opAgente2 = 1 ;}
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_2){        opAgente2 = 2 ;}
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_3){        opAgente2 = 3 ;}
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_4){        opAgente2 = 4 ;}
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_5){        opAgente2 = 5 ;}
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_6){        opAgente2 = 6 ;}
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_7){        opAgente2 = 7 ;}
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_permutacao_8){        opAgente2 = 8 ;}
                               
                                //Serviços do Simula Genético Alocação
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_1){          opAgente2 = 9 ; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_2){          opAgente2 = 10; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_3){          opAgente2 = 11; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_4){          opAgente2 = 12; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_5){          opAgente2 = 13; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_6){          opAgente2 = 14; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_7){          opAgente2 = 15; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_8){          opAgente2 = 16; }
        if(operacao_agenteM2 == ETiposServicosAgentes.agente_alocacao_9){          opAgente2 = 17; }
                         
                                //Serviços do Simula Genético Combinação
        if(operacao_agenteM2 == ETiposServicosAgentes.TotalmenteAleatorioSolucao){ opAgente2 = 18; }
        if(operacao_agenteM2 == ETiposServicosAgentes.AleatorioPior){              opAgente2 = 19; }
        if(operacao_agenteM2 == ETiposServicosAgentes.AleatorioMelhor){            opAgente2 = 20; }
        if(operacao_agenteM2 == ETiposServicosAgentes.Melhor_Dois){                opAgente2 = 21; }
        if(operacao_agenteM2 == ETiposServicosAgentes.Melhor_Pior){                opAgente2 = 22; }
        if(operacao_agenteM2 == ETiposServicosAgentes.MelhorSol_Aleatorio){        opAgente2 = 23; }
        if(operacao_agenteM2 == ETiposServicosAgentes.MelhorSol_MelhorIndividuo){  opAgente2 = 24; }
        if(operacao_agenteM2 == ETiposServicosAgentes.AleatorioMelhorS){           opAgente2 = 25; }
        if(operacao_agenteM2 == ETiposServicosAgentes.MelhorSol_Melhor_DoisIndv){  opAgente2 = 26; }
        if(operacao_agenteM2 == ETiposServicosAgentes.MelhorSol_Maior_MenorIndv){  opAgente2 = 27; }
                
                                //Serviços do Simula Genético Combinação
        if(operacao_servidor == ETiposServicosServidor.Inserir_Solucao){         opServidor = 1; }
        if(operacao_servidor == ETiposServicosServidor.AtualizaSolucao){         opServidor = 2; }
        if(operacao_servidor == ETiposServicosServidor.MelhorSolucao)  {         opServidor = 3; }
        if(operacao_servidor == ETiposServicosServidor.PiorSolucao)    {         opServidor = 4; }
        if(operacao_servidor == ETiposServicosServidor.Solucao_Aleatoria){       opServidor = 5; }
        if(operacao_servidor == ETiposServicosServidor.Roleta){                  opServidor = 6; }
        
                                //Serviços de Seleção das Soluções
        if(politicaSelecaoSolucao == null){                                      polSelecao_Solucao = 0;}
        if(politicaSelecaoSolucao == PoliticaSelecao.Melhor){                    polSelecao_Solucao = 1;}
        if(politicaSelecaoSolucao == PoliticaSelecao.ProbabilidadeMelhor){       polSelecao_Solucao = 2;}
        if(politicaSelecaoSolucao == PoliticaSelecao.Aleatorio){                 polSelecao_Solucao = 3;}
        if(politicaSelecaoSolucao == PoliticaSelecao.Pior){                      polSelecao_Solucao = 4;}
        if(politicaSelecaoSolucao == PoliticaSelecao.ProbabilidadeMenor){        polSelecao_Solucao = 5;}

        if(rotaciona)
            rotacione = 1;
        else 
            rotacione = 0;
        
        if(firstfit)
            eFirstFit = 1;
        else 
            eFirstFit = 0;
       
        String args[] = {Integer.toString(tipo_heuristica),Integer.toString(tipoAgente),Integer.toString(opAgente),
                         Integer.toString(opServidor), Integer.toString(opAgente2), Integer.toString(polSelecao_Solucao),
                         Integer.toString(porta1),Integer.toString(porta2), Integer.toString(rotacione), Integer.toString(eFirstFit)};
    
        /************************ HEURÍSTICAS SIMULA GENÉTICO ********************************/
        
        if(tipoHeuristica == ETipoHeuristicas.SimulaGenetico){
        
            if(tipo_agente == ServicoAgente.Alocacao){
            
                AgentesAlocacao_E_Permutacao.main(args);
            }
            if(tipo_agente == ServicoAgente.Permutacao){
            
                AgentesAlocacao_E_Permutacao.main(args);
            }
            if(tipo_agente == ServicoAgente.Combinacao){
            
                AgentesCombinacao.main(args);
            }
        }
    }
    
    public void iniciaClienteBRKGA(){ /* ?????????? */}    
    
/*####################################################################################################################################*/
    
    public static void main(String args[]) throws IOException, FileNotFoundException, PecaInvalidaException, Exception{
    
        int iteracoes = 1,contador = 2;
        IniciaClientes ic = new IniciaClientes();
        
                                              //ALGORITMOS DE INICIALIZAÇÃO DA MEMÓRIA 1
        
//      ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic, ServicoAgente.Inicializacao, 
//                                                    ETiposServicosAgentes.Inicializacao_HHDHeuristic,
//                                                    ETiposServicosServidor.Inserir_Solucao, 2525, PoliticaSelecao.Melhor,
//                                                    PoliticaSelecao.MelhorPior, 0.166, 80, 1, null);
        
        ic.iniciaClientesGRASP(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO, ServicoAgente.Inicializacao,
                                                    ETiposServicosAgentes.Inicializacao_Grasp,
                                                    ETiposServicosServidor.Inserir_Solucao, 2525, 0.8, 10,
                                                    null, null,80,1);
        ic.iniciaClientesGRASP(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO, ServicoAgente.Inicializacao,
                                                    ETiposServicosAgentes.Inicializacao_Grasp,
                                                    ETiposServicosServidor.Inserir_Solucao, 2525, 0.25, 10,
                                                    null, null,80,1);
       
        ic.iniciaClientesGRASP(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO, ServicoAgente.Inicializacao,
                                                    ETiposServicosAgentes.Inicializacao_Grasp,
                                                    ETiposServicosServidor.Inserir_Solucao, 2525, 0.5, 10,
                                                    null, null,80,1);
         ic.iniciaClientesGRASP(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO, ServicoAgente.Inicializacao,
                                                    ETiposServicosAgentes.Inicializacao_Grasp,
                                                    ETiposServicosServidor.Inserir_Solucao, 2525, 0.75, 10,
                                                    null, null,80,1);
          ic.iniciaClientesGRASP(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO, ServicoAgente.Inicializacao,
                                                    ETiposServicosAgentes.Inicializacao_Grasp,
                                                    ETiposServicosServidor.Inserir_Solucao, 2525, 1,10,
                                                    null, null,80,1);     

                                           //ALGORITMOS DE MELHORIA DA MEMÓRIA 1
        
//       ################################################################3
//**        
//        ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.MelhorSolucao, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.08, 96, 3, EstrategiaBinPackTree.OneSolutionHHD);
//**      
//        ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.Solucao_Aleatoria, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.06, 96, 3, EstrategiaBinPackTree.OneSolutionHHD);
        
//        ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.PiorSolucao, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.06, 96, 3, EstrategiaBinPackTree.OneSolutionHHD);
//
          
/*
ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.MelhorSolucao, 2525, PoliticaSelecao.Melhor,
                PoliticaSelecao.MelhorPior, 0.04, 96, 4, EstrategiaBinPackTree.OneSolutionHHD);
       
          ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.Solucao_Aleatoria, 2525, PoliticaSelecao.Melhor,
                PoliticaSelecao.MelhorPior, 0.09, 96, 4, EstrategiaBinPackTree.OneSolutionHHD);
          
          ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.PiorSolucao, 2525, PoliticaSelecao.Melhor,
                PoliticaSelecao.MelhorPior, 0.09, 96, 4, EstrategiaBinPackTree.OneSolutionHHD);

*/
//*****          
//          ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.MelhorSolucao, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.01, 95, 4, EstrategiaBinPackTree.OneSolutionHHD);
//       
//          ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.Solucao_Aleatoria, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.02, 90, 4, EstrategiaBinPackTree.OneSolutionHHD);
//          
//          ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.PiorSolucao, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.01, 90, 4, EstrategiaBinPackTree.OneSolutionHHD);
        
          
//        ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria_Tree, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.Solucao_Aleatoria, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.09, 96, 4, EstrategiaBinPackTree.OneSolutionHHD);
        
//        ################################################################3
                
                
//        ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.MelhorSolucao, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.1, 99, 2, null);
//        
//        ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.Solucao_Aleatoria, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.166, 96, 2, null);
//        
//        ic.iniciaClienteHHDHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria, ServicoAgente.HHDHeuristic_Melhoria, 
//                ETiposServicosAgentes.Consulta_Solucao, ETiposServicosServidor.MelhorSolucao, 2525, PoliticaSelecao.Melhor,
//                PoliticaSelecao.MelhorPior, 0.166,80, 1, null);
        
        //AQUI !!!
/*        ic.iniciaClientesGRASP(ETipoHeuristicas.Grasp2d_Melhoria, ServicoAgente.GRASP_Melhoria, ETiposServicosAgentes.Consulta_Solucao,
                                                    ETiposServicosServidor.PiorSolucao, 2525, 0.1, 1, 
                                                    PoliticaSelecao.Pior, PoliticaSelecao.MelhorPior, 95, 1);
 
        ic.iniciaClientesGRASP(ETipoHeuristicas.Grasp2d_Melhoria, ServicoAgente.GRASP_Melhoria, ETiposServicosAgentes.Consulta_Solucao,
                                                    ETiposServicosServidor.MelhorSolucao, 2525, 0.25, 4, 
                                                    PoliticaSelecao.Pior, PoliticaSelecao.MelhorPior, 96, 2);
      

        ic.iniciaClientesGRASP(ETipoHeuristicas.Grasp2d_Melhoria, ServicoAgente.GRASP_Melhoria, ETiposServicosAgentes.Consulta_Solucao,
                                                    ETiposServicosServidor.Solucao_Aleatoria, 2525, 0.25, 4, 
                                                    PoliticaSelecao.Pior, PoliticaSelecao.MelhorPior, 97, 2);
*/
////        
//        ic.iniciaClientesGRASP(ETipoHeuristicas.Grasp2d_Melhoria, ServicoAgente.GRASP_Melhoria, ETiposServicosAgentes.Consulta_Solucao,
//                                                    ETiposServicosServidor.MelhorSolucao, 2525, 0.3, 1, 
//                                                    PoliticaSelecao.Pior, PoliticaSelecao.MelhorPior, 97, 2);
//        
        ic.iniciaClientesGRASP(ETipoHeuristicas.Grasp2d_Melhoria, ServicoAgente.GRASP_Melhoria, ETiposServicosAgentes.Consulta_Solucao,
                                                    ETiposServicosServidor.PiorSolucao, 2525, 0.2, 2, 
                                                    PoliticaSelecao.Pior, PoliticaSelecao.MelhorPior, 98, 2);
         
        ic.iniciaClientesGRASP(ETipoHeuristicas.Grasp2d_Melhoria, ServicoAgente.GRASP_Melhoria, ETiposServicosAgentes.Consulta_Solucao,
                                                    ETiposServicosServidor.MelhorSolucao, 2525, 0.2, 3, 
                                                    PoliticaSelecao.Pior, PoliticaSelecao.MelhorPior, 99, 2); 
        
        ic.iniciaClientesGRASP(ETipoHeuristicas.Grasp2d_Melhoria, ServicoAgente.GRASP_Melhoria, ETiposServicosAgentes.Consulta_Solucao,
                                                    ETiposServicosServidor.MelhorSolucao, 2525, 0.1, 3, 
                                                    PoliticaSelecao.Pior, PoliticaSelecao.MelhorPior, 99, 2);
                 
        
        new Scanner(System.in).hasNextLine();
            stop = true;
    }

    public static boolean  stop = false;
    
} 