package ComunicaoConcorrenteParalela;

import Util.Funcoes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;

public class SimulaServidor{

    private int porta;
    private static String host;
    private static String name ="";
    private String tipo_mem = "";
    private SocketChannel socketChannel;
    ETiposServicosServidor tipoServicoServidor;
    IServidorServicos servicoServidor;
    private static ServerSocketChannel server;
    
    
    /*public SimulaServidor(int porta, String host, String name){
    
        this.porta = porta;
        this.host = host;
        this.name = name;   
        //this.tipo_mem = tipo_memoria.toString();
    }*/
    
    public int getPorta(){
     
        return this.porta;
    }
    
    public String getHost(){
      
        return this.host;
    }
    
    public String getName(){
      
        return this.name;
    }
           
    public void SimulaServidor(int porta, String host, String name, TipoMemoria tipo_memoria) throws IOException{
    
        if(tipo_memoria.equals(TipoMemoria.A)){
            this.porta = porta;
            this.host = host;
            this.name = name;
            tipo_mem = tipo_memoria.name();
            server = SimulaServidor.abreConexao(this.porta);
            ComunicaoAgenteServidor(server);
            //iniciaAgentesM1(this.porta);
            
        }
        //Terminar verificação da implementação abaixo !!!!
        else{
            this.porta = porta;
            this.host = host;
            this.name = name;
            tipo_mem = tipo_memoria.name();
            server = abreConexao(this.porta);
            //iniciaAgentesM2(this.porta);
            tipo_mem = "mem_B";
        }
        
        System.out.println("Criei os agentes adequados ao tipo de memória !!!");
    }
    
    public void setServicoServidor(IServidorServicos servicoServidor){
      
        this.servicoServidor = servicoServidor;
    }
  
    public SocketChannel getSocket(){
        
        return this.socketChannel;
    }
    
    public void setSocket(SocketChannel socketChannel){
      
        this.socketChannel = socketChannel;
    }  
          
    public static ServerSocketChannel abreConexao(int porta) throws IOException{
        
        System.out.println("Vou abrir uma conexão !");
        ServerSocketChannel serveChannel = null;
        
        try {
            System.out.println("Estabelecendo comunicação ..... ");
            
            serveChannel = ServerSocketChannel.open();
            serveChannel.configureBlocking(false);
            serveChannel.bind(new InetSocketAddress(porta));
            
            Funcoes.cria_PortMap(name, host, porta);
            
            server = serveChannel;
            
            System.out.println("Conexão aberta na porta : "+ porta+" !");
          //return serveChannel;
        }
        
        catch(BindException b){
        
            porta = ++porta;
            
            serveChannel = abreConexao(porta);
        
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
        
        catch(NotYetBoundException noyb){
        
            System.out.println(noyb);
                        
        }
        
        return serveChannel;
    }

    public void ComunicaoAgenteServidor(ServerSocketChannel servidor){
    
        ByteBuffer byteServidor = null;
        ByteBuffer buffer = null;
    	String obj ="TestandoTexto";
        
        boolean conectou = false;
                
        try{
            //Criando um selector
            Selector selector = Selector.open();
                 
            //Registra canal no selector
            SelectionKey key = servidor.register(selector, SelectionKey.OP_ACCEPT);
               
        while(true){
            
            //Esperar por eventos
            int num = selector.select();
        
            //Armazenar lista de eventos/keys pendentes
            Set selectedKeys = selector.selectedKeys();
            Iterator it = selectedKeys.iterator();
            
            while(it.hasNext()){

                //Seleciona a chave/key atual
                SelectionKey selKey = (SelectionKey) it.next();
                
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
                            
                        if(servChannel != null){

                             System.out.println("Alguém conectou !");

                             String teste = "\nConexão Estabelecida";

                             System.out.println("\nEnviando comunicação");
                             
                             buffer = ByteBuffer.allocate(4*teste.length());
                             byteServidor = ByteBuffer.allocate(4*teste.length());
                             
                             byteServidor = lerSocketChannel(servChannel, (SelectionKey) it.next());
                             //buffer.flip();
                             
                             //Leu algo do Socket Channel !!!
                             if(byteServidor.capacity() != 0){
                             
                                                             
                             
                             }
                             //servChannel.write(serializaMensagem(teste));
                             System.out.println("\nEnviado comunicação ...");
                             
                             System.out.println("\nVou aguardar comunicação do cliente ...");
                             
                             servChannel.read(buffer);
                             
                             System.out.println("Recebi a MSG --> "+ deserializaMensagem(buffer));
                             
                             
                             ObjetoComunicacao obcom = deserializaMensagem(byteServidor);
                             System.out.println("Objeto comunicação:\nSolução: "+obcom.getSolucao().getLista());
                             escreveSocketChannel(serializaMensagem(new ObjetoComunicacao(null, obj, null, null)), servChannel);
                     
                             servChannel.close();
                             servChannel.finishConnect();
                             System.out.println("Connection ended");
                     
                        
                             //return servChannel;
                        }
                        else{
                                //System.out.println("Escutando .....");
                        }        
                    }
                    catch(IOException e){
                        System.out.println(e);
                    }
                }
                else{
                    //Caso a selkey não seja IsAceptable
                }
             }
          }
          //return servChannel;
        }
        catch(IOException io){
        
        } 
        
        catch(NullPointerException nulo){
            System.out.println("Erro de ponteiro nulo ...");
            System.out.println(nulo);
        }
                    
    }
    
    public static void iniciaAgentesM1(int porta_comunicacao){
    
        JOptionPane.showMessageDialog(null, "Tenho que iniciar uma comunicacao com o servidor na porta "+ porta_comunicacao);
        //System.out.println("Tenho que iniciar com o servidor");
        
        SimulaCliente agente1 = new SimulaCliente(porta_comunicacao,"agente1", ServicoAgente.Combinacao);
        SimulaCliente agente2 = new SimulaCliente(porta_comunicacao,"agente2", ServicoAgente.Combinacao);
        
        Thread agente1_ = new Thread(agente1);
        Thread agente2_ = new Thread(agente1);
        
        agente1_.start();
        agente2_.start();
       // Agente agente3 = new Agente(porta_comunicacao,"agente3", ServicoAgente.C);
    }
    
    public static void iniciaAgentesM2(int porta_comunicacao){/*Implementar para 2 memoria*/ }    

    public ByteBuffer lerSocketChannel(SocketChannel socket, SelectionKey selectKey){//SelectionKey selectKey
        
        //SocketChannel schannel = (SocketChannel) selectKey.channel();
        
        //Dado ou criar um ByteBuffer "allocateDirect()";
        ByteBuffer byteBuffer = ByteBuffer.allocate(porta);//Definir o nº de itens*4 !!!
        try {
            //Limpar o buffer e ler bytes do socket
            byteBuffer.clear();
            int numReadBuffer = socket.read(byteBuffer);
            
            if(numReadBuffer == - 1){
                
                //Não tem mais bytes a ser lidos do canal
                socket.close();//Fecha o Canal.
            }
            else{
                byteBuffer.flip();
            }
        } 
        catch (IOException ioe) {
            System.out.println(ioe);
        }
        
        return byteBuffer;
    }
    
    //Escrever de um Buffer para um SocketChannel
    public void escreveSocketChannel(ByteBuffer byteBuffer, SocketChannel socket){
        try {
            //Preencha o buffer com os bytes a escrever
            
            //Preparar o buffer para leitura pelo socket
            byteBuffer.flip();
            
            int numWriteBuffer = socket.write(byteBuffer);
        }
        catch(IOException ioe){
            JOptionPane.showMessageDialog(null, ioe);
            //System.out.println(ioe);
        }
    }
    
    //Métodos para serializar e deserializar mensagens
    public ByteBuffer serializaMensagem(ObjetoComunicacao objeto){ //Passa o objeto no caso SOLUÇÃO
    
        byte[] bytes_obj = null;
        ObjectOutput out = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
               
        try{
            out = new ObjectOutputStream(baos);
            out.writeObject(objeto);
            bytes_obj = baos.toByteArray();
            
            out.close();
            baos.close();
        }
        catch(IOException ioe){
            System.out.println(ioe);
        }
        
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes_obj);
        
        return  byteBuffer;
    }
    
    public ObjetoComunicacao  deserializaMensagem(ByteBuffer byteBuffer){
        
        ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer.array());
        ObjectInput entrada = null;
        Object objeto = null;
        
        try{
            entrada = new ObjectInputStream(bais);
            objeto = entrada.readObject();
            
            bais.close();
            entrada.close();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Nenhuma classe encontrada !!!");
            System.out.println(ex);
        }
        catch(IOException ioe){
            
            System.out.println("Não há mensagem de retorno !!!");
            System.out.println(ioe);
        }
                
        return (ObjetoComunicacao) objeto;
    } 
    
    public Selector criaSelector(String host, int porta_com){
        
        Selector selector = null;
        try {
            //Criar canais de sockets e associá-los a um selector
            SocketChannel sc1 = SocketChannel.open(new InetSocketAddress(host, porta_com));
            SocketChannel sc2 = SocketChannel.open(new InetSocketAddress(host, porta_com));
            
            //Registra os canais ao selector
            sc1.register(selector, sc1.validOps());
            sc2.register(selector, sc2.validOps());
            
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
        
        return selector;
    }
    
    public void processaSelectionKey(SelectionKey selectionKey) throws IOException{
    
        ByteBuffer byteBuffer = null;
        //Verifica prontidão e tipo de operação
        if((selectionKey.isValid()) && (selectionKey.isConnectable())){
        
            SocketChannel socketC = (SocketChannel) selectionKey.channel();
            boolean sucesso = socketC.finishConnect();
                
            if(!sucesso){
            
                //Um erro ocorreu
                selectionKey.cancel();
            }
        }
        if((selectionKey.isValid()) && (selectionKey.isReadable())){
        
            SocketChannel socketC = (SocketChannel) selectionKey.channel();
            lerSocketChannel(socketC, selectionKey);
        }
        if((selectionKey.isValid()) && (selectionKey.isWritable())){
        
            SocketChannel socketC = (SocketChannel) selectionKey.channel();
            escreveSocketChannel(byteBuffer, socketC);
        }
    }
      
    public static void main(String args[]) throws IOException{
		
        //Selector selector = Selector.open();
        ServerSocketChannel serve;

        try {

            System.out.println("Iniciando o servidor ... ");

            serve = ServerSocketChannel.open();
            serve.configureBlocking(false);

            ServerSocket ss = serve.socket();
            InetSocketAddress adress = new InetSocketAddress(2525);
            ss.bind(adress);

            int cont = 0;

            while(cont < 2000){

                    SocketChannel scnl = serve.accept();

                    if(scnl != null){
                            System.out.println("oi");

                            if(scnl != null){

                                    System.out.println("Fiz a primeira conexão ...");
                            }

                            if(scnl.isConnected() != false){

                                    InputStream entrada = scnl.socket().getInputStream();
                                    OutputStream saida = scnl.socket().getOutputStream();
                            }
                            else{
                                    System.out.println("Opppapapapap");
                            }
                    }
                    else{
                            System.out.println("Não conectou ninguém ainda !");
                    }
                    cont++;
            }		
        } catch (IOException e) {
                System.out.println(e);
        }
    }
}