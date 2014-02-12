package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import Heuristicas.Individuo;
import Heuristicas.Solucao;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

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
    public static void ordenaSolucoesHeuristicasCrescente(LinkedList<HeuristicaConstrutivaInicial.Solucao> list_solucoes){

        Collections.sort(list_solucoes, new ComparadorSolucoesGeral());				
    }

    public static void ordenaSolucoesHeuristicasDecrescente(LinkedList<HeuristicaConstrutivaInicial.Solucao> list_solucoes){

        Collections.sort(list_solucoes, new ComparadorSolucoesGeral());		
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

    public static Chapa getChapa() {

        return chapa;
    }

    public static void setChapa(Chapa chapa) {

        Funcoes.chapa = chapa;
    }

    public static Pedidos[] getVetor_info() {

        return vetor_info;
    }

    public static void setVetor_info(Pedidos[] vetor_info) {

        Funcoes.vetor_info = vetor_info;
    }

    public static int aleatorio_entre_Intervalo(int inicial, int final_){

        Random random = new Random();

        return (inicial + random.nextInt(final_ - inicial));
    }
    
    public static double calculaLimiteInferior(double somatorio_itens, double area_placa){
        
        double limite = (somatorio_itens/area_placa);
        
      return Math.ceil(limite);
    }
    
	
}