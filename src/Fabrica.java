import java.util.ArrayList;

public class Fabrica {
	
	private Monitor monitor ;
	private Thread h1, h2, h3,h4, h5, h6, h7, h8, h9; 
	//private Thread h1;

	

	public Fabrica( Monitor monitor ) {
		
		 	this.monitor = monitor;
		 
		 	int secuencia1[] = {0};
		    int secuencia2[] = {4};
		    int secuencia3[] = {7, 2, 1};
		    int secuencia4[] = {11, 9, 8};
		    int secuencia5[] = {5, 3};
		    int secuencia6[] = {13, 12};
		    int secuencia7[] = {6};
			int secuencia8[] = {10};
			int secuencia9[] = {14};
		/*     ArrayList<Integer> t2 = new ArrayList<>();
		    ArrayList<Integer> t3 = new ArrayList<>();
		    t2.add(1);
		    t2.add(6);
		    t3.add(10);
		    t3.add(8); */
		    
		    Tarea origen1 = new Tarea(monitor, secuencia1);
			Tarea origen2 = new Tarea(monitor, secuencia2);
			Tarea origen22 = new Tarea(monitor, secuencia9);
		//    Tarea cpu1 = new Tarea(monitor, secuencia3, t2);
		//    Tarea cpu2 = new Tarea(monitor, secuencia4, t3);
		    Tarea cpu1 = new Tarea(monitor, secuencia3);
		    Tarea cpu2 = new Tarea(monitor, secuencia4);
		    Tarea cpu11 = new Tarea(monitor, secuencia7);
		    Tarea cpu22 = new Tarea(monitor, secuencia8);
		    Tarea nucleo1 = new Tarea(monitor, secuencia5);
		    Tarea nucleo2 = new Tarea(monitor, secuencia6);
		
		    h1 = new Thread(origen1);
			h2 = new Thread(origen2);
			h9 = new Thread(origen22);
		    h3 = new Thread(cpu1);
		    h4 = new Thread(cpu2);
		    h5 = new Thread(nucleo1);
		    h6 = new Thread(nucleo2);
		    h7 = new Thread(cpu11);
		    h8 = new Thread(cpu22);
		
		    h1.start();
			h2.start();
			h9.start();
		    h3.start();
		    h4.start();
		    h5.start();
		    h6.start();
		    h7.start();
		    h8.start();
	}//constructor

	
	public void Interrumpir()
	{
		 h1.interrupt();
		 h2.interrupt();
		 h3.interrupt();
		 h4.interrupt();
		 h5.interrupt();
		 h6.interrupt();
		 h7.interrupt();
		 h8.interrupt();
		 h9.interrupt();
		 
		
	}
	

}//CLASE