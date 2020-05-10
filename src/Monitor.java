import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor {
    // nada
    private Semaphore mutex = new Semaphore(1); // semaforo binario de exclusion mutua
    private Cola[] VariablesDeCondicion; // condiciones de sincronizacion de cada transicion
    private RedDePetri RdP; // red que controla la logica del sistema
    private Politicas politica; // politicas que resuelven los conflictos de la red
    private Condicion condicionDeFinalizacion;
    private int finalN2;
    private int finalN1;
    private final int CANT_TAREAS = 1000;
    // private String disparosRealizados;
    // private ArrayList<Integer> disparos;
    // private ArrayList<Integer> vSensibilizadas;
    private ArrayList<Integer> vSensibilizadas;

    private int[] vColas;
    private int[] m;
    private boolean k;

    public Monitor(RedDePetri red, Politicas politicas, Condicion condicion) {
        this.RdP = red;
        this.VariablesDeCondicion = new Cola[RdP.getCantTransiciones()]; // se generan tantas variables de condicion
                                                                         // como transiciones haya en la RdP
        this.politica = politicas;
        GenerarVarCond();
        this.condicionDeFinalizacion = condicion;
        this.finalN1 = 0;
        this.finalN2 = 0;
    }

    public void disparar(int transicion) throws IllegalDisparoException {

        int cont = 0;
        int seleccionado;
        int auxIndice;
        try {
            mutex.acquire();
            k = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        k = RdP.esSensibilizada(transicion) && RdP.disparar(transicion);;
        while (k) {
            //k = RdP.disparar(transicion);
            //k = RdP.esSensibilizada(transicion) && RdP.disparar(transicion);;
            System.out.println("Transicion: " + transicion);
            if (k) {
                /*while(!(RdP.esSensibilizada(transicion))){
                    VariablesDeCondicion[transicion].Delay(); 
                }*/
                vSensibilizadas = RdP.getSensibilizadas();
                vColas = new int[RdP.getCantTransiciones()];
                m = new int[RdP.getCantTransiciones()];

                vColas = quienesEstan();
                for (int i = 0; i < vSensibilizadas.size(); i++) {
                    if (vSensibilizadas.get(i) == 1 && vColas[i] == 1) {
                        m[i] = 1;
                        cont++;
                        auxIndice = i;
                    } else {
                        m[i] = 0;
                    }
                }
                if (cont == 0) {
                    //VariablesDeCondicion[transicion].Delay();
                    k = false;
                }

                if (cont == 1)
                    VariablesDeCondicion[transicion].Resume();

                if (cont > 1) {
                    seleccionado = politica.cual(m);
                    VariablesDeCondicion[transicion].Resume();
                }

                cont = 0;
                System.out.println("Disparada: " + transicion);
            } 
            while(!RdP.esSensibilizada(transicion)) {
                
                VariablesDeCondicion[transicion].Delay();
                System.out.println("se traba aca");
                // k = true;
                //mutex.release();
            }
            //actualizarCondiciones(transicion);
            //desbloquearHilo();
        }
        actualizarCondiciones(transicion);
        desbloquearHilo();
        mutex.release(); // devuelve mutex

    }

    private void GenerarVarCond() { // crea tantas variables de condicion como cantidad de transiciones tiene la red
                                    // de petri
        for (int i = 0; i < this.VariablesDeCondicion.length; i++) {
            this.VariablesDeCondicion[i] = new Cola(this.mutex);
        }
    }

    private int[] quienesEstan() {
        for (int i = 0; i < VariablesDeCondicion.length; i++) {
            if (this.VariablesDeCondicion[i].Empty()) {
                vColas[i] = 0;
            } else {
                vColas[i] = 1;
            }

        }
        return vColas;
    }

    private void actualizarCondiciones(int transicion) {// lleva la cuenta de las tareas que se hacen en cada
                                                        // nucleo(service_rateN1 y N2)
        if (transicion == 3)
            finalN1++;
        else if (transicion == 12)
            finalN2++;

        if ((finalN1 + finalN2) >= CANT_TAREAS && !condicionDeFinalizacion.getCondicion()) {
            System.out.println("Llega");
            // desbloquearTodos();
            condicionDeFinalizacion.setCondicion(true);
            System.out.println("Tareas completadas en N1: " + finalN1);
            System.out.println("Tareas completadas en N2: " + finalN2);
            RdP.printArchivo(finalN1, "Tareas completadas en N1");
            RdP.printArchivo(finalN2, "Tareas completadas en N2");
        }

    }

    private ArrayList<Integer> ColasAndSensibilizadas() { //devuelve una lista de las transiciones sensibilizadas
        // que tienen hilos queriendo dispararlas
        ArrayList<Integer> desbloqueables = new ArrayList<>();
        for(int i = 0; i < this.VariablesDeCondicion.length; i++){
            if( !(VariablesDeCondicion[i].Empty()) && RdP.esSensibilizada(i)){
                desbloqueables.add(i);
            }
        }
        return desbloqueables;
    }


    private void desbloquearHilo(){      //desbloquea un hilo de las colas de condicion cuya transicion esté sensibilizada
        ArrayList<Integer> desbloqueables = ColasAndSensibilizadas();
        if (desbloqueables.isEmpty()) System.out.println("no hay nada para desbloquear");
        if(!desbloqueables.isEmpty()){
            System.out.println(VariablesDeCondicion[politica.cual(desbloqueables)]);
            VariablesDeCondicion[politica.cual(desbloqueables)].Resume();
            k = true; 
        } 
        return;
    }
}
