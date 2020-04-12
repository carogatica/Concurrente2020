import java.util.ArrayList;

public class Fabrica {

    RedDePetri rdp = new RedDePetri(marcaInicial, Imenos, Imas,matrizInhibicionH, transicionesConTiempo, archivo);
    Politicas politica = new Politicas(rdp);

    //VER
    //politica.addPrioridad(0 , 0);  

    Condicion condicionDeFinalizacion = new Condicion(false);
    Monitor monitor = new Monitor(rdp, politica, condicionDeFinalizacion);
    int secuencia1[] = {0, 4};
    int secuencia2[] = {0, 14};
    int secuencia3[] = {7, 2, 1};
    int secuencia4[] = {11, 9, 8};
    int secuencia5[] = {5, 3};
    int secuencia6[] = {13, 12};
    int secuencia7[] = {6};
    int secuencia8[] = {10};
/*     ArrayList<Integer> t2 = new ArrayList<>();
    ArrayList<Integer> t3 = new ArrayList<>();
    t2.add(1);
    t2.add(6);
    t3.add(10);
    t3.add(8); */
    
    Tarea origen1 = new Tarea(monitor, secuencia1);
    Tarea origen2 = new Tarea(monitor, secuencia2);
//    Tarea cpu1 = new Tarea(monitor, secuencia3, t2);
//    Tarea cpu2 = new Tarea(monitor, secuencia4, t3);
    Tarea cpu1 = new Tarea(monitor, secuencia3);
    Tarea cpu2 = new Tarea(monitor, secuencia4);
    Tarea cpu11 = new Tarea(monitor, secuencia7);
    Tarea cpu22 = new Tarea(monitor, secuencia8);
    Tarea nucleo1 = new Tarea(monitor, secuencia5);
    Tarea nucleo2 = newTarea(monitor, secuencia6);

    Thread h1 = new Thread(origen1);
    Thread h2 = new Thread(origen2);
    Thread h3 = new Thread(cpu1);
    Thread h4 = new Thread(cpu2);
    Thread h5 = new Thread(nucleo1);
    Thread h6 = new Thread(nucleo2);
    Thread h7 = new Thread(cpu11);
    Thread h8 = new Thread(cpu22);

    h1.start();
    h2.start();
    h3.start();
    h4.start();
    h5.start();
    h6.start();
    h7.start();
    h8.start();
}