package HHDInternal;

import HHDInterfaces.ISolutionProvider;


public class SolutionProviderFactory{

    
	private SolutionProviderFactory(){}
	
		
	public ISolutionProvider newSolutionProvider() {
		return new HHDHeuristic();
	}
	
	public ISolutionProvider newSolutionProvider(String solutionProviderClassName){
	
		try {
			Class conversorClass = Class.forName(solutionProviderClassName);
			return (ISolutionProvider) conversorClass.newInstance();
		}
		catch (Exception ex)
		{
			System.err.println("Impossível criar objeto: não foi possível obter instância da SolutionProvider " + solutionProviderClassName);
			ex.printStackTrace(System.err);
		}
		return null;
	}

	public static SolutionProviderFactory getInstance(){
	
		return new SolutionProviderFactory();
	}

}
