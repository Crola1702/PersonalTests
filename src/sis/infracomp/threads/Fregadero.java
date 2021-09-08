package sis.infracomp.threads;

import java.util.*;

public class Fregadero {
    private int tamFregadero;
    private ArrayList<Object> buff;

    public Fregadero(int tamFregadero){
        this.tamFregadero = tamFregadero;
        buff = new ArrayList<Object>();
    }

    public synchronized void agregar(int cubiertos){
            buff.add(cubiertos);
            System.out.println("Fregadero: "+buff+" --- Metieron al fregadero un "+cubiertos);
    }

    public synchronized int retirar(){
            Integer i = (Integer) buff.remove(0);
            System.out.println("Fregadero: "+buff+" --- Lavaplatos está lavando "+i);
            return i;
    }

    public synchronized boolean sePuedeMeter(){
        return buff.size() < tamFregadero;
    }

    public synchronized boolean sePuedeRetirar(){
        return buff.size() > 0;
    }
}
