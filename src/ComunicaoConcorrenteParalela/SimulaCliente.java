package ComunicaoConcorrenteParalela;

import Heuristicas.Solucao;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SimulaCliente implements Runnable{

    static String name;
    static int porta_comunicacao;
    static ServicoAgente tipo_agente;
    static SocketChannel socketChannel;
    static int operacao_agente;
        
    public SimulaCliente(int porta_comunicao_, String name_, ServicoAgente servico_){
        porta_comunicacao = porta_comunicao_;
        name = name_;
        tipo_agente = servico_;
        //run();
    }
    
    public static void criaConexaoServidor(int porta_comunicacao){

        System.out.println("Sou o agente >>"+ name);
        System.out.println("Criei um agente e vou conectá-lo ao servidor na porta ... "+porta_comunicacao);
        
        try {
            SocketChannel sChannel = SocketChannel.open();
            socketChannel = sChannel;
            
            sChannel.configureBlocking(false);
            
             if (sChannel.connect(new InetSocketAddress("localhost", porta_comunicacao))) {

    		ObjectInputStream ois = 
                     new ObjectInputStream(sChannel.socket().getInputStream());

                System.out.println("AGENTE >>"+ name);
    		String s = "";
    		
                while(!s.equalsIgnoreCase("FIM")){
                    s = (String)ois.readObject();
                    System.out.println("\nString is: '" + s + "'");
                }
                
                ois.close();
                sChannel.close();
             }
        }
        catch (IOException io) {
            System.out.println(io);
        }
        catch (ClassNotFoundException cfe) {
            System.out.println(cfe);
        }
    }
    public void execute(){
      
        ObjetoComunicacao objeto;
          //Pega os dados do InputStream do Socket      
          //Processa informação do socket de entrada e processa informação
          objeto = processaInformacao(lerSocketChannel(socketChannel, null));
  
          //Escrever o resultado para o socketChannel
          //Após o processamento é realizado novamente a serialização e enviar ao socket de saída
          escreveSocketChannel(serializaMensagem(objeto), socketChannel);
                       
          //Verificar se está implementado nos métodos
             //flush();
             //read.close();
             //write.close();
    }
    public ObjetoComunicacao processaInformacao(ByteBuffer byteBuffer) throws IllegalArgumentException{

      Solucao solucao = new Solucao();
      ObjetoComunicacao objeto = new ObjetoComunicacao();
      objeto = deserializaMensagem(byteBuffer);
      
      //Se for agente de Serviço A
      if(objeto.getServicoAgente().equals("servico_a")){
           //Vou retornar a melhor solução
          
          //solucao = servicoServidor.retornaMelhorSolucao(null);  //Passar memória adequada !!!
          
          objeto.setSolucao(solucao);
      }
      //Se for agente de Serviço B
      else if(objeto.getServicoAgente().equals("servico_b")){
          //Vou retornar a pior solução
          
          //solucao = servicoServidor.retornaPiorSolucao(null);       //Passar memória adequada !!!
          
          objeto.setSolucao(solucao);
      }
      //Se for agente de Serviço C
      else{
         //Vou retornar uma solucao aleatória
         
          //solucao = servicoServidor.retornaSolucaoAleatoria(null);   //Passar memória adequada !!!
         
          objeto.setSolucao(solucao);
      }
      
      return objeto;
    }

    public ByteBuffer lerSocketChannel(SocketChannel socket, SelectionKey selectKey){//SelectionKey selectKey
        
        //SocketChannel schannel = (SocketChannel) selectKey.channel();
        
        //Dado ou criar um ByteBuffer "allocateDirect()";
        ByteBuffer byteBuffer = ByteBuffer.allocate(porta_comunicacao);//Definir o nº de itens*4 !!!
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
            System.out.println(ioe);
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
            System.out.println(ex);
        }
        catch(IOException ioe){
            System.out.println(ioe);
        }
                
        return (ObjetoComunicacao) objeto;
    } 
        
    @Override
    public void run() {
        String args[] = null;
        main(args);    
    }
    public static void main(String args[]){
    //Chama Conexão com o Servidor
        int porta_comunicacao = 100;
        criaConexaoServidor(porta_comunicacao);
        
        //Verificar o encerramento da conexão 
    }
        
}
