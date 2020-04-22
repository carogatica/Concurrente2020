
/*
 * 

// 1) seteo de prioridades
//que prioridad tiene cada transicion?? ---> iguales prioridad --> random

//eleccion de T

//eleccion de buffer
// elegit buffer para mantener cargas equitativas  --> elige el buffer qye tenga
//menos token, y si tienen igual cantidad, eleccion random


 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class Politicas {
    private HashMap<Integer, Integer> prioridades;
    private RedDePetri RdP;
    int posicionTransicionBuffer1;
    int posicionTransicionBuffer2;
    int posicionPlazaBuffer1;
    int posicionPlazaBuffer2;

    public Politicas(RedDePetri RdP, int posicionTransicionBuffer1, int posicionTransicionBuffer2, int posicionPlazaBuffer1, int posicionPlazaBuffer2) {
        this.prioridades = new HashMap<>();
        this.RdP = RdP;
        this.posicionTransicionBuffer1 = posicionTransicionBuffer1;
        this.posicionTransicionBuffer2 = posicionTransicionBuffer2;
        this.posicionPlazaBuffer1 = posicionTransicionBuffer1;
        this.posicionPlazaBuffer2 = posicionTransicionBuffer2;



    }

    public void addPrioridad(int transicion, int prioridad){
        this.prioridades.put(transicion, prioridad);
    }

    public void loteriaTransicion()
    {
    	
    }
    
    public int cual( int[] M) //devuelve la transicion de mayor prioridad entre las sensibilizadas
    {
    	int eleccion [] = new int[RdP.getCantidadTransiciones()];
    	int ceros [] = new int[RdP.getCantidadTransiciones()];
    	int decision = 0;
    	
    	 Random randomno = new Random();

         // check next int value  
       //  System.out.println("Next int value: " + randomno.nextInt(10000));
    	 /*
    	
    	 for(int u = 0 ; u < eleccion.length; u++){
            eleccion[u] = 0;
            ceros[u] = 0;
         }//for
         */
    	 
    	 int numero_al;
    	 boolean done=true;
    	 
    	 while ( done == true)
    	 {
    		 numero_al = randomno.nextInt( RdP.getCantidadTransiciones() );    		 
    			
    		// System.out.println(" numero_al: " + numero_al);
    		 
        	 for(int u = 0 ; u < eleccion.length; u++)
        	 {
	              if ( u == numero_al)        		 {	 
	        			 eleccion[u]=1;	 break;
	        		 }
        	 }//for              
              
              
              for(int uu = 0 ; uu < eleccion.length; uu++){                 
            	  eleccion[uu] = eleccion[uu] * M[uu] ;                  
               }//for   
              
              for(int i = 0 ; i < eleccion.length; i++)
         	 {
 	              if ( eleccion[i]==1) 
 	            	  {
 	            	  decision = i;
 	            	  done=false;	 
 	            	  }
 	        		 
         	 }//for 
              
    	  }//while
    	 
    	 int[] marca = new int[ RdP.getCantidadPlaza() ]; // 16 plazas
     	 
    	 if( eleccion[posicionTransicionBuffer2]==1 || eleccion[posicionTransicionBuffer1]==1 )
    	 {
    		 if (  marca [posicionPlazaBuffer2] < marca [posicionPlazaBuffer1] ) {
    			 decision = posicionTransicionBuffer2;
    		 }
    		 else {
    			 decision = posicionTransicionBuffer1;
    		 }    		 
    	 }
    	 
    	 
    	return decision;
    	
    }//cual
    
    
}//class



    
    
    