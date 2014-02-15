package HHDInternal;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.ISolution;
import HeuristicaConstrutivaInicial.Objeto;
import java.util.LinkedList;


public class SolucaoHeuristica implements ISolution{

    private LinkedList listaBins;
    private LinkedList pedidoAtendido;     
    private IDimensao2d tamanhoChapa;

    //Poderia generalizar e dá uma solução representada na LinkedList<Individuo>
    
    //public SolucaoHeuristica(IListaPedidos pedido, IDimensao2d tamanho){
    public SolucaoHeuristica(LinkedList<Pedidos> pedido, IDimensao2d tamanho){
        
        pedidoAtendido = pedido;
        listaBins = new LinkedList();
        tamanhoChapa = tamanho;
    }

    @Override
    public int getQtd(){
            
        return listaBins.size();
    }

    public void adicionarPlanoDeCorte(Objeto plano){

        listaBins.add(plano);
    }
	
    @Override
    public IDimensao2d getTamanhoChapa() {
        
        return tamanhoChapa;
    }

    @Override
    public Bin retornePlanoDeCorte(int indice){

        if(indice < listaBins.size() && indice >= 0)
             return (Bin) listaBins.get(indice);

        return null;
    }
}