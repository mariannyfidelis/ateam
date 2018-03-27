package Utilidades;

import ComunicaoConcorrenteParalela.TipoMemoria;
import Heuristicas.Memoria;
import java.io.IOException;

public class TesteGRASP {

    
    public static void main(String args[]) throws IOException{
    
        //Aqui as memórias serão inicializadas e aguardará por conexões
        
        Memoria memoria_capacidade_respeitada = new Memoria(4040, "localhost","Mem1", 10, TipoMemoria.A);
               
    
        //Aqui deve ser inicializado as 3 memórias cada com a condição de parada !!!!!
    }
    
}
