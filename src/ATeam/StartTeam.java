package ATeam;

public class StartTeam {

	public static void main(String[] args) throws Exception {

		Thread servidor = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					ServidorFinal.main(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		IniciaClientes.main(args);

		servidor.join(); // Encerra quando o servidor finalizar !!!!
		
		
		//garantir que o Inicia Cliente encerre após o servidor 
		//Verficar se somente com o .join resolve !!!
	}

}
