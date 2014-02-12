package j_HeuristicaArvoreNAria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Solucao{

	private int cont = 0;
	private LinkedList<Bin> solucao; 
	
	public Solucao(){
		solucao = new LinkedList<Bin>();
		cont = 0;
	}
	
	public void adicionaBin(Bin bin){ 
		
		solucao.add(bin);
		cont =  cont + 1;
	}
	
	public void removeBin(Bin bin){ 
		
		solucao.remove(bin);
		cont =  cont - 1;
	}
	
	public void removeUltimaBin(){ 
		
		solucao.removeLast();
		cont =  cont - 1;
	}
	
	public Bin resgataUltimaBin(){
		
		Bin bin_ultima = solucao.getLast();
		
		return bin_ultima;
	}
	
	public Iterator<Bin> listaBinSolucao(){
		
		Iterator<Bin> iterator = solucao.iterator();
		
		return iterator;	
	}
	
	public LinkedList<Bin> retornaSolucao(){
		
		return solucao;
	}
	
	public void atribui_solucao(Solucao outra_solucao){
		
		this.cont = outra_solucao.size();
		this.solucao = outra_solucao.retornaSolucao();		
	}
	
	public void unir_duas_solucoes(Solucao solucao1, Solucao solucao2){
		
		int cont = solucao1.size() + solucao2.size();
		this.cont = cont;
		
		Iterator<Bin> iterador_bin_S2 = solucao2.listaBinSolucao();
		Bin bin_temporaria;
		
		while(iterador_bin_S2.hasNext()){
		
			bin_temporaria = iterador_bin_S2.next();
			solucao1.adicionaBin(bin_temporaria);			
		}
		
		//atribui_solucao(solucao1);
		this.solucao = solucao1.retornaSolucao();
	}
	
	public int size(){
		
		return cont;
	}

	public void imprime_solucao(){
			
			if(solucao == null){
				System.out.println("Lista Vazia de Soluções !!!");
			}
			else{
				
				Iterator<Bin> iterator = solucao.iterator();
				int cont;
				
				while(iterator.hasNext()){
					cont = 0;
					
					System.out.println("------------------ BIN ---------------------");
					
					Bin b = iterator.next();
					Queue<No> fila = null;
					ArrayList<No> array = b.imprime(b.root(), fila);
										
					No.imprimeNo(array.get(cont));
					
					while( cont < array.size()){
						
						Iterator<No> iterato = array.get(cont).getFilhos().iterator();
						
						while(iterato.hasNext()){
							
							No.imprimeNo(iterato.next());
						}
							cont++;
					}
				}			
			}		
	}

	/* Tem que ter um método de converte solução parcial em solução completa com posições absoluta
	 * a serem passadas para a classe Desenha solução ou para memórias parciais e completas dos agentes */
}
