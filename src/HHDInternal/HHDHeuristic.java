package HHDInternal;

import HHDComparadores.*;
import java.util.Queue;
import java.net.Socket;
import HHDInterfaces.IBin;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import HHDInterfaces.ISobra;
import ATeam.IniciaClientes;
import Heuristicas.Individuo;
import HHDInterfaces.IPedido;
import java.util.Collections;
import java.util.ListIterator;
import ATeam.ETipoHeuristicas;
import java.net.SocketException;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPecaPronta;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import HHDBinPackingTree.IBPTNode;
import Utilidades.PoliticaSelecao;
import HHDInterfaces.IListaPedidos;
import HHDBinPackingTree.BPTPedaco;
import HHDBinPackingTree.BinPackTree;
import java.io.FileNotFoundException;
import HHDBinPackingTree.BinPackCorte;
import HHDInterfaces.IGenericSolution;
import HHDInterfaces.ISolutionProvider;
import HHDInterfaces.ISolutionInterface;
import HeuristicaConstrutivaInicial.Bin;
import Heuristicas.EstrategiaBinPackTree;
import HHD_Exception.PecaInvalidaException;
import HHDBinPackingTree.BinPackTreeForest;
import SimulaGenetico.ETiposServicosAgentes;
import HHDBinPackingTree.BinPackTreeConversor;
import ComunicaoConcorrenteParalela.ServicoAgente;
import HHDBinPackingTree.IBPTNodeITestePecaWrapper;
import SimulaGenetico.OperacoesSolucoes_Individuos;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HHDHeuristic implements ISolutionProvider, Runnable {

    public static final int MAX_MENOR_PEDACO = 0;
    public static final int MAX_MAIOR_AREA = 1;
    public static final int MAX_MENOR_DIMENSAO = 2;
    private int estrategia;
    private boolean permiteRotacao;
    Double Sobra = 0.0;
    Double FAV = 0.0;    //Função de avaliação1
    Double FAVM = 0.0;   //Função de avaliação2
    Double FAV2 = 0.0;
    Double MenorAp = 0.0;
    ISolutionInterface resultado;
    
    /*****************************************CRIADO SÓ PARA REALIZAÇÃO DE TESTES*****************************************/
    private Socket socketClientFinal;
    private ObjectInputStream in;   //Entrada para leitura do socket
    private ObjectOutputStream out; //Saída   para escrita no socket
    private boolean executando;
    private Thread thread;
    private String name;
    private ServicoAgente tipo_agente;
    private ETiposServicosAgentes operacao_agente;    //Tipo de operação realizada pelo agente
    private ETiposServicosServidor operacao_servidor; //Tipo de operação requerida da memória pelo agente 
    private ETipoHeuristicas heuristica;
    private EstrategiaBinPackTree estrategiaTree;
    private int porta_comunicacao;
    ObjetoComunicacaoMelhorado objeto;//= new ObjetoComunicacaoMelhorado();
    PoliticaSelecao polSelecaoSolucao = null;
    PoliticaSelecao polSelecaoPlacas = null;
    private int hhdtype;
    private float aprov;
    private float aproveitamentoNivel;

    /**********************************************************************************************************************/
    /*ATRIBUTOS DE CLIENTE AGENTE*/
    public HHDHeuristic() {

        permiteRotacao = true;
        estrategia = MAX_MENOR_PEDACO;

        //Novos atributos para Agente/Cliente se conectar
        executando = false;
        objeto = new ObjetoComunicacaoMelhorado();
    }

    public HHDHeuristic(String name, String endereco, int portacomunicacao, ETiposServicosAgentes eSAgn,
                                                                        ETiposServicosServidor eSServ) throws Exception {
        //permiteRotacao = false;
        permiteRotacao = true;
        estrategia = MAX_MENOR_PEDACO;

        //Novos atributos para Agente/Cliente se conectar
        this.name = name;
        this.porta_comunicacao = portacomunicacao;
        this.operacao_servidor = eSServ;
        this.operacao_agente = eSAgn;

        executando = false;
        objeto = new ObjetoComunicacaoMelhorado();

        open(endereco, portacomunicacao);
    }

    /********************************** MÉTODOS ÚTEIS PARA CRIAÇÃO DA CONEXÃO COM O SERVIDOR *************************************/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////                                                                                                    ////
    ////  Aqui implementa os métodos de simulação Cliente (open,start, close, send, receive e run !!!     ////
    ////                                                                                                    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////      
    private void open(String endereco, int portacomunicacao) throws Exception {

        try {
            //Método open que estabelece conexão com o Servidor e obtem o InputStream e OutputStream da conexão;
            socketClientFinal = new Socket(endereco, portacomunicacao);

            //in  = new BufferedReader(new InputStreamReader(socketClientFinal.getInputStream()));
            //out = new PrintStream(socketClientFinal.getOutputStream());
            out = new ObjectOutputStream(socketClientFinal.getOutputStream());
            in = new ObjectInputStream(socketClientFinal.getInputStream());

        }catch(SocketException se){
            System.out.println("Exceção conexão recusada");
            System.out.println(se);
            close();
        }
        catch (IOException e) {
            System.out.println("Exceção IO no OPEN do HHDHEURISTIC");
            System.out.println(e);
            close();
        }
    }

    public void close() {//Libera os recursos alocados

        //Método close() para liberar recursos alocados de forma adequada.
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (socketClientFinal != null) {
            try {
                socketClientFinal.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        in = null;
        out = null;
        socketClientFinal = null;

    }

    public void start_run() {//Inicia a thread auxiliar, se possível. O objeto cliente precisa estar inicializado e não pode está executando !

        if (executando) {

            return;
        }

        executando = true;
        thread = new Thread(this);
        thread.run();

    }
    
    public void start() {//Inicia a thread auxiliar, se possível. O objeto cliente precisa estar inicializado e não pode está executando !

        if (executando) {

            return;
        }

        executando = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() throws Exception{ //Para a thread auxiliar de forma adequada, encerrando todos os recursos alocados.

        executando = false;

        if(thread != null) {

            thread.join();
        }
    }

    public boolean isExecutando(){

        return executando;
    }

    //Os dois métodos seguintes define a escrita e a leitura de operações nas memórias por parte dos Agentes !!!
    public void deveEnviar(ObjetoComunicacaoMelhorado obj){

        this.objeto.setSolucao(obj.getSolucao());
        this.objeto.setMSGServicoAgente(obj.getMSGServicoAgente());
        this.objeto.setTipoServicoAgente(obj.getTipoServicoAgente2());
        this.objeto.setTipoServicoServidor(obj.getTipoServicoServidor());
    }

    public ObjetoComunicacaoMelhorado getDeveEnviar(){

        return this.objeto;
    }

    public void send(ObjetoComunicacaoMelhorado obcom){

        try {
            //out.println(obcom.getMSGServicoAgente());
            out.writeObject(obcom);
            out.flush();
        } catch (IOException ex) {

            System.out.println("Erro na Entrada-Saida do Objeto enviado pelo cliente");
            System.out.println(ex);
            close();
        }
    }

    public ObjetoComunicacaoMelhorado receive(){

        ObjetoComunicacaoMelhorado objetoComunicacao = null;

        try {
            objetoComunicacao = (ObjetoComunicacaoMelhorado) in.readObject();
        } catch (IOException ex) {
            System.out.println("Erro de Entrada e saída no receive do CLiente !!!");
            System.out.println(ex);
        } catch (ClassNotFoundException exClass) {
            System.out.println("Erro no receive do CLiente problema de conversão de classe !!!");
            System.out.println(exClass);
        }

        return objetoComunicacao;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getPortaComunicacao() {

        return porta_comunicacao;
    }

    public void setPortaComunicacao(int porta) {

        this.porta_comunicacao = porta;
    }

    public ServicoAgente getServicoAgente() {

        return tipo_agente;
    }

    public void setServicoAgente(ServicoAgente servico) {

        this.tipo_agente = servico;
    }

    public ETiposServicosAgentes getTipoServico() {

        return operacao_agente;
    }

    public void setTipoServico(ETiposServicosAgentes etipoServico) {

        this.operacao_agente = etipoServico;
    }

    public ETiposServicosServidor getTipoServicoServidor() {

        return operacao_servidor;
    }

    public void setTipoServicoServidor(ETiposServicosServidor etipoServicoServidor) {

        this.operacao_servidor = etipoServicoServidor;
    }

    public ETipoHeuristicas getTipoHeuristica() {

        return this.heuristica;
    }

    public void setTipoHeuristica(ETipoHeuristicas etipoheuristica) {

        this.heuristica = etipoheuristica;
    }    
    
    private int getHHDType() {
        
        return this.hhdtype;
    }
    
    private void setHHDType(int type) {
        
        this.hhdtype = type;
    }

    private float getAprovt() {
      
        return this.aprov;
    }
    
    private void setAprovt(float aproveitamentoPlaca) {
        
        this.aprov = aproveitamentoPlaca;
    }
    private float getAprovNivel() {
        
        return aproveitamentoNivel;
    }
    
    private void setAprovNivel(float aproveitamentoNivel) {
        
        this.aproveitamentoNivel = aproveitamentoNivel;
    }
    
    public EstrategiaBinPackTree getEstrategiaTree() {
        
        return this.estrategiaTree;
    }
    
    public void setEstrategiaTree(EstrategiaBinPackTree estrategiaBinPackTree) {
        
        this.estrategiaTree = estrategiaBinPackTree;
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

    public PoliticaSelecao getPoliticaSelecaoSolucao() {

        return this.polSelecaoSolucao;
    }

    public void setPoliticaSelecaoSolucao(PoliticaSelecao polSolucao) {

        this.polSelecaoSolucao = polSolucao;
    }

    public PoliticaSelecao getPoliticaSelecaoPlacas() {

        return polSelecaoPlacas;
    }

    public void setPoliticaSelecaoPlacas(PoliticaSelecao polPlacas) {

        this.polSelecaoPlacas = polPlacas;
    }

    /**********************************************************************************************************************/
    /********************************** MÉTODOS ÚTEIS PARA EXECUÇÃO DO HHDHEURISTIC ***************************************/
    public SolucaoHeuristica criaSolucao(LinkedList<Pedidos> pedido, IDimensao2d tamanhoChapa) {

        /*ISolutionInterface solucao;
        solucao = obtenhaSolucao(tamanhoChapa, pedido);*/

        SolucaoHeuristica solucao;
        solucao = obtenhaSolucao(tamanhoChapa, pedido);

        return solucao;
    }

    @Override
    public ISolutionInterface criaSolucao(IListaPedidos pedido, IDimensao2d tamanhoChapa) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    //private ISolutionInterface obtenhaSolucao(IDimensao2d tamanhoChapa, IListaPedidos pedido){
    private SolucaoHeuristica obtenhaSolucao(IDimensao2d tamanhoChapa, LinkedList<Pedidos> pedido) {

        LinkedList<Pedidos> listaPedidos = pedido;

        Collections.sort(listaPedidos, new ComparadorAreas());
        Collections.reverse(listaPedidos);

        Iterator<Pedidos> iterator = listaPedidos.iterator();

        //System.out.println("\n\nPedidos Ordenados por Área\n");
//foi comentado para execucao
//        while (iterator.hasNext()) {
//
//            Pedidos pedidos = iterator.next();
//
//            System.out.println("Id -- " + pedidos.id() + "\t " + pedidos.retorneDimensao().retorneBase()
//                    + "  x  " + pedidos.retorneDimensao().retorneAltura());
//        }

        //System.out.println("");
        SolucaoHeuristica resultadoHeuristica = new SolucaoHeuristica(pedido, tamanhoChapa);

        int contador = 0, numSobras = 0;
        float mediasobra = 0;
        int a = 0, j;
        Bin aux;
        Individuo indCompleto = new Individuo();
        ArrayList<Integer> arrayInt = new ArrayList<Integer>();

        while (!listaPedidos.isEmpty()) {

//            System.out.println("\nContador de Bins --> " + (++contador));

            resultadoHeuristica.adicionarPlanoDeCorte(aux = corteChapa(new PedacoDisponivel(new Ponto(0, 0), tamanhoChapa, 1), listaPedidos, tamanhoChapa));

            float sobr = tamanhoChapa.retorneArea() - aux.getFav().floatValue();
            mediasobra = mediasobra + sobr;

//            System.out.println("\n ############## Informacao da placa ##############");
//            System.out.println("Tamanho da area da placa --> [ " + tamanhoChapa.retorneArea() + " ]");
//            System.out.println("Somatorio dos itens da placa -->  [ " + aux.getFav() + " ]");
//            System.out.println("Somatório das Sobras da placa --> [ " + sobr + " ]");
//            System.out.println("Número de Sobras --> [ " + aux.getListaSobras().size() + " ]");
//            System.out.println("Média das sobras --> [ " + sobr / aux.getListaSobras().size() + " ]");
//            System.out.println("Numero de cortes --> [ " + aux.getListaCortes().size() + " ]");

            FAV = FAV + aux.getFav();
            Double sobra = 100 - (aux.getFav() / (tamanhoChapa.retorneArea()) * 100);
//            System.out.println("Sobra calculada --> [" + sobra + " ]");

            if (sobra > MenorAp) {
                MenorAp = sobra;
            }

            numSobras = numSobras + aux.getListaSobras().size();
            aux.setAproveitamento(sobra);
            aux.setSobra(new Double(sobr));

            a++;

//            System.out.println("\nA Bin  " + contador + " foi completada !");

            //arrayInt.addAll(aux.getListaItensIndiv());
            indCompleto.setListaItens2(aux.getListaItensIndiv());

        }

        //Aqui deve-se calcular o FAV e o FAV2 da solução gerada pela Heurística

        float w, h;
        w = tamanhoChapa.retorneBase();
        h = tamanhoChapa.retorneAltura();
        /* 
        do{
        Bin placa = resultadoHeuristica.retornePlanoDeCorte(a);
        
        FAV = FAV + placa.getFav();
        Double sobra = 100-(placa.getFav()/(w*h)*100);
        
        placa.setSobra(Sobra);
        placa.setSobra(sobra);
        
        if (sobra > MenorAp){
        MenorAp = sobra;                	                	
        }
        a++;
        }while(a < resultadoHeuristica.getQtd());*/



        j = resultadoHeuristica.getQtd();

        FAV = FAV / (j * w * h);
        FAV2 = ((FAV * 100) + MenorAp) / j;
//        System.out.println("\nMenorAproveitamento --> [" + MenorAp + " ]");
//        System.out.println("FAV  --> [ " + FAV * 100 + " ]");
//        System.out.println("FAV2 --> [ " + FAV2 + " ]");
//        System.out.println("Média das sobras TOTAL --> [ " + mediasobra / numSobras + " ]");
        resultadoHeuristica.setFAV(FAV);
        resultadoHeuristica.setFAV2(FAV2);

        IGenericSolution solucaoConvertida;
        solucaoConvertida = SolutionConversorFactory.getInstance().newConversor().converteParaSolucao(resultadoHeuristica/*, false*/);

        ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> solucao = VerificaImprimeArvoreDeCorte((BinPackTreeForest) solucaoConvertida);

        Utilidades.Funcoes.imprimeBinPackTree(solucao);

        //Será comentado para realização de testes
	 /*ISolutionInterface*/ //resultado = (ISolutionInterface) new Solucao(solucaoConvertida);
	 /*return resultado;*/

        /*Aqui será realizado um teste simples, onde verifica se foi gerado um conjunto de árvores*/
        resultadoHeuristica.calculaLinhasDeCorteEMediaSobras();

        return resultadoHeuristica;
    }

    private Bin corteChapa(PedacoDisponivel pedaco, LinkedList<Pedidos> listaPedidos, IDimensao2d tamanhoChapa) {

        //System.out.println("Pedaço Disponível ---> "+pedaco.retorneBase()+" x "+pedaco.retorneAltura());

//        System.out.println("\n############## Informação Pedaço Disponível ################### \n");
//        System.out.println("\nAltura --> " + pedaco.retorneAltura());
//        System.out.println("Base --> " + pedaco.retorneBase());
//        System.out.println("Ponto Inferior Esquerdo --> ( " + pedaco.getPontoInferiorEsquerdo().getX() + ", "
//                + pedaco.getPontoInferiorEsquerdo().getY() + ")");
//
//        System.out.println("Ponto Superior Direito --> ( " + pedaco.getPontoSuperiorDireito().getX() + ", "
//                + pedaco.getPontoSuperiorDireito().getY() + ")");

        Bin planoDeCorte = new Bin(); //Cria novo plano de corte

        ListIterator iteradorPedidos = listaPedidos.listIterator();

        IPedido pedidoAtual = maiorPedidoQueCabe(pedaco, iteradorPedidos); //Sempre pegará o maior pedido que cabe na menor sobra ou não

//        System.out.println("Informações do Maior Pedido que cabe");

        if (pedidoAtual == null) {
            System.out.println("Return igual a NULL será adicionado as sobras !");
        }

        if (pedidoAtual != null) {

//            System.out.println("Pedido selecionado ---> (W x H) --> (" + pedidoAtual.retorneDimensao().retorneBase() + " x "
//                    + pedidoAtual.retorneDimensao().retorneAltura() + " )");
//
//            System.out.println("\nQuantidade Antes --> " + pedidoAtual.quantidade());

            pedidoAtual.atendaUmPedido();

//            System.out.println("Quantidade Depois --> " + (pedidoAtual.quantidade() - pedidoAtual.getPedidosAtendidos()));

            if (pedidoAtual.quantidade() == pedidoAtual.getPedidosAtendidos()) {

                iteradorPedidos.remove();
            }

            LinkedList listaDeCortes = new LinkedList(),
                    listaDePedacosRestantes = new LinkedList();

            Peca pecaCortada = this.efetueMelhorCorte(pedidoAtual, pedaco, listaDeCortes, listaDePedacosRestantes);

//            System.out.println("\nLista de Cortes --> " + listaDeCortes.toArray().length);
//            System.out.println("Lista de Pedaços Restantes --> " + listaDePedacosRestantes.toArray().length);

            planoDeCorte.listaCortes.addAll(listaDeCortes);
            planoDeCorte.adicionePeca(pecaCortada);

            //Incrementar o calculo do Fav e a lista de Individuos
            planoDeCorte.setFav(planoDeCorte.getFav() + pedidoAtual.retorneDimensao().retorneArea());

            //planoDeCorte.getIndividuo().adicionaItemLista(pedidoAtual.id(), pedidoAtual.retorneDimensao().retorneArea());
            planoDeCorte.individuo.adicionaItemLista(pedidoAtual.id(), pedidoAtual.retorneDimensao().retorneArea());

            /*
            float w = tamanhoChapa.retorneBase(); 
            float h = tamanhoChapa.retorneAltura();
            
            //Aqui calcula-se a sobra da placa e grava em cada plava
            Double sobra = 100-(planoDeCorte.getFav()/(w*h)*100);
            
            if (sobra > MenorAp){
            MenorAp = sobra;                	                	
            }
            
            planoDeCorte.setSobra(sobra);*/

            if (listaDePedacosRestantes.size() == 2) {

                PedacoDisponivel menorPedaco = (PedacoDisponivel) Collections.min(listaDePedacosRestantes, new ComparadorISobra());
                PedacoDisponivel maiorPedaco = (PedacoDisponivel) Collections.max(listaDePedacosRestantes, new ComparadorISobra());

                planoDeCorte.integrar(this.corteChapa(menorPedaco, listaPedidos, tamanhoChapa));
                planoDeCorte.integrar(this.corteChapa(maiorPedaco, listaPedidos, tamanhoChapa));
            } else if (listaDePedacosRestantes.size() == 1) {

                PedacoDisponivel pedD = (PedacoDisponivel) listaDePedacosRestantes.getFirst();
                Ponto infE = pedD.getPontoInferiorEsquerdo();
                Ponto supD = pedD.getPontoSuperiorDireito();

                Corte corte = (Corte) listaDeCortes.getFirst();
                Ponto pntChapa = corte.getPontoChapaCortada();

//                System.out.println("\n### Informação do Corte ###\n");
//                System.out.println("Ponto de Corte --> ( " + pntChapa.getX() + ", " + pntChapa.getY() + " )");
//                System.out.println("Corte vertical -> " + corte.eVertical());

                planoDeCorte.integrar(this.corteChapa((PedacoDisponivel) listaDePedacosRestantes.getFirst(), listaPedidos, tamanhoChapa));
            }
        } else {
            planoDeCorte.adicionarSobra(pedaco);
        }

        return planoDeCorte;
    }

    private Peca efetueMelhorCorte(IPedido pedido, PedacoDisponivel pedaco, LinkedList listaDeCortes, 
                                                                                                LinkedList listaDePedacosRestantes) {

        //Implementar corte vertical incrementando as outras estratégias !!!
        boolean primeiroCorteVertical = corteVerticalEMelhor(pedido, pedaco);

        Corte corte1 = new Corte();
        Corte corte2 = new Corte();

        float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;

        if (primeiroCorteVertical) {

//            System.out.println("\nCorte vertical é melhor");

            if((pedido.retorneDimensao().retorneBase() > pedaco.retorneBase()) ||
                                                         (pedido.retorneDimensao().retorneAltura()>pedaco.retorneAltura())){
            
                pos1 = pedido.retorneDimensao().retorneAltura();
                tamanho1 = pedaco.retorneAltura();
                pos2 = pedido.retorneDimensao().retorneBase();
                tamanho2 = pedido.retorneDimensao().retorneAltura();
            }else{
                
                pos1 = pedido.retorneDimensao().retorneBase();
                tamanho1 = pedaco.retorneAltura();
                pos2 = pedido.retorneDimensao().retorneAltura();
                tamanho2 = pedido.retorneDimensao().retorneBase();
            }
//                pos1 = pedido.retorneDimensao().retorneBase();  //Mudei para fazer um teste !!!
//                tamanho1 = pedaco.retorneAltura();
//                pos2 = pedido.retorneDimensao().retorneAltura();
//                tamanho2 = pedido.retorneDimensao().retorneBase();
        } else {

            if((pedido.retorneDimensao().retorneBase() == pedaco.retorneDimensao().retorneBase())&&
                    (pedido.retorneDimensao().retorneAltura() == pedaco.retorneDimensao().retorneAltura())){
            
//                System.out.println("Peça e sobra com dimensões iguais ... ");
                Peca pecaCortada = null;

                try {
                    pecaCortada = new Peca(pedaco, pedido, permiteRotacao, false);
                } catch (PecaInvalidaException e) {  }
                
                return pecaCortada;
                
            }else if((pedido.retorneDimensao().retorneBase() == pedaco.retorneDimensao().retorneAltura())&&
                    (pedido.retorneDimensao().retorneAltura() == pedaco.retorneDimensao().retorneBase())){
            
//                System.out.println("Peça e sobra com dimensões iguais ... ");
               
                Peca pecaCortada = null;

                try {
                    pecaCortada = new Peca(pedaco, pedido, permiteRotacao, false);
                } catch (PecaInvalidaException e) {  }
                
                return pecaCortada;
            }
            /*Mudei aqui segunda dia 26 de maio 9:57 da manha*/
//            if(pedido.retorneDimensao().compareAreas(pedaco.retorneDimensao()) == 1){
//            
//                System.out.println("Peça e pedido com dimensões iguais ...");
//                Peca pecaCortada = null;
//
//                try {
//                    pecaCortada = new Peca(pedaco, pedido, permiteRotacao, false);
//                } catch (PecaInvalidaException e) {
//                }
//                
//                return pecaCortada;                
//            }
            
//            System.out.println("\nCorte horizontal é melhor");
            
            if((pedido.retorneDimensao().retorneBase() > pedaco.retorneBase())||
                                                         (pedido.retorneDimensao().retorneAltura()>pedaco.retorneAltura())){
            
                pos1 = pedido.retorneDimensao().retorneBase();
                tamanho1 = pedaco.retorneBase();
                pos2 = pedido.retorneDimensao().retorneAltura();
                tamanho2 = pedido.retorneDimensao().retorneBase();
            
            }else{
                
                pos1 = pedido.retorneDimensao().retorneAltura();
                tamanho1 = pedaco.retorneBase();
                pos2 = pedido.retorneDimensao().retorneBase();
                tamanho2 = pedido.retorneDimensao().retorneAltura();
            }
//                pos1 = pedido.retorneDimensao().retorneAltura(); //Mudei para fazer um teste foi inserido um if e else
//                tamanho1 = pedaco.retorneBase();
//                pos2 = pedido.retorneDimensao().retorneBase();
//                tamanho2 = pedido.retorneDimensao().retorneAltura();
        }

        //registraCorte(float pos, Ponto pontoChapa, boolean corteVertical,float tamanho, int idCorte)

        //Verificar como adaptar para cada um tipo de corte e adaptar Ponto Inferior Esquerdo 
        corte1.registraCorte(pos1, pedaco.getPontoInferiorEsquerdo(), primeiroCorteVertical, tamanho1, pedaco.getId());
        corte2.registraCorte(pos2, pedaco.getPontoInferiorEsquerdo(), !primeiroCorteVertical, tamanho2, pedaco.getId());

        PedacoDisponivel AreaRestante1, AreaRestante2;
        AreaRestante1 = pedaco.corte(corte1);
        AreaRestante2 = pedaco.corte(corte2);

        if (AreaRestante1 != null) {

//            System.out.println("Vou adicionar a sobra 1 !!!");
//            System.out.println("\nSobra 1 gerada com PInfE (" + AreaRestante1.getPontoInferiorEsquerdo().getX() + ","
//                    + AreaRestante1.getPontoInferiorEsquerdo().getY() + " ) e PSupDir(" + AreaRestante1.getPontoSuperiorDireito().getX() + ","
//                    + AreaRestante1.getPontoSuperiorDireito().getY() + ")");

            listaDePedacosRestantes.add(AreaRestante1);
            listaDeCortes.add(corte1);
        }
        if (AreaRestante2 != null) {

//            System.out.println("Vou adicionar a sobra 2 !!!");
//            System.out.println("\nSobra 2 gerada com PInfE (" + AreaRestante2.getPontoInferiorEsquerdo().getX() + ","
//                    + AreaRestante2.getPontoInferiorEsquerdo().getY() + ") e PSupDir(" + AreaRestante2.getPontoSuperiorDireito().getX() + ","
//                    + AreaRestante2.getPontoSuperiorDireito().getY() + ")");

            listaDePedacosRestantes.add(AreaRestante2);
            listaDeCortes.add(corte2);
        }

        Peca pecaCortada = null;

        try {
            pecaCortada = new Peca(pedaco, pedido, permiteRotacao, false);
        } catch (PecaInvalidaException e) {
        }

        return pecaCortada;
    }

    /* ESTA FUNÇÃO ABAIXO FUNCIONA APENAS PARA ESTRATEGIA 1 */
    private boolean corteVerticalEMelhor(IPedido pedido, PedacoDisponivel pedaco) {

        if(pedido.retorneDimensao().compareAreas(pedaco.retorneDimensao()) == 1){
        
            return false;
        }
        
        AreaRetangular areaCorteVertical1 = new AreaRetangular(pedido.retorneDimensao().retorneBase(),
                pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());

        AreaRetangular areaCorteVertical2 = new AreaRetangular(pedaco.retorneBase() - pedido.retorneDimensao().retorneBase(),
                pedaco.retorneAltura());

        AreaRetangular menorVertical;

        if (areaCorteVertical1.retorneArea() < areaCorteVertical2.retorneArea()) {
            menorVertical = areaCorteVertical1;
        } else {
            menorVertical = areaCorteVertical2;
        }

        AreaRetangular areaCorteHorizontal1 = new AreaRetangular(pedaco.retorneBase(),
                pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());
        AreaRetangular areaCorteHorizontal2 = new AreaRetangular(pedaco.retorneBase() - pedido.retorneDimensao().retorneBase(),
                pedido.retorneDimensao().retorneAltura());

        AreaRetangular menorHorizontal;

        if (areaCorteHorizontal1.retorneArea() < areaCorteHorizontal2.retorneArea()) {
            menorHorizontal = areaCorteHorizontal1;
        } else {
            menorHorizontal = areaCorteHorizontal2;
        }

        if (menorVertical.retorneArea() > menorHorizontal.retorneArea()) {
            return true;
        }

        return false;
    }

    private IPedido maiorPedidoQueCabe(PedacoDisponivel pedaco, ListIterator iteradorPedidos) {

        int cabe_return = -1;
        IPedido maior, maior2, maior3;

        while (iteradorPedidos.hasNext()) {

            maior = (IPedido) iteradorPedidos.next();
            cabe_return = pedaco.cabePeca(maior.retorneDimensao(), permiteRotacao);

            if (cabe_return == 0 || cabe_return == 1) {

                if (cabe_return == 0) {

//                    System.out.println("Retorna o pedido sem rotação !");
                    return maior;
                } else {

//                    System.out.println("Retorna o pedido rotacionado !");

                    return maior;
                    //return new Pedidos(maior.id(), maior.retorneDimensao().retorneAltura(), maior.retorneDimensao().retorneBase());


                    //return maior2;
                }
            }

        }

        return null;
    }

    /* Abaixo, a implementacao da "solucaoParcial" para o problema do corte
     * 
     * @param listaSobras possui objetos que atendem à interface ISobra, e para os quais serao gerados planos de corte
     * @param tamanhoChapa informa as dimensões da chapa, caso acabem os elementos da lista de sobras e ainda existam
     *        elementos a serem cortados 
     */
    //@Override
    public void solucao_Parcial(IGenericSolution florest, LinkedList listaPecas, LinkedList listaSobras, IDimensao2d tamanhoChapa) {

        Collections.sort(listaSobras, new ComparadorISobra());

        Collections.sort(listaPecas, new ComparadorIPecaPronta());
        Collections.reverse(listaPecas);

        ListIterator iteradorSobras = listaSobras.listIterator();

        LinkedList listaPlanosDeCorte = new LinkedList();

        PlanoDeCorte plano;

//        System.out.println("Numero de pecas para aplicar as sobras ...> "+listaPecas.size());
        while (iteradorSobras.hasNext()) {

            ISobra sobraAtual = (ISobra) iteradorSobras.next();
            plano = this.corteChapa(sobraAtual, listaPecas);
            listaPlanosDeCorte.add(plano);
        }

//        System.out.println("Numero de pecas para aplicar a novas placas completas ...> "+listaPecas.size());
        
        PlanoDeCorte binCompleto;
        LinkedList listaBinsCompletos = new LinkedList();

        while (!listaPecas.isEmpty()) {

            binCompleto = this.corteChapa((ISobra) (new PedacoDisponivel(new Ponto(0, 0), tamanhoChapa, 1)), listaPecas);
            listaBinsCompletos.add(binCompleto);
        }

//        System.out.println("realizando a conversão parcial ....");
        
        SolutionConversorFactory.getInstance().newConversor().converteSubSolucao(florest,listaPlanosDeCorte,listaBinsCompletos, 
                                                                                                    tamanhoChapa/*, isGrasp*/);
        
    }

    private PlanoDeCorte corteChapa(ISobra sobraAtual, LinkedList listaPecas) {

//        System.out.println("\n############## Informação Sobra Disponível ################### \n");
//        
//        System.out.println("\nAltura --> " + sobraAtual.retorneAltura());
//        System.out.println("Base --> " + sobraAtual.retorneBase());
//        System.out.println("Ponto Inferior Esquerdo --> ( " + sobraAtual.getPontoInferiorEsquerdo().getX() + ", "
//                + sobraAtual.getPontoInferiorEsquerdo().getY() + ")");
//        System.out.println("Ponto Superior Direito --> ( " + sobraAtual.getPontoSuperiorDireito().getX() + ", "
//                + sobraAtual.getPontoSuperiorDireito().getY() + ")");
//
//        System.out.println("Quantidade de peças recebidas para a sobra --> "+listaPecas.size());
        
        ListIterator iteradorPecas = listaPecas.listIterator();

        IPecaPronta pecaAtual = maiorPecaQueCabe(sobraAtual, iteradorPecas);

        
        PlanoDeCorte planoDeCorte = new PlanoDeCorte(sobraAtual);

        if (pecaAtual != null) {

//            System.out.println("Peça selecionada ---> (W x H) --> (" 
//                                                    +((IBPTNodeITestePecaWrapper)pecaAtual).retorneDimensao().retorneBase() + " x "
//                                                    + ((IBPTNodeITestePecaWrapper)pecaAtual).retorneDimensao().retorneAltura() + " )"+" Id -> "
//                                                    +((IBPTNodeITestePecaWrapper)pecaAtual).getNoEmpacotado().getID());
        
            pecaAtual.retireUmaPeca(); //Este metodo define que uma das pecas representada por "pecaAtual" esta cortada

            if (pecaAtual.getPecasDisponiveis() == 0) {
               iteradorPecas.remove();
               
            }

            LinkedList listaDeCortes = new LinkedList(), listaDePedacosRestantes = new LinkedList();

            this.efetueCorte(pecaAtual, sobraAtual, listaDeCortes, listaDePedacosRestantes);
            //planoDeCorte.adicioneCortes(listaDeCortes);
            planoDeCorte.getListaCortes().addAll(listaDeCortes);
            planoDeCorte.adicionePeca(pecaAtual);

            //System.out.println("\nLista de Cortes gerada  --> " + listaDeCortes.toArray().length);
            //System.out.println("Lista de Cortes do Plano de Corte --> "+ planoDeCorte.getListaCortes().size());
            //System.out.println("Lista de Pedaços Restantes --> " + listaDePedacosRestantes.toArray().length);
            
            if (listaDePedacosRestantes.size() == 2) {
                ISobra menorPedaco = (ISobra) Collections.min(listaDePedacosRestantes, new ComparadorISobra());
                ISobra maiorPedaco = (ISobra) Collections.max(listaDePedacosRestantes, new ComparadorISobra());

                planoDeCorte.integrar(this.corteChapa(menorPedaco, listaPecas));
                planoDeCorte.integrar(this.corteChapa(maiorPedaco, listaPecas));
            } else if (listaDePedacosRestantes.size() == 1) {
                planoDeCorte.integrar(this.corteChapa((ISobra) listaDePedacosRestantes.getFirst(), listaPecas));
            }
        } else {
            planoDeCorte.adicionarSobra(sobraAtual);
        }

        return planoDeCorte;
    }

    private void efetueCorte(IPecaPronta pedido, ISobra pedaco, LinkedList listaDeCortes, LinkedList listaDePedacosRestantes) {

        boolean primeiroCorteVertical = corteVerticalEMelhor(pedido, pedaco);

        float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;

//        System.out.println("\nCorte para o pedido em uma sobra ... ");
//        System.out.println("PecaPronta --> ("+pedido.retorneDimensao().retorneBase()+" , "+pedido.retorneDimensao().retorneAltura()+")");
//        System.out.println("Sobra escolhida --> ("+pedaco.retorneDimensao().retorneBase()+" , "+pedaco.retorneDimensao().retorneAltura()+")");
        
        if (primeiroCorteVertical) {
            
//            System.out.println("Cortar vertical ...");
            
            if((pedido.retorneDimensao().retorneBase() > pedaco.retorneBase()) ||
                                                         (pedido.retorneDimensao().retorneAltura()>pedaco.retorneAltura())){
            
                pos1 = pedido.retorneDimensao().retorneAltura();
                tamanho1 = pedaco.retorneAltura();
                pos2 = pedido.retorneDimensao().retorneBase();
                tamanho2 = pedido.retorneDimensao().retorneAltura();
            }else{
                
                pos1 = pedido.retorneDimensao().retorneBase();
                tamanho1 = pedaco.retorneAltura();
                pos2 = pedido.retorneDimensao().retorneAltura();
                tamanho2 = pedido.retorneDimensao().retorneBase();
            }

        } else {
            
            /*Mudei aqui segunda dia 26 de maio 9:57 da manha*/
            if((pedido.retorneDimensao().retorneBase() == pedaco.retorneDimensao().retorneBase())&&
                    (pedido.retorneDimensao().retorneAltura() == pedaco.retorneDimensao().retorneAltura())){
            
//                System.out.println("Peça e sobra com dimensões iguais ... ");
                pedido.setPosicao(pedaco.getPontoInferiorEsquerdo());
                
                return;
            }else if((pedido.retorneDimensao().retorneBase() == pedaco.retorneDimensao().retorneAltura())&&
                    (pedido.retorneDimensao().retorneAltura() == pedaco.retorneDimensao().retorneBase())){
            
//                System.out.println("Peça e sobra com dimensões iguais ... ");
                pedido.setPosicao(pedaco.getPontoInferiorEsquerdo());
                
                return;
            }
//            if(pedido.retorneDimensao().compareAreas(pedaco.retorneDimensao()) == 1){
//            
//                System.out.println("Peça e pedido com dimensões iguais ... ");
//                pedido.setPosicao(pedaco.getPontoInferiorEsquerdo());
//                
//                return;
//            }
//            System.out.println("Cortar horizontal ...");
            if((pedido.retorneDimensao().retorneBase() > pedaco.retorneBase())||
                                                         (pedido.retorneDimensao().retorneAltura()>pedaco.retorneAltura())){
            
                pos1 = pedido.retorneDimensao().retorneBase();
                tamanho1 = pedaco.retorneBase();
                pos2 = pedido.retorneDimensao().retorneAltura();
                tamanho2 = pedido.retorneDimensao().retorneBase();
            
            }else{
                
                pos1 = pedido.retorneDimensao().retorneAltura();
                tamanho1 = pedaco.retorneBase();
                pos2 = pedido.retorneDimensao().retorneBase();
                tamanho2 = pedido.retorneDimensao().retorneAltura();
            }
        }

        Corte corte1 = new Corte(pos1, pedaco.getPontoInferiorEsquerdo(), primeiroCorteVertical, tamanho1, pedaco.getId());
        Corte corte2 = new Corte(pos2, pedaco.getPontoInferiorEsquerdo(), !primeiroCorteVertical,tamanho2, pedaco.getId());

        PedacoDisponivel pedacoCortado, AreaRestante1, AreaRestante2;
        pedacoCortado = new PedacoDisponivel(pedaco.getPontoInferiorEsquerdo(), pedaco.getPontoSuperiorDireito(), pedaco.getId());
        AreaRestante1 = pedacoCortado.corte(corte1);
        AreaRestante2 = pedacoCortado.corte(corte2);

        if (AreaRestante1 != null) {
            listaDePedacosRestantes.add(AreaRestante1);
            listaDeCortes.add(corte1);
        }
        if (AreaRestante2 != null) {
            listaDePedacosRestantes.add(AreaRestante2);
            listaDeCortes.add(corte2);
        }

        pedido.setPosicao(pedaco.getPontoInferiorEsquerdo());
    }

    //Este código e uma duplicata do código para as outras interfaces. ELIMINAR UMA DAS DUAS.
    private boolean corteVerticalEMelhor(IPecaPronta pedido, ISobra pedaco) {

         if((pedido.retorneDimensao().retorneBase() == pedaco.retorneDimensao().retorneBase())&&
                    (pedido.retorneDimensao().retorneAltura() == pedaco.retorneDimensao().retorneAltura())){
                            
             return false;
         }else if((pedido.retorneDimensao().retorneBase() == pedaco.retorneDimensao().retorneAltura())&&
                    (pedido.retorneDimensao().retorneAltura() == pedaco.retorneDimensao().retorneBase())){
            
             return false;
        }
        
        AreaRetangular areaCorteVertical1 = new AreaRetangular(pedido.retorneDimensao().retorneBase(),
                pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());

        AreaRetangular areaCorteVertical2 = new AreaRetangular(pedaco.retorneBase()
                - pedido.retorneDimensao().retorneBase(), pedaco.retorneAltura());

        AreaRetangular menorVertical;

        if (areaCorteVertical1.retorneArea() < areaCorteVertical2.retorneArea()) {
            menorVertical = areaCorteVertical1;
        } else {
            menorVertical = areaCorteVertical2;
        }

        AreaRetangular areaCorteHorizontal1 = new AreaRetangular(pedaco.retorneBase(),
                pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());
        AreaRetangular areaCorteHorizontal2 = new AreaRetangular(pedaco.retorneBase()
                - pedido.retorneDimensao().retorneBase(), pedido.retorneDimensao().retorneAltura());

        AreaRetangular menorHorizontal;

        if (areaCorteHorizontal1.retorneArea() < areaCorteHorizontal2.retorneArea()) {
            menorHorizontal = areaCorteHorizontal1;
        } else {
            menorHorizontal = areaCorteHorizontal2;
        }

        if (menorVertical.retorneArea() > menorHorizontal.retorneArea()) {
            return true;
        }

        return false;
    }

    private IPecaPronta maiorPecaQueCabe(ISobra pedaco, ListIterator iteradorPedidos) {//Recebe o iterator de Peças

        int cabe_return = -1, id;

        IPecaPronta maior;
        IPedido maior_;
        
        while (iteradorPedidos.hasNext()) {

            maior = (IPecaPronta) ((BPTPedaco) iteradorPedidos.next()).getIPecaIPedidoWrapper();
            //maior = (IPecaPronta) iteradorPedidos.next();                        

            cabe_return = pedaco.cabePeca(maior.retorneDimensao(), permiteRotacao);

            if (cabe_return == 0 || cabe_return == 1) {

                    return (IPecaPronta) maior;               
            }
        }

        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                         //
    // Esse algoritmo de desmonte pode estabeler comunicação entre o HHDHeuristic e o GRASP, uma vez que ele   //
    // pode desmontar uma bin completa e chamar o GRASP para execução, ao término une as duas soluções !!!     //
    //                                                                                                         //      
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public LinkedList<Pedidos> algoritmoDesmontHDH(IBin bin_escolhida) {

        //Desmonta uma Bin inteira e devolve uma lista de Pedidos Não Atendidos
        int idP;
        IBin bin_atual;
        Peca peca_atual;
        IPedido I_pedido;
        Pedidos pedido;

        LinkedList<Pedidos> l = new LinkedList<Pedidos>();

        Iterator<Peca> iter_peca = bin_escolhida.getListaPecas().iterator();

        while (iter_peca.hasNext()) {

            peca_atual = iter_peca.next();

            I_pedido = peca_atual.getPedidoAtendido();
            pedido = new Pedidos(I_pedido.id(), I_pedido.retorneDimensao().retorneBase(), I_pedido.retorneDimensao().retorneAltura());

            //Devolve o pedido
            pedido.devolvaPedido();

            //Remove a peça da lista de peça
            bin_escolhida.getListaPecas().remove(peca_atual);

            //Adiciona o pedido a uma lista de pedidos Não Atendidos
            l.add(pedido);
        }

        //Realiza limpeza da bin

        bin_escolhida.getListaPecas().clear();
        bin_escolhida.getListaCortes().clear();
        bin_escolhida.getListaSobras().clear();

        return l;
    } 
    
    /*Algoritmo Agente para desmontar parte uma solução, desmontando placas abaixo de um aproveitamentoNivel*/
    public SolucaoHeuristica criaSolucaoHHDMelhoria2Version(SolucaoHeuristica solucaoF, float aproveitamento){
    
        IBin placa1 = null;
        IBin placa2 = null;
        Double fav1;
        Double fav2;
        
//        System.out.println("\n\n\nVOU TESTAR O HHDMELHORIA 2 VERSION DESMONTANDO POR APROVEITAMENTO\n");
        //Armazena o tamanho da chapa e cria uma lista de Pedidos necessária ao HHDHeuristic
        IDimensao2d tamanhoChapa = solucaoF.getTamanhoChapa();
        LinkedList<Pedidos> listPedidos = new LinkedList<Pedidos>();

        //Solução temporária que armazena a solução completa
        SolucaoHeuristica solutionNova = new SolucaoHeuristica();
        SolucaoHeuristica solution = new SolucaoHeuristica(solucaoF);
        
        //Vou ordenar minha solução
        HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solucaoF);//Seria interessante ordenar por aproveitamentoNivel !
    
        Bin bin;
//        System.out.println("Vou desmontar as placas HD com aproveitamento abaixo de -> "+aproveitamento);
        Iterator<Bin> iterator = solucaoF.getObjetos().iterator();
        
        while(iterator.hasNext()){
        
            bin = iterator.next();
            //Vou desmontar as placas com aproveitamentoNivel menor que o informado
            if(bin.getAproveitamento() <= aproveitamento){
            
//                System.out.println("Peças da placa escolhida: [ "+ bin.getListaPecas().size()+" ]");
                listPedidos.addAll(algoritmoDesmontHDH(bin));
                solution.set_Fav(solution.get_Fav() - (bin.getFav()));
                solution.setSomatorioSobras(solution.getSomatorioSobras() - bin.getSobra());
                solution.removePlaca(bin);              
            }
        }
//        System.out.println("Solution ---> "+ solution.getQtd());
//        System.out.println("Solucao  ---> "+ solucaoF.getQtd());
//        System.out.println("Lista Pedidos --> " +listPedidos);
        
        if(solution.getQtd() == solucaoF.getQtd()){
        
            //Não há o que melhorar pois, nenhuma placa foi removida !
//            System.out.println("Nenhum aproveitamento abaixo do especificado");
            return solucaoF;
        }
        else{
            solution.removeIndividuo();
            //Aqui retira a lista de Pedidos da solução escolhida

            solutionNova = obtenhaSolucao(tamanhoChapa, listPedidos);

            solution.getObjetos().addAll(solutionNova.getObjetos());
            solution.getIPedidos().addAll(solutionNova.getIPedidos());
            solution.getLinkedListIndividuos().addAll(solutionNova.getLinkedListIndividuos());

            solution.calculaLinhasDeCorteEMediaSobras();
            solution.setTipoHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria);

//            System.out.println("\n\n\n%%%%%%%%%%%%%%%%%%%%% HHDMELHORIA 2 VERSION ENCERRANDO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        }
        return solution;                
    }

    /*Algoritmo agente que dada uma política de seleção para as placas desmonta algumas placas com base na política de seleção*/
    public SolucaoHeuristica criaSolucaoHHDMelhoria(SolucaoHeuristica solucaoF, PoliticaSelecao polSelecao) {

        /*ISolutionInterface solucao;
        solucao = obtenhaSolucao(tamanhoChapa, pedido);*/
        IBin placa1 = null;
        IBin placa2 = null;
        Double fav1;
        Double fav2;

        System.out.println("\n\n\nAQUI EU VOU TESTAR O HHDMELHORIA SIMPLES DESMONTANDO DUAS PLACAS\n");
        //Armazena o tamanho da chapa e cria uma lista de Pedidos necessária ao HHDHeuristic
        IDimensao2d tamanhoChapa = solucaoF.getTamanhoChapa();
        LinkedList<Pedidos> listPedidos = new LinkedList<Pedidos>();

        //Solução temporária que armazena a solução completa
        SolucaoHeuristica solutionNova = new SolucaoHeuristica();
        SolucaoHeuristica solution = new SolucaoHeuristica(solucaoF);

        //Aqui deve ser selecionado duas Placas e realiza desmonte das duas placas
        if (polSelecao.equals(PoliticaSelecao.MelhorMelhor)) {

            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa1));

            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.removePlaca(placa1);

            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa2));

            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.removePlaca(placa2);

        } else if (polSelecao.equals(PoliticaSelecao.MelhorPior)) {

            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa1));

            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.removePlaca(placa1);

            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMenorOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa2));

            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.removePlaca(placa2);
        } else if (polSelecao.equals(PoliticaSelecao.MelhorAleatorio)) {

            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMaiorOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa1));

            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.removePlaca(placa1);

            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaAleatoriaOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa2));

            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.removePlaca(placa2);
        } else {//Pior - Aleatória

            placa1 = (Bin) OperacoesSolucoes_Individuos.selecionaListaMenorOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa1));

            solution.set_Fav(solution.get_Fav() - (placa1.getFav()));
            solution.removePlaca(placa1);

            placa2 = (Bin) OperacoesSolucoes_Individuos.selecionaListaAleatoriaOcupacaoG(solution);
            listPedidos.addAll(algoritmoDesmontHDH(placa2));

            solution.set_Fav(solution.get_Fav() - (placa2.getFav()));
            solution.removePlaca(placa2);
        }
        //Aqui retira a lista de Pedidos da solução escolhida

        solutionNova = obtenhaSolucao(tamanhoChapa, listPedidos);

        solution.getObjetos().addAll(solutionNova.getObjetos());
        solution.getIPedidos().addAll(solutionNova.getIPedidos());
        solution.getLinkedListIndividuos().addAll(solutionNova.getLinkedListIndividuos());

        solution.calculaLinhasDeCorteEMediaSobras();

        return solution;
    }

    //Esse método verifica se a árvore de corte foi criada !!!
    public static ArrayList<ArrayList<IBPTNode>> VerificaImprimeArvoreDeCorte(IGenericSolution arvorecortes) {

        int quantidadeArvores = arvorecortes.getQtd();
        IDimensao2d tamanhoChapa = arvorecortes.getTamanhoChapa();
        BinPackTree binPackTree;

        //Criar uma fila pra testar a sequência de visitas criada pela árvore e a busca em largura
        Queue<HHDBinPackingTree.IBPTNode> fila = null;
        ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> vetor_arvore = new ArrayList<ArrayList<IBPTNode>>();

//        System.out.println("\n\nQuantidade de árvores de Corte --> " + quantidadeArvores);
//        System.out.println("Tamanho da placa --> (W x H) --> (" + tamanhoChapa.retorneBase() + " X " + tamanhoChapa.retorneAltura() + " )");
//
//        System.out.println("\n\n#####################  Imprimindo Árvore de corte ####################\n\n");

        for (int i = 0; i < quantidadeArvores; i++) {

            binPackTree = (BinPackTree) arvorecortes.retornePlanoDeCorte(i);
            //IBin ib = arvorecortes.retornePlanoDeCorte(a);

            //Bin bin = (Bin) arvorecortes.retornePlanoDeCorte(a);
            vetor_arvore.add(Utilidades.Funcoes.retornaSequenciaBlarguraBinPacking(binPackTree.getRaiz(), fila));

//            System.out.println("BinPackTree Id --> " + binPackTree.getId());

            Iterator<BinPackCorte> itBinpackTree = binPackTree.getListaCortes().iterator();
            int cont = 0;
            while (itBinpackTree.hasNext()) {

                
                BinPackCorte b = itBinpackTree.next();
                
//                System.out.println("\n\nInformações do Corte " + cont + "\n");
//                System.out.println("Id --> " + b.getId());
//                System.out.println("Tamanho --> " + b.getTamanho());

                if (b.eVertical()) {
//                    System.out.println("Corte Vertical");
                } else {
//                    System.out.println("Corte Horizontal");
                }

//                System.out.println("Posição Corte --> " + b.getPosicaoCorte());
//                System.out.println("Ponto (" + b.getPontoChapaCortada().getX() + " , " + b.getPontoChapaCortada().getY() + " )");

               cont++; 
            }

//            System.out.println("\n\n################ Lista de Peças ##################");

            cont = 0;
            int numPecas =  binPackTree.getListaPecas().size();
//            System.out.println("\nNúmero de peças na lista: "+ numPecas);
            Iterator<BPTPedaco> iterPecas = binPackTree.getListaPecas().iterator();

            while (iterPecas.hasNext()) {

                BPTPedaco bpt = iterPecas.next();

//                System.out.println("\nDimensões Peças- (Largura x Altura) --> (" + bpt.retorneBase() + " x " + bpt.retorneAltura() + " )");
//                System.out.println("PInfEsq (" + bpt.getPontoInfEsq().getX() + " , " + bpt.getPontoInfEsq().getY() + " )");
//                System.out.println("PSupDir (" + bpt.getPontoSupDir().getX() + " , " + bpt.getPontoSupDir().getY() + " )");
                cont++;
            }

            cont = 0;
            int numSobras =  binPackTree.getListaSobras().size();
            
            Iterator<BPTPedaco> iterSobras = binPackTree.getListaSobras().iterator();

//            System.out.println("\n\n ############### Lista de Sobras #################");

//            System.out.println("\nNúmero de sobras na lista: "+ numSobras);
            while (iterSobras.hasNext()) {

                BPTPedaco bpt = iterSobras.next();

//                System.out.println("\nDimensões Sobras - (Largura x Altura) --> (" + bpt.retorneBase() + " x " + bpt.retorneAltura() + " )");
//                System.out.println("PInfEsq (" + bpt.getPontoInfEsq().getX() + " , " + bpt.getPontoInfEsq().getY() + " )");
//                System.out.println("PSupDir (" + bpt.getPontoSupDir().getX() + " , " + bpt.getPontoSupDir().getY() + " )");
                cont++;
            }
//            System.out.println("\n\n\n");

            //Após adicionar o array de Nós calcula-se o desperdício !
            ArrayList<IBPTNode> array = vetor_arvore.get(i);
            for (int vet = (array.size() - 1); vet >= 0; vet--) {

                BPTPedaco.CalculaDesperdicioAproveitamento(((BPTPedaco) array.get(vet)));
            }
//            System.out.println("Terminei o cálculo do desperdício e aproveitamento por nível !");
        }

        return vetor_arvore;
    }

    ////////////////////////////////////////////////////////////////////////////
    //                                                                        //
    //     MÉTODO QUE DESMONTA E REMONTA DADO UM RAMO DA ÁRVORE               //
    //                                                                        //
    ////////////////////////////////////////////////////////////////////////////
    
    /*Algoritmo agente que desmonta parte de um nível da árvore com base em um valor de desperdício e aproveitamentoNivel dado !*/
    public BinPackTreeForest HHDHeuristicMonteDesmonte1(SolucaoHeuristica solHeu, IDimensao2d tamanho, EstrategiaBinPackTree ebpt,
                                                           boolean desmontaBinCompleta, PoliticaSelecao polSelecao, 
                                                           float aproveitamento,float desperdicio) {

        boolean isGrasp = false;//eGrasp
        IBPTNode noDesmonte     = null, noDesmonte2  = null;
        LinkedList listPecas    = new LinkedList(), ListaPecasT   = new LinkedList();
        LinkedList listPecas2   = new LinkedList(), ListaPecasT2  = new LinkedList();
        LinkedList listCortes   = new LinkedList(), ListaCortesT  = new LinkedList();
        LinkedList listCortes2  = new LinkedList(), ListaCortesT2 = new LinkedList();
        LinkedList listaSobras  = new LinkedList(), ListaSobrasT  = new LinkedList(), lista_SobrasT  = new LinkedList();
        LinkedList listaSobras2 = new LinkedList(), ListaSobrasT2 = new LinkedList();
        
        LinkedList<IBPTNode> list_INode = new LinkedList<IBPTNode>();

        //Solução temporária que armazena a solução completa
        SolucaoHeuristica solutionNova = new SolucaoHeuristica();
        SolucaoHeuristica solution     = new SolucaoHeuristica(solHeu); //solucao que irá receber !

        //Vou ordenar minha solução
        HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solHeu);//Seria interessante ordenar por aproveitamentoNivel !
    
        if((solHeu.getTipoHeuristic() == ETipoHeuristicas.GRASP_2D_CONSTRUTIVO) ||
                                                                 (solHeu.getTipoHeuristic() == ETipoHeuristicas.Grasp2d_Melhoria)){
            isGrasp = true;
        }
        else{
            isGrasp = false;
        }
        
        System.out.println("Tipo da heurística --> "+solHeu.getTipoHeuristic());    
                
        BinPackTree bptree1 = null, bptree2 = null;
        Queue<HHDBinPackingTree.IBPTNode> fila = null, fila2 = null;
        ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> vetor_arvore = new ArrayList<ArrayList<IBPTNode>>();
        
        IGenericSolution binPackTreeForest = SolutionConversorFactory.getInstance().newConversor().converteParaSolucao(solHeu/*, isGrasp*/);
        
        Iterator<BinPackTree> iterator = ((BinPackTreeForest) binPackTreeForest).getListaBinPackTrees().iterator();
        
        int c = 0;
        
        while(iterator.hasNext()){
        
            BinPackTree bptree = iterator.next();
            System.out.println("\nArvore "+c+" -> "+bptree.toString());
            ListaPecasT2.addAll(bptree.getListaPecas());   //ListaPecasT2,ListaCortesT2 e ListaSobrasT2 armazenam de todas as trees;
            ListaCortesT2.addAll(bptree.getListaCortes());
            ListaSobrasT2.addAll(bptree.getListaSobras());
            
            vetor_arvore.add(Utilidades.Funcoes.retornaSequenciaBlarguraBinPacking(bptree.getRaiz(), fila));
            
            //Após adicionar o array de Nós calcula-se o desperdício !
            ArrayList<IBPTNode> array = vetor_arvore.get(c);
            for (int vet = (array.size() - 1); vet >= 0; vet--) {

                BPTPedaco.CalculaDesperdicioAproveitamento(((BPTPedaco) array.get(vet)));
            }
            fila = null;
            c++;
        }
        ((BinPackTreeForest) binPackTreeForest).calculaFAVFAV2((BinPackTreeForest) binPackTreeForest, vetor_arvore); //ver se calcula dentro da conversao
        
        //Aqui em função da estratégia é definido o desmonte e remonte;

        if ((ebpt == EstrategiaBinPackTree.OneSolutionHHD) || (ebpt == EstrategiaBinPackTree.TwoSolutionsHHD)){
            
            for (int i = 0; i < vetor_arvore.size(); i++) {
                    
                  IBPTNode ibptNode = vetor_arvore.get(i).get(0); //[Nó raiz]
//                  System.out.println("\nDesperdício do nó   --> " + ((BPTPedaco) ibptNode).getDesperdicio());
//                  System.out.println("Desperdício informado --> " + (desperdicio * 100));

                  if (((BPTPedaco) ibptNode).getDesperdicio() >= (desperdicio * 100)) {

                        noDesmonte = ibptNode;
                        list_INode.add(noDesmonte);
                        //break;
                  }else{
                  
                      for (int a = 0; a < vetor_arvore.get(i).size(); a++) {
                          
                          IBPTNode ibptNodeNivel = vetor_arvore.get(i).get(a);
                          
//                          System.out.println("\nAproveitamento do nó --> " + ((BPTPedaco) ibptNodeNivel).getAproveitamento());
//                          System.out.println("Aproveitamento informado --> " + (aproveitamento));
                          
                          if (((BPTPedaco) ibptNodeNivel).getAproveitamento() <= aproveitamento) {

                              noDesmonte = ibptNodeNivel;
                              list_INode.add(noDesmonte); 
                              break;
                          }
                      }                  
                  }
            }
            if (list_INode.isEmpty()) {

//                    System.out.println("Não foi encontrado um nó com desperdício e/ou aproveitamento igual ou acima do "
//                                                                                                    + "valor especificado !\n");

                 return null;
            }
            else{
                
//                System.out.println("Quantidade de árvore a desmontar ... "+list_INode.size());
                //Aqui será percorrido cada árvore que será desmontada e construirá a lista de peças, sobras e cortes;
                Iterator<IBPTNode>  iterNode = list_INode.iterator();
                
                while(iterNode.hasNext()){
                
                    IBPTNode ibn = iterNode.next();
                    BinPackTree bin = ibn.getArvore();
                    
                    //Aqui são selecionados todas as lista com informações de toda a placa e a partir de um subnó !
                    ListaPecasT = bin.getListaPecas(ibn);
                    ListaCortesT = bin.getListaCortes(ibn);
                    
                    lista_SobrasT = bin.getListaSobras();  //Uma lista de sobra recebe a partir do nó raiz
                    ListaSobrasT = bin.getListaSobras(ibn);//Uma lista de sobra recebe a partir do nó escolhido
                    ListaSobrasT2.removeAll(ListaSobrasT); 
                    lista_SobrasT.removeAll(ListaSobrasT); //Atualiza a lista removendo a sobra apartir do nivel.
                    
                    listPecas.addAll(ListaPecasT);    //listPecas,listCortes,listSobras armazenam das árvores que vão ser removidas
                    listCortes.addAll(ListaCortesT);
                    //listaSobras.addAll(ListaSobrasT);
                    
                    //Essa condicional verifica se o IBPTNode é o nó raiz da árvore, se condição verdaeira removerá o plano de corte.
                    if(ibn.equals(bin.getRaiz())){
                        ((BinPackTreeForest) binPackTreeForest).removaPlanoDeCorte(bin);
                        
                    }else{//Se a condicional não for satisfeita transforma a partir do nó escolhido em sobra.
                    
                        ISobra isobra = ibn.remova();
                        IBPTNode pai = ((BPTPedaco)isobra).getFather();
                        try{
                          if(pai.getLeftSon() == (IBPTNode) ibn){

                              pai.setLeftson((IBPTNode)isobra);
                          }
                          else{
                              pai.setRightSon((IBPTNode)isobra);
                          }
                        }catch(Exception ex){
                        
                        }
                        
//                        System.out.println("Pai da sobra -> ( "+ ((BPTPedaco)isobra).getFather().getDimensoes().retorneBase()+" ,"
//                                + ((BPTPedaco)isobra).getFather().getDimensoes().retorneAltura()+")");
                        
//                        System.out.println("\nIsobra de tamanho --> (" + isobra.retorneBase() + " , " + isobra.retorneAltura() + " )");
                        
                        lista_SobrasT.add(isobra);
                        listaSobras.addAll(lista_SobrasT);
                        ListaSobrasT2.add(isobra);
                    }

                }
                
                //Aqui eu elimino as sobras e cortes das bins removidas
                ListaPecasT2.removeAll(listPecas);  //Atualiza a lista de todas menos as que foram removidas
                ListaCortesT2.removeAll(listCortes);
                //ListaSobrasT2.removeAll(listaSobras);
                
//                System.out.println("## Numero de peças para solução parcial --> "+listPecas.size());
//                System.out.println("## Numero de sobras para solução parcial --> "+listaSobras.size());
//                System.out.println("## Numero de sobrasT2 para solução parcial --> "+ListaSobrasT2.size());
                
                //binPackTreeForest = solucao_Parcial(listPecas, listaSobras, tamanho);
                
                /*return (BinPackTreeForest)*/ solucao_Parcial(binPackTreeForest, listPecas, /*listaSobras*/ListaSobrasT2, tamanho);
                return (BinPackTreeForest) binPackTreeForest;
            }
            
        }else{
        
            return null;
        }
        
    }
    
    //Esse algoritmo desmonta árvores com aproveitamento abaixo do especificado e tenta reaplica a estratégia de solução Parcial
    //as sobras das arvores anteriores, caso não seja possível novos planos de corte são gerado;
    
    public BinPackTreeForest HHDHeuristicMonteDesmonte2Version(SolucaoHeuristica solHeu, IDimensao2d tamanho, 
                                                                            EstrategiaBinPackTree ebpt, boolean desmontaBinCompleta,
                                                                            PoliticaSelecao polSelecao, float desperdicio) {

        boolean isGrasp = false;
        IBPTNode noDesmonte     = null, noDesmonte2  = null;
        LinkedList listPecas    = new LinkedList(), ListaPecasT   = new LinkedList();
        LinkedList listPecas2   = new LinkedList(), ListaPecasT2  = new LinkedList();
        LinkedList listCortes   = new LinkedList(), ListaCortesT  = new LinkedList();
        LinkedList listCortes2  = new LinkedList(), ListaCortesT2 = new LinkedList();
        LinkedList listaSobras  = new LinkedList(), ListaSobrasT  = new LinkedList();
        LinkedList listaSobras2 = new LinkedList(), ListaSobrasT2 = new LinkedList();
        
        LinkedList<IBPTNode> list_INode = new LinkedList<IBPTNode>();
        
        //Solução temporária que armazena a solução completa
        SolucaoHeuristica solutionNova = new SolucaoHeuristica();
        SolucaoHeuristica solution = new SolucaoHeuristica(solHeu); //solucao que irá receber !
        
        HeuristicaConstrutivaInicial.FuncoesGrasp.ordenaSolucaoDecrescente(solHeu);
        
        ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> vetor_arvore = new ArrayList<ArrayList<IBPTNode>>();
        Queue<HHDBinPackingTree.IBPTNode> fila = null, fila2 = null;
        
        if((solHeu.getTipoHeuristic() == ETipoHeuristicas.GRASP_2D_CONSTRUTIVO) ||
                                       (solHeu.getTipoHeuristic() == ETipoHeuristicas.Grasp2d_Melhoria)){
             isGrasp = true;
        }
        else{
             isGrasp = false;
        }
        
        System.out.println("Tipo da heurística --> "+solHeu.getTipoHeuristic());    
                
        IGenericSolution bbptf = SolutionConversorFactory.getInstance().newConversor().converteParaSolucao(solHeu/*, isGrasp*/);
//        ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> solucao 
//                                    = HHDHeuristic.VerificaImprimeArvoreDeCorte((BinPackTreeForest) bbptf);
        
        Iterator<BinPackTree> iterator = ((BinPackTreeForest)bbptf).getListaBinPackTrees().iterator();
        
        int c = 0;
        
        while(iterator.hasNext()){
        
            BinPackTree bptree = iterator.next();

            ListaPecasT2.addAll(bptree.getListaPecas());   //ListaPecasT2,ListaCortesT2 e ListaSobrasT2 armazenam de todas as trees;
            ListaCortesT2.addAll(bptree.getListaCortes());
            ListaSobrasT2.addAll(bptree.getListaSobras());
            
            vetor_arvore.add(Utilidades.Funcoes.retornaSequenciaBlarguraBinPacking(bptree.getRaiz(), fila));
            
            //Após adicionar o array de Nós calcula-se o desperdício !
            ArrayList<IBPTNode> array = vetor_arvore.get(c);
            for (int vet = (array.size() - 1); vet >= 0; vet--) {

                BPTPedaco.CalculaDesperdicioAproveitamento(((BPTPedaco) array.get(vet)));
            }
            fila = null;
            c++;
        }
        ((BinPackTreeForest)bbptf).calculaFAVFAV2((BinPackTreeForest) bbptf, vetor_arvore); //ver se calcula dentro da conversao
        
        if ((ebpt == EstrategiaBinPackTree.OneSolutionHHD) ||
                                                       (ebpt == EstrategiaBinPackTree.TwoSolutionsHHD)) {

            for (int i = 0; i < vetor_arvore.size(); i++) {
                    
                  IBPTNode ibptNode = vetor_arvore.get(i).get(0); //[Nó raiz]
                  System.out.println("\nDesperdício do nó --> " + ((BPTPedaco) ibptNode).getDesperdicio());
                  System.out.println("Desperdício informado --> " + (desperdicio * 100));

                  if (((BPTPedaco) ibptNode).getDesperdicio() >= (desperdicio * 100)) {

                        noDesmonte = ibptNode;
                        list_INode.add(noDesmonte);
                        //break;
                  }
            }
            //Caso nó desmonte saia igual a nulo tratar
            if (list_INode.isEmpty()) {

                 System.out.println("Não foi encontrado um nó com desperdício igual ou acima do valor especificado !\n");

                 return null;
            }
            else{
                
                System.out.println("Quantidade de árvore a desmontar ... "+list_INode.size());
                //Aqui será percorrido cada árvore que será desmontada e construirá a lista de peças, sobras e cortes;
                Iterator<IBPTNode>  iterNode = list_INode.iterator();
                
                while(iterNode.hasNext()){
                
                    IBPTNode ibn = iterNode.next();
                    BinPackTree bin = ibn.getArvore();
                    
                    //Aqui são selecionados todas as lista com informações de toda a placa e a partir de um subnó !
                    ListaPecasT = bin.getListaPecas();
                    ListaCortesT = bin.getListaCortes();
                    ListaSobrasT = bin.getListaSobras();
                    
                    listPecas.addAll(ListaPecasT);    //listPecas,listCortes,listSobras armazenam das árvores que vão ser removidas
                    listCortes.addAll(ListaCortesT);
                    listaSobras.addAll(ListaSobrasT);
                    
                    ((BinPackTreeForest) bbptf).removaPlanoDeCorte(bin);
                }
                
                //Aqui eu elimino as sobras e cortes das bins removidas
                ListaPecasT2.removeAll(listPecas);  //Atualiza a lista de todas menos as que foram removidas
                ListaCortesT2.removeAll(listCortes);
                ListaSobrasT2.removeAll(listaSobras);
            
                System.out.println("## Numero de peças para solução parcial --> "+listPecas.size());
                System.out.println("## Numero de sobras para solução parcial --> "+ListaSobrasT2.size());
                
                /*return (BinPackTreeForest)*/ solucao_Parcial(bbptf, listPecas, ListaSobrasT2, tamanho);
                return (BinPackTreeForest) bbptf;
           }

        }
        
        return null;  //Pode dá erro no futuro caso todas as execeções possíveis não seja tratada 
    }
    
    

    /***************************************** MÉTODO RUN PARA EXECUTAR A THREAD  *****************************************/
    @Override
    public void run() { //Run do HHDHEURISTIC


        System.out.println("Iniciei a thread do HHDHeuristic ...");
        //Agora o método o run que será executado pela thread auxiliar.....
        //Aqui dependendo do algoritmo ele enviará uma mensagem de escrita para o servidor dizendo a sua ação

        //ObjetoComunicacaoMelhorado obcom = getObjetoComunicacaoMelhorado();
        ObjetoComunicacaoMelhorado obcom = getDeveEnviar();

        //Ele escreve uma requisição para o Atendente !!!
        if(obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Inicializacao_HHDHeuristic){

            System.out.println("Connect 0...");

            try {
                open("localhost", getPortaComunicacao());
                System.out.println("Connected !!!");
//              socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
                ObjetoComunicacaoMelhorado ob = receive();
            }
            catch (Exception e) {
                System.out.println("Ocorreu alguma exceção !");
            }

            System.out.println("Entrando o laço: " +executando);
            
            while (executando) {

                obcom.setMSGServicoAgente("Terminei_Envio");

//                System.out.println("Vou enviar um objeto HHDheuristic");
                send(obcom);

                //Leitura de informações envidas pelo servidor !!!
                System.out.println("Vou receber um objeto");

                obcom = receive();
                String mensagem = obcom.getMSGServicoAgente();

                if (mensagem == null) {
                    System.out.println("Mensagem nula !");
                    break;
                }
                if ("Terminei".equals(mensagem)) {

                    System.out.println("Servidor recebeu todas as soluções -- HHDHeuristic !");
                    break;
                }
                System.out.println("Mensagem enviada pelo servidor: " + mensagem);

            }

            close();

        }

        if ((obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao)
                                    || (obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao)) {

            ObjetoComunicacaoMelhorado ob_atual = new ObjetoComunicacaoMelhorado();
            ob_atual.setTipoServicoAgente(obcom.getTipoServicoAgente2());
            ob_atual.setTipoServicoServidor(obcom.getTipoServicoServidor());

            while (!IniciaClientes.stop) {
                try {
                    Thread.sleep(300);//500
                }
                catch (InterruptedException ex) {}
                
                obcom.setTipoServicoAgente(ob_atual.getTipoServicoAgente2());
                obcom.setTipoServicoServidor(ob_atual.getTipoServicoServidor());
                                
                try {
                    
                    System.out.println("Connect 1..."+this.toString());
                    
                    open("localhost", getPortaComunicacao());
                    
                    System.out.println("Connect 2...");
                    //socketClientFinal.setSoTimeout(2500);//Definição de timeout para operação de leitura e a leitura da mensagem enviada pelo servidor !
                    ObjetoComunicacaoMelhorado ob = receive();
                    System.out.println("Connect 3...");
                    
                    String msg = ob.getMSGServicoAgente();
                    
                    System.out.println("Ocupado?: "+msg);
                    
                    if (msg.equals("true")) {

                        System.out.println("Espere para reiniciar..");
                        
                        close();
                        continue;
                    }

                    System.out.println("Entrei no consulta HHDHeuristc Melhoria");

                    while (executando) {

                        if ((obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Consulta_Solucao)) {

                            //Ele escreve uma requisição para o Atendente !!!
                            obcom.setMSGServicoAgente("BuscaSolucao");
//                            System.out.println("Vou enviar um objeto de solicitação");
                            send(obcom);

                            //Leitura de informações envidas pelo servidor !!!
//                            System.out.println("Vou receber o objeto solicitado");
                            obcom = receive();
                            String mensagem = obcom.getMSGServicoAgente();

                            if ("SolucaoPronta".equals(mensagem)) {

                                //Aqui chama o processa informação do Agente Grasp
//                                System.out.println("Vou iniciar o HHD Melhoria");
                                SolucaoHeuristica solucao = null;  //dfksçdkfçsfkdsçlkfsçldkfçsldkfçlsdkfçlskdçlfkdsçlfkdsçlfkdsçl
                                        
                                if(this.getHHDType() == 1){
                                    
                                    solucao = this.criaSolucaoHHDMelhoria(obcom.getSolucao(), getPoliticaSelecaoPlacas());
                                }
                                if(this.getHHDType() == 2){
                                
                                    solucao = this.criaSolucaoHHDMelhoria2Version(obcom.getSolucao(), getAprovt());
                                }
                                if(this.getHHDType() == 3){
                                    
                                    solucao = obcom.getSolucao();
                                    BinPackTreeForest bptf = null;
                                    
                                    if((getEstrategiaTree() == EstrategiaBinPackTree.OneSolutionHHD) ||
                                                                                        (getEstrategiaTree() == EstrategiaBinPackTree.OneSolutionGRASP)){
                                    
                                        bptf = this.HHDHeuristicMonteDesmonte2Version(obcom.getSolucao(), solucao.getTamanhoChapa(), 
                                                                    getEstrategiaTree(), false, getPoliticaSelecaoSolucao(), getAprovNivel());
                                    }
                                    if((getEstrategiaTree() == EstrategiaBinPackTree.TwoSolutionsHHD)||
                                                                                        (getEstrategiaTree() == EstrategiaBinPackTree.TwoSolutionsGRASP)){
                                    
                                        bptf = this.HHDHeuristicMonteDesmonte2Version(obcom.getSolucao(), solucao.getTamanhoChapa(), 
                                                                    getEstrategiaTree(), false, getPoliticaSelecaoPlacas(), getAprovNivel());
                                    }
                                    
                                    obcom.setTreeSolucao(bptf);
                                    obcom.setMSGServicoAgente("Vou adicionar a nova solução em uma lista reservada !");
                                    
                                }
                                if(this.getHHDType() == 4){
                                    
                                    solucao = obcom.getSolucao();
                                    BinPackTreeForest bptf = null;
                                    
                                    if((getEstrategiaTree() == EstrategiaBinPackTree.OneSolutionHHD) ||
                                                                    (getEstrategiaTree() == EstrategiaBinPackTree.OneSolutionGRASP)){
                                    
                                        bptf = this.HHDHeuristicMonteDesmonte1(obcom.getSolucao(), solucao.getTamanhoChapa(), 
                                                  getEstrategiaTree(), false, getPoliticaSelecaoSolucao(),getAprovt(), getAprovNivel());
                                    }
                                    if((getEstrategiaTree() == EstrategiaBinPackTree.TwoSolutionsHHD)||
                                                                    (getEstrategiaTree() == EstrategiaBinPackTree.TwoSolutionsGRASP)){
                                    
                                        bptf = this.HHDHeuristicMonteDesmonte1(obcom.getSolucao(), solucao.getTamanhoChapa(), 
                                                  getEstrategiaTree(), false, getPoliticaSelecaoPlacas(),getAprovt(), getAprovNivel());
                                    }
                                    
                                    obcom.setTreeSolucao(bptf);
                                    obcom.setMSGServicoAgente("Vou adicionar a nova solução em uma lista reservada !");
                                    
                                }
                                 
                                 if(this.getHHDType() == 1 ||this.getHHDType() == 2){                                                             
                                
                                    solucao.setTipoHeuristic(ETipoHeuristicas.HHDHeuristic_Melhoria);
                                    obcom.setSolucao(solucao);
                                    obcom.setMSGServicoAgente("Vou escrever a nova solução");
                                }
                                                                                                
                                System.out.println("Terminei o HHDHeuristic Melhoria e setei a solução");
                                
                                obcom.setTipoServicoAgente(ETiposServicosAgentes.Escrever_Solucacao);
                                obcom.setTipoServicoServidor(ETiposServicosServidor.AtualizaSolucao);
                            }
                            
                            if ("Terminei".equals(mensagem)) {
                                System.out.println("Servidor retornou a solução adequada !");
                                break;
                            }
                            if (mensagem == null) {
                                System.out.println("Mensagem nula !");
                                break;
                            }
                            System.out.println("Mensagem enviada pelo servidor: " + mensagem);
                        }
                        if ((obcom.getTipoServicoAgente2() == ETiposServicosAgentes.Escrever_Solucacao)) {

                            System.out.println("Vou enviar um objeto");
                            send(obcom);
                            System.out.println("Aguardando confirmação do servidor");
                            obcom = receive();
                            String mensagem = obcom.getMSGServicoAgente();

                            if ("Terminei".equals(mensagem)) {

                                System.out.println("Servidor finalizou por completo a operação !");
                                break;
                            }
                            if (mensagem == null) {
                                System.out.println("Mensagem nula !");
                                break;
                            }
                        }
                    }
                    
                    close();  //Antes de encerrar devemos liberar todos os recursos !!!

                } catch (Exception e) {
                    System.out.println("Ocorreu alguma exceção !");
                    e.printStackTrace();
                    close();  //Antes de encerrar devemos liberar todos os recursos !!!
                }
            }
        }
    }

    /***************************************************************************************************************************/
    /*************************************** MÉTODO MAIN DO ALGORITMO HHDHEURISTIC *********************************************/
    public static void main(String args[]) throws FileNotFoundException, IOException, PecaInvalidaException, Exception {

        int porta, typeHD;
        float aproveitamentoNivel;
        float aproveitamentoPlaca;
        String name = " ";
        ServicoAgente tipo_agente = null;
        ETipoHeuristicas tipo_Heuristica = null;
        ETiposServicosAgentes operacao_agente = null;
        ETiposServicosServidor operacao_servidor = null;
        EstrategiaBinPackTree  estrategiaTree    = null;
        PoliticaSelecao politicaSelecaoSol = null, politicaSelecaoPlac = null;


        if (args.length < 11) {
            System.out.println("Está faltando argumentos !");
            System.exit(1);
        }
        /*##########################  CONFIGURANDO ARGUMENTOS DO ALGORITMO HHDHEURISTIC #########################################*/
        switch (Integer.parseInt(args[0])) {

            case 1:
                tipo_Heuristica = ETipoHeuristicas.HHDHeuristic;
                name = "HHDHeuristic";
                break;
            case 2:
                tipo_Heuristica = ETipoHeuristicas.HHDHeuristic_Melhoria;
                name = "HHDHeuristic_Melhoria";
                break;
            case 3:
                tipo_Heuristica = ETipoHeuristicas.HHDHeuristic_Melhoria_Tree;
                name = "HHDHeuristic_Melhoria_Tree";
                break;
            default:
                System.out.println("Nenhuma das opções são válidas para o algoritmo HHDHEURISTIC !");
                System.exit(1);
        }
        switch (Integer.parseInt(args[1])) {

            case 1:
                tipo_agente = ServicoAgente.Inicializacao;
                break;
            case 2:
                tipo_agente = ServicoAgente.HHDHeuristic_Melhoria;
                break;
            default:
                System.out.println("Opção Inválida para o Serviço do Agente");
                System.exit(0);
        }
        switch (Integer.parseInt(args[2])) {

            case 1:
                operacao_agente = ETiposServicosAgentes.Inicializacao_HHDHeuristic;
                break;
            case 2:
                operacao_agente = ETiposServicosAgentes.Consulta_Solucao;
                break;
            case 3:
                operacao_agente = ETiposServicosAgentes.Escrever_Solucacao;
                break;
            default:
                System.out.println("Opção Inválida para o Tipo de Serviço do Agente");
                System.exit(0);
        }
        switch (Integer.parseInt(args[3])) {

            case 1:
                operacao_servidor = ETiposServicosServidor.Inserir_Solucao;
                break;
            case 2:
                operacao_servidor = ETiposServicosServidor.AtualizaSolucao;
                break;
            case 3:
                operacao_servidor = ETiposServicosServidor.MelhorSolucao;
                break;
            case 4:
                operacao_servidor = ETiposServicosServidor.PiorSolucao;
                break;
            case 5:
                operacao_servidor = ETiposServicosServidor.Solucao_Aleatoria;
                break;
            case 6:
                operacao_servidor = ETiposServicosServidor.Roleta;
                break;
            default:
                System.out.println("Opção Inválida para o serviço do Servidor");
                System.exit(0);
        }

        porta = Integer.parseInt(args[4]);

        PoliticaSelecao polSelecaoSolucao = null, polSelecaoPlacas = null;

        switch (Integer.parseInt(args[5])) {
            case 0:
                polSelecaoSolucao = null;
                break;
            case 1:
                polSelecaoSolucao = PoliticaSelecao.Melhor;
                break;
            case 2:
                polSelecaoSolucao = PoliticaSelecao.ProbabilidadeMelhor;
                break;
            case 3:
                polSelecaoSolucao = PoliticaSelecao.Aleatorio;
                break;
            case 4:
                polSelecaoSolucao = PoliticaSelecao.Pior;
                break;
            case 5:
                polSelecaoSolucao = PoliticaSelecao.ProbabilidadeMenor;
                break;
            default:
                System.out.println("Nenhuma opção para Política de Seleção de Solução");
        }
        switch (Integer.parseInt(args[6])) {
            case 0:
                polSelecaoPlacas = null;
                break;
            case 1:
                polSelecaoPlacas = PoliticaSelecao.MelhorMelhor;
                break;
            case 2:
                polSelecaoPlacas = PoliticaSelecao.MelhorPior;
                break;
            case 3:
                polSelecaoPlacas = PoliticaSelecao.MelhorAleatorio;
                break;
            case 4:
                polSelecaoPlacas = PoliticaSelecao.PiorAleatorio;
                break;
            case 5:
                polSelecaoPlacas = PoliticaSelecao.PiorPior;
                break;
            default:
                System.out.println("Nenhuma opção para Política de  Seleção de Placas");
        }

        aproveitamentoNivel = Float.parseFloat(args[7]);

        aproveitamentoPlaca = Float.parseFloat(args[8]);
        
        typeHD = Integer.parseInt(args[9]);
        
        switch (Integer.parseInt(args[10])) {
            case 0:
                estrategiaTree = null;
                break;
            case 1:
                estrategiaTree = EstrategiaBinPackTree.OneSolutionHHD;
                break;
            case 2:
                estrategiaTree = EstrategiaBinPackTree.TwoSolutionsHHD;
                break;
            case 3:
                estrategiaTree = EstrategiaBinPackTree.OneSolutionGRASP;
                break;
            case 4:
                estrategiaTree = EstrategiaBinPackTree.TwoSolutionsGRASP;
                break;
            default:
                System.out.println("Nenhuma opção para Política de  Seleção de Placas");
        }
        
        ObjetoComunicacaoMelhorado obj = new ObjetoComunicacaoMelhorado();
        obj.setTipoServicoAgente(operacao_agente);
        obj.setTipoServicoServidor(operacao_servidor);


        /*##########################    ORGANIZAÇÃO E INÍCIO DO ALGORITMO HHDHEURISTIC  #########################################*/

        if (tipo_Heuristica == ETipoHeuristicas.HHDHeuristic) {


            LinkedList<Pedidos> list_pedidos = new LinkedList<Pedidos>();


            SolucaoHeuristica solucao;
            System.out.println("Iniciando cliente ... ");
            System.out.println("Iniciando execução do algoritmo !");
            System.out.println("Iniciando conexão com o servidor heuristic... ");

            HHDHeuristic heuristca1 = new HHDHeuristic();

            heuristca1.setName("HHDHeuristic_Construtivo");
            heuristca1.setPortaComunicacao(porta);
            heuristca1.setServicoAgente(tipo_agente);
            heuristca1.setTipoServico(operacao_agente);
            heuristca1.setTipoHeuristica(tipo_Heuristica);
            heuristca1.setTipoServicoServidor(operacao_servidor);
            heuristca1.setAprovt(aproveitamentoPlaca);
            heuristca1.setHHDType(typeHD);
//                heuristca1.open("localhost", porta);

            System.out.println("Conexão estabelecida com sucesso !!! ");

            list_pedidos = FuncoesHDInternal.lerArquivo();
            Iterator<Pedidos> iterator = list_pedidos.iterator();

            System.out.println("\n\nLista de Pedidos  \n");

            while (iterator.hasNext()) {

                Pedidos pedido = iterator.next();
                System.out.println("Id -- " + pedido.id() + "\t " + pedido.retorneDimensao().retorneBase()
                        + "  x  " + pedido.retorneDimensao().retorneAltura());
            }

            solucao = heuristca1.criaSolucao(list_pedidos, FuncoesHDInternal.getChapa());
            SolucaoHeuristica solucao_t = new SolucaoHeuristica(solucao);
            solucao.setTipoHeuristic(ETipoHeuristicas.HHDHeuristic);
            obj.setSolucao(solucao);

            heuristca1.setObjetoComunicacaoMelhorado(obj);
            heuristca1.deveEnviar(obj);

            //Iniciando a thread auxilair !!!
            System.out.println("Vou iniciar o HHDHeuristic run() ...");
            heuristca1.start_run();

            System.out.println("Quantidade de bins encontradas --> " + solucao.getQtd());

            int c = 0;
            LinkedList<Peca> pecas = new LinkedList<Peca>();

            while (c < solucao.getQtd()) {

                Bin bin_atual = solucao.retornePlanoDeCorte(c);

                pecas = bin_atual.getListaPecas();

                System.out.println("\n### Bin " + (++c) + " ###");
                System.out.println("Lista Peças --> " + bin_atual.getListaPecas().size());
                System.out.println("Lista de Sobras --> " + bin_atual.getListaSobras().size());
                System.out.println("Lista de Cortes --> " + bin_atual.getListaCortes().size());

                Iterator<Peca> iterato = pecas.iterator();

                while (iterato.hasNext()) {

                    Peca peca_atual = iterato.next();
                    IPedido pedidoAtendido = peca_atual.getPedidoAtendido();

                    System.out.println("\n -- Peça -- ");
                    System.out.println("Peça ID --> " + pedidoAtendido.id());
                    System.out.println("Ponto InfEQ --> (" + peca_atual.getPontoInfEsq().getX() + ", "
                            + peca_atual.getPontoInfEsq().getY() + ")");
                    System.out.println("Ponto SupDi --> (" + peca_atual.getPontoSupDir().getX() + ", "
                            + peca_atual.getPontoSupDir().getY() + ")\n\n");
                }
            }

        }
        if (tipo_Heuristica == ETipoHeuristicas.HHDHeuristic_Melhoria) {

            System.out.println("\n############### Vou realizar um teste com o HHDHeuristic Melhoria ######################### \n");

            SolucaoHeuristica solucao;
            System.out.println("Iniciando cliente ... ");
            System.out.println("Iniciando execução do algoritmo !");
            System.out.println("Iniciando conexão com o servidor heuristic... ");

            HHDHeuristic heuristca1 = new HHDHeuristic();

            heuristca1.setName("HHDHeuristic_Melhoria");
            heuristca1.setPortaComunicacao(porta);
            heuristca1.setServicoAgente(tipo_agente);
            heuristca1.setTipoServico(operacao_agente);
            heuristca1.setTipoHeuristica(tipo_Heuristica);
            heuristca1.setTipoServicoServidor(operacao_servidor);
//                heuristca1.open("localhost", porta);

            heuristca1.setPoliticaSelecaoSolucao(polSelecaoSolucao);
            heuristca1.setPoliticaSelecaoPlacas(polSelecaoPlacas);
            heuristca1.setAprovt(aproveitamentoPlaca);
            heuristca1.setHHDType(typeHD);

            heuristca1.setObjetoComunicacaoMelhorado(obj);
            heuristca1.deveEnviar(obj);

            heuristca1.start();

            System.out.println("\n############# FINALIZANDO O ALGORITMO CORRETAMENTE MELHORIA ############################\n");

        }

        if (tipo_Heuristica == ETipoHeuristicas.HHDHeuristic_Melhoria_Tree) {

            System.out.println("\n############### Vou realizar um teste com o HHDHeuristic Melhoria Árvore ####################### \n");

            SolucaoHeuristica solucao;
            System.out.println("Iniciando cliente ... ");
            System.out.println("Iniciando execução do algoritmo !");
            System.out.println("Iniciando conexão com o servidor heuristic... ");
            
            
            HHDHeuristic heuristca1 = new HHDHeuristic();

            heuristca1.setName("HHDHeuristic_Melhoria");
            heuristca1.setPortaComunicacao(porta);
            heuristca1.setServicoAgente(tipo_agente);
            heuristca1.setTipoServico(operacao_agente);
            heuristca1.setTipoHeuristica(tipo_Heuristica);
            heuristca1.setTipoServicoServidor(operacao_servidor);
//                heuristca1.open("localhost", porta);

            heuristca1.setPoliticaSelecaoSolucao(polSelecaoSolucao);
            heuristca1.setPoliticaSelecaoPlacas(polSelecaoPlacas);
            heuristca1.setAprovt(aproveitamentoPlaca);
            heuristca1.setAprovNivel(aproveitamentoNivel);
            heuristca1.setHHDType(typeHD);
            heuristca1.setEstrategiaTree(estrategiaTree);

            heuristca1.setObjetoComunicacaoMelhorado(obj);
            heuristca1.deveEnviar(obj);

            heuristca1.start();

            System.out.println("\n######## FINALIZANDO O ALGORITMO MELHORIA SOLUCAO PARCIAL CORRETAMENTE  ######################\n");           
        }
    }

    @Override
    public void solucaoParcial(LinkedList listaPecas, LinkedList listaSobras, IDimensao2d tamanhoChapa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

}