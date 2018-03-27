package HHDInterfaces;

public interface IObservable {

	public abstract void attach(ITreeObserver observador);

	public abstract ITreeObserver detach(ITreeObserver observador);
	
	public void sendUpdateSignal(int indice);
	
	public void sendUpdateSignal();
}
