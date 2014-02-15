package HHDInternal;


public class Position{

	private float x;
	private float y;
	
	public Position(Position p){
		x = p.x;
		y = p.y;
	}
	
	public Position(float posx, float posy){
		setPosition(posx, posy);
	}
	
	public void setPosition (float posx, float posy){
		x = posx;
		y = posy;
	}
	
	public float getPositionX(){
		return x;
	}

	public float getPositionY(){

		return y;
	}
	
	//O pr�ximo m�todo retorna true se duas posi��es s�o as mesmas
	public boolean ehIgualA(Position p){
	
		return (x == p.getPositionX()) && (y == p.getPositionY());
	}
}
