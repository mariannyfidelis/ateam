package Heuristicas;

import java.util.Set;
import java.util.Iterator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import SimulaGenetico.Agentes;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.channels.Selector;
import HHDInternal.SolucaoHeuristica;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import ComunicaoConcorrenteParalela.TipoMemoria;
import ComunicaoConcorrenteParalela.SimulaServidor;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;

public class Memoria extends SimulaServidor implements Runnable{

    private int tamanho_memoria;
    private int num_solucoes;
    private LinkedList<SolucaoHeuristica> lista_solucoes;       

    public Memoria(int tamanho_memoria){

        System.out.println("Inicializei uma Memória de tamanho "+ tamanho_memoria);
        num_solucoes = 0;
        this.tamanho_memoria = tamanho_memoria;
        lista_solucoes = new LinkedList<SolucaoHeuristica>();
    }

    public Memoria(int porta, String host, String name,int tamanho_memoria, TipoMemoria tipoMemoria/*POLÍTICA DESTRUIÇÃO*/) throws IOException{

        System.out.println("Inicializei a Memória "+ name+" de tamanho "+ tamanho_memoria);
        num_solucoes = 0;
        this.tamanho_memoria = tamanho_memoria;
        lista_solucoes = new LinkedList<SolucaoHeuristica>();
        
        this.simulaServidor(porta, host, name, tipoMemoria);
        //super.SimulaServidor(porta, host, name, tipoMemoria);
    }

    public int getTamanho_memoria() {

        return tamanho_memoria;
    }

    public void setTamanho_memoria(int tamanho_memoria) {

        this.tamanho_memoria = tamanho_memoria;
    }

    public int getNum_solucoes() {

        return num_solucoes;
    }

    public void setNum_solucoes(int num_solucoes){

        this.num_solucoes = num_solucoes;
    }

    public LinkedList<SolucaoHeuristica> getLista_solucoes(){

        return lista_solucoes;
    }

    public void setLista_solucoes(LinkedList<SolucaoHeuristica> lista_solucoes) {

        this.lista_solucoes = lista_solucoes; //Talvez this.lista_solucoes.addAll(lista_solucoes)
    }

    public SolucaoHeuristica getSolucaoMemoria(int indice){

        return lista_solucoes.get(indice);
    }

    public void adiciona_solucao(SolucaoHeuristica solucao){

        if(getNum_solucoes() + 1 <= getTamanho_memoria()){

            //this.lista_solucoes.add(solucao);
            getLista_solucoes().add(solucao);
            setNum_solucoes(getNum_solucoes() + 1);
            //setTamanho_memoria(getTamanho_memoria() - 1);
        }
    }

    public void remove_solucao(SolucaoHeuristica solucao){

        getLista_solucoes().remove(solucao);
        setNum_solucoes(getNum_solucoes() - 1);
        //setTamanho_memoria(getTamanho_memoria() + 1);
    }

    public void adiciona_conjuntoSolucoes(LinkedList<SolucaoHeuristica> list_solucoes){

        if(getNum_solucoes() + list_solucoes.size() <= getTamanho_memoria()){

            this.lista_solucoes = list_solucoes;  //Talvez seja melhor: this.lista_solucoes.addAll(list_solucoes);
            setNum_solucoes(list_solucoes.size()); // Talvez setNum_solucoes(getNum_solucoes()+ list_solucoes.size());
        }
        //setTamanho_memoria(getTamanho_memoria() - list_solucoes.size());
    }

    public void imprimeSolucoes(){

        Iterator<SolucaoHeuristica> it_sol = getLista_solucoes().iterator();
        SolucaoHeuristica solucao_;
        int cont = 1;
        while(it_sol.hasNext()){

            solucao_ = it_sol.next();
            //SolucaoHeuristica.imprimeSolucao(solucao_, cont);
            solucao_.imprimeSolucao(solucao_, cont);
            cont++;
        }            
    }
    
    //Será interessante criar um método que simula o servidor
    public void simulaServidor(int porta, String host, String name, TipoMemoria tipo_memoria) throws IOException{
        
        ServerSocketChannel server = null;
                
        if(tipo_memoria.equals(TipoMemoria.A)){
            
            server = SimulaServidor.abreConexao(porta);
            
            //Aqui será simulado o funcionamento de um servidor, criando um selector e chamando métodos 
            //ligados a memória como adicionar soluções recebidas de algum Agente (ex: Inicilização-GRASP)
            
            try{
                Selector selector = Selector.open();
                
                ByteBuffer buffer = ByteBuffer.allocate(5000);
                ByteBuffer[] vetor_Byte_Buffer = new ByteBuffer[tamanho_memoria]; //Um vetor de ByteBuffer que recebe no máximo tamanho da memória !
                
                //Registra canal no selector
                SelectionKey key = server.register(selector, SelectionKey.OP_ACCEPT);
                
                while(true){

                    System.out.println("Aguardando conexão !!!");
                    //Esperar por eventos
                    int num = selector.select();

                    //Armazenar lista de eventos/keys pendentes
                    Set selectedKeys = selector.selectedKeys();
                    Iterator it = selectedKeys.iterator();

                    //Processar cada chave/key
                    while(it.hasNext()){

                        //Seleciona a chave/key atual
                        SelectionKey selKey =  (SelectionKey) it.next();

                        //Remove a chave selecionada da lista
                        it.remove();

                        //Checando requisição de conexão
                        if(selKey.isAcceptable()){

                            //Seleciona o canal com a requisição de conexão
                            ServerSocketChannel serverChanel = (ServerSocketChannel) selKey.channel();

                            //#####################  AQUI IMPLEMENTA A ACEITAÇÃO DE NOVAS CONEXÕES  ###################
                            int porta_local = serverChanel.socket().getLocalPort();

                            try{
                              //Aceitando a requisição de novas conexões
                                SocketChannel servChannel = serverChanel.accept();//Talvez serveChannel

                               //Inicialmente irá aguardar a conexão do agente iniciador
                               if(servChannel != null){
                                    
                                    System.out.println("Alguém conectou !"); //--:> Depois seria interessante diferenciar os Agentes

                                    String teste = "\nConexão Estabelecida";

                                    System.out.println("\nEnviando comunicação");
                                    buffer = ByteBuffer.allocate(4040);

                                    buffer.flip();

                                    //servChannel.write(serializaMensagem(new ObjetoComunicacao(null, "Pronto para receber dados!", 
                                      //                                  ETiposServicosAgentes.agente_inicializacao,ETiposServicosServidor.AtualizaSolucao)));//, servChannel);
                                    
                                    //Aqui será realizado um teste com Objeto Comunicação Melhorado
                                    servChannel.write(serializaMensagem(new ObjetoComunicacaoMelhorado(null, "Pronto para receber dados!", 
                                                        null,null)));
                                    
                                    System.out.println("\nEnviado comunicação ...");

                                    System.out.println("\nVou aguardar comunicação do cliente ...");

                                    //Realiza a leitura no socket e armazena a leitura em um Buffer !!!

                                    //Limpar o buffer
                                    buffer.clear();

                                    int numBytesRead = 0, cont = 0;
                                    //ObjetoComunicacao obcom = new ObjetoComunicacao();
                                    ObjetoComunicacaoMelhorado obcom = new ObjetoComunicacaoMelhorado();
                                   
                                    boolean testes_b = true;
                                    do{
                                        //Prepara o buffer para leitura
                                        buffer.clear();
                                        
                                        //Aqui o Servidor tenta ler um ByteBuffer
                                        try{
                                            System.out.println("Entre no teste");
                                            //numBytesRead = servChannel.read(buffer);
                                            do{ 
                                                numBytesRead = (int) servChannel.read(buffer);
                                            }while(numBytesRead > 0);
                                            ObjetoComunicacaoMelhorado obj = Agentes.deserializaMensagemMelhorado(buffer);//deserializaMensagemObM(buffer);

                                                System.out.println("\n\nTestando o Objeto Comunicação Melhorado !!!");
                                                System.out.println("Servico Agente --> "+obj.getMSGServicoAgente());
                                                System.out.println("Tipo Serviço Agente --> "+obj.getTipoServicoAgente());
                                                System.out.println("Tipo Serviço Servidor --> "+obj.getTipoServicoServidor());
                                                System.out.println("FAV --> "+obj.getSolucao().getFAV());
                                                System.out.println("FAV2 --> "+obj.getSolucao().getFAV2());

                                                Iterator<Individuo> ind = obj.getSolucao().getLinkedListIndividuos().iterator();

                                                while(ind.hasNext()){

                                                    System.out.println("\nLista Intens --> "+ ind.next().getListaItens());
                                                }
                                        }
                                        catch(Exception e){
                                        
                                            System.out.println("Lançando uma exceção !!!");
                                                
                                        
                                             
                                        }
                                        
                                        System.out.println("Num -> "+cont++);

                                        //obcom = deserializaMensagemObM(buffer);

                                        
                                        //System.out.println(""+obcom.getMSGServicoAgente());
                                       // System.out.println(""+obcom.getTipoServicoAgente2().toString());
                                        //System.out.println(""+obcom.getTipoServicoServidor().toString());

                                        //Por enquanto só imprima
                                        //HeuristicaConstrutivaInicial.Solucao.imprimeSolucao(obcom.getSolucao(),cont);
                                        
                                        if(numBytesRead == -1){
                                             //servChannel.close();
                                        }else{                                       
                                            buffer.flip();
                                        }

                                    }while(testes_b != false);//{
                                
                                    /*if(buffer != null){

                                         obcom = deserializaMensagem(buffer);
                                         solucao =  obcom.getSolucao();                      
                                         this.adiciona_solucao(solucao);
                                         System.out.println("Recebi a MSG do Agente --> "+ deserializaMensagem(buffer));
                                     }*/
                               }
                            }
                            catch(IOException e){
                                
                                System.out.println(e);
                            }
                        }
                    }
                }
            }
            catch(IOException io){}
        }
    }
    
    //Método que chamará a execução do servidor
    public void execute(){
      
        //ObjetoComunicacao objeto;
        ObjetoComunicacaoMelhorado objeto;
          //Pega os dados do InputStream do Socket      
          //Processa informação do socket de entrada e processa informação
          objeto = processaInformacao(super.lerSocketChannel(super.getSocket(), null));
  
          //Escrever o resultado para o socketChannel
          //Após o processamento é realizado novamente a serialização e enviar ao socket de saída
          escreveSocketChannel(serializaMensagem(objeto), super.getSocket());
                       
          //Verificar se está implementado nos métodos
             //flush();
             //read.close();
             //write.close();
    }
  
    public ObjetoComunicacaoMelhorado processaInformacao(ByteBuffer byteBuffer) throws IllegalArgumentException{

      SolucaoHeuristica solucao = new SolucaoHeuristica();
      //ObjetoComunicacao objeto = new ObjetoComunicacao();
      ObjetoComunicacaoMelhorado objeto = new ObjetoComunicacaoMelhorado();
      
      objeto = deserializaMensagemObM(byteBuffer);
      
      //Aqui o tipo de Serviço de Servidor indica qual o serviço que agente deseja !!!
      if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.MelhorSolucao)){
           //Vou retornar a melhor solução
          solucao = retorna_melhor_Solucao(getLista_solucoes());  //Passar memória adequada !!!
          objeto.setSolucao(solucao);
      }
      //Se for agente de Serviço B
      else if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.PiorSolucao)){
          //Vou retornar a pior solução
          solucao = retorna_pior_Solucao(getLista_solucoes());       //Passar memória adequada !!!
          objeto.setSolucao(solucao);
      }
      //Se for agente de Serviço C
      else if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.Solucao_Aleatoria)){
         //Vou retornar uma solucao aleatória
         solucao = retorna_aleatoria_Solucao(getLista_solucoes());   //Passar memória adequada !!!
         objeto.setSolucao(solucao);
      }
      else{
          //Aqui ele deseja atualiza a memória
      }
      
      return objeto;
    }
     
    /*########################################################################################
                MÉTODOS QUE MANIPULAM E RETORNAM SOLUÇÕES ADEQUADAS
    ########################################################################################*/
    
    SolucaoHeuristica solucao = new SolucaoHeuristica();
    SimulaGenetico.OperacoesSolucoes_Individuos operacoesSolucoes = new SimulaGenetico.OperacoesSolucoes_Individuos();
    
    public SolucaoHeuristica retorna_melhor_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {
    
        solucao.setSolucao(operacoesSolucoes.retornaMelhorSolucao(getLista_solucoes()));
        return solucao;
    }
    
    public SolucaoHeuristica retorna_pior_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {
    
        solucao = operacoesSolucoes.retornaPiorSolucao(getLista_solucoes());
        
        return solucao;
    }

    public SolucaoHeuristica retorna_aleatoria_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {
    
        solucao = operacoesSolucoes.retornaSolucaoAleatoria(getLista_solucoes());
        return solucao;
    }
    
    //Método run simula Thread !!!!    
    @Override
    public void run() {
        try {
            SimulaServidor.abreConexao(super.getPorta());
        } catch (IOException ex) {
            Logger.getLogger(Memoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
}
