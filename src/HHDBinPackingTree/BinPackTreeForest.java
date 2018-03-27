package HHDBinPackingTree;

import HHDInterfaces.IBin;
import java.util.Iterator;
import HHDInterfaces.IPeca;
import java.util.ArrayList;
import HHDInterfaces.ISobra;
import java.util.LinkedList;
import java.io.Serializable;
import HHDInterfaces.IPedaco;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IListaPedidos;
import HHDInterfaces.ITreeObserver;
import HHDInterfaces.IGenericSolution;
import HHDInterfaces.ISolutionProvider;
import HHDInterfaces.IPecaIPedacoWrapper;
import HHDInternal.SolutionProviderFactory;

public class BinPackTreeForest implements IGenericSolution,Serializable{

    private IListaPedidos pedidosAtendidos;
    private LinkedList listaBinPackTrees;
    private IDimensao2d tamanhoChapa;

    //variaveis a serem incluidas
    private float porcentagemDesperdicio;
    private LinkedList listaObservers;

    private float FAV;
    private float FAV2;
    private float Fav;
    
    private int    qtdLinhasCorte;
    private Double qtdAreaSobra;
    private float  mediaSob;
    
    //URGENTE URGENTE
    //DESCOBRIR EM QUE PONTO PODERA SER ADICIONADA ROTAÇÃO
    public BinPackTreeForest(IDimensao2d dimensaoChapa){
      
        tamanhoChapa = dimensaoChapa;
        listaBinPackTrees = new LinkedList();
        listaObservers = new LinkedList();
        
        qtdLinhasCorte = 0;
        qtdAreaSobra   = new Double(0);
        mediaSob       = 0;
    }

    @Override
    public int getQtd() {
        
        return listaBinPackTrees.size();
    }
 
    public float getFav() {
        
        return Fav;
    }

    public void setFav(float Fav) {
        
        this.Fav = Fav;
    }
    
    public float getFAV() {
        
        return FAV;
    }

    public void setFAV(float FAV) {
        
        this.FAV = FAV;
    }

    public float getFAV2() {
        
        return FAV2;
    }

    public void setFAV2(float FAV2) {
        
        this.FAV2 = FAV2;
    }

    public int getQtdLinhasCorte(){
        
        return this.qtdLinhasCorte;
    }
    
    public  void setQtdLinhasCorte(int qtdLinhasCorte) {
        
        this.qtdLinhasCorte = qtdLinhasCorte;
    }

    public  Double getSomatorioSobras(){
    
        return this.qtdAreaSobra;
    }
    
    public  void setSomatorioSobras(Double qtdAreaSobra) {
        
        this.qtdAreaSobra = qtdAreaSobra;
    }
    
    public  float getMediaSobras(){
    
        return this.mediaSob;
    }
    
    public  void setMediaSobras(float mediaSob) {
        
        this.mediaSob = mediaSob;
    }
    
    public float getPorcentagemDesperdicio() {
        return porcentagemDesperdicio;
    }

    public void setPorcentagemDesperdicio(float porcentagemDesperdicio) {
        this.porcentagemDesperdicio = porcentagemDesperdicio;
    }
    
    public LinkedList getListaBinPackTrees() {
        
        return this.listaBinPackTrees;
    }

    public void setListaBinPackTrees(LinkedList listaBinPackTrees) {
        
        this.listaBinPackTrees = listaBinPackTrees;
    }

    public void determinaChapa(IDimensao2d tamanho){
        
        tamanhoChapa = tamanho;
    }

    public void adicionaPlanoDeCorte(BinPackTree arvore){
        
        listaBinPackTrees.add(arvore);
        arvore.setFloresta(this);
    }

    @Override
    public IDimensao2d getTamanhoChapa() {
        
        return tamanhoChapa;
    }

    @Override
    public IBin retornePlanoDeCorte(int indice) {
        
        return (IBin) listaBinPackTrees.get(indice);
    }

    public int retornaIndicePlanoDeCorte(BinPackTree arvore) {
        
        return listaBinPackTrees.indexOf(arvore);
    }

    @Override
    public void sendUpdateSignal(int indice){
        
        for(int i = 0; i < listaObservers.size(); i++)
            ((ITreeObserver)listaObservers.get(i)).update(indice);
    }

    @Override
    public void sendUpdateSignal(){
        
        for(int i = 0; i < listaObservers.size(); i++)
            ((ITreeObserver)listaObservers.get(i)).update();
    }

    @Override
    public ITreeObserver detach(ITreeObserver observador){
        
        try {
                listaObservers.remove(observador);
                return observador;
        }
        catch (Exception ex){
            return null;
        }
    }

    @Override
    public void attach(ITreeObserver observador){

        listaObservers.add(observador);
    }

    @Override
    public boolean mova(IPedaco pedaco, ISobra areaDestino, int indiceBin) {
        
        //int a = - 1;
        IPeca pecaTransferida = ((IPecaIPedacoWrapper) pedaco).getPeca();

        IBin planoDeCorte = this.retornePlanoDeCorte(pedaco.retorneIndiceBin());
        
        if((areaDestino != null) && (areaDestino.cabePeca(pecaTransferida.getDimensoes(), pecaTransferida.permiteRotacao()))==0){
         
                ISolutionProvider heuristica = SolutionProviderFactory.getInstance().newSolutionProvider();

                if(!pedaco.estaRemovido())
                        planoDeCorte.remova(pedaco);


                LinkedList listaPecas = new LinkedList(), listaSobras = new LinkedList();
                listaPecas.add(pecaTransferida.getIPecaIPedidoWrapper());
                listaSobras.add(areaDestino);
                heuristica.solucaoParcial(listaPecas, listaSobras, this.getTamanhoChapa());
                planoDeCorte.retireListaRemovidas(pedaco);
                planoDeCorte.aglutineSobras();
                this.sendUpdateSignal(indiceBin);
                this.sendUpdateSignal(this.retornaIndicePlanoDeCorte((BinPackTree) planoDeCorte));

                return true;
        }

        return false;
    }

    @Override
    public void aglutineSobras(int indice){

        this.retornePlanoDeCorte(indice).aglutineSobras();//Chama método "Aglutinar Sobras" lá do BinPackTree		
    }

    public void removaPlanoDeCorte(BinPackTree tree){

        listaBinPackTrees.remove(tree);
        sendUpdateSignal();
    }
    
    public void calculaFAVFAV2(BinPackTreeForest bbptf, 
                                          ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> solucao){

        try{
            IBPTNode inode;
            BPTPedaco no;
            float w    = bbptf.getTamanhoChapa().retorneBase();
            float h    = bbptf.getTamanhoChapa().retorneAltura();
            float area = w * h;
            int j = 0;
            int   qtdLinhasCorte = 0;
            int   qtdSobra       = 0;
            float qtdAreaSobra   = 0;
            float mediaSobras;
            float  sobra   = 0;
            Double MenorAp = 0.0;
            FAV     = (float) 0.0;
            FAV2    = (float) 0.0;
            Fav     = (float) 0.0;

            Iterator<BinPackTree> iterBPT = bbptf.getListaBinPackTrees().iterator();

            while(iterBPT.hasNext()){

                j++;
                BinPackTree bpt = iterBPT.next();

                qtdLinhasCorte = qtdLinhasCorte + bpt.getListaCortes().size();

                inode = bpt.getRaiz();
                no = (BPTPedaco) inode;

                FAV = FAV + (area - (no.getSomatorioSobras()));

                qtdAreaSobra   = qtdAreaSobra + no.getSomatorioSobras();
                qtdSobra       = qtdSobra + bpt.getListaSobras().size();

                sobra = (no.getDesperdicio());
                if (sobra > MenorAp){
                    MenorAp = new Double(sobra);
                }

                Fav = Fav + FAV;
                
                bpt.set_Fav(FAV);//bpt.set_Fav(area - (no.getSomatorioSobras()));
                                
                bbptf.setFav(FAV);
                
            }

            FAV2 = (float) (((FAV)+MenorAp)/(j*w*h));
            FAV = FAV/(j*w*h);

            bbptf.setFAV(FAV);
            bbptf.setFAV2(FAV2);
            bbptf.setQtdLinhasCorte(qtdLinhasCorte);
            bbptf.setSomatorioSobras(new Double(qtdAreaSobra));
            bbptf.setMediaSobras(qtdAreaSobra/qtdSobra);
            
            System.out.println("Atributos calculados na conversão ....");
            System.out.println("\nFAV --> "+bbptf.getFAV()+"   FAV2 --> "+bbptf.getFAV2());
            System.out.println("Qtd de arvores -> "+bbptf.getQtd());
            System.out.println("QtdLinhasCorte --> "+bbptf.getQtdLinhasCorte());
            System.out.println("SomatórioSobra --> "+bbptf.getSomatorioSobras());
            System.out.println("MediaSobras --> "+bbptf.getMediaSobras()+"\n\n");
        }catch(Exception erro){
        
            System.out.println("Ocorreu exceção ao calcular o FAV e outros !!!! :( :(");
        }
    }
  
}