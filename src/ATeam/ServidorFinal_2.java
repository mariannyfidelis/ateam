package ATeam;

import java.io.File;
import java.util.List;
import java.net.Socket;
import java.util.Scanner;
import Utilidades.Funcoes;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;
import java.util.LinkedList;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import j_HeuristicaArvoreNAria.Bin;
import Utilidades.PoliticaDestruicao;
import java.net.SocketTimeoutException;
import j_HeuristicaArvoreNAria.SolucaoNAria;
import SimulaGenetico.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoTree;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;


public class ServidorFinal_2 implements Runnable{

    private ServerSocket serverSocket;  //para receber conexões de clientes
    private boolean inicializado;//atributo booleano pra saber se o servidor foi iniciado ou não
    private static boolean executando;//atributo booleano pra saber se o servidor está executando ou não
    private boolean trabalhando;
    private Thread  thread;//atributo Thread  para controlar e representar a thread auxiliar para recebimento de conexões do servidor;
    private List<AtendenteCliente_M2> atendentes; //atributo que armazena a lista de atendentes!
  
    private String name;
    private int tamanho_memoria;
    private int num_solucoes;
    private LinkedList<SolucaoNAria> lista_solucoes;
    private LinkedList   requisicoes;
    private PoliticaDestruicao politicaDestruicao;
  
    private Socket socketCliente      = null;
    private ObjectInputStream  input  = null;
    private ObjectOutputStream output = null;
    
    private static FileWriter arquivoFAV1;
    private static FileWriter arquivoFAV2;
    private static FileWriter arquivoMediaSobra;
    private static FileWriter arquivoSomatorioSobra;
    private static FileWriter arquivoNumBins;
    private static FileWriter arquivoPrincipal_NumBins;
    private static FileWriter arquivoPrincipal_FAV;
    private static FileWriter arquivoPrincipal_FAV2;
    private static FileWriter arquivoPrincipal_MediaSobra;
    private static FileWriter arquivoPrincipal_SomatorioSobra;
    private static FileWriter arquivoFinalCompleto;
    private static FileWriter arquivoPrincipalSolucao2;
    private long ini;
    
    public  ServidorFinal_2(String name, int portaComunicacao, int tamanho_memoria, PoliticaDestruicao polDestruicao) throws Exception{
    
        atendentes   = new ArrayList<AtendenteCliente_M2>();
        inicializado = false;
        executando   = false;
        trabalhando  = false;
    
        this.name = name;
        this.tamanho_memoria = tamanho_memoria;
        this.num_solucoes = 0;
        lista_solucoes = new LinkedList<SolucaoNAria>();
        this.politicaDestruicao = polDestruicao;
        requisicoes = new LinkedList();

        abriArquivosGraficosM2();
        
        open(portaComunicacao);   
  }
  public void abriArquivosGraficosM2(){
  
      try {    
      
          arquivoFAV1              = new FileWriter(new File("ArquivoFAV1_M2"));
          arquivoFAV2              = new FileWriter(new File("ArquivoFAV2_M2"));
          arquivoMediaSobra        = new FileWriter(new File("ArquivoMediaSobra_M2"));
          arquivoSomatorioSobra    = new FileWriter(new File("ArquivoSomatorioSobra_M2"));
          arquivoNumBins           = new FileWriter(new File("ArquivoNumBins_M2"));
          arquivoFinalCompleto     = new FileWriter(new File("ArquivoFinalCompletoM2"));
          arquivoPrincipalSolucao2 = new FileWriter(new File("ArquivoHistSolucaoM2"));
          //arquivoLog = new FileWriter(new File("ArquivoGrafico1"));
          //arquivoGraficoPlot = new FileWriter(new File("ArquivoServidor2"));
          //arquivo2   = new FileWriter(new File("ArquivoGrafico2"));
          //arquivoGraficoPlot2 = new FileWriter(new File("ArquivoServidor2_2"));
      }
      catch(IOException exp){
          Logger.getLogger(ServidorFinal.class.getName()).log(Level.SEVERE, null, exp);
      }
  }
  
  public void abriArquivosPrincipaisGraficosM2(){   
         
      long dife = System.currentTimeMillis();
      SolucaoNAria s = getSolucaoMemoria(0);
      Iterator<Bin> iterBin = s.listaBinSolucao();
      
      //float area = s.getTamanhoChapa().retorneArea();
       //getObjetos().get(0).retorneDimensao().retorneArea();
      
      try {
            arquivoPrincipal_FAV             = new FileWriter(new File("ArquivoPrincipal_FAV_M2"));
            arquivoPrincipal_FAV2            = new FileWriter(new File("ArquivoPrincipal_FAV2_M2"));
            arquivoPrincipal_MediaSobra      = new FileWriter(new File("ArquivoPrincipal_MediaSobra_M2"));
            arquivoPrincipal_SomatorioSobra  = new FileWriter(new File("ArquivoPrincipal_SomatorioSobra_M2"));
            arquivoPrincipal_NumBins         = new FileWriter(new File("ArquivoPrincipal_NumeroBins_M2"));
      }
      catch (IOException ex) {
          Logger.getLogger(ServidorFinal.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      try {
            arquivoPrincipal_NumBins.write("set grid\n");
            arquivoPrincipal_NumBins.write("set title  "+"\"Gráfico Número de Bins da M2\"\n");
            arquivoPrincipal_NumBins.write("set xlabel "+ "\"Solucao\"\n");
            arquivoPrincipal_NumBins.write("set ylabel "+ "\"Bins\"\n");
            //arquivoPrincipal_FAV.write("set ytics 10.0\n");
            arquivoPrincipal_NumBins.write("set xrange [1.00:"+this.tamanho_memoria+"]\n");
            arquivoPrincipal_NumBins.write("set yrange [0.00:100.00]\n");//Tem que ser limite inferior
            arquivoPrincipal_NumBins.write("plot "+ "\"ArquivoNumBins_M2\"\n");
            
            //Arquivo GNUPlot FAV
            arquivoPrincipal_FAV.write("set grid\n");
            arquivoPrincipal_FAV.write("set title  "+"\"Gráfico Função de Avaliação FAV Simples\"\n");
            arquivoPrincipal_FAV.write("set xlabel "+ "\"Tempo\"\n");
            arquivoPrincipal_FAV.write("set ylabel "+ "\"Inverso da qualidade FAV\"\n");
            //arquivoPrincipal_FAV.write("set ytics 10.0\n");
            arquivoPrincipal_FAV.write("set xrange [0.00:"+(dife-ini)/1000+".00]\n");
            arquivoPrincipal_FAV.write("set yrange [0.00:100.00]\n");
            arquivoPrincipal_FAV.write("plot "+ "\"ArquivoFAV1_M2\"\n");
            
            //arquivoFAV1,arquivoFAV2,arquivoMediaSobra,arquivoSomatorioSobra
            //Abrindo Arquivo GNUPlot
            arquivoPrincipal_FAV2.write("reset\n");
            arquivoPrincipal_FAV2.write("set grid\n");
            arquivoPrincipal_FAV2.write("set title  "+"\"Gráfico Função de Avaliação FAV2 Melhorada\"\n");
            arquivoPrincipal_FAV2.write("set xlabel "+ "\"Tempo\"\n");
            arquivoPrincipal_FAV2.write("set ylabel "+ "\"Inverso da qualidade FAV2\"\n");
            //arquivoPrincipal_FAV2.write("set ytics 10.0\n");
            arquivoPrincipal_FAV2.write("set xrange [0.00:"+(dife-ini)/1000+".00]\n");
            arquivoPrincipal_FAV2.write("set yrange [0.00:100.00]\n");
            arquivoPrincipal_FAV2.write("plot "+ "\"ArquivoFAV2_M2\"\n");
            
            //arquivoFAV1,arquivoFAV2,arquivoMediaSobra,arquivoSomatorioSobra
            //Abrindo Arquivo GNUPlot
            arquivoPrincipal_MediaSobra.write("reset\n");
            arquivoPrincipal_MediaSobra.write("set grid\n");
            arquivoPrincipal_MediaSobra.write("set title  "+"\"Gráfico Função de Avaliação Media Sobra\"\n");
            arquivoPrincipal_MediaSobra.write("set xlabel "+ "\"Tempo\"\n");
            arquivoPrincipal_MediaSobra.write("set ylabel "+ "\"Inverso da qualidade Média Sobra\"\n");
            //arquivoPrincipal_MediaSobra.write("set ytics 100.0\n");
            arquivoPrincipal_MediaSobra.write("set xrange [0.00:"+(dife-ini)/1000+".00]\n");
            arquivoPrincipal_MediaSobra.write("set yrange [0.00:1000.00]\n");
            arquivoPrincipal_MediaSobra.write("plot "+ "\"ArquivoMediaSobra_M2\"\n");
            
            //arquivoFAV1,arquivoFAV2,arquivoMediaSobra,arquivoSomatorioSobra
            //Abrindo Arquivo GNUPlot
            arquivoPrincipal_SomatorioSobra.write("reset\n");
            arquivoPrincipal_SomatorioSobra.write("set grid\n");
            arquivoPrincipal_SomatorioSobra.write("set title  "+"\"Gráfico Função de Avaliação Somatorio Sobras\"\n");
            arquivoPrincipal_SomatorioSobra.write("set xlabel "+ "\"Tempo\"\n");
            arquivoPrincipal_SomatorioSobra.write("set ylabel "+ "\"Inverso da qualidade\"\n");
            //arquivoPrincipal_SomatorioSobra.write("set ytics 50.0\n");
            arquivoPrincipal_SomatorioSobra.write("set xrange [0.00:"+(dife-ini)/1000+".00]\n");
            arquivoPrincipal_SomatorioSobra.write("set yrange [0.00:"+100+"\n");
            arquivoPrincipal_SomatorioSobra.write("plot "+ "\"ArquivoSomatorioSobra_M2\"\n");
        } 
        catch (IOException ex) {
            System.out.println(ex);
        }     
  }
  
  private void open(int portaComunicacao) throws Exception{
    
        serverSocket = new ServerSocket(portaComunicacao,1000);
        inicializado = true;
  }
  
  public static boolean  isExecutando(){
      return executando;
  }
  
  private void close(){ //Tem a responsabilidade de liberar recursos alocados pelo objeto Servidor !
    
    //OBS: Deve-se encerrar cada um dos atendentes !
    for(AtendenteCliente_M2 atendente : atendentes){
	
	try{
	    //Chamar o método stop() de cada atendente que encerrar a thread auxiliar e libera recursos alocados pelo atendente !
	    atendente.stop();
	}
	catch(Exception e){
	    System.out.println(e);
	}
    }
    
    try{
      serverSocket.close();
    }
    catch(Exception e){
      System.out.println(e);
    }
    //Reinicia os valores dos atributos do servidor
    
    serverSocket = null;
    
    inicializado = false;
    executando = false;
    
    thread = null;
  }
  
  public void start(){//Método para iniciar a execução do servidor. Ou seja para iniciar a thread auxiliar do servidor !
    
        if(! inicializado || executando){

          return;
        }

        executando = true;

        thread = new Thread(this); //Ao criamos a nova thread, passamos como objeto o objeto que implementa a interface Runnable.
                                  //A nova thread executará o método run que foi implementado no objeto informado o this !
        thread.start(); //A chamada desse método start() faz com que a thread inicie a execução do método run() indicado.
    
  }
  
  public void stop() throws Exception{ //Forma correta de controlar o inicio e parada de qualquer thread auxiliar;
  
        /*if(!inicializado || executando){
          return;
        }*/

        executando = false;

        if(thread != null){

          thread.join(); // Esse método bloqueia a thread atual até que a thread auxilair seja finalizada;

        }
  }
  
  @Override
  public void run(){//Dentro do método run() que será executado pela thread auxiliar
    
        System.out.println("Aguardando conexão de clientes!");
        ini = System.currentTimeMillis();

        while(executando){//Torna-se true no método start() e false no método stop();

            System.out.println("Esperando alguém conectar ...");
            
            try{

              serverSocket.setSoTimeout(2500);//É necessário definir um tempo limite para a thread e verificar a condição do laço !

              final Socket socket = serverSocket.accept();
              
              final ObjectInputStream input_;
              final ObjectOutputStream output_;
              
              synchronized (this) {
              
                  System.out.println("Conexão estabelecida !"); 
                  
                  input_  = new ObjectInputStream(socket.getInputStream());
                  output_ = new ObjectOutputStream(socket.getOutputStream());

                  System.out.println("Connected, Esta ocupado ?");
                  //synchronized (this) {
                  String ocp = "";
//
//                    if (getNum_solucoes() == 0) {
//                        ocp = "true";
//                    } else {
                    ocp = Boolean.toString(ocupadoMomento());
//                    }

                    escreveInformacao(new ObjetoComunicacaoTree(null, ocp, null, null), output_);

                    if (ocupadoMomento()) {

                        System.out.println("Sim, retorne S2....");
                        socket.close();
                        continue;
                    } else {
                        System.out.println("Nao S2!!!!!!!!!!!!");
                        setOcupado();
                    }
              }
              
              System.out.println("Atendendo requisiçao S2.....");
              new Thread(){

                    @Override
                    public void run() {
                        try {
                            atendeRequisicao(socket, input_, output_); //Atende requisição na forma de fila !
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } 
                    }
                    
                }.start();
                System.out.println("Esta sendo atendida pelo S2 .....");
                
                System.out.println("Fim, Retornar ao laço...");
              

              //Devemos atendê-lo. Ou seja obter o Input e Output stream. Ler a mensagem, devolver a mensagem até msg FIM ser enviada !

              //Implementaremos uma classe AtendenteCliente. Na sua criação informamos o socket que representa o cliente;
              //System.out.println("\nVou criar um Atendente");

              //AtendenteCliente_M2 atendenteCliente = new AtendenteCliente_M2(socket, this);

              //System.out.println("Criei um atendente !");

              //Cada atendenteCliente criado é um recurso alocado pelo servidor.Guardando assim uma lista,
              //para que antes de encerrar, ele pudesse liberar também estes recursos.
              //atendenteCliente.start(); //Responsável por gerenciar o atendimento do cliente espcificado conexctado !

              //System.out.println("Atendente já está atendendo ... ");

              //atendentes.add(atendenteCliente);

            }
            catch(SocketTimeoutException e){
              //Exceção indicando que o Timeout ocorreu. Thread desbloqueada. Logo a condição será reavaliada
              //System.out.println(e);
            }
            catch(Exception e){
              //Qualquer outra Exceção considerada fatal !
              //e.printStackTrace();
              break;
            }
        } 

        close(); //Após a execução do laço devemos liberar os recursos alocados antes da finalização da thread, que encerrar ao terminar o método run();
        //}
        Funcoes.ordenaSolucoesHeuristicasNariaDecrescente(this.lista_solucoes);
        abriArquivosPrincipaisGraficosM2();

  }

  /**
   * * AQUI SERÁ IMPLEMENTADO OS MÉTODOS QUE SIMULAM O SERVIÇO FORNECIDO PELO SERVIDOR/MEMÓRIA
   * * MÉTODOS FORAM COPIADOS DO CÓDIGO MEMÓRIA   * 
   * *
   * * 
   * * 
   **/
  public boolean ocupadoMomento(){
  
      return trabalhando;
  }
  
  public void setOcupado(){
      
      if(ocupadoMomento()){
      
          trabalhando = false;
      }else{
      
          trabalhando = true;
      }
  }
  
  public void atendeRequisicao(Socket socket, ObjectInputStream in, ObjectOutputStream out) throws IOException, InterruptedException{
  
      try {
          Thread.sleep(50);
      } catch (InterruptedException ex) {
          ex.printStackTrace();
      }
      
      while(true){
          
          System.out.println("Laço ..................");
          ObjetoComunicacaoTree objetoCom = null; 

            //O Atendente irá processar a requisição do Cliente !
            objetoCom = processaInformacao(in, out);

            if(objetoCom == null){
            
                System.out.println("Estou encerrando ... ENFDRM2");
                //setOcupado();
                break;
            }
            if("Terminei".equals(objetoCom.getMSGServicoAgente())){

                escreveInformacao(objetoCom,out);
                System.out.println("Encerrando conexão !");
                
                break;
            }
            else{
                escreveInformacao(objetoCom,out);
            }
      }
      //close(); //Ates de encerrar o método run(), liberamos todos os recursos alocados pelo AtendenteCliente!!!
      socket.close();
      setOcupado();
  }
  
  public void adicionaRequisicao(Socket socket_cliente){
  
      requisicoes.add(socket_cliente);
  }
  
  synchronized public ObjetoComunicacaoTree processaInformacao(ObjectInputStream in, ObjectOutputStream out) throws IOException{
  
    ObjetoComunicacaoTree objeto;// = new ObjetoComunicacaoMelhorado();
    
    objeto = recebeInformacao(in);
    
    if(objeto == null){
    
        System.out.println("Objeto comunicação nulo ......");
        return null;            
    }
    
    String mensagemServico = objeto.getMSGServicoAgente();
    System.out.println("Recebi MSG do Cliente:  "+mensagemServico);
    ETiposServicosAgentes  servicoAgente   = objeto.getTipoServicoAgente2();
    ETiposServicosServidor servicoServidor = objeto.getTipoServicoServidor();
  
    SolucaoNAria solucao;
    
    if(servicoServidor ==  ETiposServicosServidor.Inserir_Solucao){
        
        if(servicoAgente ==  ETiposServicosAgentes.Escrever_Solucacao){
            
            if(mensagemServico.equals("Escrevendo Solução")){
            
                solucao  = new SolucaoNAria(objeto.getSolucao());
                this.adiciona_solucao(solucao);
                
                //Retorna para o Cliente que está pronto para nova solicitação 1
                objeto.setMSGServicoAgente("Terminei");
            
                return objeto;
            }
        }        
    }
    if(servicoAgente ==  ETiposServicosAgentes.Consulta_Solucao){
    
        //SERVIÇOS DE CONSULTA POR PARTE DO AGENTE NO SERVIDOR !
        //SERVIÇOS FORNECIDOS POR PARTE DO SERVIDOR !!!!
        //Aqui o tipo de Serviço de Servidor indica qual o serviço que agente deseja !!!
        if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.MelhorSolucao)){

            //Vou retornar a melhor solução
            solucao = this.retorna_melhor_Solucao(this.getLista_solucoes());  //Passar memória adequada !!!
            objeto.setSolucao(solucao);
            objeto.setMSGServicoAgente("SolucaoPronta");
            return objeto;
        }
        //Se for agente de Serviço B
        else if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.PiorSolucao)){
                    
            //Vou retornar a pior solução
            solucao = this.retorna_pior_Solucao(this.getLista_solucoes());       //Passar memória adequada !!!
            objeto.setSolucao(solucao);
            objeto.setMSGServicoAgente("SolucaoPronta");
            
            return objeto;
        }
        //Se for agente de Serviço C
        else if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.Solucao_Aleatoria)){
            //Vou retornar uma solucao aleatória
            solucao = this.retorna_aleatoria_Solucao(this.getLista_solucoes());   //Passar memória adequada !!!
            objeto.setSolucao(solucao);
            objeto.setMSGServicoAgente("SolucaoPronta");
                    
            return objeto;
        }
        else{/*Aqui ele deseja atualiza a memória*/}
    }
    
    return objeto;
  }
 
  private void escreveInformacao(ObjetoComunicacaoTree objeto, ObjectOutputStream output){

        try {
            output.writeObject(objeto);
            output.flush();
        }
        catch (IOException ex) {
            //System.out.println(ex);
        }
    }

  private ObjetoComunicacaoTree recebeInformacao(ObjectInputStream input) {

        ObjetoComunicacaoTree objeto = new ObjetoComunicacaoTree();

        try{
            objeto = (ObjetoComunicacaoTree) input.readObject();
        }
        catch(ClassNotFoundException cex){
            //System.out.println(cex);
        }
        catch (IOException ex) {
            //System.out.println(ex);
            return null;
        }
    
        return objeto;
  }
  
  
  public int getTamanho_memoria() {

      return tamanho_memoria;
  }

  public void setTamanho_memoria(int tamanho_memoria) {
      
      this.tamanho_memoria = tamanho_memoria;
  }

  synchronized public int getNum_solucoes() {

    try{
      return num_solucoes;}
    catch(Exception e){
        System.out.println("erro");
        return 0;
    }
  }

  public void setNum_solucoes(int num_solucoes){
      
      this.num_solucoes = num_solucoes;
  }

  public LinkedList<SolucaoNAria> getLista_solucoes(){

      return lista_solucoes;
  }

  public void setLista_solucoes(LinkedList<SolucaoNAria> lista_solucoes) {

      this.lista_solucoes = lista_solucoes; //Talvez this.lista_solucoes.addAll(lista_solucoes)
  }

  public SolucaoNAria getSolucaoMemoria(int indice){
      
      return lista_solucoes.get(indice);
  }

  public synchronized void adiciona_solucao(SolucaoNAria solucao) throws IOException{

      if(getNum_solucoes() + 1 <= getTamanho_memoria()){
          
          long fim = System.currentTimeMillis();
          long diff = fim - ini;
          arquivoFAV1.write((diff/1000)+" "+(solucao.getFAV()*100)+"\n");
          arquivoFAV2.write((diff/1000)+" "+solucao.getFAV2()+"\n");
          //arquivoMediaSobra.write((diff/1000)+" "+solucao.getMediaSobras()+"\n");
          arquivoSomatorioSobra.write((diff/1000)+" "+solucao.getSomatorioSobra()+"\n");
          arquivoNumBins.write(getNum_solucoes()+" "+solucao.numBinsSolucao()+"\n");
          arquivoFinalCompleto.write((diff / 1000) + " " +(solucao.getFAV() * 100) +" " + solucao.getFAV2() + " " + 
                                                     " " + solucao.getSomatorioSobra()+" " + solucao.numBinsSolucao()+
                                    " "+ solucao.getTipoHeuristic().toString()+" "+(this.getNum_solucoes()+1)+"\n" );
          //this.lista_solucoes.add(solucao);
          getLista_solucoes().add(solucao);
          setNum_solucoes(getNum_solucoes() + 1);
          Funcoes.ordenaSolucoesHeuristicasNariaDecrescente(this.lista_solucoes);
          
          System.out.println("Somatorio Sobra --> "+ solucao.getSomatorioSobra());
          //String aa = Long.toString(System.currentTimeMillis()/1000)+" "+solucao.getSomatorioSobra().toString();
                    
          if(getNum_solucoes() > 1 && getNum_solucoes() <= 2){//arquivo2.write();
          }else{}//setTamanho_memoria(getTamanho_memoria() - 1);
          
           //Aqui será escrito o arquivo com as melhores, piores e soluções medianas
          if(getNum_solucoes() > 2){
                arquivoPrincipalSolucao2.write((diff / 1000) + " " + this.getSolucaoMemoria(0).getFAV2() + "\n");
                arquivoPrincipalSolucao2.write((diff / 1000) + " " + this.getSolucaoMemoria(Math.round(this.lista_solucoes.size() / 2)).getFAV2() + "\n");
                arquivoPrincipalSolucao2.write((diff / 1000) + " " + this.getSolucaoMemoria(this.lista_solucoes.size() - 1).getFAV2() + "\n");     
          }
      }
      else{          
          int numSol = getNum_solucoes();
          if(solucao.getFAV2() > getLista_solucoes().get(numSol-1).getFAV2()){
              //Terá que remover uma solução para inserir a nova solução
              System.out.println("Chamei o agente destruidor antes -> "+getNum_solucoes());
              
              agenteDestruidor();
              System.out.println("Chamei o agente destruidor depois -> "+getNum_solucoes());
              
              adiciona_solucao(solucao);
          }
      }
  }

  public synchronized void remove_solucao(SolucaoNAria solucao){

      getLista_solucoes().remove(solucao);
      setNum_solucoes(getNum_solucoes() - 1);
      //setTamanho_memoria(getTamanho_memoria() + 1);
  }

  public void adiciona_conjuntoSolucoes(LinkedList<SolucaoNAria> list_solucoes){

      if(getNum_solucoes() + list_solucoes.size() <= getTamanho_memoria()){

          this.lista_solucoes = list_solucoes;  //Talvez seja melhor: this.lista_solucoes.addAll(list_solucoes);
          setNum_solucoes(list_solucoes.size()); // Talvez setNum_solucoes(getNum_solucoes()+ list_solucoes.size());
      }
      //setTamanho_memoria(getTamanho_memoria() - list_solucoes.size());
  }

  public void imprimeSolucoes(){
        
      Iterator<SolucaoNAria> it_sol = getLista_solucoes().iterator();
      SolucaoNAria solucao_;
      int cont = 1;
      
      while(it_sol.hasNext()){
          
          System.out.println("\nSolucao "+ cont);
          solucao_ = it_sol.next();
          System.out.println("FAV -> "+solucao_.getFAV()+"  FAV2 --> "+solucao_.getFAV2());
          System.out.println("Somatorio Sobra -> "+solucao_.getSomatorioSobra());
          
          solucao_.imprime_solucao();
          //SolucaoNAria.imprimeSolucao(solucao_, cont);
          cont++;
      }            
  }
  
  /*########################################################################################
                MÉTODOS QUE MANIPULAM E RETORNAM SOLUÇÕES ADEQUADAS
  ########################################################################################*/
  
  SolucaoNAria solucao = new SolucaoNAria();
  SimulaGenetico.OperacoesSolucoes_Individuos operacoesSolucoes = new SimulaGenetico.OperacoesSolucoes_Individuos();
  
  public SolucaoNAria retorna_melhor_Solucao(LinkedList<SolucaoNAria> list_solucao) {
      
      //solucao.setSolucao(operacoesSolucoes.retornaMelhorSolucaoNaria(getLista_solucoes()));
      solucao = operacoesSolucoes.retornaMelhorSolucaoNaria(getLista_solucoes());
      
      return solucao;
  }
  
  public SolucaoNAria retorna_pior_Solucao(LinkedList<SolucaoNAria> list_solucao) {
    
      solucao = operacoesSolucoes.retornaPiorSolucaoNaria(getLista_solucoes());    
        
      return solucao;
  }

  public SolucaoNAria retorna_aleatoria_Solucao(LinkedList<SolucaoNAria> list_solucao) {
    
      solucao = operacoesSolucoes.retornaSolucaoAleatoriaNaria(getLista_solucoes());
  
      return solucao;
  }
          
  protected void agenteDestruidor() {
  
      if(this.getPoliticaDestruicao() == PoliticaDestruicao.Melhor){
          
          this.remove_solucao(getSolucaoMemoria(0));
      }
      if(this.getPoliticaDestruicao() == PoliticaDestruicao.Pior){
          
          this.remove_solucao(getSolucaoMemoria(getLista_solucoes().size() - 1));
      }
      if(this.getPoliticaDestruicao() == PoliticaDestruicao.Aleatorio){
          
          SecureRandom r = new SecureRandom();
          int sorteado = r.nextInt(getLista_solucoes().size());
          this.remove_solucao(getSolucaoMemoria(sorteado));
      }
      
      if(this.getPoliticaDestruicao() == PoliticaDestruicao.ProbabilidadeLinearMelhor){
          
          //??????????
      }
      if(this.getPoliticaDestruicao() == PoliticaDestruicao.ProbabilidadeLinearPior){
          
          //??????????
      }
  }

  private PoliticaDestruicao getPoliticaDestruicao() {
  
      return this.politicaDestruicao;
  }
   
  public static void main(String args_[]) throws Exception{
  
     /*Criamos uma instância do ServidorFinal e o iniciamos. Depois da chamada do método start(), a execução da thread
      principal continua. Deste ponto em diante, temos duas threads em execução: a principal e a auxiliar do servidor!
     */
      String args[]={"Memoria2","3000", "50","6"};
            
      if(args.length < 4){
      
          System.out.println("Está faltando argumentos !");
          System.out.println("Deve serguir o seguinte padrão: (NOME, PORTA,TAMANHO_MEMÓRIA, POLÍTICA_DESTRUIÇÃO)");
          System.exit(1);
      }
      String name = args[0];
      int portaComunicacao = Integer.parseInt(args[1]);
      int tamanho_memoria = Integer.parseInt(args[2]);
      PoliticaDestruicao polDestruicao = null;
      
      switch (Integer.parseInt(args[3])){
          case 1:
            polDestruicao = PoliticaDestruicao.Aleatorio;
            break;
          case 2:    
            polDestruicao = PoliticaDestruicao.Melhor;
            break;
          case 3:
            polDestruicao = PoliticaDestruicao.MelhorAleatorio;
            break;
          case 4:
            polDestruicao = PoliticaDestruicao.MelhorMelhor;
            break;
          case 5:
            polDestruicao = PoliticaDestruicao.MelhorPior;
            break;
          case 6:
            polDestruicao = PoliticaDestruicao.Pior;
            break;
          case 7:
            polDestruicao = PoliticaDestruicao.PiorAleatorio;
            break;
        case 8:
            polDestruicao = PoliticaDestruicao.ProbabilidadeLinearMelhor;
            break;
        case 9:
            polDestruicao = PoliticaDestruicao.ProbabilidadeLinearPior;
            break;
        default:
            System.out.println("Opção Inválida para a política de destruição");
            System.exit(0); 
    }              
    
    ServidorFinal_2 servidor2 = new ServidorFinal_2(name, portaComunicacao, tamanho_memoria, polDestruicao);
    servidor2.start();
    
    /*Bloquearemos a thread principal até que o usuário pressione ENTER. Assim evitamos que a aplicação termine
     e seja encerrada e deixamos o Servidor em execução !!!*/
    System.out.println("Pressione ENTER para para encerrar o servidor !");
    
    new Scanner(System.in).nextLine();
    
    /*Quando o usuário pressionar ENTER, a thread principal é desbloqueada e logo em seguida finalizamos o servidor,
     de forma normal !!!*/
    System.out.println("Quantidade de soluções na memória: "+servidor2.getNum_solucoes());
    servidor2.stop();
    
    //Fecha os arquivos com informações de LOG
    arquivoPrincipal_FAV.close();
    arquivoPrincipal_FAV2.close();
    arquivoPrincipal_MediaSobra.close();
    arquivoPrincipal_SomatorioSobra.close();
    arquivoPrincipal_NumBins.close();
    
    arquivoFAV1.close();
    arquivoFAV2.close();
    arquivoMediaSobra.close();
    arquivoSomatorioSobra.close();
    arquivoNumBins.close();
    arquivoFinalCompleto.close();
    arquivoPrincipalSolucao2.close();
        
    System.out.println("Aqui irei imprimir minhas soluções !!!");
    //servidor2.imprimeSolucoes();
    
  }
  
}
