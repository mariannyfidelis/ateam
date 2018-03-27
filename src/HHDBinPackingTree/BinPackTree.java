package HHDBinPackingTree;

import HHDInternal.Peca;
import java.util.Vector;
import HHDInterfaces.IBin;
import java.util.Iterator;
import HHDInterfaces.IPeca;
import java.util.ArrayList;
import HHDInterfaces.IGroup;
import java.util.Collection;
import java.util.LinkedList;
import HHDInterfaces.ICorte;
import HHDInterfaces.ISobra;
import java.io.Serializable;
import HHDInterfaces.IPedaco;
import java.util.ListIterator;
import HHDInterfaces.IDimensao2d;
import HHDInternal.AreaRetangular;
import HHDInterfaces.IPecaIPedacoWrapper;

/**
 *  Mantém a estrutura de arvore binaria de
 *  corte guilhotinado.
 */
public class BinPackTree implements IBin,Serializable{

    private BinPackTreeForest Floresta;

    private int id;
    private boolean Valida;

    private IBPTNode Raiz;
    private IBPTNode NoSelecionado;

    private LinkedList nosRetirados;	
    private LinkedList sobrasMarcadas;
    private LinkedList ListaNosSelecionados;

    private float FAV;

    public BinPackTree (IDimensao2d dimensoes, int identificador, BinPackTreeForest floresta){

        id = identificador;
        Raiz = new BPTPedaco(dimensoes, this);
                
        Floresta = floresta;
        nosRetirados = new LinkedList();
        sobrasMarcadas = new LinkedList();
        ListaNosSelecionados = new LinkedList();
        FAV = 0;
    }

    @Override
    public void aglutineSobras(){

        retireCortesInuteis(this.Raiz);
    }

    public boolean corte(IBPTNode pedaco, float posicao, boolean orientacaoVertical){

        if(pedaco.pertenceArvore(this) && pedaco.getClass().isAssignableFrom(ICuttable.class))
                return ((ICuttable) pedaco).corte(posicao, orientacaoVertical);

        return false;
    }


    //Essas listas podem ser criadas no momento que a arvore � criada, para melhorar
    //o desempenho. Em compensacao, o consumo de memoria sera maior.
    //Verificar se a troca vale a pena. (Tempo vs. Espaco)
    private LinkedList criaListaCortes(IBPTNode node){

        LinkedList listaCortes = new LinkedList();

        if(node != null){

            if(node.getLeftSon() != null){

                ICorte corte = new BinPackCorte(node.getCutPosition(), node.getCutTamanho(), node.corteVertical(), node.getPosition());
                listaCortes.add(corte);
                listaCortes.addAll(criaListaCortes(node.getLeftSon()));
                listaCortes.addAll(criaListaCortes(node.getRigthSon()));

            }
        }

        return listaCortes;
    }

    private LinkedList criaListaPecas(IBPTNode node){

        
            LinkedList listaPecas = new LinkedList();

            if(node != null)
                    if(node.temPeca()){
                        ((BPTPedaco)node).getIPecaIPedidoWrapper();
                        ((BPTPedaco)node).getPedidoAtendido().devolvaPedido();
                        listaPecas.add(node);
                    }
                    else
                    {
                            listaPecas.addAll(criaListaPecas(node.getLeftSon()));
                            listaPecas.addAll(criaListaPecas(node.getRigthSon()));
                    }

            return listaPecas;
    }

    private LinkedList criaListaSobras(IBPTNode node){

        LinkedList listaSobras = new LinkedList();

        if(node != null){
        
            if(node.isCuttable()){
            
                listaSobras.add(node);
            }
            else{
                listaSobras.addAll(criaListaSobras(node.getLeftSon()));
                listaSobras.addAll(criaListaSobras(node.getRigthSon()));
            }
        }

        return listaSobras;
    }


    public void determineChapa(float base, float altura){

        Raiz = new BPTPedaco(new AreaRetangular(base, altura), this);
    }

    public BinPackTreeForest getFloresta(){

        return Floresta;
    }

    @Override
    public int getId(){

        return id;
    }

    /* ATENÇÃO!!!!!!
     * 
     * Os comandos abaixos estao sendo implementados SEM CONSIDERAR A INTERFACE QUE SERA UTILIZADA
      COM MARCELO, apenas a logica de execucao esta sendo levada em conta.
     * 
     * Antes da integracao, deve haver uma reuniao com Marcelo para decidir como estarao formatas listas e outros
     * elementos que serao utilizados por ambos os sistemas.
     * */

    @Override
    public LinkedList getListaCortes(){

        return criaListaCortes(this.Raiz);
    }
    
    //Só um teste poderá ser removido a qualquer momento
    public LinkedList getListaCortes(IBPTNode node){

        return criaListaCortes(node);
    }
    
    @Override
    public LinkedList getListaPecas(){

        return criaListaPecas(this.Raiz);
    }
    
    //Só um teste poderá ser removido a qualquer momento
    public LinkedList getListaPecas(IBPTNode node){

        return criaListaPecas(node);
    }

    @Override
    public LinkedList getListaSelecionadas(){

        return this.ListaNosSelecionados;
    }

    @Override
    public LinkedList getListaSobras(){

        return criaListaSobras(this.Raiz);
    }
    
    //Só um teste poderá ser removido a qualuer momento
    public LinkedList getListaSobras(IBPTNode node){

        return criaListaSobras(node);
    }

    @Override
    public Collection getListaSobrasMarcadas(){

        return (Collection) sobrasMarcadas.clone();
    }

    public IBPTNode getRaiz() {

        return Raiz;
    }

    @Override
    public void inicializaListaSelecionadas(){

        this.ListaNosSelecionados = new LinkedList();
    }

    @Override
    public void inicializeListaSobrasMarcadas(){

        sobrasMarcadas = new LinkedList();
    }

    @Override
    public void marqueSobra(ISobra pedacoAtual){

        sobrasMarcadas.add(pedacoAtual);
    }

    // implementacao aparentemente OK
    @Override
    public boolean remova(IPedaco pedaco){
//		if(!pedaco.temPeca())
//			return false;

        IBPTNode peca;

        if(PecaArvoreIPedacoWrapper.class.isAssignableFrom(pedaco.getClass()))
                peca = (IBPTNode) ((PecaArvoreIPedacoWrapper) pedaco).getPeca();
        else
                throw new ClassCastException("Classe IPedaco enviada a arvore nao contém um empacotador de no de arvore valido.");



        IBPTNode paiPeca = peca.getFather();

        if(peca.isSelected())
        {
                peca.setSelected(false);
                this.ListaNosSelecionados.remove(peca);
        }
//		if(peca.getLeftSon() != null)
//		{
//			LinkedList lista = new LinkedList();
//			lista.add(peca.getLeftSon().getIPecaIPedacoWrapper());
//			lista.add(peca.getRigthSon().getIPecaIPedacoWrapper());
//			IPeca pecaAgrupadora = this.agrupePecas(lista);
//			((PecaArvoreIPedacoWrapper)pedaco).setPeca((IBPTNode) pecaAgrupadora);
//			nosRetirados.add(pecaAgrupadora);
//		}
//		else
        nosRetirados.add(peca);
        BPTPedaco pedacoDisponivel = (BPTPedaco) peca.remova(); 


        if(paiPeca == null)
                this.Raiz = pedacoDisponivel;

        else if(paiPeca.getLeftSon() == peca)
                paiPeca.setLeftson(pedacoDisponivel);
        else
                paiPeca.setRightSon(pedacoDisponivel);


//		this.aglutineSobras();
        return true;
}

    private boolean retireCortesInuteis(IBPTNode node){
        
        if(node.temPeca())
            return false;
        
        else if(node.getLeftSon() == null)
                return true;

        boolean retiradoEsquerdo = retireCortesInuteis(node.getLeftSon());
        boolean retiradoDireito = retireCortesInuteis(node.getRigthSon()); 

        if(retiradoEsquerdo && retiradoDireito){
            
            node.removeCortes();
            
            return true;
        }

        return false;
    }
    
    public IBPTNode retorneChapa() {
    
        return Raiz;
    }

    @Override
    public boolean selecione(IPedaco pedaco){

        if(!pedaco.temPeca())
                if(((IBPTNode)((PecaArvoreIPedacoWrapper)pedaco).getPeca()).getLeftSon() == null)
                        return false;
                else
                {
                        selecioneFilhos(((IBPTNode)((PecaArvoreIPedacoWrapper)pedaco).getPeca()));
                        return true;
                }

        IBPTNode peca;

        if(PecaArvoreIPedacoWrapper.class.isAssignableFrom(pedaco.getClass()))
                peca = (IBPTNode) ((PecaArvoreIPedacoWrapper) pedaco).getPeca();
        else
                throw new ClassCastException("Classe IPedaco enviada � arvore nao cont�m um empacotador de no de arvore valido.");


        if(peca.isSelected())
                this.ListaNosSelecionados.remove(peca);
        else
                this.ListaNosSelecionados.add(peca);

        peca.setSelected(!peca.isSelected());

        return true;
    }

    private void selecioneFilhos(IBPTNode node){

        if(node.temPeca())
                this.selecione(node.getIPecaIPedacoWrapper());
        else
        {
                if(node.isCuttable())
                        return;

                this.selecioneFilhos(node.getLeftSon());
                this.selecioneFilhos(node.getRigthSon());
        }
    }

    @Override
    public boolean selecioneApenas(IPedaco pedaco){

            if(!pedaco.temPeca())
                    if(((IBPTNode)((PecaArvoreIPedacoWrapper)pedaco).getPeca()).getLeftSon() == null)
                            return false;
                    else{

                            ListIterator iteradorSelecionados = ListaNosSelecionados.listIterator();
                            IBPTNode aux;

                            while(iteradorSelecionados.hasNext())
                            {
                                    aux = (IBPTNode) iteradorSelecionados.next(); 
                                    aux.setSelected(false);
                            }
                            if(ListaNosSelecionados.size() > 0)
                                    ListaNosSelecionados = new LinkedList();

                            selecioneFilhos(((IBPTNode)((PecaArvoreIPedacoWrapper)pedaco).getPeca()));
                            return true;
                    }

            IBPTNode peca;

            if(PecaArvoreIPedacoWrapper.class.isAssignableFrom(pedaco.getClass()))
                    peca = (IBPTNode) ((PecaArvoreIPedacoWrapper) pedaco).getPeca();
            else
                    throw new ClassCastException("Classe IPedaco enviada � arvore nao cont�m um empacotador de no de arvore valido.");
            if(peca.isSelected())
                    ListaNosSelecionados.remove(peca);

            peca.setSelected(!peca.isSelected());
            ListIterator iterador = ListaNosSelecionados.listIterator();
            IBPTNode aux;

            while(iterador.hasNext()){

                    aux = (IBPTNode) iterador.next(); 
                    aux.setSelected(false);
            }

            if(ListaNosSelecionados.size() > 0)
                    ListaNosSelecionados = new LinkedList();

            if(peca.isSelected())
                    ListaNosSelecionados.add(peca);

            return true;

    }

    public void setFloresta(BinPackTreeForest floresta){

            Floresta = floresta;
            this.Raiz.setFloresta(floresta);
    }

    @Override
    public void setId(int i){

            id = i;
    }

    public void substitua(IBPTNode areaDestino, IBPTNode subArvore) throws Exception {

            if(!areaDestino.pertenceArvore(this))
                    throw new Exception("No destino nao pertence a esta arvore");

            if(subArvore == null)
                    throw new Exception("subArvore nao pode ser nula.");

            IBPTNode pai = ((IBPTNode) areaDestino).getFather();

            subArvore.setFather(pai);
            subArvore.setArvore(this);
            if(pai == null){

                    this.Raiz = subArvore;
                    return;
            }

            if(pai.getLeftSon() == (IBPTNode) areaDestino){

                    pai.setLeftson(subArvore);
                    return;
            }

            pai.setRightSon(subArvore);
    }

    @Override
    public LinkedList getPecasRetiradas() {
            
        return nosRetirados;
    }

    @Override
    public void reiniciaListaPecasRetiradas(){

            nosRetirados = new LinkedList();
    }

    @Override
    public IPeca agrupePecas(LinkedList listaPedacos){

            LinkedList listaPecas = new LinkedList();
            Iterator iteradorPedacos = listaPedacos.iterator();

            if(listaPedacos.size() <= 1)
                    return null;

            while(iteradorPedacos.hasNext()){

                    IPecaIPedacoWrapper pedacoAtual = (PecaArvoreIPedacoWrapper) iteradorPedacos.next(); 
                    listaPecas.add(pedacoAtual.getPeca());
                    if(((IBPTNode)pedacoAtual.getPeca()).getArvore() != this || ((IBPTNode)pedacoAtual.getPeca()).estaRemovidoDaArvore())
                            return null;

                    if(!((IBPTNode)pedacoAtual.getPeca()).temPeca())
                            return null;

                    if(pedacoAtual.isSelected())
                    {
                            pedacoAtual.setSelected(false);
                            ListaNosSelecionados.remove(pedacoAtual.getPeca());
                    }
            }

            Vector arrayPecas = new Vector(listaPecas);

            int numPecas = arrayPecas.size();
            int [] arrayNiveis = descobreNivelPecas(arrayPecas);
            int menorNivel = ((IBPTNode) arrayPecas.get(0)).getNivel();

            int i;
            for(i = 1; i < numPecas; i++)
            {
                    int nivelAtual = ((IBPTNode) arrayPecas.get(i)).getNivel();
                    if(nivelAtual < menorNivel)
                            menorNivel = nivelAtual;
            }

            //Colocando os nos no mesmo nivel
            for(i = 0; i < numPecas; i++)
                    while(arrayNiveis[i] != menorNivel)
                    {
                            arrayPecas.set(i, ((IBPTNode) arrayPecas.get(i)).getFather());
                            arrayNiveis[i]--;
                    }

            IBPTNode pai = null;
            while(true)
            {
                    boolean encontrouDiferentes = false;
                    LinkedList listaAAdicionar = new LinkedList();

                    for(i = 1; i < arrayPecas.size(); i++)
                            if(arrayPecas.get(i) == arrayPecas.get(i - 1))
                            {
                                    arrayPecas.remove(i);
                                    i--;
                            }



                    if(arrayPecas.size() == 1)
                    {
                            pai = ((IBPTNode)arrayPecas.get(0));
                            break;
                    }



                    for(i = 1; i < arrayPecas.size(); i++)
                    {
                            if(((IBPTNode) arrayPecas.get(i)).getFather() == ((IBPTNode) arrayPecas.get(i - 1)).getFather())
                            {
                                    Object elemento1 = arrayPecas.get(i);
                                    Object elemento2 = arrayPecas.get(i - 1);
                                    arrayPecas.remove(elemento1);
                                    arrayPecas.remove(elemento2);
                                    listaAAdicionar.add(((IBPTNode)elemento1).getFather());
                                    //arrayPecas.remove(i--); //foi encontrado pai comum. Logo, um deles pode ser removido
                                    i--;
                            }
                            else
                                    encontrouDiferentes = true;
                    }
                    if(encontrouDiferentes || arrayPecas.size() == 1)
                            sobeNivelElementos(arrayPecas);

                    arrayPecas.addAll(listaAAdicionar);
            }

            //criar peca agrupadora nesse ponto

            IBPTNode pecaAgrupadora = this.criePecaAgrupadora(pai);
            try {
                    this.substitua(pai, pecaAgrupadora);
            } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                    System.exit(0);
            }


            return pecaAgrupadora;
    }

    private IBPTNode criePecaAgrupadora(IBPTNode raizPecasAgrupadas){

            return new PecaAgrupadora(raizPecasAgrupadas);
    }

    private int[] descobreNivelPecas(Vector arrayPecas) {
        
        int [] arrayNiveis = new int [arrayPecas.size()];

        for(int i = 0; i < arrayPecas.size(); i++)
                 arrayNiveis[i] = ((IBPTNode) arrayPecas.get(i)).getNivel();

        return arrayNiveis;
    }

    private void sobeNivelElementos(Vector arrayPecas){

        for(int i = 0;i < arrayPecas.size(); i++)
                arrayPecas.set(i, ((IBPTNode)arrayPecas.get(i)).getFather());
    }

    
    @Override
    public boolean desagrupe(IGroup noGrupo){

        PecaAgrupadora grupo = (PecaAgrupadora) noGrupo;

        IBPTNode noRaiz = grupo.raizPecasAgrupadas;

        try {
                this.substitua(grupo, noRaiz);
        } catch (Exception e) {
                System.err.println(e.getMessage());
                return false;
        }

        return true;
    }

    @Override
    public void retireListaRemovidas(IPedaco pedaco){

         this.nosRetirados.remove(((IPecaIPedacoWrapper)pedaco).getPeca());
    }

    @Override
    public Double getFav() {
        
        //Verificar se estes cálculos podem ser refeitos !!!
        double sobras = (double) ((BPTPedaco)this.Raiz).getSomatorioSobras();
        double tamanhoPlaca = ((BPTPedaco)this.Raiz).getArea();
         
        setFAV(FAV);
        
        return (tamanhoPlaca - sobras);
    }

    public void setFAV(float FAV) {
        
        this.FAV = FAV;
    }
    
    public Double getAproVeitamento() {
        //Verificar se estes cálculos podem ser refeitos !!!
        return ((double) ((BPTPedaco)this.Raiz).getAproveitamento());
    }

    @Override
    public Double getSobra() {
   
        //Verificar se estes cálculos podem ser refeitos !!!
        return (double) ((BPTPedaco)this.Raiz).getSomatorioSobras();
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

    void set_Fav(float FAV) {
        
        this.FAV = FAV;
    }

}