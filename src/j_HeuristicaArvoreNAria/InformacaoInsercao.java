package j_HeuristicaArvoreNAria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class InformacaoInsercao {

    private Bin bin_insercao;
    private float fitness;
    private TiposInsercao insBest;
    private No raiz_insert;
    private int no_j, no_k; //Verificar se deve ser inteiro ou NO
    private No as_subnode;
    private boolean rotacao_item;
	
    public InformacaoInsercao(){

        bin_insercao = new Bin();
        fitness = 0;
        insBest = TiposInsercao.Vazio;
        setRaiz_Insert(null, 0, 0); //Verificar isso aqui pois o "null" pode dar um erro !!!
        setAsSubnode(null);
        setRotacao_Item(false);
    }

    public Bin getBin_Insercao() {

        return bin_insercao;
    }

    public void setBin_Insercao(Bin bin_insercao) {

        this.bin_insercao = bin_insercao;
    }

    public float getFitness() {

        return fitness;
    }

    public void setFitness(float fitness) {

        this.fitness = fitness;
    }

    public TiposInsercao getInsBest() {

        return insBest;
    }

    public void setInsBest(TiposInsercao insBest) {

        this.insBest = insBest;
    }

    public No getRaiz_Insert() {

        return raiz_insert;
    }

    public void setRaiz_Insert(No raiz_insert, int j, int k) {

        this.raiz_insert = raiz_insert;
        no_j = j; 
        no_k = k;
    }

    public int getNo_j(){

        return no_j;
    }

    public int getNo_k(){

        return no_k;
    }

    public No getAsSubnode() {

        return as_subnode;
    }

    public void setAsSubnode(No as_subnode) {

        this.as_subnode = as_subnode;
    }

    public boolean getRotacao_Item() {

        return rotacao_item;
    }

    public void setRotacao_Item(boolean rotacao_item) {

        this.rotacao_item = rotacao_item;
    }
	
    public static Pedidos retornaMelhorInsert(LinkedList<Pedidos> list, int[]  nBinsFor, InformacaoInsercao[] insBFor){

            int id_pedido,id_aster = 0, menorBinFor = 0, cont = 0;
            Pedidos pedidos, pedido_aster = null;
            Iterator<Pedidos> iterator = list.iterator();

            while(iterator.hasNext()){

                    pedidos = iterator.next();
                    id_pedido = pedidos.getId(); 

                    if(cont == 0) {
                            pedido_aster = pedidos;
                            menorBinFor = nBinsFor[id_pedido - 1];
                            id_aster = id_pedido;
                    }

                    if((cont > 0) && (nBinsFor[id_pedido - 1] < menorBinFor)){

                            menorBinFor = nBinsFor[id_pedido - 1]; 
                            pedido_aster = pedidos;
                            id_aster = id_pedido;
                    }

                    if((cont > 0) && (nBinsFor[id_pedido - 1] == menorBinFor)){//nBinsFor[id_aster]

                            if(insBFor[id_pedido - 1].getFitness() >= insBFor[id_aster - 1].getFitness()){

                                    pedido_aster = pedidos;
                                    menorBinFor = nBinsFor[id_pedido - 1];
                                    id_aster = id_pedido;

                            }
                            else{ /*Mantém as mesmas informações !!!*/ }
                    }

                    cont++;
            }		
            return pedido_aster;
    }
	
    public static InformacaoInsercao retornaMelhorSolucao(ArrayList<InformacaoInsercao> array_informacao, int cont){

        InformacaoInsercao informacao_insercao = new InformacaoInsercao();

        float melhor_fitness = 0, fitness_insercao;

            for(int i = 0; i < cont; i++){

                fitness_insercao = array_informacao.get(i).getFitness(); 

                if(fitness_insercao > melhor_fitness){

                    melhor_fitness = fitness_insercao;
                    informacao_insercao = array_informacao.get(i);
                }
            }

        return informacao_insercao;
    }
}