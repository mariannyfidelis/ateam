package Heuristicas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import Utilidades.Chapa;
import Utilidades.Funcoes;
import j_HeuristicaArvoreNAria.Pedidos;

public class FirstBestFit {

	static Pedidos[] info_item;
	static LinkedList<Individuo> lista_individuos;
	ArrayList<Integer> array_inteiro = new ArrayList<Integer>();
	
	public static LinkedList<Individuo> encaixe_itens(LinkedList<Pedidos> lista_pedidos, Chapa chapa, boolean firstFit){
		
		int i, cont_placa = 0, indice_individuo = 0, aux_indiv = 0;
		double area_item, restante, melhor_restante = 0, aux; 
		
		Pedidos pedido;
		boolean insercao = false;
		Individuo individuo, aux_individuo;
		
		lista_individuos = new LinkedList<Individuo>();
		
		info_item = new Pedidos[lista_pedidos.size()];
		info_item = Funcoes.atribui_info(lista_pedidos, info_item);
		
		for (i = 0 ; i < lista_pedidos.size(); i++){
			
			pedido = lista_pedidos.get(i);
		    area_item = pedido.retornaArea();
			
		    System.out.println("\n\nPedido "+(i+1));
			pedido.imprimePedido();
			
						
			if (cont_placa == 0) {
				
				individuo = new Individuo();
				individuo.setSomatorioItens(area_item);
				individuo.setListaItens(i);
				individuo.setFitness();
				lista_individuos.add(individuo);
				
				System.out.println("\nCriei o 1º individuo");
				System.out.println("O somatorio de itens eh: "+ individuo.getSomatorioItens());
				System.out.println("O fiteness atual eh: "+ individuo.getFitness());
				
				cont_placa++;
			} 
			else {

				Iterator<Individuo> iterator = lista_individuos.iterator();
				melhor_restante = 0;
				aux_indiv = 0;
				
				insercao = false;  // Sempre deve está atualizado
				
				while(iterator.hasNext()){
					
					aux_individuo = iterator.next();
					
					aux = aux_individuo.getSomatorioItens();
					restante = aux + area_item;
					
					if(restante <= Chapa.getArea() && firstFit == true){
						
						aux_individuo.setSomatorioItens(area_item);
						aux_individuo.setListaItens(i);
						aux_individuo.setFitness();
						insercao = true;
						
						System.out.println("Estou inserindo em um indivíduo existente!");
						System.out.println("O somatorio de itens eh: "+ aux_individuo.getSomatorioItens());
						System.out.println("O fiteness atual eh: "+ aux_individuo.getFitness());
						
						break;
					}
					else{
						if(restante > melhor_restante && restante <= Chapa.getArea()){
							
							melhor_restante =  restante;
							indice_individuo = aux_indiv;
							insercao = true;
						}						
					}
					aux_indiv++;
				}
				
				if(firstFit == false && insercao == true){
					
					
					aux_individuo = lista_individuos.get(indice_individuo);
					aux_individuo.setSomatorioItens(area_item);
					aux_individuo.setFitness();
					aux_individuo.setListaItens(i);
				}
				//else insercao = false;
					
				if(insercao == false){
                                        System.out.println("Criando um novo indivíduo .... ");
					individuo = new Individuo();
					individuo.setSomatorioItens(area_item);
					individuo.setListaItens(i);
					lista_individuos.add(individuo);
					cont_placa++;	
				}
			}
		}	
		
		return lista_individuos;
	}
	
	public static void removeTodosIndividuos(LinkedList<Individuo> list_indv){
		
		list_indv.removeAll(list_indv);
	}
}
