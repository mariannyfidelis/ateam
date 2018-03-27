package HHDComparadores;

import HHDInternal.Pedaco;
import java.util.Comparator;


public class ComparadorPedacos implements Comparator{

    public int compare(Object arg0, Object arg1){
	
	if(Pedaco.class.isAssignableFrom(arg0.getClass()) &&   Pedaco.class.isAssignableFrom(arg1.getClass()))
              return ((Pedaco) arg0).retorneDimensao().compareAreas(((Pedaco) arg1).retorneDimensao());
	else
	   throw new ClassCastException();
	}
}
