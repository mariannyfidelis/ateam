package ATeam;

import Heuristicas.Memoria;
import ComunicaoConcorrenteParalela.TipoMemoria;
import java.io.IOException;

public class IniciaServidor {
   
    public static void main(String args[]) throws IOException{
    
    //Aqui as memórias são inicializadas
       Memoria memoria_capcid_respeitada = new Memoria(3000, "127.0.1.1", "Memoria_1", 10, TipoMemoria.A);
       // Memoria memoria_capcd_Nao_respeitada = new Memoria(porta, host, null, tamanho_memoria, TipoMemoria.B);
       // Memoria memoria_arvore = new Memoria(porta, host, null, tamanho_memoria, TipoMemoria.C));
         
    }
    
}
