package sis.infracomp.threads;

import java.util.Random;

public class Comensal extends Thread{
    private int numPlatos;              // El número de platos totales que deben comer los comensales
    private int numPlatosActual;        // El número de platos actual que han comido
    private int numPlatosMitad;         // El número de platos necesarios para tener que esperar al resto de comensales
    private boolean cubiertoT1;         // Si es true, entonces tiene un cubierto de tipo T1, false de lo contrario
    private boolean cubiertoT2;         // Si es true, entonces tiene un cubierto de tipo T2, false de lo contrario
    private int tipoCubiertoBuscado;    // El tipo de cubierto que está busscando en esta iteración (1 o 2)
    private Fregadero fregadero;
    private Mesa mesa;
    private Random r = new Random();

    public Comensal(int numPlatos, Fregadero fregadero, Mesa mesa){
        this.fregadero = fregadero;
        this.mesa = mesa;
        this.numPlatos = numPlatos;
        this.numPlatosMitad = Math.floorDiv(numPlatos, 2);

        this.cubiertoT1 = false;
        this.cubiertoT2 = false;
        this.tipoCubiertoBuscado = 0; 
    }

    public void run(){
        mesa.comenzarComer();             
        while (numPlatosActual < numPlatosMitad){  
            comer();
        }
        mesa.terminarComer();
        if (mesa.getComensalesComiento() == 0){
            mesa.continuarComiendo();
        }
        else{
            mesa.decirAlgo(Thread.currentThread().getId()+": Esperando a que el resto de los comensales lleguen a "+numPlatosMitad);
            mesa.esperarComensales();
        }
        mesa.comenzarComer();
        while (numPlatosActual < numPlatos){
            comer();
        }
        mesa.terminarComer();
        mesa.decirAlgo(Thread.currentThread().getId()+": Estoy lleno");
        if (mesa.getComensalesComiento() == 0){
            mesa.decirAlgo(Thread.currentThread().getId()+": Fui el último en terminar de comer, a mimir");
            System.exit(0);
        }
    }

    private void verificarCubierto(int cubierto){
        if (cubierto == 1){
            this.cubiertoT1 = true;
            this.tipoCubiertoBuscado = 2;
        }else{
            this.cubiertoT2 = true;
            this.tipoCubiertoBuscado = 1;
        }
    }

    private void comer(){
        if (cubiertoT1 && cubiertoT2){
            try{sleep((long) r.nextInt(3)+3);}catch(Exception e){e.printStackTrace();}
            numPlatosActual += 1;
            mesa.decirAlgo(Thread.currentThread().getId()+": Voy a comer. Me faltan "+(numPlatos-numPlatosActual)+" platos");
            try{sleep((long) r.nextInt(3)+3);}catch(Exception e){e.printStackTrace();}

            this.cubiertoT1 = false;
            
            while(!fregadero.sePuedeMeter()){
                Thread.yield();
            }
            fregadero.agregar(1);
            this.cubiertoT2 = false;
            while(!fregadero.sePuedeMeter()){
                Thread.yield();
            }
            fregadero.agregar(2);

            tipoCubiertoBuscado = 0;
        }
        else{

            int cubierto = mesa.retirar();
            if (tipoCubiertoBuscado == 0){
                verificarCubierto(cubierto);
            }
            else if (cubierto == tipoCubiertoBuscado){
                verificarCubierto(cubierto);
            }
            else if (cubiertoT1 || cubiertoT2) {
                mesa.agregar(cubierto);
                mesa.agregar(cubierto);

                this.cubiertoT1 = false;
                this.cubiertoT2 = false;
            } 
            else{
                mesa.agregar(cubierto);
            }   
        }
    }
}
