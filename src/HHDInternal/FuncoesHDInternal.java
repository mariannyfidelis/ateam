package HHDInternal;

import java.io.File;
import Utilidades.Chapa;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import HHDBinPackingTree.BinPackTreeForest;
import HHDBinPackingTree.ComparadorBinPackTree;
import Utilidades.Funcoes;
import javax.swing.JOptionPane;

public class FuncoesHDInternal {
    
    static Chapa chapa;
    private static Pedidos[] vetor_info;
        
    public static LinkedList<Pedidos> lerArquivo() throws IOException{
	                
        //File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/Utilidades/arquivoFleszarHD.txt");
        //File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/Utilidades/L1HD2");
        //File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/Utilidades/L2HD");
        
        /*############### TESTES PARA DISSERTAÇÃO FLESZAR 2013  ###################*/
        
        //File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/TestClass1F/C1_I1_20H");
        //File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/TestClass1F/C1_I1_40H");
        //File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/TestClass1F/C1_I1_60H");
        //File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/TestClass1F/C1_I1_80H");
        
        //Teste classe 1 - 80 itens
        File arquivo = new File("/home/marianny/workspace/ProjetoA-Team/src/TestClass1F/C1_I4_60H");
        //File arq = new File("/home/lec/Documentos/ProjetoA-Team/src/TestClass1F/C1_I9_100G");
        
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
                        Integer l = Integer.valueOf(resultado[0].trim());
                        Integer a = Integer.valueOf(resultado[1].trim());
                        chapa = new Chapa(l, a);
                        Funcoes.setChapa(chapa);
                }
                if(cont == 1){
                    result = linha;
                    Integer tam = Integer.valueOf(result);
                    vetor_info = new Pedidos[tam];
                }

                if(cont > 1){
                        result = linha;
                        String resultado[] = result.split(" ");
                        listaPedidos.add(new Pedidos(cont-1, Integer.valueOf(resultado[0].trim()), 
                                  Integer.valueOf(resultado[1].trim()))); 
                        vetor_info[cont-2] = new Pedidos(cont-1, Integer.valueOf(resultado[0].trim()), Integer.valueOf(resultado[1].trim()));
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

    public static Chapa getChapa(){
    
        return chapa;
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

        FuncoesHDInternal.vetor_info = vetor_info;
    }
    
    //Compara BinPacksTree
    public static void ordenaSolucaoCrescente(BinPackTreeForest solucaoArvore){
    
        Collections.sort(solucaoArvore.getListaBinPackTrees(), new ComparadorBinPackTree());
    }
    
    public static void ordenaSolucaoDecrescente(BinPackTreeForest solucaoArvore){
    
        Collections.sort(solucaoArvore.getListaBinPackTrees(), new ComparadorBinPackTree());
        Collections.reverse(solucaoArvore.getListaBinPackTrees());
    }
    
    
}