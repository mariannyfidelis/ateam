package j_HeuristicaArvoreNAria;

import Heuristicas.Individuo;
import Utilidades.Chapa;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

public class Bin implements Serializable{

	public int id;
        public No raiz;
        private Double Fav;
        private Double Sobra;
	private float aproveitamento;
        //private static Individuo sequenciaInd;
        
	public Bin(){
		
            raiz = null;
            Fav = 0.0;
            //sequenciaInd = new Individuo();
            //Sobra = 0.0;
	}
	
	public int getID(){
		
            return id;
	}
	
	public void setID(int idi){
		
            id = idi;
	}
        
        public Double getFav() {
    
            return Fav;
        }
        
        public void setFav(Double Fav) {
        
            this.Fav = Fav;
        }

        public Double getSobra() {
       
            return Sobra;
        }

        public void setSobra(Double Sobra) {
        
            this.Sobra = Sobra;
        }
        
        public float getAproveitamento(){
        
            return this.aproveitamento;
        }
        
        public void setAproveitamento(float aproveitamento){
        
            this.aproveitamento = aproveitamento;
        }
        
//        public static Individuo getSequenciaInd(){
//        
//            return sequenciaInd;
//        }
//        
//	public void setSequenciaInd(Individuo ind){
//            
//            sequenciaInd = ind;
//        }
        
	public void adiciona_item(No item){
		
            raiz = item;	
	}
	
	public No root(){
		
            return raiz;
	}
		
	public void setRoot(No elemento){
			
            raiz = elemento;		
	}
		
	public Iterator<No> elements(No root){
		
            Iterator<No> iterator = root.children();

            return iterator;
	}

	public static ArrayList<No> positions(No root){
	
            ArrayList<No> array = new ArrayList<No>();
            Iterator<No> iterator = root.children();

            while(iterator.hasNext()){

                    array.add(iterator.next());
            }

            return array;
	}

	public int size(){

            ArrayList<No> a = positions(raiz);

            return a.size();
	}
	
	public boolean estaVazia(){
		
            if(raiz == null){

                    return true;	
            }

            return false;
	}
	
	public static LinkedList<Pedidos> busca_largura(No root, Queue<No> fila){ 
		
            fila = new LinkedList<No>();
            LinkedList<No> caminho = new LinkedList<No>();

            LinkedList<Pedidos> lista_pedidos = new LinkedList<Pedidos>();

            Iterator<No> iterator; 
            No no;

            fila.add(root);

            while(!fila.isEmpty()){

                    no = fila.poll(); 

                    if(no.getItem() != null){
                            int cont = 1;

                             lista_pedidos.add(new Pedidos(cont, no.getItem().getLargura(), no.getItem().getAltura())); 
                             caminho.add(no); //por enquanto fica aqui (Será necessário ????)

                            cont++;
                    }

                    no.calcula_retangulo_maximo(no, false);
                    no.calcula_slack(no);
                    no.setMaxima_area_livre(no.maior_area_livre(no));

                    iterator = no.children();

                    while(iterator.hasNext()){

                            no = iterator.next();
                            fila.add(no);
                    }
            }

            return lista_pedidos;
	}

	public LinkedList<Pedidos> getItensBin(Bin bin){
		
		Queue<No> fila = null;
		LinkedList<Pedidos> array_itens = new LinkedList<Pedidos>();
		
		array_itens = busca_largura(bin.root(), fila);
		
		return array_itens;
	}
	public ArrayList<No> busca_profundidade(No inicial, ArrayList<No> caminho){ 
		
            Iterator<No> iterator = inicial.children(); 
            No vert;

            caminho.add(inicial);

            while (iterator.hasNext()) {
                    vert = (No) iterator.next();
                    busca_profundidade(vert, caminho);
            }

            return caminho;
	}
	
    public boolean padrao_viavel(No root){

        if((root.getRetanguloMinimo().getW_p()) <= ((float) Chapa.getLargura())){
                System.out.println("Padrão Válido !!!");

                return true;
        }
        else{
                return false;
        }
    }

    public static void InsertAsSubnode(No pai, No filho, boolean rotacao_item){
	
//        idB = filho.getItem().getId();
//        areaIdB = filho.getItem().getArea();
//        sequenciaInd.adicionaItemLista(idB, areaIdB);
        
            No no, root = null;
            boolean rotaciona = false;
		
            filho.getItem().setRotacao(rotacao_item);
            filho.calcula_area_padrao(filho);

            if(pai.getItem() == null){

                pai.getFilhos().add(filho);
                filho.setPai(pai);

                root = pai;
            }

            //no = filho.getPai();
            no = filho;

            while(no != null){

                    if(no.getItem() == null){

                            rotaciona = false;

                    }else{
                            rotaciona = no.getItem().getRotacao();
                     }

                    no.calcula_retangulo_minimo(no, rotaciona);
                    no.calcula_area_padrao(no);

                    if(rotaciona == false){

                        if(no.getTipo_corte().corte == No_orientacao.horizontal){

                            Funcoes.ordenaPedidosDecrescenteRetMinimo(false, no.getFilhos());
                        }
                        else{
                            Funcoes.ordenaPedidosDecrescenteRetMinimo(true, no.getFilhos());
                        }

                    }

                    if(no != null){
                            root = no;
                    }

                    no = no.getPai();
            }

            Queue<No> fila = null;
            busca_largura(root, fila); 
	}

//        static int idB;
//        static float areaIdB;
	public static No InsertVert_HorizRoot(No raiz, No filho, No_orientacao corte, boolean rotacao_item){
		
//                idB = filho.getItem().getId();
//                areaIdB = filho.getItem().getArea();
//                sequenciaInd.adicionaItemLista(idB,areaIdB);
		No novo;
		boolean node_v;
		
		filho.getItem().setRotacao(rotacao_item);
		filho.calcula_area_padrao(filho);
		
		if(corte == No_orientacao.vertical){
			
                    System.out.println("Meu tipo de corte é Vertical");
                    novo = new No(true);
                    novo.setPai(null);
                    
                    System.out.println("Novo nó criado");
                    
                    novo.getFilhos().add(filho);			
		
                    node_v = true;
                }
		else{
			
			System.out.println("Meu tipo de corte é Horizontal");
			novo = new No(false);
			novo.setPai(null);
			
			System.out.println("Novo nó criado");
									
			novo.setFilhos(filho);
			
			node_v = false;
		}	
		
		novo.getFilhos().add(raiz);
		
		raiz.setPai(novo); 
		filho.setPai(novo);
		
		Iterator<No> iterator = novo.getFilhos().iterator();
	
		while(iterator.hasNext()){
			No.imprimeNo(iterator.next());
		}
		
		filho.calcula_retangulo_minimo(filho, filho.getItem().getRotacao());
		novo.calcula_retangulo_minimo(novo, false);
		novo.calcula_area_padrao(novo);
		
		Funcoes.ordenaPedidosDecrescenteRetMinimo(node_v, novo.getFilhos());
		
                raiz = novo;
		
                return raiz;
	}
	
	public static void InsertInParallelTo(No no_item, No no_k){
//		
//            idB = no_item.getItem().getId();
//            areaIdB = no_item.getItem().getArea();
//	    sequenciaInd.adicionaItemLista(idB, areaIdB);
            
            No novo, aux_pai = null, no_aux = null;
		
		no_aux = no_k;
		aux_pai = no_aux.getPai();
		
		if(no_k.getItem() != null){
			
			System.out.println("\nO nó escolhido é Item !!!");
			No.imprimeNo(no_k);
			
			if(aux_pai.getTipo_corte().corte == No_orientacao.horizontal){
				
				novo = new No(true);
				novo.getFilhos().add(no_item);
				no_item.setPai(novo);
			}
			else{
				novo = new No(false);
				novo.getFilhos().add(no_item);
				no_item.setPai(novo);
			}
			
			novo.getFilhos().add(no_k);
			
			//Depois de criar um novo nó e adicionar os dois filhos deve-se calcular o retângulo Mínimo
			novo.calcula_retangulo_minimo(novo, false);
			novo.calcula_area_padrao(novo);
			
			aux_pai.getFilhos().add(novo);
			aux_pai.getFilhos().remove(no_k);
			novo.setPai(aux_pai);
			
			no_k.setPai(novo);
			
			No pai = novo.getPai();
			No root = pai;
			
			while(pai != null){
				
				pai.calcula_retangulo_minimo(pai, false);
				pai.calcula_area_padrao(pai);
				
				if(pai != null){
					root = pai;
				}
				
				pai = pai.getPai();
			}
			Queue<No> fila = null;
			
			busca_largura(root, fila);
			//Pode ser que seja necessário o cálculo de Retângulo Máximo aqui depois de calcular todos os retângulos Mínimos
			
			System.out.println("\nCriei um nó e adicionei item e o no K a ele !!!");
			
			//Verificar se é necessário a validação da inserção com itens!!!
		}
		else if(no_k.getItem() == null){
			
			boolean bool = true;
			
			System.out.println("\nO nó escolhido não é Item !!!");
			
			if(no_k.getTipo_corte().corte ==  No_orientacao.horizontal){
				
				bool = false;
				novo = new No(true);
				novo.getFilhos().add(no_item);
				no_item.setPai(novo);
			}
			else{
				
				bool = true;
				novo = new No(false);
				novo.getFilhos().add(no_item);
				no_item.setPai(novo);
			}

			int cont = (no_k.getFilhos().size() -1);
			ArrayList<No> array = new ArrayList<No>();
			
			if(cont > 1){
				
				array = Bin.positions(no_k);
				No aux = array.get((array.size()-1));
				No novo2 = new No(bool);
				
				no_k.getFilhos().remove(aux);
				no_k.calcula_retangulo_minimo(no_k, false);
				
				novo.getFilhos().add(no_k);
				novo.calcula_retangulo_minimo(novo, false);
				
				no_k.setPai(novo);//olhar se é melhor aqui ou lá embaixo
				
				novo2.getFilhos().add(novo);
				novo2.getFilhos().add(aux);
				
				novo.setPai(novo2);
				aux.setPai(novo2);
				
				novo2.calcula_retangulo_minimo(novo2, false);
				
				novo2.setPai(aux_pai);
				aux_pai.getFilhos().add(novo2);
				aux_pai.getFilhos().remove(no_k);
				
				//no_k.setPai(novo);
				//Deve se calcular o while dos retângulos mínimos aqui !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				//pegar código lá em AsSubnode !!!!!!!!!!!!!!!
				aux_pai.calcula_retangulo_minimo(aux_pai, false);
				
				No pai = aux_pai.getPai();
				No root = pai;
				
				while(pai != null){
					
					pai.calcula_retangulo_minimo(pai, false);
					pai.calcula_area_padrao(pai);
				
					if(pai != null){
						
						root = pai;
					}
					
					pai = pai.getPai();
				}
				
				Queue<No> fila = null;
				busca_largura(root, fila);				
			} 
			else{
				
				novo.getFilhos().add(no_k);
				novo.calcula_retangulo_minimo(novo, false);
				
				if((novo.getRetanguloMinimo().getW_p() > aux_pai.getRetanguloMaximo().getW_P() || 
																	 novo.getRetanguloMinimo().getH_p() > aux_pai.getRetanguloMaximo().getH_P())
						||(novo.getTipo_corte().corte == aux_pai.getTipo_corte().corte)
						
						){
					
					System.out.println("O retângulo mínimo do novo nó está estrapolando seus limites ");
					
					no_item.setPai(null);
					novo.getFilhos().remove(no_item);
					novo.getFilhos().remove(no_k);
				}
				//Verificar se é necessário esse ELSE_AQUI !!!
				else{
					novo.setPai(aux_pai);
					aux_pai.getFilhos().add(novo);
					
					no_k.setPai(novo);
					
					No pai = novo.getPai();
					
					while(pai != null){
						
						pai.calcula_retangulo_minimo(pai, false);
						pai.calcula_area_padrao(pai);
						
						pai = pai.getPai();
					}
				}
			}
		}
	}//fim do método 

	
	//Novo método InsertInParalellTo  2 !!!!!
	
	
	public static void InsertInParallelTo2(No raiz_insert, No no_item, int no_j, int no_k2, boolean rotacao_item){
		
		int var = no_k2 - (no_j - 1);
		boolean rotaciona, node_v;
		
//                idB = no_item.getItem().getId();
//                areaIdB = no_item.getItem().getArea();
//                sequenciaInd.adicionaItemLista(idB, areaIdB);
		
                No novo;
		
		no_item.getItem().setRotacao(rotacao_item);
		no_item.calcula_retangulo_minimo(no_item, no_item.getItem().getRotacao());
		no_item.calcula_area_padrao(no_item);
				
		ArrayList<No> array = new ArrayList<No>();
		array = Bin.positions(raiz_insert);
		
		if(raiz_insert.getTipo_corte().corte == No_orientacao.horizontal){
				
			novo = new No(true);
			novo.getFilhos().add(no_item);
			no_item.setPai(novo);
			
			node_v = true;
		}
		else{
			novo = new No(false);
			novo.getFilhos().add(no_item);
			no_item.setPai(novo);
			
			node_v = false;
		}
			
		if(var == 1){
			
                    No aux = array.get(no_j);
	                     
                    novo.getFilhos().add(aux);
                    aux.setPai(novo);
		
                    novo.calcula_retangulo_minimo(novo, false);
                    novo.calcula_area_padrao(novo);
	        
                    Funcoes.ordenaPedidosDecrescenteRetMinimo(node_v, novo.getFilhos());
	        
                    novo.setPai(raiz_insert);
                    raiz_insert.getFilhos().remove(aux);
                    raiz_insert.getFilhos().add(novo);
	        
                    No pai = novo.getPai();
                    No root = pai;
		
                    while(pai != null){
		
                        if(pai.getItem() == null){
                            rotaciona = false;
                        }
                        else{
                            rotaciona = pai.getItem().getRotacao();
                        }
		        
                        pai.calcula_retangulo_minimo(pai, rotaciona);
                        pai.calcula_area_padrao(pai);
			
                        if(rotaciona == false){
		    	
                            if(pai.getTipo_corte().corte == No_orientacao.horizontal){
			
                                Funcoes.ordenaPedidosDecrescenteRetMinimo(false, pai.getFilhos());
                            }
                            else{
                                Funcoes.ordenaPedidosDecrescenteRetMinimo(true, pai.getFilhos());
                            }		    	   		    	   
                        }
		       
                        if(pai != null){
                            root = pai;
                        }
			
                        pai = pai.getPai();
                    }
                    
                    Queue<No> fila = null;
                    busca_largura(root, fila);
                }
		
                else if(var > 1){

                    No novo2, aux_filho;
                    boolean node_v2;
		
                    if(raiz_insert.getTipo_corte().corte == No_orientacao.horizontal){
		
                        novo2 = new No(false);
                        node_v2 = false;
                    }
                    else{
		
                        novo2 = new No(true);
                        node_v2 = true;
                    }
		
                    while(no_j  <= no_k2){
			   
                        aux_filho = array.get(no_j);
                        novo2.getFilhos().add(aux_filho);
                        aux_filho.setPai(novo2);
			
                        raiz_insert.getFilhos().remove(aux_filho);
			   
                        no_j++;
                    }
		
                    novo2.calcula_retangulo_minimo(novo2, false);
                    novo2.calcula_area_padrao(novo2);
			   
                    Funcoes.ordenaPedidosDecrescenteRetMinimo(node_v2, novo2.getFilhos());
			   
                    novo.getFilhos().add(novo2);
                    novo2.setPai(novo);			   
			   
                    novo.calcula_retangulo_minimo(novo, false);
                    novo.calcula_area_padrao(novo);
		
                    Funcoes.ordenaPedidosDecrescenteRetMinimo(node_v, novo.getFilhos());
		
                    raiz_insert.getFilhos().add(novo);
                    novo.setPai(raiz_insert);
		
                    No pai = novo.getPai();
                    No root = pai;
		
                    while(pai != null){
				
                        if(pai.getItem() == null){
                            rotaciona = false;
                        }
                        else{
                            rotaciona = pai.getItem().getRotacao();
                        }

                        pai.calcula_retangulo_minimo(pai, rotaciona);
			pai.calcula_area_padrao(pai);
			
                        if(rotaciona == false){
		    	
                            if(pai.getTipo_corte().corte == No_orientacao.horizontal){
			
                                Funcoes.ordenaPedidosDecrescenteRetMinimo(false, pai.getFilhos());
                            }
                            else{
                                Funcoes.ordenaPedidosDecrescenteRetMinimo(true, pai.getFilhos());
                            }
                        }
		       
                        if(pai != null){
                            root = pai;
                        }
			
                        pai = pai.getPai();
                    }
		
                    Queue<No> fila = null;
                    busca_largura(root, fila);
                }
        }
	
	public ArrayList<No> imprime(No raiz, Queue<No> fila){
				
		fila = new LinkedList<No>();
		ArrayList<No> caminho = new ArrayList<No>();
		Iterator<No> iterator; 
		No no;
			
		fila.add(raiz);
		
		while(!fila.isEmpty()){
			
			no = fila.poll();
			caminho.add(no);
			
			iterator = no.children(); 
			
			while(iterator.hasNext()){
			
				no = iterator.next();
				fila.add(no);
			}
		}
		
		return caminho;
	}	
}