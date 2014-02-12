package j_HeuristicaArvoreNAria;

import HeuristicaConstrutivaInicial.IPedido;
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
		
		File arquivo = new File("/home/marianny/Desktop/Dissertacao/ArtigsApresentacao/Alex Trindade/grasp2d2/" +
				                "teste1.dat");
		
		try{
			FileReader arq_reader = new FileReader(arquivo);
			BufferedReader buffer_reader = new BufferedReader(arq_reader);
			String linha = "";
			String result = "";
			
			LinkedList<Pedidos> listaPedidos = new LinkedList<Pedidos>();
			int cont = 0;
						
			while((linha = buffer_reader.readLine()) != null){
				
				if(cont == 0){
					result = linha;
					String resultado[] = result.split(" ");
					chapa = new Chapa(Integer.valueOf(resultado[0].trim()), Integer.valueOf(resultado[1].trim()));
				}
				
				if(cont > 1){
					result = linha;
					String resultado[] = result.split(" ");
					listaPedidos.add(new Pedidos(cont, Integer.valueOf(resultado[0].trim()), 
					          Integer.valueOf(resultado[1].trim()))); 
				}
				cont++;
			}
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
}