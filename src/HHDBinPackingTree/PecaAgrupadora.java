package HHDBinPackingTree;

import HHDInternal.Ponto;
import HHDInterfaces.ICorte;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IGroup;
import java.util.LinkedList;
import HHDInterfaces.IPecaPronta;
import HHDInterfaces.IPecaIPedacoWrapper;
import HHDInterfaces.ISobra;
import HHDInternal.CutLayoutNodesPainter;


public class PecaAgrupadora implements IBPTNode, IGroup{

    IBPTNode raizPecasAgrupadas;
    boolean isSelected;

    
    public PecaAgrupadora(IBPTNode raizPecasAgrupadas){
        
        this.raizPecasAgrupadas = raizPecasAgrupadas;
    }

    @Override
    public boolean isCuttable(){
        
        return false; // nós agrupados nao podem ser cortados
    }

    @Override
    public float getWidth(){
        
        return raizPecasAgrupadas.getWidth();
    }

    @Override
    public IBPTNode getFather(){
        
        return raizPecasAgrupadas.getFather();
    }

    @Override
    public void setFather(IBPTNode father){
        
        raizPecasAgrupadas.setFather(father);
    }

    @Override
    public float getHeight(){
        
        return raizPecasAgrupadas.getHeight();
    }

    @Override
    public short getKind(){
        
        return IBPTNode.PECA_AGRUPADA;
    }

    @Override
    public IBPTNode getLeftSon(){
        
        return null;
    }

    @Override
    public Ponto getPosition(){
        
        return raizPecasAgrupadas.getPosition();
    }

    @Override
    public void changePositionTo(Ponto position) 
    {
            raizPecasAgrupadas.changePositionTo(position);
    }

    @Override
    public IBPTNode getRigthSon() 
    {
            return null; // n�s "PecaAgrupada" nao possuem filhos
    }

    @Override
    public void Paint() {

    }

    @Override
    public boolean pertenceArvore(BinPackTree tree) 
    {
            return raizPecasAgrupadas.pertenceArvore(tree);
    }

    @Override
    public boolean temPeca() 
    {
            return true;
    }

    @Override
    public boolean isSelected() 
    {
            return isSelected;
    }

    @Override
    public void setSelected(boolean b) 
    {
            isSelected = b;
    }

    @Override
    public IDimensao2d getDimensoes() 
    {
            return raizPecasAgrupadas.getDimensoes();
    }

    @Override
    public void setLeftson(IBPTNode node) 
    {
            this.lanceErro("Impossível criar filhos em nó \"peca agrupada\"");
    }

    @Override
    public void setRightSon(IBPTNode node) 
    {
            this.lanceErro("Impossível criar filhos em nó \"peca agrupada\"");
    }
    
    private void lanceErro(String s) {
            try {
                    throw new Exception (s);
            } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    System.exit(0);
            }
    }

    // As anomalias a seguir sao devidas ao fato do conceito de "n�" e de "peca" nao estarem separados na arvore
    @Override
    public float getCutPosition() 
    {
            return -1;
    }

    @Override
    public float getCutTamanho() 
    {
            return -1;
    }

    @Override
    public boolean corteVertical() 
    {
            return false;
    }
    ////*** fim anomalias ***

    @Override
    public BinPackTree getArvore() 
    {
            return raizPecasAgrupadas.getArvore();
    }

    @Override
    public int getNivel() 
    {
            return raizPecasAgrupadas.getNivel();
    }

    @Override
    public boolean permiteRotacao() 
    {

            return raizPecasAgrupadas.permiteRotacao();
    }

    @Override
    public ISobra remova() 
    {
            return raizPecasAgrupadas.remova();
    }

    @Override
    public IPecaIPedacoWrapper getIPecaIPedacoWrapper() 
    {
            return new PecaArvoreIPedacoWrapper(this, 

                            raizPecasAgrupadas.retorneIndiceBin());
    }

    @Override
    public Ponto getPontoInfEsq() 
    {
            return raizPecasAgrupadas.getPontoInfEsq();
    }

    @Override
    public Ponto getPontoSupDir() 
    {
            return raizPecasAgrupadas.getPontoSupDir();
    }

    @Override
    public IPecaPronta getIPecaIPedidoWrapper() 
    {
            return new IBPTNodeITestePecaWrapper(this);
    }

    @Override
    public void acceptPainter(CutLayoutNodesPainter painter){
        
            painter.paintIGroup(this);
    }

    @Override
    public LinkedList getListaCortes() 
    {
            return criaListaCortes(this.raizPecasAgrupadas);
    }

    @Override
    public LinkedList getListaPecas() 
    {
            return criaListaPecas(this.raizPecasAgrupadas);
    }

    private LinkedList criaListaCortes(IBPTNode node) 
    {
            LinkedList listaCortes = new LinkedList();

            if(node != null)
            {

                    if(node.getLeftSon() != null)
                    {
                            ICorte corte = new BinPackCorte(node.getCutPosition(), node.getCutTamanho(), node.corteVertical(), node.getPosition());
                            listaCortes.add(corte);
                            listaCortes.addAll(criaListaCortes(node.getLeftSon()));
                            listaCortes.addAll(criaListaCortes(node.getRigthSon()));
                    }

            }


            return listaCortes;
    }

    private LinkedList criaListaPecas(IBPTNode node) 
    {
            LinkedList listaPecas = new LinkedList();

            if(node != null)
                    if(node.temPeca())
                            listaPecas.add(node);
                    else
                    {
                            listaPecas.addAll(criaListaPecas(node.getLeftSon()));
                            listaPecas.addAll(criaListaPecas(node.getRigthSon()));
                    }

            return listaPecas;
    }

    @Override
    public int retorneIndiceBin() {
            return this.raizPecasAgrupadas.retorneIndiceBin();
    }

    @Override
    public void setArvore(BinPackTree arvore) 
    {
            this.raizPecasAgrupadas.setArvore(arvore);
    }

    @Override
    public boolean estaRemovidoDaArvore(){
        
        return raizPecasAgrupadas.estaRemovidoDaArvore();
    }

    @Override
    public void removeCortes(){
        
        try {
                throw new Exception();
        } catch (Exception e)
        {
                System.err.println("Tentando remover cortes de uma peca!!! QUE PECADO!!!");
                e.printStackTrace(System.err);
                System.exit(0);
        }

    }

    @Override
    public int getID(){
        
        return -1;
    }

    @Override
    public void setFloresta(BinPackTreeForest floresta){
 
        this.raizPecasAgrupadas.setFloresta(floresta);
    }
    
    @Override
    public BinPackTreeForest getFloresta(){
        
        return this.raizPecasAgrupadas.getFloresta();
    }
}