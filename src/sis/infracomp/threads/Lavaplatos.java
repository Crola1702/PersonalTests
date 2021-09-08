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
        while (true){
            while(!fregadero.sePuedeRetirar()){
                Thread.yield();
            }
            int cubierto = fregadero.retirar();
            try{
                sleep((long) r.nextInt(2)+1);
                // sleep(1);
            }catch(Exception e){e.printStackTrace();}
            mesa.agregar(cubierto);
        }
    }
}
