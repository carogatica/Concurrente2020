import java.util.ArrayList;

public class Tarea implements Runnable{
    private Monitor monitor;
    private int disparos[];
    private static boolean finalizar;

    public Tarea(Monitor monitor, int disparos[]){
        this.monitor = monitor;
        this.disparos = disparos;
        finalizar=false;
    }

    @Override
	public void run() {
		while(!finalizar){
            try{
                for(int i=0; i<disparos.length; i++){
                    monitor.disparar(disparos[i]);
                }
            }catch(Exception e){
                System.out.println("ocurrio: "+ e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public static void finalizar(){
        finalizar=true;
	}
}