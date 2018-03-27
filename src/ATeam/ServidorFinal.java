package ATeam;

import java.io.File;
import java.util.List;
import java.net.Socket;
import java.util.Scanner;
import java.io.FileWriter;
import java.util.Iterator;
import Utilidades.Funcoes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.net.ServerSocket;
import HHDInternal.HHDHeuristic;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import HHDInternal.SolucaoHeuristica;
import Utilidades.PoliticaDestruicao;
import HHDInterfaces.IGenericSolution;
import java.net.SocketTimeoutException;
import HeuristicaConstrutivaInicial.Bin;
import HHDBinPackingTree.BinPackTreeForest;
import HHD_Exception.PecaInvalidaException;
import SimulaGenetico.ETiposServicosAgentes;
import HHDInternal.SolutionConversorFactory;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;
import HHDInternal.Corte;
import java.util.logging.Level;
import java.util.logging.Logger;
import HHDBinPackingTree.BinPackTree;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.ISolution;
import HHDInternal.CutLayoutVisualization;
import HHDInternal.Peca;
import HHDInternal.PedacoDisponivel;
import java.awt.Dimension;
import java.awt.Graphics;

public class ServidorFinal implements Runnable {

    private ServerSocket serverSocket;//para receber conexões de clientes
    private boolean inicializado;     //atributo booleano pra saber se o servidor foi iniciado ou não
    private static boolean executando;       //atributo booleano pra saber se o servidor está executando ou não
    private static boolean trabalhando;
    private Thread thread;            //atributo Thread  para controlar e representar a thread auxiliar para recebimento de conexões do servidor;
    private List<AtendenteCliente> atendentes; //atributo que armazena a lista de atendentes!
    private String name;
    private int tamanho_memoria;
    private int num_solucoes;
    private LinkedList<SolucaoHeuristica> lista_solucoes;
    private LinkedList<BinPackTreeForest> listForest;
    //  private LinkedList   requisicoes;
    private PoliticaDestruicao politicaDestruicao;
    private Socket socketCliente = null;
    private static FileWriter arquivoPrincipal_NumBins;
    private static FileWriter arquivoPrincipal_FAV;
    private static FileWriter arquivoPrincipal_FAV2;
    private static FileWriter arquivoPrincipal_MediaSobra;
    private static FileWriter arquivoPrincipal_SomatorioSobra;
    private static FileWriter arquivoFAV1;
    private static FileWriter arquivoFAV2;
    private static FileWriter arquivoMediaSobra;
    private static FileWriter arquivoSomatorioSobra;
    private static FileWriter arquivoNumBins;
    private static FileWriter arquivoPrincipalSolucao;
    private static FileWriter arquivoMelhorSolucao;
    private static FileWriter arquivoFinalCompleto;
    private static FileWriter arqBinAvaliacao;
    private static FileWriter desenho_Solucao;
    private long ini;
    private int contador = 0;
    SolucaoHeuristica solucao = new SolucaoHeuristica();
    SimulaGenetico.OperacoesSolucoes_Individuos operacoesSolucoes = new SimulaGenetico.OperacoesSolucoes_Individuos();

    public ServidorFinal(String name, int portaComunicacao, int tamanho_memoria, PoliticaDestruicao polDestruicao) throws Exception {

        atendentes = new ArrayList<AtendenteCliente>();
        inicializado = false;
        executando = false;
        trabalhando = false;

        this.name = name;
        this.tamanho_memoria = tamanho_memoria;
        this.num_solucoes = 0;
        lista_solucoes = new LinkedList<SolucaoHeuristica>();
        listForest = new LinkedList<BinPackTreeForest>();
        this.politicaDestruicao = polDestruicao;
//      requisicoes = new LinkedList();

        abriArquivosGraficos();
        open(portaComunicacao);
    }

    private void open(int portaComunicacao) throws Exception {

        serverSocket = new ServerSocket(portaComunicacao,1000);
        inicializado = true;
    }

    private void close() { //Tem a responsabilidade de liberar recursos alocados pelo objeto Servidor !

        //OBS: Deve-se encerrar cada um dos atendentes !
        for (AtendenteCliente atendente : atendentes) {

            try {
                //Chamar o método stop() de cada atendente que encerrar a thread auxiliar e libera recursos alocados pelo atendente !
                atendente.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Reinicia os valores dos atributos do servidor

        serverSocket = null;

        inicializado = false;
        executando = false;

        thread = null;
    }

    private void start() {//Método para iniciar a execução do servidor. Ou seja para iniciar a thread auxiliar do servidor !

        if (!inicializado || executando) {

            return;
        }

        executando = true;

        thread = new Thread(this); //Ao criamos a nova thread, passamos como objeto o objeto que implementa a interface Runnable.
        //A nova thread executará o método run que foi implementado no objeto informado o this !
        thread.start(); //A chamada desse método start() faz com que a thread inicie a execução do método run indicado.

        //System.out.println("Executando??? " + isExecutando());

    }

    public static boolean isExecutando() {

        return executando;
    }

    private void stop() throws Exception { //Forma correta de controlar o inicio e parada de qualquer thread auxiliar;

        /*if(!inicializado || executando){
        return;
        }*/
        executando = false;

        if (thread != null) {

            thread.join(); // Esse método bloqueia a thread atual até que a thread auxilair seja finalizada;          
        }
    }

    @Override
    public void run() { //Dentro do método run que será executado pela thread auxiliar

        //System.out.println("Aguardando conexão !");
        ini = System.currentTimeMillis();

        while(executando) { //Torna-se true no método start() e false no método stop();

            //System.out.println("Esperando alguém conectar ... ");

            try {

                serverSocket.setSoTimeout(2500);//É necessário definir um tempo limite para a thread e verificar a condição do laço !

                //Mudei aqui
                final Socket socket ;
                final ObjectInputStream input;
                final ObjectOutputStream output;
                
                socket = serverSocket.accept();
                
                synchronized (this) {
                    
                    //ObjectInputStream input;
                    //ObjectOutputStream output;

                    //System.out.println("Conexão estabelecida !");
                                
                    input  = new ObjectInputStream(socket.getInputStream());
                    output = new ObjectOutputStream(socket.getOutputStream());

                    //System.out.println("Connected, Esta ocupado ?");

                    //synchronized (this) {

                    String ocp = "";
//
//                    if (getNum_solucoes() == 0) {
//                        ocp = "true";
//                    } else {
                    ocp = Boolean.toString(ocupadoMomento());
//                    }

                    escreveInformacao(new ObjetoComunicacaoMelhorado(null, ocp, null, null), output);

                    if (ocupadoMomento()) {

                        //System.out.println("Sim, retorne....");
                        socket.close();
                                                
                        continue;
                    } else {
                        //System.out.println("Não !!!!!!!!!!!!");
                        setOcupado();
                    }
                }
                //Vai adicionanando as requisições ao final da fila
                //adicionaRequisicao(socket);

                
                //System.out.println("Atendendo requisiçao .....");
                new Thread(){

                    @Override
                    public void run() {
                        try {
                            atendeRequisicao(socket, input, output); //Atende requisição na forma de fila !
                        } catch (Exception ex) {
                            //ex.printStackTrace();                                                        
                        } 
                    }
                    
                }.start();
                //System.out.println("Esta sendo atendida .....");
                

                //}
                //Devemos atendê-lo. Ou seja obter o Input e Output stream. Ler a mensagem, devolver a mensagem até msg FIM ser enviada !

                //Implementaremos uma classe AtendenteCliente. Na sua criação informamos o socket que representa o cliente;
                //System.out.println("\nVou criar um Atendente");

                //AtendenteCliente atendenteCliente = new AtendenteCliente((atendentes.size() + 1),socket, this);

                //System.out.println("Criei um atendente !");

                //Cada atendenteCliente criado é um recurso alocado pelo servidor.Guardando assim uma lista,
                //para que antes de encerrar, ele pudesse liberar também estes recursos.
                //atendenteCliente.start(); //Responsável por gerenciar o atendimento do cliente espcificado conexctado !

                //System.out.println("Atendente já está atendendo ... ");

                //atendentes.add(atendenteCliente);

                //System.out.println("Fim, Retornar ao laço...");

            } catch (SocketTimeoutException e) {
                //Exceção indicando que o Timeout ocorreu. Thread desbloqueada. Logo a condição será reavaliada
                //e.printStackTrace();
            } catch (Exception e) {
                //Qualquer outra Exceção considerada fatal !
                //e.printStackTrace();
                break;
            }
        }

        close(); //Após a execução do laço devemos liberar os recursos alocados antes da finalização da thread, que encerrar ao terminar o método run();
        //}
        Funcoes.ordenaSolucoesHeuristicasDecrescente(this.lista_solucoes);
        abriArquivosGraficosPrincipal();

    }

    /**
     * * AQUI SERÁ IMPLEMENTADO OS MÉTODOS QUE SIMULAM O SERVIÇO FORNECIDO PELO SERVIDOR/MEMÓRIA
     * * MÉTODOS FORAM COPIADOS DO CÓDIGO MEMÓRIA   * 
     * *
     * * 
     * * 
     **/
    
    public static boolean ocupadoMomento() {

        return trabalhando;
    }

    private void setOcupado() {

        if (ocupadoMomento()) {
            
            trabalhando = false;
        } else {

            trabalhando = true;
        }
    }

    private void atendeRequisicao(Socket socket, ObjectInputStream in, ObjectOutputStream out) throws IOException, InterruptedException {

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        while (true) {

            //System.out.println("Laço ..................");
            ObjetoComunicacaoMelhorado objetoCom = null;

            //O Atendente irá processar a requisição do Cliente !
            objetoCom = processaInformacao(in, out);
            
            if(objetoCom == null){
            
                //System.out.println("Estou encerrando ... ENFDR");
                //setOcupado();
                break;
            }
            if ("Terminei".equals(objetoCom.getMSGServicoAgente())) {

                escreveInformacao(objetoCom,out);
                //System.out.println("Encerrando conexão !");

                break;
            } else {
                escreveInformacao(objetoCom, out);
            }
        }
        socket.close(); //Ates de encerrar o método run(), liberamos todos os recursos alocados pelo AtendenteCliente!!!
        setOcupado();
    }

//  private void adicionaRequisicao(Socket socket_cliente){
//  
//      
//      requisicoes.add(socket_cliente);
//      System.out.println("Adicionei uma requisição --> "+ requisicoes.size());
//  }
    synchronized public ObjetoComunicacaoMelhorado processaInformacao(ObjectInputStream in, ObjectOutputStream out) throws IOException, InterruptedException {

        ObjetoComunicacaoMelhorado objeto;// = new ObjetoComunicacaoMelhorado();

        objeto = recebeInformacao(in);
 
        if(objeto == null){
        
            //System.out.println("Objeto comunicação nulo ......");
            return null;            
        }
        
        String mensagemServico = objeto.getMSGServicoAgente();
        //System.out.println("Recebi : " + mensagemServico);
        ETiposServicosAgentes servicoAgente = objeto.getTipoServicoAgente2();
        ETiposServicosServidor servicoServidor = objeto.getTipoServicoServidor();

        SolucaoHeuristica solucao_;

        /*if("Terminei".equals(mensagemServico)){
        
        return objeto;
        }*/
        //SERVIÇOS DE INICIALIZAÇÃO POR PARTE DO AGENTE NO SERVIDOR !

       
        if (servicoServidor == ETiposServicosServidor.Inserir_Solucao) {

            if (servicoAgente == ETiposServicosAgentes.Inicializacao_Grasp) {

                if (mensagemServico.equals("Ainda_Enviando")) {

                    solucao_ = new SolucaoHeuristica(objeto.getSolucao());
                    solucao_.setTipoHeuristic(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO);
                    this.adiciona_solucao(solucao_);

                    //Retorna para o Cliente que está pronto para nova solicitação 1
                    objeto.setMSGServicoAgente("OK");

                    return objeto;
                }
                if (mensagemServico.equals("Terminei_Envio")) {

                    solucao_ = new SolucaoHeuristica(objeto.getSolucao());
                    solucao_.setTipoHeuristic(ETipoHeuristicas.GRASP_2D_CONSTRUTIVO);
                    this.adiciona_solucao(solucao_);

                    //Retorna para o Cliente que está pronto para nova solicitação 1
                    objeto.setMSGServicoAgente("Terminei");

                    return objeto;
                }
            }

            if (servicoAgente == ETiposServicosAgentes.Inicializacao_HHDHeuristic) {

                //System.out.println("Atendente para HHDHeuristic");
                if (mensagemServico.equals("Terminei_Envio")) {

                    solucao_ = new SolucaoHeuristica(objeto.getSolucao());
                    solucao_.setTipoHeuristic(ETipoHeuristicas.HHDHeuristic);
                    this.adiciona_solucao(solucao_);

                    //Retorna para o Cliente que está pronto para nova solicitação 1
                    objeto.setMSGServicoAgente("Terminei");

                    return objeto;
                }
            }
            if (servicoAgente == ETiposServicosAgentes.Inicializacao_Aleatoria) {
                /*Não faz nada pro enquanto !!! */
            }

        }

        if (servicoServidor == ETiposServicosServidor.AtualizaSolucao) {

            //Serviços de atualização de Memória do Servidor
            //Aqui ele adicionará uma solução atualizada na memória !!!
            //System.out.println("Cliente quer atualizar solução na memória");
            if (servicoAgente == ETiposServicosAgentes.Escrever_Solucacao) {

                if(objeto.getMSGServicoAgente().equalsIgnoreCase("Vou adicionar a nova solução em uma lista reservada !")){
                
                    if(objeto.getTreeSolucao() == null){
                      
                        //Retorna para o Cliente que está pronto para nova solicitação 1
                        objeto.setMSGServicoAgente("Terminei");
                        //System.out.println("Não adicionei pois é nula ...");
                        
                        return objeto;
                    }
                    else{
                        
                        this.getBinPackTreeForest().add(objeto.getTreeSolucao());
                        int numSol = getNum_solucoes();
                        try {
                            //Aqui eu vou criar uma solução heurística a partir de uma floresta BinPackTreeForest
                            SolucaoHeuristica solucao_tree_convertida = new SolucaoHeuristica((IGenericSolution)objeto.getTreeSolucao());
                        
                            //System.out.println("\n###### Solução Heuristica Tree Conversion Solution #########");
                            //solucao_tree_convertida.imprimeSolucao(solucao_tree_convertida, numSol);
                            
                            objeto.setSolucao(solucao_tree_convertida);
                            
                        }catch (PecaInvalidaException ex) {
                            System.out.println("Ocorreu algum erro na hora da conversão ... ");
                        }catch (Exception ex) {
                            System.out.println("Ocorreu algum erro na hora da conversão ... ");
                            System.out.println("Algum elemento na impressão pode ter dado erro");
                        }
                        
                        BinPackTreeForest btf = objeto.getTreeSolucao();
                    
                        if((objeto.getTreeSolucao().getFAV2()) >= (getLista_solucoes().get(numSol-1).getFAV2().floatValue())){
                    
                            long fim = System.currentTimeMillis();
                            long diff = fim - ini;
                        
                            arquivoFAV1.write((diff / 1000) + " " + (btf.getFAV() * 100) + "\n");
                            arquivoFAV2.write((diff / 1000) + " " + btf.getFAV2() + "\n");
                            arquivoMediaSobra.write((diff / 1000) + " " + btf.getMediaSobras() + "\n");
                            arquivoSomatorioSobra.write((diff / 1000) + " " + btf.getSomatorioSobras() + "\n");
                            arquivoNumBins.write(getNum_solucoes() + " " + btf.getQtd() + "\n");
                            arquivoFinalCompleto.write((diff / 1000) + " " +(btf.getFAV() * 100) +" " + btf.getFAV2() + " " + 
                                                                          btf.getMediaSobras() +  " " + btf.getSomatorioSobras() + " "+
                                                                          btf.getQtd() + " " + btf.getQtdLinhasCorte() + " "+
                                                                          "HHDHeuristicTree"+" "+(this.getNum_solucoes()+1)+
                                                                          "\n" );
                    
                        }
                        if (this.getNum_solucoes() < this.getTamanho_memoria()) {

                            this.adiciona_solucao(objeto.getSolucao());
                            //Retorna para o Cliente que está pronto para nova solicitação 1
                            objeto.setMSGServicoAgente("Terminei");

                            return objeto;
                        } else {
                            //Chama AgenteDestruidor
                            //System.out.println("Chamei o agente destruidor antes -> " + getNum_solucoes());

                            synchronized (this) {
                                this.agenteDestruidor();
                            }
                            //System.out.println("Chamei o agente destruidor depois -> " + getNum_solucoes());
                            this.adiciona_solucao(objeto.getSolucao());

                            //Retorna para o Cliente que está pronto para nova solicitação 1
                            objeto.setMSGServicoAgente("Terminei");

                            return objeto;
                        }
//                        System.out.println("Adicionei a lista das BinsPacksTrees ... ");        
//
//                        //Retorna para o Cliente que está pronto para nova solicitação 1
//                        objeto.setMSGServicoAgente("Terminei");
//
//                        return objeto;
                    }
                }
                if (this.getNum_solucoes() < this.getTamanho_memoria()) {

                    this.adiciona_solucao(objeto.getSolucao());

                    //Retorna para o Cliente que está pronto para nova solicitação 1
                    objeto.setMSGServicoAgente("Terminei");

                    return objeto;
                } else {
                    //Chama AgenteDestruidor
                    //System.out.println("Chamei o agente destruidor antes -> " + getNum_solucoes());

                    synchronized (this) {
                        this.agenteDestruidor();
                    }
                    //System.out.println("Chamei o agente destruidor depois -> " + getNum_solucoes());
                    this.adiciona_solucao(objeto.getSolucao());

                    //Retorna para o Cliente que está pronto para nova solicitação 1
                    objeto.setMSGServicoAgente("Terminei");

                    return objeto;
                }

                //Aqui assim que escreve uma solução na memória esta deve ser ORDENADA !!! ???????????
            }
        }
        //SERVIÇOS DE CONSULTA POR PARTE DO AGENTE NO SERVIDOR !
        if (servicoAgente == ETiposServicosAgentes.Consulta_Solucao) {

            if (objeto.getMSGServicoAgente().equals("Terminei_Envio")) {

                //System.out.println("Servidor irá encerrar sua comunicação com o Combinador ... ");

                objeto.setMSGServicoAgente("Terminei");
                return objeto;
            }
            //SERVIÇOS FORNECIDOS POR PARTE DO SERVIDOR !!!!
            //Aqui o tipo de Serviço de Servidor indica qual o serviço que agente deseja !!!
            if (objeto.getTipoServicoServidor().equals(ETiposServicosServidor.MelhorSolucao)) {

                //Vou retornar a melhor solução
                solucao_ = this.retorna_melhor_Solucao(this.getLista_solucoes());  //Passar memória adequada !!!
                objeto.setSolucao(solucao_);
                objeto.setMSGServicoAgente("SolucaoPronta");

                return objeto;
            } //Se for agente de Serviço B
            else if (objeto.getTipoServicoServidor().equals(ETiposServicosServidor.PiorSolucao)) {

                //Vou retornar a pior solução
                solucao_ = this.retorna_pior_Solucao(this.getLista_solucoes());       //Passar memória adequada !!!
                objeto.setSolucao(solucao_);
                objeto.setMSGServicoAgente("SolucaoPronta");

                return objeto;
            } //Se for agente de Serviço C
            else if (objeto.getTipoServicoServidor().equals(ETiposServicosServidor.Solucao_Aleatoria)) {
                //Vou retornar uma solucao aleatória
                solucao_ = this.retorna_aleatoria_Solucao(this.getLista_solucoes());   //Passar memória adequada !!!
                objeto.setSolucao(solucao_);
                objeto.setMSGServicoAgente("SolucaoPronta");

                return objeto;
            } else {/*Aqui ele deseja atualiza a memória aqui deve ser implementados as outras políticas de seleção*/

            }
        }

        return objeto;
    }

    private void escreveInformacao(ObjetoComunicacaoMelhorado objeto, ObjectOutputStream output) {

        try {
            output.writeObject(objeto);
            output.flush();
        } catch (IOException ex) {
           // ex.printStackTrace();
        }
    }

    private ObjetoComunicacaoMelhorado recebeInformacao(ObjectInputStream input) {

        ObjetoComunicacaoMelhorado objeto = new ObjetoComunicacaoMelhorado();

        try {
            objeto = (ObjetoComunicacaoMelhorado) input.readObject();
        } catch (ClassNotFoundException cex) {
            //cex.printStackTrace();
            return null;
        } catch (IOException ex) {
            //ex.printStackTrace();
            return null;
        }

        return objeto;
    }
    
    public LinkedList<BinPackTreeForest> getBinPackTreeForest(){
    
        return listForest;
    }
    public int getTamanho_memoria() {

        return tamanho_memoria;
    }

    public void setTamanho_memoria(int tamanho_memoria) {

        this.tamanho_memoria = tamanho_memoria;
    }

    public int getNum_solucoes() {

        return num_solucoes;
    }

    public void setNum_solucoes(int num_solucoes) {

        this.num_solucoes = num_solucoes;
    }

    public LinkedList<SolucaoHeuristica> getLista_solucoes() {

        return lista_solucoes;
    }

    public void setLista_solucoes(LinkedList<SolucaoHeuristica> lista_solucoes) {

        this.lista_solucoes = lista_solucoes; //Talvez this.lista_solucoes.addAll(lista_solucoes)
    }

    public SolucaoHeuristica getSolucaoMemoria(int indice) {

        try{
        return lista_solucoes.get(indice);
        }catch(Exception e){System.out.println("verifique solução memória pode está vazia");
        return null;}
    }

    public synchronized void adiciona_solucao(SolucaoHeuristica solucao) throws IOException {

        if (getNum_solucoes() + 1 <= getTamanho_memoria()) {

            
            
            contador = contador +1;
            
            long fim = System.currentTimeMillis();
            long diff = fim - ini;
            //ArquivoPrincipal_FAV.write("Solucao "+ getNum_solucoes()+" "+solucao.getFAV()+" "+ solucao.getFAV2() +" "+(diff/1000)+"\n");
            arquivoFAV1.write((diff / 1000) + " " + (solucao.getFAV() * 100) + "\n");
            arquivoFAV2.write((diff / 1000) + " " + (solucao.getFAV2() * 100) + "\n");
            arquivoMediaSobra.write((diff / 1000) + " " + solucao.getMediaSobras() + "\n");
            arquivoSomatorioSobra.write((diff / 1000) + " " + solucao.getSomatorioSobras() + "\n");
            arquivoNumBins.write(contador/*getNum_solucoes()*/ + " " + solucao.getQtd() + "\n");
            arquivoFinalCompleto.write((diff / 1000) + " " +(solucao.getFAV() * 100) +" " + (solucao.getFAV2() * 100) + " " + 
                                                                      solucao.getMediaSobras() +  " " + solucao.getSomatorioSobras() + " "+
                                                                      solucao.getQtd() + " " + solucao.getQtdLinhasCorte() + " "+
                                                                      solucao.getTipoHeuristic().toString()+" "+contador+
                                                                      "\n" );//(this.getNum_solucoes()+1)
            int con = 1;
            Iterator<Bin> iter = solucao.getObjetos().iterator();
            arqBinAvaliacao.write("Solucao "+solucao.getTipoHeuristic().toString()+"\n");
            while(iter.hasNext()){
                          
                arqBinAvaliacao.write(con+" "+iter.next().getAproveitamento()+"\n");
                
                con++;
            }
            //this.lista_solucoes.add(solucao);
            
            getLista_solucoes().add(solucao);
            setNum_solucoes(getNum_solucoes() + 1);
            Funcoes.ordenaSolucoesHeuristicasDecrescente(this.lista_solucoes);
            
            //Aqui será escrito o arquivo com as melhores, piores e soluções medianas
            if(getNum_solucoes() > 2){
            
                arquivoPrincipalSolucao.write((diff / 1000) + " " + this.getSolucaoMemoria(0).getFAV2() * 100 + "\n");
                arquivoPrincipalSolucao.write((diff / 1000) + " " + this.getSolucaoMemoria(Math.round(this.lista_solucoes.size() / 2)).getFAV2() * 100 + "\n");
                arquivoPrincipalSolucao.write((diff / 1000) + " " + this.getSolucaoMemoria(this.lista_solucoes.size() - 1).getFAV2()*100 + "\n");     
            
            }
            try{
            
               if((solucao.getTipoHeuristic() == ETipoHeuristicas.HHDHeuristic)||
                                                            (solucao.getTipoHeuristic() == ETipoHeuristicas.HHDHeuristic_Melhoria) ||
                                                            (solucao.getTipoHeuristic() == ETipoHeuristicas.HHDHeuristic_Melhoria_Tree)){
                
                   IGenericSolution solucaoConvertida = SolutionConversorFactory.getInstance().newConversor().converteParaSolucao(solucao/*, false*/);
                   listForest.add((BinPackTreeForest) solucaoConvertida); 
               }
               if((solucao.getTipoHeuristic() == ETipoHeuristicas.GRASP_2D_CONSTRUTIVO)||
                                                                  (solucao.getTipoHeuristic() == ETipoHeuristicas.Grasp2d_Melhoria)  ||
                                                                  (solucao.getTipoHeuristic() == ETipoHeuristicas.Grasp2dTree_Melhoria)){
               
                   IGenericSolution solucaoConvertida = SolutionConversorFactory.getInstance().newConversor().converteParaSolucao(solucao/*, true*/);
                   listForest.add((BinPackTreeForest) solucaoConvertida); 
               }
            }catch(Exception e){
                
                System.out.println("Pode dá erro, pois o Tipo Heuristica é nulo !!!");
            }
            //Adiciona na forma de árvore para imprimir no final !
         
            //setTamanho_memoria(getTamanho_memoria() - 1);          
        }else{
            
            //Terá que remover uma solução para inserir a nova solução
            int numSol = getNum_solucoes();

            if(solucao.getFAV2() > getLista_solucoes().get(numSol-1).getFAV2()){
            
                boolean boo = true;
                Iterator<SolucaoHeuristica> iterator = getLista_solucoes().iterator();
                while(iterator.hasNext()){
                    SolucaoHeuristica solution = iterator.next();
                    
                    if(solucao.getFAV().equals(solution.getFAV())){                    
                        if(solucao.getFAV2().equals(solution.getFAV2())){                        
                            if(solucao.getQtd() == solution.getQtd()){                                
                                if(solucao.getMediaSobras().equals(solution.getMediaSobras())){                                
                                    if(solucao.getQtdLinhasCorte() == solution.getQtdLinhasCorte()){                                    
                                        if(solucao.getSomatorioSobras().equals(solution.getSomatorioSobras())){
                                            boo = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }                    
                    }                    
                }
                if(boo == true){
                    //System.out.println("Chamei o agente destruidor antes -> " + getNum_solucoes());
                    agenteDestruidor();
                    //System.out.println("Chamei o agente destruidor depois -> " + getNum_solucoes());
                    adiciona_solucao(solucao);
                }
            }            
        }
    }

    public synchronized void remove_solucao(SolucaoHeuristica solucao){

        getLista_solucoes().remove(solucao);
        setNum_solucoes(getNum_solucoes() - 1);
        //setTamanho_memoria(getTamanho_memoria() + 1);
    }

    public synchronized void adiciona_conjuntoSolucoes(LinkedList<SolucaoHeuristica> list_solucoes) {

        if (getNum_solucoes() + list_solucoes.size() <= getTamanho_memoria()) {

            this.lista_solucoes = list_solucoes;  //Talvez seja melhor: this.lista_solucoes.addAll(list_solucoes);
            setNum_solucoes(list_solucoes.size()); // Talvez setNum_solucoes(getNum_solucoes()+ list_solucoes.size());
        }
        //setTamanho_memoria(getTamanho_memoria() - list_solucoes.size());
    }

    public FileWriter desenhaSolucao(FileWriter desenho, ISolution solucao) throws IOException{
        
        FileWriter solucao_desenhada = desenho;
        SolucaoHeuristica solucao_ = (SolucaoHeuristica) solucao;
        Iterator<Bin> placas = solucao_.getObjetos().iterator();
        IDimensao2d placa = solucao_.getTamanhoChapa();
        
        float larguraDaPlaca   = placa.retorneBase();
        float alturaDaPlaca    = placa.retorneAltura();
        int   contadorDePlacas = solucao_.getQtd();
        int zoom = (int)(300/alturaDaPlaca);
        int larguraDaImagem = (int)((1+(1+larguraDaPlaca)*contadorDePlacas) * zoom);
        int alturaDaImagem = (int)((2 + alturaDaPlaca) * zoom);
        
        solucao_desenhada.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n");
        solucao_desenhada.write("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"\n");
        solucao_desenhada.write("\t\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n");
        solucao_desenhada.write("<svg xmlns=\"http://www.w3.org/2000/svg\"\n");
        solucao_desenhada.write("\txmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\"\n");
        solucao_desenhada.write("\twidth=\""+larguraDaImagem+"px\" height=\""+alturaDaImagem+"px\">\n");
        solucao_desenhada.write("");
        
        int contaNovaPlaca = 0;
        
        while(placas.hasNext()){
            Bin bin_atual = placas.next();
            LinkedList pecas  = bin_atual.getListaPecas();
            LinkedList sobras = bin_atual.getListaSobras();
            LinkedList cortes = bin_atual.getListaCortes();       

            for (Object peca : pecas) {
                Peca pecaAtual = (Peca)peca;
                int larguraPeca = (int)(pecaAtual.retornaBase());
                int alturaPeca  = (int)(pecaAtual.retornaAltura());
                int x_peca = contaNovaPlaca + (int)(pecaAtual.getPontoInfEsq().getX());
                int y_peca = (int)(pecaAtual.getPontoInfEsq().getY());
                
                solucao_desenhada.write("\t<rect x=\""+x_peca*zoom+"\" y=\""+y_peca*zoom+"\" width=\""+larguraPeca*zoom+"\" height=\""+alturaPeca*zoom+"\" fill=\"#FFFFFF\" stroke=\"black\" stroke-width=\"1\"/>\n");		       
            }
            for (Object peca : pecas) {
                Peca pecaAtual = (Peca)peca;
                int larguraPeca = (int)(pecaAtual.retornaBase());
                int alturaPeca  = (int)(pecaAtual.retornaAltura());
                int x_peca = contaNovaPlaca + (int)(pecaAtual.getPontoInfEsq().getX());
                int y_peca = (int)(pecaAtual.getPontoInfEsq().getY());
            
                int x_texto = /*(contaNovaPlaca-1)*/ +(x_peca*zoom+((larguraPeca*zoom)/2));
                int y_texto = (y_peca*zoom+((alturaPeca*zoom)/2 + 4 ));
                int id_peca = pecaAtual.getPedidoAtendido().id();
                
                solucao_desenhada.write("\t<text x=\""+(x_texto)+"\" y=\""+y_texto+"\" font-size=\"12px\" text-anchor=\"middle\" >P"+id_peca+"</text>\n");				
            }
            
            for (Object sobra : sobras) {
                PedacoDisponivel sobraAtual = (PedacoDisponivel)sobra;
                int larguraSobra = (int)(sobraAtual.retorneBase());
                int alturaSobra = (int)(sobraAtual.retorneAltura());
                int x_sobra = contaNovaPlaca +(int)(sobraAtual.getPontoInferiorEsquerdo().getX());
                int y_sobra = (int)(sobraAtual.getPontoInferiorEsquerdo().getY());
                
                solucao_desenhada.write("\t<rect x=\""+x_sobra*zoom+"\" y=\""+y_sobra*zoom+"\" width=\""+larguraSobra*zoom+"\" height=\""+alturaSobra*zoom+"\" fill=\"#AAAAAA\" stroke=\"black\" stroke-width=\"1\"/>\n");
            }
            
            /*for (Object corte : cortes) {
                Corte corteAtual = (Corte)corte;
            }*/
        
          contaNovaPlaca = contaNovaPlaca +(int)larguraDaPlaca+1;
        }
        solucao_desenhada.write("</svg>\n");
        solucao_desenhada.close();
        return solucao_desenhada;
    
    }
    public FileWriter desenhaSolucao2(FileWriter desenho, ISolution solucao) throws IOException{
        
        FileWriter solucao_desenhada = desenho;
        SolucaoHeuristica solucao_ = (SolucaoHeuristica) solucao;
        Iterator<Bin> placas = solucao_.getObjetos().iterator();
        IDimensao2d placa = solucao_.getTamanhoChapa();
        
        float larguraDaPlaca   = placa.retorneBase();
        float alturaDaPlaca    = placa.retorneAltura();
        int   contadorDePlacas = solucao_.getQtd();
        int zoom = (int)(300/alturaDaPlaca);
        int larguraDaImagem = (int)((1+(1+larguraDaPlaca)*contadorDePlacas) * zoom);
        int alturaDaImagem = (int)((2 + alturaDaPlaca) * zoom);
        
        solucao_desenhada.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n");
        solucao_desenhada.write("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"\n");
        solucao_desenhada.write("\t\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n");
        solucao_desenhada.write("<svg xmlns=\"http://www.w3.org/2000/svg\"\n");
        solucao_desenhada.write("\txmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\"\n");
        solucao_desenhada.write("\twidth=\""+larguraDaImagem+"px\" height=\""+alturaDaImagem+"px\">\n");
        solucao_desenhada.write("");
        
        while(placas.hasNext()){
            Bin bin_atual = placas.next();
            LinkedList pecas  = bin_atual.getListaPecas();
            LinkedList sobras = bin_atual.getListaSobras();
            LinkedList cortes = bin_atual.getListaCortes();       

            for (Object peca : pecas) {
                Peca pecaAtual = (Peca)peca;
                int larguraPeca = (int)(pecaAtual.retornaBase());
                int alturaPeca  = (int)(pecaAtual.retornaAltura());
                int x_peca = (int)(pecaAtual.getPontoInfEsq().getX());
                int y_peca = (int)(pecaAtual.getPontoInfEsq().getY());
                
                solucao_desenhada.write("\t<rect x=\""+x_peca*zoom+"\" y=\""+y_peca*zoom+"\" width=\""+larguraPeca*zoom+"\" height=\""+alturaPeca*zoom+"\" fill=\"#FFFFFF\" stroke=\"black\" stroke-width=\"1\"/>\n");		       
                
                int x_texto = (x_peca*zoom+((larguraPeca*zoom)/2));
                int y_texto = (y_peca*zoom+((alturaPeca*zoom)/2 + 4 ));
                int id_peca = pecaAtual.getPedidoAtendido().id();
                
                solucao_desenhada.write("\t<text x=\""+(x_texto)+"\" y=\""+y_texto+"\" font-size=\"12px\" text-anchor=\"middle\" >P"+id_peca+"</text>\n");				
            }
            
            for (Object sobra : sobras) {
                PedacoDisponivel sobraAtual = (PedacoDisponivel)sobra;
                int larguraSobra = (int)(sobraAtual.retorneBase());
                int alturaSobra = (int)(sobraAtual.retorneAltura());
                int x_sobra = (int)(sobraAtual.getPontoInferiorEsquerdo().getX());
                int y_sobra = (int)(sobraAtual.getPontoInferiorEsquerdo().getY());
                
                solucao_desenhada.write("\t<rect x=\""+x_sobra*zoom+"\" y=\""+y_sobra*zoom+"\" width=\""+larguraSobra*zoom+"\" height=\""+alturaSobra*zoom+"\" fill=\"#AAAAAA\" stroke=\"black\" stroke-width=\"1\"/>\n");
            }
            
            /*for (Object corte : cortes) {
                Corte corteAtual = (Corte)corte;
            }*/
        
        
        }
        solucao_desenhada.close();
        return solucao_desenhada;
    
    }
    
    public void imprimeSolucoes() {

        Iterator<SolucaoHeuristica> it_sol = this.getLista_solucoes().iterator();
        SolucaoHeuristica solucao_;
        int cont = 1;
        while (it_sol.hasNext()) {

            solucao_ = it_sol.next();
            //SolucaoHeuristica.imprimeSolucao(solucao_, cont);
            solucao_.imprimeSolucao(solucao_, cont);
            cont++;
        }
    }

    //Esses métodos aqui adicionam uma lista de árvores, que será realizado os testes
    public void adicionaSolucaoArvoreBinaria(BinPackTreeForest florest){

        listForest.add(florest);
    }

    public void removeSolucaoArvoreBinaria(BinPackTreeForest florest){

        listForest.remove(florest);
    }

    /*########################################################################################
    MÉTODOS QUE MANIPULAM E RETORNAM SOLUÇÕES ADEQUADAS
    ########################################################################################*/
    public synchronized SolucaoHeuristica retorna_melhor_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {

        solucao.setSolucao(operacoesSolucoes.retornaMelhorSolucao(getLista_solucoes()));
        return solucao;
    }

    public synchronized SolucaoHeuristica retorna_pior_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {

        solucao = operacoesSolucoes.retornaPiorSolucao(getLista_solucoes());

        return solucao;
    }

    public synchronized SolucaoHeuristica retorna_aleatoria_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {

        solucao = operacoesSolucoes.retornaSolucaoAleatoria(getLista_solucoes());

        return solucao;
    }
    
    //Implementando aqui .... as novas políticas
    public synchronized SolucaoHeuristica retorna_linearMelhor_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {

        solucao = operacoesSolucoes.retorna_linearMelhor_Solucao(getLista_solucoes());

        return solucao;
    }
    public synchronized SolucaoHeuristica retorna_linearPior_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {

        solucao = operacoesSolucoes.retorna_linearPior_Solucao(getLista_solucoes());

        return solucao;
    }
    public synchronized SolucaoHeuristica retorna_Triangular_Solucao(LinkedList<SolucaoHeuristica> list_solucao) {

        solucao = operacoesSolucoes.retorna_Triangular_Solucao(getLista_solucoes());

        return solucao;
    }

    protected void agenteDestruidor() {

        if (this.getPoliticaDestruicao() == PoliticaDestruicao.Melhor) {

            this.remove_solucao(getSolucaoMemoria(0));
        }
        if (this.getPoliticaDestruicao() == PoliticaDestruicao.Pior) {

            this.remove_solucao(getSolucaoMemoria(getLista_solucoes().size() - 1));
        }
        if (this.getPoliticaDestruicao() == PoliticaDestruicao.Aleatorio) {

            SecureRandom r = new SecureRandom();
            int sorteado = r.nextInt(getLista_solucoes().size());
            this.remove_solucao(getSolucaoMemoria(sorteado));
        }

        if (this.getPoliticaDestruicao() == PoliticaDestruicao.ProbabilidadeLinearMelhor) {
            
            Funcoes f = new Funcoes();
            f.linearRand(false, this.getTamanho_memoria());
        }
        if (this.getPoliticaDestruicao() == PoliticaDestruicao.ProbabilidadeLinearPior) {
            
            Funcoes f = new Funcoes();
            f.linearRand(true, this.getTamanho_memoria());
        }
        if (this.getPoliticaDestruicao() == PoliticaDestruicao.TriangularProbabilidade) {
            
            Funcoes f = new Funcoes();
            f.triangularRand(this.getTamanho_memoria());
        }
    }

    private void abriArquivosGraficos() {

        try {
            arquivoFAV1 = new FileWriter(new File("ArquivoFAV1"));
            arquivoFAV2 = new FileWriter(new File("ArquivoFAV2"));
            arquivoMediaSobra = new FileWriter(new File("ArquivoMediaSobra"));
            arquivoSomatorioSobra = new FileWriter(new File("ArquivoSomatorioSobra"));
            arquivoNumBins = new FileWriter(new File("ArquivoNumBins"));
            arquivoFinalCompleto    = new FileWriter(new File("ArquivoFinalCompletoM1"));
            arquivoPrincipalSolucao = new FileWriter(new File("ArquivoHistSolucao"));
            arqBinAvaliacao         = new FileWriter(new File("ArquivoSolucaoAproveitamento"));
            arquivoMelhorSolucao    = new FileWriter(new File("arquivoMelhorSolucao"));
            desenho_Solucao = new FileWriter(new File("ArquivoSolucaoDesenhada"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void abriArquivosGraficosPrincipal() {

        long dife = System.currentTimeMillis();
        
        try {
        SolucaoHeuristica s = getSolucaoMemoria(0);
        float area = s.getObjetos().get(0).retorneDimensao().retorneArea();

       
            arquivoPrincipal_FAV = new FileWriter(new File("ArquivoPrincipal_FAV"));
            arquivoPrincipal_FAV2 = new FileWriter(new File("ArquivoPrincipal_FAV2"));
            arquivoPrincipal_MediaSobra = new FileWriter(new File("ArquivoPrincipal_MediaSobra"));
            arquivoPrincipal_SomatorioSobra = new FileWriter(new File("ArquivoPrincipal_SomatorioSobra"));
            arquivoPrincipal_NumBins = new FileWriter(new File("ArquivoPrincipal_NumeroBins"));
                        
        } catch (IOException ex ){            
            ex.printStackTrace();
        }
        catch (NullPointerException ex) {
            System.out.println("sem solução na memória ...");
        }

        try {

//          arquivoPrincipalSolucao.write((diff/ 1000) + " " + this.getSolucaoMemoria(0).getFAV2() + "\n");
//          arquivoPrincipalSolucao.write((diff/ 1000) + " " + this.getSolucaoMemoria(Math.round(this.lista_solucoes.size() / 2)).getFAV2() + "\n");
//          arquivoPrincipalSolucao.write((diff/ 1000) + " " + this.getSolucaoMemoria(this.lista_solucoes.size() - 1).getFAV2() + "\n");

            arquivoPrincipal_NumBins.write("set grid\n");
            arquivoPrincipal_NumBins.write("set title  " + "\"Gráfico Número de Bins\"\n");
            arquivoPrincipal_NumBins.write("set xlabel " + "\"Solucao\"\n");
            arquivoPrincipal_NumBins.write("set ylabel " + "\"Bins\"\n");
            //arquivoPrincipal_FAV.write("set ytics 10.0\n");
            arquivoPrincipal_NumBins.write("set xrange [1.00:" + this.tamanho_memoria + "]\n");
            arquivoPrincipal_NumBins.write("set yrange [0.00:100.00]\n");//Tem que ser limite inferior
            arquivoPrincipal_NumBins.write("plot " + "\"ArquivoNumBins\"\n");

            //Arquivo GNUPlot FAV
            arquivoPrincipal_FAV.write("set grid\n");
            arquivoPrincipal_FAV.write("set title  " + "\"Gráfico Função de Avaliação FAV Simples\"\n");
            arquivoPrincipal_FAV.write("set xlabel " + "\"Tempo\"\n");
            arquivoPrincipal_FAV.write("set ylabel " + "\"Inverso da qualidade FAV\"\n");
            //arquivoPrincipal_FAV.write("set ytics 10.0\n");
            arquivoPrincipal_FAV.write("set xrange [0.00:" + (dife - ini) / 1000 + ".00]\n");
            arquivoPrincipal_FAV.write("set yrange [0.00:100.00]\n");
            arquivoPrincipal_FAV.write("plot " + "\"ArquivoFAV1\"\n");

            //arquivoFAV1,arquivoFAV2,arquivoMediaSobra,arquivoSomatorioSobra
            //Abrindo Arquivo GNUPlot
            arquivoPrincipal_FAV2.write("reset\n");
            arquivoPrincipal_FAV2.write("set grid\n");
            arquivoPrincipal_FAV2.write("set title  " + "\"Gráfico Função de Avaliação FAV2 Melhorada\"\n");
            arquivoPrincipal_FAV2.write("set xlabel " + "\"Tempo\"\n");
            arquivoPrincipal_FAV2.write("set ylabel " + "\"Inverso da qualidade FAV2\"\n");
            //arquivoPrincipal_FAV2.write("set ytics 10.0\n");
            arquivoPrincipal_FAV2.write("set xrange [0.00:" + (dife - ini) / 1000 + ".00]\n");
            arquivoPrincipal_FAV2.write("set yrange [0.00:100.00]\n");
            arquivoPrincipal_FAV2.write("plot " + "\"ArquivoFAV2\"\n");

            //arquivoFAV1,arquivoFAV2,arquivoMediaSobra,arquivoSomatorioSobra
            //Abrindo Arquivo GNUPlot
            arquivoPrincipal_MediaSobra.write("reset\n");
            arquivoPrincipal_MediaSobra.write("set grid\n");
            arquivoPrincipal_MediaSobra.write("set title  " + "\"Gráfico Função de Avaliação Media Sobra\"\n");
            arquivoPrincipal_MediaSobra.write("set xlabel " + "\"Tempo\"\n");
            arquivoPrincipal_MediaSobra.write("set ylabel " + "\"Inverso da qualidade Média Sobra\"\n");
            //arquivoPrincipal_MediaSobra.write("set ytics 100.0\n");
            arquivoPrincipal_MediaSobra.write("set xrange [0.00:" + (dife - ini) / 1000 + ".00]\n");
            arquivoPrincipal_MediaSobra.write("set yrange [0.00:1000.00]\n");
            arquivoPrincipal_MediaSobra.write("plot " + "\"ArquivoMediaSobra\"\n");

            //arquivoFAV1,arquivoFAV2,arquivoMediaSobra,arquivoSomatorioSobra
            //Abrindo Arquivo GNUPlot
            arquivoPrincipal_SomatorioSobra.write("reset\n");
            arquivoPrincipal_SomatorioSobra.write("set grid\n");
            arquivoPrincipal_SomatorioSobra.write("set title  " + "\"Gráfico Função de Avaliação Somatorio Sobras\"\n");
            arquivoPrincipal_SomatorioSobra.write("set xlabel " + "\"Tempo\"\n");
            arquivoPrincipal_SomatorioSobra.write("set ylabel " + "\"Inverso da qualidade\"\n");
            //arquivoPrincipal_SomatorioSobra.write("set ytics 50.0\n");
            arquivoPrincipal_SomatorioSobra.write("set xrange [0.00:" + (dife - ini) / 1000 + ".00]\n");
            //variável área com problema
            //arquivoPrincipal_SomatorioSobra.write("set yrange [0.00:" + area + "]\n");
            arquivoPrincipal_SomatorioSobra.write("plot " + "\"ArquivoSomatorioSobra\"\n");

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            //System.out.println(":D");
        }
    }
    
    private PoliticaDestruicao getPoliticaDestruicao() {

        return this.politicaDestruicao;
    }

    public static void main(String args[]) throws Exception {

        /*Criamos uma instância do ServidorFinal e o iniciamos. Depois da chamada do método start(), a execução da thread
        principal continua. Deste ponto em diante, temos duas threads em execução: a principal e a auxiliar do servidor!
         */
    	long tempoAtual;
        long tempoInicial = System.currentTimeMillis();
        
        if (args.length < 5) {

            System.out.println("Está faltando argumentos !");
            System.out.println("Deve serguir o seguinte padrão: (NOME, PORTA,TAMANHO_MEMÓRIA, POLÍTICA_DESTRUIÇÃO, TEMPO DE EXCUÇÃO)");
            System.exit(1);
        }
        String name = args[0];
        int portaComunicacao = Integer.parseInt(args[1]);
        int tamanho_memoria = Integer.parseInt(args[2]);
        long tempofinal = Long.parseLong(args[4]);
        
        PoliticaDestruicao polDestruicao = null;

        switch (Integer.parseInt(args[3])) {
            case 1:
                polDestruicao = PoliticaDestruicao.Aleatorio;
                break;
            case 2:
                polDestruicao = PoliticaDestruicao.Melhor;
                break;
            case 3:
                polDestruicao = PoliticaDestruicao.MelhorAleatorio;
                break;
            case 4:
                polDestruicao = PoliticaDestruicao.MelhorMelhor;
                break;
            case 5:
                polDestruicao = PoliticaDestruicao.MelhorPior;
                break;
            case 6:
                polDestruicao = PoliticaDestruicao.Pior;
                break;
            case 7:
                polDestruicao = PoliticaDestruicao.PiorAleatorio;
                break;
            case 8:
                polDestruicao = PoliticaDestruicao.ProbabilidadeLinearMelhor;
                break;
            case 9:
                polDestruicao = PoliticaDestruicao.ProbabilidadeLinearPior;
                break;
            case 10:
                polDestruicao = PoliticaDestruicao.TriangularProbabilidade;
                break;
            default:
                System.out.println("Opção Inválida para a política de destruição");
                System.exit(0);
        }

        ServidorFinal servidor = new ServidorFinal(name, portaComunicacao, tamanho_memoria, polDestruicao);
        servidor.start();

        /*Bloquearemos a thread principal até que o usuário pressione ENTER. Assim evitamos que a aplicação termine
        e seja encerrada e deixamos o Servidor em execução !!!*/
        System.out.println("Pressione ENTER para para encerrar o servidor !");

        tempoAtual = System.currentTimeMillis();
        
        while((tempoAtual - tempoInicial) < tempofinal) {
        	tempoAtual = System.currentTimeMillis();
        }
        //new Scanner(System.in).nextLine();

        /*Quando o usuário pressionar ENTER, a thread principal é desbloqueada e logo em seguida finalizamos o servidor,
        de forma normal !!!*/
        
        
        if( (tempoAtual - tempoInicial) >= tempofinal) {
        	servidor.stop();
        	System.out.println("Quantidade de soluções na memória: " + servidor.getNum_solucoes());
        }
        try{
        //Fecha os arquivos com informações de LOG
        arquivoPrincipal_FAV.close();
        arquivoPrincipal_FAV2.close();
        arquivoPrincipal_MediaSobra.close();
        arquivoPrincipal_SomatorioSobra.close();
        arquivoPrincipal_NumBins.close();
        arquivoPrincipalSolucao.close();

        arquivoFAV1.close();
        arquivoFAV2.close();
        arquivoMediaSobra.close();
        arquivoSomatorioSobra.close();
        arquivoNumBins.close();
        arquivoFinalCompleto.close();
        arqBinAvaliacao.close();
        
        
        servidor.imprimeSolucoes();
        SolucaoHeuristica first = servidor.getLista_solucoes().getFirst();
        
        arquivoMelhorSolucao.write((first.getFAV() * 100) +" " + (first.getFAV2() * 100) + " " + 
                                                                      first.getMediaSobras() +  " " + first.getSomatorioSobras() + " "+
                                                                      first.getQtd() + " " + first.getQtdLinhasCorte() + " "+
                                                                      first.getTipoHeuristic().toString()+" "+"\n" );
        
        arquivoMelhorSolucao.close();
        
        servidor.desenhaSolucao(desenho_Solucao, first);
        desenho_Solucao.close();
        
        /*Iterator a = first.getObjetos().iterator();
        while(a.hasNext()){
        
            Bin b = (Bin) a.next();
            Iterator iter_b = b.getListaCortes().iterator();
            Iterator iter_peca = b.getListaPecas().iterator();
            
                    
        //arquivoMelhorSolucao.close();
        }*/
        }
        catch (NullPointerException e){
            //System.out.println("pronto ....");
        }
        /*while(a.hasNext()){
        
            Bin b = (Bin) a.next();
            Iterator iter_b = b.getListaCortes().iterator();
            while(iter_b.hasNext()){
            
                Corte ct = (Corte) iter_b.next();
                System.out.println("\nCorte Id - "+ ct.getId()+"\tPonto de corte - ("+ct.getPontoChapaCortada().getX()+" , "
                                    +ct.getPontoChapaCortada().getY()+") "
                                    + " Tamanho - "+ct.getTamanho()+ "  Posicao corte - "+ct.getPosicaoCorte()+"  "
                                    +"É vertical - "+ct.eVertical());
            }
        }*/
        /*System.out.println("Aqui irei imprimir minhas soluções !!!");
        System.out.println("\n\n########### Imprimindo na forma de árvore ###############\n\n");
        Iterator<BinPackTreeForest> iterator = servidor.getBinPackTreeForest().iterator();
        int c = 1;
        while(iterator.hasNext()){
        System.out.println("\n\nSolução - Floresta --> "+c);
        BinPackTreeForest bbptf = iterator.next();
        ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> solucao = HHDHeuristic.VerificaImprimeArvoreDeCorte((BinPackTreeForest) bbptf);
        bbptf.calculaFAVFAV2(bbptf, solucao);
        System.out.println("FAV  Tree -> "+bbptf.getFAV());
        System.out.println("FAV2 Tree -> "+bbptf.getFAV2());
        System.out.println("Qtd  Tree -> "+bbptf.getQtd());
        System.out.println("Qtd Linhas corte -> " +bbptf.getQtdLinhasCorte());
        System.out.println("Somatorio Sobras -> "+bbptf.getSomatorioSobras());
        System.out.println("Media das Sobras -> "+bbptf.getMediaSobras());
        Utilidades.Funcoes.imprimeBinPackTree(solucao);
        c++;
        }*/
         
    }   
    
}