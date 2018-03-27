package Utilidades;

public class InfArqPortMap {

    private String nome_Memoria;
    private String IP;
    private int porta;

    public InfArqPortMap(String nome_Memoria, String IP, int porta) {
        
        this.nome_Memoria = nome_Memoria;
        this.IP = IP;
        this.porta = porta;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getNome_Memoria() {
        return nome_Memoria;
    }

    public void setNome_Memoria(String nome_Memoria) {
        this.nome_Memoria = nome_Memoria;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
    
}
