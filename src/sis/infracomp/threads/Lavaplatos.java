package sis.infracomp.threads;

import java.util.Random;

public class Lavaplatos extends Thread {
    private Fregadero fregadero;
    private Mesa mesa;
    private Random r = new Random();

    public Lavaplatos (Fregadero fregadero, Mesa mesa){
        this.fregadero = fregadero;
        this.mesa = mesa;
    }

    public void run(){
        // Ejecuta un ciclo infinito para lavar los platos
        while (true){

            // Verifica si es posible retirar un cubierto del fregadero para lavarlo
            while(!fregadero.sePuedeRetirar()){

                // Si no hay cubiertos aún espera de manera semi-activa con un yield
                Thread.yield();
            }

            // Cuando tenga la posibilidad de retirar el cubierto ejecuta la función retirar de manera sincronizada
            int cubierto = fregadero.retirar();

            // Luego procede a lavar el cubierto con la instrucción sleep
            try{
                sleep((long) (r.nextInt(2)+1)*1000);
            }catch(Exception e){e.printStackTrace();}

            // Finalmente, agrega el cubierto a la mesa
            mesa.agregar(cubierto);
        }
    }
}
