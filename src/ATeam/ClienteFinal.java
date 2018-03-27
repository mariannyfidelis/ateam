package ATeam;

import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import SimulaGenetico.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ServicoAgente;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;

public class ClienteFinal implements Runnable{

  private Socket socketClientFinal;
  private ObjectInputStream  in;  //Entrada para leitura do socket
  private ObjectOutputStream out; //Saída para escrita no socket
  private boolean inicializado;
  private boolean executando;
  private Thread thread;
   
  //Outros atributos
  private String name;
  private ServicoAgente tipo_agente;
  private ETiposServicosAgentes operacao_agente;    //Tipo de operação realizada pelo agente
  private ETiposServicosServidor operacao_servidor; //Tipo de operação requerida da memória pelo agente 
  private int porta_comunicacao;
  
  //Construtor
  public ClienteFinal(String name, String endereco, int portacomunicacao, ETiposServicosAgentes eSAgn,
                                                                            ETiposServicosServidor eSServ) throws Exception{
    
      this.name = name;
      this.porta_comunicacao = portacomunicacao;
      this.operacao_servidor = eSServ;
      this.operacao_agente   = eSAgn;
      
      inicializado = false;
      executando = false;
    
      open(endereco, portacomunicacao);
  }
  
  private  void open(String endereco, int portacomunicacao) throws Exception{
  
    try{
      //Método open que estabelece conexão com o Servidor e obtem o InputStream e OutputStream da conexão;
      socketClientFinal = new Socket(endereco, portacomunicacao);
      
      //in  = new BufferedReader(new InputStreamReader(socketClientFinal.getInputStream()));
      //out = new PrintStream(socketClientFinal.getOutputStream());
      in  = new ObjectInputStream(socketClientFinal.getInputStream());
      out = new ObjectOutputStream(socketClientFinal.getOutputStream());
      
      inicializado = true;
      
    }
    catch(Exception e){
	
      System.out.println(e);
      close();
    }
  }

  protected void close(){//Libera os recursos alocados
      
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
  
  protected void start(){//Inicia a thread auxiliar, se possível. O objeto cliente precisa estar inicializado e não pode está executando !
    
    if(!inicializado || executando){
      
      return;      
    }
    
    executando = true;
    thread = new Thread(this);
    thread.start();   
  }
  
  protected void stop() throws Exception{ //Para a thread auxiliar de forma adequada, encerrando todos os recursos alocados.
    
    executando = false;
    
    if(thread != null){
       
      thread.join();
    }
  }
  
  protected boolean isExecutando(){
    
    return executando;
  }
  
  //private void send(String mensagem){
  
  //Os dois métodos seguintes define a escrita e a leitura de operações nas memórias por parte dos Agentes !!!
  protected void send(ObjetoComunicacaoMelhorado obcom){ 
        
      try {
          //out.println(mensagem);
          out.writeObject(obcom);
      } 
      catch (IOException ex) {
          
          System.out.println("Erro na Entrada-Saida do Objeto enviado pelo cliente");
          System.out.println(ex);
          
          close();
      }
  }
  
  protected ObjetoComunicacaoMelhorado receive(ObjectInputStream inputObj){
  
      try{
          ObjetoComunicacaoMelhorado objetoComunicacao = (ObjetoComunicacaoMelhorado) inputObj.readObject();
          
          return objetoComunicacao;
      }
      catch(IOException ex){
          System.out.println("Erro de Entrada e saída no receive do CLiente !!!");
          System.out.println(ex);
      }
      catch(ClassNotFoundException exClass){
          System.out.println("Erro no receive do CLiente problema de conversão de classe !!!");
          System.out.println(exClass);
      }
      
      return null;
  }
  
  //Esse método aqui será implementado em cada agente que herdará os métodos restantes de CLIENTEFINAL
  
  /*private ObjetoComunicacaoMelhorado processaInformacao(ObjetoComunicacaoMelhorado obComunicacao){
        
      return objetoCOmunciacao;
  }*/
  
  @Override
  public void run(){
        
    //Agora o método o run() que será executado pela thread auxiliar.....
    while(executando){ //Laço baseado no atributo executando !
    
        try{
	   socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
	   
	   //String mensagem = in.readLine(); //Original !!!
           
           //Leitura de informações envidas pelo servidor !!!
           ObjetoComunicacaoMelhorado obcom = receive(in);
           String mensagem = obcom.getMSGServicoAgente();
	   
	   if(mensagem == null){
	     System.out.println("Mensagem nula !");
             
	     break;
	   }
	   System.out.println("Mensagem enviada pelo servidor: " +mensagem);
	  
	}
	catch(Exception e){
	    System.out.println(e);
	    break;
	}
    }
    
    //Antes de encerrar devemos liberar todos os recursos !!!
    close();
    
  }
    
  protected String getName(){
    
      return name;
  }
  
  protected void setName(String name){
  
      this.name = name;    
  }
  
  protected int getPortaComunicacao(){
    
      return porta_comunicacao;
  }
  
  protected void setPortaComunicacao(int porta){    
        
      this.porta_comunicacao = porta;   
  }
  
  protected ServicoAgente getServicoAgente(){
  
      return tipo_agente;
  }
  
  protected void setServicoAgente(ServicoAgente servico){
  
      this.tipo_agente = servico;
  }
  
  protected ETiposServicosAgentes getTipoServico(){
  
      return operacao_agente;
  }
    
  protected void setTipoServico(ETiposServicosAgentes etipoServico){
      
      this.operacao_agente = etipoServico;
  }

  protected ETiposServicosServidor getTipoServicoServidor(){

      return operacao_servidor;
  }

  protected void setTipoServicoServidor(ETiposServicosServidor etipoServicoServidor){

      this.operacao_servidor = etipoServicoServidor;
  }
  
  public static void main(String args[]) throws Exception{

      System.out.println("Iniciando cliente ... ");
      System.out.println("Iniciando conexão com o servidor ... ");
    
      //Iniciamos criando um cliente passando os parâmetros do cliente !!!
      ClienteFinal cliente = new ClienteFinal("Grasp","localhost", 2525, ETiposServicosAgentes.Inicializacao_Grasp,
                                                                         ETiposServicosServidor.Inserir_Solucao);
    
    System.out.println("Conexão estabelecida com sucesso ...");
    
    /*É quem inicia a thread auxiliar. A partir desta linha, o cliente passa a receber 
    * as mensagens do servidor de forma ASSÍNCRONA */
    cliente.start(); 
    
    Scanner scanner = new Scanner(System.in);
    
    while(true){
    
       System.out.println("Digite uma mensagem : ");
       String mensagem = scanner.nextLine();
       
       if(!cliente.isExecutando()){
	
	 break;
       }
       
       cliente.send(new ObjetoComunicacaoMelhorado());
       
       if("FIM".equals(mensagem)){
	 break;
       }           
    }
    
    //Esse método além de encerrar a thread auxiliar, fecha os recursos alocados pelo cliente !!!
    System.out.println("Encerrando cliente ... ");
        try {
            cliente.stop();
        } catch (Exception ex) {}
  }
}
