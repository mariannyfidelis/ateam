package AgCombinacao;

import ComunicaoConcorrenteParalela.ObjetoComunicacao;
import ComunicaoConcorrenteParalela.ServicoAgente;
import Heuristicas.Individuo;
import Heuristicas.Solucao;
import Heuristicas.Util;
import Utilidades.Funcoes;
import algoritmosAgCombinacao.OperacoesSolucoes_Individuos;
import j_HeuristicaArvoreNAria.Pedidos;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AgentesAlocacao_E_Permutacao extends Agentes{
    
    
    //Geração de números aleatórios
    SecureRandom r = new SecureRandom();
    
    //Deve existir uma lista com as listas/individuos que extrapolam o limite da placa
    List<Individuo> list_ind_extrap;
    List<Integer> list_excedente;
    
    public AgentesAlocacao_E_Permutacao(int porta_comunicao, String name, ServicoAgente servico_, ETiposServicosAgentes operacaoAgente){
        
        this.porta_comunicacao = porta_comunicao;
        this.name = name;
        this.tipo_agente = servico_;
        this.operacao_agente = operacaoAgente;
        //run();
    }
    
    /*########################################################################################
              MÉTODOS COMPLEMENTARES AOS FUNCIONAMENTO DOS AGENTES 
   ########################################################################################*/
    
    public void execute(ServicoAgente servicoAgente, ETiposServicosAgentes tipo_servico) {
       
       ObjetoComunicacao objeto;
       
       //Pega os dados do InputStream do Socket      
       //Processa informação do socket de entrada e processa informação
       objeto = processaInformacao(lerSocketChannel(socketChannel, null),servicoAgente, tipo_servico);
  
       //Escrever o resultado para o socketChannel
       //Após o processamento é realizado novamente a serialização e enviar ao socket de saída
       escreveSocketChannel(serializaMensagem(objeto), socketChannel);
                       
       //Verificar se está implementado nos métodos
       //flush();
       //read.close();
       //write.close();
    }    
    
    public ObjetoComunicacao processaInformacao(ByteBuffer byteBuffer,ServicoAgente servico, ETiposServicosAgentes tipo) throws IllegalArgumentException{

      Solucao solucao = new Solucao();
      ObjetoComunicacao objeto = new ObjetoComunicacao();
      objeto = deserializaMensagem(byteBuffer);
      
      if(servico.equals(ServicoAgente.Alocacao)){
          
          switch(tipo){

             case agente_alocacao_1:
                  solucao = agente_alocacao_1(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;

             case agente_alocacao_2:
                  solucao = agente_alocacao_2(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;

             case agente_alocacao_3:
                  solucao = agente_alocacao_3(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;

             case agente_alocacao_4: break;

             case agente_alocacao_5:
                  solucao = agente_alocacao_5(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;

             case agente_alocacao_6:
                  solucao = agente_alocacao_6(objeto.getSolucao());  
                  objeto.setSolucao(solucao);
             break;

             case agente_alocacao_7:
                  solucao = agente_alocacao_7(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;

             case agente_alocacao_8: break;

             case agente_alocacao_9:
                  solucao = agente_alocacao_9(objeto.getSolucao()); 
                  objeto.setSolucao(solucao);
             break;
          }
         return objeto;
      }
      else if(servico.equals(ServicoAgente.Permutacao)){
      
          switch(tipo){

             case agente_permutacao_1:
                  solucao = agente_permutacao_1(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;
                 
             case agente_permutacao_2:
                  solucao = agente_permutacao_2(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;
                 
             case agente_permutacao_3:
                  solucao = agente_permutacao_3(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;
                 
             case agente_permutacao_4:
                  solucao = agente_permutacao_4(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;
                 
             case agente_permutacao_5: break;
                 
             case agente_permutacao_6:
                  solucao = agente_permutacao_6(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;
                 
             case agente_permutacao_7: break;
                 
             case agente_permutacao_8:
                  solucao = agente_permutacao_8(objeto.getSolucao());
                  objeto.setSolucao(solucao);
             break;
          }
          
          return objeto;
      }
      else{}
      
     return objeto; 
   }
    
   //Verificar a quantidade de indivíduos com a capacidade não respeitada
   public int verificaCapacidadeNRespeitada(Solucao solucao_meSol){
    
        int cont = 0;
        Individuo individuo;
        
        list_ind_extrap = new ArrayList<Individuo>();
        Iterator<Individuo> iterator_ind = solucao_meSol.getLista().iterator();
        
        while(iterator_ind.hasNext()){
            individuo = iterator_ind.next();
            
            if(individuo.isCapacidadeRespeitada() == false){
                this.list_ind_extrap.add(individuo);
                cont++;
            }else
                break;
        }
    
        return cont;
    }
    //Solucao deve estar ordenada de forma Decrescente !!!
    public Individuo selecionaRandomicamenteListaNrespeitada(Solucao solucao){
    
       int cont = verificaCapacidadeNRespeitada(solucao);
      
       return solucao.getLista().get(r.nextInt(cont));
    }
    
    public Individuo selecionaRandomicamenteListaRespeitada(Solucao solucao){
    
       int cont = verificaCapacidadeNRespeitada(solucao);
      
       return solucao.getLista().get(Funcoes.aleatorio_entre_Intervalo(cont, solucao.size()));
    }
    
    //Retorna próxima sublista da capacidade Respeitada
    public Individuo selecionaProximaListaRespeitada(Solucao solucao){
    
        int cont = verificaCapacidadeNRespeitada(solucao);
        
        return solucao.getLista().get(cont);
    }
    
    //Colocar todos os indivíduos que extrapolam em uma lista
    public List<Integer> listIndividuosExtrapolam(Individuo list_ind){
    
        //É necessário a priori o cálculo do excedente
        int item;
        Integer integer;
        
        double d = list_ind.getFitness();
        Pedidos[] pedidos = Funcoes.getVetor_info();
        List<Integer> list_int = new ArrayList<Integer>();
        
        if(d < 0){
            Iterator<Integer> list_itens = list_ind.IteratorListaItens();

            while(list_itens.hasNext()){
                 
                integer = list_itens.next();
                item = integer.intValue();
                
                //Se a área do item é maior ou igual ao o excedente, netão adiciona a lista !!!
                if(pedidos[item].retornaArea() >= (d * (-1))){
                    list_int.add(integer);
                }
            }
        }
        
        return list_int;
    }    
    
   /*########################################################################################
              MÉTODOS DE ALOCAÇÃO PARA AGENTES EXECUTAREM 
   ########################################################################################*/
    
   public Solucao agente_alocacao_1(Solucao solucao_memoria){
             
        int item;
        Integer item_selecionado;
        
        this.solucao.setSolucao(solucao_memoria);
        
        //Selecione randomicamente uma sublista com restrição de capacidade não respeitada
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Gera a partir  dessa sublista/individuo com um conjunto de itens que seja maior ou igual ao excedentes
        
        list_excedente = new ArrayList<Integer>(listIndividuosExtrapolam(individuo1));
        
        Funcoes.ordenaDecrescenteListaItens((ArrayList<Integer>) list_excedente);
        
        //Seleciona no conjunto gerado, o item de menor tamanho seria o último
        //Se estiver ordenado de forma decrescente por área !!!
        item_selecionado = list_excedente.get(list_excedente.size() - 1);
        
        //Seleciona a sublista/individuo de menor ocupação
        
        //OBS: Seria interessante ordenar a nova solução aqui e passar ao próximo método !!!
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        individuo1.removeItemLista(item_selecionado,  Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        individuo2.adicionaItemLista(item_selecionado,Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        
        //É necessárioa a realização dos cálculos das propriedades dos indivíduos alterados !!!
        
        //Calcular Somatório de itens
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());

        //Calcular o fitness
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
        return this.solucao;
    }
    
    public Solucao agente_alocacao_2(Solucao solucao_memoria){
    
        int item;
        Integer item_selecionado;
        
        this.solucao.setSolucao(solucao_memoria);
        
        //Selecione randomicamente uma sublista com restrição de capacidade não respeitada
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        //Gera a partir  dessa sublista/individuo com um conjunto de itens que seja maior ou igual ao excedentes
        
        list_excedente = new ArrayList<Integer>(listIndividuosExtrapolam(individuo1));
        
        Funcoes.ordenaDecrescenteListaItens((ArrayList<Integer>) list_excedente);
        
        //Seleciona no conjunto gerado, o item de menor tamanho seria o último
        //Se estiver ordenado de forma decrescente por área !!!
        item_selecionado = list_excedente.get(list_excedente.size() - 1);
        
        //Seleciona randomicamente, dentre todas as sublistas, outra sublista com restrição respeitada
        individuo2 = selecionaRandomicamenteListaRespeitada(this.solucao);        
        this.solucao.removeIndividuos(individuo2);
        
        //Move o item para a nova sublista selecionada !!!
        individuo1.removeItemLista(item_selecionado,  Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        individuo2.adicionaItemLista(item_selecionado,Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        
        //É necessárioa a realização dos cálculos das propriedades dos indivíduos alterados !!!
        
        //Calcular Somatório de itens
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());

        //Calcular o fitness
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
        return this.solucao;
    }
    
    public Solucao agente_alocacao_3(Solucao solucao_memoria){
        
        int item;
        Integer item_selecionado;
        
        this.solucao.setSolucao(solucao_memoria);
        
        //Selecione randomicamente uma sublista com restrição de capacidade não respeitada
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        //Gera a partir  dessa sublista/individuo com um conjunto de itens que seja maior ou igual ao excedentes
        
        list_excedente = new ArrayList<Integer>(listIndividuosExtrapolam(individuo1));
        
        Funcoes.ordenaDecrescenteListaItens((ArrayList<Integer>) list_excedente);
        
        //Seleciona no conjunto gerado, o item de menor tamanho seria o último
        //Se estiver ordenado de forma decrescente por área !!!
        item_selecionado = list_excedente.get(list_excedente.size() - 1);
        
        individuo2 = selecionaProximaListaRespeitada(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        //Move o item para a nova sublista selecionada !!!
        individuo1.removeItemLista(item_selecionado,  Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        individuo2.adicionaItemLista(item_selecionado,Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        
        //É necessárioa a realização dos cálculos das propriedades dos indivíduos alterados !!!
        
        //Calcular Somatório de itens
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());

        //Calcular o fitness
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
        return this.solucao;
    }
    
    public void agente_alocacao_4(Solucao solucao_memoria){/*Não escolhi essa !!!*/}
        
    public Solucao agente_alocacao_5(Solucao solucao_memoria){
    
        int item;
        Integer item_selecionado;
        
        this.solucao.setSolucao(solucao_memoria);
        
        //Selecione randomicamente uma sublista com restrição de capacidade não respeitada
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        list_excedente = new ArrayList<Integer>(listIndividuosExtrapolam(individuo1));
        
        //Seleciona randomicamente um item da lista
        item_selecionado = list_excedente.get(r.nextInt(list_excedente.size()));
        
        //Seleciona a sublista/individuo de menor ocupação
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        individuo1.removeItemLista(item_selecionado,  Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        individuo2.adicionaItemLista(item_selecionado,Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        
        //É necessárioa a realização dos cálculos das propriedades dos indivíduos alterados !!!
        
        //Calcular Somatório de itens
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());

        //Calcular o fitness
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
        return this.solucao;
    }
    
    public Solucao agente_alocacao_6(Solucao solucao_memoria){
    
        int item;
        Integer item_selecionado;
        
        
        this.solucao.setSolucao(solucao_memoria);
        
        //Selecione randomicamente uma sublista com restrição de capacidade não respeitada
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        list_excedente = new ArrayList<Integer>(listIndividuosExtrapolam(individuo1));
        
        //Seleciona randomicamente um item da lista
        item_selecionado = list_excedente.get(r.nextInt(list_excedente.size()));
        
        //Seleciona randomicamente, dentre todas as sublistas, outra sublista com restrição respeitada
        individuo2 = selecionaRandomicamenteListaRespeitada(this.solucao);        
        this.solucao.removeIndividuos(individuo2);
        
        //Move o item para a nova sublista selecionada !!!
        individuo1.removeItemLista(item_selecionado,  Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        individuo2.adicionaItemLista(item_selecionado,Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        
        //É necessárioa a realização dos cálculos das propriedades dos indivíduos alterados !!!
        
        //Calcular Somatório de itens
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());

        //Calcular o fitness
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
        return this.solucao;
    }
    
    public Solucao agente_alocacao_7(Solucao solucao_memoria){
    
        int item;
        Integer item_selecionado;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Selecione randomicamente uma sublista com restrição de capacidade não respeitada
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        list_excedente = new ArrayList<Integer>(listIndividuosExtrapolam(individuo1));
        
        //Seleciona randomicamente um item da lista
        item_selecionado = list_excedente.get(r.nextInt(list_excedente.size()));
        
        //Seleciona a próxima sublista com restrição de capacidade respeitada !!!
        individuo2 = selecionaProximaListaRespeitada(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        //Move o item para a nova sublista selecionada !!!
        individuo1.removeItemLista(item_selecionado,  Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        individuo2.adicionaItemLista(item_selecionado,Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        
        //É necessárioa a realização dos cálculos das propriedades dos indivíduos alterados !!!
        
        //Calcular Somatório de itens
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());

        //Calcular o fitness
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
        return this.solucao;
    }
    public void agente_alocacao_8(Solucao solucao_memoria){/*Não escolhi essa*/}
    
    public Solucao agente_alocacao_9(Solucao solucao_memoria){
    
        int item;
        Integer item_selecionado;
        
        
        this.solucao.setSolucao(solucao_memoria);
        //Selecione randomicamente uma sublista com restrição de capacidade não respeitada
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        //Gera a partir  dessa sublista/individuo com um conjunto de itens que seja maior ou igual ao excedentes
        
        list_excedente = new ArrayList<Integer>(listIndividuosExtrapolam(individuo1));
        
        Funcoes.ordenaDecrescenteListaItens((ArrayList<Integer>) list_excedente);
        
        //Seleciona no conjunto gerado, o item de menor tamanho seria o último
        //Se estiver ordenado de forma decrescente por área !!!
        item_selecionado = list_excedente.get(list_excedente.size() - 1);
        
        //Seleciona a sublista/individuo de menor ocupação
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        
        /*Seria interessante apagar esse item dessa memória e inserir no final*/
        individuo1.removeItemLista(item_selecionado,  Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        individuo2.adicionaItemLista(item_selecionado,Funcoes.getVetor_info()[item_selecionado.intValue()].retornaArea());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
        return this.solucao;
    }
    
/*########################################################################################
          MÉTODOS DE PERMUTAÇÃO PARA AGENTES EXECUTAREM 
########################################################################################*/
        
    public Solucao agente_permutacao_1(Solucao solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona nesta sublista, o item de menor tamanho
        item1 = Util.selecionaMenorItem((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona dentre todas as sublistas, a que possui a menor ocupação
        individuo2 = OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        //Seleciona nessa sublista, o item de menor tamanho
        item2 = Util.selecionaMenorItem((ArrayList<Integer>) individuo2.getListaItens());
        
        //Troca entre as duas sublistas, os itens selecionados
        
        individuo1.removeItemLista(item1,  Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        individuo2.removeItemLista(item2,  Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        
        individuo1.adicionaItemLista(item2,Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        individuo2.adicionaItemLista(item1,Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        
        
        //Calcular Somatório de itens
        
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());
        
        //Calcular o fitness
        
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());

        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
              
      return this.solucao;
    }
    
    public Solucao agente_permutacao_2(Solucao solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona nesta sublista, o item de maior tamanho
        item1 = Util.selecionaMaiorItem((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona dentre todas as sublistas, a que possui a menor ocupação
        individuo2 = OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        //Seleciona nessa sublista, o item de menor tamanho
        item2 = Util.selecionaMaiorItem((ArrayList<Integer>) individuo2.getListaItens());
        
        //Troca entre as duas sublistas, os itens selecionados
        
        individuo1.removeItemLista(item1,  Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        individuo2.removeItemLista(item2,  Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        
        individuo1.adicionaItemLista(item2,Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        individuo2.adicionaItemLista(item1,Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        
        //Calcular Somatório de itens
        
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());
        
        //Calcular o fitness
        
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());

        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
        
      
      return this.solucao;
        
    }
    
    public Solucao agente_permutacao_3(Solucao solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona nesta sublista, o item de maior tamanho
        item1 = Util.selecionaMaiorItem((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona dentre todas as sublistas, a que possui a menor ocupação
        individuo2 = OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        //Seleciona nessa sublista, o item de menor tamanho
        item2 = Util.selecionaMenorItem((ArrayList<Integer>) individuo2.getListaItens());
        
        //Troca entre as duas sublistas, os itens selecionados
        
        individuo1.removeItemLista(item1,  Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        individuo2.removeItemLista(item2,  Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        
        individuo1.adicionaItemLista(item2,Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        individuo2.adicionaItemLista(item1,Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        
        //Calcular Somatório de itens
        
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());
        
        //Calcular o fitness
        
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());

        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
              
      return this.solucao;
        
    }
    
    public Solucao agente_permutacao_4(Solucao solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona nesta sublista, o item de maior tamanho
        item1 = Util.selecionaMaiorItem((ArrayList<Integer>) individuo1.getListaItens());
        
        // Selecione dentre todas as sublistas, a que possui maior ocupação, porém com a restrição
        // de capacidade respeitada;
        individuo2 = selecionaProximaListaRespeitada(this.solucao);        
        this.solucao.removeIndividuos(individuo2);
    
        //Selecione nesta sublista, o item de maior tamanho
        item2 = Util.selecionaMaiorItem((ArrayList<Integer>) individuo2.getListaItens());
        
        //Troca entre as sublistas selecionadas, os itens selecionados
        individuo1.removeItemLista(item1,  Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        individuo2.removeItemLista(item2,  Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        
        individuo1.adicionaItemLista(item2,Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        individuo2.adicionaItemLista(item1,Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        
        //Calcular Somatório de itens
        
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());
        
        //Calcular o fitness
        
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());

        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
              
      return this.solucao;
    }
    
    public void agente_permutacao_5(Solucao solucao_memoria){/*Não implementei essa*/}
    
    public Solucao agente_permutacao_6(Solucao solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente duas sublistas
        
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        //Para cada sublista seleciona randomicamente um item
        item1 =  Util.selecionaItemAleatorio((ArrayList<Integer>) individuo1.getListaItens());
        item2 =  Util.selecionaItemAleatorio((ArrayList<Integer>) individuo2.getListaItens());
        
        //Troca entre as duas sublistas selecionadas, os itens selecionados
        individuo1.removeItemLista(item1,  Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        individuo2.removeItemLista(item2,  Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        
        individuo1.adicionaItemLista(item2,Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        individuo2.adicionaItemLista(item1,Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        
        //Calcular Somatório de itens
        
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());
        
        //Calcular o fitness
        
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());
        
        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
              
      return this.solucao;
    }
    
    public void agente_permutacao_7(Solucao solucao_memoria){/*Não escolhi essa*/}
    
    public Solucao agente_permutacao_8(Solucao solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona randomicamente um item desta sublista
        item1 = Util.selecionaItemAleatorio((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona randomicamente uma outra sublista
        individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
        this.solucao.removeIndividuos(individuo2);
        
        //Seleciona randomicamente um item desta sublista
        item2 = Util.selecionaItemAleatorio((ArrayList<Integer>) individuo2.getListaItens());
        
        //Troca entre as sublistas selecionadas, os itens selecionados
        individuo1.removeItemLista(item1,  Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        individuo2.removeItemLista(item2,  Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        
        individuo1.adicionaItemLista(item2,Funcoes.getVetor_info()[item2.intValue()].retornaArea());
        individuo2.adicionaItemLista(item1,Funcoes.getVetor_info()[item1.intValue()].retornaArea());
        
        //Calcular Somatório de itens
        
        individuo1.setSomatorioItens2(individuo1.calculaSomatorioItens());
        individuo2.setSomatorioItens2(individuo2.calculaSomatorioItens());
        
        //Calcular o fitness
        
        individuo1.setFitness();
        individuo2.setFitness();

        //Verifica se a capacida está sendo respeitada
        
        individuo1.setCapacidadeRespeitada(individuo1.calculaCapacidadePlaca());
        individuo2.setCapacidadeRespeitada(individuo2.calculaCapacidadePlaca());

        this.solucao.adicionaIndividuo(individuo1);
        this.solucao.adicionaIndividuo(individuo2);
              
      return this.solucao;
    }   
}