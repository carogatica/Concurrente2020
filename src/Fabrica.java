import java.util.ArrayList;

public class Fabrica {
	
	private Monitor monitor ;
	private Thread h1, h2, h3,h4, h5, h6, h7, h8; 
	//private Thread h1;
	private boolean solo_h1;


	

	public Fabrica( Monitor monitor ) {
		
		 	this.monitor = monitor;
		 	solo_h1 = true;
		 
		 	int secuencia1[] = {0, 4};      // tarea 1
		    int secuencia2[] = {0, 14};     //tarea 2
		    int secuencia3[] = {7, 2, 1};   // up/down cp1
		    int secuencia4[] = {11, 9, 8};  // up/down cp2
		    int secuencia5[] = {5, 3}; 		// n1
		    int secuencia6[] = {13, 12};    //n2
		    int secuencia7[] = {6};         //1
		    int secuencia8[] = {10};        //2
		
		    if (! solo_h1)
		    {
			    Tarea origen2 = new Tarea(monitor, secuencia2);
			    Tarea cpu2 = new Tarea(monitor, secuencia4);
			    Tarea cpu22 = new Tarea(monitor, secuencia8);
			    Tarea nucleo2 = new Tarea(monitor, secuencia6);
			  
			    h2 = new Thread(origen2);
			    h4 = new Thread(cpu2);
			    h6 = new Thread(nucleo2);
			    h8 = new Thread(cpu22);
			  
			    h2.start();
			    h4.start();
			    h6.start();
			    h8.start();

		    }	
		    
		    Tarea origen1 = new Tarea(monitor, secuencia1);
		    Tarea cpu1 = new Tarea(monitor, secuencia3);
		    Tarea cpu11 = new Tarea(monitor, secuencia7);
		    Tarea nucleo1 = new Tarea(monitor, secuencia5);
		
		    h1 = new Thread(origen1);
		    h3 = new Thread(cpu1);
		    h5 = new Thread(nucleo1);
		    h7 = new Thread(cpu11);
		
		    h1.start();
		    h3.start();
		    h5.start();
		    h7.start();
	}//constructor

	
	public void Interrumpir()
	{
		 if (! solo_h1)
		    {
			
			 h2.interrupt();
			 h4.interrupt();
			 h6.interrupt();
			 h8.interrupt();
		    }	
		
		 h1.interrupt();
		 h3.interrupt();
	
		 h5.interrupt();
		 h7.interrupt();
		 
		
	}
	

}//CLASE