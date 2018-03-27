package j_HeuristicaArvoreNAria;

import java.util.Queue;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.Serializable;
import Heuristicas.Individuo;
import ATeam.ETipoHeuristicas;
import HHDInterfaces.IDimensao2d;

public class SolucaoNAria implements Serializable{

	private int    cont = 0;
        private Double FAV;  //Função de Avaliação 1
        private Double FAV2; //Função de Avaliação 2
        private Double Fav;
	private LinkedList<Bin> solucao; 
	private IDimensao2d     tamanhoChapa;
        private int    numLinhasCorte;
        private Double mediaSobras;
        private Double somatorioSobras;
        //private static Individuo seqEncaixe;
        private ETipoHeuristicas etipoHeuristica;
        
        
	public SolucaoNAria(){
            
            solucao = new LinkedList<Bin>();
            this.FAV = 0.0;
            this.FAV2 = 0.0;
            this.Fav = 0.0;
            this.somatorioSobras = null;
            //seqEncaixe = new Individuo();
            cont = 0;
	}
	
        public SolucaoNAria(SolucaoNAria solucao){
        
            this.solucao = new LinkedList<Bin>(solucao.retornaSolucao());
            this.FAV  = solucao.getFAV();
            this.FAV2 = solucao.getFAV2();
            this.Fav  = solucao.get_Fav();
            this.somatorioSobras = solucao.getSomatorioSobra();
            this.tamanhoChapa = solucao.getTamanhoChapa();
            this.etipoHeuristica = solucao.getTipoHeuristic();
            cont = solucao.size();
            //seqEncaixe = new Individuo();
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
        
        public int numBinsSolucao(){
        
            return solucao.size();
        }
	
	public LinkedList<Bin> retornaSolucao(){
		
            return solucao;
	}
	
	public void atribui_solucao(SolucaoNAria outra_solucao){
	
            this.FAV = outra_solucao.getFAV();
            this.FAV2 = outra_solucao.getFAV2();
            this.Fav  = outra_solucao.get_Fav();
            this.somatorioSobras = outra_solucao.getSomatorioSobra();
            this.tamanhoChapa = outra_solucao.getTamanhoChapa();
            this.cont = outra_solucao.size();
            this.solucao = outra_solucao.retornaSolucao();		
	}
	
	public void unir_duas_solucoes(SolucaoNAria solucao1, SolucaoNAria solucao2){
		
            int conta = solucao1.size() + solucao2.size();
            this.cont = conta;

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

        public void calculaSomatorioSobra(){
        
            if(solucao == null){
                System.out.println("Lista Vazia de Soluções !!!");
            }
            else{
                Iterator<Bin> iterator = this.solucao.iterator();
                int j = 0;
                float arealivre, somatorio = 0;
                Bin b;
                //Testar cálculo aqui de FAV e FAV2!!
                Double FAV = 0.0, FAV2 = 0.0;
                Double sobra_bin = 0.0, MenorAp = 0.0;
                ArrayList<Double> FAVCorrente = new ArrayList<Double>(); //Lista de FAVs
                ArrayList<Double> listSobras = new ArrayList<Double>(); //Sobras referentes de cada placa !
        
                while(iterator.hasNext()){
                    
                    b = iterator.next();
                    arealivre = b.root().getMaxima_area_livre();
                    
                    b.setFav(new Double(Utilidades.Funcoes.getChapa().retorneArea() - arealivre));//ou this.getChapa().retorneArea();
                    b.setSobra(new Double(arealivre));
                    
                    
                    somatorio = somatorio + arealivre;
                    
                    FAV = FAV + b.getFav();
                    System.out.println("FAV M2 Melhorado --> "+FAV);
                    
                    sobra_bin = 100 - (b.getFav()/(Utilidades.Funcoes.getChapa().retorneArea())*100);
                    listSobras.add(sobra_bin);
                    //b.setAproveitamento(Utilidades.Funcoes.getChapa().retorneArea() - arealivre);
                    b.setAproveitamento(100 - (sobra_bin.floatValue())); 
                    if (sobra_bin > MenorAp){
                        MenorAp = sobra_bin;                	                	
                    }
                    j++;
                    
                    //Atualizo a sequência de encaixe da solução
                    //b.getSequenciaInd().setListaItens2(b.getSequenciaInd().getListaItens());
                    
                }
                
                this.set_Fav(FAV);
                
                FAV2 = ((FAV)+MenorAp)/j;
                FAV = FAV/(j*(Utilidades.Funcoes.getChapa().retorneArea()));
                        
                this.setFAV(FAV);
                this.setFAV2(FAV2);
                System.out.println("Somatorio Arvore "+ somatorio);
                this.setSomatorioSobra(somatorio);
                
            }                   
        }
        
        public Double getSomatorioSobra(){
        
            return this.somatorioSobras;
        }
        
        public void setSomatorioSobra(float somatorio){
        
            this.somatorioSobras = new Double((double) somatorio);
        
        }
        
//        public static Individuo getSequenciaInd(){
//        
//            return seqEncaixe;
//        }
//        
//	public void setSequenciaInd(Individuo ind){
//            
//            seqEncaixe = ind;
//        }	
        public void imprime_solucao(){
			
            if(solucao == null){
                    System.out.println("Lista Vazia de Soluções !!!");
            }
            else{

                Iterator<Bin> iterator = solucao.iterator();
                int conte;

                System.out.println("Agente -> "+getTipoHeuristic());
                //System.out.println("Sequencia Encaixe --> [ "+getSequenciaInd().getListaItens()+" ]");
                while(iterator.hasNext()){
                    
                    conte = 0;

                    System.out.println("------------------ BIN ---------------------");

                    Bin b = iterator.next();
                    System.out.println("Fav   --> "+ b.getFav());
                    System.out.println("Sobra --> "+ b.getSobra());
                    System.out.println("Aproveitamento -->"+ b.getAproveitamento()+"\n");
                                        
                    Queue<No> fila = null;
                    ArrayList<No> array = b.imprime(b.root(), fila);

                    No.imprimeNo(array.get(conte));

                    while( conte < array.size()){

                        Iterator<No> iterato = array.get(conte).getFilhos().iterator();

                        while(iterato.hasNext()){

                            No.imprimeNo(iterato.next());
                        }
                        
                        conte++;
                    }
                }			
            }		
	}
        public Double get_Fav(){
    
            return this.Fav;
        }
        
        public void set_Fav(Double fav){
            
            this.Fav = fav;
        }
        
        public Double getFAV() {
        
            return FAV;
        }

        public void setFAV(Double fAV) {
            
            FAV = fAV;
        }

        public Double getFAV2() {    
        
            return FAV2;
        }

        public void setFAV2(Double fAV2) {
        
            FAV2 = fAV2;
        }
        
        public IDimensao2d getTamanhoChapa() {
        
            return tamanhoChapa;
        }
        
        public void setTamanhoChapa(IDimensao2d tamanhoChapa) {
        
            this.tamanhoChapa = tamanhoChapa;
        }
        //Aqui armazena a história das soluções na memória
        public ETipoHeuristicas getTipoHeuristic(){
    
            return etipoHeuristica;
        }    
    
        public void  setTipoHeuristic(ETipoHeuristicas etipoHeuristica){
            
            this.etipoHeuristica = etipoHeuristica;
        }
        
        public String getNameAgente(){
    
            return getTipoHeuristic().toString();
        }
        /* Tem que ter um método de converte solução parcial em solução completa com posições absoluta
	* a serem passadas para a classe Desenha solução ou para memórias parciais e completas dos agentes */

}
