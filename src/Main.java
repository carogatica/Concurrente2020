import java.util.Calendar;
import java.util.HashMap;


public class Main {

    private static String path_matrices = "/home/caro/Documents/Concurrente/Concurrente2020/matrices";
    private static String path_Imas = path_matrices+"/Matriz_I+.txt";
    private static String path_Imenos = path_matrices+"/Matriz_I-.txt";
    private static String path_H = path_matrices+"/Matriz_H.txt";
    private static String path_marca = path_matrices+"/Marca_Inicial.txt";
    private static int[] marcaInicial;
    private static int[][] Imas;
    private static int[][] Imenos;
    private static int[][] matrizInhibicionH;

    private  static void Calendario( Calendar today,  long  milisegToday)
    {
        System.out.print("Hora de comienzo: ");     //horas:minutos:segundos:milisegundos de inicio del programa
        System.out.println(today.get(Calendar.HOUR_OF_DAY)+":"+today.get(Calendar.MINUTE)+":"+today.get(Calendar.SECOND)+":"+today.get(Calendar.MILLISECOND));
        
	    	
    }
    
    private static void CalendarioFin(Calendar today,  long  milisegToday)
    {

    	 int duracionSeg, duracionMin;
         long duracionMiliseg;
         
        Calendar today2 = Calendar.getInstance();
        System.out.print("Hora de fin: ");   //horas:minutos:segundos:milisegundos de fin del programa
        System.out.println(today2.get(Calendar.HOUR_OF_DAY)+":"+today2.get(Calendar.MINUTE)+":"+today2.get(Calendar.SECOND)+":"+today2.get(Calendar.MILLISECOND));
        duracionSeg=today2.get(Calendar.SECOND)-today.get(Calendar.SECOND);  //resta de los segundos de la hora final menos la hora inicial
        duracionMin=today2.get(Calendar.MINUTE)-today.get(Calendar.MINUTE);  //resta de los minutos de la hora final menos la hora inicial
        if(duracionSeg<0){
            duracionSeg=60+duracionSeg;     //verifica que la resta no sea negativa
            duracionMin--;
        }
        if(duracionMin<0){
            duracionMin=60+duracionMin;
        }
        duracionMiliseg =today2.getTimeInMillis() - milisegToday;
        System.out.println("Duracion total del programa: "+ duracionMin + " min y " + duracionSeg + " seg." );
        System.out.println("Duracion total en miliseg: " + duracionMiliseg);
        System.out.println("Fin del hilo Main");

    }
    
     public static void cargaMatrices(Archivo f, Operaciones op)
     {
         Imas = f.leerMatriz(path_Imas);
         Imenos = f.leerMatriz(path_Imenos);
         matrizInhibicionH = f.leerMatriz(path_H);
        marcaInicial = op.toVector(f.leerMatriz(path_marca));
        	 
     }
    
    public static void main(String[] args) throws InterruptedException {
       
    	Calendar today = Calendar.getInstance();

        long milisegToday = today.getTimeInMillis();
    	Calendario(today, milisegToday);
    		
        Archivo archivo = new Archivo();      // ???
      
        Operaciones op = new Operaciones();
        
        Archivo f = new Archivo();
        
        cargaMatrices(f, op);
       
        //------------------
    
        int posicionTransicionBuffer1 = 4;
        int posicionTransicionBuffer2 = 14;
        int posicionPlazaBuffer1 = 1;
        int posicionPlazaBuffer2 = 15;
        
        RedDePetri rdp = new RedDePetri(marcaInicial, Imenos, Imas,matrizInhibicionH, archivo);
        Politicas politica = new Politicas(rdp ,posicionTransicionBuffer1, posicionTransicionBuffer2, posicionPlazaBuffer1, posicionPlazaBuffer2);
        
        Condicion condicionDeFinalizacion = new Condicion(false);
        Monitor monitor = new Monitor(rdp, politica,condicionDeFinalizacion);

        Fabrica fabrica_hilos = new Fabrica(monitor);

        while (condicionDeFinalizacion.getCondicion() ==false){
                System.out.println("esperando");
                Thread.sleep(1000);
        }
              
        fabrica_hilos.Interrumpir();

        Thread.sleep(1000);
        
        CalendarioFin(today,  milisegToday);
        
        System.exit(0);

    }

}
