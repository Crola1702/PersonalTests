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
        // Comienza a comer, es decir, aumenta el contador de comensales comiendo
        mesa.comenzarComer();

        // Ejecuta un loop para comer desde el primer plato hasta el plato de la mitad
        while (numPlatosActual < numPlatosMitad){  
            comer();
        }

        // Una vez termina de comer, decrementa el contador de comensales comiendo
        mesa.terminarComer();

        // Si este comensal es el último entonces pide a la mesa notificar al resto que ha acabado para continuar con la cena
        if (mesa.getComensalesComiento() == 0){
            mesa.continuarComiendo();
        }

        // Si no ha terminado, dice que esperará a que el resto de comensales terminen la mitad de la cena y espera en la mesa
        else{
            mesa.decirAlgo(Thread.currentThread().getId()+": Esperando a que el resto de los comensales lleguen a "+numPlatosMitad);
            mesa.esperarComensales();
        }

        // Una vez todos se despiertan ejecutan comenzarComer para aumentar el contador de comensales comiendo en la mesa
        mesa.comenzarComer();

        // Una vez más se ejecuta el while pero esta vez para terminar de comer el numero de platos completo (puesto que numPlatosActual ya tiene el valor intermedio)
        while (numPlatosActual < numPlatos){
            comer();
        }

        // Cuando termina de comer, ejecuta terminarComer para decrementar el contador de comensales comiendo y dice que está lleno
        mesa.terminarComer();
        mesa.decirAlgo(Thread.currentThread().getId()+": Estoy lleno");

        // Si es el último comensal en terminar el último plato, entonces dice que fue el último en comer y se va a mimir, terminando así la ejecución del programa
        if (mesa.getComensalesComiento() == 0){
            mesa.decirAlgo(Thread.currentThread().getId()+": Fui el último en terminar de comer, a mimir");
            System.exit(0);
        }
    }


    /**
     * Este método verifica qué cubierto se está buscando y actualiza los cubiertos que ya se tienen
     * 
     * @param cubierto el cubierto que se ha agarraado en esta iteración
     */
    private void verificarCubierto(int cubierto){
        if (cubierto == 1){
            this.cubiertoT1 = true;
            this.tipoCubiertoBuscado = 2;
        }else{
            this.cubiertoT2 = true;
            this.tipoCubiertoBuscado = 1;
        }
    }

    /**
     * El método comer es el plato principal de este programa, pues utiliza condiciones aprovechando
     * que cubiertoT1 y cubiertoT2 son booleanos
     */
    private void comer(){
        // En primera instancia se verifica si se posee un cubiertoT1 y un cubiertoT2 para comer el plato 
        if (cubiertoT1 && cubiertoT2){

            // Si es así, el comensal come el plato (espera entre 3 y 5 segundos) y aumenta el número de platos que ha comido
            mesa.decirAlgo(Thread.currentThread().getId()+": Voy a comer. Me faltan "+(numPlatos-numPlatosActual)+" platos");
            try{sleep((long) (r.nextInt(3)+3)*1000);}catch(Exception e){e.printStackTrace();}
            numPlatosActual += 1;

            // Una vez terminado el plato, se libera de sus cubiertos y busca meterlos en el fregadero, por ello se deshace primero del cubiertoT1
            this.cubiertoT1 = false;
            
            // Mientras no pueda meterlo en el fregadero espera en modo semi-activo con yield, una vez pueda ejecuta el método agregar del fregadero
            while(!fregadero.sePuedeMeter()){
                Thread.yield();
            }
            fregadero.agregar(1);

            // Luego realiza el mismo procedimiento con el cubiertoT2
            this.cubiertoT2 = false;
            while(!fregadero.sePuedeMeter()){
                Thread.yield();
            }
            fregadero.agregar(2);

            // Finalmente se deshace del tipo de cubierto que busca, de modo que pueda agarrar cualquiera
            tipoCubiertoBuscado = 0;

            // Finalmente espera de 1 a 3 segundos por el siguiente plato
            try{sleep((long) (r.nextInt(3)+1)*1000);}catch(Exception e){e.printStackTrace();}
        }

        // Si no puede comer entonces realiza las siguientes instrucciones
        else{

            // Agarra un cubierto de la msea
            int cubierto = mesa.retirar();

            // Verifica si no busca un cubierto en especial (caso en el que haya terminado de comer un plato) y actualiza los datos de sus cubiertos
            if (tipoCubiertoBuscado == 0){
                verificarCubierto(cubierto);
            }

            // Si el cubierto que agarró corresponde con el buscado, entonces actualiza los datos de sus dos cubiertos
            else if (cubierto == tipoCubiertoBuscado){
                verificarCubierto(cubierto);
            }

            // Si el cubierto que agarró no corresponde con el que busca entonces deja los dos en la mesa y se deshace de ellos
            else if (cubiertoT1 || cubiertoT2) {
                mesa.agregar(cubierto);
                mesa.agregar(cubierto);

                this.cubiertoT1 = false;
                this.cubiertoT2 = false;
            } 

            // Finalmente, si el cubierto que agarró no corresponde con el que busca entonces lo vuelve a dejar en la mesa
            else{
                mesa.agregar(cubierto);
            }   
        }
    }
}
