package sis.infracomp.threads;

public class Main {
    private static int numComensales = 6;
    private static int numCubiertosT1 = 7;
    private static int numCubiertosT2 = 10;
    private static int numPlatos = 8;
    private static int tamFregadero = 5;

    // private static int numComensales = 2;
    // private static int tamFregadero = 4;
    // private static int numPlatos = 2;
    // private static int numCubiertosT1 = 2;
    // private static int numCubiertosT2 = 2;
    public static void main(String[] args) {
        Fregadero fregadero = new Fregadero(tamFregadero);
        Mesa mesa = new Mesa();
        Lavaplatos lavaplatos = new Lavaplatos(fregadero, mesa);

        for (int i = 0; i<numCubiertosT1; i++)
            mesa.agregar(1);   

        for (int i = 0; i<numCubiertosT2; i++)
            mesa.agregar(2);   

        lavaplatos.start();
        for (int i = 0; i<numComensales; i++){
            new Comensal(numPlatos, fregadero, mesa).start();
        }
    }
}
