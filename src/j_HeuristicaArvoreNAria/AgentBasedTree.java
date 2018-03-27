package j_HeuristicaArvoreNAria;

import java.net.Socket;
import java.io.IOException;
import java.util.LinkedList;
import ATeam.IniciaClientes;
import ATeam.ETipoHeuristicas;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import SimulaGenetico.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ServicoAgente;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoTree;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;

public class AgentBasedTree implements Runnable{

    private Socket socketClientFinal;
    private ObjectInputStream     in;  //Entrada para leitura do socket
    private ObjectOutputStream   out; //Saída para escrita no socket        
    
    private boolean inicializado;
    private boolean executando;
    private Thread  thread;
    private String  name;
    private boolean rotaciona;
    private boolean first_Fit;
    private ServicoAgente tipo_agente;
    private ETipoHeuristicas tipo_heuristica;
    private ETiposServicosAgentes  operacao_agente;    //Tipo de operação realizada pelo agente
    private ETiposServicosServidor operacao_servidor; //Tipo de operação requerida da memória pelo agente 
    private int porta_comunicacao;
    ObjetoComunicacaoTree objeto = null;// = new ObjetoComunicacaoMelhorado();
    
    public AgentBasedTree(String name, String endereco, int portacomunicacao, ETiposServicosAgentes eSAgn,
                                                                                    ETiposServicosServidor eSServ) throws Exception{
        this.name = name;
        this.porta_comunicacao = portacomunicacao;
        this.operacao_servidor = eSServ;
        this.operacao_agente   = eSAgn;
      
        inicializado = false;
        executando = false;   
        objeto = new ObjetoComunicacaoTree();
        
        //open(endereco, portacomunicacao);
    }
    
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
            System.out.println("Exceção IO no open do AgenteBasedTree");
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
  
    public void start_run(){
    
        if (executando) {

            return;
        }

        executando = true;
        thread = new Thread(this);
        thread.run();
    }
    
    public void start(){//Inicia a thread auxiliar, se possível. O objeto cliente precisa estar inicializado e não pode está executando !
    
        if (executando) {

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
    public void deveEnviar(ObjetoComunicacaoTree obj){
    
        this.objeto = obj;
    }
    
    public ObjetoComunicacaoTree getDeveEnviar(){
    
        return this.objeto;
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
  
    public ObjetoComunicacaoTree receive(){
  
        ObjetoComunicacaoTree objetoComunicacao = null;
       
        try{
            objetoComunicacao = (ObjetoComunicacaoTree) in.readObject();
        }
        catch(IOException ex){
            System.out.println("Erro de Entrada e saída no receive do CLiente !!!");
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
    
    public ETipoHeuristicas getTipoHeuristica() {

        return this.tipo_heuristica;
    }

    public void setTipoHeuristica(ETipoHeuristicas etipoheuristica) {

        this.tipo_heuristica = etipoheuristica;
    }
  
    public ObjetoComunicacaoTree getObjetoComunicacaoTree() {
    
        return this.objeto;
    }
    
    public void setObjetoComunicacaoTree(ObjetoComunicacaoTree obj) {
    
        this.objeto.setMSGServicoAgente(obj.getMSGServicoAgente());
        this.objeto.setSolucao(obj.getSolucao());
        this.objeto.setTipoServicoAgente(obj.getTipoServicoAgente2());
        this.objeto.setTipoServicoServidor(obj.getTipoServicoServidor());
    }
    
    @Override
    public void run(){ //Run do Grasp
         
        //Só encerrará conexão quando o servidor receber todas as soluções criadas !!!
        //Agora o método o run() que será executado pela thread auxiliar.....
        ObjetoComunicacaoTree obcom = getObjetoComunicacaoTree();
        
        if ((obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao)
                                            || (obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao)) {

            ObjetoComunicacaoTree ob_atual = new ObjetoComunicacaoTree();
            ob_atual.setTipoServicoAgente(obcom.getTipoServicoAgente2());
            ob_atual.setTipoServicoServidor(obcom.getTipoServicoServidor());
        
            while (!IniciaClientes.stop) {
                
                try {
                    Thread.sleep(400);
                }
                catch (InterruptedException ex) {}
                
                obcom.setTipoServicoAgente(ob_atual.getTipoServicoAgente2());
                obcom.setTipoServicoServidor(ob_atual.getTipoServicoServidor());
            
                try{
                    
                   System.out.println("Connect 1 IH..."+this.toString());
                    
                    open("localhost", getPortaComunicacao());
                    
                    System.out.println("Connect 2 IH...");
                    //socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
                    ObjetoComunicacaoTree ob = receive();
                    System.out.println("Connect 3 IH...");
                    
                    String msg = ob.getMSGServicoAgente();
                    
                    System.out.println("Ocupado IH?: "+msg);
                    
                    if (msg.equals("true")) {

                        System.out.println("Espere para reiniciar..");
                        
                        close();
                        continue;
                    }

                    System.out.println("Entrei no Insertion ..."); 

                    while(executando){ 
                    
                        if(obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao){ 

                            System.out.println("Entrei para solicitar solucao a memoria 2");

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
                                System.out.println("Vou iniciar o Justification !");
                                //Esse objetoM1 será manipulado no próximo passo, e escreverá em uma memória diferente !

                                this.setObjetoComunicacaoTree(obcom);

                                System.out.println("Terminei a busca para o Justification e setei a solução");

                                obcom.setTipoServicoAgente(getTipoServico());

                                //Vou aplicar o justification na solução
                                SolucaoNAria solucaoArvore = new SolucaoNAria();
                                Metodos_heuristicos m = new Metodos_heuristicos();
                                
                                System.out.println("$$$$$$$$$$ Solução que o Justifcation vai trabalhar $$$$$$");
                                System.out.println("FAV2 -> "+ obcom.getSolucao().getFAV2());
                                System.out.println("FAV -> "+ obcom.getSolucao().getFAV());
                                System.out.println("Somatorio sobra -> "+ obcom.getSolucao().getSomatorioSobra());
                                System.out.println("Fav -> "+ obcom.getSolucao().get_Fav());
                                System.out.println("Tamanho de bins -> "+ obcom.getSolucao().retornaSolucao().size());
                                solucaoArvore = m.Justificacao(obcom.getSolucao(), 4, Utilidades.Funcoes.getVetor_info(),
                                                                                                               Utilidades.Funcoes.getChapa());
                                solucaoArvore.calculaSomatorioSobra();
                                solucaoArvore.setTipoHeuristic(getTipoHeuristica()); 
                                solucaoArvore.imprime_solucao();
                                
                                //ObjetoComunicacaoTree objTree = new ObjetoComunicacaoTree();

                                System.out.println("Preparando para escrever solução Justification ... ");
                                obcom.setMSGServicoAgente("Escrevendo Solução");
                                obcom.setSolucao(solucaoArvore);
                                obcom.setTipoServicoAgente(ETiposServicosAgentes.Escrever_Solucacao);
                                obcom.setTipoServicoServidor(ETiposServicosServidor.Inserir_Solucao);

                                this.setObjetoComunicacaoTree(obcom);

                                //send(objTree);
                                //objTree = receive();
                                //mensagem = objTree.getMSGServicoAgente();                                
                            }
                            if("Terminei".equals(mensagem)){
                                    
                                System.out.println("Servidor escreveu a solução do Justification!");
                                break;
                            }
                            if(mensagem == null){
                                System.out.println("Mensagem nula !");
                                break;
                            }
                        }
                        
                        if(obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao){ 

                            //socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !

                            System.out.println("Vou escrever uma solução na Memória 2;");
                            obcom.setMSGServicoAgente("Escrevendo Solução");
                            System.out.println("Vou enviar um objeto");
                            send(obcom);
                            
                            System.out.println("Aguardando confirmação do servidor");
                            obcom = receive();
                            String mensagem = obcom.getMSGServicoAgente();
                            
                            if(mensagem == null){
                                System.out.println("Mensagem nula !");
                                break;
                            }
                            if("Terminei".equals(mensagem)){
                                System.out.println("Servidor escreveu uma solução !");
                                break;
                            }
                            System.out.println("Mensagem enviada pelo servidor: " +mensagem);
                            
                        }
                    }//while(executando)
                    close();  //Antes de encerrar devemos liberar todos os recursos !!!                   
                
                }catch (Exception e) {
                    System.out.println("Ocorreu alguma exceção !");
                    close();  //Antes de encerrar devemos liberar todos os recursos !!!
                }
                
                //Retorna ao while true !!!
                if(getTipoHeuristica() == ETipoHeuristicas.FirstFit || getTipoHeuristica() == ETipoHeuristicas.BestFit){
                
                    break;
                }
            }
        } 
    }

    public static void main(String[] args) throws IOException, Exception {
	
        int porta;
        boolean rotaciona;
        boolean firstFit;
        String name = " ";
        ServicoAgente tipo_agente = null;
        ETipoHeuristicas tipo_Heuristica = null;
        ETiposServicosAgentes operacao_agente    = null;
        ETiposServicosServidor operacao_servidor = null;
        
        if(args.length < 7){
            System.out.println("Está faltando argumentos !");
            System.exit(1);
        }
                          //Tipo de Heuristica Utilizada
        switch(Integer.parseInt(args[0])){
            case 1: tipo_Heuristica = ETipoHeuristicas.FirstFit;       name = "FirstFit";      break;
            case 2: tipo_Heuristica = ETipoHeuristicas.BestFit;        name = "BestFit";       break;
            case 3: tipo_Heuristica = ETipoHeuristicas.Justification;  name = "Justification"; break;
        }
                          //Serviços de Inicialização, Escrita e Consulta
        switch(Integer.parseInt(args[1])){
            case 1: tipo_agente = ServicoAgente.Inicializacao;      break;
            case 2: tipo_agente = ServicoAgente.Tree;               break;
        }
        switch(Integer.parseInt(args[2])){
            case 1:operacao_agente = ETiposServicosAgentes.Consulta_Solucao;      break;
            case 2:operacao_agente = ETiposServicosAgentes.Escrever_Solucacao;    break;
        }
        switch(Integer.parseInt(args[3])){
            case 1: operacao_servidor = ETiposServicosServidor.Inserir_Solucao;   break;
            case 2: operacao_servidor = ETiposServicosServidor.AtualizaSolucao;   break;
            case 3: operacao_servidor = ETiposServicosServidor.MelhorSolucao;     break;
            case 4: operacao_servidor = ETiposServicosServidor.PiorSolucao;       break;
            case 5: operacao_servidor = ETiposServicosServidor.Solucao_Aleatoria; break;
        }
        
        porta = Integer.parseInt(args[4]);
        
        if(Integer.parseInt(args[5]) == 1){
            rotaciona = true;
        }else rotaciona = false;
        
        if(Integer.parseInt(args[6]) == 1){
            firstFit = true;
        }else firstFit = false;
        
        //################## ENCERROU CONFIGURAÇÃO DE ARGUMENTOS ##################################
        
        ObjetoComunicacaoTree objTree = new ObjetoComunicacaoTree();
        objTree.setTipoServicoAgente(operacao_agente);
        objTree.setTipoServicoServidor(operacao_servidor);
        
        if((tipo_Heuristica == ETipoHeuristicas.FirstFit) ||(tipo_Heuristica == ETipoHeuristicas.BestFit)){
        
            System.out.println("Iniciando execução do Algoritmo Insertion Heuristic");
        
            LinkedList<Pedidos> pedidos;	
            pedidos = Funcoes.lerArquivo();
            
            Funcoes.imprimeListaPedidos(pedidos);
            System.out.println("\nPedidos ordenados\n\n");
            Funcoes.ordenaPedidosDecrescente(pedidos);
            //Funcoes.imprimeListaPedidos(pedidos);
        
            SolucaoNAria solucao = new SolucaoNAria();
            Metodos_heuristicos m = new Metodos_heuristicos();
        
            //(Rotaciona, FirstFit, Pedidos, Chapa)
            solucao = m.FFIH_BFIH(rotaciona,firstFit, pedidos, Funcoes.chapa);
            solucao.calculaSomatorioSobra();
            solucao.setTipoHeuristic(tipo_Heuristica); 
            objTree.setSolucao(solucao);
        
            System.out.println("Imprimindo a solução gerada pelo Algoritmo escolhido !");
            solucao.imprime_solucao();
        }
                                
        System.out.println("Iniciando cliente ... ");
        System.out.println("Iniciando conexão com o servidor ... ");
        
        AgentBasedTree agenteArvore = new AgentBasedTree(name, "localhost", porta, operacao_agente, operacao_servidor);
        
        agenteArvore.setObjetoComunicacaoTree(objTree);
        System.out.println("\nConexão estabelecida com sucesso ...");

        agenteArvore.setName(name);
        agenteArvore.setPortaComunicacao(porta);
        agenteArvore.setServicoAgente(tipo_agente);
        agenteArvore.setTipoServico(operacao_agente);
        agenteArvore.setTipoHeuristica(tipo_Heuristica);
        agenteArvore.setTipoServicoServidor(operacao_servidor);
        
        
        if(agenteArvore.getTipoHeuristica() == ETipoHeuristicas.FirstFit 
                                    ||agenteArvore.getTipoHeuristica() == ETipoHeuristicas.BestFit){
        
            agenteArvore.start_run();
        
        }
        else{
            //Iniciando a thread auxiliar !!!
            agenteArvore.start();
        }
    }
}