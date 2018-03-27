package Heuristicas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import Utilidades.Chapa;
import Utilidades.Funcoes;
import java.io.Serializable;

public class Individuo implements Serializable{

	private boolean capac_respeitada;
	private double fitness;
	private double somatorio_itens;
	private List<Integer> lista_itens;
	
	
	@SuppressWarnings("static-access")
	public Individuo(Individuo indv){
        
            this.capac_respeitada = indv.isCapacidadeRespeitada();
            this.fitness = indv.getFitness();
            this.somatorio_itens = indv.getSomatorioItens();
            this.lista_itens = new ArrayList<Integer>(indv.getListaItens());
        }
        public Individuo(){
		
		capac_respeitada = true;
                try{
		    fitness = Funcoes.getChapa().getArea();
                }catch(Exception e){}
                
		somatorio_itens = 0.0;
		lista_itens = new ArrayList<Integer>();
	}
	
	public Individuo(ArrayList<Integer> lista_item, double somatorio){
		
		this.somatorio_itens = somatorio;
		this.lista_itens.addAll(lista_item);
	}
	
	public double getSomatorioItens(){
		
		return somatorio_itens;
	}
	
	public List<Integer> getListaItens(){
		
		return lista_itens;
	}
	
	public void setSomatorioItens(double area_item){
		
		somatorio_itens = somatorio_itens + area_item;
	}
        
        public void setSomatorioItens2(double somatorio){
		
		somatorio_itens = somatorio;
	}
	
	public void setListaItens(List<Integer> list){
		
                lista_itens.addAll(list);
			
	}
	
	public void setListaItens2(List<Integer> list){
		
		try{
			lista_itens.addAll(lista_itens.size(), list);
		}		
		catch (Exception e) {
			System.out.println("Acontecerá um erro na lista!");
                        System.out.println(e);
		}
	}
    
	public void setListaItens(Integer id_item){
		
		lista_itens.add(id_item);		
	}
	
	public int getSize(){
		
		return lista_itens.size();
	}
	
	public Iterator<Integer> IteratorListaItens(){
		
		return lista_itens.iterator();
	}
	
	public double getFitness() {
		
		return fitness;
	}

        public void setFitness(double fitness) {
		
		this.fitness = fitness;
	}
        
	public void setFitness() {
		
		this.fitness = Util.calcula_fitness_placa(getSomatorioItens(), Chapa.getArea());
	}
	
        public boolean  calculaCapacidadePlaca(){
        
           double capacidade = Chapa.getArea() - getSomatorioItens();
           if(capacidade < 0){
              return false;
           }
         
           return true;
        }
        
        public void adicionaItemLista(Integer id_item, double area_item){
	
		this.lista_itens.add(id_item);
		this.somatorio_itens =  this.somatorio_itens + area_item;		
	}
        
	public void removeItemLista(int id_item, double area_item){
	
		lista_itens.remove(id_item);
		somatorio_itens =  somatorio_itens - area_item;		
	}
	
	//@SuppressWarnings("unchecked")
	/*public Individuo clone(){
		 
		return new Individuo((/*(ArrayList<Integer>)lista_itens.clone()), somatorio_itens);

	}*/

	public boolean isCapacidadeRespeitada() {
		
            return capac_respeitada;
	}

	public void setCapacidadeRespeitada(boolean capac_respeitada) {
		
            this.capac_respeitada = capac_respeitada;
	}
	
	public double calculaSomatorioItens(){
		
            int aux;
            double somatorio = 0;
            Iterator<Integer> iterator = getListaItens().iterator();

            while(iterator.hasNext()){

                    aux = iterator.next();
                    somatorio = somatorio + Funcoes.getVetor_info()[aux-1].retornaArea();
            }
          return somatorio;
	}
}