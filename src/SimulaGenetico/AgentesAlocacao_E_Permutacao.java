package SimulaGenetico;

import java.util.List;
import java.net.Socket;
import Heuristicas.Util;
import Utilidades.Funcoes;
import java.util.Iterator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import Heuristicas.Individuo;
import ATeam.ETipoHeuristicas;
import java.io.ObjectInputStream;
import java.security.SecureRandom;
import Utilidades.PoliticaSelecao;
import java.io.ObjectOutputStream;
import HHDInternal.SolucaoHeuristica;
import HeuristicaConstrutivaInicial.Bin;
import ComunicaoConcorrenteParalela.ServicoAgente;
import j_HeuristicaArvoreNAria.Metodos_heuristicos;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoTree;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;
import Utilidades.Chapa;

public class AgentesAlocacao_E_Permutacao extends Agentes implements Runnable{
    
    private Socket socketClientFinal;
    private ObjectInputStream     in;  //Entrada para leitura do socket
    private ObjectOutputStream   out; //Saída para escrita no socket        
    
    private boolean inicializado;
    private boolean executando;
    private Thread  thread;
    private String  name;
    private ETiposServicosAgentes  operacao_agente;    //Tipo de operação realizada pelo agente
    private ETiposServicosServidor operacao_servidor1; //Tipo de operação requerida da memória pelo agente 
    private ETiposServicosServidor operacao_servidor2;
    private ETipoHeuristicas       tipoheuristica;
    private int porta_comunicacao, porta_comunicacao2;
    private ServicoAgente          tipo_agente;    
    ObjetoComunicacaoMelhorado     objetoM1 = null;// = new ObjetoComunicacaoMelhorado();
    ObjetoComunicacaoTree          objetoM2 = null;
    
    PoliticaSelecao polSelecaoSolucao = null;
    SolucaoHeuristica solucao = new SolucaoHeuristica(); 
    //Geração de números aleatórios
    SecureRandom r = new SecureRandom();
    
    //Deve existir uma lista com as listas/individuos que extrapolam o limite da placa
    List<Individuo> list_ind_extrap;
    List<Integer> list_excedente;
    
    public AgentesAlocacao_E_Permutacao(int porta_comunicao, String name, ServicoAgente servico_, ETiposServicosAgentes operacaoAgente){
        
        this.porta_comunicacao_ = porta_comunicao;
        this.name_ = name;
        this.tipo_agente_ = servico_;
        this.operacao_agente_ = operacaoAgente;
    }
    
    public AgentesAlocacao_E_Permutacao(String name, String endereco, int porta1, int porta2, ETiposServicosAgentes eSAgn, 
                                                                                     ETiposServicosAgentes eSAgn2,
                                                                                     ETiposServicosServidor eSServ,
                                                                                     ServicoAgente servico_) throws Exception {
                
         this.name = name;
         this.porta_comunicacao  = porta1;
         this.porta_comunicacao2 = porta2;
         this.operacao_servidor1 = eSServ;
         this.operacao_agente   = eSAgn;
         this.tipo_agente = servico_;
         inicializado = false;
         executando = false;   
               
         objetoM1 = new ObjetoComunicacaoMelhorado();
         objetoM2 = new ObjetoComunicacaoTree();
         //open(endereco, porta1);
        
    }
    
    /*########################################################################################
              MÉTODOS RELACIOANDOS A CONEXÃO E OPERAÇÃO DOS AGENTES 
   ########################################################################################*/
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////                                                                                                    ////
    ////  Aqui implementa os métodos de simulação Cliente (open,start, close, send, receive e run() !!!     ////
    ////                                                                                                    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////      
          
    private void open(String endereco, int portacomunicacao) throws Exception{
  
        try{
          //Método open que estabelece conexão com o Servidor e obtem o InputStream e OutputStream da conexão;
          socketClientFinal = new Socket(endereco, portacomunicacao);
                    
          out = new ObjectOutputStream(socketClientFinal.getOutputStream());
          in  = new ObjectInputStream(socketClientFinal.getInputStream());
          
          inicializado = true;
        }
        catch(IOException e){
            System.out.println("Exceção IO no open do Agente Combinação");                    
            System.out.println(e);
            close();
        }
      }

    public void close(){//Libera os recursos alocados
      
        //Método close() para liberar recursos alocados de forma adequada.
        if(in != null){
            try{
                in.close();
            }
            catch(Exception e){
              System.out.println(e);
            }	
        }

        if(out != null){
            try{
              out.close();
            }
            catch(Exception e){
              System.out.println(e);
            }	
        }

        if(socketClientFinal != null){
            try{
              socketClientFinal.close();
            }
            catch(Exception e){
              System.out.println(e);
            }	
          }

          in  = null;
          out = null;
          socketClientFinal = null;

          inicializado = false;
          executando   = false;

          thread = null;
  }     
  
    public void start(){//Inicia a thread auxiliar, se possível. O objetoM1 cliente precisa estar inicializado e não pode está executando !
    
        if(!inicializado || executando){

          return;      
        }

        executando = true;
        thread = new Thread(this);
        thread.start();
    
    }
  
    public void stop() throws Exception{ //Para a thread auxiliar de forma adequada, encerrando todos os recursos alocados.
    
    executando = false;
    
    if(thread != null){
       
      thread.join();
    }
  }
  
    public boolean isExecutando(){
    
        return executando;
    }
  
    //Os dois métodos seguintes define a escrita e a leitura de operações nas memórias por parte dos Agentes !!!
    
    public void deveEnviar(ObjetoComunicacaoMelhorado obj){
    
        this.objetoM1 = obj;
    }
    
    public ObjetoComunicacaoMelhorado getDeveEnviar(){
    
        return this.objetoM1;
    }
    
    public ETipoHeuristicas getTipoHeuristica(){
    
        return this.tipoheuristica;
    }
    
    public void setTipoHeuristica(ETipoHeuristicas etipoheuristica){
    
        this.tipoheuristica = etipoheuristica;
    }
    
    public void send(ObjetoComunicacaoMelhorado obcom){ 
        
      try {
          //out.println(obcom.getMSGServicoAgente());
          out.writeObject(obcom);
          out.flush();
      } 
      catch (IOException ex) {
          
          System.out.println("Erro na Escrita do Objeto enviado pelo cliente");
          System.out.println(ex);
          
          close();
      }
    }
  
    public ObjetoComunicacaoMelhorado receive(){
  
        ObjetoComunicacaoMelhorado objetoComunicacao = null;
       
        try{
            objetoComunicacao = (ObjetoComunicacaoMelhorado) in.readObject();
        }
        catch(IOException ex){
            System.out.println("Erro de leitura no receive do CLiente !!!");
            System.out.println(ex);
            //receive();
        }
        catch(ClassNotFoundException exClass){
            System.out.println("Erro no receive do CLiente problema de conversão de classe !!!");
            System.out.println(exClass);
            
        }
      
        return objetoComunicacao;
    }
    
    public void send(ObjetoComunicacaoTree obcom){ 
    
        try {
            //out.println(obcom.getMSGServicoAgente());
            out.writeObject(obcom);
            out.flush();
        } 
        catch (IOException ex) {

            System.out.println("Erro na Entrada-Saida do Objeto enviado pelo cliente");
            System.out.println(ex);
            close();
        }
    }
  
    public ObjetoComunicacaoTree receiveT(){
  
        ObjetoComunicacaoTree objetoComunicacao = null;
       
        try{
            objetoComunicacao = (ObjetoComunicacaoTree) in.readObject();
        }
        catch(IOException ex){
            System.out.println("Erro de Entrada e saída no receive do CLiente !!!");
            System.out.println(ex);
        }
        catch(ClassNotFoundException exClass){
            System.out.println("Erro no receive do CLiente problema de conversão de classe !!!");
            System.out.println(exClass);
        }
      
        return objetoComunicacao;
    }
    
    public String getName(){
    
      return name;
    }
  
    public void setName(String name){
  
        this.name = name;    
    }
  
    public int getPortaComunicacao(){
    
        return porta_comunicacao;
    }
  
    public void setPortaComunicacao(int porta){    
    
        this.porta_comunicacao = porta;   
    }
  
    public ServicoAgente getServicoAgente(){
  
        return tipo_agente;
    }
  
    public void setServicoAgente(ServicoAgente servico){
  
        this.tipo_agente = servico;
    }
  
    public ETiposServicosAgentes getTipoServico(){
  
        return operacao_agente;
    }
    
    public void setTipoServico(ETiposServicosAgentes etipoServico){
      
        this.operacao_agente = etipoServico;
    }

    public ETiposServicosServidor getTipoServicoServidor(){

        return operacao_servidor1;
    }

    public void setTipoServicoServidor(ETiposServicosServidor etipoServicoServidor){

        this.operacao_servidor1 = etipoServicoServidor;
    }
  
    public ObjetoComunicacaoMelhorado getObjetoComunicacaoMelhorado() {
    
        return this.objetoM1;
    }
    
    public void setObjetoComunicacaoMelhorado(ObjetoComunicacaoMelhorado obj) {
    
        this.objetoM1.setMSGServicoAgente(obj.getMSGServicoAgente());
        this.objetoM1.setSolucao(obj.getSolucao());
        this.objetoM1.setTipoServicoAgente(obj.getTipoServicoAgente2());
        this.objetoM1.setTipoServicoServidor(obj.getTipoServicoServidor());
    }
            
    public ObjetoComunicacaoTree getObjetoComunicacaoTree() {
        
        return this.objetoM2;
    }
    
    public void setObjetoComunicacaoTree(ObjetoComunicacaoTree obj) {
    
        this.objetoM2.setMSGServicoAgente(obj.getMSGServicoAgente());
        this.objetoM2.setSolucao(obj.getSolucao());
        this.objetoM2.setTipoServicoAgente(obj.getTipoServicoAgente2());
        this.objetoM2.setTipoServicoServidor(obj.getTipoServicoServidor());
    }
            
    public PoliticaSelecao getPoliticaSelecaoSolucao(){
        return this.polSelecaoSolucao;
    }
    
    public void setPoliticaSelecaoSolucao(PoliticaSelecao polSolucao){
        this.polSelecaoSolucao = polSolucao;
    }
    
    public j_HeuristicaArvoreNAria.SolucaoNAria aplicaAGLArvoreN(SolucaoHeuristica solucao){
       
       float larg, alt;
       j_HeuristicaArvoreNAria.SolucaoNAria solucion = new j_HeuristicaArvoreNAria.SolucaoNAria();
       Metodos_heuristicos m = new Metodos_heuristicos();
       
       larg = solucao.getTamanhoChapa().retorneBase();
       alt = solucao.getTamanhoChapa().retorneAltura();
       
       
       //FFIH_BFIH(boolean rotaciona, boolean firstFit, LinkedList<Pedidos> listaPedidos, Chapa chapa
       //c = m.FFIH_BFIH(true,true, pedidos, Funcoes.chapa); 
       solucion = m.FFIH_BFIH(true, false, recebeIndividuo_ListPedidos(solucao), new Chapa(larg, alt));
       solucion.imprime_solucao();
       
       return solucion;
   } 
   
   //Esse método vai lá pra árvore N-aria
   public LinkedList<j_HeuristicaArvoreNAria.Pedidos> recebeIndividuo_ListPedidos(SolucaoHeuristica solucao){
   
       Integer refPedido;
       //Aqui deve-se selecionar a Lista de Objetos
       LinkedList<j_HeuristicaArvoreNAria.Pedidos> n_pedidos = 
                                    new LinkedList<j_HeuristicaArvoreNAria.Pedidos>();
       
       j_HeuristicaArvoreNAria.Pedidos pedidos_arvore;
       
       List<Integer> list_item = new ArrayList<Integer>();
       Iterator<Bin> iter_objeto = solucao.getObjetos().iterator();
            
        while(iter_objeto.hasNext()){
        
            Individuo ind =  new Individuo();
            ind = iter_objeto.next().getIndividuo();

            list_item = ind.getListaItens();
            
            Iterator<Integer> it_integer = list_item.iterator();
            
            while(it_integer.hasNext()){
            //for(int i = 0; i < list_item.size(); i++){
                
                //refPedido = list_item.get(i);
                refPedido = it_integer.next();
                //pedidos_arvore = (j_HeuristicaArvoreNAria.Pedidos) solucao.getIPedidos().get(refPedido.intValue());
                pedidos_arvore = solucao.retornaPedido(refPedido.intValue(), solucao.getIPedidos());
                
                n_pedidos.add(new j_HeuristicaArvoreNAria.Pedidos(pedidos_arvore));
            }
        }
        
        //Verificar se isso aqui funciona beleza ?!!!
        LinkedList<j_HeuristicaArvoreNAria.Pedidos> p = solucao.getIPedidos();
        j_HeuristicaArvoreNAria.Pedidos[] vetPedid = new j_HeuristicaArvoreNAria.Pedidos[p.size()];
       
        Utilidades.Funcoes.atribui_info(p, vetPedid);
        
        return n_pedidos;
       
       //Aqui transforma a lista de Individuos de cada objeto em uma ListPedidos
    }
    
    /*########################################################################################
              MÉTODOS COMPLEMENTARES AOS FUNCIONAMENTO DOS AGENTES 
   ########################################################################################*/
    
    public void execute(ServicoAgente servicoAgente, ETiposServicosAgentes tipo_servico) {
       
       ObjetoComunicacaoMelhorado objeto;
       
       //Pega os dados do InputStream do Socket      
       //Processa informação do socket de entrada e processa informação
       objeto = processaInformacao(lerSocketChannel(socketChannel, null),servicoAgente, tipo_servico);
  
       //Escrever o resultado para o socketChannel
       //Após o processamento é realizado novamente a serialização e enviar ao socket de saída
       escreveSocketChannel(serializaMensagemMelhorado(objeto), socketChannel);
                       
       //Verificar se está implementado nos métodos
       //flush();
       //read.close();
       //write.close();
    }    
    
    public ObjetoComunicacaoMelhorado processaInformacao(ByteBuffer byteBuffer,ServicoAgente servico, ETiposServicosAgentes tipo) throws IllegalArgumentException{

      SolucaoHeuristica solucao_ = new SolucaoHeuristica();
      ObjetoComunicacaoMelhorado objeto = new ObjetoComunicacaoMelhorado();
      objeto = deserializaMensagemMelhorado(byteBuffer);
      
      if(servico.equals(ServicoAgente.Alocacao)){
          
          switch(tipo){

             case agente_alocacao_1:
                  solucao_ = agente_alocacao_1(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case agente_alocacao_2:
                  solucao_ = agente_alocacao_2(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case agente_alocacao_3:
                  solucao_ = agente_alocacao_3(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case agente_alocacao_4: break;

             case agente_alocacao_5:
                  solucao_ = agente_alocacao_5(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case agente_alocacao_6:
                  solucao_ = agente_alocacao_6(objeto.getSolucao());  
                  objeto.setSolucao(solucao_);
             break;

             case agente_alocacao_7:
                  solucao_ = agente_alocacao_7(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case agente_alocacao_8: break;

             case agente_alocacao_9:
                  solucao_ = agente_alocacao_9(objeto.getSolucao()); 
                  objeto.setSolucao(solucao_);
             break;
          }
         return objeto;
      }
      else if(servico.equals(ServicoAgente.Permutacao)){
      
          switch(tipo){

             case agente_permutacao_1:
                  solucao_ = agente_permutacao_1(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;
                 
             case agente_permutacao_2:
                  solucao_ = agente_permutacao_2(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;
                 
             case agente_permutacao_3:
                  solucao_ = agente_permutacao_3(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;
                 
             case agente_permutacao_4:
                  solucao_ = agente_permutacao_4(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;
                 
             case agente_permutacao_5: break;
                 
             case agente_permutacao_6:
                  solucao_ = agente_permutacao_6(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;
                 
             case agente_permutacao_7: break;
                 
             case agente_permutacao_8:
                  solucao_ = agente_permutacao_8(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;
          }
          
          return objeto;
      }
      else{}
      
     return objeto; 
   }
    
    //Verificar a quantidade de indivíduos com a capacidade não respeitada
    public int verificaCapacidadeNRespeitada(/*HeuristicaConstrutivaInicial.Solucao*/SolucaoHeuristica solucao_meSol){
    
        int cont = 0;
        Individuo individuo;
        
        list_ind_extrap = new ArrayList<Individuo>();
        Iterator<Individuo> iterator_ind = solucao_meSol.getLinkedListIndividuos().iterator();
        
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
    public Individuo selecionaRandomicamenteListaNrespeitada(SolucaoHeuristica solucao){
   
       int cont = verificaCapacidadeNRespeitada(solucao);
      
       return solucao.getLinkedListIndividuos().get(r.nextInt(cont));
    }
    
    public Individuo selecionaRandomicamenteListaRespeitada(SolucaoHeuristica solucao){
    
       int cont = verificaCapacidadeNRespeitada(solucao);
      
       return solucao.getLinkedListIndividuos().get(Funcoes.aleatorio_entre_Intervalo(cont, solucao.getObjetos().size()));
    }
    
    //Retorna próxima sublista da capacidade Respeitada
    public Individuo selecionaProximaListaRespeitada(SolucaoHeuristica solucao){
    
        int cont = verificaCapacidadeNRespeitada(solucao);
        
        return solucao.getLinkedListIndividuos().get(cont);
    }
    
    //Colocar todos os indivíduos que extrapolam em uma lista
    public List<Integer> listIndividuosExtrapolam(Individuo list_ind){
    
        //É necessário a priori o cálculo do excedente
        int item;
        Integer integer;
        
        double d = list_ind.getFitness();
        j_HeuristicaArvoreNAria.Pedidos[] pedidos = Funcoes.getVetor_info();
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
    
   public SolucaoHeuristica agente_alocacao_1(SolucaoHeuristica solucao_memoria){
             
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
        
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
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
    
    public SolucaoHeuristica agente_alocacao_2(SolucaoHeuristica solucao_memoria){
    
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
    
    public SolucaoHeuristica agente_alocacao_3(SolucaoHeuristica solucao_memoria){
        
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
    
    public void agente_alocacao_4(SolucaoHeuristica solucao_memoria){/*Não escolhi essa !!!*/}
        
    public SolucaoHeuristica agente_alocacao_5(SolucaoHeuristica solucao_memoria){
    
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
        
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
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
    
    public SolucaoHeuristica agente_alocacao_6(SolucaoHeuristica solucao_memoria){
    
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
    
    public SolucaoHeuristica agente_alocacao_7(SolucaoHeuristica solucao_memoria){
    
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
    public void agente_alocacao_8(SolucaoHeuristica solucao_memoria){/*Não escolhi essa*/}
    
    public SolucaoHeuristica agente_alocacao_9(SolucaoHeuristica solucao_memoria){
    
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
        
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
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
        
    public SolucaoHeuristica agente_permutacao_1(SolucaoHeuristica solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona nesta sublista, o item de menor tamanho
        item1 = Util.selecionaMenorItem((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona dentre todas as sublistas, a que possui a menor ocupação
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
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
    
    public SolucaoHeuristica agente_permutacao_2(SolucaoHeuristica solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona nesta sublista, o item de maior tamanho
        item1 = Util.selecionaMaiorItem((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona dentre todas as sublistas, a que possui a menor ocupação
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
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
    
    public SolucaoHeuristica agente_permutacao_3(SolucaoHeuristica solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona nesta sublista, o item de maior tamanho
        item1 = Util.selecionaMaiorItem((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona dentre todas as sublistas, a que possui a menor ocupação
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaListaMenorOcupacao(this.solucao);
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
    
    public SolucaoHeuristica agente_permutacao_4(SolucaoHeuristica solucao_memoria){
    
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
    
    public void agente_permutacao_5(SolucaoHeuristica solucao_memoria){/*Não implementei essa*/}
    
    public SolucaoHeuristica agente_permutacao_6(SolucaoHeuristica solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente duas sublistas
        
        individuo1 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
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
    
    public void agente_permutacao_7(SolucaoHeuristica solucao_memoria){/*Não escolhi essa*/}
    
    public SolucaoHeuristica agente_permutacao_8(SolucaoHeuristica solucao_memoria){
    
        Integer item1, item2;
                
        this.solucao.setSolucao(solucao_memoria);
        
        //Seleciona randomicamente uma sublista com restrição de capacidade Não-Respeitada
        
        individuo1 = selecionaRandomicamenteListaNrespeitada(solucao_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Seleciona randomicamente um item desta sublista
        item1 = Util.selecionaItemAleatorio((ArrayList<Integer>) individuo1.getListaItens());
        
        //Seleciona randomicamente uma outra sublista
        individuo2 = SimulaGenetico.OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
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
    
    @Override
    public void run() {
    
        ObjetoComunicacaoMelhorado obcom = getObjetoComunicacaoMelhorado();
        ObjetoComunicacaoTree  obcomTree  = getObjetoComunicacaoTree();
        
        if(obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao){
        
            System.out.println("Entrei para solicitar solucao a memoria 1");
            
            while(executando){ 
  
                try{
                    socketClientFinal.setSoTimeout(2500);
                       //Ele escreve uma requisição para o Atendente !!!
                       obcom.setMSGServicoAgente("BuscaSolucao");
                       System.out.println("Vou enviar um objeto de solicitação");
                       send(obcom);
                       
                       //Leitura de informações envidas pelo servidor !!!
                       System.out.println("Vou receber o objeto solicitado");
                       obcom = receive();
                       
                       if(obcom == null){
                           System.out.println("Não tinha nada");
                           continue;
                       }
                       String mensagem = obcom.getMSGServicoAgente();
                       
                       if("SolucaoPronta".equals(mensagem)){
                       
                           //Aqui chama o processa informação do Agente Grasp
                           System.out.println("Vou iniciar o Simula Genético");
                                                      
                           //Esse objetoM1 será manipulado no próximo passo, e escreverá em uma memória diferente !
                           this.setObjetoComunicacaoMelhorado(obcom);
                           
                           System.out.println("Terminei a busca no Simula Genético e setei a solução");
                           
                           obcom.setMSGServicoAgente("Terminei");
                           send(obcom);
                           
                           mensagem = obcom.getMSGServicoAgente();
                          
                       }
                       if("Terminei".equals(mensagem)){
                           System.out.println("Servidor retornou a solução adequada ao Simula Genético!");
                           break;
                       }
                       if(mensagem == null){
                           System.out.println("Mensagem nula !");
                           break;
                       }
                       System.out.println("Mensagem enviada pelo servidor: " +mensagem);
                }                
                catch(Exception e){
                    
                }
             }
            //Antes de encerrar devemos liberar todos os recursos !!!
            close();
        }
        
        if(obcomTree.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao){
        
            //ObjetoComunicacaoTree obTree = getObjetoComunicacaoTree();
            System.out.println("Entrei para escrever solucao na memoria 2");
            while(executando){ 

                try{
                   socketClientFinal.setSoTimeout(2500);
                   
                   System.out.println("Vou escrever uma solução na Memória;");
                   obcomTree.setMSGServicoAgente("Escrevendo Solução");
                   System.out.println("Vou enviar um objeto");
                   send(obcomTree);
                   
                   //Leitura de informações envidas pelo servidor !!!
                   System.out.println("Vou receber um objeto");
                   obcomTree = receiveT();
                   String mensagem = obcomTree.getMSGServicoAgente();
                   
                   if(mensagem == null){
                       System.out.println("Mensagem nula !");
                       break;
                   }
                   if("Terminei".equals(mensagem)){
                       System.out.println("Servidor escreveu uma solução !");
                       break;
                   }
                   System.out.println("Mensagem enviada pelo servidor: " +mensagem);
                }
                catch(Exception e){
                }
                
            }
            //Antes de encerrar devemos liberar todos os recursos !!!
            close();        
        }
    
    }
    public static void main(String args[]){
    
        //Depois eu vejo como utilizar isso aqui !!!!
    
    }
}