package HeuristicaConstrutivaInicial;


public interface IDimensao2d {
    
	public float retorneBase();
	public float retorneAltura();
	
	public float retorneArea ();

	public int compareAreas(IDimensao2d dimensao);
}
