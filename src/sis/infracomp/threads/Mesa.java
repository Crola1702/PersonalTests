package sis.infracomp.threads;

import java.util.ArrayList;

public class Mesa {
    private ArrayList<Object> buff;                 // El ArrayList del buffer
    private Object monitorEspera = new Object();    // El monitor de espera para añadir o retirar cubiertos
    private int comensalesComiendo;                 // La cantidad de comensales comiendo en un momento dado

    public Mesa(){
        buff = new ArrayList<Object>();
    }

    /**
     * Agrega un cubierto a la mesa (siempre se puede agregar)
     */
    public void agregar(int cubiertos){
        synchronized(monitorEspera){
            buff.add(cubiertos);
            monitorEspera.notify();
        }
    }

    /**
     * Retira un cubierto
     * El cubierto solo se puede retirar si hay en el buffer
     * Si no hay cubierto, espera en un monitor de espera
     */
    public int retirar(){
        synchronized(monitorEspera){
            while (buff.size() == 0){
                try{monitorEspera.wait();}catch(Exception e){e.printStackTrace();}
            }
            Integer i = (Integer) buff.remove(0);
            System.out.println("Mesa: "+buff+" --- " + Thread.currentThread().getId() +" ha retirado un: "+i);
            return i;
        }
    }

    /**
     * Realiza una espera en el objeto Mesa
     */
    public synchronized void esperarComensales(){
        try{wait();}catch(Exception e){e.printStackTrace();}
    }

    /**
     * Realiza un notifyAll en el objeto Mesa
     */
    public synchronized void continuarComiendo(){
        notifyAll();
    }

    /**
     * Aumenta el contador de comensales comiendo 
     */
    public synchronized void comenzarComer(){
        System.out.println(Thread.currentThread().getId()+": Comienzo a comer. Ya hay "+comensalesComiendo);
        this.comensalesComiendo += 1;
    }

    /**
     * Reduce el contador de comensales comiendo 
     */
    public synchronized void terminarComer(){
        this.comensalesComiendo -= 1;
    }

    /**
     * @return el número de comensales comiendo
     */
    public synchronized int getComensalesComiento(){
        return this.comensalesComiendo;
    }

    public synchronized void decirAlgo(String algo){
        System.out.println(algo);
    }
}
