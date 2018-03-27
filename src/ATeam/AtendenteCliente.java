package ATeam;

//Exige a implementação do método run(), que será executado por uma thread auxiliar;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import HHDInternal.SolucaoHeuristica;
import SimulaGenetico.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;


public class AtendenteCliente implements Runnable{

  //Atributos : Socket, Input, Output e o restante semelhante aos que definimos para o servidorfinal;
  private Socket socketCliente      = null;
  private ObjectInputStream  input  = null;
  private ObjectOutputStream output = null;
          
  private boolean inicializado;
  private boolean executando;
  
  private Thread thread;
  
  //Teste para Ateam
  ServidorFinal servidor;
  private int id;
 
  public AtendenteCliente(int id, Socket socket, ServidorFinal servidor) throws Exception{
    
    this.id = id;
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
        ObjetoComunicacaoMelhorado objetoCom = null; 
              
        //O Atendente irá processar a requisição do Cliente !
        objetoCom = processaInformacao();
        
        if("Terminei".equals(objetoCom.getMSGServicoAgente())){
        
            escreveInformacao(objetoCom);
            //close();//Encerra o Atendente
            break;
        }
        else{
            escreveInformacao(objetoCom);
        }
     
	//Utilizar o OutPut do AtendenteCliente !!!
        //Aqui o atendente dá retorno de sua ação para o Cliente !!!
        //output.writeObject(objetoCom);
        
        //System.out.println("MSG - Agente: "+objetoCom.getMSGServicoAgente());
      }
      //catch(SocketTimeoutException e){
      //    System.out.println(e);
      //    }
      catch(Exception e){
	System.out.println(e);
	break; //Qualquer outra excessão forçamos encerrar o AtendenteCliente !!!
      }
    }
    
    System.out.println("Encerrando conexão !");
    
    try {
        close(); //Ates de encerrar o método run(), liberamos todos os recursos alocados pelo AtendenteCliente!!!
    } catch (Exception ex) {
        System.out.println(ex); 
    }  
}

  synchronized public ObjetoComunicacaoMelhorado processaInformacao() throws IOException, InterruptedException{

    ObjetoComunicacaoMelhorado objeto;// = new ObjetoComunicacaoMelhorado();
    
    objeto = recebeInformacao();
    
    String mensagemServico = objeto.getMSGServicoAgente();
    System.out.println("Id -> "+ getId()+"    Recebi : "+mensagemServico);
    ETiposServicosAgentes  servicoAgente   = objeto.getTipoServicoAgente2();
    ETiposServicosServidor servicoServidor = objeto.getTipoServicoServidor();    
    
    SolucaoHeuristica solucao;
        
    /*if("Terminei".equals(mensagemServico)){
    
        return objeto;
    }*/
    //SERVIÇOS DE INICIALIZAÇÃO POR PARTE DO AGENTE NO SERVIDOR !
            
    if(servicoServidor ==  ETiposServicosServidor.Inserir_Solucao){
        
        if(servicoAgente == ETiposServicosAgentes.Inicializacao_Grasp){

            if(mensagemServico.equals("Ainda_Enviando")){
            
                solucao  = new SolucaoHeuristica(objeto.getSolucao());
                servidor.adiciona_solucao(solucao);
                
                //Retorna para o Cliente que está pronto para nova solicitação 1
                objeto.setMSGServicoAgente("OK");
                
                return objeto;
            }
            if(mensagemServico.equals("Terminei_Envio")){
            
                solucao  = new SolucaoHeuristica(objeto.getSolucao());
                servidor.adiciona_solucao(solucao);
                
                //Retorna para o Cliente que está pronto para nova solicitação 1
                objeto.setMSGServicoAgente("Terminei");
                
                return objeto;
            }
        }
        
        if(servicoAgente == ETiposServicosAgentes.Inicializacao_HHDHeuristic){

            System.out.println("Atendente para HHDHeuristic");
            if(mensagemServico.equals("Terminei_Envio")){
            
                solucao  = new SolucaoHeuristica(objeto.getSolucao());
                servidor.adiciona_solucao(solucao);
                
                //Retorna para o Cliente que está pronto para nova solicitação 1
                objeto.setMSGServicoAgente("Terminei");
                
                return objeto;
            }
        }
        if(servicoAgente == ETiposServicosAgentes.Inicializacao_Aleatoria){/*Não faz nada pro enquanto !!! */}
    
    }
    
    if(servicoServidor ==  ETiposServicosServidor.AtualizaSolucao){
    
        //Serviços de atualização de Memória do Servidor
        //Aqui ele adicionará uma solução atualizada na memória !!!
        System.out.println("Cliente quer atualizar solução na memória");
        if(servicoAgente == ETiposServicosAgentes.Escrever_Solucacao){
        
            if(servidor.getNum_solucoes() < servidor.getTamanho_memoria()){
                      
                servidor.adiciona_solucao(objeto.getSolucao());       
            
                //Retorna para o Cliente que está pronto para nova solicitação 1
                objeto.setMSGServicoAgente("Terminei");
                
                return objeto;
            }
            else{
                //Chama AgenteDestruidor
                servidor.agenteDestruidor();
                servidor.adiciona_solucao(objeto.getSolucao());       
                
                //Retorna para o Cliente que está pronto para nova solicitação 1
                objeto.setMSGServicoAgente("Terminei");
                
                return objeto;
            }
            
            //Aqui assim que escreve uma solução na memória esta deve ser ORDENADA !!! ???????????
        }
    }
    //SERVIÇOS DE CONSULTA POR PARTE DO AGENTE NO SERVIDOR !
    if(servicoAgente == ETiposServicosAgentes.Consulta_Solucao){
    
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
  
  private void escreveInformacao(ObjetoComunicacaoMelhorado objeto){
        
        try {
            output.writeObject(objeto);
            output.flush();
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    private ObjetoComunicacaoMelhorado recebeInformacao(){

        ObjetoComunicacaoMelhorado objeto = new ObjetoComunicacaoMelhorado();

        try{
            objeto = (ObjetoComunicacaoMelhorado) input.readObject();
        }
        catch(ClassNotFoundException cex){
            System.out.println(cex);
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    
        return objeto;
    }
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}