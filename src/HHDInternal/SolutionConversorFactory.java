package HHDInternal;

import HHDBinPackingTree.BinPackTreeConversor;
import HHDInterfaces.ISolutionConversor;


public class SolutionConversorFactory{

	private static final SolutionConversorFactory singleton = new SolutionConversorFactory();
	
	private SolutionConversorFactory() {} // Apenas para garantir que não serão criadas novas copias dessa factory

	public static SolutionConversorFactory getInstance(){
            
		return singleton;
	}
	
	//Atenção: o nomeClasseConversora deve ser "completamente qualificado". (isto é, este nome deve incluir
	//			  todo o caminho dos pacotes)
	
	//TODO Estabelecer padrao de nomes e pacotes para os conversores, de forma a facilitar o uso desta factory.
	public ISolutionConversor newConversor(String nomeClasseConversora) throws Exception{
            
            try {
            	Class conversorClass = Class.forName(nomeClasseConversora);
            	return (ISolutionConversor) conversorClass.newInstance();
            }
            catch (Exception ex){
                
		System.err.println("Impossível criar objeto: não foi possível obter instância da classe conversora " + nomeClasseConversora);
		throw ex;
            }
	}
	
	//Conversor "default"
	public ISolutionConversor newConversor(){
	
            return new BinPackTreeConversor();
	}
}
