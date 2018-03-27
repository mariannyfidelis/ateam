package j_HeuristicaArvoreNAria;

import HHDInternal.Dimensao2D;
import Utilidades.Chapa;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Metodos_heuristicos{

    LinkedList<Bin> solution = null;

    int var_rot;
    int no_j, no_k;
    int bin_id = 1;

    boolean rotacao_item = false;
    boolean ant_rotacao_item;
    boolean indica_rotacao;

    No raiz_insert = null;
    No no__subnode = null;
    No no__insert = null;

/*
 * 
 * MÉTODO DE INSERÇÃO FIRST E BEST FIT -->>> HEURÍSTICA DE CONSTRUÇÃO DE SOLUÇÕES !!!!!!
 * 
 * */	

    public SolucaoNAria FFIH_BFIH(boolean rotaciona, boolean firstFit, LinkedList<Pedidos> listaPedidos, Chapa chapa){
	
        indica_rotacao = rotaciona;

        if(indica_rotacao == true)	var_rot = 2;
        else var_rot = 1;

        //Funcoes.ordenaPedidosDecrescente(listaPedidos);

        int i, cont = 0, idBin;
        int index = 0;
        Pedidos pedido;

        No no;
        No raiz  = null;
        No raiz1 = null;
        Bin bin_ = null;
        Iterator<Bin> iterator;			
        TiposInsercao insBest = TiposInsercao.Vazio;

        SolucaoNAria num_bins = new SolucaoNAria();

        InformacaoInsercao informacaoInsercao;
        ArrayList<InformacaoInsercao> array_informacao_insercao = new ArrayList<InformacaoInsercao>();
        ArrayList<Integer> array_id = new ArrayList<Integer>();

        for (i = 0 ; i < listaPedidos.size(); i++){
        
            insBest = TiposInsercao.Vazio;
            
            pedido = (Pedidos) listaPedidos.get(i);

            System.out.println("\n\nPedido "+(i+1));
            pedido.imprimePedido();

            no = new No(i+1,pedido.retornaLargura(),pedido.retornaAltura());
            no.setPai(null);
            no.calcula_retangulo_minimo(no, false);
            //no.calcula_retangulo_maximo(no, false);
            //Deve-se Limpar as informacoes do array agora para o novo pedido
            InformacaoInsercao inf = new InformacaoInsercao();

            for (InformacaoInsercao ing : array_informacao_insercao) {

                array_informacao_insercao.set(index, inf);
                index++;
            }

            iterator = num_bins.listaBinSolucao();

            while(iterator.hasNext()){

                cont++;
                bin_  = iterator.next();
                idBin = bin_.getID();
                raiz  = bin_.root();

                if(no.getItem().getArea() <= raiz.getMaxima_area_livre()){	

                    informacaoInsercao = InsEnum(bin_, no, raiz);
                    insBest = informacaoInsercao.getInsBest(); 		
                    array_informacao_insercao.set(idBin -1, informacaoInsercao);
                }

                if((firstFit == true) && (insBest != TiposInsercao.Vazio)){

                    System.out.println("Entrei no loop FIRST FIT\n");

                    break;
                }
                //if(firstFit == false){

                        //array_id.add(idBin);
                //}
            }

            informacaoInsercao = InformacaoInsercao.retornaMelhorSolucao(array_informacao_insercao, cont);

            cont = 0;//Atualiza novamente o contador !!! 
            index = 0;

            bin_    = informacaoInsercao.getBin_Insercao();
            insBest = informacaoInsercao.getInsBest();
            raiz    = informacaoInsercao.getBin_Insercao().root();
            raiz_insert  = informacaoInsercao.getRaiz_Insert();
            no__subnode  = informacaoInsercao.getAsSubnode();
            rotacao_item = informacaoInsercao.getRotacao_Item();
            no_j = informacaoInsercao.getNo_j();
            no_k = informacaoInsercao.getNo_k();

            /*Aqui deve-se atualizar a Função de Avaliação (FAV) da placa*/
            if(insBest != TiposInsercao.Vazio){

                if (insBest == TiposInsercao.HorizRoot){

                    System.out.println("AQUI VOU INSERIR HORIZ ROOT !!!!");
                    System.out.println("Bin fav --> "+ bin_.getFav()+"\n");
                    bin_.setFav(bin_.getFav() + no.getItem().getArea());
                    System.out.println("Bin fav depois --> "+ bin_.getFav()+"\n");
                    
                    raiz1 = Bin.InsertVert_HorizRoot(raiz, no, No_orientacao.horizontal, rotacao_item);
                    bin_.setRoot(raiz1);

                    Queue<No> fila = null;
                    Bin.busca_largura(raiz1, fila);
                    
                }
                else if (insBest == TiposInsercao.VertRoot){

                    System.out.println("AQUI VOU INSERIR VERT ROOT !!!!");
                    System.out.println("Bin fav --> "+ bin_.getFav()+"\n");
                    bin_.setFav(bin_.getFav() + no.getItem().getArea());
                    System.out.println("Bin fav depois --> "+ bin_.getFav()+"\n");
                    
                    raiz1 = Bin.InsertVert_HorizRoot(raiz, no, No_orientacao.vertical, rotacao_item);
                    bin_.setRoot(raiz1);

                    Queue<No> fila = null;
                    Bin.busca_largura(raiz1, fila);

                }
                else if (insBest == TiposInsercao.AsSubnode){

                    System.out.println("AQUI VOU INSERIR ASSUBNODE!!");
                    System.out.println("Bin fav --> "+ bin_.getFav()+"\n");
                    bin_.setFav(bin_.getFav() + no.getItem().getArea());
                    System.out.println("Bin fav depois --> "+ bin_.getFav()+"\n");
                    
                    Bin.InsertAsSubnode(no__subnode, no, rotacao_item);

                }
                else if (insBest == TiposInsercao.InParallelTo){

                    System.out.println("AQUI VOU INSERIR INPARELL TO!!!");
                    System.out.println("Bin fav --> "+ bin_.getFav()+"\n");
                    
                    bin_.setFav(bin_.getFav() + no.getItem().getArea());
                    System.out.println("Bin fav depois --> "+ bin_.getFav()+"\n");
                    
                    Bin.InsertInParallelTo2(raiz_insert, no, no_j, no_k, rotacao_item);
                } else{}

            }
            else{

                System.out.println("\nAqui adiciono o item  a uma nova bin !!!");

                Bin bins = new Bin();
                bins.setID(bin_id);
                System.out.println("Fav da bin antes --> "+ bins.getFav());
                bins.setFav(new Double(bins.getFav().floatValue()+no.getItem().getArea()));
                System.out.println("Fav da bin depois --> "+ bins.getFav());
                
                no.calcula_retangulo_maximo(no, true);
                no.calcula_slack(no);

                no.setMaxima_area_livre(no.maior_area_livre(no));

                bins.adiciona_item(no);
                bins.setRoot(no);

                num_bins.adicionaBin(bins);

                bin_id = bin_id + 1; 

                informacaoInsercao.setBin_Insercao(bins);
                array_informacao_insercao.add(informacaoInsercao);
            }
        }//fim for

        /*Aqui termina o laço e todas as bins estão com suas FAV calculas
        então aplica-se o cálculoFAV_FAV2 */
        num_bins.setTamanhoChapa(new Dimensao2D((float) chapa.getLargura(), (float) chapa.getAltura()));
        
        calculaFAV_FAV2(num_bins, chapa);
        
        return num_bins; //solucaoNAria
    }
	
	
    public InformacaoInsercao InsEnum(Bin bin, No item_, No raiz){ 
		
		Item item = item_.getItem();
		
		InformacaoInsercao informacaoInsercao = new InformacaoInsercao(); 
		
		float w_,h_,n_;
		float ant_fitness = 0, fitness, melhor_fitness = 0;
		float wi = item.getLargura(), hi = item.getAltura();
		TiposInsercao insBest = TiposInsercao.Vazio, ant_insBest = TiposInsercao.Vazio; 
		
		
		for(int i = 0; i < var_rot; i++){ 
												
			switch (i+1) {
				case 1:
				
					wi = item.getLargura();
					hi = item.getAltura();
					item.setRotacao(false);
					
					break;
				case 2:
					
					wi = item.getAltura();
					hi = item.getLargura();
					item.setRotacao(true);
					
					break;
				
				default:
					break;
			}
			
			if(!(raiz.getTipo_corte().corte == No_orientacao.horizontal)){
					
				w_ = ( (raiz.getRetanguloSlack().getW_s_p() ) - wi);
				h_ = ( (float) Chapa.getAltura() - hi );
				n_ = Math.abs( (raiz.getRetanguloMinimo().getH_p()) - hi );
					
				fitness = raiz.fitness(w_, h_, n_);
				System.out.println("FITNESS ----->>> "+fitness);
				
								
				if((w_ >= 0) && (fitness > melhor_fitness) ){
					
					ant_fitness = melhor_fitness;
					melhor_fitness = fitness;
					ant_insBest = insBest;
					insBest = TiposInsercao.HorizRoot;
					rotacao_item = item.getRotacao();
					System.out.println("Devo inserir HorizRoot !!!\n");
					
					//Atribuir valores ao objeto InformacaoInsercao
					
					informacaoInsercao.setBin_Insercao(bin);
					informacaoInsercao.setFitness(melhor_fitness);
					informacaoInsercao.setInsBest(insBest);
					informacaoInsercao.setRotacao_Item(rotacao_item);
					informacaoInsercao.setAsSubnode(null);
					informacaoInsercao.setRaiz_Insert(null, 0, 0);
				}				
			}
			
			if(!(raiz.getTipo_corte().corte == No_orientacao.vertical)){
					
				w_ = ((float) Chapa.getLargura() - wi);
				h_ = ( (raiz.getRetanguloSlack().getH_s_p()) - hi);
				n_ = Math.abs((raiz.getRetanguloMinimo().getW_p()) - wi);
				
				fitness = raiz.fitness(w_, h_, n_);
				System.out.println("FITNESS ----->>> "+fitness);
				
				if((h_ >= 0) && (fitness > melhor_fitness) ){
					
					ant_fitness = melhor_fitness;
					melhor_fitness = fitness;
					ant_insBest = insBest;
					insBest = TiposInsercao.VertRoot;
					rotacao_item = item.getRotacao();
					System.out.println("Devo inserir VertRoot !!!\n");
					
					//Atribuir valores ao objeto InformacaoInsercao
					
					informacaoInsercao.setBin_Insercao(bin);
					informacaoInsercao.setFitness(melhor_fitness);
					informacaoInsercao.setInsBest(insBest);
					informacaoInsercao.setRotacao_Item(rotacao_item);
					informacaoInsercao.setAsSubnode(null);
					informacaoInsercao.setRaiz_Insert(null, 0, 0);
				}	
			}//end-if
			
			if(melhor_fitness == 1){
				
                            System.out.println("Vou dar um break em InsEnum!!!");
			
                            return informacaoInsercao;
			}
	    }//end for
			
	    if(raiz.getTipo_corte().corte == No_orientacao.horizontal){
			    	
	    	System.out.println("Vou chamar o método InsEnumHnode !!!");
	    	informacaoInsercao = InsEnumHNode(item_,raiz, melhor_fitness, ant_fitness, insBest, ant_insBest,informacaoInsercao);
	    	insBest = informacaoInsercao.getInsBest();//Talvez não seja necessário aqui !!! 
	    }
	    else if(raiz.getTipo_corte().corte == No_orientacao.vertical){
	    	
	    	System.out.println("Vou chamar o método InsEnumVnode !!!");
	    	informacaoInsercao = InsEnumVNode(item_,raiz, melhor_fitness, ant_fitness, insBest, ant_insBest,informacaoInsercao);
	    	insBest =informacaoInsercao.getInsBest();//Talvez não seja necessário aqui !!!
	    }
	    
	    return informacaoInsercao;
	    
	}//fim do método InsEnum
	
	
	public InformacaoInsercao InsEnumHNode(No item_, No raiz, float melhor_fitness, float ant_fitness, TiposInsercao insBest,TiposInsercao ant_insBest, InformacaoInsercao informacaoInsercao){
		
		Item item = item_.getItem();
		boolean valida = false; 
		
		float w_ = 0, h_ = 0, n_ = 0;
		float fitness;
		float wi = item.getLargura(), hi = item.getAltura();
		
		for(int i = 0; i < var_rot; i++){  
			
			switch (i+1){
				case 1:
					wi = item.getLargura();
					hi = item.getAltura();
					item.setRotacao(false);
					
					break;
				case 2:
					wi = item.getAltura();
					hi = item.getLargura();
					item.setRotacao(true);
					
					break;
				default:
					break;
			}
			
			if((wi >= raiz.getRetanguloMaximo().getW_P())){// || (hi >= raiz.getRetanguloMaximo().getH_P())){ 
				
				System.out.println("Entrei no continue\n\n");
				continue;
			}
		
			System.out.println("\nVariáveis para AsSubnode ");
					
			w_ = raiz.getRetanguloSlack().getW_s_p() - wi; System.out.println("\nw_ --> "+w_);
			h_ = raiz.getRetanguloMaximo().getH_P() - hi;  System.out.println("\nh_ --> "+h_);
			n_ = No.min(raiz, hi);						   System.out.println("\nn_ --> "+n_);
  		    			
			fitness = raiz.fitness(w_,h_,n_);
			System.out.println("\nFitness para AsSubnode --->> "+fitness);
			
			if((w_ >= 0) && fitness > melhor_fitness ){
				
				ant_fitness = melhor_fitness;
				melhor_fitness = fitness;
				
				ant_insBest = insBest;
				insBest = TiposInsercao.AsSubnode;
				
				ant_rotacao_item = rotacao_item;
				rotacao_item = item.getRotacao();
				no__subnode = raiz;
				
				System.out.println("Devo inserir AsSubnode\n");
				
				//Aqui deve atribuir o objeto Informacaoinsercao
				
				informacaoInsercao.setAsSubnode(no__subnode);
				informacaoInsercao.setRaiz_Insert(null, 0, 0);
				informacaoInsercao.setFitness(melhor_fitness);
				informacaoInsercao.setInsBest(insBest);
				informacaoInsercao.setRotacao_Item(rotacao_item);
			}	
			
			if(melhor_fitness == 1){
				System.out.println("Vou dar um break em InsEnumHNode");//Imprimir qual o no pai nessa inserção
				
				return informacaoInsercao;
			}

				int k,j = 0;   //Índice do início da subsequência !!!
				
				ArrayList<No> array = Bin.positions(raiz);
				No no = array.get(j);
				No no2 = null;
				float w_sum = 0; 

				while((j <= (array.size()-1)) && (hi + no.getRetanguloMinimo().getH_p() > raiz.getRetanguloMaximo().getH_P())){
				
					j = j + 1;
					
					if(j <= (array.size()-1)){
						
						no = array.get(j);
						System.out.println("j --> "+j);
					}
				}
				
				k = j;  	//Índice do fim da subsequência !!!
				
				System.out.println("Saindo o NO_J como :  "+ j);
				
				if(k <= (array.size()-1)){
					
					no2 = array.get(k);
					w_sum = no2.getRetanguloMinimo().getW_p();
				}
				 
				while(k <= (array.size()-1)){
					
					no = array.get(j); 
					
					System.out.println("\nVariáveis para InParalellTo ");
					
					w_ = raiz.getRetanguloSlack().getW_s_p() + w_sum - wi;   						 System.out.println("\nw_ --> "+w_);
					h_ = raiz.getRetanguloMaximo().getH_P() - no.getRetanguloMinimo().getH_p() - hi; System.out.println("\nh_ --> "+h_);
					n_ = Math.abs(w_sum - wi);														 System.out.println("\nn_ --> "+n_);
					
					fitness = raiz.fitness(w_,h_,n_); 
					System.out.println("\nFitness para InParallelTo --->> "+fitness);
					
					if((w_ >= 0) && (fitness > melhor_fitness)){
						
						ant_fitness = melhor_fitness; 
						melhor_fitness = fitness;
						
						ant_insBest = insBest;
						insBest = TiposInsercao.InParallelTo;
						
						ant_rotacao_item = rotacao_item;
						rotacao_item = item.getRotacao();
						raiz_insert = raiz;
						
						no_j = j;
						no_k = k;
																		
						informacaoInsercao.setAsSubnode(null);
						informacaoInsercao.setRaiz_Insert(raiz_insert, no_j, no_k);
						informacaoInsercao.setFitness(melhor_fitness);
						informacaoInsercao.setInsBest(insBest);
						informacaoInsercao.setRotacao_Item(rotacao_item);
						
						
						valida = verificaInParallelTo2(raiz_insert, item_, no_j, no_k, rotacao_item);
						
						if(valida == false){
							
							if(insBest == TiposInsercao.InParallelTo){
								
								informacaoInsercao.setRaiz_Insert(null, 0, 0);
							}
							
							System.out.println("Vou validar retornando falso !!!\n\n");
							
							//Re-atribui as variáveis anteriores
							
							rotacao_item = ant_rotacao_item;
							melhor_fitness = ant_fitness;
							insBest = ant_insBest;
							
							if(insBest == TiposInsercao.HorizRoot){
								
								informacaoInsercao.setAsSubnode(null);
								informacaoInsercao.setFitness(melhor_fitness);
								informacaoInsercao.setInsBest(insBest);
								informacaoInsercao.setRotacao_Item(rotacao_item);
							}
							if(insBest == TiposInsercao.VertRoot){
	
								informacaoInsercao.setAsSubnode(null);
								informacaoInsercao.setFitness(melhor_fitness);
								informacaoInsercao.setInsBest(insBest);
								informacaoInsercao.setRotacao_Item(rotacao_item);
							}
							if(insBest == TiposInsercao.AsSubnode){
								
								informacaoInsercao.setAsSubnode(no__subnode);
								informacaoInsercao.setFitness(melhor_fitness);
								informacaoInsercao.setInsBest(insBest);
								informacaoInsercao.setRotacao_Item(rotacao_item);
							}
							
						}
					}
						
					/*if(melhor_fitness == 1){
						System.out.println("Vou dar um break em InsEnumHNode");
						
						return informacaoInsercao;
					}*/
					
					if((j < k) && (w_sum > wi)){
						
						w_sum = w_sum - no.getRetanguloMinimo().getW_p();
						j = j + 1;    	//AVANÇA O PRIMEIRO ÍNDICE
						//no = array.get(j); 
					}
					else{
						k = k + 1;		//AVANÇA O SEGUNDO ÍNDICE
						
						if(k <= (array.size()-1)){
							no2 = array.get(k);
							w_sum = w_sum + no2.getRetanguloMinimo().getW_p();
						}
					}//fim-else					
			}//fim while	
				
		}//fim-for	
		
		Iterator<No> iterator = raiz.children();
		No q = null;
		
		while(iterator.hasNext()){ 
			q = iterator.next();

			if(q.getItem() == null){
				
				System.out.println("Entrei na chamada recursiva de InVNode !!!");
				//insBest = InsEnumVNode(item_, q, melhor_fitness, ant_fitness, insBest,ant_insBest, informacaoInsercao).getInsBest();
				informacaoInsercao = InsEnumVNode(item_, q, melhor_fitness, ant_fitness, insBest,ant_insBest, informacaoInsercao);
				insBest = informacaoInsercao.getInsBest();
			}
		}
		
		return informacaoInsercao;
		
	}//fim-procedimento
	
	public InformacaoInsercao InsEnumVNode(No item_, No raiz, float melhor_fitness, float ant_fitness, TiposInsercao insBest,TiposInsercao ant_insBest, InformacaoInsercao informacaoInsercao){
		
		Item item = item_.getItem();
		boolean valida = false;
		float w_ = 0, h_ = 0,n_ = 0;
		float fitness;
		float wi = item.getLargura(), hi = item.getAltura();
		
		for(int i = 0; i < var_rot; i++){
			
			switch (i+1){
				case 1:
				
					wi = item.getLargura();
					hi = item.getAltura();
					item.setRotacao(false);
					
					break;
				case 2:
					wi = item.getAltura();
					hi = item.getLargura();
					item.setRotacao(true);
					
					break;
					
				default:
					break;
			}
			
			if( (hi >= raiz.getRetanguloMaximo().getH_P())){// || (wi >= raiz.getRetanguloMaximo().getW_P())){
				
				continue;
			}
			
			System.out.println("\nVariáveis para AsSubNode");
			w_ = raiz.getRetanguloMaximo().getW_P() - wi;  System.out.println("\nw_ --> "+w_);
			h_ = raiz.getRetanguloSlack().getH_s_p() - hi; System.out.println("\nh_ --> "+h_);
			n_ = No.min2(raiz, wi);						   System.out.println("\nn_ --> "+n_);
  		    
			fitness = raiz.fitness(w_,h_,n_);
			System.out.println("Fitness para AsSubnode --->> "+fitness);
			
			if((h_ >= 0) && fitness >= melhor_fitness ){
				
				ant_fitness = melhor_fitness;
				melhor_fitness = fitness;
				
				ant_insBest = insBest;
				insBest = TiposInsercao.AsSubnode;
				
				ant_rotacao_item = rotacao_item;
				rotacao_item = item.getRotacao();
				
				no__subnode = raiz;
				
				System.out.println("Devo inserir AsSubnode\n");
				
				informacaoInsercao.setAsSubnode(no__subnode);
				informacaoInsercao.setRaiz_Insert(null, 0, 0);
				informacaoInsercao.setFitness(melhor_fitness);
				informacaoInsercao.setInsBest(insBest);
				informacaoInsercao.setRotacao_Item(rotacao_item);
				
			}	
			
			if(fitness == 1){
				System.out.println("Vou dar um break em InsEnumVNode");	
				
				return informacaoInsercao;
			}
			
			int k,j = 0;   //Índice do início da subsequência !
				
			ArrayList<No> array = Bin.positions(raiz);
			No no = array.get(j);
			No no2 = null;
			float h_sum = 0; 
				
			while(j <= (array.size()-1) && (wi + no.getRetanguloMinimo().getW_p() > raiz.getRetanguloMaximo().getW_P())){
				System.out.println("Entrei aqui incrementando o no j\n\n");	
				j = j + 1;
				
				if(j <= (array.size()-1)){
					
					no = array.get(j);
					System.out.println("j --> "+j);
				}
			}
			
			k = j;   //Índice do fim da subsequência !!!
			System.out.println("Saindo o NO_J como :  "+ j);
			
			if(k <= (array.size()-1)){
				no2 = array.get(k);
				h_sum = no2.getRetanguloMinimo().getH_p();
			}
			
			while(k <= (array.size()-1)){
				
				no = array.get(j);
				
				System.out.println("\nVariáveis para InParallelTo");
				
				w_ = raiz.getRetanguloMaximo().getW_P() - no.getRetanguloMinimo().getW_p() - wi;	System.out.println("\nw_ --> "+w_);
				h_ = raiz.getRetanguloSlack().getH_s_p() + h_sum - hi;								System.out.println("\nh_ --> "+h_);
				n_ = Math.abs(h_sum - hi);															System.out.println("\nn_ --> "+n_);
						
				fitness = raiz.fitness(w_,h_,n_);
				System.out.println("\nFitness para InParallelTo --->> "+fitness);
				
				if((h_ >= 0) && (fitness > melhor_fitness)){

					ant_fitness = melhor_fitness;
					melhor_fitness = fitness;
					ant_insBest = insBest;
					insBest = TiposInsercao.InParallelTo;
					ant_rotacao_item = rotacao_item;
					rotacao_item = item.getRotacao();
					raiz_insert = raiz;
					
					no_j = j;
					no_k = k;
					
					no__insert = no;
										
					informacaoInsercao.setAsSubnode(null);
					informacaoInsercao.setRaiz_Insert(raiz_insert, no_j, no_k);
					informacaoInsercao.setFitness(melhor_fitness);
					informacaoInsercao.setInsBest(insBest);
					informacaoInsercao.setRotacao_Item(rotacao_item);
										
					valida = verificaInParallelTo2(raiz_insert, item_, no_j, no_k, rotacao_item);
					         
					if(valida == false){
						
						if(insBest == TiposInsercao.InParallelTo){
							
							informacaoInsercao.setRaiz_Insert(null, 0, 0);
						}
		
						System.out.println("Vou validar retornando falso !!!\n\n");
		
						rotacao_item = ant_rotacao_item;
						melhor_fitness = ant_fitness;
						insBest = ant_insBest;
						
						if(insBest == TiposInsercao.HorizRoot){
							
							informacaoInsercao.setAsSubnode(null);
							informacaoInsercao.setFitness(melhor_fitness);
							informacaoInsercao.setInsBest(insBest);
							informacaoInsercao.setRotacao_Item(rotacao_item);
						}
						if(insBest == TiposInsercao.VertRoot){

							informacaoInsercao.setAsSubnode(null);
							informacaoInsercao.setFitness(melhor_fitness);
							informacaoInsercao.setInsBest(insBest);
							informacaoInsercao.setRotacao_Item(rotacao_item);
						}
						if(insBest == TiposInsercao.AsSubnode){
							
							informacaoInsercao.setAsSubnode(no__subnode);
							informacaoInsercao.setFitness(melhor_fitness);
							informacaoInsercao.setInsBest(insBest);
							informacaoInsercao.setRotacao_Item(rotacao_item);
						}
						
					}
				}
						
				/*if(melhor_fitness == 1){
				System.out.println("Vou dar um break em InsEnumHNode");
				
				return informacaoInsercao;
				}*/
				if((j < k) && (h_sum > hi)){
							
					h_sum = h_sum - no.getRetanguloMinimo().getH_p();
					j = j + 1;    	//AVANÇA O PRIMEIRO ÍNDICE
				}
				else{
					k = k + 1;		//AVANÇA O SEGUNDO ÍNDICE
						
					if(k <= (array.size()-1)){
						no2 = array.get(k);
						h_sum = h_sum + no2.getRetanguloMinimo().getH_p();
					}
				}//fim-else					
			}//fim while
		}//fim-for	
			
		Iterator<No> iterator = raiz.children();
		No q = null;
		
		while(iterator.hasNext()){
			
			q = iterator.next();
			System.out.println("Ret Maximo --> Largura --> "+q.getRetanguloMaximo().getW_P()+" Altura --> "+q.getRetanguloMaximo().getH_P());
			
			if(q.getItem() == null){
		
				System.out.println("Aqui");
				System.out.println("Vou chamar o InsHNode na recursiva\n\n");

				//insBest = InsEnumHNode(item_, q, melhor_fitness,ant_fitness, insBest,ant_insBest, informacaoInsercao).getInsBest();				
				informacaoInsercao = InsEnumHNode(item_, q, melhor_fitness,ant_fitness, insBest,ant_insBest, informacaoInsercao);
				insBest = informacaoInsercao.getInsBest(); 
					
			}else{System.out.println("Aqui");}
		}
		
		return informacaoInsercao;
		
	}//fim-procedimento
	
	public static boolean verifica_InsertParallelTo(No no_item, No no_k, No no_j){
		
		No novo, aux_pai = null, no_aux = null;
		
		no_aux = no_k;
		aux_pai = no_aux.getPai();
		
		if(no_k.getItem() != null){
			
			return true;
			/*No novo, aux_pai = null, no_aux = null;
						
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
				
				//O erro aconteceu aqui depois de adicionar os removers !!!!!!!
				
				novo.getFilhos().add(no_k); //Aqui deve-se chamar o cálculo do retângulo mínimo !!!!
				
				//Depois de criar um novo nó e adicionar os dois filhos deve-se calcular o retângulo Mínimo
				novo.calcula_retangulo_minimo(novo, false);
				novo.calcula_area_padrao(novo);
				
				aux_pai.getFilhos().add(novo);
				aux_pai.getFilhos().remove(no_k);
				novo.setPai(aux_pai);
				
				no_k.setPai(novo);
				
				No pai = novo.getPai();
				
				while(pai != null){
					
					pai.calcula_retangulo_minimo(pai, false);
					pai.calcula_area_padrao(pai);
					
					pai = pai.getPai();
				}
				//Pode ser que seja necessário o cálculo de Retângulo Máximo aqui depois de calcular todos os retângulos Mínimos
				
				System.out.println("\nCriei um nó e adicionei item e o no K a ele !!!");*/
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
				
				novo.getFilhos().add(no_aux);
				
				novo.calcula_retangulo_minimo(novo, false);
				
				if(novo.getRetanguloMinimo().getW_p() > aux_pai.getRetanguloMaximo().getW_P() || 
																				novo.getRetanguloMinimo().getH_p() > aux_pai.getRetanguloMaximo().getH_P() ){
					
					System.out.println("O retângulo mínimo do novo nó está estrapolando seus limites ");
					
					no_item.setPai(null);
					
					//Tomar cuidado com isso aqui !!!
					novo.getFilhos().removeFirst();
					novo.getFilhos().removeLast();
									
					return false;
				}
				else{			
					No aux;
					ArrayList<No> array = new ArrayList<No>(); 
					array = Bin.positions(no_k);
								
					//É necessário verificar 2 coisas :1--> o calculo dos retângulos/ 2--> Se é necessário remover os filhos que são trocados !!!
					if((array.size()-1) > 1){ 
					
						aux = array.get((array.size()-1));
						
						No novo2 = new No(bool);
						novo2.getFilhos().add(novo);
						novo.setPai(novo2); 
						
						aux = array.get(array.size()-1);
						novo2.getFilhos().add(aux);
						aux.setPai(novo2);
						
						//Tenho que remover os elementos trocados
						no_k.getFilhos().remove(aux);
						
						//É interessante o cáculo da verificação dos retângulos mínimos aqui também uma vez que há uma nova ligação e inserção !!!!!! 
						aux_pai = no_k.getPai();
						aux_pai.getFilhos().add(novo2);
						novo2.setPai(aux_pai);
						
						//Tenho que remover os elementos trocados
						aux_pai.getFilhos().remove(no_k);
												
						no_k.setPai(novo);
					}
					else{
				
						//Aqui eu devo verificar se o meu nó e o nó pai de k são iguais !!! Se sim return false;
						aux_pai = no_k.getPai();
						
						if(novo.getTipo_corte().corte == aux_pai.getTipo_corte().corte){
							
							//Deve retornar falso que significa que o padrão é inviável !!! E deve retornar ao fitness anterior !!!!
							return false;
						}
						else{
							novo.setPai(aux_pai);
							aux_pai.getFilhos().add(novo);
							
							no_k.setPai(novo);
						}
					}
				}
			}
			
			return true;
		}
	
	public static boolean verifica_InsertParallelTo2(No no_item, No no_k, No no_j){
		
		No novo, aux_pai = null, no_aux = null;
		
		boolean var_booleana = true;
		no_aux = no_k;
		aux_pai = no_aux.getPai();
		
		if(no_k.getItem() != null){
			
			//Na inserção em Paralelo é onde realmente acontece as ligações nesse caso !!!
			return true;
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
				
				if((novo.getRetanguloMinimo().getW_p() > aux_pai.getRetanguloMaximo().getW_P() ||
									novo.getRetanguloMinimo().getH_p() > aux_pai.getRetanguloMaximo().getH_P()) 
									|| (novo2.getRetanguloMinimo().getW_p() > aux_pai.getRetanguloMaximo().getW_P() ||
										novo2.getRetanguloMinimo().getH_p() > aux_pai.getRetanguloMaximo().getH_P())
						){//verificar quem é o pai de novo 2;

					var_booleana = false;			
				}
				else{
					
					var_booleana = true;
				}
				
				// Vou desfazer aqui as ligações que criei;
				
				no_k.getFilhos().add(aux);
				aux.setPai(no_k);
				no_k.calcula_retangulo_minimo(no_k, false);
				
				novo2.getFilhos().remove(aux);
				novo.getFilhos().remove(no_item);
				no_item.setFilhos(null);
				
				no_k.setPai(aux_pai);
				aux_pai.getFilhos().add(no_k);
				
				novo.getFilhos().remove(no_k);
				novo2.getFilhos().remove(novo);
				
				novo.setPai(null);
				novo2.getPai().getFilhos().remove(novo2);
				novo2.setPai(null);
				
				aux_pai.calcula_retangulo_minimo(aux_pai, false);
				
				return var_booleana;
			} 
			else{
				
				//Se contador for menor que 1 !!!
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
									
					var_booleana = false;
				}
				//verificar se é necessário esse ELSE_AQUI !!!
				else{
					novo.setPai(aux_pai);
					aux_pai.getFilhos().add(novo);
					
					no_k.setPai(novo);
				}
			}
		}
						
		return var_booleana;
	}//fim do método 

	
	public static boolean verificaInParallelTo2(No raiz_insert, No no_item, int no_j, int no_k2, boolean rotacao_item){

		int var = no_k2 - (no_j - 1);
		
		boolean verifica = true;
		No novo;
		
		no_item.getItem().setRotacao(rotacao_item);
		no_item.calcula_retangulo_minimo(no_item, no_item.getItem().getRotacao());
				
		ArrayList<No> array = new ArrayList<No>();
		array = Bin.positions(raiz_insert);
		
		if(raiz_insert.getTipo_corte().corte == No_orientacao.horizontal){
				
			novo = new No(true);
			novo.getFilhos().add(no_item);
			no_item.setPai(novo);
		}
		else{
			novo = new No(false);
			novo.getFilhos().add(no_item);
			no_item.setPai(novo);
		}
			
		if(var == 1){

			System.out.println("Entrei na verificação de InParalellTo !!!!");
			No aux = array.get(no_j);

	        if(aux.getItem() == null){
	        	
	        	System.out.println("Meu nó é do tipo corte!");
	             aux.calcula_retangulo_minimo(aux, false);

	             if(novo.getTipo_corte().corte == aux.getTipo_corte().corte){
	             
	            	 System.out.println("Meu nó é do tipo corte e igual ao tipo de corte de novo !!!");
	            	 verifica = false;
	             }
	        }
	        else{
	        	System.out.println("Meu nó é do tipo item!");    
	            aux.calcula_retangulo_minimo(novo, aux.getItem().getRotacao());

	            novo.getFilhos().add(aux);
	            novo.calcula_retangulo_minimo(novo, false);
	                    
	            if((novo.getRetanguloMinimo().getH_p() > aux.getRetanguloMaximo().getH_P()) || 
	              (novo.getRetanguloMinimo().getW_p() > aux.getRetanguloMaximo().getW_P())){
	                       
	                  novo.getFilhos().remove(aux);
	                  verifica = false;      
	            }
	        }
	                
	        no_item.setPai(null);
	        novo.getFilhos().remove(no_item);
	        novo = null;
	   }
	  
	   return verifica;
	}

/*
 * 
 * MÉTODO DE INSERÇÃO CRITICAL FIT -->>> HEURÍSTICA DE CONSTRUÇÃO DE SOLUÇÕES !!!!!!
 * 
 * */	
	
	public SolucaoNAria Critical_Fit(LinkedList<Pedidos> itens_pedidos){

		int i, j = 0, d;
		int[] nDom = new int[itens_pedidos.size()];		
		int[] nBinsFor = new int[itens_pedidos.size()]; 	
		
		InformacaoInsercao[] insBestForInsercao = (InformacaoInsercao[]) new Object[itens_pedidos.size()];
		InformacaoInsercao infoInsert;
		
		TiposInsercao insBest = TiposInsercao.Vazio, insBest_anterior;
		insBest_anterior = insBest;//talvez não seja necessário !!!!
		
		Funcoes.ordenaPedidosDecrescente(itens_pedidos);
					
		Pedidos[] array_pedidos = new Pedidos[itens_pedidos.size()];
		itens_pedidos.toArray(array_pedidos);
		
		
		int id_pedido, jd_pedidos;
		jd_pedidos = array_pedidos[0].getId();
		
		for(i = 0; i < array_pedidos.length; i++){
			
			id_pedido = array_pedidos[i].getId();
			
			d = 0;
			nDom[id_pedido - 1] = d;
			
			while(j < i){
				
				if((array_pedidos[j].retornaLargura() >= array_pedidos[i].retornaLargura()) &&
						(array_pedidos[j].retornaAltura() >= array_pedidos[i].retornaAltura())){
			
					nDom[id_pedido - 1] = d + 1;
				}
				
				d = d + 1;				
				j++;
			}
			
			j = 0;
			
		}
		
		int no_j, no_k;
		
		int bin_ID = 1, id_bin_adicionada;
		boolean rotacao_item;
		No no = new No();
		No raiz = null;
		No raiz1 = null;
		No as_subnode, raiz_insert;
		
		Bin bin_;
		Item item = new Item();
		

		Pedidos pedido_lista_pedidos, pedido_atualPedidos;
		Bin bin_atual, melhor_Bin = null; //Depois transferir variáveis para fora dos loops na hora dos testes !!!!  
		Bin b;
		
		SolucaoNAria solucao = new SolucaoNAria();
				
		LinkedList<Pedidos> list_itens_ndominados = new LinkedList<Pedidos>();
		Iterator<Pedidos> iterados_pedidos = itens_pedidos.iterator();
		
		//TiposInsercao[] insB[] = new TiposInsercao[itens_pedidos.size()][];
		//ArrayList<ArrayList<TiposInsercao>> insBs = new ArrayList<ArrayList<TiposInsercao>>();
		ArrayList<ArrayList<InformacaoInsercao>> insBs = new ArrayList<ArrayList<InformacaoInsercao>>();
		
		while(iterados_pedidos.hasNext()){ 
			insBs.add(new ArrayList<InformacaoInsercao>());
		}
		
		InformacaoInsercao informacaoInsercao;//talvez esse deva ser removido !!!
		
		iterados_pedidos = itens_pedidos.iterator();
		
		while(iterados_pedidos.hasNext()){ //Enquanto minha lista de itens não for vazia !
			
			list_itens_ndominados = Pedidos.lista_itens_no_dominados(itens_pedidos, nDom);
			Iterator<Pedidos> list_noDominates = list_itens_ndominados.iterator();
								
			while(list_noDominates.hasNext()){ //Enquanto existir itens não dominados e não empacotados  em U !!!!!
			
				//Acho que deve ser aqui que infor Insercao deve ser instanciado !!!
				informacaoInsercao = new InformacaoInsercao();
				
				pedido_atualPedidos = list_noDominates.next();
				id_pedido = pedido_atualPedidos.getId();
				
				nBinsFor[id_pedido - 1] = 0; 
				insBestForInsercao[id_pedido - 1] = informacaoInsercao;//TiposInsercao.Vazio;
								
				Iterator<Bin> iterador_bins = solucao.retornaSolucao().iterator();
								
				//no = new No(id_pedido,pedido_atualPedidos.retornaLargura(),pedido_atualPedidos.retornaAltura());
				
				item.setId(id_pedido);
				item.setLargura(pedido_atualPedidos.retornaLargura());
				item.setAltura(pedido_atualPedidos.retornaAltura());
				
				no.setItem(item);
				no.setPai(null);
				no.calcula_retangulo_minimo(no, false);
				
				while(iterador_bins.hasNext()){ //Percorrer nas bins
				
					bin_atual = iterador_bins.next();
					bin_id = bin_atual.getID();
					
					raiz = bin_atual.root();
										
					//if(insB[id_pedido - 1][bin_id - 1] == TiposInsercao.Undefined){
					if(insBs.get(id_pedido - 1).get(bin_id - 1).getInsBest() == TiposInsercao.Undefined){
						
						insBest = TiposInsercao.Vazio;
						//insBest = InsEnum(bin_atual, no, bin_atual.root()).getInsBest();
					    
						informacaoInsercao = InsEnum(bin_atual, no, bin_atual.root());
						insBest = informacaoInsercao.getInsBest();
						insBs.get(id_pedido - 1).set(bin_id - 1, informacaoInsercao);
					}
					    
					//if(insB[id_pedido - 1][bin_id - 1] == TiposInsercao.Vazio){
					if(insBs.get(id_pedido - 1).get(bin_id - 1).getInsBest() != TiposInsercao.Vazio){
						
					    nBinsFor[id_pedido - 1] = nBinsFor[id_pedido - 1] + 1;
					    
					    float fitness_insB, fitness_insBestForInsert;
					    
					    fitness_insB = insBs.get(id_pedido - 1).get(bin_id - 1).getFitness();
					    fitness_insBestForInsert = insBestForInsercao[id_pedido -1].getFitness();
					    
						if(fitness_insB > fitness_insBestForInsert){
						
							insBestForInsercao[id_pedido -1] = insBs.get(id_pedido - 1).get(bin_id - 1);
							
						}
					}
				}//Fim do loop das bins !!! 
			}//Fim do loop de U
			
			//Última etapa do algoritmo

			Pedidos i_aste = null; //Esse i* recebe um pedido de um método q retorna o melhor item de um vetor U !!!!
			InformacaoInsercao inf;
						
			i_aste = InformacaoInsercao.retornaMelhorInsert(list_itens_ndominados, nBinsFor, insBestForInsercao);
			inf = insBestForInsercao[i_aste.getId() - 1];
			
			no = new No(i_aste.getId(),i_aste.retornaLargura(),i_aste.retornaAltura());
			no.setPai(null);
			no.calcula_retangulo_minimo(no, false);
			
			no_j = inf.getNo_j();
			no_k = inf.getNo_k();
			
			insBest = inf.getInsBest();
			bin_ = inf.getBin_Insercao();
			raiz = bin_.root();
			as_subnode = inf.getAsSubnode();
			raiz_insert = inf.getRaiz_Insert();
			rotacao_item = inf.getRotacao_Item();	
			
			b = bin_;//Verificar se essa atribuição deixa o código correto !!!!
			//id_bin_adicionada = b.getID();
			
			if(insBest != TiposInsercao.Vazio){
				
								
				if (insBest == TiposInsercao.HorizRoot){
					
					System.out.println("AQUI VOU INSERIR HORIZ ROOT !!!!");	
					raiz1 = Bin.InsertVert_HorizRoot(raiz, no, No_orientacao.horizontal, rotacao_item);
					
					bin_.setRoot(raiz1); //COMENTEI MAS É PRA DESCOMENTAR E ADAPTAR !!!
					
					melhor_Bin = bin_;
					Queue<No> fila = null;
					Bin.busca_largura(raiz1, fila);
				}
				else if (insBest == TiposInsercao.VertRoot){
					
					System.out.println("AQUI VOU INSERIR VERT ROOT !!!!");
					raiz1 = Bin.InsertVert_HorizRoot(raiz, no, No_orientacao.vertical, rotacao_item);
					bin_.setRoot(raiz1);
					/*bin_.setRoot(raiz1);*/ //COMENTEI MAS É PRA DESCOMENTAR E ADAPTAR !!!
					
					melhor_Bin = bin_;
					Queue<No> fila = null;
					Bin.busca_largura(raiz1, fila);
											
				}
				else if (insBest == TiposInsercao.AsSubnode){
					System.out.println("opa to saindo como AsSubnode");
					System.out.println("AQUI VOU INSERIR ASSUBNODE!!");
					
					melhor_Bin = bin_;
					Bin.InsertAsSubnode(as_subnode, no, rotacao_item);
					
				}
				else if (insBest == TiposInsercao.InParallelTo){
					
					System.out.println("AQUI VOU INSERIR INPARELL TO!!!");
					
					melhor_Bin = bin_;
					Bin.InsertInParallelTo2(raiz_insert, no, no_j, no_k, rotacao_item);
				} else{}
				
			}
			else{
			
				System.out.println("\nAqui adiciono o item  a uma nova bin !!!");
								
				Bin bins = new Bin();
				bins.setID(bin_ID);
				
				no.calcula_retangulo_maximo(no, true);
				no.calcula_slack(no);
								
				no.setMaxima_area_livre(no.maior_area_livre(no));
				
				bins.adiciona_item(no);
				bins.setRoot(no);
				
				//Variáveis que armazenam informações sobre a melhor bin !!
				melhor_Bin = bins;
				//melhor_bin_id =	bins.getID();
				
				solucao.adicionaBin(bins);
				
				bin_ID = bin_ID + 1;
			}
			
			//b = solucao.retornaSolucao().get(id_bin_adicionada - 1); //Verificar se é essa a melhor opção
			b = melhor_Bin;
			id_bin_adicionada = b.getID();
			
			iterados_pedidos = itens_pedidos.iterator();
			
			while(iterados_pedidos.hasNext()){ //Atribuir indefinido a matriz insB
				
				pedido_lista_pedidos = iterados_pedidos.next();
				//insBs.get(pedido_lista_pedidos.getId() - 1).add(TiposInsercao.Undefined);
				
				inf.setInsBest(TiposInsercao.Undefined);
				insBs.get(pedido_lista_pedidos.getId() -1).set(id_bin_adicionada - 1,inf);
			}
			
			j = i_aste.getId();
			
						
			for(i = i_aste.getId(); i < array_pedidos.length; i++){
				
				id_pedido = array_pedidos[i].getId();
				
				//d = 0;
				//nDom[id_pedido - 1] = d;
				
				while(j < i){
					
					if((array_pedidos[j].retornaLargura() >= array_pedidos[i].retornaLargura()) &&
							(array_pedidos[j].retornaAltura() >= array_pedidos[i].retornaAltura())){
				
						nDom[id_pedido - 1] = nDom[id_pedido - 1] - 1;
					}
					j++;
				}
				j = i_aste.getId();
			}
			
			//Aqui deve-se remover o objeto inserido de I, talvez possa dar erro na hora de percorrer o vetor para
			//atualizar o nDom[i] será aconselavel eu utilizar smente Iterator ?!!!
		}
		
		return solucao;
	}
		
	
/*
 * 
 * MÉTODO DE JUSTIFICAÇÃO -->>> HEURÍSTICA DE MELHORIA !!!!!!
 * 
 * */
	
  public SolucaoNAria Justificacao(SolucaoNAria solucao, int num_execucoes, Pedidos[] itens_pedidos, Chapa chapa){
		
        int bin_id = 1, cont = 0;
        float somatorio = 0, LB;
	
        Pedidos pedido;
        InformacaoInsercao informacaoInsercao = new InformacaoInsercao();
        //Iterator<Pedidos> iterator = itens_pedidos.iterator();
        ArrayList<InformacaoInsercao> array_informacao_insercao = new ArrayList<InformacaoInsercao>();
	
        System.out.println("Justified - Tamanho da solucao recebida -> "+ solucao.retornaSolucao().size());
        
        while(cont < itens_pedidos.length){
	
            //pedido = iterator.next();
            pedido = itens_pedidos[cont];
            somatorio = somatorio + pedido.retornaArea();
            
            cont++;
        }
        System.out.println("Somatorio justification --> "+ somatorio);
	
        float lim = somatorio/(float) Chapa.getArea();
        LB = Math.round(lim);
        
        if(LB - lim < 0) LB = LB+1;
        
        System.out.println("LB -->> "+LB);

        if(solucao.size() == LB){

            System.out.println("Vou retornar a solução pois esta já é ótima");
            return solucao;
        }

        SolucaoNAria solucao2 = new SolucaoNAria(solucao);
        //solucao2.atribui_solucao(solucao); //Comentário por enquanto para realizar os testes !

        LinkedList<Pedidos> lista_itens;
        Iterator<Bin> bins_sol2;

        //int id_bin;
        No no;
        Bin bin_S2;
        Bin bin = null;
        No raiz_bin = null;

        TiposInsercao insBest;
        Iterator<Pedidos> iterator_pedidos;
		
        for(int exec = 1; exec <= num_execucoes; exec++){   

            System.out.println("Execução de nº "+exec);
            System.out.println("Agora vou ordenar as bins ...");
            Funcoes.ordenaPedidosDecrescenteBins(solucao.retornaSolucao());
            
            System.out.print("Area livre [");
            int cont_= 0;
            while(cont_ < solucao.retornaSolucao().size()){
                System.out.print(" "+solucao.retornaSolucao().get(cont_).root().getMaxima_area_livre()+",");
                cont_++;
            }System.out.println(" ]");
            
            SolucaoNAria solucao1 = new SolucaoNAria();//Talvez só precise de uma instância e está é atualizada com vazio !!!
            bins_sol2 = solucao2.listaBinSolucao();

            while(bins_sol2.hasNext()){ //Verificar a possibiliadade de inserir (numBins > 0) e remover as bins

                bin_S2 = bins_sol2.next();

                //I <- itens da última bin de X2 (Solução 2)
                lista_itens = bin_S2.getItensBin(solucao2.resgataUltimaBin()); 

                //Remove a última bin de Solução 2
                System.out.println("Vou remover a última Bin ...");
                solucao2.removeUltimaBin();

                System.out.println("Vou ordenar a lista de itens da última Bin ...");
                Funcoes.ordenaPedidosDecrescente(lista_itens);	

                iterator_pedidos = lista_itens.iterator();

                while(iterator_pedidos.hasNext()){

                    pedido = iterator_pedidos.next();

                    no = new No(pedido.getId(),pedido.retornaLargura(),pedido.retornaAltura());
                    no.setPai(null);
                    no.calcula_retangulo_minimo(no, false);

                    insBest = TiposInsercao.Vazio;

                    Iterator<Bin> iterator_bin = solucao1.listaBinSolucao();

                    while(iterator_bin.hasNext()){

                        bin = iterator_bin.next();
                        raiz_bin = bin.root();

                        informacaoInsercao = InsEnum(bin, no, raiz_bin);
                        insBest = informacaoInsercao.getInsBest();
                        array_informacao_insercao.add(informacaoInsercao);//Verificar isso aqui e adaptar como no FIRST !!!

                        if(insBest != TiposInsercao.Vazio){

                            break;
                        }
                    }

                    //Aqui não precisa pois no algoritmo de Justificação ele executa o First FIT !!!
                    //informacaoInsercao = InformacaoInsercao.retornaMelhorSolucao(array_informacao_insercao,0);//verificar ta errado

                    insBest  = informacaoInsercao.getInsBest();
                    bin      = informacaoInsercao.getBin_Insercao();
                    raiz_bin = informacaoInsercao.getBin_Insercao().root();
                    no__subnode  = informacaoInsercao.getAsSubnode();
                    raiz_insert  = informacaoInsercao.getRaiz_Insert();
                    rotacao_item = informacaoInsercao.getRotacao_Item();
                    no_j = informacaoInsercao.getNo_j();
                    no_k = informacaoInsercao.getNo_k();

                    if(insBest != TiposInsercao.Vazio){

                        No raiz1;

                        if (insBest == TiposInsercao.HorizRoot){

                            System.out.println("AQUI VOU INSERIR HORIZ ROOT !!!!");	
                            raiz1 = Bin.InsertVert_HorizRoot(raiz_bin, no, No_orientacao.horizontal, rotacao_item);
                            bin.setRoot(raiz1);

                            Queue<No> fila = null;
                            Bin.busca_largura(raiz1, fila);
                        }
                        else if (insBest == TiposInsercao.VertRoot){

                            System.out.println("AQUI VOU INSERIR VERT ROOT !!!!");
                            raiz1 = Bin.InsertVert_HorizRoot(raiz_bin, no, No_orientacao.vertical, rotacao_item);
                            bin.setRoot(raiz1);

                            Queue<No> fila = null;
                            Bin.busca_largura(raiz1, fila);

                        }
                        else if (insBest == TiposInsercao.AsSubnode){
                            
                            System.out.println("opa to saindo como AsSubnode");
                            System.out.println("AQUI VOU INSERIR ASSUBNODE!!");

                            Bin.InsertAsSubnode(no__subnode, no, rotacao_item);

                        }
                        else if (insBest == TiposInsercao.InParallelTo){

                            System.out.println("AQUI VOU INSERIR INPARELL TO!!!");
                            Bin.InsertInParallelTo2(raiz_insert, no, no_j, no_k, rotacao_item);
                        } else{}

                    }
                    else{

                        System.out.println("\nAqui adiciono o item  a uma nova bin !!!");

                        Bin bins = new Bin();
                        bins.setID(bin_id);

                        no.calcula_retangulo_maximo(no, true);
                        no.calcula_slack(no);
                        no.setMaxima_area_livre(no.maior_area_livre(no));

                        bins.adiciona_item(no);
                        bins.setRoot(no);

                        solucao1.adicionaBin(bins);

                        //Quando criar uma nova bin, incrementa para o próximo id !!!
                        bin_id = bin_id + 1;
                    }

                }//Iterator de pedidos

                if((solucao1.size() + solucao2.size()) < solucao.size()){

                    //X <<-- X1 U X2
                    solucao.unir_duas_solucoes(solucao1, solucao2);

                    if(solucao.size() == LB){

                        return solucao;
                    }
                }
                
                //inserir aqui para pegar novamente o iterator para o número de bins. Assim atualizando a lista de iterator
                bins_sol2 = solucao2.listaBinSolucao();
        }

        //X2 <<-- X1
        solucao2.atribui_solucao(solucao1);
      }

      System.out.println("Justified - Tamanho da solucao retornada -> "+ solucao.retornaSolucao().size());
      return solucao;
   }
        
   public SolucaoNAria calculaFAV_FAV2(SolucaoNAria solucao, Chapa chapa){
    
        int num_bin,j = 0;
        int w = (int) chapa.getLargura();
        int h = (int) chapa.getAltura();
        
        Bin bin;
        float arealivre, somatorio = 0;
        Double FAV = 0.0, FAV2 = 0.0;
        Double sobra_bin = 0.0, MenorAp = 0.0;
        
        ArrayList<Double> FAVCorrente = new ArrayList<Double>(); //Lista de FAVs
        ArrayList<Double> listSobras = new ArrayList<Double>(); //Sobras referentes de cada placa !
        
        Iterator<Bin> iteratoBIN = solucao.listaBinSolucao();
        
        while(iteratoBIN.hasNext()){
        
            bin = iteratoBIN.next();
            arealivre = bin.root().getMaxima_area_livre();
            
             bin.setFav(new Double(Utilidades.Funcoes.getChapa().retorneArea() - arealivre));//ou this.getChapa().retorneArea();
             bin.setSobra(new Double(arealivre));
            
            FAV = FAV + bin.getFav();
            
            FAVCorrente.add((bin.getFav()/(w*h))*100);
            
            sobra_bin = 100 - (bin.getFav()/(w*h)*100);
            listSobras.add(sobra_bin);
            
            //Atualiza as sobras dentro de cada BIN
            //bin.setAproveitamento(sobra_bin.floatValue());
            bin.setAproveitamento(Utilidades.Funcoes.getChapa().retorneArea() - arealivre);
            if (sobra_bin > MenorAp){
            
                MenorAp = sobra_bin;                	                	
            }
            j++;
        }
        
        solucao.set_Fav(FAV);
        //Cálculo final das Funções de Avaliação mas para aplicar na SolucaoNAria
        FAV2 = ((FAV)+MenorAp)/j;
        FAV = FAV/(j*w*h);
        
        solucao.setFAV(FAV);
        solucao.setFAV2(FAV2);
    
    
        return solucao;
    }
	
	
} // fim da classe !!!!