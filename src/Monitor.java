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
    private int cantidad = 0;
    // private String disparosRealizados;
    // private ArrayList<Integer> disparos;
    // private ArrayList<Integer> vSensibilizadas;
    private ArrayList<Integer> vSensibilizadas;

    private int[] vvSensibilizadas;
    private int[] vColas;
    private int[] m;
    private boolean k;
    private boolean test_on = true;

    
    
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
        int seleccionado = 0;
        int auxIndice = 0;
        try {
            mutex.acquire();
            k = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        while (k) {
            //k = RdP.disparar(transicion);
            if(RdP.esTemporizada(transicion))
            {
                dispararTemporizada(transicion);
            }
            else
            {
        
                k = RdP.esSensibilizada(transicion) && RdP.disparar(transicion);
                if(test_on) System.out.println("Intento T: " + transicion +  " " + leyendas(transicion));
                if (k) 
                {//disparo
            	    if(test_on)	System.out.println("Disparada: " + transicion + " " + leyendas(transicion));
	                vSensibilizadas = RdP.getSensibilizadas();
	                vvSensibilizadas = new int[RdP.getCantTransiciones()];
	                   for ( Integer orden :  vSensibilizadas )
	                   {
	                	   vvSensibilizadas[orden] = 1;
	                   }
	                vColas = new int[RdP.getCantTransiciones()];
	                m = new int[RdP.getCantTransiciones()];
	                
                    for (int i = 0; i < VariablesDeCondicion.length; i++) 
                    {
	    	            if (this.VariablesDeCondicion[i].Empty()) {
	    	                vColas[i] = 0;
	    	            } else {
	    	                vColas[i] = 1;
	    	            }
	    	
	    	        }
	                
                    for (int i = 0; i < vvSensibilizadas.length; i++) 
                    {
	                  	if ((vvSensibilizadas[i] == 1) && (vColas[i] == 1)) {
	                  		m[i] = 1;
	                        cont++;
	                        auxIndice = i;
	                    } else {
	                        m[i] = 0;
	                    }
	                }
	
	                if(test_on) imprimir_vector(vvSensibilizadas,"vvSensibilizadas ");
	                if(test_on) imprimir_vector(vColas,          "vColas           ");
	                if(test_on) imprimir_vector( m,              " m               ");
	                if(test_on) System.out.println("");
	                if(test_on) if(cont > 0) { System.out.println("cont: " + cont);}
	                
	                if (cont == 0)
	                    k = false;
	
                    if (cont == 1) 
                    {
	                	if(test_on) System.out.print( "UP --> "+  auxIndice);
	                    VariablesDeCondicion[ auxIndice].Resume();
                        return;
                    }
	                
                    if (cont > 1) 
                    {
	                    seleccionado = politica.cual(m);
	                    //no concidera el caso de las T que no alimentan los buffers
	                    //en que momento se usan las prioridadeS??
	                    VariablesDeCondicion[seleccionado].Resume();
	                	if(test_on) System.out.print( "UP sel --> "+ seleccionado);
	                	return;
	                }
	
	                cont = 0;
	                    
            } // if SI disparop
            else
            {// No disparo
            	mutex.release(); // devuelve mutex
                VariablesDeCondicion[transicion].Delay();

            }//else
        }
        
        }//while
        mutex.release(); // devuelve mutex
        actualizarCondiciones(transicion);

    }

    private void dispararTemporizada(int transicion) throws IllegalDisparoException {
        int cont = 0;
        int auxIndice = 0;
        int seleccionado = 0;
        TransicionConTiempo t = RdP.getConTiempo(transicion);
        if(t.estaAdentroDeVentana())
        {
            RdP.disparar(transicion);
        }
            else if(t.estaAntes()){
    
                mutex.release();

                vSensibilizadas = RdP.getSensibilizadas();
                vvSensibilizadas = new int[RdP.getCantTransiciones()];
                   for ( Integer orden :  vSensibilizadas )
                   {
                       vvSensibilizadas[orden] = 1;
                   }
                vColas = new int[RdP.getCantTransiciones()];
                m = new int[RdP.getCantTransiciones()];
                
                for (int i = 0; i < VariablesDeCondicion.length; i++) 
                {
                    if (this.VariablesDeCondicion[i].Empty()) {
                        vColas[i] = 0;
                    } else {
                        vColas[i] = 1;
                    }
        
                }
                
                for (int i = 0; i < vvSensibilizadas.length; i++) 
                {
                      if ((vvSensibilizadas[i] == 1) && (vColas[i] == 1)) {
                          m[i] = 1;
                        cont++;
                        auxIndice = i;
                    } else {
                        m[i] = 0;
                    }
                }

                if (cont == 1) 
                {
                    if(test_on) System.out.print( "UP --> "+  auxIndice);
                    VariablesDeCondicion[ auxIndice].Resume();
                    return;
                }
                
                if (cont > 1) 
                {
                    seleccionado = politica.cual(m);
                    //no concidera el caso de las T que no alimentan los buffers
                    //en que momento se usan las prioridadeS??
                    VariablesDeCondicion[seleccionado].Resume();
                    if(test_on) System.out.print( "UP sel --> "+ seleccionado);
                    return;
                }

                cont = 0;

                try {
                    Thread.sleep(t.cuantoDormir()); //duerme lo necesario para que al despertarse pueda disparar la temporizada
                    mutex.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RdP.disparar(transicion); //llamada recursiva, en la mas interna deberia de entrar en el primer if y disparar la transicion
            }
            else throw new IllegalDisparoException("llego despues no se va a disparar nunca");
            //else{
            //mutex.release(); // devuelve mutex
            //actualizarCondiciones(transicion);
            //return;
        //}
        mutex.release(); // devuelve mutex
        actualizarCondiciones(transicion);
        }
    
    private void imprimir_vector( int[] v, String nombre)
    {
    	System.out.print( " " + nombre + ": [");
    	
	    	int suma = 0;
    	for( int i = 0; i< v.length; i++ )
			{
				 suma = suma +v[i];   
			}
	    	
    	if( suma > 0) {
    		for( int i = 0; i< v.length; i++ )
    		{
    			if( v[i] == 0 ) {
	    			if( i == (v.length - 1) )		System.out.println( v[i] + "] ");
	    			else 	System.out.print( v[i] + ", ");
    			}else
    			{
    				if( i == (v.length - 1) )		System.out.println( "X"+ "] ");
	    			else 	System.out.print("X"+ ", ");
    			}
    		}
    	}else
    	{
    		System.out.println(" nulo");
    	}
    }
    
    private String leyendas(int transicion)
    {
        String detale;

        switch(transicion)
        {
        case 0:		detale = " tarea 1/2 A"; break;
        case 4:		detale = " tarea 2/2 A"; break; 
        
        case 3: 	detale = " Nucleo 1/2 A"; break;
        case 5: 	detale = " Nucleo 2/2 A"; break;
        
        case 7: 	detale = " CPU 1/3 A"; break;
        case 2: 	detale = " CPU 2/3 A"; break;
        case 1: 	detale = " CPU 3/3 A"; break;
        
        case 6: 	detale = " KeepLive 1 A"; break;
        
        case 14:	detale = " tarea 2/2 B"; break; 
        
        case 13: 	detale = " Nucleo 1/2 B"; break;
        case 12: 	detale = " Nucleo 2/2 B"; break;
        
        case 11: 	detale = " CPU 1/3 B"; break;
        case 9: 	detale = " CPU 2/3 B"; break;
        case 8: 	detale = " CPU 3/3 B"; break;
        
        case 10: 	detale = " KeepLive B"; break;
        
        
        default:    detale = "nose";break;
        
        }//case
        
        return detale;

    }
    

    private void GenerarVarCond() { // crea tantas variables de condicion como cantidad de transiciones tiene la red
                                    // de petri
        for (int i = 0; i < this.VariablesDeCondicion.length; i++) {
            this.VariablesDeCondicion[i] = new Cola(this.mutex);
        }
    }

   // private int[] quienesEstan() {
     private void quienesEstan( int[] Colas) {
	    
	    for (int i = 0; i < VariablesDeCondicion.length; i++) {
	            if (this.VariablesDeCondicion[i].Empty()) {
	                Colas[i] = 0;
	            } else {
	                Colas[i] = 1;
	            }
	
	        }
      //  return vColas;
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
            System.out.println("*Tareas completadas en N1: " + finalN1);
            System.out.println("*Tareas completadas en N2: " + finalN2);
            RdP.printArchivo(finalN1, "Tareas completadas en N1");
            RdP.printArchivo(finalN2, "Tareas completadas en N2");
        }

    }
}