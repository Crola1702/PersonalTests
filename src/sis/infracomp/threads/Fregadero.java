package sis.infracomp.threads;

import java.util.*;

public class Fregadero {
    private int tamFregadero;       // El tamaño máximo del fregadero
    private ArrayList<Object> buff; // El ArrayList que guarda los cubiertos

    public Fregadero(int tamFregadero){
        this.tamFregadero = tamFregadero;
        buff = new ArrayList<Object>();
    }

    /**
     * Añade un cubierto a la cola buffer, no se verifica si es posible hacerlo dentro de la función ya que esta se hace antes de hacer la inserción
     * 
     * @param cubierto el tipo de cubierto insertado (1 para cubiertoT1 y 2 para cubiertoT2)
     */
    public synchronized void agregar(int cubierto){
            buff.add(cubierto);
            System.out.println("Fregadero: "+buff+" --- Metieron al fregadero un "+cubierto);
    }

    /**
     * Quita un cubierto del tope del buffer, no se verifica si es posible haerlo dentro de la función ya que esta se hace antes de hacer la eliminación
     * 
     * @return el tipo de cubierto que se retiró (1 para cubiertoT1 y 2 para cubiertoT2)
     */
    public synchronized int retirar(){
            Integer i = (Integer) buff.remove(0);
            System.out.println("Fregadero: "+buff+" --- Lavaplatos está lavando "+i);
            return i;
    }

    /**
     * Un método sincronizado que verifica si es posible ingresar un cubierto al fregadero
     * 
     * @return true si hay espacio en el fregadero, falso si está lleno
     */
    public synchronized boolean sePuedeMeter(){
        return buff.size() < tamFregadero;
    }

    /**
     *  Un método sincronizado que verifica si es posible retirar un cubierto del fregadero
     * 
     * @return true si hay cubiertos en el fregadero, false de lo contrario
     */
    public synchronized boolean sePuedeRetirar(){
        return buff.size() > 0;
    }
}
