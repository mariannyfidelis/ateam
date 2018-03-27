package HHDBinPackingTree;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPeca;
import HHDInternal.Ponto;
import java.io.Serializable;

public interface IBPTNode extends IPeca,Serializable{

    public static final short PEDACO = 0;
    public static final short LISTA_PEDACOS = 1;
    public static final short PECA_AGRUPADA = 2;

    public abstract boolean isCuttable();

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract IBPTNode getFather();

    public abstract void setFather(IBPTNode father);

    public abstract short getKind();

    public abstract IBPTNode getLeftSon();

    public abstract Ponto getPosition();

    public abstract void changePositionTo(Ponto position);

    public abstract IBPTNode getRigthSon();

    public abstract void Paint();

    public abstract boolean pertenceArvore(BinPackTree tree);

    public abstract boolean temPeca();

    @Override
    public abstract boolean isSelected();

    @Override
    public abstract void setSelected(boolean b);

    @Override
    public abstract IDimensao2d getDimensoes();

    public abstract void setLeftson(IBPTNode node);

    public abstract void setRightSon(IBPTNode node);

    public abstract float getCutPosition();

    public abstract float getCutTamanho();

    public abstract boolean corteVertical();

    public abstract BinPackTree getArvore();

    public abstract int getNivel();

    public abstract int retorneIndiceBin();

    public abstract void setArvore(BinPackTree arvore);

    public abstract boolean estaRemovidoDaArvore();

    public abstract void removeCortes();

    public abstract int getID();

    public void setFloresta(BinPackTreeForest floresta);

    public BinPackTreeForest getFloresta();

}