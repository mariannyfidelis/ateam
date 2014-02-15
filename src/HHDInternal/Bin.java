package HHDInternal;

import HHDComparadores.*;
import HHDInterfaces.IBin;
import HHDInterfaces.IPeca;
import HHDInterfaces.ISobra;
import HHDInterfaces.IGroup;
import HHDInterfaces.IPedaco;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Collections;
import java.util.ListIterator;

public class Bin implements IBin{ //A Bin aki não implementa IBIN

    public LinkedList listaPecas;
    public LinkedList listaCortes;
    public LinkedList listaSobras;
    public LinkedList listaSelecionadas;
    public LinkedList listaPedidosNaoAtendidos;

    private int id;
    public LinkedList sobrasMarcadas;

    public Bin(){

        listaPecas = new LinkedList();
        listaCortes = new LinkedList();
        listaSobras = new LinkedList();
        listaSelecionadas = new LinkedList();
        listaPedidosNaoAtendidos = new LinkedList();
        sobrasMarcadas = new LinkedList();
        id = -1;
    }

    public boolean integrar(Bin binParcial){

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
}
