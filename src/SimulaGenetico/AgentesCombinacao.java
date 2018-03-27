package SimulaGenetico;

import java.util.List;
import java.net.Socket;
import Utilidades.Chapa;
import java.util.Iterator;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.LinkedList;
import Heuristicas.Individuo;
import ATeam.ETipoHeuristicas;
import ATeam.IniciaClientes;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Utilidades.PoliticaSelecao;
import HHDInternal.SolucaoHeuristica;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import HeuristicaConstrutivaInicial.Bin;
import j_HeuristicaArvoreNAria.SolucaoNAria;
import ComunicaoConcorrenteParalela.ServicoAgente;
import j_HeuristicaArvoreNAria.Metodos_heuristicos;
import ComunicaoConcorrenteParalela.ObjetoComunicacao;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoTree;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;
import HeuristicaConstrutivaInicial.HeuristicaGRASP;
import j_HeuristicaArvoreNAria.Pedidos;
import java.net.SocketException;

public class AgentesCombinacao extends Agentes implements Runnable{

    private Socket socketClientFinal;
    private ObjectInputStream     in;  //Entrada para leitura do socket
    private ObjectOutputStream   out; //Saída para escrita no socket        
    
    private boolean inicializado;
    private boolean executando;
    private boolean isFirst;
    private boolean rotaciona;
    private Thread  thread;
    private String  name;
    private ETiposServicosAgentes  operacao_agente;    //Tipo de operação realizada pelo agente
    private ETiposServicosAgentes  operacao_agente2;
    private ETiposServicosServidor operacao_servidor1; //Tipo de operação requerida da memória pelo agente 
    private ETiposServicosServidor operacao_servidor2;
    private ETipoHeuristicas       tipoheuristica;
    private int porta_comunicacao, porta_comunicacao2;
    private ServicoAgente          tipo_agente;    
    ObjetoComunicacaoMelhorado     objetoM1 = null;// = new ObjetoComunicacaoMelhorado();
    ObjetoComunicacaoTree          objetoM2 = null;
    
    PoliticaSelecao polSelecaoSolucao = null;
    SolucaoHeuristica solucao = new SolucaoHeuristica();
    private int maxIter;
    
    /*########################################################################################
                MÉTODOS QUE ESTABELECEM COMUNICAÇÃO COM O SERVIDOR
    ########################################################################################*/
   
    public AgentesCombinacao(){  }
    
    public AgentesCombinacao(ETiposServicosAgentes operacaoAgente){
    
        this.operacao_agente = operacaoAgente;
    }
    
    public AgentesCombinacao(String name, String endereco, int porta1, int porta2, ETiposServicosAgentes eSAgn, 
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
         
         isFirst = false;
         rotaciona = false;
         
         objetoM1 = new ObjetoComunicacaoMelhorado();
         objetoM2 = new ObjetoComunicacaoTree();
         //open(endereco, porta1);
        
    }
   
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
        }catch(SocketException se){
            System.out.println("Exceção conexão recusada");
            System.out.println(se);
            close();
        }catch(IOException e){
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

          //inicializado = false;
          //executando   = false;
          thread = null;
  }     
  
    public void start(){//Inicia a thread auxiliar, se possível. O objetoM1 cliente precisa estar inicializado e não pode está executando !
    
        if(executando){

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
    
    public void send(ObjetoComunicacaoMelhorado obcom){ 
        
        try {
            //out.println(obcom.getMSGServicoAgente());
            out.writeObject(obcom);
            out.flush();
        } catch (IOException ex) {
            
            System.out.println("Erro na Escrita do Objeto enviado pelo cliente");
            System.out.println(ex);
            close();
      }
    }
  
    public ObjetoComunicacaoMelhorado receive(){
  
        ObjetoComunicacaoMelhorado objetoComunicacao = null;
       
        try{
            objetoComunicacao = (ObjetoComunicacaoMelhorado) in.readObject();
        } catch(IOException ex){
            System.out.println("Erro de leitura no receive do CLiente !!!");
            System.out.println(ex);
            //receive();
        } catch(ClassNotFoundException exClass){
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
        } catch(IOException ex){
            System.out.println("Erro de Entrada e saída no receive do CLiente !!!");
            System.out.println(ex);
        } catch(ClassNotFoundException exClass){
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
    
    public int getPortaComunicacao2(){
    
        return porta_comunicacao2;
    }
    
    public void setPortaComunicacao2(int porta2){
    
        this.porta_comunicacao2 = porta2;
    }
  
    public boolean IsFirst(){
    
        return isFirst;
    }
    
    public void setIsFirst(boolean firstFit){
    
        this.isFirst = firstFit;
    }
    
    public boolean getRotaciona(){
    
        return rotaciona;
    }
    
    public void setRotaciona(boolean rotaciona){
    
        this.rotaciona = rotaciona;
    }
    
    public void setNumiteracoes(int i) {
        this.maxIter = i;
    }
    
    public int getNumiteracores(){
    
        return this.maxIter;
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
    
    public ETiposServicosAgentes getTipoServico2(){
  
        return operacao_agente2;
    }
    
    public void setTipoServicoAg2(ETiposServicosAgentes etipoServico){
      
        this.operacao_agente2 = etipoServico;
    }

    public ETiposServicosServidor getTipoServicoServidor(){

        return operacao_servidor1;
    }
    
    public void setTipoServicoServidor(ETiposServicosServidor etipoServicoServidor){

        this.operacao_servidor1 = etipoServicoServidor;
    }
  
    public ETiposServicosServidor getTipoServicoServidor2(){

        return operacao_servidor2;
    }

    public void setTipoServicoServidor2(ETiposServicosServidor etipoServicoServidor){

        this.operacao_servidor2 = etipoServicoServidor;
    }
    
    public ETipoHeuristicas getTipoHeuristica(){
    
        return this.tipoheuristica;
    }
    
    public void setTipoHeuristica(ETipoHeuristicas etipoheuristica){
    
        this.tipoheuristica = etipoheuristica;
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
        //solucion = m.FFIH_BFIH(getRotaciona(), IsFirst(), recebeIndividuo_ListPedidosSolucaoCompleta(solucao), new Chapa(larg, alt));
        solucion = m.FFIH_BFIH(getRotaciona(), IsFirst(), recebeIndividuo_ListPedidos(solucao), new Chapa(larg, alt));
        solucion.calculaSomatorioSobra();
        solucion.setTipoHeuristic(getTipoHeuristica());
        solucion.imprime_solucao();
       
        return solucion;
    } 
   
   //Outro método que já pega o Indivuo único da solução
   public LinkedList<j_HeuristicaArvoreNAria.Pedidos> recebeIndividuo_ListPedidosSolucaoCompleta(SolucaoHeuristica solucao){
   
       Integer refPedido;
       Pedidos vetPedido[] = Utilidades.Funcoes.getVetor_info();
       LinkedList<j_HeuristicaArvoreNAria.Pedidos> n_pedidos = new LinkedList<j_HeuristicaArvoreNAria.Pedidos>();
       
       j_HeuristicaArvoreNAria.Pedidos pedidos_arvore;
       List<Integer> list_item = new ArrayList<Integer>();
       Individuo ind =  new Individuo(solucao.getIndividuo());
       
       list_item = ind.getListaItens();
       System.out.println("Minha lista de itens --> "+list_item);
       Iterator<Integer> it_integer = list_item.iterator();
       
       while(it_integer.hasNext()){
       
           refPedido = it_integer.next();
           //pedidos_arvore = (j_HeuristicaArvoreNAria.Pedidos) solucao.getIPedidos().get(refPedido.intValue());
           //pedidos_arvore = solucao.retornaPedido(refPedido.intValue(), solucao.getIPedidos());
           pedidos_arvore = vetPedido[refPedido-1];
           n_pedidos.add(new j_HeuristicaArvoreNAria.Pedidos(pedidos_arvore));
           System.out.println("Pedido criado combinação -Id - "+pedidos_arvore.id()+" L - ("+pedidos_arvore.retornaLargura()+")  A - ("+pedidos_arvore.retornaAltura()+" )");
       }
       
       //Verificar se isso aqui funciona beleza ?!!!
       //LinkedList<j_HeuristicaArvoreNAria.Pedidos> p = solucao.getIPedidos();
       ////j_HeuristicaArvoreNAria.Pedidos[] vetPedid = new j_HeuristicaArvoreNAria.Pedidos[p.size()];
       
       //Utilidades.Funcoes.atribui_info(p, vetPedid);
       
       return n_pedidos;
   }
    
   //Esse método vai lá pra árvore N-aria
   public LinkedList<j_HeuristicaArvoreNAria.Pedidos> recebeIndividuo_ListPedidos(SolucaoHeuristica solucao){
   
       Integer refPedido;
       Pedidos vetPedido[] = Utilidades.Funcoes.getVetor_info();
       //Aqui deve-se selecionar a Lista de Objetos
       LinkedList<j_HeuristicaArvoreNAria.Pedidos> n_pedidos = new LinkedList<j_HeuristicaArvoreNAria.Pedidos>();
       
       //Vou ordenar minha solução
       HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solucao);//Seria interessante ordenar por aproveitamento !
       j_HeuristicaArvoreNAria.Pedidos pedidos_arvore;
       
       List<Integer> list_item = new ArrayList<Integer>(retornaSequenciaEncaixe(solucao));
       Iterator<Bin> iter_objeto = solucao.getObjetos().iterator();
       
       Iterator<Integer> it_integer = list_item.iterator();
       while(it_integer.hasNext()){
            //for(int i = 0; i < list_item.size(); i++){
                
                //refPedido = list_item.get(i);
                refPedido = it_integer.next();
                //pedidos_arvore = (j_HeuristicaArvoreNAria.Pedidos) solucao.getIPedidos().get(refPedido.intValue());
                pedidos_arvore = vetPedido[refPedido-1];
                
                //Tirei isso aqui pq tava dando erro
                //pedidos_arvore = solucao.retornaPedido(refPedido.intValue(), solucao.getIPedidos());
                
                n_pedidos.add(new j_HeuristicaArvoreNAria.Pedidos(pedidos_arvore));
            }
       
       /*while(iter_objeto.hasNext()){
        
            Individuo ind =  new Individuo();
            ind = iter_objeto.next().getIndividuo();

            list_item = ind.getListaItens();
            
            Iterator<Integer> it_integer = list_item.iterator();
            
            while(it_integer.hasNext()){
            //for(int i = 0; i < list_item.size(); i++){
                
                //refPedido = list_item.get(i);
                refPedido = it_integer.next();
                //pedidos_arvore = (j_HeuristicaArvoreNAria.Pedidos) solucao.getIPedidos().get(refPedido.intValue());
                pedidos_arvore = vetPedido[refPedido-1];
                
                //Tirei isso aqui pq tava dando erro
                //pedidos_arvore = solucao.retornaPedido(refPedido.intValue(), solucao.getIPedidos());
                
                n_pedidos.add(new j_HeuristicaArvoreNAria.Pedidos(pedidos_arvore));
            }
        }*/
       
        /*Comentado pois não estava dando certo !!!*/
        //Verificar se isso aqui funciona beleza ?!!!
        //LinkedList<j_HeuristicaArvoreNAria.Pedidos> p = solucao.getIPedidos();
        //j_HeuristicaArvoreNAria.Pedidos[] vetPedid = new j_HeuristicaArvoreNAria.Pedidos[p.size()];
       //Utilidades.Funcoes.atribui_info(p, vetPedid);
        
        return n_pedidos;
       
       //Aqui transforma a lista de Individuos de cada objeto em uma ListPedidos
    }
    
    //////////////////////////////////  MÉTODOS ANTIGOS QUE PRECISAM SER TRATADOS ///////////////////////////////////////////
    @Override
    public void criaConexaoServidor(int porta_comunicacao){

        super.criaConexaoServidor(porta_comunicacao);
    }
        
    public void execute(ServicoAgente servicoAgente, ETiposServicosAgentes tipo_servico) {
       
       //ObjetoComunicacao objetoM1;
       ObjetoComunicacaoMelhorado objeto;
       
       //Pega os dados do InputStream do Socket      
       //Processa informação do socket de entrada e processa informação

//       objetoM1 = processaInformacao(lerSocketChannel(socketChannel, null), tipo_servico);
  
       //Escrever o resultado para o socketChannel
       //Após o processamento é realizado novamente a serialização e enviar ao socket de saída

//escreveSocketChannel(serializaMensagemMelhorado(objetoM1), socketChannel);
                       
       //Verificar se está implementado nos métodos
       //flush();
       
       //read.close();
       //write.close();
   }
    
    public ObjetoComunicacaoMelhorado processaInformacao(ObjetoComunicacaoMelhorado Obj, ETiposServicosAgentes tipo) throws IllegalArgumentException{

        SolucaoHeuristica solucao_ = new SolucaoHeuristica();
        ObjetoComunicacaoMelhorado objeto;// = new ObjetoComunicacaoMelhorado();
        //objeto = deserializaMensagemMelhorado(byteBuffer);
        objeto = Obj;
        switch(tipo){
         
             case TotalmenteAleatorioSolucao:
                  solucao_ = TotalmenteAleatorioSolucao(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case AleatorioPior:
                  solucao_ = AleatorioPior(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case AleatorioMelhor:
                  solucao_ = AleatorioMelhor(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case Melhor_Dois:
                  solucao_ = Melhor_Dois(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case Melhor_Pior:
                  solucao_ = Melhor_Pior(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case MelhorSol_Aleatorio:
                  solucao_ = MelhorSol_Aleatorio(objeto.getSolucao());  
                  objeto.setSolucao(solucao_);
             break;

             case MelhorSol_MelhorIndividuo:
                  solucao_ = MelhorSol_MelhorIndividuo(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;

             case AleatorioMelhorS:
                  solucao_ = AleatorioMelhorS(objeto.getSolucao()); 
                  objeto.setSolucao(solucao_);
             break;

             case MelhorSol_Melhor_DoisIndv:
                  solucao_ = MelhorSol_Melhor_DoisIndv(objeto.getSolucao()); 
                  objeto.setSolucao(solucao_);
             break;

             case MelhorSol_Maior_MenorIndv:
                  solucao_ = MelhorSol_Maior_MenorIndv(objeto.getSolucao());
                  objeto.setSolucao(solucao_);
             break;
        }
      
        return objeto;
    } 
   
    @Override
    public ByteBuffer lerSocketChannel(SocketChannel socket, SelectionKey selectKey){//SelectionKey selectKey
        
       return (super.lerSocketChannel(socket, selectKey));
   }
    
    //Escrever de um Buffer para um SocketChannel
    @Override
    public void escreveSocketChannel(ByteBuffer byteBuffer, SocketChannel socket){
   
        super.escreveSocketChannel(byteBuffer, socket);
   }
    
    //Métodos para serializar e deserializar mensagens
   @Override
   public ByteBuffer serializaMensagem(ObjetoComunicacao objeto){ //Passa o objetoM1 no caso SOLUÇÃO
    
       return (super.serializaMensagem(objeto));
   }
   
   @Override
   public ObjetoComunicacao  deserializaMensagem(ByteBuffer byteBuffer){
        
       return (super.deserializaMensagem(byteBuffer));
   } 
   
   /*@Override
   public ObjetoComunicacaoMelhorado  deserializaMensagemMelhorado(ByteBuffer byteBuffer){
        
       return (super.deserializaMensagemMelhorado(byteBuffer));
    } */
   
   
 /*#############################################################################################
 ###############################################################################################
 ###############################################################################################
 ###############################################################################################
                                MÉTODOS DOS AGENTES EXECUTAREM
 ##############################################################################################*/
   
   //CombinacaoAleat1 
   public SolucaoHeuristica TotalmenteAleatorioSolucao(SolucaoHeuristica solution_memoria){

        //Seleciona Solução Aleatória
        //solucao = opSolucao.retornaSolucaoAleatoria(memoria).clone();
        this.solucao.setSolucao(solution_memoria);		
        
        if((solution_memoria == this.solucao)||(solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Seleciona Indivíduos Aleatórios
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
                        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);	
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
    
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        //Atualizar a nova solução com os novos indivíduos gerados
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      
      return this.solucao;
    }
   
   //CombinacaoAleat2
   public SolucaoHeuristica AleatorioPior(SolucaoHeuristica solution_memoria){
		
        this.solucao.setSolucao(solution_memoria);
        
        
        if((solution_memoria == this.solucao)|| (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Pior Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
                
        while (individuo1.equals(individuo2)){

                individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;
    }
   
   //CombinacaoAleat3
   public SolucaoHeuristica AleatorioMelhor(SolucaoHeuristica solution_memoria){
		
        this.solucao.setSolucao(solution_memoria);
      
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);

        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
           individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

        if(solution_memoria.equals(solucao)){
             System.out.println("\n\nPODE HAVER PROBLEMAS !!! ");
        }
        
      return this.solucao;	
    }
   
   //CombinacaoAleat4
   public SolucaoHeuristica Melhor_Dois(SolucaoHeuristica solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);					
        
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
                
        while (individuo1.equals(individuo2)){
           individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;	
   }
   
   //CombinacaoAleat5
   public SolucaoHeuristica Melhor_Pior(SolucaoHeuristica solution_memoria){

        this.solucao.setSolucao(solution_memoria);				

        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
                
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);
        
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;	
    }
   
   //CombinacaoMelhor_S1
   public SolucaoHeuristica MelhorSol_Aleatorio(SolucaoHeuristica solution_memoria){

        this.solucao.setSolucao(solution_memoria);

        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Seleciona Indivíduos Aleatórios
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);	
        }
        this.solucao.removeIndividuos(individuo2);
        
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;
    }
   
   //CombinacaoMelhor S_2
   public SolucaoHeuristica MelhorSol_MelhorIndividuo(SolucaoHeuristica solution_memoria){
		
        this.solucao.setSolucao(solution_memoria);		

        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Pior Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;
    }
    
   //CombinacaoMelhor S_3
   public SolucaoHeuristica AleatorioMelhorS(SolucaoHeuristica solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);		
        
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
             individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;	
    }
   
   //CombinacaoMelhor S_4 
   public SolucaoHeuristica MelhorSol_Melhor_DoisIndv(SolucaoHeuristica solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);			
       
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
           individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        
        this.solucao.removeIndividuos(individuo2);
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

       return this.solucao;	
    }
   
   //CombinacaoMelhor S_5 
   public SolucaoHeuristica MelhorSol_Maior_MenorIndv(SolucaoHeuristica solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);			
        
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA TRABALHANDO COM A MESMA MEMÓRIA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

       return this.solucao;	
   }
   
   /*########################################################################################
              MÉTODOS DE EXECUÇÃO PRINCIPAL PARA OS AGENTES 
   ########################################################################################*/
      
    @Override
    public void run() {
        
        int num = 1;
        System.out.println("Iniciei o run do AgComb ...");
                             
        ObjetoComunicacaoMelhorado obcom  = getObjetoComunicacaoMelhorado();
        ObjetoComunicacaoTree  obcomTree  = getObjetoComunicacaoTree();

         while (!IniciaClientes.stop) {
             
             try {
                 Thread.sleep(150);//500
             } catch (InterruptedException ex) {}
            
             try{
                 
                 System.out.println("Connect 1 M1..."+this.toString());
                    
                 open("localhost", getPortaComunicacao());
                 
                 System.out.println("Connect 2 M1...");
                 //socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
                 ObjetoComunicacaoMelhorado ob = receive();
                 System.out.println("Connect 3 M1...");
                    
                 String msg = ob.getMSGServicoAgente();
                    
                 System.out.println("Ocupado M1?: "+msg);
                 
                 if (msg.equals("true")) {
                     System.out.println("Espere para reiniciar..");
                     close();
                     continue;
                 }
          
                 System.out.println("Vou iniciar o consulta AGMelhoria");
                 
                 if(obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao){

                    System.out.println("Entrei para solicitar solucao a memoria 1");

                    while(executando){ 

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

                            System.out.println("Terminei a busca para Simula Genético e setei a solução");

                            obcom.setMSGServicoAgente("Terminei_Envio");
                            send(obcom);

                            obcom = receive();
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
                    //Antes de encerrar devemos liberar todos os recursos !!!
                    close();

                    System.out.println("Aqui eu imprimo a solução recebida só para teste");
                    SolucaoHeuristica s = getObjetoComunicacaoMelhorado().getSolucao();
                    s.setTipoHeuristic(ETipoHeuristicas.SimulaGenetico);
                    s.imprimeSolucao(s, 1);
                    System.out.println("\nEncerrando conexão com a memória 1 !");

                    if(getObjetoComunicacaoMelhorado().getSolucao() == null){

                        System.out.println("\nA solução retornada é nula !!!");
                        //this.close();
                    }

                    //Ao encerrrar a consulta a memória  será aberto novamente outra conexão para escrita na outra memória
                    //Aqui ele deve aplicar um método para aplicar o firstFit ou o BestFit
                    ObjetoComunicacaoMelhorado obj = getObjetoComunicacaoMelhorado();
                    obj.setTipoServicoAgente(getTipoServico2());
                    //obj = this.processaInformacao(getObjetoComunicacaoMelhorado(), this.getTipoServico2());

                    //Aqui seria interessante ordenar as placas por aproveitamento e só depois passar para InsertionTree
                    SolucaoNAria solucaoArvore = new SolucaoNAria();
                    solucaoArvore = this.aplicaAGLArvoreN(obj.getSolucao());

                    ObjetoComunicacaoTree objTree = getObjetoComunicacaoTree();


                    System.out.println("Preparando para escrever solução Ag M2 ... ");
                    objTree.setMSGServicoAgente("Escrevendo Solução");
                    objTree.setSolucao(solucaoArvore);
                    objTree.setTipoServicoAgente(ETiposServicosAgentes.Escrever_Solucacao);
                    objTree.setTipoServicoServidor(ETiposServicosServidor.Inserir_Solucao);

                    this.setObjetoComunicacaoTree(objTree);

                    //Vou testar um while fora para ficar em loop até escrever a solução na M2
                    while(executando){
                    
                        try {
                            System.out.println("Vou iniciar comunicação com a Memória 2;");
                            System.out.println("Connect 1 M2 ..."+this.toString());
                            open("localhost", getPortaComunicacao2());
                            executando = true;
                            System.out.println("Connect 2 M2 ...");
                            //socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
                            ObjetoComunicacaoTree objM2 = receiveT();
                            System.out.println("Connect 3 M2...");
                            System.out.println("Conexão estabelecida com sucesso na Memória 2");

                            String msg_M2 = objM2.getMSGServicoAgente();                    
                            System.out.println("Ocupado?: "+msg_M2);

                            if (msg_M2.equals("true")) {
                                System.out.println("Espere para reiniciar..");
                                close();
                                continue;
                            }else{
                                break;
                            }
                        }catch (Exception ex) {
                            System.out.println("Não conseguiu estabelecer comunicação com a Memória 2");
                        }
                            //System.out.println("Vou iniciar comunicação com a Memória 2;");
                            //this.open("localhost", getPortaComunicacao2());
                            //executando = true;
                            //System.out.println("Conexão estabelecida com sucesso na Memória 2");
                    }//fim while-executando    

                    obcomTree = objTree;
                }//Depois verificar se aqui é o melhor lugar para o consulta !!!!

                if(obcomTree.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao){

                    //ObjetoComunicacaoTree obTree = getObjetoComunicacaoTree();
                    System.out.println("Entrei para escrever solucao na memoria 2");
                    System.out.println("Vou escrever uma solução na Memória 2;");
                    obcomTree.setMSGServicoAgente("Escrevendo Solução");
                    System.out.println("Vou enviar um objeto");
                    send(obcomTree);
                    
                    //Leitura de informações envidas pelo servidor !!!
                    System.out.println("Vou receber um objeto");
                    System.out.println("Aguardando confirmação do servidor 2");
                    obcomTree = receiveT();
                    String mensagem = obcomTree.getMSGServicoAgente();
                    
                    if(mensagem == null){
                        System.out.println("Mensagem nula !");
                        break;
                    }
                    if("Terminei".equals(mensagem)){
                        System.out.println("Servidor 2 escreveu uma solução !");
                        //break;
                    }
                    System.out.println("Mensagem enviada pelo servidor: " +mensagem);                       
                    System.out.println("Encerrando o Simula Genético de Combinação de forma correta verifique se a solução foi escrita ...");
                    System.out.println("Ocorreu tudo bem !!!");
                }
                
                close();    
             }catch (Exception e) {
                 System.out.println("Ocorreu alguma exceção !");
                 close();  //Antes de encerrar devemos liberar todos os recursos !!!
             }
         }
    }
    
    public static void main(String args[]) throws Exception{
        
        int porta1, porta2;
        boolean rotaciona = false;
        boolean  firstFit = false;
        String name = " ";
        ServicoAgente tipo_agente         = null;
        ETipoHeuristicas tipo_Heuristica  = null;
        ETiposServicosAgentes operacao_agente     = null;
        ETiposServicosAgentes operacao_agenteM2   = null;
        ETiposServicosServidor operacao_servidor1 = null;
        ETiposServicosServidor operacao_servidor2 = null; //Quando for escrever na memória 2 !!!!
        PoliticaSelecao politicaSelecaoSolucao    = null;
        
        if(args.length < 10){
            System.out.println("Está faltando argumentos Simula Genético Combinação !");
            System.exit(1);
        }
        
        /*##########################  CONFIGURANDO ARGUMENTOS DO ALGORITMO COMBINAÇÂO #########################################*/
        switch(Integer.parseInt(args[0])){
             
             case 1: tipo_Heuristica = ETipoHeuristicas.SimulaGenetico;                 break; 
        } 
        switch(Integer.parseInt(args[1])){
             
             case 2: tipo_agente = ServicoAgente.Combinacao;   name = "Combination";    break; 
        } 
        switch(Integer.parseInt(args[2])){
             
             case 1: operacao_agente = ETiposServicosAgentes.Consulta_Solucao;          break; 
             case 2: operacao_agente = ETiposServicosAgentes.Escrever_Solucacao;        break;
             default:
                 System.out.println("Nenhuma das opções são válidas para o algoritmo Combination!");
                 System.exit(1);
         }
        switch(Integer.parseInt(args[3])){
        
            case 1: operacao_servidor1 = ETiposServicosServidor.Inserir_Solucao;   break;
            case 2: operacao_servidor1 = ETiposServicosServidor.AtualizaSolucao;   break;
            case 3: operacao_servidor1 = ETiposServicosServidor.MelhorSolucao;     break;
            case 4: operacao_servidor1 = ETiposServicosServidor.PiorSolucao;       break;
            case 5: operacao_servidor1 = ETiposServicosServidor.Solucao_Aleatoria; break;
            case 6: operacao_servidor1 = ETiposServicosServidor.Roleta;            break;
            default:
                 System.out.println("Nenhuma das opções são válidas para o operador servidor");
                 System.exit(1);
        }
          //Serviços do Simula Genético Combinação
        switch(Integer.parseInt(args[4])){
             
             case 18: operacao_agenteM2 = ETiposServicosAgentes.TotalmenteAleatorioSolucao; break; 
             case 19: operacao_agenteM2 = ETiposServicosAgentes.AleatorioPior;              break; 
             case 20: operacao_agenteM2 = ETiposServicosAgentes.AleatorioMelhor;            break; 
             case 21: operacao_agenteM2 = ETiposServicosAgentes.Melhor_Dois;                break; 
             case 22: operacao_agenteM2 = ETiposServicosAgentes.Melhor_Pior;                break; 
             case 23: operacao_agenteM2 = ETiposServicosAgentes.MelhorSol_Aleatorio;        break; 
             case 24: operacao_agenteM2 = ETiposServicosAgentes.MelhorSol_MelhorIndividuo;  break; 
             case 25: operacao_agenteM2 = ETiposServicosAgentes.AleatorioMelhorS;           break; 
             case 26: operacao_agenteM2 = ETiposServicosAgentes.MelhorSol_Melhor_DoisIndv;  break; 
             case 27: operacao_agenteM2 = ETiposServicosAgentes.MelhorSol_Maior_MenorIndv;  break; 
             default:
                 System.out.println("Nenhuma das opções são válidas na operação agente 2");
                 System.exit(1);
        }
        switch(Integer.parseInt(args[5])){
        
            case 0: politicaSelecaoSolucao = null;                                 break;
            case 1: politicaSelecaoSolucao = PoliticaSelecao.Melhor;               break;
            case 2: politicaSelecaoSolucao = PoliticaSelecao.ProbabilidadeMelhor;  break;
            case 3: politicaSelecaoSolucao = PoliticaSelecao.Aleatorio;            break;
            case 4: politicaSelecaoSolucao = PoliticaSelecao.Pior;                 break;
            case 5: politicaSelecaoSolucao = PoliticaSelecao.ProbabilidadeMenor;   break;
        }
        porta1 = Integer.parseInt(args[6]);
        porta2 = Integer.parseInt(args[7]);
        
        switch(Integer.parseInt(args[8])){
        
            case 0: rotaciona = false;
            case 1: rotaciona = true;
        }
        switch(Integer.parseInt(args[9])){
        
            case 0: firstFit = false;
            case 1: firstFit = true;
        }
       /*##########################  TERMINOU A CONFIGURAÇÃO DOS ARGUMENTOS DO ALGORITMO COMBINAÇÂO ##################################*/
       ObjetoComunicacaoTree  objetoComTree = new ObjetoComunicacaoTree();
       ObjetoComunicacaoMelhorado obj       = new ObjetoComunicacaoMelhorado();
       obj.setTipoServicoAgente(operacao_agente);
       obj.setTipoServicoServidor(operacao_servidor1);
       
       AgentesCombinacao agente = new AgentesCombinacao(name, "localhost", porta1, porta2, operacao_agente, operacao_agenteM2,
                                                                                            operacao_servidor1, tipo_agente) ;
       agente.setName(name);
       agente.setPoliticaSelecaoSolucao(politicaSelecaoSolucao);
       
       agente.setObjetoComunicacaoMelhorado(obj);
       agente.setObjetoComunicacaoTree(objetoComTree); 
       
       agente.setTipoHeuristica(tipo_Heuristica);
       agente.setTipoServicoServidor(operacao_servidor1);
       agente.setTipoServicoServidor2(operacao_servidor2);
       agente.setServicoAgente(tipo_agente);
       agente.setTipoServico(operacao_agente);
       agente.setTipoServicoAg2(operacao_agenteM2);
       
       agente.setIsFirst(firstFit);
       agente.setRotaciona(rotaciona);
       
       //Isso aqui talvez será colocado dentro do método Run()  !!!!!  DEUS EU ACREDITO EM MILAGRES :D  :D  :D  :D  :D  :D  :D
       agente.setNumiteracoes(1);  
       System.out.println("Obrigado deus por tudo !!!");
       
       System.out.println("Vou iniciar o run ...");
       agente.start();
                     
    }
    
    public ArrayList<Integer> retornaSequenciaEncaixe(SolucaoHeuristica solucao){
    
        //OBS: A solucao deve está ordenada
        ArrayList<Integer> sequenciaEnc = new ArrayList<Integer>();
        Iterator<Bin> iterBin = solucao.getObjetos().iterator();
        
        while(iterBin.hasNext()){
        
            Bin b = iterBin.next();
            sequenciaEnc.addAll(b.getListaItensIndiv());
        }
    
        return sequenciaEnc;
    }
}