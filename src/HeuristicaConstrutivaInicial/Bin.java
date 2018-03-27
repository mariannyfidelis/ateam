package HeuristicaConstrutivaInicial;

import HHDComparadores.ComparadorPedacosDisponiveis;
import HHDInternal.Dimensao2D;
import HHDInterfaces.IBin;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IGroup;
import HHDInterfaces.IPeca;
import HHDInterfaces.IPedaco;
import HHDInterfaces.ISobra;
import HHDInternal.Corte;
import HHDInternal.Peca;
import HHDInternal.PedacoDisponivel;
import Heuristicas.Individuo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class Bin implements IBin, Serializable{//Essa BIN é o mesmo OBJETO
    
    int id;
    private float W;
    private float H;
    private float Rf;
    private Double Fav;
    private Double Sobra;
    private Double aproveitamento;
    private List<Faixas> F;
    public LinkedList listaPecas;
    public LinkedList listaCortes;
    public LinkedList listaSobras;
    public LinkedList sobrasMarcadas;
    public LinkedList listaSelecionadas;
    public LinkedList listaPedidosNaoAtendidos;   
    public Individuo individuo;
     
    public Bin( float w, float h){

        W = w;
        H = h;
        Rf = w*h;
        Fav = 0.0;
        
        //Adaptar para a mesma formalização do algoritmo em árvore
        listaPecas = new LinkedList();
        listaCortes = new LinkedList();
        listaSobras = new LinkedList();
        listaSelecionadas = new LinkedList();
        listaPedidosNaoAtendidos = new LinkedList();
        sobrasMarcadas = new LinkedList();
        id = -1;
        
        aproveitamento = 0.0;
        individuo = new Individuo();
   }
    
    public Bin( float w, float h, double f){

        W = w;
        H = h;
        Rf = w*h;
        Fav = f;
        
        //Adaptar para a mesma formalização do algoritmo em árvore
        listaPecas = new LinkedList();
        listaCortes = new LinkedList();
        listaSobras = new LinkedList();
        listaSelecionadas = new LinkedList();
        listaPedidosNaoAtendidos = new LinkedList();
        sobrasMarcadas = new LinkedList();
        id = -1;
//        individuo = new Individuo();
    }
    public Bin(){//Esse construtor referencia a Bin do HHDHeuristic

        listaPecas = new LinkedList();
        listaCortes = new LinkedList();
        listaSobras = new LinkedList();
        listaSelecionadas = new LinkedList();
        listaPedidosNaoAtendidos = new LinkedList();
        sobrasMarcadas = new LinkedList();
        id = -1;
        Fav = 0.0;
        individuo = new Individuo();
    }
    
    public float getW() {
    
        return W;
    }

    public void setW(int W) {
    
        this.W = W;
    }

    public float getH() {
    
        return H;
    }

    public void setH(int H) {
    
        this.H = H;
    }

    @Override
    public Double getSobra() {
        
        return Sobra;
    }

    public void setSobra(Double Sobra) {
        
        this.Sobra = Sobra;
    }
    public void setSobra() {
        
        setSobra(this.Rf - this.Fav);
    }

    public Double getAproveitamento(){
    
        return aproveitamento;
    }
    
    public void setAproveitamento(Double aproveitamento){
    
        this.aproveitamento = aproveitamento;
    }
    
    public Individuo getIndividuo(){
    
        return this.individuo;
    
    }
    public IDimensao2d retorneDimensao(){
                             //(altura, largura)
        return new Dimensao2D(getW(),getH());
    }
    
    @Override
    public Double getFav() {
    
        return Fav;
    }

    public void setFav(Double Fav) {

        this.Fav = Fav;
    }

    public float getRf() {
    
        return Rf;
    }

    public void setRf(int Rf) {
    
        this.Rf = Rf;
    }

    public List<Faixas> getF() {
    
        return F;
    }

    public void setF(List<Faixas> F) {
    
        this.F = F;
    }

    public boolean integrar(Bin binParcial){

        if(binParcial == null)
             
            return false;

        if(!binParcial.listaPecas.isEmpty()){

            listaPecas.addAll(binParcial.listaPecas);
            getIndividuo().setListaItens(binParcial.getIndividuo().getListaItens());
            getIndividuo().setSomatorioItens(binParcial.getIndividuo().getSomatorioItens());
            this.setFav(getIndividuo().getSomatorioItens());
            
            //this.setFav(this.getFav() + binParcial.getFav());
            /*Collections.sort(listaPecas);
            Collections.reverse(listaPecas);*/
        }

        if(!binParcial.listaCortes.isEmpty()){

            listaCortes.addAll(binParcial.listaCortes);
            Collections.sort(listaCortes, (Corte)listaCortes.getFirst());
        }

        if(!binParcial.listaSobras.isEmpty()){

            listaSobras.addAll(binParcial.listaSobras);
            Collections.sort(listaSobras, new ComparadorPedacosDisponiveis());//AKI FOI MUDADO O COMPARADOR !!!!!!!!!!!!!!!!!!!!!!!1
        }

        return true;
    }

    public void adicionePeca(Peca peca){

        listaPecas.add(peca);
    }
    
    /*public void adicionePeca(HHDInternal.Peca peca){

        listaPecas.add(peca);
    }*/
    public void adicioneCorte(Corte corte){

        listaCortes.add(corte);
        Collections.sort(listaCortes, (Corte)listaCortes.getFirst());
    }
    
    public void adicionarSobra(PedacoDisponivel pedaco){

        listaSobras.add(pedaco);
        Collections.sort(listaSobras, new ComparadorPedacosDisponiveis());
    }
    
    @Override
    public LinkedList getListaCortes(){

        return (LinkedList) listaCortes.clone();
    }

    @Override
    public LinkedList getListaPecas(){

        return (LinkedList) listaPecas.clone();
    }

    @Override
    public LinkedList getListaSobras(){

        return (LinkedList) listaSobras.clone();
    }

    @Override
    public LinkedList getListaSelecionadas(){

        return (LinkedList) listaSelecionadas;
    }
    
    //Deve ser verificado para possiveis erros
    public int getWaste(){
    
        return 0;
    }

    @Override
    public void setId(int i){
        
        id = i;
    }
    
    @Override
    public int getId() {
        
        return id;
    }

    @Override
    public boolean selecione(IPedaco pedaco){
        
        if(pedaco.temPeca()) {

             Peca peca = ((PecaIPedacoWrapper) pedaco).getPeca();
             
             if(peca.isSelected())
                     listaSelecionadas.remove(peca);
                else
                      listaSelecionadas.add(peca);
                peca.setSelected(!peca.isSelected());

                return true;
        }

            return false;
    }

    @Override
    public boolean selecioneApenas(IPedaco pedaco){
        
        if(pedaco.temPeca()){

            Peca peca = ((PecaIPedacoWrapper) pedaco).getPeca();
            if(peca.isSelected())
                listaSelecionadas.remove(peca);

            peca.setSelected(!peca.isSelected());
            ListIterator iterador = listaSelecionadas.listIterator();
            Peca aux;

            while(iterador.hasNext()){
                aux = (Peca) iterador.next(); 
                aux.setSelected(false);
            }
            if(listaSelecionadas.size() > 0)
                listaSelecionadas = new LinkedList();

            if(peca.isSelected())
                listaSelecionadas.add(peca);

            return true;
        }

        return false;
    }

    @Override
    public boolean remova(IPedaco pedaco){

         if(pedaco.temPeca()){

             Peca peca = ((PecaIPedacoWrapper) pedaco).getPeca();
                    
             if(this.listaPecas.remove(peca)) {
    
                 peca.getPedidoAtendido().devolvaPedido();
                 listaPedidosNaoAtendidos.add(peca.getPedidoAtendido());
                 listaSelecionadas.remove(peca);
                 adicionarSobra(peca.remova());
                 pedaco.removaDoBin();
                   
                return true;
             }
         }
         
      return false;
    }

    @Override
    public void inicializeListaSobrasMarcadas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection getListaSobrasMarcadas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void aglutineSobras() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void inicializaListaSelecionadas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void marqueSobra(ISobra pedacoAtual) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LinkedList getPecasRetiradas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reiniciaListaPecasRetiradas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public IPeca agrupePecas(LinkedList listaPecas) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean desagrupe(IGroup noGrupo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void retireListaRemovidas(IPedaco pedaco) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Integer> getListaItensIndiv() {
       
       Iterator<Peca> listPecas = getListaPecas().iterator();
       Integer ref;
       ArrayList<Integer> array = new ArrayList<Integer>();
       
       while(listPecas.hasNext()){
       
           ref = listPecas.next().getPedidoAtendido().id();
           array.add(ref);
       }
       return array;
    }
}