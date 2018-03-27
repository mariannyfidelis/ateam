package HHDInternal;

import java.util.List;
import Heuristicas.Util;
import HHDInterfaces.IBin;
import Utilidades.Funcoes;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.Serializable;
import Heuristicas.Individuo;
import HHDInterfaces.IPedido;
import HHDInterfaces.ISolution;
import ATeam.ETipoHeuristicas;
import HHDInterfaces.IDimensao2d;
import HHDBinPackingTree.BPTPedaco;
import HHDBinPackingTree.BinPackTree;
import HHDBinPackingTree.BinPackCorte;
import HHDInterfaces.IGenericSolution;
import HeuristicaConstrutivaInicial.Bin;
import HHDBinPackingTree.BinPackTreeForest;
import HHD_Exception.PecaInvalidaException;
import Utilidades.HistoriaSolucao;

public class SolucaoHeuristica implements ISolution, Comparable<SolucaoHeuristica>, Serializable{

    private boolean capacidade_respeitada;
    private LinkedList listaBins;
    private LinkedList pedidoAtendido;     
    private IDimensao2d  tamanhoChapa;
    private Double FAV;  //Fitness 1
    private Double Fav;
    private Double FAV2; //Fitness 2
    private Double Fav_2;
    private int numLinhasCorte;
    private Double mediaSobras;
    private Double somatorioSobras;
    private ETipoHeuristicas etipoHeuristica;
    private Individuo vetor_sequencia_ind;
    private LinkedList<HistoriaSolucao> historiaSol;
    /*ESSES CONTRUTORES SÃO ÚTEIS AO HHDHEURISTIC*/
    
    //public SolucaoHeuristica(IListaPedidos pedido, IDimensao2d tamanho){
    public SolucaoHeuristica(LinkedList<Pedidos> pedido, IDimensao2d tamanho){
        
        pedidoAtendido = pedido;
        listaBins = new LinkedList();
        tamanhoChapa = tamanho;
        this.FAV = 0.0;
        this.Fav = 0.0;
        this.FAV2 = 0.0;
        this.numLinhasCorte = 0;
        this.mediaSobras = null;
        this.somatorioSobras = null;
        historiaSol = new LinkedList<HistoriaSolucao>();
        //this.vetor_sequencia_ind = new Individuo();
    }
    
    //Aqui simula a SolucaoHeuristica do GRASP Construtiva Inicial
    public SolucaoHeuristica(IDimensao2d tamanho, LinkedList<IPedido> pedido){
        
        pedidoAtendido = pedido;
        listaBins = new LinkedList();
        tamanhoChapa = tamanho;
        historiaSol = new LinkedList<HistoriaSolucao>();
    }

    /*AQUI SERÃO OS CONSTRUTORES DA SOLUÇÃO ÚTIL AO GRASP-CONSTRUTIVO E GRASP-MELHORIA*/
    public SolucaoHeuristica(){
        
        this.capacidade_respeitada = true;
        this.listaBins =  new LinkedList<Bin>();
        this.FAV = 0.0;
        this.Fav = 0.0;
        this.FAV2 = 0.0;
        this.pedidoAtendido = new LinkedList();
        this.tamanhoChapa = new Dimensao2D(0, 0);
        
        this.numLinhasCorte = 0;
        this.mediaSobras = null;
        this.somatorioSobras = null;
        historiaSol = new LinkedList<HistoriaSolucao>();
        //this.vetor_sequencia_ind = new Individuo();
    }
    
    public SolucaoHeuristica(SolucaoHeuristica solucao){
        
        this.capacidade_respeitada = true;
        this.listaBins = new LinkedList<Bin>(solucao.getObjetos());
        this.FAV  = solucao.getFAV();
        this.Fav  = solucao.get_Fav();
        this.FAV2 = solucao.getFAV2();
        this.pedidoAtendido  = new LinkedList(solucao.getIPedidos());
        this.tamanhoChapa    = solucao.getTamanhoChapa();
        this.numLinhasCorte  = solucao.getQtdLinhasCorte();
        this.mediaSobras     = solucao.getMediaSobras();
        this.somatorioSobras = solucao.getSomatorioSobras();
        this.etipoHeuristica = solucao.getTipoHeuristic();
        this.vetor_sequencia_ind = solucao.getIndividuo();
        //historiaSol = new LinkedList<HistoriaSolucao>(solucao.getHistoriaSol());
        
    }

    public SolucaoHeuristica(List<Bin> Objetos, Double FAV, Double FAV2){
        
        this.capacidade_respeitada = true;
        this.listaBins = new LinkedList<Bin>(Objetos);
        this.FAV = FAV;
        this.FAV2 = FAV2;
        historiaSol = new LinkedList<HistoriaSolucao>();
    }
    
    public SolucaoHeuristica(IGenericSolution solution) throws PecaInvalidaException{
    
        this.capacidade_respeitada = true;
        this.FAV  = new Double(((BinPackTreeForest)solution).getFAV());
        this.FAV2 = new Double(((BinPackTreeForest)solution).getFAV2());
        this.Fav  = new Double(((BinPackTreeForest)solution).getFav());
        this.mediaSobras = new Double(((BinPackTreeForest)solution).getMediaSobras());
        
        this.vetor_sequencia_ind = new Individuo(); 
        
        this.listaBins = new LinkedList<Bin>();
        this.pedidoAtendido  = new LinkedList();
        this.numLinhasCorte  = ((BinPackTreeForest)solution).getQtdLinhasCorte();
        this.somatorioSobras = ((BinPackTreeForest)solution).getSomatorioSobras();
        this.tamanhoChapa    = ((BinPackTreeForest)solution).getTamanhoChapa();
        this.etipoHeuristica = ETipoHeuristicas.HHDHeuristic_Melhoria_Tree;
        
        int quantidadeArvores = ((BinPackTreeForest)solution).getQtd();
        BinPackTree binPackTree;
        IDimensao2d tamanhoChapa_ = ((BinPackTreeForest)solution).getTamanhoChapa();
        
        for (int i = 0; i < quantidadeArvores; i++) {
        
            Bin bin = new Bin(tamanhoChapa_.retorneBase(), tamanhoChapa_.retorneAltura());
            binPackTree = (BinPackTree) solution.retornePlanoDeCorte(i);
            
            bin.setFav(binPackTree.getFav());
            bin.setAproveitamento(binPackTree.getAproVeitamento());
            bin.setSobra(binPackTree.getSobra());
            
            Iterator<BinPackCorte> itBinpackTree = binPackTree.getListaCortes().iterator();
            int cont = 0;
            while (itBinpackTree.hasNext()) {
                
                BinPackCorte b = itBinpackTree.next();
                
                Corte corte = new Corte(b.getPosicaoCorte(), b.getPontoChapaCortada(), b.eVertical(), b.getTamanho(), cont+1);
                bin.adicioneCorte(corte);

                if (b.eVertical()) {} else {}

               cont++; 
            }
            
            cont = 0;
            Iterator<BPTPedaco> iterPecas = binPackTree.getListaPecas().iterator();

            while (iterPecas.hasNext()) {

                BPTPedaco bpt = iterPecas.next();

                IPedido ipedido = bpt.getPedidoAtendido();
                this.pedidoAtendido.add(ipedido);
                bin.getIndividuo().adicionaItemLista(ipedido.id(), ipedido.retorneDimensao().retorneArea());
                
                
                Peca peca = new Peca(new PedacoDisponivel(bpt.getPontoInfEsq(), bpt.getPontoSupDir(), cont+1),
                                                                            bpt.getPedidoAtendido(), true, false);
                 
                peca.setPosicao(bpt.getPontoInfEsq());
                bin.adicionePeca(peca);
//                System.out.println("\nDimensões Peças- (Largura x Altura) --> (" + bpt.retorneBase() + " x " + bpt.retorneAltura() + " )");
//                System.out.println("PInfEsq (" + bpt.getPontoInfEsq().getX() + " , " + bpt.getPontoInfEsq().getY() + " )");
//                System.out.println("PSupDir (" + bpt.getPontoSupDir().getX() + " , " + bpt.getPontoSupDir().getY() + " )");
                cont++;
            }
            
            cont = 0;
            Iterator<BPTPedaco> iterSobras = binPackTree.getListaSobras().iterator();

            while (iterSobras.hasNext()) {

                BPTPedaco bpt = iterSobras.next();

                PedacoDisponivel sobra = new PedacoDisponivel(bpt.getPontoInfEsq(), bpt.getPontoSupDir(), cont+1);
                bin.adicionarSobra(sobra);
                
//                System.out.println("\nDimensões Sobras - (Largura x Altura) --> (" + bpt.retorneBase() + " x " + bpt.retorneAltura() + " )");
//                System.out.println("PInfEsq (" + bpt.getPontoInfEsq().getX() + " , " + bpt.getPontoInfEsq().getY() + " )");
//                System.out.println("PSupDir (" + bpt.getPontoSupDir().getX() + " , " + bpt.getPontoSupDir().getY() + " )");
                cont++;
            }
            
            listaBins.add(bin);
            this.atualizaIndividuoSolucao(bin.getIndividuo());
        }
        this.calculaLinhasDeCorteEMediaSobras();//Atualizei na segunda 09 de junho
        
    }
    @Override
    public int getQtd(){
            
        return listaBins.size();
    }

    public void adicionarPlanoDeCorte(Bin plano){

        listaBins.add(plano);
    }
	
    @Override
    public IDimensao2d getTamanhoChapa() {
        
        return tamanhoChapa;
    }
    
    public void setTamanhoChapa(IDimensao2d tamanho){
    
        this.tamanhoChapa = tamanho;
    }
    
    @Override
    public Bin retornePlanoDeCorte(int indice){

        if(indice < listaBins.size() && indice >= 0)
             return (Bin) listaBins.get(indice);

        return null;
    }

    public void calculaFAV(){
    
        FAV = this.get_Fav();
        int j = this.getQtd();
        float w = this.tamanhoChapa.retorneBase(), h = this.tamanhoChapa.retorneAltura();
        
        FAV = FAV/(j*w*h);
        
        this.setFAV(FAV);
    }
    
    public Double get_Fav(){
    
        return this.Fav;
    }
    public void set_Fav(Double fav){
    
        this.Fav = fav;
    }
    
    public Double getFAV() {
        
        return FAV;
    }

    public void setFAV(Double fAV) {
        
        FAV = fAV;
    }

    public Double getFAV2() {
        
        return FAV2;
    }

    public void setFAV2(Double fAV2) {
        
        FAV2 = fAV2;
    }
    
    public List<Bin> getObjetos() {

        return listaBins;
    }

    public void setObjetos(List<Bin> objetos) {
        
        listaBins = new LinkedList<Bin>(objetos);
    }
    
    public void set_Objetos(List<Bin> objetos) {
        
        getObjetos().addAll(objetos);
        //SlistaBins = (ArrayList) objetos;
    }
    
    public LinkedList getIPedidos(){

        return pedidoAtendido;
    }
    
    public void setIPedidos(LinkedList pedidosAtendido){
    
        this.pedidoAtendido = pedidosAtendido;
    }
    
    public void setLinkedIPedidos(LinkedList IPedidos){
    
        pedidoAtendido = new LinkedList(IPedidos);
    }
    
    public j_HeuristicaArvoreNAria.Pedidos retornaPedido(int i, LinkedList pedidosAtendido){
    
        Iterator a = pedidosAtendido.iterator();
        while(a.hasNext()){
            System.out.println("Pedido deusss--> "+a.next());
        }
        IPedido p;
        j_HeuristicaArvoreNAria.Pedidos ped;
        //Iterator<j_HeuristicaArvoreNAria.Pedidos> itPedidosArv = pedidosAtendido.iterator();
        Iterator<IPedido> iterPedidosArv = pedidosAtendido.iterator();
        while(iterPedidosArv.hasNext()){
            System.out.println("Estou entrando no whiiiiiilleeeee");
            p = iterPedidosArv.next();
            
            if(p.id() == i){
            
                ped = new j_HeuristicaArvoreNAria.Pedidos(p.id(), p.retorneDimensao().retorneBase(), 
                                                                  p.retorneDimensao().retorneAltura());
                return ped;
            }        
        }
        
        return null;
    }
    
    public void setSolucao(SolucaoHeuristica solucao){
    
        this.listaBins = (LinkedList<Bin>) solucao.getObjetos();
        this.tamanhoChapa = solucao.getTamanhoChapa();
        this.pedidoAtendido = solucao.getIPedidos();
        this.FAV = solucao.getFAV();
        this.Fav = solucao.get_Fav();
        this.FAV2 = solucao.getFAV2();
        this.vetor_sequencia_ind = solucao.getIndividuo();
        this.somatorioSobras = solucao.getSomatorioSobras();
        this.etipoHeuristica = solucao.getTipoHeuristic();
        this.numLinhasCorte = solucao.getQtdLinhasCorte();
        this.mediaSobras = solucao.getMediaSobras();
        this.somatorioSobras = solucao.getSomatorioSobras();
        
    }
    public Individuo getIndividuo(){
    
        return this.vetor_sequencia_ind;
    }
    
    public void setIndividuo(Individuo ind){
    
        this.vetor_sequencia_ind = new Individuo(ind);
    }
    
    public void removeIndividuo(){
    
        this.vetor_sequencia_ind = null;
    }
    
    public void atualizaIndividuoSolucao(Individuo ind){
    
        getIndividuo().setListaItens2(ind.getListaItens());
        getIndividuo().setSomatorioItens(ind.getSomatorioItens());
    }
    
    public LinkedList<Individuo> getLinkedListIndividuos(){
    
        Iterator<Bin> it_indv = this.getObjetos().iterator();
        LinkedList<Individuo> listIndividuos = new LinkedList<Individuo>();
        
        while(it_indv.hasNext()){
        
            Individuo ind = it_indv.next().getIndividuo();
            listIndividuos.add(new Individuo(ind));
        }
        
        return listIndividuos;
    }
    
    public LinkedList<Individuo> adicionaIndividuo(Individuo individuo){
        
            this.getLinkedListIndividuos().add(individuo);
            
            return this.getLinkedListIndividuos();
    }
        
    public LinkedList<Individuo> removeIndividuos(Individuo individuo){
            
            this.getLinkedListIndividuos().remove(individuo);
            
            return this.getLinkedListIndividuos();
    }
    
    //Aqui remove determinada placa será útil para Grasp Melhoria
    public List<Bin> removePlaca(IBin objeto){
        
        try{
        
            this.getObjetos().remove((Bin) objeto);
        }
        catch(ClassCastException c){
        
                //this.getObjetos().remove((Bin) objeto);
        }
        return this.getObjetos();
    }
    
    public Double calculaLinhasDeCorteEMediaSobras(){
       
//        System.out.println("\n######### Calculando Corte e Medias Sobras #####################");
        Individuo individuo_completo = new Individuo();
        Iterator<Bin> iterator = this.getObjetos().iterator();
        Bin b;
        FAV = 0.0;
        int numCortes = 0, cort = 0, j;
        double qtdAreaSobra = 0.0;
        int numSobras = 0;
        float w = this.tamanhoChapa.retorneBase();
        float h = this.tamanhoChapa.retorneAltura();
        float Sobra = 0;
        Double MenorAp = 0.0;
       
        while(iterator.hasNext()){
            
            //System.out.println("\n\n");
            b = iterator.next();
            
            individuo_completo.setListaItens2(b.getIndividuo().getListaItens());
//            individuo_completo.calculaSomatorioItens();  //Coloquei no dia 2 de junho 08:35 da manhã !!!
//            System.out.println("Calcula List Itens -> ["+ individuo_completo.getListaItens()+" ] "+
//                                                                        " Somatório -> "+individuo_completo.getSomatorioItens());
//            
            numCortes = numCortes + b.getListaCortes().size();
            
//            System.out.println("LinhaCortetamanho --> " +b.getListaCortes().size());
//            System.out.println("numCortes -->" +numCortes);
            
            qtdAreaSobra = qtdAreaSobra + b.getSobra();
            numSobras = numSobras + b.getListaSobras().size();
            
//            System.out.println("LinhadeSobrasTamanho --> "+b.getListaSobras().size());
//            System.out.println("numSobras -->" +numSobras);
            
            Double sobra = 100-(b.getFav()/(w*h)*100);
            b.setAproveitamento(100 - sobra);
            
            FAV = FAV + b.getFav();
//            System.out.println("Placa o Fav -> "+b.getFav());
            
            if (sobra > MenorAp){
                //MenorAp = Sobra;                	                	
                MenorAp = sobra;
            }
        }
        this.set_Fav(FAV);
        this.setIndividuo(individuo_completo);
//        System.out.println("\nItens da solucao--> "+this.getIndividuo().getListaItens()+"\n");
        
        this.setQtdLinhasCorte(numCortes);
        this.setSomatorioSobras(new Double(qtdAreaSobra));
        
//        System.out.println("QtdLinha "+this.getQtdLinhasCorte());
//        System.out.println("SomatorioSobras "+this.getSomatorioSobras());
        
        Double media_Sobras = new Double(qtdAreaSobra/numSobras); 
        
        this.setMediaSobras(media_Sobras);
//        System.out.println("Media Sobras --> "+ this.getMediaSobras());
//        System.out.println("Agente --> "+ this.getNameAgente());
//        System.out.println("\n#########\n");
        j = this.getQtd();
//        System.out.println("J --> "+j); 
        FAV2 = ((FAV)+MenorAp)/(j*w*h);
        FAV = FAV/(j*w*h);
        //FAV2 = ((FAV*100)+MenorAp)/j;
         
//        System.out.println("FAV --> ["+FAV*100+" ]");
//        System.out.println("FAV2 --> ["+FAV2*100+" ]");
        this.setFAV(FAV);
        this.setFAV2(FAV2);
                
       return media_Sobras;
    }
    
    public int getQtdLinhasCorte(){
    
        return this.numLinhasCorte;
    }
    
    public void setQtdLinhasCorte(int numCortes){
    
        this.numLinhasCorte = numCortes;
    }
    
    public Double getSomatorioSobras(){
    
        return this.somatorioSobras;    
    }
    
    public void setSomatorioSobras(Double somatorioSobras){
    
        this.somatorioSobras = somatorioSobras;
    }
    
    public Double getMediaSobras(){
    
        return this.mediaSobras;
    }
    
    public void setMediaSobras(Double mediaSobras){
    
        this.mediaSobras = mediaSobras;
    }
             
    public void calculaFitness(){
		
        Individuo individuo;
        double somatorio_itens, somatorio_rest = 0;
        Iterator<Individuo> iterador_individuo = getLinkedListIndividuos().iterator();

        while(iterador_individuo.hasNext()){
            individuo = iterador_individuo.next();
            somatorio_itens = individuo.getSomatorioItens();
            somatorio_rest =  somatorio_rest + Util.calcula_fitness_placa(somatorio_itens, Funcoes.getChapa().getArea());
        }

        //setFitness(somatorio_rest);
    }
    
    public void setCapacidadeRespeitada(boolean capacidade){
        
        this.capacidade_respeitada = capacidade;        
    }
    
    @Override
    public int compareTo(SolucaoHeuristica sol) {
        
        if(this.FAV2 > sol.FAV2){
            return -1;
        }
        else{
            return 1;
        }
    }

    @Override
    public Bin retornePlanoDeCorte_Ob(int indice) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void imprimeSolucao(SolucaoHeuristica solucao,int cont1){
        
        int cont = 1;
        Individuo individuo;
        Iterator<Individuo> iterator = solucao.getLinkedListIndividuos().iterator();

        System.out.println("#################  SOLUÇÃO "+cont1+"####################\n\n");

        try{
            System.out.println("Tipo de Agente -->" + solucao.getTipoHeuristic().toString());
            System.out.println("Sequência de Encaixe --> "+ getIndividuo().getListaItens());}catch(NullPointerException e){
            System.out.println("Erro no individuo");
        }        System.out.println("FAV --> "+ solucao.getFAV()+ "\tFAV2 --> "+solucao.getFAV2());
        //System.out.println("");solucao.getObjetos().size();
        System.out.println("Media sobras --> "+ getMediaSobras());
        System.out.println("Quantidade --> "+solucao.getQtd());
        System.out.println("Qtd Linhas cortes --> "+solucao.getQtdLinhasCorte());
        System.out.println("Somatório Sobras --> "+solucao.getSomatorioSobras());
        
                
        while(iterator.hasNext()){

            individuo = iterator.next();
            System.out.println("Placa "+cont+" --> "+individuo.getListaItens()+"\tSomatório Itens --> "+individuo.getSomatorioItens()+
                    "\tAproveitamento Placa -> "+(solucao.getObjetos().get(cont-1).getAproveitamento()));

            cont++;
        }
        
        System.out.println("Somatório Solução --> "+solucao.get_Fav());
        System.out.println("###############################################\n\n");
    }
    
    //Aqui armazena a história das soluções na memória
    public ETipoHeuristicas getTipoHeuristic(){
    
        return etipoHeuristica;
    }    
    
    public void  setTipoHeuristic(ETipoHeuristicas etipoHeuristica){
    
        this.etipoHeuristica = etipoHeuristica;
    }
    
    public String getNameAgente(){
    
        return getTipoHeuristic().toString();
    }
    
    
    
}