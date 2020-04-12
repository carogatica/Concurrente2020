
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
        this.posicionTransicionBuffer1 = posicionTransicionBuffer2;


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

    	
    	 for(int u = 0 ; u < eleccion.length; u++){
            eleccion[u] = 0;
            ceros[u] = 0;
         }//for
    	 
    	 int numero_al;
    	 
    	 while ( eleccion == ceros)
    	 {
    		 numero_al = randomno.nextInt( RdP.getCantidadTransiciones() );    		 
    			
        	 for(int u = 0 ; u < eleccion.length; u++){
              if ( u == numero_al)        		 {	 
        			 eleccion[u]=1;	 break;
        		 }
        	 }//for              
              
              
              for(int uu = 0 ; uu < eleccion.length; uu++){                 
            	  eleccion[uu] = eleccion[uu] * M[uu] ;                  
               }//for    
              
    	  }//while
    	 
    	 int[] marca = new int[ RdP.getCantidadPlaza() ];
     	 
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

    	/*int eleccion = sensibilizadas.get(0);
        for (int transicion : sensibilizadas) {
            if (prioridades.get(transicion) > prioridades.get(eleccion)) {
                eleccion = transicion;
            }
        }
        if(eleccion==4 || eleccion==14){ //si se elige la T4 y T14 se decide entre una u otra de acuerdo a la cantidad de tokens..
            return resolverConflictoBuffer(RdP.getMarcaActual()); //...en CPU_buffer1 y CPU_buffer2
        }
        else if(eleccion == 1 || eleccion==6){
            //return resolverConflictoCPU1(RdP.getMarcaActual());
            if(Math.random()>0.5){
                return 6;
            }else return 1;
        }
        else if(eleccion == 8 || eleccion==10){
            //return resolverConflictoCPU2(RdP.getMarcaActual());
            if(Math.random()>0.5){
                return 10;
            }else return 8;
        }
        else{
            return eleccion;            //sino se retorna la otra transicion elegida
        }
        */
    /*

    private int resolverConflictoCPU1(int[] marcaActual) {//elige entre T6 y T1 segun el marcado de P6
        if(marcaActual[6]>0) return 6;
        else return 1;
    }
    private int resolverConflictoCPU2(int[] marcaActual) {//elige entre T10 y T8 segun el marcado de P10
        if(marcaActual[10]>0) return 10;
        else return 8;
    }

    private int resolverConflictoBuffer(int[] marcaActual){   //resuelve conflicto entre T4 y T14 para mantener iguales la cantidad de tokens en c/u de los dos buffers
        if(marcaActual[1]<marcaActual[15]){                   //marcado[1] = CPU_buffer1 es alimentado por T4
            return 4;                                         //marcado[15] = CPU_buffer2 es alimentado por T14
        }else if(marcaActual[1]>marcaActual[15]){
            return 14;
        }else if(Math.random()<0.5){        //si llegan a ser iguales se resuleve mediante un nro aleatorio
            return 4;
        }else return 14;
    }
    */
    

    
    
    