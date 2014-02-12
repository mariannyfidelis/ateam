package Heuristicas;

import Util.Chapa;
import java.util.Iterator;
import java.util.LinkedList;
import Util.Funcoes;
import java.io.Serializable;

public class Solucao implements Serializable{

	boolean capacidade_respeitada;
        private double fitness;
	private LinkedList<Individuo> lista_placas;
	
	public Solucao(){
            this.capacidade_respeitada = true;
            this.fitness = Chapa.getArea();
            lista_placas = new LinkedList<Individuo>();        
        }
	
	public Solucao(boolean capacidade_respeitada, LinkedList<Individuo> list_individuos, double fitness){

            this.capacidade_respeitada = capacidade_respeitada;
            this.fitness = fitness;
            lista_placas = list_individuos;
	}
	
        public boolean isCapacidadeRespeitada(){
        
            return capacidade_respeitada;        
        }
        
        public void setCapacidadeRespeitada(boolean capacidade){
        
            this.capacidade_respeitada = capacidade;        
        }
        
        public double getFitness() {
		
            return fitness;
	}
	
	public void setFitness(double fitness) {
		
            this.fitness = fitness;
	}
	
	public void calculaFitness(){
		
            Individuo individuo;
            double somatorio_itens, somatorio_rest = 0;
            Iterator<Individuo> iterador_individuo = getLista().iterator();

            while(iterador_individuo.hasNext()){
                individuo = iterador_individuo.next();
                somatorio_itens = individuo.getSomatorioItens();
                somatorio_rest =  somatorio_rest + Util.calcula_fitness_placa(somatorio_itens, Funcoes.getChapa().getArea());
                
                if(individuo.isCapacidadeRespeitada() == false){
                    setCapacidadeRespeitada(false);
                }
            }

            setFitness(somatorio_rest);
	}
	
	public LinkedList<Individuo> getLista() {
		
            return lista_placas;
	}
	
	public void setLista(LinkedList<Individuo> lista) {
            lista_placas = lista; //Pode ser q seja necessário no futuro colocar: "lista_placas.addAll(lista);"
	}
	
	public int size(){
		
            return lista_placas.size();
	}
	
	public Iterator<Individuo> iteratorIndividuos(Solucao solucao){
		
            return getLista().iterator();
	}
        
        public LinkedList<Individuo> adicionaIndividuo(Individuo individuo){
        
            this.lista_placas.add(individuo);
            return this.lista_placas;
        }
        
        public LinkedList<Individuo> removeIndividuos(Individuo individuo){
            
            this.lista_placas.remove(individuo);
            return this.lista_placas;
        }
        
        public void setSolucao(Solucao solucao){
        
            this.fitness = solucao.getFitness();
            this.capacidade_respeitada = solucao.isCapacidadeRespeitada();
            this.lista_placas = (LinkedList<Individuo>) solucao.getLista().clone();
       }
        
	@Override
	public Solucao clone(){
		 
            return new Solucao(true,((LinkedList<Individuo>)lista_placas.clone()), getFitness());
	}
        
        public static void imprimeSolucao(Solucao solucao,int cont1){
        
            int cont = 1;
            Individuo individuo;
            Iterator<Individuo> iterator = solucao.getLista().iterator();
            
            System.out.println("#################  SOLUÇÃO "+cont1+"####################\n\n");
            
            while(iterator.hasNext()){
                
                individuo = iterator.next();
                System.out.println("Placa "+cont+" --> "+individuo.getListaItens()+"\tSomatório Itens --> "+individuo.getSomatorioItens());
                                
                cont++;
            }
            System.out.println("###############################################\n\n");
        }       
}