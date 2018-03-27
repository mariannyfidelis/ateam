package HHDInterfaces;

import HHDInternal.Ponto;


public interface IPedaco{

	public int retorneIndiceBin();
	public IDimensao2d getDimensao();
	public Ponto getPontoInferiorEsquerdo();
	public boolean podeRotacionar();
	public boolean temPeca();
	public boolean isSelected();
        public void removaDoBin();
	public boolean estaRemovido();
	public int getID();
}
