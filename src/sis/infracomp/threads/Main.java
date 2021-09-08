package sis.infracomp.threads;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    // private static int numComensales = 6;
    // private static int numCubiertosT1 = 7;
    // private static int numCubiertosT2 = 10;
    // private static int numPlatos = 8;
    // private static int tamFregadero = 5;

    public int numComensales;
    public int tamFregadero;
    public int numPlatos;
    public int numCubiertosT1;
    public int numCubiertosT2;

    public static void main(String[] args) {

        // Información Importante
        System.out.println("El resultado está en un archivo llamado \"solucion.txt\" en el directorio raíz del proyecto");
        System.out.println("Para modificar las propiedades debe modificarse el archivo");

        // Creación del objeto Main que maneja todo el programa
        Main main = new Main();

        // Carga las propiedades del archivo main.properties
        main.loadProperties();

        // Estas instrucciones se encargan de modificar el out del sistema para permitir escribirlo en un archivo y que sea de más fácil acceso que la consola
        try{
            PrintStream fileOut = new PrintStream("./solucion.txt");
            System.setOut(fileOut);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

        // AQUÍ INICIA EL PROGRAMA REQUERIDO

        // Se crean objetos de tipo Fregadero, Mesa y Lavaplatos
        Fregadero fregadero = new Fregadero(main.tamFregadero);
        Mesa mesa = new Mesa();
        Lavaplatos lavaplatos = new Lavaplatos(fregadero, mesa);

        // Luego se agregan los cubiertos de tipo 1
        for (int i = 0; i<main.numCubiertosT1; i++)
            mesa.agregar(1);   

        // Y posteriormente los cubiertos de tipo 2
        for (int i = 0; i<main.numCubiertosT2; i++)
            mesa.agregar(2);
        
        // Se inicia el loop infinito del lavaplatos
        lavaplatos.start();

        // Se crean e inician cada uno de los threads Comensal
        for (int i = 0; i<main.numComensales; i++){
            new Comensal(main.numPlatos, fregadero, mesa).start();
        }
    }

    /**
     * Función hecha para cargar en Main las propiedades del archivo. El archivo está en el directorio raíz y se llama "main.properties"
     */
    public void loadProperties(){
        try (InputStream input = new FileInputStream("main.properties")){
            Properties prop = new Properties();

            prop.load(input);

            this.numComensales = Integer.parseInt(prop.getProperty("main.numComensales"));
            this.numCubiertosT1 = Integer.parseInt(prop.getProperty("main.numCubiertosT1"));
            this.numCubiertosT2 = Integer.parseInt(prop.getProperty("main.numCubiertosT2"));
            this.numPlatos = Integer.parseInt(prop.getProperty("main.numPlatos"));
            this.tamFregadero = Integer.parseInt(prop.getProperty("main.tamFregadero"));

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
