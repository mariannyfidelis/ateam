package HHDBinPackingTree;

import HHDInterfaces.ICorte;
import HHDInternal.Ponto;

public class BinPackCorte implements ICorte{

    private float posicaoCorte;
    private float tamanho;
    private boolean vertical;
    private Ponto pontoChapaCortada;

    public BinPackCorte(float cutPosition, float tamanho, boolean isVertical, Ponto ponto){

        posicaoCorte = cutPosition;
        this.tamanho = tamanho;
        pontoChapaCortada = ponto;
        vertical = isVertical;
    }
    
    @Override
    public float getPosicaoCorte(){

        return posicaoCorte;
    }
    
    @Override
    public float getTamanho(){

        return tamanho;
    }
    
    @Override
    public boolean eVertical(){

        return vertical;
    }

    //sera necessario, ou isso faz parte apenas do algoritmo? Hmmm....
    //lembrando que, no algoritmo, o ID do corte esta relacionado ao "nivel" desse corte.
    //no entanto, o nivel do corte, naquele contexto, pode significar algo diferente, quando
    //aplicado a diferentes estruturas de dados. Pode ser necessaria uma outra forma de se fazer
    //essa operacao.

    //Esses comentarios me fazem lembrar de algo importante:
    //O conversor da arvore esta GEN�RICO, ou vale apenas para o caso desse algoritmo?
    //Talvez podemos criar uma "Interface Conversor" e, para cada algoritmo, converter a estrutura do algoritmo
    //para a estrutura de dados subjacente. Isso pode ser ruim. PENSAAAAAAAr.
    @Override
    public int getId(){

        return 0;
    }

    @Override    
    public Ponto getPontoChapaCortada(){

        return pontoChapaCortada;
    }

}