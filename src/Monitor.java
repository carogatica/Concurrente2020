import java.util.ArrayList;
//import java.util

import java.util.concurrent.Semaphore;

public class Monitor{
    //nada
    private Semaphore mutex=new Semaphore(1);  //semaforo binario de exclusion mutua
    private Cola[] VariablesDeCondicion;  //condiciones de sincronizacion de cada transicion
    private RedDePetri RdP;     //red que controla la logica del sistema
    private Politicas politica; //politicas que resuelven los conflictos de la red
    private Condicion condicionDeFinalizacion;
    //private int finalN2;
    //private int finalN1;
    private final int CANT_TAREAS = 30;
    private int disparosRealizados;

    // "static" --> unico de la clase
    // "final" --> que no se puede modificar? se vuelve una constante? una vez instanciada, se vuelve cte
    // que indica que a esa variable solo se le puede asignar un valor u objeto una única vez.
    private ArrayList<Integer> disparos;
    //private ArrayList<Integer> vSensibilizadas;
    private int[] vSensibilizadas;

    

    private int[] vColas;
    private int[] m;
    private boolean k;

    public Monitor(RedDePetri red, Politicas politicas,Condicion condicion){
        this.RdP = red;
        this.VariablesDeCondicion = new Cola[RdP.getCantTransiciones()];  //se generan tantas variables de condicion como transiciones haya en la RdP
        this.politica = politicas; 
        GenerarVarCond();
        this.condicionDeFinalizacion = condicion;
        this.disparosRealizados = 0;
        this.disparos = new ArrayList<Integer>();
        //this.finalN1 = 0;
        //this.finalN2 = 0;
    }

    public void disparar(int transicion) throws IllegalDisparoException {


        debug_imp_acciones(transicion);
    	
    	
    		int cont = 0;
            int seleccionado = 0;

            try {
                mutex.acquire();
                k = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(k == true){
            	
            	disparos.add(transicion);

                k = RdP.disparar(transicion);

                if(k){
                	System.out.println(" disparada= " + transicion);

                	disparosRealizados++;
                	
                	
                    vSensibilizadas = RdP.getSensibilizadas();
                    vColas = new int[RdP.getCantTransiciones()]; 
                    m = new int[RdP.getCantTransiciones()]; 

                    vColas = quienesEstan();
                    
                    
               debug_imprimir_vector( vSensibilizadas, "vSensibilizadas");
               debug_imprimir_vector( vColas,          "vColas         ");

                	
                	
                    for (int i=0; i < vSensibilizadas.length; i++){
                        if(vSensibilizadas[i] == 1 && vColas[i] == 1){
                            m[i] = 1;
                            cont++;
                          //  auxIndice = i;
                        } 
                        else
                        	{m[i] = 0;}
                    }
                    
                debug_imprimir_vector( m,               "m              ");

                    if(cont == 0)
                    	{	     	System.out.println(" NADIE");	
                    			k = false;
                    	}


                    if(cont == 1)          	
                    {
                    	 for (int i=0; i < m.length; i++)
	                     	{
	                    		if ( m[i] != 0 ) seleccionado = i;
	                     	}
                     	System.out.println("DESPERTO T:" + seleccionado);

                    	VariablesDeCondicion[seleccionado].Resume();
                    	
                	    evaluar_condicion();

                    	return;
                	}
                
                    if(cont > 1)
                    {
                        seleccionado = politica.cual(m);
                    	System.out.println("DESPERTO xPol T:" + seleccionado);

                        VariablesDeCondicion[seleccionado].Resume();
                       

                        return;

                    }
                    
                    cont = 0;
                }
                else{
                	
                	evaluar_condicion();
                    mutex.release();
                	System.out.println("duerme T:" + transicion);

                    VariablesDeCondicion[transicion].Delay();
                }
            }
            
            evaluar_condicion();
            mutex.release();        //devuelve mutex
        
    	    

    }
    
    private void debug_imprimir_vector( int[] V, String leyenda)
    {
    	//System.out.println("");
        for (int i=0; i < V.length; i++)
        	{
            	//"("+ i +")" + 
        		if( i==0 )System.out.print( leyenda +  V[i] + ",");
        		else if( i==4 )System.out.print( "/" + V[i]+ ",");
        		else if( i==8 )System.out.print( "/" + V[i]+ ",");
        		else if( i==12 )System.out.print( "/" + V[i]+ ",");

        		else	System.out.print( V[i] + "," );

        	}
    	 System.out.println("");

    }
    
    private void debug_imp_acciones( int T)
    {
    	if(T == 0 || T==4) System.out.println(" T:" + T + " de [ 0, 4 ] generacion tarea ");
    			
    	if(T == 7 || T==2 || T==1) System.out.println(" T:" + T + " de [ 7, 2, 1 ] on/off ");

    	if(T == 5 || T==3) System.out.println(" T:" + T + " de [ 5, 3 ] nucleo ");

    	if(T == 6) System.out.println(" T:" + T + " de [ 6 ]  ");

    }
    
    
    private void GenerarVarCond(){ //crea tantas variables de condicion como cantidad de transiciones tiene la red de petri
        for(int i = 0; i < this.VariablesDeCondicion.length; i++){
            this.VariablesDeCondicion[i] = new Cola();
        }
    }
    
    private int[]  quienesEstan(){ 
        for(int i = 0; i < VariablesDeCondicion.length; i++){ 
            if( this.VariablesDeCondicion[i].Empty() )
            	{vColas[i] = 0; 	}
            else 
            	{vColas[i] = 1;}
            
        }
        return vColas;
    }
    
    
    private void evaluar_condicion()
    
    {
    	if( disparosRealizados == 100 || disparosRealizados == 200 || disparosRealizados == 300 || disparosRealizados == 400 || disparosRealizados == 600 )
    		{
    		System.out.println( " disparosRealizados ------>      " +  disparosRealizados);
    		}

    	
    	 if(  CANT_TAREAS < disparosRealizados)
    	   {    		 
     		System.out.println( " disparosRealizados ------>      " +  disparosRealizados);
   		   condicionDeFinalizacion.setCondicion(true);    		  
    	   }
    	
    }
    
    public ArrayList<Integer> getDisparos()
    {
    	return this.disparos;
    	
    }
    
    
}
