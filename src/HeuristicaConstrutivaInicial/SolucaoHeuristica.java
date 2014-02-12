package HeuristicaConstrutivaInicial;

import Heuristicas.Individuo;
import java.util.LinkedList;

public class SolucaoHeuristica implements ISolution{

    private LinkedList listaBins;
    private LinkedList pedidoAtendido;     
    private IDimensao2d tamanhoChapa;

    
    //Poderia generalizar e dá uma solução representada na LinkedList<Individuo>
    private LinkedList<Individuo> list_individuo;
    private double fitness;
    
    //public SolucaoHeuristica(IListaPedidos pedido, IDimensao2d tamanho){
    public SolucaoHeuristica(LinkedList<IPedido> pedido, IDimensao2d tamanho){
        
        pedidoAtendido = pedido;
        listaBins = new LinkedList();
        tamanhoChapa = tamanho;
    }

    @Override
    public int getQtd(){
            
        return listaBins.size();
    }

    public void adicionarPlanoDeCorte(Objeto plano){//Objeto

        listaBins.add(plano);
    }
	
    @Override
    public IDimensao2d getTamanhoChapa() {
        
        return tamanhoChapa;
    }

    @Override
    public Objeto retornePlanoDeCorte(int indice){

        if(indice < listaBins.size() && indice >= 0)
             return (Objeto) listaBins.get(indice);

        return null;
    }
}