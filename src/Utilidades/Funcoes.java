package Utilidades;

import HHDBinPackingTree.BPTPedaco;
import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Iterator;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import Heuristicas.Solucao;
import java.util.Formatter;
import java.util.LinkedList;
import Heuristicas.Individuo;
import java.util.Collections;
import java.io.BufferedReader;
import javax.swing.JOptionPane;
import HHDBinPackingTree.IBPTNode;
import java.io.FileNotFoundException;
import HHDInternal.SolucaoHeuristica;
import j_HeuristicaArvoreNAria.ComparaSolucaoNAriaFAV;
import j_HeuristicaArvoreNAria.Pedidos;
import j_HeuristicaArvoreNAria.SolucaoNAria;
import java.security.SecureRandom;
import java.util.GregorianCalendar;

public class Funcoes {

    private static Chapa chapa;
    private static Pedidos[] vetor_info;
	
	
    public static LinkedList<Pedidos> lerSolucoesMemoriaSolucao (Solucao solucao){
    
        int i;
        Integer id_item;
        List<Integer> lista_itens = new ArrayList<Integer>();
        LinkedList<Pedidos> list_pedidos = new LinkedList<Pedidos>();
        
        Iterator<Individuo> itr_solucao = solucao.iteratorIndividuos(solucao);
        
        while(itr_solucao.hasNext()){
        
            Individuo individuo_atual = itr_solucao.next();
            
            lista_itens = individuo_atual.getListaItens();
            
            for(i = 0; i < lista_itens.size(); i++){
            
                id_item = lista_itens.get(i);
                
                list_pedidos.add(new Pedidos(id_item, vetor_info[id_item].retornaLargura(), vetor_info[id_item].retornaAltura()));
            }
        }
        
        return list_pedidos;
    }  
     
    //Cria e ler arquivo PORT_MAP com informações das Memórias
    public static void cria_PortMap(String name, String IP, int porta){
    
        String nome_arquivo = "PORT_MAP.txt";
        
        try {
            
            Formatter arquivo_saida = new Formatter(nome_arquivo);
            arquivo_saida.format(""+name+" "+IP+" "+porta+"", name, IP, porta);
            
            arquivo_saida.close();
            
        } catch (Exception e) {
            System.out.println("Erro na escrita do arquivo");
        }
    
    }
    
    public static ArrayList<InfArqPortMap> ler_PortMap(){
    
        String linha = "";
        String result = "";
        String mostra = "";
        String nome_arquivo = "PORT_MAP.txt";
        
        File arquivo = new File(nome_arquivo);
        ArrayList<InfArqPortMap> list_info = null;
        
        //Verifica se o arquivo existe
        if(arquivo.exists()){
        
           int cont = 0;
           mostra = "Arquivo "+nome_arquivo+" aberto com sucesso !";
        
           list_info = new ArrayList<InfArqPortMap>();
                       
           try {
               
                //Abrindo arquivo para leitura
                FileReader reader = new FileReader(nome_arquivo);
                
                //Leitor do arquivo
                BufferedReader buffer = new BufferedReader(reader);
                
                while(true){
                
                    linha = buffer.readLine();
                    result = linha;
                                       
                    if(linha == null){ 
                        break;
                    }
                    else{
                        
                        String resultado[] = result.split(" ");
                        InfArqPortMap info = new InfArqPortMap(resultado[0],
                                                               resultado[1], 
                                                               Integer.valueOf(resultado[3].trim()));
                        list_info.add(info);
                    }
                    
                    cont++;
                }
            }
            catch (Exception e) { System.out.println("Erro leitura arquivo !");
            }
        }
        
      return list_info;
    }
        
    public static LinkedList<Pedidos> lerArquivo() throws IOException{
		
        //File arquivo = new File("C:/Users/Junior/workspace/AgentesEquipe/src/Util/arquivo1.txt");

        File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/Util/arquivo1.txt");

        try{
            FileReader arqu_reader = new FileReader(arquivo);
            BufferedReader buffer = new BufferedReader(arqu_reader);

            String linha = "";
            String result = "";

            LinkedList<Pedidos> listaPedidos = new LinkedList<Pedidos>();
            int cont = 0;

            while((linha = buffer.readLine()) != null){

                    if(cont == 0){
                            result = linha;
                            String resultado[] = result.split(" ");
                            setChapa(new Chapa(Integer.valueOf(resultado[0].trim()), Integer.valueOf(resultado[1].trim())));
                    }

                    if(cont > 1){
                            result = linha;
                            String resultado[] = result.split(" ");
                            listaPedidos.add(new Pedidos(cont, Integer.valueOf(resultado[0].trim()), 
                                      Integer.valueOf(resultado[1].trim()))); 
                    }
                    cont++;
            }
            arqu_reader.close();
            buffer.close();

            return listaPedidos;
        }
        catch(FileNotFoundException file_error){
            JOptionPane.showMessageDialog(null, file_error);
	}
		
	return null;
    }	
		
    public static LinkedList<Pedidos> lerEntrada(){

        int i,quantidade;
        float larg, alt;

        Scanner input = new Scanner(System.in);
        LinkedList<Pedidos> listaPedidos = new LinkedList<Pedidos>();

        System.out.println("Informe a lista de pedidos !!!");
        System.out.println("Informe a quantidade de pedidos ...");
        quantidade = input.nextInt(); 

        for(i = 1;i <= quantidade; i++){

            System.out.println("Informe a altura e largura:");
            alt = input.nextFloat();
            larg = input.nextFloat();

            listaPedidos.add(new Pedidos(i, alt, larg));
        }

        return listaPedidos;
    }
		
    public static void imprimeListaPedidos(LinkedList<Pedidos> listaPedidos){
        
        int i;
        Pedidos pedido;	

        for (i = 0 ; i < listaPedidos.size();i++){

            pedido = listaPedidos.get(i);
            System.out.println("\n\nPedido "+(i+1));
            pedido.imprimePedido();
        }
    }
		
    public static void ordenaPedidosCrescente(LinkedList<Pedidos> pedid){
			
	Collections.sort(pedid, new ComparadorAreas());		
    }
		
    public static void ordenaCrescenteListaItens(ArrayList<Integer> array, Pedidos[] vetor_info){

        LinkedList<Pedidos> lnk_pedidos = new LinkedList<Pedidos>();

        Iterator<Integer> iterator = array.iterator();

        while(iterator.hasNext()){

           lnk_pedidos.add(vetor_info[iterator.next()]);
        }

        Collections.sort(lnk_pedidos, new ComparadorAreas());
    }
		
    public static void ordenaDecrescenteListaItens(ArrayList<Integer> array, Pedidos[] vetor_info){

        LinkedList<Pedidos> lnk_pedidos = new LinkedList<Pedidos>();

        Iterator<Integer> iterator = array.iterator();

        while(iterator.hasNext()){

            lnk_pedidos.add(vetor_info[iterator.next()]);
        }

        Collections.sort(lnk_pedidos, new ComparadorAreas());
        Collections.reverse(lnk_pedidos);
    }
	
    public static void ordenaCrescenteListaItens(ArrayList<Integer> array){
    
    	//int item;
        ComparadorAreaItem compara = new ComparadorAreaItem();
        compara.setVetorInfo(Funcoes.getVetor_info());

        Collections.sort(array, compara);

    }

    public static void ordenaDecrescenteListaItens(ArrayList<Integer> array){
    	
        ComparadorAreaItem compara = new ComparadorAreaItem();
        compara.setVetorInfo(Funcoes.getVetor_info());

        Collections.sort(array, compara);
        Collections.reverse(array);	

    }
    public static void ordenaPedidosDecrescente(LinkedList<Pedidos> pedid){

        Collections.sort(pedid, new ComparadorAreas());		
        Collections.reverse(pedid);
    }

    public static void ordenaIndividuosCrescente(LinkedList<Individuo> list_individuos){

        Collections.sort(list_individuos, new ComparadorIndividuos());		

    }

    public static void ordenaIndividuosDecrescente(LinkedList<Individuo> list_individuos){

        Collections.sort(list_individuos, new ComparadorIndividuos());		
        Collections.reverse(list_individuos);
    }
    
    public static void ordenaIndividuosCrescente(LinkedList<Individuo> list_individuos, String str){

        Collections.sort(list_individuos, new ComparadorIndividuoSomatorio());		
    }

    public static void ordenaIndividuosDecrescente(LinkedList<Individuo> list_individuos, String str){

        Collections.sort(list_individuos, new ComparadorIndividuoSomatorio());		
        Collections.reverse(list_individuos);
    }

    public static void ordenaSolucoesCrescente(LinkedList<Solucao> list_solucoes){

        Collections.sort(list_solucoes, new ComparadorSolucoes());				
    }

    public static void ordenaSolucoesDecrescente(LinkedList<Solucao> list_solucoes){

        Collections.sort(list_solucoes, new ComparadorSolucoes());		
        Collections.reverse(list_solucoes);
    }
    
    //Ordena as "Solucoes Heuristicas"
    public static void ordenaSolucoesHeuristicasCrescente(LinkedList<SolucaoHeuristica> list_solucoes){

        Collections.sort(list_solucoes, new ComparadorSolucoesHeuristicas());				
    }

    public static void ordenaSolucoesHeuristicasDecrescente(LinkedList<SolucaoHeuristica> list_solucoes){

        Collections.sort(list_solucoes, new ComparadorSolucoesHeuristicas());		
        Collections.reverse(list_solucoes);
    }

    //####################################  ÚTIL PARA SOLUÇÕES NARIAS  #########################################
    public static void ordenaSolucoesHeuristicasNariaCrescente(LinkedList<SolucaoNAria> list_solucoes){

        Collections.sort(list_solucoes, new ComparaSolucaoNAriaFAV());				
    }

    public static void ordenaSolucoesHeuristicasNariaDecrescente(LinkedList<SolucaoNAria> list_solucoes){

        Collections.sort(list_solucoes, new ComparaSolucaoNAriaFAV());		
        Collections.reverse(list_solucoes);
    }
    
    public static Pedidos[] atribui_info(LinkedList<Pedidos> lista_pedidos, Pedidos[] vetor_inf){

        int i;
        Pedidos pedido;	

        vetor_info = new Pedidos[lista_pedidos.size()];

        for (i = 0 ; i < lista_pedidos.size();i++){

                pedido = lista_pedidos.get(i);
                vetor_inf[i] = pedido;
                vetor_info[i] = pedido;
        }

        return vetor_info;
    }

    public static Pedidos[] getVetor_info() {

        return vetor_info;
    }

    public static void setVetor_info(Pedidos[] vetor_info) {

        Funcoes.vetor_info = vetor_info;
    }
    
    public static Chapa getChapa() {

        return chapa;
    }

    public static void setChapa(Chapa chapa) {

        Funcoes.chapa = chapa;
    }

    public static int aleatorio_entre_Intervalo(int inicial, int final_){

        Random random = new Random();

        return (inicial + random.nextInt(final_ - inicial));
    }
    
    public static double calculaLimiteInferior(double somatorio_itens, double area_placa){
        
        double limite = (somatorio_itens/area_placa);
        
      return Math.ceil(limite);
    }
    
   //Aqui imprime a lista/caminho de visita na árvore na forma de Busca em Largura
   public static ArrayList<IBPTNode> retornaSequenciaBlarguraBinPacking(IBPTNode raiz, Queue<IBPTNode> fila){
       
       float somatorioSobra = 0;
       float desperdicio, aproveitamento;
       
       fila = new LinkedList<IBPTNode>();
       ArrayList<IBPTNode> caminho = new ArrayList<IBPTNode>();
       IBPTNode leftSon = null; 
       IBPTNode rightSon = null;
       
       IBPTNode no;
       
       fila.add(raiz);
       
       while(!fila.isEmpty()){
	
           no = fila.poll();
           caminho.add(no);
           
           leftSon = no.getLeftSon(); 
           rightSon = no.getRigthSon();
           
           if(leftSon != null)
               fila.add(leftSon);
           
           if(rightSon != null)
               fila.add(rightSon);
       }
      
       return caminho;
   }

   //Imprimir a árvore de corte OBS: ver como funciona a interpretação
   public static void imprimeBinPackTree(ArrayList<ArrayList<IBPTNode>> solucao){
   
       int cont = 0;
       
       while(cont < solucao.size()){
           
           System.out.println("\n\n\n ################## BinPackTree "+ (cont+1)+" ########################### ");
           
           ArrayList<IBPTNode> placa = solucao.get(cont);
           
           while(!placa.isEmpty()){
           
               IBPTNode no_arvore = placa.get(0);
           
               BPTPedaco no = (BPTPedaco) no_arvore;
               
               System.out.println("\nNível --> " + no.getNivel());
               
               if(no.getFather() == null){ 
                   
                   System.out.println("É um Nó Raiz ");
                   
                   if(no.corteVertical()){
                       System.out.println(" V ");
                       System.out.println("Dimensões = (L x W) -> ("+ no.getDimensoes().retorneBase()+" , "+ no.getDimensoes().retorneAltura()+")");
                       System.out.println("Somatório Sobra --> "+no.getSomatorioSobras());
                       System.out.println("Desperdício --> "+no.getDesperdicio());
                       System.out.println("Aproveitamento em % --> "+no.getAproveitamento());
                   }
                   else{
                       System.out.println(" H ");
                       System.out.println("Dimensões = (L x W) -> ("+ no.getDimensoes().retorneBase()+" , "+ no.getDimensoes().retorneAltura()+")");
                       System.out.println("Somatório Sobra --> "+no.getSomatorioSobras());
                       System.out.println("Desperdício --> "+no.getDesperdicio());
                       System.out.println("Aproveitamento em % --> "+no.getAproveitamento());
                   }
               }
               else{
               
                   if(no.temPeca()){
                       System.out.println("Peça "+ no.getPedidoAtendido().id());
                       System.out.println("Peça Dimensões = (L x W) -> ("+ no.getDimensoes().retorneBase()+" , "+
                                                                           no.getDimensoes().retorneAltura()+")");
                       System.out.println("Somatório Sobra --> "+no.getSomatorioSobras());
                       System.out.println("Desperdício --> "+no.getDesperdicio());
                       System.out.println("Aproveitamento em % --> "+no.getAproveitamento());
                   }
                   else if(no.temPeca() == false && no.isCuttable()){
                       System.out.println("Sobra "+ no.getId());
                       System.out.println("Sobra Dimensões = (L x W) -> ("+ no.getDimensoes().retorneBase()+" , "+
                                                                            no.getDimensoes().retorneAltura()+")");
                       System.out.println("Somatório Sobra --> "+no.getSomatorioSobras());
                       System.out.println("Desperdício --> "+no.getDesperdicio());
                       System.out.println("Aproveitamento em % --> "+no.getAproveitamento());
                   }
                   else{
                       if(no.corteVertical()){
                            System.out.println(" V ");
                            System.out.println("Dimensões = (L x W) -> ("+ no.getDimensoes().retorneBase()+" , "+ no.getDimensoes().retorneAltura()+")");
                            System.out.println("Somatório Sobra --> "+no.getSomatorioSobras());
                            System.out.println("Desperdício --> "+no.getDesperdicio());
                            System.out.println("Aproveitamento em % --> "+no.getAproveitamento());
                       }
                       else{
                            System.out.println(" H ");
                            System.out.println("Dimensões = (L x W) -> ("+ no.getDimensoes().retorneBase()+" , "+ no.getDimensoes().retorneAltura()+")");
                            System.out.println("Somatório Sobra --> "+no.getSomatorioSobras());
                            System.out.println("Desperdício --> "+no.getDesperdicio());
                            System.out.println("Aproveitamento em % --> "+no.getAproveitamento());
                       }
                   }
               }
               /*System.out.println("Filho Esquerdo --> "+no.getLeftSon());
               System.out.println("Filho Direito --> "+no.getRigthSon());
               System.out.println("Dimensões = (L x W) -> ("+ no.getDimensoes().retorneBase()+" , "+ no.getDimensoes().retorneAltura()+")");
               System.out.println("Corte vertical --> "+ no.corteVertical());
               System.out.println("Tem peça --> "+ no.temPeca());
               System.out.println("Está cortado --> "+ no.isCuttable());
               //System.out.println("Está selecionado --> "+ no.isSelected());
               */
               placa.remove(0);
           }
           cont++;
       }
       
   }
   
   public int rand(int n_){
   
       SecureRandom r = new SecureRandom();
       
       return r.nextInt(n_);
   }
   
   public int linearRand(boolean worst, int n){
          
       SecureRandom random = new SecureRandom();
       
       if(worst == true){
       
           return ((int)((Math.sqrt(1+8*((int)(random.nextInt()%((1+n)*n/2))))-1)/2));
       }
       else{
           return 9 - ((int)((Math.sqrt(1+8*((int)(random.nextInt()%((1+n)*n/2))))-1)/2));
       }
       
   }
   
   public int triangularRand(int n){
   
       int a1, a2, a, m = (int) (n / 2.0 + 0.5);
       SecureRandom random = new SecureRandom();
  
       a1 = m*(m+1)/2; a2 = (n-m)*((n-m)+1)/2; 
       if ((a = ((random.nextInt() % (a1+a2)) +1)) <= a1){
           return ((int)(Math.ceil((Math.sqrt(1+8*a)-1)/2)));
       }else{
           a = a1 + a2 - a + 1;
           return (n - ((int)(Math.ceil((Math.sqrt(1+8*a)-1)/2))));
       }
   }
   
   public static void main(String args[]){
   
       int cont = 1;
       Funcoes f = new Funcoes();
       
       System.out.println("### Randômico comum ####");
       while(cont< 10){
       
           System.out.println("Valor gerado -> "+f.rand(10));
       
            cont++;
       }
       
       cont = 1;
       System.out.println("\n### BESTPROBLINEAR  ####");
       while(cont< 10){
       
           System.out.println("Valor gerado -> "+(f.linearRand(false,10)));//9??
       
            cont++;
       }
       
       cont = 1;
       System.out.println("\n### WORSTPROBLINEAR ####");
       while(cont< 10){
       
           System.out.println("Valor gerado -> "+f.linearRand(true, 10));//9??
       
            cont++;
       }
       
       cont = 1;
       System.out.println("\n### TRIANGULAPROB   ####");
       while(cont< 10){
       
           System.out.println("Valor gerado -> "+f.triangularRand(10));
       
           cont++;
       }
   }
}