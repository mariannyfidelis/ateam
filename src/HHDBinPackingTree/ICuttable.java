package HHDBinPackingTree;

import HHDInterfaces.ISobra;

public interface ICuttable extends ISobra {
	
    public abstract boolean corte(float pCorte, boolean vertical);
}
