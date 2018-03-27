package algoritmosAgCombinacao;

import HHDInternal.Dimensao2D;
import HHDInterfaces.IBin;
import HHDInterfaces.IDimensao2d;
import HeuristicaConstrutivaInicial.Bin;
import java.util.List;
import Heuristicas.Util;
import Utilidades.Funcoes;
import java.util.Iterator;
import java.util.LinkedList;
import Heuristicas.Individuo;
import java.io.Serializable;

public class Solucao implements Comparable<Solucao> , Serializable{

    boolean capacidade_respeitada;
    private List<Bin> Objetos;
    private Double FAV;  //Fitness 1
    private Double FAV2; //Fitness 2
    //adicionar para generalizar a solução
    private LinkedList pedidoAtendido;     
    private IDimensao2d tamanhoChapa;

         
    public Solucao(){
        
        this.capacidade_respeitada = true;
        this.Objetos =  new LinkedList<Bin>();
        this.FAV = 0.0;
        this.FAV2 = 0.0;
        this.pedidoAtendido = new LinkedList();
        this.tamanhoChapa = new Dimensao2D(0, 0);

    }
    public Solucao(Solucao solucao){
        
        this.capacidade_respeitada = true;
        this.Objetos = new LinkedList<Bin>(solucao.getObjetos());
        this.FAV = solucao.getFAV();
        this.FAV2 = solucao.getFAV2();
        this.pedidoAtendido = new LinkedList(solucao.getIPedidos());
        this.tamanhoChapa = solucao.getTamanhoChapa();

    }

    public Solucao (List<Bin> Objetos, Double FAV, Double FAV2){
        
        this.capacidade_respeitada = true;
        this.Objetos = new LinkedList<Bin>(Objetos);
        this.FAV = FAV;
        this.FAV2 = FAV2;
    }

    public List<Bin> getObjetos() {

        return Objetos;
    }

    public void setObjetos(List<Bin> objetos) {
        
        Objetos = new LinkedList<Bin>(objetos);
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

    public LinkedList getIPedidos(){

        return pedidoAtendido;
    }
    
    public void setIPedidos(LinkedList pedidosAtendido){
    
        this.pedidoAtendido = pedidosAtendido;
    }
    
    public j_HeuristicaArvoreNAria.Pedidos retornaPedido(int i, LinkedList pedidosAtendido){
    
        j_HeuristicaArvoreNAria.Pedidos p;
        Iterator<j_HeuristicaArvoreNAria.Pedidos> itPedidosArv = pedidosAtendido.iterator();
        
        while(itPedidosArv.hasNext()){
        
            p = itPedidosArv.next();
            
            if(p.id() == i){
            
                return p;
            }        
        }
        
        return null;
    }
        
    public void setLinkedIPedidos(LinkedList IPedidos){
    
        pedidoAtendido = new LinkedList(IPedidos);
    }
    
    public IDimensao2d getTamanhoChapa(){
    
        return tamanhoChapa;
    }
    
    public void setTamanhoChapa(IDimensao2d tamanho){
    
        this.tamanhoChapa = tamanho;
    }
    
    public void setSolucao(Solucao solucao){
    
        this.Objetos = solucao.getObjetos();
        this.tamanhoChapa = solucao.getTamanhoChapa();
        this.pedidoAtendido = solucao.getIPedidos();
        this.FAV = solucao.getFAV();
        this.FAV2 = solucao.getFAV2();
    
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
    public int compareTo(Solucao s) {
            
        if(this.FAV2 > s.FAV2){
            return -1;
        }
        else{
            return 1;
        }
    }
    
    public static void imprimeSolucao(Solucao solucao,int cont1){
        
        int cont = 1;
        Individuo individuo;
        Iterator<Individuo> iterator = solucao.getLinkedListIndividuos().iterator();

        System.out.println("#################  SOLUÇÃO "+cont1+"####################\n\n");

        while(iterator.hasNext()){

            individuo = iterator.next();
            System.out.println("Placa "+cont+" --> "+individuo.getListaItens()+"\tSomatório Itens --> "+individuo.getSomatorioItens());

            cont++;
        }
        System.out.println("###############################################\n\n");
    }

}