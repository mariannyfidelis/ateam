package HeuristicaConstrutivaInicial;

import AgCombinacao.AgentesOperadores;
import Heuristicas.Individuo;
import j_HeuristicaArvoreNAria.Chapa;
import j_HeuristicaArvoreNAria.Metodos_heuristicos;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    
    //Simula um agente que seleciona uma solução e aplica a Árvore N-Ária !!!!
    public void aplicaAGLArvoreN(Solucao solucao){
       
       float larg, alt;
       j_HeuristicaArvoreNAria.Solucao solucion = new j_HeuristicaArvoreNAria.Solucao();
       Metodos_heuristicos m = new Metodos_heuristicos();
       
       larg = solucao.getTamanhoChapa().retorneBase();
       alt = solucao.getTamanhoChapa().retorneAltura();
       
       
       //FFIH_BFIH(boolean rotaciona, boolean firstFit, LinkedList<Pedidos> listaPedidos, Chapa chapa
       //c = m.FFIH_BFIH(true,true, pedidos, Funcoes.chapa); 
       solucion = m.FFIH_BFIH(true, false, recebeIndividuo_ListPedidos(solucao), new Chapa(larg, alt));
       solucion.imprime_solucao();
   } 
   
   //Esse método vai lá pra árvore N-aria
   public LinkedList<j_HeuristicaArvoreNAria.Pedidos> recebeIndividuo_ListPedidos(Solucao solucao){
   
       Integer refPedido;
       //Aqui deve-se selecionar a Lista de Objetos
       LinkedList<j_HeuristicaArvoreNAria.Pedidos> n_pedidos = 
                                    new LinkedList<j_HeuristicaArvoreNAria.Pedidos>();
       
       j_HeuristicaArvoreNAria.Pedidos pedidos_arvore;
       
       List<Integer> list_item = new ArrayList<Integer>();
       Iterator<Objeto> iter_objeto = solucao.getObjetos().iterator();
            
        while(iter_objeto.hasNext()){
        
            Individuo ind =  new Individuo();
            ind = iter_objeto.next().getIndividuo();

            list_item = ind.getListaItens();
            
            Iterator<Integer> it_integer = list_item.iterator();
            
            while(it_integer.hasNext()){
            //for(int i = 0; i < list_item.size(); i++){
                
                //refPedido = list_item.get(i);
                refPedido = it_integer.next();
                //pedidos_arvore = (j_HeuristicaArvoreNAria.Pedidos) solucao.getIPedidos().get(refPedido.intValue());
                pedidos_arvore = solucao.retornaPedido(refPedido.intValue(), solucao.getIPedidos());
                
                n_pedidos.add(new j_HeuristicaArvoreNAria.Pedidos(pedidos_arvore));
            }
        }
        
        return n_pedidos;
       
       //Aqui transforma a lista de Individuos de cada objeto em uma ListPedidos
   }
    public static void main(String[] args) throws FileNotFoundException, IOException, PecaInvalidaException {

        List<Solucao> solucao_GRASP = new LinkedList<Solucao>(); 
        
        List<Objeto> Solucao = new ArrayList<Objeto>();
        
        LinkedList<Heuristicas.Solucao> VetorSolucao = new LinkedList<Heuristicas.Solucao>();

        ArrayList<Item> L = new ArrayList<Item>();
        List<Item> Lc = new ArrayList<Item>();

        L = Funcoes.LerArq();
        
        List<IPedido> listaPedidosNaoAtendido = new ArrayList<IPedido>(L);
        
        Funcoes.OrdenaList(L);
        
        System.out.println("Lista Pedidos Não-Atendidos");
        
        for(int index = 0; index < listaPedidosNaoAtendido.size(); index++)
            System.out.print(listaPedidosNaoAtendido.get(index).id() + " - ");
                
        Lc = (List<Item>) L.clone();
        Funcoes.ImprimeItens("\n\nLista", Lc);

        //VetorSolucao = HeuristicaGRASP.Grasp2d(L,L, 50, 50,0.5, 10, listaPedidosNaoAtendido);
        
        solucao_GRASP = HeuristicaGRASP.Grasp2d(L,L, 50, 50,0.5, 10, listaPedidosNaoAtendido);
        
        //System.out.println("Numero de Solucoes Geradas --> "+ VetorSolucao.size());
        System.out.println("\nNumero de Solucoes Geradas --> "+ solucao_GRASP.size());
        /*//Imprimir as soluções geradas !!!
        for (int i = 0; i < VetorSolucao.size();i++){
            
            Heuristicas.Solucao s = new Heuristicas.Solucao();
            s = VetorSolucao.get(i);
            
            System.out.println("Solucao "+i+" Com "+s.getLista().size()+" individuos\n");
            
            LinkedList<Heuristicas.Individuo> ind = new LinkedList<Heuristicas.Individuo>();
            
            ind = s.getLista();

            for(int j = 0;j < ind.size();j++){
            
                Heuristicas.Individuo it = new Heuristicas.Individuo();
                it = ind.get(j);
                System.out.print("Individuo "+j+" Qtde Itens "+it.getSize()+" Itens ");
                
                for (int k = 0;k < it.getSize();k++){
                       System.out.print(it.getListaItens().get(k).toString()+" ");
                }
                System.out.println();
            }
         }*/
        Iterator<Solucao> iter_solucao = solucao_GRASP.iterator();
        int cont = 0;
        
        while(iter_solucao.hasNext()){
            
            Solucao solucao_t = new Solucao();
            solucao_t = iter_solucao.next();
            
            System.out.println("Solucao "+cont+" Com "+solucao_t.getObjetos().size()+" individuos\n");
            
            Iterator<Objeto> iter_objeto = solucao_t.getObjetos().iterator();
            
            while(iter_objeto.hasNext()){
            //for(int k = 0; k < solucao_t.getObjetos().size(); k++){
            
                Individuo ind =  new Individuo();
                ind = iter_objeto.next().getIndividuo();
            
                System.out.println("List itens --> "+ ind.getListaItens());
            } 
            
            System.out.println();
            
            cont++;
        }
        System.out.println();
        System.out.println("\nRealizar um teste de montagem !!!");
        
        //Aqui realizou um teste que Dado, uma lista de Integer e aplica-se o algoritmo de árvore !
        //Main m = new Main();
        //m.aplicaAGLArvoreN(solucao_GRASP.get(0));
        
        Individuo ind1 = new Individuo();
        Individuo ind2 = new Individuo();
        
        ind1 = solucao_GRASP.get(0).getObjetos().get(0).getIndividuo();
        ind2 = solucao_GRASP.get(0).getObjetos().get(1).getIndividuo();
        
        //Testando o crossover de 1 ponto
        System.out.println("\n\nTestando simplesmente as combinações !!!\n");
        
       // System.out.println(" ### Crossover de 1 ponto 1 ###");
       // AgentesOperadores.operadorCrossoverPonto1(ind1,ind2);
        
        System.out.println("\n\n ### Crossover de 1 ponto 1 VV  ###");
        AgentesOperadores.operadorCrossoverPonto1_v(ind1, ind2);
        
        System.out.println("\n\n ### Crossover de 2 pontos ###");
        
        //Testando o crossover de 2 pontos
        AgentesOperadores.operadorCrossoverPonto2(ind1, ind2);
   }
    
   
}