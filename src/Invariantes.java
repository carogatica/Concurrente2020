import java.util.ArrayList;

public class Invariantes {

	
	private ArrayList<Integer> disparos;
	
	
	public Invariantes ( ArrayList<Integer> disparos )
	{
		this.disparos = disparos;
		//verificarTinvariantes();
		
	}
	

	public void verificarTinvariantes(){
        ArrayList<Integer> invariante = new ArrayList<>();  //contiene la secuencia de transiciones que cumple con un T-invariante
        invariante.add(0);
        invariante.add(4);
        invariante.add(7);
        invariante.add(2);
        invariante.add(5);
        invariante.add(3);
        invariante.add(1);
        sacarTodasLasTransiciones(disparos,invariante);
        invariante.clear();

        invariante.add(0);
        invariante.add(14);
        invariante.add(11);
        invariante.add(9);
        invariante.add(13);
        invariante.add(12);
        invariante.add(8);
        sacarTodasLasTransiciones(disparos,invariante);
        invariante.clear();

        invariante.add(0);
        invariante.add(4);
        invariante.add(6);
        invariante.add(5);
        invariante.add(3);
        sacarTodasLasTransiciones(disparos,invariante);
        invariante.clear();

        invariante.add(0);
        invariante.add(14);
        invariante.add(10);
        invariante.add(13);
        invariante.add(12);
        sacarTodasLasTransiciones(disparos,invariante);
        invariante.clear();

        //return disparos;
    }

    //A partir de todas las transiciones disparadas y de un t-invariante, se mira cuantas veces esta ese t-invariante en el arreglo de transiciones
    private void sacarTodasLasTransiciones(ArrayList<Integer> disparos, ArrayList<Integer> invariante){
        boolean inv = false;
        do{
            inv = invariante(disparos, invariante);
            System.out.println("do while: "+inv);
            System.out.println("disparos1: "+disparos.size());
        }while(inv);

    }
    private boolean invariante(ArrayList<Integer> disparos,  ArrayList<Integer> invariante){
        ArrayList<Integer> valoresAsacar = new ArrayList<>();   //guarda el indice(del arreglo "disparos") de la transicion a sacar
        int valor;
        valor = buscarTransicion(0,invariante.get(0),disparos);
        if(valor < 0){
            return false;
        }
        valoresAsacar.add(valor); //se fija para la primer transicion

        for(int i=1; i < invariante.size(); i++){
            valor = buscarTransicion(valoresAsacar.get(i-1)+1,invariante.get(i),disparos);  //arranca de la posicion siguiente al ultimo numero sacado
            if(valor < 0){
                return false;
            }
            valoresAsacar.add(valor);
        }
        System.out.println("valoresAsacar: "+valoresAsacar.size());
       /*
        for(Integer recorrer: valoresAsacar){
            disparos.remove(recorrer.intValue());
        }*/


        for(int i=0; i < valoresAsacar.size(); i++){
            disparos.remove(valoresAsacar.get(i).intValue()-i);
        }

        System.out.println("disparos2: "+disparos.size());
        return true;
    }

    private int buscarTransicion(int inicio, int transicion, ArrayList<Integer> disparos){ //busca la transicion indicada en el arreglo de transiciones que se dispararon
        for(int i=inicio; i<disparos.size(); i++){              //devuelve la posicion en el arreglo en la que se encuentra dicha transicion
            if(disparos.get(i)==transicion){
               // System.out.println(disparos.get(i)+" es igual a "+transicion);
                return i;
            }
           // System.out.println(disparos.get(i)+" es distinto a "+transicion);
        }
        return -1; //no se encontro igualdad
    }
	
}
