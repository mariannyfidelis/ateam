package j_HeuristicaArvoreNAria;

import Utilidades.Chapa;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class Funcoes {

    static Chapa chapa;
	
    public static LinkedList<Pedidos> lerArquivo() throws IOException{

        //File arquivo = new File("/home/marianny/Desktop/Dissertacao/ArtigsApresentacao/Alex Trindade/grasp2d2/" +
        //		                "teste1.dat");
        File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/Utilidades/arquivoFleszarHD.txt");
        
        try{
            
            FileReader arq_reader = new FileReader(arquivo);
            BufferedReader buffer_reader = new BufferedReader(arq_reader);
            String linha = "";
            String result = "";

            int cont = 0;
            int contadorP = 0;
            Pedidos[] vetor_info = null;
            LinkedList<Pedidos> listaPedidos = new LinkedList<Pedidos>();
            
            while((linha = buffer_reader.readLine()) != null){

                if(cont == 0){
                    result = linha;
                    String resultado[] = result.split(" ");
                    Integer l = Integer.valueOf(resultado[0].trim());
                    Integer a = Integer.valueOf(resultado[1].trim());
                    chapa = new Chapa(l,a);
                    Utilidades.Funcoes.setChapa(chapa);
                }
                if(cont == 1){
                    result = linha;
                    String resultado[] = result.split(" ");
                    Integer tamVet = Integer.valueOf(resultado[0].trim());
                    vetor_info = new Pedidos[tamVet];
                    System.out.println("Meu vetor é de tamanho --> "+vetor_info.length);
                }
                if(cont > 1){
                    result = linha;
                    String resultado[] = result.split(" ");
                    Integer l    = Integer.valueOf(resultado[0].trim());
                    Integer a    = Integer.valueOf(resultado[1].trim());
                    Pedidos pedd = new Pedidos(cont-1,l ,a);
                    listaPedidos.add(pedd);
                    vetor_info[contadorP] = pedd;
                    System.out.println("Adicionei pedido --> "+ (contadorP+1));
                    contadorP++;
                }
                cont++;
            }
            Utilidades.Funcoes.setVetor_info(vetor_info);
            arq_reader.close();
            buffer_reader.close();

            return listaPedidos;
        }
        catch (FileNotFoundException erro) {

            JOptionPane.showMessageDialog(null, erro);
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

    public static void ordenaPedidosDecrescente(LinkedList<Pedidos> pedid){

            Collections.sort(pedid, new ComparadorAreas());		
            Collections.reverse(pedid);
    }

    public static void ordenaPedidosCrescenteRetMinimo(boolean node_v, LinkedList<No> filhos){

        if(node_v == true){

                Collections.sort(filhos, new ComparadorRetMinimo_VNode());	
        }
        else{
                Collections.sort(filhos, new ComparadorRetMinimo_HNode());
        }
    }

    public static void ordenaPedidosDecrescenteRetMinimo(boolean node_v, LinkedList<No> filhos){

        if(node_v == true){

            Collections.sort(filhos, new ComparadorRetMinimo_VNode());	
            Collections.reverse(filhos);
        }
        else{
            Collections.sort(filhos, new ComparadorRetMinimo_HNode());	
            Collections.reverse(filhos);
        }
    }
    
    public static void ordenaPedidosCrescenteBins(LinkedList<Bin> solucao){
    
        Collections.sort(solucao, new CompararBins());	
    }
    
    public static void ordenaPedidosDecrescenteBins(LinkedList<Bin> solucao){
    
        Collections.sort(solucao, new CompararBins());	
        Collections.reverse(solucao);
    }
}