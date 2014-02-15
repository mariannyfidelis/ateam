package HHDInternal;

import java.awt.Color;

public class DefinicaoCores {
    
    static {
        setDefault();
    }

    public static Color corChapa; 
    public static Color corPeca;
    public static Color corMarcacaoAgrupamento;
    public static float transparenciaAgrupamento;
    public static Color corCorte;
    public static Color corSelecao;
    public static float transparenciaSelecao;
    
    public static void setDefault () {
    
        corChapa = new Color ( 169, 237, 252 ); 
        corPeca = Color.YELLOW;
        corMarcacaoAgrupamento = new Color ( 255,100,50 );
        transparenciaAgrupamento = 0.3f;
        corCorte = Color.RED;
        corSelecao = Color.BLUE;
        transparenciaSelecao = 0.3f;
    }
}
