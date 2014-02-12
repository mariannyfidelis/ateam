package HeuristicaConstrutivaInicial;

import Heuristicas.Individuo;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class Objeto implements IBin{
    
    int id;
    private int W;
    private int H;
    private Double Fav;
    private int Rf;
    private List<Faixas> F;

    public LinkedList listaPecas;
    public LinkedList listaCortes;
    public LinkedList listaSobras;
    public LinkedList listaSelecionadas;
    public LinkedList listaPedidosNaoAtendidos;
    public LinkedList sobrasMarcadas;
    public Individuo individuo;
    
    public Objeto( int w, int h){

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
        
        individuo = new Individuo();
   }
    
    public Objeto( int w, int h, double f){

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
    }
    
    public int getW() {
    
        return W;
    }

    public void setW(int W) {
    
        this.W = W;
    }

    public int getH() {
    
        return H;
    }

    public void setH(int H) {
    
        this.H = H;
    }

    public Individuo getIndividuo(){
    
        return this.individuo;
    
    }
    public IDimensao2d retorneDimensao(){
                             //(altura, largura)
        return new Dimensao2D(getH(), getW());
    
    }
    public Double getFav() {
    
        return Fav;
    }

    public void setFav(Double Fav) {

        this.Fav = Fav;
    }

    public int getRf() {
    
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

    public boolean integrar(Objeto binParcial){

        if(binParcial == null)
             
            return false;

        if(!binParcial.listaPecas.isEmpty()){

            listaPecas.addAll(binParcial.listaPecas);
            /*Collections.sort(listaPecas);
            Collections.reverse(listaPecas);*/
        }

        if(!binParcial.listaCortes.isEmpty()){

            listaCortes.addAll(binParcial.listaCortes);
            Collections.sort(listaCortes, (Corte)listaCortes.getFirst());
        }

        if(!binParcial.listaSobras.isEmpty()){

            listaSobras.addAll(binParcial.listaSobras);
            Collections.sort(listaSobras, new ComparadorPedacosDisponiveis());
        }

        return true;
    }

    public void adicionePeca(Peca peca){

            listaPecas.add(peca);
    }

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
}