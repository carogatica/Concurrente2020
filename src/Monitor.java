import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor{
    private Semaphore mutex=new Semaphore(1);  //semaforo binario de exclusion mutua
    private Cola[] VariablesDeCondicion;  //condiciones de sincronizacion de cada transicion
    private RedDePetri RdP;     //red que controla la logica del sistema
    private Politicas politica; //politicas que resuelven los conflictos de la red
    //private Condicion condicionDeFinalizacion;
    //private int finalN2;
    //private int finalN1;
    //private final int CANT_TAREAS = 1000;
    //private String disparosRealizados;
    //private ArrayList<Integer> disparos;
    private int[] vSensibilizadas;
    private int[] vColas;
    private int[] vResultante;
    private boolean k;

    public Monitor(RedDePetri red, Politicas politicas,Condicion condicion){
        this.RdP = red;
        this.VariablesDeCondicion = new Cola[RdP.getCantTransiciones()];  //se generan tantas variables de condicion como transiciones haya en la RdP
        this.politica = politicas;
        GenerarVarCond();
        //this.condicionDeFinalizacion = condicion;
        //this.finalN1 = 0;
        //this.finalN2 = 0;
    }

    public void disparar(int transicion) throws IllegalDisparoException {
        { //dispara una transicion   
            int cont = 0;
            int seleccionado;
            int auxIndice;
            try {
                mutex.acquire();
                k = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(k == true){
                k = RdP.disparar(transicion);

                if(k){
                    vSensibilizadas = RdP.sensibilizadas();
                    vColas = VariablesDeCondicion.quienesEstan();
                    for (int i=0; i < vSensibilizadas.length(); i++){
                        if(vSensibilizadas[i] == 1 && vColas[i] == 1){
                            vResultante[i] = 1;
                            cont++;
                            auxIndice = i;
                        } 
                        else vResultante[i] = 0;
                    }
                    if(cont == 0) k = false;

                    if(cont == 1){
                        VariablesDeCondicion[transicion].Resume();
                        k = false;
                    }
                    if(cont > 1){
                        seleccionado = Politica.cual(vResultante);
                        VariablesDeCondicion[transicion].Resume();
                        k = false;
                    }
                }
                else{
                    mutex.release();
                    VariablesDeCondicion[transicion].Delay();
                }
            }
            mutex.release();        //devuelve mutex
        }
    }

    
}