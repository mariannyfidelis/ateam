package HHDInternal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Funcoes {
    
    static Chapa chapa;
        
    public static Chapa getChapa(){
    
        return chapa;
    
    }
    public static LinkedList<Pedidos> lerArquivo() throws IOException{
	
                
        //File arquivo = new File("/home/marianny/Desktop/Dissertacao/ArtigsApresentacao/Alex Trindade/grasp2d2/teste1.dat");
        File arquivo = new File("/home/marianny/Dropbox/Projeto/Projeo ATeam/ProjetoA-TeamTeste/src/Util/arquivo2HD.dat");
        
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
                        listaPedidos.add(new Pedidos(cont-1, Integer.valueOf(resultado[0].trim()), 
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

}