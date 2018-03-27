package HHDInterfaces;

import HHDInternal.Ponto;

public interface ICorte {

	public abstract float getPosicaoCorte();

	public abstract float getTamanho();

	public abstract boolean eVertical();

	public abstract int getId();

	public abstract Ponto getPontoChapaCortada();
}