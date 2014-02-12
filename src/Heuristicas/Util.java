package Heuristicas;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import Util.Funcoes;
import Util.Pedidos;

public class Util {

    static Pedidos[] pedidos_info;
	
    public static LinkedList<Integer> retornaConjuntoMaiorCapacidade(Individuo individuo, Pedidos[] pedidos_info, double capacidade){

        LinkedList<Integer> conjunto_itens = new LinkedList<Integer>();
        Iterator<Integer> iterator = individuo.IteratorListaItens();

        int id_item;
        double area_item;

        while(iterator.hasNext()){

                id_item = iterator.next();
                area_item = pedidos_info[id_item].retornaArea();

                if(area_item >= capacidade){

                        conjunto_itens.add(id_item);
                }
        }

        return conjunto_itens;
    }

    public double calcula_custo(ArrayList<Integer> array, Pedidos[] pedidos_info){

        int var;
        double custo = 0;

        for(int i = 0; i < array.size();i++){

                var = array.get(i);
                custo = custo + pedidos_info[var-1].retornaArea();
        }

            return custo;
    }

    public static void imprimeListaIndividuos(LinkedList<Individuo> list_individuo){

        Iterator<Individuo> iterator = list_individuo.iterator();

        Individuo individuo_atual;

        while(iterator.hasNext()){

            individuo_atual = iterator.next();

            System.out.println("Somatório Itens --> "+ individuo_atual.getSomatorioItens());

            Iterator<Integer> list_itens = individuo_atual.IteratorListaItens();

            while(list_itens.hasNext()){

                    System.out.print(list_itens.next()+ "  ");
            }

            System.out.println("\n");			
    }
    }


    public static Integer selecionaMaiorItem(ArrayList<Integer> array){

        Integer item;

        Funcoes.ordenaDecrescenteListaItens(array);
        item = array.get(0);

        return item;
    }

    public static Integer selecionaMenorItem(ArrayList<Integer> array){

        Integer item;

        Funcoes.ordenaDecrescenteListaItens(array);
        item = array.get(array.size()- 1);

        return item;
    }

    public static Integer selecionaItemAleatorio(ArrayList<Integer> array){

        Integer item;

        SecureRandom random = new SecureRandom();
        item = random.nextInt(array.size());

        return item;
    }

    public static double calcula_fitness_placa(double somatorio_itens, double area_placa){

        double result = area_placa - somatorio_itens;

        return result;
    }
		
}