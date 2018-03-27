package HeuristicaConstrutivaInicial;

import java.util.List;
import java.net.Socket;
import HHDInternal.Peca;
import HHDInternal.Corte;
import HHDInternal.Ponto;
import HHDInterfaces.IBin;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import ATeam.IniciaClientes;
import java.util.Collections;
import Heuristicas.Individuo;
import HHDInterfaces.IPedido;
import HHDInternal.Dimensao2D;
import java.io.BufferedReader;
import ATeam.ETipoHeuristicas;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.InputStreamReader;
import Utilidades.PoliticaSelecao;
import java.io.ObjectOutputStream;
import HHDInternal.PedacoDisponivel;
import HHDInternal.SolucaoHeuristica;
import java.io.FileNotFoundException;
import HHD_Exception.PecaInvalidaException;
import SimulaGenetico.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ServicoAgente;
import SimulaGenetico.OperacoesSolucoes_Individuos;
import Utilidades.ComparadorSolucoesHeuristicaMemoria;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HeuristicaGRASP implements Runnable{

    private Socket socketClientFinal;
    private ObjectInputStream     in;  //Entrada para leitura do socket
    private ObjectOutputStream   out; //Saída para escrita no socket        
    
    private boolean inicializado;
    private boolean executando;
    private Thread  thread;
    private String  name;
    private ServicoAgente tipo_agente;
    private ETiposServicosAgentes  operacao_agente;    //Tipo de operação realizada pelo agente
    private ETiposServicosServidor operacao_servidor; //Tipo de operação requerida da memória pelo agente 
    private ETipoHeuristicas tipoheuristica;
    private int porta_comunicacao;
    private ArrayList<SolucaoHeuristica> vectorSolucoes;
    
    ObjetoComunicacaoMelhorado objeto = null;// = new ObjetoComunicacaoMelhorado();
    double alpha;
    int  maxIter;
    private int typeGraspMelhoria;
    PoliticaSelecao polSelecaoSolucao = null;
    PoliticaSelecao polSelecaoPlacas  = null;
    private int hhdtype;
    private float aprov;

    
    //Aqui inicia o construtor
    public HeuristicaGRASP(String name, String endereco, int portacomunicacao, ETiposServicosAgentes eSAgn, ETiposServicosServidor eSServ) throws Exception {
        
        this.name = name;
        this.porta_comunicacao = portacomunicacao;
        this.operacao_servidor = eSServ;
        this.operacao_agente   = eSAgn;
      
        inicializado = false;
        executando = false;   
      
        vectorSolucoes = new ArrayList<SolucaoHeuristica>();
        objeto = new ObjetoComunicacaoMelhorado();
        
        //open(endereco, portacomunicacao);
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
        }
        catch(IOException e){
            System.out.println("Exceção IO no open do grasp");
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

          inicializado = false;

          thread = null;
    }     
    
    public void start_run() {//Inicia a thread auxiliar, se possível. O objeto cliente precisa estar inicializado e não pode está executando !

        if (executando) {

            return;
        }

        executando = true;
        thread = new Thread(this);
        thread.run();
    }
    
    public void start(){//Inicia a thread auxiliar, se possível. O objeto cliente precisa estar inicializado e não pode está executando !
    
        //if(!inicializado || executando){
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
    
        this.objeto = obj;
    }
    
    public ObjetoComunicacaoMelhorado getDeveEnviar(){
    
        return this.objeto;
    }
    
    public void send(ObjetoComunicacaoMelhorado obcom){ 
     
        try {
            //out.println(obcom.getMSGServicoAgente());
            out.writeObject(obcom);
            out.flush();
        } 
        catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erro na Escrita do Objeto enviado pelo cliente");
            System.out.println(ex);

            close();
        }
  }
  
    public ObjetoComunicacaoMelhorado receive(){
  
        ObjetoComunicacaoMelhorado objetoComunicacao = null;
       
        try{
            objetoComunicacao = (ObjetoComunicacaoMelhorado) in.readObject();
        }
        catch(IOException ex){
            System.out.println("Erro de leitura no receive do CLiente !!!");
            System.out.println(ex);
        }
        catch(ClassNotFoundException exClass){
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

    public ETiposServicosServidor getTipoServicoServidor(){

        return operacao_servidor;
    }

    public void setTipoServicoServidor(ETiposServicosServidor etipoServicoServidor){

        this.operacao_servidor = etipoServicoServidor;
    }
  
    public ETipoHeuristicas getTipoHeuristica(){
    
        return this.tipoheuristica;
    }
    
    public void setTipoHeuristica(ETipoHeuristicas etipoheuristica){
    
        this.tipoheuristica = etipoheuristica;
    }
    
    public int getHHDType() {
        
        return this.hhdtype;
    }
    
    public void setHHDType(int type) {
        
        this.hhdtype = type;
    }

    public float getAprovt() {
        return this.aprov;
    }
    
    public void setAprovt(float aproveitamentoPlaca) {
        this.aprov = aproveitamentoPlaca;
    }
    
    public ObjetoComunicacaoMelhorado getObjetoComunicacaoMelhorado() {
    
        return this.objeto;
    }
    
    public void setObjetoComunicacaoMelhorado(ObjetoComunicacaoMelhorado obj) {
    
        this.objeto.setMSGServicoAgente(obj.getMSGServicoAgente());
        this.objeto.setSolucao(obj.getSolucao());
        this.objeto.setTipoServicoAgente(obj.getTipoServicoAgente2());
        this.objeto.setTipoServicoServidor(obj.getTipoServicoServidor());
    }
    
    public ArrayList<SolucaoHeuristica> getVectorSolucoes() {
        
        return this.vectorSolucoes;
    }
    
    public void setVectorSolucoes(ArrayList<SolucaoHeuristica> vectorSolucoes) {
        
        this.vectorSolucoes = vectorSolucoes;
    }
    
    private double getAlpha() {
        
        return this.alpha;
    }
    
    private void setAlpha(double alpha) {
        
        this.alpha = alpha;
    }
    
    private int getMaxIter() {
        return maxIter;
    }  
    
    private void setMaxIter(int maxIter) {
        this.maxIter = maxIter;
    }
    
    private int getGraspMelhoriaType() {
        
        return typeGraspMelhoria;
    }
    
    private void setGraspMelhoriaType(int type){
    
        this.typeGraspMelhoria = type;
    }  
    
    private PoliticaSelecao getPoliticaSelecaoSolucao(){
        
        return this.polSelecaoSolucao;
    }
    
    private void setPoliticaSelecaoSolucao(PoliticaSelecao polSolucao){
        
        this.polSelecaoSolucao = polSolucao;
    }
    
    private PoliticaSelecao getPoliticaSelecaoPlacas(){
        
        return polSelecaoPlacas;
    }
    
    private void setPoliticaSelecaoPlacas(PoliticaSelecao polPlacas){
       
        this.polSelecaoPlacas = polPlacas;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////                                                                                                    ////
    ////  Aqui implementa outros métodos de necessários a simulação do cliente agente !!!                   ////
    ////                                                                                                    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /*public void conectaMemoria(int port, ByteBuffer Buffer){
    
        int breake = -1;
        Selector selector = null;
        
        try{
            selector = Selector.open();

            //Criar um canal socket e registrá-lo ao selector.

            InetSocketAddress addr = new InetSocketAddress(port);
            SocketChannel sc = SocketChannel.open();

            sc.configureBlocking(false);
            System.out.println("Iniciando Conexão !!!");
            sc.connect(addr);

            sc.register(selector, sc.validOps());
        }
        catch(IOException e){
            System.out.println(e);
        }
        
        //Esperar por eventos
        while(true){
            
            try {
                selector.select();
            }
            catch(IOException e){}
            
            //Armazenar a lista de possíveis operações pendentes
            Iterator it = selector.selectedKeys().iterator();  

            //Processar cada selectedKey

            while(it.hasNext()){

                //Seleciona uma chave/key
                SelectionKey selKey = (SelectionKey) it.next();

                //Remove a chave/key
                it.remove();

                try{
                    breake = processaSelectionKey(selKey, Buffer);
                    
                    if(breake == 1){
                        
                        System.out.println("Break == 1 !!!");
                        break;
                    }
                }
                catch(IOException e){
                     selKey.cancel();
                     System.out.println(e);
                }
            }
            
            if(breake == 1){
            
                System.out.println("Saindo .... !");
                break;
            }
        } 
    }
    public static int processaSelectionKey(SelectionKey selKey, ByteBuffer Buffer) throws IOException{
        
        int op = 0;
        ByteBuffer buffer = ByteBuffer.allocate(4040);
        SocketChannel socketChannel = null;
        String texto = "";
        
        if(selKey.isValid() && selKey.isConnectable()){
        
            System.out.println("Estou pronto na conexão e vou enviar as Soluções Geradas!!!");
            //Armazena o canal com a requisição
            socketChannel =  (SocketChannel) selKey.channel();
            
            boolean sucesso =  socketChannel.finishConnect();
            
            if(!sucesso){
            
                //Um erro ocorreu poque o processo não foi completado !
                //Retira do registro a chave/key
                selKey.cancel();
            }
            
            selKey.interestOps(SelectionKey.OP_READ);
            
        }
        if(selKey.isValid() && selKey.isReadable()){
            
            System.out.println("Estou pronto para leitura !!!");
            socketChannel =  (SocketChannel) selKey.channel();
            
            System.out.println("Recebendo comunicação do servidor !");
            
            //Limpar o buffer
           // buffer.clear();
            
            int num_reader = socketChannel.read(buffer);
            
            System.out.println("Num_reader --> "+num_reader);
            
            if(num_reader == -1){
            
                //Não há mais bytes a ser lido
                socketChannel.close();
            }
            else{
                buffer.flip();
            }
            
            //Aqui desserializa a mensagem
            ObjetoComunicacaoMelhorado obmelhor = deserializaMensagemMelhorado(buffer);

            System.out.println("Comunicação recebida ... --> "+obmelhor.getMSGServicoAgente());
            
            op = 0;
            
            selKey.interestOps(SelectionKey.OP_WRITE);
            
        }
        
        if (selKey.isValid() && selKey.isWritable()) {
            
            String text = "Recebi e está tudo OK !!!";
                        
            socketChannel =  (SocketChannel) selKey.channel();
            System.out.println("Estou pronto para escrita !!!");
            
            socketChannel.write(Buffer);//serializaMensagemMelhorado(null));
            
            op = 1;
        }
        
        else{
            System.out.println("Não escreveu nada !!!");
            
        }

             
        return op;   
    }
    */
    
    public static LinkedList<SolucaoHeuristica> Grasp2d(ArrayList<Item> L,ArrayList<Item> l,float w, float h,double alpha,int maxit,
                                                      List<IPedido> listaPedidosNaoAtendidos) throws PecaInvalidaException, IOException{
	
        FuncoesGrasp.OrdenaList(L);
        
        LinkedList<SolucaoHeuristica> ListaSolucao = new LinkedList<SolucaoHeuristica>();
        
        LinkedList<Corte> list_cortes = new LinkedList<Corte>();
        LinkedList<Peca>  list_pecas  = new LinkedList<Peca>() ;
        LinkedList<PedacoDisponivel> list_sobras = new LinkedList<PedacoDisponivel>();
        
        LinkedList<Corte> cortes_auxFH = new LinkedList();
        LinkedList<Corte> cortes_auxFV = new LinkedList();
        
        LinkedList<Peca> pecas_auxFH = new LinkedList();
        LinkedList<Peca> pecas_auxFV = new LinkedList();
        
        LinkedList<PedacoDisponivel> sobras_auxFH = new LinkedList();
        LinkedList<PedacoDisponivel> sobras_auxFV = new LinkedList();
        
        LinkedList<Heuristicas.Solucao> VetorSolucao = new LinkedList<Heuristicas.Solucao>();
        
        
        List<Item> c   = new ArrayList<Item>();
        List<Item> lc  = new ArrayList<Item>();
        List<Item> lrc = new ArrayList<Item>();
        List<Bin> Solucao = new ArrayList<Bin>();
        List<Bin> SolucaoFinal = new ArrayList<Bin>();
        
        List<Item> fhi = new ArrayList<Item>();
        List<Item> fvi = new ArrayList<Item>();
        List<Item> bh  = new ArrayList<Item>();
        List<Item> bv  = new ArrayList<Item>();
        
        StringResultado sh = new StringResultado();
        StringResultado sv = new StringResultado();
        StringResultado S  = new StringResultado();
        StringResultado Sm = new StringResultado();
        
        List<StringResultado> SolucaoC = new ArrayList<StringResultado>();
        List<String> SolucaoCorrente   = new ArrayList<String>();
        List<String> MelhorSolucao     = new ArrayList<String>();
        List<Double> FAVCorrente       = new ArrayList<Double>();
        List<Double> FAVMelhor         = new ArrayList<Double>();
        
        Double FAV = 0.0;
        Double FAVM = 0.0;
        
        Double FAV2 = 0.0;
	Double MenorAp =0.0;
        
        Item p = new Item();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //Verificar a utilidade e importância no código
        
        c = (List<Item>) l.clone();

        float rfh;
        float rfw;
        int x  = 0;
        int j  = 0;
        int j1 = 10000;
        int cont  = 0;
        boolean r = false;

        // for (int it = 0; it < 50; it++){
        
        long ini = System.currentTimeMillis();
        
        Item item_temp;
        List<Item> listem ;
        Individuo individuo_temp = new Individuo();
        
        double somatorioSobras = 0.0;
        
        //Condição de Parada é o nº de soluções
        while (x < maxit){
        
            SolucaoHeuristica solucao_atual = new SolucaoHeuristica();
            solucao_atual.setTipoHeuristic(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO);
            somatorioSobras = 0.0;  //zera o somatório da solução atual !
            
            int corte = 0;
            
//            System.out.println("\n"+(x+1)+"ª iteração\n");
            j = 0;
            Solucao.clear();
            
            list_pecas.clear();
            list_cortes.clear();                
            list_sobras.clear();
            
            while(!c.isEmpty()){// && (!listaPedidosNaoAtendidos.isEmpty())) {//Enquanto existir item no conjunto C de pedidos 
        
                Bin placa = new Bin(w, h);
                
                PedacoDisponivel pedaco_disponivel = new PedacoDisponivel((new Ponto(0, 0)), new Dimensao2D(w, h), (x+1));
                
                //Teste adicionando -- As listas auxiliares para faixas horizontais e verticais
                //sobras_auxFH.add(pedaco_disponivel);
                //sobras_auxFV.add(pedaco_disponivel);
                
                //Teste de controle de sobras temporário
                list_sobras.add(pedaco_disponivel);
                //list_sobras.add(pedaco_disponivel);
                
                List<Faixas> faixaTemp = new ArrayList<Faixas>();
                rfh = h;
                rfw = w;
                
                Faixas fh = new Faixas();
                Faixas fv = new Faixas();
                
                //Criando uma estrutura de Faixa auxiliar para armazenar informações extras
                Faixas fh_aux = new Faixas();
                Faixas fv_aux = new Faixas();

                Ponto pIfE = new Ponto(0, 0);
                Ponto pSD = new Ponto(rfw, rfh);
                    
                lc.addAll(c); //Adiciona a lista de candidatos todos os elementos de C

                while (!lc.isEmpty()){ //Enquanto existir item na lista de candidatos faça
                    
                    //**System.out.println("\n\nA nova faixa vai ser criada a partir dos seguintes pontos: ");
                    //System.out.println("InfE ("+pIfE.getX()+","+pIfE.getY()+")\tSupDir ("+pSD.getX()+","+pSD.getY()+")");
                    
                    //PedacoDisponivel pedaco_disponivel = new PedacoDisponivel(pIfE, pSD, (x+1));
                    //sobras_auxFH.add(pedaco_disponivel);
                    //sobras_auxFV.add(pedaco_disponivel);
                    
                    cont++;
                    
                    //**FuncoesGrasp.ImprimeItens("Lista LC ", lc);
                    
                    lrc = FuncoesGrasp.CriaLrc(lc, alpha); //Cria uma lista restrita de candidatos
                    
                    //**FuncoesGrasp.ImprimeItens("\n\nLista LRC ", lrc);
                    
                    p = lrc.get(FuncoesGrasp.Aleatorio(lrc.size())); //Seleciona um pedido aleatório na LRC
                    
                    //**System.out.println("\nItem escolhido: "+"Id - "+p.getId()+" W - "+p.getW()+" H - "+p.getH()+" D - "+p.getD()+" O - "+p.getO());
                    
                    if (c.size() > 0 && c.get(c.size()-1).getV() > rfw*rfh){
                       
                        //**System.out.println("\nItem não pode ser alocado será removido da Lista de Candidatos !");
                        //Seria interessante remover o item ou comentar esse código aki !
                        lc.remove(p);
                        //break;
                        continue;
                    }
                    
                    if (!FuncoesGrasp.PodeAlocar(p, rfw, rfh)){ //Se o não Item pode ser alocado em rf
                        
                        //**System.out.println("\nItem não pode ser alocado e foi removido da Lista de Candidatos !");
                        lc.remove(p);
                        continue; // Volta ao Inicio do Laço
                    }
                    
                    if(!FuncoesGrasp.RotacaoItem(p, rfw, rfh)){ // Verifica se o Item pode ou deve ser rotacionado em 90º
                     
                        //**System.out.println("Não pode rotacionar o item !");
                        
                        fh = FuncoesGrasp.CriaFh(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas horizontal
                        fv = FuncoesGrasp.CriaFv(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas vertical
                        
                        fh_aux = FuncoesGrasp.CriaFh(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas horizontal
                        fv_aux = FuncoesGrasp.CriaFv(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas vertical
                        
                        r = false;
                                                
                    }else{
                        
                        //**System.out.println("Pode rotacionar o item !");
                        fh = FuncoesGrasp.CriaFh(p, rfw, rfh, 1, pIfE, pSD); //Cria a Faixas horizontal
                        fv = FuncoesGrasp.CriaFv(p, rfw, rfh, 1, pIfE, pSD);// Cria a Faixas vertical
                        
                        fh_aux = FuncoesGrasp.CriaFh(p, rfw, rfh, 1, pIfE, pSD); //Cria a Faixas horizontal
                        fv_aux = FuncoesGrasp.CriaFv(p, rfw, rfh, 1, pIfE, pSD);// Cria a Faixas vertical
                        
                        r = true;
                    }
                        
                    //**System.out.println("\nFh --> InfEsq("+fh.getPontoInferiorEsquerdo().getX()+","+fh.getPontoInferiorEsquerdo().getY()+")"
                    //**                  + " SupDireito ("+fh.getPontoSuperiorDireito().getX()+","+fh.getPontoSuperiorDireito().getY() +")");
                    //**System.out.println("Fv --> InfEsq("+fv.getPontoInferiorEsquerdo().getX()+","+fv.getPontoInferiorEsquerdo().getY()+")"
                    //**                  + " SupDireito ("+fv.getPontoSuperiorDireito().getX()+","+fv.getPontoSuperiorDireito().getY() +")");
                    
                    //**System.out.println("\nFh (Rw,Rh) --> InfEsq("+fh.getPontInfERw().getX()+","+fh.getPontInfERw().getY()+")"
                    //**                    + " SupDireito ("+fh.getPontSupDRh().getX()+","+fh.getPontSupDRh().getY() +")");
                    
                    //**System.out.println("Fv (Rw, Rh) --> InfEsq("+fv.getPontInfERw().getX()+","+fv.getPontInfERw().getY()+")"
                    //**                    + " SupDireito ("+fv.getPontSupDRh().getX()+","+fv.getPontSupDRh().getY() +")");
                    
                    //**System.out.println("\n\nFh (W x H) ("+fh.getW()+" x "+fh.getH()+") (RFW x RFH) ("+fh.getRw()+" x "+fh.getRh()+") Or. "+fh.getO());
                    //**System.out.println("Fv (W x H) ("+fv.getW()+" x "+fv.getH()+") (RFW x RFH) ("+fv.getRw()+" x "+fv.getRh()+") Or. "+fv.getO());
                    
                    Ponto pCorteH, pCorteV;
                    Ponto pCorteH_Tree = null, pCorteV_Tree = null;
                    float alt,larg;
                    if(r == true){
                        //System.out.println("O item vai ser rotacionado...");
                        //Aqui deve ser criado o corte relacionado as faixas
                        pCorteH = new Ponto(fh.getPontoInferiorEsquerdo().getX(),fh.getPontoInferiorEsquerdo().getY() + p.getW());
                        pCorteV = new Ponto(fv.getPontoInferiorEsquerdo().getX() + p.getH(),fv.getPontoInferiorEsquerdo().getY());
                        
                        //Será útil na conversão para árvore
                        pCorteH_Tree = new Ponto(fh.getPontoInferiorEsquerdo().getX(),fh.getPontoInferiorEsquerdo().getY());
                        pCorteV_Tree= new Ponto(fv.getPontoInferiorEsquerdo().getX(),fv.getPontoInferiorEsquerdo().getY());
                        
                        larg = p.getH();
                        alt = p.getW();
                    }
                    else{
                        //System.out.println("O item não vai ser rotacionado...");
                        //Aqui deve ser criado o corte relacionado as faixas
                        pCorteH = new Ponto(fh.getPontoInferiorEsquerdo().getX(),fh.getPontoInferiorEsquerdo().getY() + p.getH());
                        pCorteV = new Ponto(fv.getPontoInferiorEsquerdo().getX() + p.getW(),fv.getPontoInferiorEsquerdo().getY());
                        
                        //Será útil para conversão para árvore
                        pCorteH_Tree = new Ponto(fh.getPontoInferiorEsquerdo().getX(),fh.getPontoInferiorEsquerdo().getY());
                        pCorteV_Tree = new Ponto(fv.getPontoInferiorEsquerdo().getX(),fv.getPontoInferiorEsquerdo().getY());
                        
                        larg = p.getW();
                        alt  = p.getH();
                    }
                    
                    //**System.out.println("\n///////////////Criado um Ponto de Corte das Faixas \\\\\\\\\\\\\\\\\\");
                    //**System.out.println("Faixa Horizontal P("+pCorteH.getX()+","+pCorteH.getY()+") ||||  "
                    //**        + "Faixa Vertical -- P("+pCorteV.getX()+","+pCorteV.getY()+")");
                    
                    corte = corte + 1;
                    
                    /*Corte corteFax_H = new Corte(alt, pCorteH, false, 
                                        fh.getPontoSuperiorDireito().getX() - fh.getPontoInferiorEsquerdo().getX(), list_cortes.size()+1);//*/
                    Corte corteFax_H = new Corte(alt, pCorteH_Tree, false, 
                                        fh.getPontoSuperiorDireito().getX() - fh.getPontoInferiorEsquerdo().getX(), list_cortes.size()+1);//corte);
                    corteFax_H.setPontoChapaCortadaGrasp(pCorteH_Tree);
                    corteFax_H.setPosicaoCorteGrasp(alt);
                    
                    /*Corte corteFax_V = new Corte(larg, pCorteV, true, 
                                        fv.getPontoSuperiorDireito().getY() - fv.getPontoInferiorEsquerdo().getY(), list_cortes.size()+1);*/
                    Corte corteFax_V = new Corte(larg, pCorteV_Tree, true, 
                                        fv.getPontoSuperiorDireito().getY() - fv.getPontoInferiorEsquerdo().getY(), list_cortes.size()+1);//corte);
                    corteFax_V.setPontoChapaCortadaGrasp(pCorteV_Tree);
                    corteFax_V.setPosicaoCorteGrasp(larg);
                    
                    corte = corte + 1;

                    //**System.out.println("\n Alocando um item");
                    
                    FuncoesGrasp.AlocarItemFh(p, fh, fhi,sh,"H",fh_aux, cortes_auxFH,pecas_auxFH,sobras_auxFH, pSD,list_sobras); //Aloca o Item p na Faixas horizontal
                    FuncoesGrasp.AlocarItemFv(p, fv, fvi,sv,"V",fv_aux, cortes_auxFV,pecas_auxFV,sobras_auxFV, pSD,list_sobras); //Aloca o Item p na Faixas vertical
                    
                    //Colocar informação de itens alocados aqui para controle @@@@@@@
                    
                    //Aqui comeca a conversao
                    //**System.out.println("\nFaixa Atualizada\n");
                    
                    //**System.out.println("Fh (W x H) ("+fh.getW()+" x "+fh.getH()+") (RFW x RFH) ("+fh.getRw()+" x "+fh.getRh()+") Or. "+fh.getO());
                    //**System.out.println("Fv (W x H) ("+fv.getW()+" x "+fv.getH()+") (RFW x RFH) ("+fv.getRw()+" x "+fv.getRh()+") Or. "+fv.getO());

                    bh = FuncoesGrasp.CriaBh(lc, bh,fh.getRw(), fh.getRh(), p);
                    bv = FuncoesGrasp.CriaBv(lc, bv,fv.getRw(), fv.getRh(), p);

                    //**FuncoesGrasp.ImprimeItens("\nLista de BH ", bh);
                    //**FuncoesGrasp.ImprimeItens("\nLista de BV ", bv);

                    while (!bh.isEmpty()){
                    
                        //funcoes.ImprimeItens("***BH****",bh);
                        bh = FuncoesGrasp.MelhoreFh(fh, bh, bh.get(0), fhi,sh,fh_aux, cortes_auxFH, pecas_auxFH, sobras_auxFH,pSD, list_sobras);
                    }

                    while (!bv.isEmpty()){
                    
                        //funcoes.ImprimeItens("***BV***",bv);
                        bv = FuncoesGrasp.MelhoreFv(fv, bv, bv.get(0), fvi,sv, fv_aux, cortes_auxFV, pecas_auxFV, sobras_auxFV,pSD,list_sobras);
                    } 

                    //**System.out.println("\nAtualização das Faixas\nApós criação de BV e BH \n");
                    
                    //**System.out.println("Fh (W x H) ("+fh.getW()+" x "+fh.getH()+") (RFW x RFH) ("+fh.getRw()+" x "+fh.getRh()+") Or. "+fh.getO());
                    //**System.out.println("Fv (W x H) ("+fv.getW()+" x "+fv.getH()+") (RFW x RFH) ("+fv.getRw()+" x "+fv.getRh()+") Or. "+fv.getO());

                    double Pih = fh.getPi()/(fh.getH()*fh.getW());
                    double Piv = fv.getPi()/(fv.getH()*fv.getW());

                    //**System.out.println("Pih -> "+Pih);
                    //**System.out.println("Piv -> "+Piv);
                    
                    List<Item> itemTemp = new ArrayList<Item>();
                    
                    if (Pih < Piv){
                        
                        fh.setO(0);
                        
                        itemTemp.addAll(fhi);
                        fh.setItem(itemTemp);
                        
                        //Aqui seleciona a "Lista de Itens" da "Faixa Horizontal"
                        listem = new LinkedList<Item>(fhi);
                        //listem = fhi;
                        Iterator<Item> it_item = listem.iterator();
                        item_temp = new Item();
                        individuo_temp = new Individuo();
                        individuo_temp = placa.getIndividuo();
                        
                        while(it_item.hasNext()){
                            item_temp = it_item.next();
                            individuo_temp.adicionaItemLista(item_temp.getId(), item_temp.getV());
                        }
                        
                        individuo_temp.setFitness();
                        individuo_temp.setCapacidadeRespeitada(true);
                        //solucao_atual.atualizaIndividuoSolucao(individuo_temp);
                        //solucao_atual.getIndividuo().setListaItens2(individuo_temp.getListaItens());
                        /*Aqui deve-se atribuir as informações da faixa auxilia para faixa */
                        //fh.setPontInfERw(fh_aux.getPontInfERw()); //Acho que não é mais necessário
                        //fh.setPontSupDRh(fh_aux.getPontSupDRh()); //Acho que não é mais necessário
                        
                        faixaTemp.add(fh);
                        
                        list_cortes.add(corteFax_H);
                        
                        pIfE = new Ponto(fh.getPontoInferiorEsquerdo().getX(), fh.getPontoSuperiorDireito().getY());
                        pSD  = new Ponto(fh.getPontoSuperiorDireito().getX(), h);
                        
                        /*Mudança 15 de abril 21:00*/
                        if((pSD.getX() - pIfE.getX() > 0) && (pSD.getY() - pIfE.getY() > 0)){
                        
                            PedacoDisponivel pedaco_disponivel_proxima_faixa = new PedacoDisponivel(pIfE,pSD,list_sobras.size()+1);
                            sobras_auxFH.add(pedaco_disponivel_proxima_faixa);
                        }
                            
                        
                        /*Até aqui !!! */
                        
                        /*if(!list_sobras.isEmpty()){
                            list_sobras.removeFirst();
                            list_sobras.add(new PedacoDisponivel(pIfE, pSD, list_sobras.size()+1));
                        }*/// Creio que não será mais necessário pois será passado para os auxiliares !!!
                        
                        //É importante limpar e atribuir novamente a lista de sobras
                        list_sobras.clear(); list_sobras.removeAll(list_sobras);
                        
                        //Aqui será adicionado o resultado das listas auxiliares
                        list_cortes.addAll(cortes_auxFH);
                        list_pecas.addAll(pecas_auxFH);
                        list_sobras.addAll(sobras_auxFH);
        
                        //Imprimir informações  sobre cada lista e o que será passado a Lista Principal
                        /*System.out.println("\n################ LISTAS COM INFORMAÇÕES ####################");
                        Peca pc;      Corte ct;     PedacoDisponivel sb;
                                                
                        System.out.println("Lista de Peças");
                        
                        Iterator<Peca> it = list_pecas.iterator();
                        
                        while(it.hasNext()){
                        
                            pc = it.next();                        
                                                        
                            System.out.println("Peça Id - " +pc.getPedidoAtendido().id()+"\tPontInfE( "+pc.getPontoInfEsq().getX()
                                                            +","+pc.getPontoInfEsq().getY()+")" + "\tPontSupDir("+pc.getPontoSupDir().getX()
                                                            +","+pc.getPontoSupDir().getY()+")");
                        }
                        
                        System.out.println("\nLista de Corte");
                        
                        Iterator<Corte> itc = list_cortes.iterator();
                        
                        while(itc.hasNext()){
                        
                            ct = itc.next();
                            
                            System.out.println("Corte Id - "+ ct.getId()+"\tPonto de corte - ("+ct.getPontoChapaCortada().getX()+" , "
                                                                        +ct.getPontoChapaCortada().getY()+") "
                                    + " Tamanho - "+ct.getTamanho());
                        }                        
                        
                        System.out.println("\nLista de Sobra");
                        
                        Iterator<PedacoDisponivel> itpd = list_sobras.iterator();
                        
                        while(itpd.hasNext()){
                        
                            sb = itpd.next();                        
                                                        
                            System.out.println("Sobra Id - "+sb.getId()+"\tPontInfE( "+sb.getPontoInferiorEsquerdo().getX()
                                                            +","+sb.getPontoInferiorEsquerdo().getY()+")" 
                                                            +"\tPontSupDir("+sb.getPontoSuperiorDireito().getX()
                                                            +","+sb.getPontoSuperiorDireito().getY()+")");
                        }
                        
                        */
                        
//                        System.out.println("\n\n");
                        
                        //Limpa as coleções auxiliares para a próxima iteração
                        cortes_auxFH.clear();
                        pecas_auxFH.clear();
                        sobras_auxFH.clear();
                        
                        cortes_auxFV.clear();
                        pecas_auxFV.clear();
                        sobras_auxFV.clear();
                        
                        cortes_auxFH.removeAll(cortes_auxFH);
                        pecas_auxFH.removeAll(pecas_auxFH);
                        sobras_auxFH.removeAll(sobras_auxFH);
                        
                        cortes_auxFV.removeAll(cortes_auxFV);
                        pecas_auxFV.removeAll(pecas_auxFV);
                        sobras_auxFV.removeAll(sobras_auxFV);
                                                
                        S.setS(S.getS()+sh.getS());
                        
                        if (r){
                            rfh = rfh - p.getW();
                        }
                        else{
                            rfh = rfh - p.getH();
                        }
                        
                        //**System.out.println("Rfh -> "+ rfh+"\n");
                        
                        c = FuncoesGrasp.AtualizaC2(fhi, c, placa, lc, rfw, rfh,listaPedidosNaoAtendidos);

                        //c = FuncoesGrasp.AtualizaC(fhi, c, placa);
                        // System.out.println("Orientação Faixas: "+fh.getO());
                    }else{
                    
                        
                        fv.setO(1);
                        itemTemp.addAll(fvi);
                        
                        S.setS(S.getS()+sv.getS());
                        fv.setItem(itemTemp);
                        
                        //Aqui seleciona a "Lista de Itens" da "Faixa Vertical"
                        listem = new LinkedList<Item>(fvi);
                        //listem = fvi;
                        Iterator<Item> it_item = listem.iterator();
                        item_temp = new Item();
                        
                        individuo_temp =  new Individuo();
                        individuo_temp =  placa.getIndividuo();
                        
                        while(it_item.hasNext()){
                            item_temp = it_item.next();
                            individuo_temp.adicionaItemLista(item_temp.getId(), item_temp.getV());
                        }
                        
                        individuo_temp.setFitness();
                        individuo_temp.setCapacidadeRespeitada(true);
                        //solucao_atual.atualizaIndividuoSolucao(individuo_temp);
                        /*Aqui deve-se atribuir as informações da faixa auxilia para faixa */
                        //fv.setPontInfERw(fv_aux.getPontInfERw()); //Acho que não é mais necessário
                        //fv.setPontSupDRh(fv_aux.getPontSupDRh()); //Acho que não é mais necessário
                        
                        faixaTemp.add(fv);
                        list_cortes.add(corteFax_V);
                        
                        pIfE = new Ponto(fv.getPontoSuperiorDireito().getX(), fv.getPontoInferiorEsquerdo().getY());
                        pSD  = new Ponto(w, fv.getPontoSuperiorDireito().getY());
                        
                        /*Mudança dia 15 de abril */
                        if((pSD.getX() - pIfE.getX() > 0) && (pSD.getY() - pIfE.getY() > 0)){
                        
                            PedacoDisponivel pedaco_disponivel_proxima_faixa = new PedacoDisponivel(pIfE,pSD,list_sobras.size()+1);
                            sobras_auxFV.add(pedaco_disponivel_proxima_faixa);
                        }/*if(!list_sobras.isEmpty()){

                            list_sobras.removeFirst();
                            list_sobras.add(new PedacoDisponivel(pIfE, pSD, list_sobras.size()+1));
                          }*/// Creio que não será mais necessário pois será passado para os auxiliares !!!
                        //É importante limpar e atribuir novamente a lista de sobras
                        
                        list_sobras.clear(); list_sobras.removeAll(list_sobras);
                        
                        //Aqui será adicionado o resultado das listas auxiliares
                        list_cortes.addAll(cortes_auxFV);
                        list_pecas.addAll(pecas_auxFV);
                        list_sobras.addAll(sobras_auxFV);
                        
                        //Imprimir informações  sobre cada lista e o que será passado a Lista Principal
                        /*System.out.println("\n################ LISTAS COM INFORMAÇÕES ####################");
                        Peca pc;      Corte ct;     PedacoDisponivel sb;
                                                
                        System.out.println("Lista de Peças");
                        
                        Iterator<Peca> it = list_pecas.iterator();
                        
                        while(it.hasNext()){
                        
                            pc = it.next();                        
                                                        
                            System.out.println("Peça Id - " +pc.getPedidoAtendido().id()+"\tPontInfE( "+pc.getPontoInfEsq().getX()
                                                            +","+pc.getPontoInfEsq().getY()+")" + "\tPontSupDir("+pc.getPontoSupDir().getX()
                                                            +","+pc.getPontoSupDir().getY()+")");
                        }
                        
                        System.out.println("\nLista de Corte");
                        
                        Iterator<Corte> itc = list_cortes.iterator();
                        
                        while(itc.hasNext()){
                        
                            ct = itc.next();
                            
                            System.out.println("Corte Id - "+ ct.getId()+"\tPonto de corte - ("+ct.getPontoChapaCortada().getX()+" , "
                                                                        +ct.getPontoChapaCortada().getY()+") "
                                    + " Tamanho - "+ct.getTamanho());
                        }                        
                        
                        System.out.println("\nLista de Sobra");
                        
                        Iterator<PedacoDisponivel> itpd = list_sobras.iterator();
                        
                        while(itpd.hasNext()){
                        
                            sb = itpd.next();                        
                                                        
                            System.out.println("Sobra Id - "+sb.getId()+"\tPontInfE( "+sb.getPontoInferiorEsquerdo().getX()
                                                            +","+sb.getPontoInferiorEsquerdo().getY()+")" 
                                                            +"\tPontSupDir("+sb.getPontoSuperiorDireito().getX()
                                                            +","+sb.getPontoSuperiorDireito().getY()+")");
                        }
                        
                        */
                        System.out.println("\n\n");
                        
                        
                        //Limpa as coleções auxiliares para a próxima iteração
                        cortes_auxFV.clear();
                        pecas_auxFV.clear();
                        sobras_auxFV.clear();
                        
                        cortes_auxFH.clear();
                        pecas_auxFH.clear();
                        sobras_auxFH.clear();
                        
                        cortes_auxFV.removeAll(cortes_auxFV);
                        pecas_auxFV.removeAll(pecas_auxFV);
                        sobras_auxFV.removeAll(sobras_auxFV);
                        
                        cortes_auxFH.removeAll(cortes_auxFH);
                        pecas_auxFH.removeAll(pecas_auxFH);
                        sobras_auxFH.removeAll(sobras_auxFH);
                        
                        if(r){
                            rfw = rfw - p.getH();
                        }
                        else{
                            rfw = rfw - p.getW();
                        }
                        
                        //**System.out.println("Rfw -> "+ rfw+"\n");
                        
                        c = FuncoesGrasp.AtualizaC2(fvi, c, placa,lc, rfw,rfh, listaPedidosNaoAtendidos);                        
                    }

                    fhi.clear();
                    fvi.clear();
                    sh.setS("");
                    sv.setS("");
                    lc.clear();
                    lc.addAll(c);

                    /*Aqui deve-se atribuir e setar os pontos InfEsquerdo  e SupDireito das faixas*/
                    
                    /*fh.setPontoInferiorEsquerdo(null);
                    fh.setPontoSuperiorDireito(null);
                    fh.setPontInfERw(null);
                    fh.setPontSupDRh(null);
                    
                    fv.setPontoInferiorEsquerdo(null);
                    fv.setPontoSuperiorDireito(null);
                    fv.setPontInfERw(null);
                    fv.setPontSupDRh(null);*/
                }
                
                //Somatório sobra
                
                
                
                //placa.setSobra(somatorioSobras);
                placa.setSobra((double) (w*h) - placa.getFav()); 
                somatorioSobras = somatorioSobras + placa.getSobra();
                //System.out.println("SomatorioSobras-> "+ somatorioSobras);
                //**System.out.println("SomatorioSobras-> "+ placa.getSobra());
                
                placa.setF(faixaTemp); //Adiciona as listas de placas !!!
                
                //Aqui será apresentado as informações finais
                //**System.out.println("\n################ LISTAS COM INFORMAÇÕES ####################");
                        
                //System.out.println("Lista de Peças");
                
                Peca pc; Corte ct; PedacoDisponivel sbpd;
                int cont_ct = 1, cont_sbpd = 1;
                
                Iterator<Peca>  itpeca = list_pecas.iterator();
                
                while(itpeca.hasNext()){
                    
                   pc = itpeca.next();
                   placa.adicionePeca(pc);
                   //**System.out.println("Peça Id - " +pc.getPedidoAtendido().id()+"\tPontInfE( "+pc.getPontoInfEsq().getX()
                   //**                                         +","+pc.getPontoInfEsq().getY()+")" + "\tPontSupDir("+pc.getPontoSupDir().getX()
                   //**                                         +","+pc.getPontoSupDir().getY()+")");
                }
                
                //System.out.println("Lista de Corte");
                 
                Iterator<Corte> itc = list_cortes.iterator();
                       
                while(itc.hasNext()){                
                
                   ct = itc.next();
                  
                   if(cont_ct != ct.getId()){
 
                      ct.setId(cont_ct);
                   }
                  
//                   System.out.println("Corte Id - "+ ct.getId()+"\tPonto de corte - ("+ct.getPontoChapaCortada().getX()+" , "
//                                                                        +ct.getPontoChapaCortada().getY()+") "
//                                    + " Tamanho - "+ct.getTamanho()+ "  Posicao corte - "+ct.getPosicaoCorte()+"  "
//                                    +"É vertical - "+ct.eVertical());
                   cont_ct = cont_ct + 1;
                  
                   //Acrescentar caso aconteça algum problema de referência de corte
                   
                   if(ct.eVertical()){
                       if(ct.getPontoChapaCortada().getX()+ct.getPosicaoCorte() < w){
                   
                           placa.adicioneCorte(ct);                           
                       }                       
                       
                   }else{                   
                       if(ct.getPontoChapaCortada().getY()+ct.getPosicaoCorte() < h){
                   
                           placa.adicioneCorte(ct);                           
                       } 
                   }
                   //placa.adicioneCorte(ct);                   
                }
                
                //System.out.println("Lista de Sobras");
                
                Iterator<PedacoDisponivel> itpd = list_sobras.iterator();
                       
                while(itpd.hasNext()){
                
                   sbpd = itpd.next();
                  
                   if(cont_sbpd != sbpd.getId()){
 
                      sbpd.setId(cont_sbpd);
                   }
                                   
                   cont_sbpd = cont_sbpd + 1;
                   
                   placa.adicionarSobra(sbpd);
                   
                   //**System.out.println("Sobra Id - "+sbpd.getId()+"\tPontInfE( "+sbpd.getPontoInferiorEsquerdo().getX()
                   //**                                         +","+sbpd.getPontoInferiorEsquerdo().getY()+")" 
                   //**                                         +"\tPontSupDir("+sbpd.getPontoSuperiorDireito().getX()
                   //**                                         +","+sbpd.getPontoSuperiorDireito().getY()+")");
                }
                 
                //Limpando as informações das listas para a próxima iteração !                
                
                list_pecas.clear();
                list_cortes.clear();                
                list_sobras.clear();                
                
                FAV = FAV + placa.getFav();
                S.setFav((placa.getFav()/(w*h))*100);
                S.setI(x);
                SolucaoCorrente.add(S.getS());
                FAVCorrente.add(S.getFav());

                Solucao.add(placa);
                
                //Aqui eu gravo a placa de menor aproveitamento
                Double sobra = 100-(placa.getFav()/(w*h)*100);
                placa.setAproveitamento(sobra);

                if (sobra > MenorAp){
                    MenorAp = sobra;                	                	
                }
                        
                //placa.setFav(0.0); 
                S.setS("");
                
                j++;
            }
            
            if (j < j1){ j1 = j;}

            FAV2 = ((FAV)+MenorAp)/(j*w*h);
            FAV = FAV/(j*w*h);
            //FAV2 = ((FAV*100)+MenorAp)/j;
            
            VetorSolucao.add(FuncoesGrasp.ConvertSolucao(Solucao,FuncoesGrasp.listIPedido(), Solucao.get(0).retorneDimensao()));
            
            SolucaoHeuristica SolucaoTemp = new SolucaoHeuristica(Solucao,FAV,FAV2);
            
            solucao_atual.setSomatorioSobras(somatorioSobras);
            //solucao_atual.set_Objetos(Solucao);
            solucao_atual.setObjetos(Solucao);
            solucao_atual.setFAV(FAV);
            solucao_atual.setFAV2(FAV2);
            //solucao_atual.setLinkedIPedidos(new LinkedList(l));
            solucao_atual.setLinkedIPedidos(FuncoesGrasp.CriaListaPedidos(l));
            solucao_atual.setTamanhoChapa(new Dimensao2D(w,h));
            //solucao_atual.setSomatorioSobras(somatorioSobras);
            solucao_atual.calculaLinhasDeCorteEMediaSobras();
            //System.out.println("List Itens -> "+ solucao_atual.getIndividuo().getListaItens());
	    //ListaSolucao.add(SolucaoTemp);
            ListaSolucao.add(solucao_atual);
            
            if (FAV2 > FAVM){
            
                MelhorSolucao.clear();
                MelhorSolucao.addAll(SolucaoCorrente);
                FAVMelhor.clear();
                FAVMelhor.addAll(FAVCorrente);
                FAVM = FAV2;
                SolucaoFinal.clear();
                SolucaoFinal.addAll(Solucao);
            }
            
            c = (List<Item>) l.clone();
            x++;
            FAV = 0.0;
            FAV2 = 0.0;
            MenorAp = 0.0;
	    SolucaoCorrente.clear();
            FAVCorrente.clear();

            //System.out.println(x);
    }
        
    // }
    //System.out.printf("Tempo Total: %d \n ",(System.currentTimeMillis()-ini)/1000);
    
    //System.out.println("Melhor Solucao: "+FAVM*100+"% Iteracao: "+MelhorSolucao.size()+" Contador: "+cont/maxit+" Padrao: "+Sm.getS());
    //System.out.println("Melhor Solucao: "+FAVM+"% Iteracao: "+MelhorSolucao.size()+" Contador: "+cont/maxit+" Padrao: "+Sm.getS());
    
    //System.out.println("Padrao: "+j1+" "+MelhorSolucao.size()+" "+FAVMelhor.size());

    //Alteracao comentário nos dois for a seguir, removendo as impressões
    for (int i = 0; i < MelhorSolucao.size(); i++){
        //System.out.println("Padrao de Corte "+(i+1)+":"+MelhorSolucao.get(i));
        //System.out.println("Aproveitamento: "+FAVMelhor.get(i));
    }
    
    Collections.sort(ListaSolucao);
    
    //for(int i=0;i < ListaSolucao.size();i++){
            
        //System.out.println("Solucao "+i+" FAV: "+ListaSolucao.get(i).getFAV()+" FAV2: "+ListaSolucao.get(i).getFAV2());
    //}

    return ListaSolucao;
    //return VetorSolucao;
   }
    public static SolucaoHeuristica GraspMelhoria2version(SolucaoHeuristica solucao, int maxiteracao,double alpha, float aprov)
                                                                                            throws PecaInvalidaException, IOException{
        float w, h;
        float aproveitamento = aprov;
//        System.out.println("\n\n##########  Grasp Melhoria 2 versao imprimindo a solução recebida ################");
//        solucao.imprimeSolucao(solucao, 1);
        
        w = solucao.getTamanhoChapa().retorneBase();
        h = solucao.getTamanhoChapa().retorneAltura();
        
        //System.out.println("Informações da placa: (W x H) - ("+w+" x "+h+")");
        
        IBin placa1 = null;
        IBin placa2 = null;
        Double fav1;
        Double fav2;
        
        //Solução temporária que armazena a solução completa
        SolucaoHeuristica solutionNova = new SolucaoHeuristica();
        SolucaoHeuristica solution     = new SolucaoHeuristica(solucao);
        
        ArrayList<Item> candidatos;
        LinkedList<SolucaoHeuristica> listSolucao;
        LinkedList<IPedido> listPedidos = new LinkedList<IPedido>();
        
        //Vou ordenar minha solução
        HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solucao);//Seria interessante ordenar por aproveitamento !
    
        Bin bin;
        //System.out.println("Vou desmontar as placas com aproveitamento abaixo de -> "+aprov);
        Iterator<Bin> iterator = solucao.getObjetos().iterator();
        
        while(iterator.hasNext()){
        
            bin = iterator.next();
            //Vou desmontar as placas com aproveitamento menor que o informado
            if(bin.getAproveitamento() <= aproveitamento){
            
                //System.out.println("Peças da placa escolhida: [ "+ bin.getListaPecas().size()+" ]");
                listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(bin));
                solution.set_Fav(solution.get_Fav() - (bin.getFav()));
                solution.setSomatorioSobras(solution.getSomatorioSobras() - bin.getSobra());
                solution.removePlaca(bin);
            }
        }
//        System.out.println("Solution ---> "+ solution.getQtd());
//        System.out.println("Solucao  ---> "+ solucao.getQtd());
//        System.out.println("Lista Pedidos --> " +listPedidos);
//        
        if(solution.getQtd() == solucao.getQtd()){
        
            //Não há o que melhorar pois, nenhuma placa foi removida !
            //System.out.println("Nenhum aproveitamento abaixo do especificado");
            return solucao;
        }
        else{
        
            solution.removeIndividuo();
            candidatos = new ArrayList<Item>(FuncoesGrasp.convertIpedidoItem(listPedidos));
            
            //Aqui atribui parâmetros e chama o GRASp-2D
            
            listSolucao = HeuristicaGRASP.Grasp2d(candidatos, candidatos, w, h, alpha, maxiteracao, null);
        
            Collections.sort(listSolucao, new ComparadorSolucoesHeuristicaMemoria());
            solutionNova = listSolucao.getFirst();

//            System.out.println("\n### Solucao Nova gerada !####");
//            solutionNova.imprimeSolucao(solutionNova, maxiteracao);
//
//            System.out.println("\n###  Solution antes de acoplar !");
//            solution.imprimeSolucao(solution, 1);

            solution = FuncoesGrasp.acoplaSolucao(solution, solutionNova);

            //System.out.println("\n\t########## IMPRIMINDO A SOLUÇÃO QUE FOI ACOPLADA PARA AVERIGUAÇÃO ################");
            //solution.imprimeSolucao(solution, 1);
            //System.out.println("\n\t$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

            solution.calculaLinhasDeCorteEMediaSobras();
            solution.setTipoHeuristic(ETipoHeuristicas.Grasp2d_Melhoria);

            //System.out.println("\n\n%%%%%%%%%%%%%%%%%%%####### FIM DO GRASP 2 VERSION MELHORIA ###################$$$$$$$$$$$$$$$$$$$$$$$\n\n\n");
        
        }
        
        //Vou ordenar antes de retornar a solução encontrada !!
        //HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solucao);
        HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solution);
        
        return solution;
    }
    
    /*################## AQUI SERÁ IMPLEMENTADO A LÓGICA DO GRASP MELHORIA ###################################*/
    public static SolucaoHeuristica Grasp2d_Melhoria(SolucaoHeuristica solucao, PoliticaSelecao polSelecaoPlaca, int maxiteracao, 
                                                                           double alpha) throws PecaInvalidaException, IOException{
        
        
        //System.out.println("\n\n##########  Grasp Melhoria imprimindo a solução recebida ################");
        //SolucaoHeuristica.imprimeSolucao(solucao, 1);
//        solucao.imprimeSolucao(solucao, 1);
        
        //System.out.println("\n\n\n%%%%%%%%%%%%%%%%%%%  INÍCIO GRASP MELHORIA ##################$$$$$$$$$$$$$$$$$$$$$$$\n\n\n");
        
        float w, h;
        w = solucao.getTamanhoChapa().retorneBase();
        h = solucao.getTamanhoChapa().retorneAltura();
        
        //System.out.println("Informações da placa: (W x H) - ("+w+" x "+h+")");
        
        IBin placa1 = null;
        IBin placa2 = null;
        Double fav1;
        Double fav2;
        
        //Solução temporária que armazena a solução completa
        SolucaoHeuristica solutionNova = new SolucaoHeuristica();
        SolucaoHeuristica solution     = new SolucaoHeuristica(solucao);
        
        ArrayList<Item> candidatos;
        LinkedList<SolucaoHeuristica> listSolucao;
        LinkedList<IPedido> listPedidos = new LinkedList<IPedido>();
        
        //Aqui deve ser selecionado duas Placas e realiza desmonte das duas placas
        if(polSelecaoPlaca.equals(PoliticaSelecao.MelhorMelhor)){
        
            //System.out.println("Requer MelhorMelhor");
            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            //System.out.println("Peças da placa 1 escolhida: [ "+ placa1.getListaPecas().size()+" ]");
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa1));
            
            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa1);
                        
            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            //System.out.println("Peças da placa 2 escolhida: ["+ placa2.getListaPecas().size()+" ]");
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa2));
            
            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa2);
                        
            //System.out.println("Solution ---> "+ solution.getQtd());
            //System.out.println("Solucao  ---> "+ solucao.getQtd());
            //System.out.println("Lista Pedidos --> " +listPedidos);
        }
        else if(polSelecaoPlaca.equals(PoliticaSelecao.MelhorPior)){
        
            //System.out.println("Requer MelhorPior");
            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa1));
            
            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            //solution.removeIndividuo();
            solution.removePlaca(placa1);
            
            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMenorOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa2));
            
            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa2);
        }
        else if(polSelecaoPlaca.equals(PoliticaSelecao.MelhorAleatorio)){
        
            //System.out.println("Requer MelhorAleatorio");
            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa1));
            
            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa1);
            
            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaAleatoriaOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa2));
            
            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa2);
        }
        else if(polSelecaoPlaca.equals(PoliticaSelecao.PiorAleatorio)){
        
            //System.out.println("Requer PiorAleatoria");
            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMenorOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa1));
            
            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa1);
            
            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaAleatoriaOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa2));
            
            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa2);
        }
        else{//Pior - Pior
        
            //System.out.println("Requer PiorPior");
            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMenorOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa1));
            
            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa1);
            
            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMenorOcupacaoG(solution);
            listPedidos.addAll(FuncoesGrasp.algoritmo_desmonteGRASP(placa2));
            
            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.setSomatorioSobras(solution.getSomatorioSobras() - placa1.getSobra());
            solution.removePlaca(placa2);
            
        }
        solution.removeIndividuo();
        candidatos = new ArrayList<Item>(FuncoesGrasp.convertIpedidoItem(listPedidos));
        //Aqui atribui parâmetros e chama o GRASp-2D
        listSolucao = HeuristicaGRASP.Grasp2d(candidatos, candidatos, w, h, alpha, maxiteracao, null);
        
        Collections.sort(listSolucao, new ComparadorSolucoesHeuristicaMemoria());
        solutionNova = listSolucao.getFirst();
       
        //System.out.println("\n### Solucao Nova gerada !####");
        solutionNova.imprimeSolucao(solutionNova, maxiteracao);
        
        //System.out.println("\n###  Solution antes de acoplar !");
        solution.imprimeSolucao(solution, 1);
        
        solution = FuncoesGrasp.acoplaSolucao(solution, solutionNova);
       
        //System.out.println("\n\t##$$$$IMPRIMINDO A SOLUÇÃO QUE FOI ACOPLADA PARA AVERIGUAÇÃO $$$$$$$######");
        solution.imprimeSolucao(solution, 1);
        //System.out.println("\n\t$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        
        solution.calculaLinhasDeCorteEMediaSobras();
        solution.setTipoHeuristic(ETipoHeuristicas.Grasp2d_Melhoria);
        
        //System.out.println("\n\n%%%%%%%%%%%%%%%%%%%####### FIM DO GRASP MELHORIA 1 version###################$$$$$$$$$$$$$$$$$$$$$$$\n\n\n");
        return solution;
    }
    
     @Override
    public void run(){ //Run do Grasp
         
        //Só encerrará conexão quando o servidor receber todas as soluções criadas !!!
        //Agora o método o run que será executado pela thread auxiliar.....
        //System.out.println("Iniciei a thread do GRASP ...");
        ObjetoComunicacaoMelhorado obcom = getObjetoComunicacaoMelhorado();
                
        if(obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Inicializacao_Grasp){
            
            //System.out.println("Connect 0 grasp...");
            
            try {
                open("localhost", getPortaComunicacao());
                //System.out.println("Connected !!!");
//              socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
                ObjetoComunicacaoMelhorado ob = receive();
            }
            catch (Exception e) {
                
                try {
                    System.out.println("Ocorreu alguma exceção !");
                    this.stop();
                } catch (Exception ex) {
                    Logger.getLogger(HeuristicaGRASP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            int contSolGrasp = 0;
            int qtdSolucoes = getVectorSolucoes().size();
 
            //System.out.println("Entrando o laço: " +executando);
            
            while(executando){ 
                
                try{
//                   socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !

                  //Ele escreve uma requisição para o Atendente !!!

                   if(obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Inicializacao_Grasp){

                       Iterator<SolucaoHeuristica> iterator = getVectorSolucoes().listIterator(0);
                       contSolGrasp++;
                       
                       //SolucaoHeuristica solH = ;
                       //solH.calculaLinhasDeCorteEMediaSobras();
                       
                       obcom.setSolucao(iterator.next());
//                       System.out.println("Cont: "+contSolGrasp);
//                       System.out.println("Qtd Solucoes: "+ qtdSolucoes);

                       if(contSolGrasp == qtdSolucoes){
                           //System.out.println("Ultimo envio");
                           obcom.setMSGServicoAgente("Terminei_Envio");
                       }else{
                           //System.out.println("Tem ainda pra enviar");
                           obcom.setMSGServicoAgente("Ainda_Enviando");
                       }
                       iterator.remove();

                       //System.out.println("Vou enviar um objeto");
                       send(obcom);

                   }
                   //Leitura de informações envidas pelo servidor !!!
                   //System.out.println("Vou receber um objeto");
                    obcom = receive();
                    String mensagem = obcom.getMSGServicoAgente();

                    if(mensagem == null){
                        //System.out.println("Mensagem nula !");
                        break;
                    }
                    if("Terminei".equals(mensagem)){

                        //System.out.println("Servidor recebeu todas as soluções !");
                        break;
                    }
                    //System.out.println("Mensagem enviada pelo servidor: " +mensagem);
                    //System.out.println("No laço: " +executando);
                }
                catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("erro inicialização grasp !");
                    try {
                        this.stop();
                    } catch (Exception ex) {
                        
                    }
                    break;
                }
                
            }
            //Antes de encerrar devemos liberar todos os recursos !!!
            close();
        }
        if((obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao) || 
                                                        (obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao)){
        
            
            ObjetoComunicacaoMelhorado ob_atual_ = new ObjetoComunicacaoMelhorado();
            ob_atual_.setTipoServicoAgente(obcom.getTipoServicoAgente2());
            ob_atual_.setTipoServicoServidor(obcom.getTipoServicoServidor());
            
            while (!IniciaClientes.stop) {
                
               try {
                   Thread.sleep(300);
               }
               catch (InterruptedException ex) {}
               
               obcom.setTipoServicoAgente(ob_atual_.getTipoServicoAgente2());    //????Porquê dá nulo????
               obcom.setTipoServicoServidor(ob_atual_.getTipoServicoServidor());
               
               try {
            
                   //System.out.println("Connect 1 grasp ..."+this.toString());
                    
                    open("localhost", getPortaComunicacao());
                    
                    //System.out.println("Connect 2 grasp ...");
                    //socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
                    ObjetoComunicacaoMelhorado ob = receive();
                    //System.out.println("Connect 3 grasp ...");
                    
                    String msg = ob.getMSGServicoAgente();
                    
                    //System.out.println("Ocupado para grasp?: "+msg);
                    
                    if (msg.equals("true")) {

                        //System.out.println("Espere para reiniciar..");
                        
                        close();
                        continue;
                    }
                   
                    //System.out.println("Entrei no consulta GRASP-Melhoria |status "+executando);
                   
                    while (executando) {
               
//                           socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !

                           if((obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao)){
                           //if((getTipoServico() == ETiposServicosAgentes.Consulta_Solucao)){

                               //Ele escreve uma requisição para o Atendente !!!
                               obcom.setMSGServicoAgente("BuscaSolucao");
                               //System.out.println("Vou enviar um objeto de solicitação (melhoria)");
                               send(obcom);

                               //Leitura de informações envidas pelo servidor !!!
                               //System.out.println("Vou receber o objeto solicitado (melhoria)");
                               obcom = receive();
                               String mensagem = obcom.getMSGServicoAgente();

                               if("SolucaoPronta".equals(mensagem)){

                                   //Aqui chama o processa informação do Agente Grasp
                                   //System.out.println("Vou iniciar o Grasp Melhoria");
                                   SolucaoHeuristica solucao;
                                   
                                   if(this.getGraspMelhoriaType() == 1){
                                       solucao = Grasp2d_Melhoria(obcom.getSolucao(), PoliticaSelecao.PiorPior, 
                                                                                       getMaxIter(),getAlpha());
                                   }
                                   else{
                                       solucao = GraspMelhoria2version(obcom.getSolucao(),getMaxIter(),getAlpha(), getAprovt());
                                   }
                                   
                                   obcom.setSolucao(solucao);
                                   //System.out.println("Terminei o Grasp Melhoria e setei a solução");

                                   obcom.setMSGServicoAgente("Vou escrever a nova solução");
                                   obcom.setTipoServicoAgente(ETiposServicosAgentes.Escrever_Solucacao);
                                   obcom.setTipoServicoServidor(ETiposServicosServidor.AtualizaSolucao);
                               }
                               if("Terminei".equals(mensagem)){
                                   //System.out.println("Servidor retornou a solução adequada !");
                                   break;
                               }
                               if(mensagem == null){
                                   //System.out.println("Mensagem nula !");
                                   break;
                               }
                               //System.out.println("Mensagem enviada pelo servidor: " +mensagem);
                           }
                           if((obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao)){
                           //if((getTipoServico() == ETiposServicosAgentes.Escrever_Solucacao)){

                               //System.out.println("Vou enviar um objeto (melhoria)");
                               send(obcom);
                               //System.out.println("Aguardando confirmação do servidor");
                               obcom = receive();
                               String mensagem = obcom.getMSGServicoAgente();

                               if("Terminei".equals(mensagem)){

                                   //System.out.println("Servidor finalizou por completo a operação !");
                                   break;
                               }
                               if(mensagem == null){
                                   //System.out.println("Mensagem nula !");
                                   break;
                               }                   
                           }
                    }
                    //Antes de encerrar devemos liberar todos os recursos !!!
                    close();
                    
               } catch (Exception e) {
                   e.printStackTrace();
                   System.out.println("Ocorreu alguma exceção !");
                   close();  //Antes de encerrar devemos liberar todos os recursos !!!
               }
            }
        }       
    }
    
    public static void main(String args[])throws FileNotFoundException, IOException, PecaInvalidaException, Exception {
         
         int typeGrasp;
         float aprovPlaca;
         String name = " ";
         ServicoAgente tipo_agente = null;
         ETipoHeuristicas tipo_Heuristica = null;
         ETiposServicosAgentes operacao_agente    = null;
         ETiposServicosServidor operacao_servidor = null;
         
         if(args.length < 11){//7
             System.out.println("Está faltando argumentos !");
             System.exit(1);
         }
         /*##########################  CONFIGURANDO ARGUMENTOS DO ALGORITMO GRASP #########################################*/
         switch(Integer.parseInt(args[0])){
             
             case 4: tipo_Heuristica = ETipoHeuristicas.GRASP_2D_CONSTRUTIVO;  name = "GRASP_2D_CONSTRUTIVO";    break; 
             case 5: tipo_Heuristica = ETipoHeuristicas.Grasp2d_Melhoria ;     name = "GRASP_2D_MELHORIA";       break;
             case 6: tipo_Heuristica = ETipoHeuristicas.Grasp2dTree_Melhoria;  name = "GRASP_2D_MELHORIA_TREE";  break;
             default:
                 System.out.println("Nenhuma das opções são válidas para o algoritmo GRASP !");
                 System.exit(1);
         }
         switch(Integer.parseInt(args[1])){
         
             case 1: tipo_agente = ServicoAgente.Tree;                  break;
             case 2: tipo_agente = ServicoAgente.Alocacao;              break;
             case 3: tipo_agente = ServicoAgente.Combinacao;            break;
             case 4: tipo_agente = ServicoAgente.Permutacao;            break;
             case 5: tipo_agente = ServicoAgente.Inicializacao;         break;
             case 6: tipo_agente = ServicoAgente.GRASP_Melhoria;        break;
             case 7: tipo_agente = ServicoAgente.HHDHeuristic_Melhoria; break;
             default:
                System.out.println("Opção Inválida para o Serviço do Agente");
                System.exit(0);
         }
         switch(Integer.parseInt(args[2])){
           
            case 1: operacao_agente = ETiposServicosAgentes.Inicializacao_Grasp;        break;
            case 2: operacao_agente = ETiposServicosAgentes.Inicializacao_HHDHeuristic; break;
            case 3: operacao_agente = ETiposServicosAgentes.Inicializacao_Aleatoria;    break;
            case 4: operacao_agente = ETiposServicosAgentes.Consulta_Solucao;           break;
            case 5: operacao_agente = ETiposServicosAgentes.Escrever_Solucacao;         break;
            default:
                System.out.println("Opção Inválida para o Tipo de Serviço do Agente");
                System.exit(0);
        }
        switch(Integer.parseInt(args[3])){
            case 1: operacao_servidor = ETiposServicosServidor.Inserir_Solucao;     break;
            case 2: operacao_servidor = ETiposServicosServidor.AtualizaSolucao;     break;
            case 3: operacao_servidor = ETiposServicosServidor.MelhorSolucao;       break;
            case 4: operacao_servidor = ETiposServicosServidor.PiorSolucao;         break;
            case 5: operacao_servidor = ETiposServicosServidor.Solucao_Aleatoria ;  break;
            case 6: operacao_servidor = ETiposServicosServidor.Roleta;              break;
            default:
                System.out.println("Opção Inválida para o serviço do Servidor");
                System.exit(0);
        }
        
        int    porta   = Integer.parseInt(args[4]);
        double alpha   = Double.parseDouble(args[5]);
        int    maxIter = Integer.parseInt(args[6]);
        
        PoliticaSelecao polSelecaoSolucao = null, polSelecaoPlacas = null;
        
        switch(Integer.parseInt(args[7])){
            case 0:  polSelecaoSolucao = null;                                   break;
            case 1:  polSelecaoSolucao = PoliticaSelecao.Melhor;                 break;
            case 2:  polSelecaoSolucao = PoliticaSelecao.ProbabilidadeMelhor;    break;
            case 3:  polSelecaoSolucao = PoliticaSelecao.Aleatorio;              break;
            case 4:  polSelecaoSolucao = PoliticaSelecao.Pior;                   break;
            case 5:  polSelecaoSolucao = PoliticaSelecao.ProbabilidadeMenor;     break;
            default:
                System.out.println("Nenhuma opção para Política de Seleção de Solução");
        }
        switch(Integer.parseInt(args[8])){
            case 0: polSelecaoSolucao = null;                                    break;
            case 1:  polSelecaoPlacas = PoliticaSelecao.MelhorMelhor;            break;
            case 2:  polSelecaoPlacas = PoliticaSelecao.MelhorPior;              break;
            case 3:  polSelecaoPlacas = PoliticaSelecao.MelhorAleatorio;         break;
            case 4:  polSelecaoPlacas = PoliticaSelecao.PiorAleatorio;           break;
            default:
                System.out.println("Nenhuma opção para Política de  Seleção de Placas");
        }
        
        aprovPlaca  = Float.parseFloat(args[9]);
        typeGrasp   = Integer.parseInt(args[10]);
                
        /*##################### ENCERRANDO CONFIGURAÇÕES DE ARGUMENTOS ###############################################*/
        
        ObjetoComunicacaoMelhorado obj = new ObjetoComunicacaoMelhorado();
        obj.setTipoServicoAgente(operacao_agente);
        obj.setTipoServicoServidor(operacao_servidor);
        
        //Nessa parte o tipo de algoritmo é escolhido !!!
        if(tipo_Heuristica == ETipoHeuristicas.GRASP_2D_CONSTRUTIVO){
        
            //System.out.println("Iniciando execução do algoritmo Grasp Construtivo!");

            List<SolucaoHeuristica> solucao_GRASP = new LinkedList<SolucaoHeuristica>(); 

            List<Bin> Solucao = new ArrayList<Bin>();

            LinkedList<Heuristicas.Solucao> VetorSolucao = new LinkedList<Heuristicas.Solucao>();

            ArrayList<Item> L = new ArrayList<Item>();
            List<Item> Lc = new ArrayList<Item>();

            L = FuncoesGrasp.LerArq();

            List<IPedido> listaPedidosNaoAtendido = new ArrayList<IPedido>(L);

            FuncoesGrasp.OrdenaList(L);

            Lc = (List<Item>) L.clone();
//          FuncoesGrasp.ImprimeItens("\n\nLista", Lc); comentei aqui para execucao
            Dimensao2D d = FuncoesGrasp.lerArq();
            
            float larguraPlaca = d.retorneBase();
            float alturaPlaca  = d.retorneAltura();
                        
            solucao_GRASP = HeuristicaGRASP.Grasp2d(L,L, larguraPlaca, alturaPlaca, alpha, maxIter, listaPedidosNaoAtendido);
            //System.out.println("\nNumero de Solucoes Geradas --> "+ solucao_GRASP.size());

            //System.out.println("Iniciando cliente ... ");
            //System.out.println("Iniciando conexão com o servidor ... ");

            //Aqui vou conectar na memória
            HeuristicaGRASP HG2d = new HeuristicaGRASP(name, "localhost", porta, operacao_agente,operacao_servidor);
            //HG2d.open(name, porta_comunicacao);

            //Aqui todos os atributos do algoritmo são setados !!!
            HG2d.setAlpha(alpha);
            HG2d.setMaxIter(maxIter);
            HG2d.setObjetoComunicacaoMelhorado(obj);
            HG2d.setTipoHeuristica(tipo_Heuristica);
            //System.out.println("\nConexão estabelecida com sucesso ...");

            HG2d.setName(name);
            HG2d.setPortaComunicacao(porta);
            HG2d.setServicoAgente(tipo_agente);
            HG2d.setTipoServico(operacao_agente);
            HG2d.setAprovt(aprovPlaca);
            HG2d.setHHDType(typeGrasp);
            HG2d.setTipoServicoServidor(operacao_servidor);

            Iterator<SolucaoHeuristica> iter_solucao = solucao_GRASP.iterator();

            int cont = 0;

            while(iter_solucao.hasNext()){
                System.out.println("Solucao: "+cont);
                SolucaoHeuristica solucao_t = new SolucaoHeuristica(iter_solucao.next());
                HG2d.getVectorSolucoes().add(solucao_t); //vectorSolucoes.add(solucao_t);
                cont++;
            }

            //Iniciando a thread auxilair !!!
            //System.out.println("Vou iniciar o GRASP run() ...");
            HG2d.start_run();
        
        }
        
        //Aqui será o outro algoritmo GRASP de Melhoria !!!
        if(tipo_Heuristica == ETipoHeuristicas.Grasp2d_Melhoria){
        
            //System.out.println("Iniciando execução do algoritmo Grasp Melhoria!");
            //System.out.println("Iniciando cliente ... ");
            //System.out.println("Iniciando conexão com o servidor ... ");

            //Aqui vou conectar na memória
            HeuristicaGRASP HG2d = new HeuristicaGRASP(name, "localhost", porta, operacao_agente,operacao_servidor);
            //HG2d.open(name, porta_comunicacao);

            //Aqui todos os atributos do algoritmo são setados !!!
            HG2d.setAlpha(alpha);
            HG2d.setMaxIter(maxIter);
            HG2d.setObjetoComunicacaoMelhorado(obj);
            //System.out.println("\nConexão estabelecida com sucesso ...");

            HG2d.setName(name);
            HG2d.setPortaComunicacao(porta);
            HG2d.setServicoAgente(tipo_agente);
            HG2d.setTipoServico(operacao_agente);
            HG2d.setTipoHeuristica(tipo_Heuristica);
            HG2d.setTipoServicoServidor(operacao_servidor);
            HG2d.setPoliticaSelecaoSolucao(polSelecaoSolucao);
            HG2d.setPoliticaSelecaoPlacas(polSelecaoPlacas);
            HG2d.setAprovt(aprovPlaca);
            HG2d.setHHDType(typeGrasp);
            
            //Iniciando a thread auxilair !!!
            System.out.println("Vou iniciar o run() GRASP Melhoria  ...");
            HG2d.start();
        }
        if(tipo_Heuristica == ETipoHeuristicas.Grasp2dTree_Melhoria){ /*Esse trecho de código aqui irá ficar dentro do */}
     }
  /*  
     public static void main(String[] args) throws FileNotFoundException, IOException, PecaInvalidaException, Exception {

        //Aqui deve iniciar as variáveis para os testes !!!
        //Por enquanto estão sendo setados os valores das variáveis (OBS: Devem ser pedidas no argumento)
        int porta_comunicacao = 3000;
        String name = "GRASP_2D_CONSTRUTIVO";
        ServicoAgente tipo_agente = ServicoAgente.Inicializacao;
        ETiposServicosAgentes operacao_agente = ETiposServicosAgentes.Inicializacao_Grasp;
        ETiposServicosServidor operacao_servidor = ETiposServicosServidor.Inserir_Solucao;
        
        System.out.println("Iniciando cliente ... ");
        System.out.println("Iniciando conexão com o servidor ... ");
    
        //Aqui vou conectar na memória
        HeuristicaGRASP HG2d = new HeuristicaGRASP(name, "localhost", porta_comunicacao,operacao_agente,operacao_servidor);
        
        System.out.println("Conexão estabelecida com sucesso ...");
        
        HG2d.setName(name);
        HG2d.setPortaComunicacao(porta_comunicacao);
        HG2d.setServicoAgente(tipo_agente);
        // HG2d.setTipoServico(operacao_agente);
        HG2d.setTipoServicoServidor(operacao_servidor);
        
        //Iniciando a thread auxilair !!!
        HG2d.start();
        
        //Aqui é realizada a execução do Algoritmo !!!             
        List<SolucaoHeuristica> solucao_GRASP = new LinkedList<SolucaoHeuristica>(); 
        
        List<Bin> Solucao = new ArrayList<Bin>();
        
        LinkedList<Heuristicas.Solucao> VetorSolucao = new LinkedList<Heuristicas.Solucao>();

        ArrayList<Item> L = new ArrayList<Item>();
        List<Item> Lc = new ArrayList<Item>();

        L = FuncoesGrasp.LerArq();
        
        List<IPedido> listaPedidosNaoAtendido = new ArrayList<IPedido>(L);
        
        FuncoesGrasp.OrdenaList(L);
        
        /*System.out.println("Lista Pedidos Não-Atendidos");
        for(int index = 0; index < listaPedidosNaoAtendido.size(); index++)
            System.out.print(listaPedidosNaoAtendido.get(index).id() + " - ");
                
        Lc = (List<Item>) L.clone();
        FuncoesGrasp.ImprimeItens("\n\nLista", Lc);

        solucao_GRASP = HeuristicaGRASP.Grasp2d(L,L, 50, 50,0.5, 1, listaPedidosNaoAtendido);
        
        //HeuristicaGRASP.Grasp2d_Melhoria(solucao_GRASP.get(0), PoliticaSelecao.Melhor, 9, 0.5);
        IGenericSolution solucaoConvertida = SolutionConversorFactory.getInstance().newConversor().converteParaSolucao(solucao_GRASP.get(0),true);

         ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> solucao = HHDHeuristic.VerificaImprimeArvoreDeCorte(solucaoConvertida);//(BinPackTreeForest)
         
         System.out.println("######");
         
         Utilidades.FuncoesGrasp.imprimeBinPackTree(solucao);
        
         System.out.println("######");
         
        //Aqui será criado um vetor de ByteBuffer para armazenar os Objetos Comunicação
        ByteBuffer[] vetor_Byte_Buffer = new ByteBuffer[solucao_GRASP.size()]; 
        ArrayList<ByteBuffer> arrayBuffer = new ArrayList<ByteBuffer>(solucao_GRASP.size());
        
        System.out.println("\nNumero de Solucoes Geradas --> "+ solucao_GRASP.size());
        
        Iterator<SolucaoHeuristica> iter_solucao = solucao_GRASP.iterator();
        int cont = 0;
        
        while(iter_solucao.hasNext()){
            
            SolucaoHeuristica solucao_t = new SolucaoHeuristica();
            solucao_t = iter_solucao.next();
            
            //Aqui serializa o Bin Comunicação Melhorado com as soluções e armazena em um vetor de ByteBuffer
            vetor_Byte_Buffer[cont] = (HG2d.serializaMensagemMelhorado(new ObjetoComunicacaoMelhorado(solucao_t, name, operacao_agente, 
                                            operacao_servidor)));            
            System.out.println("Solucao "+cont+" Com "+solucao_t.getObjetos().size()+" individuos\n");
            
            Iterator<Bin> iter_objeto = solucao_t.getObjetos().iterator();
            
            while(iter_objeto.hasNext()){
            //for(int k = 0; k < solucao_t.getObjetos().size(); k++){
            
                Individuo ind =  new Individuo();
                ind = iter_objeto.next().getIndividuo();
            
                System.out.println("List itens --> "+ ind.getListaItens());
            } 
            
            System.out.println();
            
            cont++;
        }
      
    }
*/
   
}