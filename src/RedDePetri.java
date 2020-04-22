import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RedDePetri{
    private int[] marcaActual;
    private int[][] Imenos;
    private int[][] Imas;

    private int[][] H;
    private int[] Ext;
    private int[] E;
    private int[] Eaux;
    private int[] B;
    private int[] Q;
    private int[] sensibilizadas;
    //15 T
    //16 P

    private Operaciones op;
    private Archivo archivo;

    private int[][] PInvariantes = {{0,3,1},  //cada fila es{p0,p1,...,pk,r} tal que -> m(p0)+m(p1)+...+m(pk) = r
                                    {4,5,1},
                                    {13,14,1},
                                    {2,7,8,1},
                                    {9,12,11,1}};

    public RedDePetri(int[] marcaInicial, int[][] Imenos, int[][] Imas, int[][] H, Archivo archivo) {

        this.marcaActual = marcaInicial;
        this.Imenos = Imenos;
        this.Imas = Imas;
        this.op = new Operaciones();
        this.H = op.traspuesta(H); //ya la guarda como traspuesta para agilizar los calculos
        this.archivo = archivo;
        crearExtendida();
    }
    
    public int getCantidadTransiciones()
    { 
    	return Imenos[0].length;
    } // cant transiciones == cant columnas
    
    public int getCantidadPlaza()
    { 
    	return Imenos.length;
    } // cant transiciones == cant columnas

    private void crearExtendida() { //crea  Q, Z, E, B y el Ex, todos los vectores para ver si esta sensibilizada
        this.Q = op.and(marcaActual,marcaActual); // vector de marcas binario
        crearE();
        crearB();
        actualizarExt();
    }

    private void actualizarExt() { //Vector extendido, crea y actualiza. E and B and Z
        actualizarE();
        actualizarB();
        this.Ext =op.and(E,B);
        printArchivo(E,"E");
        printArchivo(B,"B");
        printArchivo(Ext,"Ext");
    }

    private void crearE() { //Crea y actualiza el vector de sensibilizadas
        this.E = new int[Imenos[0].length];
        this.Eaux = new int[E.length];
        for (int i = 0; i < Eaux.length ; i++) {
            Eaux[i] = 0;
        }
        actualizarE();
    }

    private void actualizarE() {
        for (int i = 0; i < E.length ; i++) {
            Eaux[i] = E[i];
            this.E[i] = (sensibilizadaComun(i) ? 1 : 0); //actualiza vector de sensibilizadas, 1 si esta sensibilizada la transicion
                                                         //y 0 si no esta sensibilizada
        }
    }

    private boolean sensibilizadaComun(int i){ //marcaActual-(Imenos*Sigma) para comprobar si la transicion saca tokens y hay toquens en la plaza
        int[] mprueba = op.sumar(marcaActual, op.multiplicarXEscalar(op.multiplicar(Imenos, generarVectorDisparo(i)), -1));
        for(int u = 0 ; u < mprueba.length; u++){
            if(mprueba[u] < 0) return false;
        }
        return true;
    }

    private void crearB() { //Vector de transiciones des-sensibilizadas por arco inhibidor
        this.B = new int[H.length];
        actualizarB();
    }

    private void actualizarB() { //
        this.Q = op.and(marcaActual,marcaActual); // vector de marcas binario
        this.B = op.complementar(op.multiplicar(H,Q));  //B = not(H' * Q)
    }

    public int getCantTransiciones(){
        return Imenos[0].length;
    }

    public boolean esSensibilizada(int transicion){     //le pregunta a la transicion con tal subindice(ID) si esta sensibilizada
        return (Ext[transicion]!=0);
    }

    public int[] getMarcaActual() {
        return marcaActual;
    }

    public int[] getSensibilizadas(){      //devuelve una lista de todas las transiciones sensibilizadas(comunes y temporales)

    	sensibilizadas = new int[E.length];
    	
        for(int i = 0; i < E.length; i++) {
            if(esSensibilizada(i)) sensibilizadas[i] =1;
            else sensibilizadas[i] =0;
        }
        return sensibilizadas;
    }

    private boolean seCumplenPInvariantes() {
        for(int i = 0; i < PInvariantes.length; i++){
            int suma = 0;
            for(int j = 0; j < PInvariantes[i].length ; j++){
                if(j < PInvariantes[i].length - 1) {
                    int indiceDePlaza = PInvariantes[i][j];
                    suma += marcaActual[indiceDePlaza];
                }
                else if(j == PInvariantes[i].length -1){
                    if(suma != PInvariantes[i][j]) return false;
                }
            }
        }
        return true;
    }

    private int[] generarVectorDisparo(int transicion){  //A partir del nro de transicion (ID) se genera un vector de disparo con todos ceros menos la transicion a disparar
        int[] vectorDisparo = new int[Imenos[0].length];
        for(int i=0; i<Imenos[0].length; i++){
            vectorDisparo[i]=0;         //reseteo el vector con todos ceros
        }
        vectorDisparo[transicion]=1;        //tiene 1 solo en la transicion que se desea disparar
        return vectorDisparo;
    }
    public void imprimir(int[] a, String name){
        System.out.println(name+": ");
        for(int i=0; i< a.length;i++){
            System.out.print(a[i]+" ");
        }
        System.out.println();
        System.out.println("---------------------");
    }


    public boolean disparar(int transicion) throws IllegalDisparoException {  //dispara una transicion modificando la marcaActual con la ec. generalizada
        boolean k;
        if(esSensibilizada(transicion)){      //verifica si la transicion esta sensibilizada a partir de Ext
            int[] disparoSensibilizado = generarVectorDisparo(transicion); //sigma
         //   imprimir(disparoSensibilizado, "Sigma");
         //   imprimir(marcaActual, "Mj");
            printArchivo(marcaActual,"Mj");
            marcaActual = op.sumar(marcaActual, op.multiplicarXEscalar(op.multiplicar(Imenos, disparoSensibilizado),-1)); //Ecuacion de estado generalizada: Mj+1 = Mj - Imenos*(sigma and Ex)
         //   imprimir(marcaActual, "Mj+1");
            marcaActual = op.sumar(marcaActual, op.multiplicar(Imas, disparoSensibilizado)); //Ecuacion de estado generalizada: Mj+2 = Mj+1 + Imas*(sigma and Ex)
         //   imprimir(marcaActual, "Mj+2");
            printArchivo(marcaActual,"Mj+2");

            actualizarExt();

            if(!seCumplenPInvariantes()){
                System.out.println("\n\n ERROR NO SE CUMPLEN P INVARIANTES! \n");
                try {
                    archivo.escribirArchivo("ERROR, NO SE CUMPLEN LOS P INVARIANTES");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        k = true;
        }else{
            k = false;
            //throw new IllegalDisparoException();
        }
        return k;
    }

    public void imprimir(int[][] a, String name){
        System.out.print(name+": ");
        for(int i=0; i< a.length;i++){
            imprimir(a[i], "");
        }
        System.out.println();
        System.out.println("---------------------\n");
    }

    private void printArchivo(int[] a, String name){
       // String cadena = name + ": ";
        String cadena = "";
        for (int n = 0; n < a.length; n++) {
                cadena += a[n] + " ";
        }
        cadena +=  " :" + name;
            try {

                archivo.escribirArchivo(cadena);   //escribe en el archivo log

            } catch (IOException ex) {
                System.out.println(ex.getMessage());

            }
        }

    public void printArchivo(int a, String name){
         String cadena = name + ": " + a;
        try {

            archivo.escribirArchivo(cadena);   //escribe en el archivo log

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}