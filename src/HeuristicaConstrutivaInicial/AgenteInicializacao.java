package HeuristicaConstrutivaInicial;

import AgCombinacao.Agentes;
import AgCombinacao.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacao;
import ComunicaoConcorrenteParalela.ServicoAgente;
import Heuristicas.Solucao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class AgenteInicializacao extends Agentes{

    /*########################################################################################
                AGENTE ENCARREGADO DE INICIALIZAR A MEMÓRIA COM SOLUÇÕES INICIAIS
    ########################################################################################*/
    
    public AgenteInicializacao(int porta_comunicao, String name, ServicoAgente servico_, ETiposServicosAgentes tipoServicos){

        super.setName(name);
        super.setPortaComunicacao(porta_comunicao);
        
        super.setServicoAgente(servico_);
        super.setTipoServico(tipoServicos);
        
        //super.criaConexaoServidor(super.getPortaComunicacao());
    }
        
    public SocketChannel criaConexaoServidor(){
        return super.criaConexaoServidor2(super.getPortaComunicacao());
    }
    
    @Override
    public void escreveSocketChannel(ByteBuffer byteBuffer, SocketChannel socket){
        super.escreveSocketChannel(byteBuffer, socket);
    }
    
    @Override
    public ByteBuffer lerSocketChannel(SocketChannel socket, SelectionKey selectKey){
        return super.lerSocketChannel(socket, selectKey);
    }
    
    @Override
    public ByteBuffer serializaMensagem(ObjetoComunicacao objeto){
        return super.serializaMensagem(objeto);
    }
    
    @Override
    public ObjetoComunicacao deserializaMensagem(ByteBuffer byteBuffer){
        return super.deserializaMensagem(byteBuffer);
    }

    public int processaSelectionKey(SelectionKey selKey, LinkedList<Heuristicas.Solucao> vetorSolucao) throws IOException, InterruptedException{
        
        int op = 0;
        ByteBuffer buffer = ByteBuffer.allocate(4040);
        ObjetoComunicacao objcom = new ObjetoComunicacao();
        
        SocketChannel socketChannel = null;
        String texto = "";
        
        if(selKey.isValid() && selKey.isConnectable()){
        
            System.out.println("Estou pronto na conexão !!!");
            
            //Armazena o canal com a requisição
            socketChannel =  (SocketChannel) selKey.channel();
                
            boolean sucesso =  socketChannel.finishConnect();
            
            if(!sucesso){
            
                //Um erro ocorreu poque o processo não foi completado !
                //Retira do registro a chave/key
                System.out.println("Aconteceu um erro na conexão !");
                selKey.cancel();
            }
            
            selKey.interestOps(SelectionKey.OP_READ);
                    
        }
        if(selKey.isValid() && selKey.isReadable()){
            
            System.out.println("Estou pronto para leitura !!!");
            socketChannel =  (SocketChannel) selKey.channel();
            
            System.out.println("Recebendo comunicação do servidor !");
            
            //Limpar o buffer
            buffer.clear();
            
            int num_reader = socketChannel.read(buffer);
            
            if(num_reader == -1){
            
                //Não há mais bytes a ser lido
                socketChannel.close();
            }
            else{
                buffer.flip();
            }
            
            objcom = deserializaMensagem(buffer);

            System.out.println("Comunicação recebida ... --> "+objcom.getServicoAgente());
            
            op = 0;
            
            selKey.interestOps(SelectionKey.OP_WRITE);
        }
        
        if (selKey.isValid() && selKey.isWritable()) {
            
            
            //############## Deve Enviar Aqui as Soluções para  a Memória ########################
            socketChannel =  (SocketChannel) selKey.channel(); 
            String text = "Recebi e está tudo OK --> ";
            ObjetoComunicacao obc = null;// new ObjetoComunicacao(null, text, 
                                    //ETiposServicosAgentes.agente_inicializacao, ETiposServicosServidor.AtualizaSolucao);
           
            System.out.println("Estou pronto para escrita na memória adequada !!!");
            //List<ByteBuffer> list_Buffer = new ArrayList<ByteBuffer>();
            Queue<ByteBuffer> fila_buffer = new ArrayDeque<ByteBuffer>();
            //socketChannel.write(serializaMensagem(obc));
                      
            for(int i = 0; i < vetorSolucao.size(); i++){
        
                Heuristicas.Solucao soluc = vetorSolucao.get(i);
                soluc.setCapacidadeRespeitada(true);
                soluc.calculaFitness();
                soluc.setLista(soluc.getLista());
               
                buffer = serializaMensagem(new ObjetoComunicacao(new Solucao(true, soluc.getLista(), soluc.getFitness()), text+(i+1), 
                                    ETiposServicosAgentes.agente_inicializacao, ETiposServicosServidor.AtualizaSolucao));
               
                fila_buffer.add(buffer);
                
                //Imprimir a solução enviada pelo socket
                //Heuristicas.Solucao.imprimeSolucao(soluc, i+1);
            }
            System.out.println("Vou imprimir aqui a lista de buffer !\n");
            int cont = 1;
            
            while(!fila_buffer.isEmpty()){
            
                Solucao.imprimeSolucao(deserializaMensagem(fila_buffer.poll()).getSolucao(), cont++);
                socketChannel.write(fila_buffer.poll());
                
            }
            
            //Após escrita aguarda leitura de que tudo ocorreu bem e encerra comunicação
            if(fila_buffer.isEmpty()){
            
                socketChannel.write(serializaMensagem(new ObjetoComunicacao(null, "Fim", null,null)));
                selKey.interestOps(SelectionKey.OP_READ);            
            }
            
           
             op = 1;
        }
              
        else{
            System.out.println("Nada para escrever !!!");
        }

        return op;   
   }    
    //###################### MÉTODO PRINCIPAL DO AGENTE DE INICIALIZAÇÃO ###############################
    
    public static void main(String[] args) throws FileNotFoundException, IOException, PecaInvalidaException {

        int breake = 0;
        Selector selector = null;
        SocketChannel socketChannel = null;
        ObjetoComunicacao ob_comunicacao = new ObjetoComunicacao();
        
        List<Objeto> Solucao = new ArrayList<Objeto>();

        LinkedList<Heuristicas.Solucao> VetorSolucao = new LinkedList<Heuristicas.Solucao>();
        
        LinkedList<HeuristicaConstrutivaInicial.Solucao> ListSolucao = 
                                        new LinkedList<HeuristicaConstrutivaInicial.Solucao>();

        ArrayList<Item> L = new ArrayList<Item>();
        List<Item> Lc = new ArrayList<Item>();

        L = Funcoes.LerArq();
        Funcoes.OrdenaList(L);

        Lc = (List<Item>) L.clone();
        Funcoes.ImprimeItens("Lista", Lc);

        //VetorSolucao = HeuristicaGRASP.Grasp2d(L,L, 50, 50,0.5, 50, null);
        ListSolucao = HeuristicaGRASP.Grasp2d(L,L, 50, 50,0.5, 50, null);
        
        System.out.println("Numero de Solucoes Geradas --> "+ VetorSolucao.size());

        //Preparar conexão desse agente
                
        AgenteInicializacao ag_inicializacao = new AgenteInicializacao(4040, " Inicializacao", ServicoAgente.Inicializacao,
                                                                               ETiposServicosAgentes.agente_inicializacao);
        socketChannel = ag_inicializacao.criaConexaoServidor();
        
        //Aqui deve operar da seguinte maneira quando retornar o socketChannel
        //Criar um selector e enviar as mensagens serializadas
             
        selector = Selector.open();
              
        //O interesse do agente de inicialização é conectar e escrever as soluções processadas.
        //SelectionKey selK = (SelectionKey)
        socketChannel.register(selector, socketChannel.validOps());
        
        //Espera por eventos
        while(true){
        
             selector.select();
             
             Set<SelectionKey> selKeys = selector.selectedKeys();
             Iterator iterator = selKeys.iterator();
             
             if(selKeys.isEmpty() == true){
      
                 System.out.println("Não contém nenhum elemento no selector !");
             }
             else{
                 System.out.println("Já existe evento no selector!!!");
             }
             
             //Processar cada selectedKey
             
             while(iterator.hasNext()){
             
                 SelectionKey selK = (SelectionKey) iterator.next();
                 
                 iterator.remove();
                 
                 //Processar os eventos
                 try {
                     breake = ag_inicializacao.processaSelectionKey(selK, VetorSolucao);
                     
                     if (breake == 1){
                     
                         break;
                     }
                 }
                 catch (Exception e) {
                     System.out.println("Erro  no Processa Selection key\n Saindo .....");
                     break;
                 }
             }
             if(breake == 1){
        
                 break;                
             }  
        }
       
        socketChannel.finishConnect();
       
     }//Fim do main 
            
}//Fim da classe