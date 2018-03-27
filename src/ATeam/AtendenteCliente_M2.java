package ATeam;

import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoTree;
import SimulaGenetico.ETiposServicosAgentes;
import j_HeuristicaArvoreNAria.SolucaoNAria;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AtendenteCliente_M2 implements Runnable{

    //Atributos : Socket, Input, Output e o restante semelhante aos que definimos para o servidorfinal;
  private Socket socketCliente      = null;
  private ObjectInputStream  input  = null;
  private ObjectOutputStream output = null;
          
  private boolean inicializado;
  private boolean executando;
  
  private Thread thread;
  
  //Teste para Ateam
  ServidorFinal_2 servidor;
  
  public AtendenteCliente_M2(Socket socket, ServidorFinal_2 servidor) throws Exception{
    
    this.socketCliente = socket;
    this.inicializado = false;
    this.executando = false;
    this.servidor = servidor;
    
    open(); //Esse método é análogo ao método do Servidor. Irá iniciar o InputStream e o OutputStream e setar inicializado verdadeiro
  }
  
  private void open() throws Exception{ /*Verificar como adaptar o InputStream e o OutputStream*/
    
    try{
    
      //input  = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
      //output = new PrintStream(socketCliente.getOutputStream());
           
      input  = new ObjectInputStream(socketCliente.getInputStream());
      output = new ObjectOutputStream(socketCliente.getOutputStream());
                  
      inicializado = true; 
       
      System.out.println("Iniciei um atendente ....");
    }
    catch(Exception e){
        try {
            close();
        } 
        catch (IOException ex) {
            System.out.println(ex);            
        }
        catch (Exception ex) {
            System.out.println(ex);
            throw e;
        }
      
    }       
  }

  private void close() throws Exception{ //O método para a liberação dos recursos alocados pelo AtendenteCliente
    
    if(input != null){
      try{
	  input.close();	
      }
      catch(Exception e){
	  System.out.println(e);	
      }
    }
    
    if(output != null){
      try{
	  output.close();	
      }
      catch(Exception e){
	  System.out.println(e);	
      }
    }
 
    try{
	socketCliente.close();	
    }
    catch(Exception e){
	System.out.println(e);	
    }
    
    //Reinicia os atributos
    input   = null;
    output  = null;
    socketCliente = null;
    
    inicializado = false;
    executando   = false;
    
    thread = null;
  }
  
  public  void start() throws Exception{
    
    //Esse método cria e inicia a thread auxiliar, muda o estado do AtendenteCliente para executando = true.
    if(!inicializado || executando){
	return;      
    }
    
    executando = true;
    thread = new Thread(this);
    thread.start();
  }
  
  public  void stop()  throws Exception{
    
    //Esse método altera o estado do AtendenteCliente para executando = false. O que faz com que a thread auxiliar encerre;
    //Espera até que a thread auxiliar seja finalizada !
    executando = false;
    
    if(thread != null){
    
      thread.join();
      
    }
  }
  
  @Override
  public void run(){

    // Dentro desse método a construção é muito semelhante a construção do Servidor. Copiaremos por enquanto o código que faz o
    // atendimento do cliente, implementado na versão básica do servidor. É este código que deve ser executado dentro da thread
    // auxiliar do AtendenteCliente !!!
    while(executando){

      try{

        socketCliente.setSoTimeout(2500);	

        //Primeiramente o Atendete recebe uma mensagem de requisição !!
        ObjetoComunicacaoTree objetoCom = null; 

        //O Atendente irá processar a requisição do Cliente !
        objetoCom = processaInformacao();

        if("Terminei".equals(objetoCom.getMSGServicoAgente())){

            escreveInformacao(objetoCom);
            
            break;
        }
        else{
            escreveInformacao(objetoCom);
        }
     }
     catch(Exception e){
        System.out.println(e);
        break; //Qualquer outra excessão forçamos encerrar o AtendenteCliente !!!
     }
   }
      System.out.println("Encerrando conexão !");

      try {
         close(); //Ates de encerrar o método run(), liberamos todos os recursos alocados pelo AtendenteCliente!!!
      }
      catch (Exception ex) {
         System.out.println(ex); 
      }  
  }


  private ObjetoComunicacaoTree processaInformacao() throws IOException{
  
    ObjetoComunicacaoTree objeto;// = new ObjetoComunicacaoMelhorado();
    
    objeto = recebeInformacao();
    
    String mensagemServico = objeto.getMSGServicoAgente();
    System.out.println("Recebi MSG do Cliente:  "+mensagemServico);
    ETiposServicosAgentes  servicoAgente   = objeto.getTipoServicoAgente2();
    ETiposServicosServidor servicoServidor = objeto.getTipoServicoServidor();
  
    SolucaoNAria solucao;
    
    if(servicoServidor ==  ETiposServicosServidor.Inserir_Solucao){
        
        if(servicoAgente ==  ETiposServicosAgentes.Escrever_Solucacao){
            
            if(mensagemServico.equals("Escrevendo Solução")){
            
                solucao  = new SolucaoNAria(objeto.getSolucao());
                servidor.adiciona_solucao(solucao);
                
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
            solucao = servidor.retorna_melhor_Solucao(servidor.getLista_solucoes());  //Passar memória adequada !!!
            objeto.setSolucao(solucao);
            objeto.setMSGServicoAgente("SolucaoPronta");
            return objeto;
        }
        //Se for agente de Serviço B
        else if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.PiorSolucao)){
                    
            //Vou retornar a pior solução
            solucao = servidor.retorna_pior_Solucao(servidor.getLista_solucoes());       //Passar memória adequada !!!
            objeto.setSolucao(solucao);
            objeto.setMSGServicoAgente("SolucaoPronta");
            
            return objeto;
        }
        //Se for agente de Serviço C
        else if(objeto.getTipoServicoServidor().equals(ETiposServicosServidor.Solucao_Aleatoria)){
            //Vou retornar uma solucao aleatória
            solucao = servidor.retorna_aleatoria_Solucao(servidor.getLista_solucoes());   //Passar memória adequada !!!
            objeto.setSolucao(solucao);
            objeto.setMSGServicoAgente("SolucaoPronta");
                    
            return objeto;
        }
        else{/*Aqui ele deseja atualiza a memória*/}
    }
    
    return objeto;
  }
 
  private void escreveInformacao(ObjetoComunicacaoTree objeto){

        try {
            output.writeObject(objeto);
            output.flush();
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

  private ObjetoComunicacaoTree recebeInformacao() {

        ObjetoComunicacaoTree objeto = new ObjetoComunicacaoTree();

        try{
            objeto = (ObjetoComunicacaoTree) input.readObject();
        }
        catch(ClassNotFoundException cex){
            System.out.println(cex);
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    
        return objeto;
  }
}
