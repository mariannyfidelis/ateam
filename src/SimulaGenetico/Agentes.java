package SimulaGenetico;

import ComunicaoConcorrenteParalela.ObjetoComunicacao;
import ComunicaoConcorrenteParalela.ServicoAgente;
import Heuristicas.Individuo;
import Heuristicas.Solucao;
import algoritmosAgCombinacao.Filhos;
import algoritmosAgCombinacao.OperacoesSolucoes_Individuos;
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

public class Agentes implements AgentesServicos{

    String name;
    int porta_comunicacao;
    ServicoAgente tipo_agente;
    ETiposServicosAgentes operacao_agente;
        
    HeuristicaConstrutivaInicial.Solucao solucao = new HeuristicaConstrutivaInicial.Solucao();
    Individuo individuo1, individuo2;
	
    SocketChannel socketChannel = null;
    Filhos filhos = new Filhos();
    ObjetoComunicacao ob_com = new ObjetoComunicacao();
    OperacoesSolucoes_Individuos opSolucao = new OperacoesSolucoes_Individuos();
         
    public SocketChannel getSocketChannel(){
    
        return socketChannel;
    }
    
    @Override
    public void criaConexaoServidor(int porta_comunicacao){}
    
    @Override
    public SocketChannel criaConexaoServidor2(int porta_comunicacao) {
        
        System.out.println("Sou o agente >> "+ this.name);
        System.out.println("Criei um agente e vou conectá-lo ao servidor ...");
        System.out.println("A porta eh: "+porta_comunicacao);
        ObjetoComunicacao objeto_cliente = new ObjetoComunicacao();
                
        SocketChannel sChannel = null;
        try {
            
            InetSocketAddress addr = new InetSocketAddress(porta_comunicacao);
            sChannel = SocketChannel.open();
            socketChannel = sChannel;
            
            sChannel.configureBlocking(false);
            
            System.out.println("Iniciando Conexão !!!");
            
            sChannel.connect(addr);
            
            if(sChannel == null){
                System.out.println("Não criou o Socket Channel");
            }
            else{
                System.out.println("\nSocketChannel criado .. \nSaindo com o socket ...");
            }
             
            return sChannel;
               
        }
        catch (IOException io) {
            System.out.println(io);
        }
     
        return sChannel;
    }

    @Override
    public void escreveSocketChannel(ByteBuffer byteBuffer, SocketChannel socket) {
       
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

    @Override
    public ByteBuffer lerSocketChannel(SocketChannel socket, SelectionKey selectKey) {
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

    @Override
    public ByteBuffer serializaMensagem(ObjetoComunicacao objeto) {
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
    @Override
    public ObjetoComunicacao deserializaMensagem(ByteBuffer byteBuffer) {
        
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
    
    //Acho que aqui seria interessante o processamento de selection Keys;
    
}
