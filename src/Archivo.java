import java.io.*;
import java.util.ArrayList;

public class Archivo {

    private FileWriter archivo = null;
    private PrintWriter escritor = null;

    public Archivo() {

        try
        {
            archivo=new FileWriter("C:\\Users\\alejandro\\Desktop\\log.txt");

            escritor=new PrintWriter(archivo);

        } catch(Exception e) {     //Si existe un problema cae aqui

            System.out.println("Error al escribir: "+ e.getMessage());
        }
    }

    public void escribirArchivo(String datos) throws IOException {
        try {
            escritor.println(datos);
            escritor.flush();

        }catch(Exception e){
            throw new IOException("Error al escribir: "+ e.getMessage());
        }
    }

    public static int[][] leerMatriz(String pathname) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        int[][] mat2 = new int[0][];

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File(pathname);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            //System.out.println("Leyendo el contendio del archivo.txt");
            String linea;
            ArrayList<String> lineas = new ArrayList<>();
            while ((linea = br.readLine()) != null) {
               // System.out.println(linea + "         ");
                lineas.add(linea);
            }
            //System.out.println(lineas.size()+"\n"+lineas.get(0).length()+"\n");
            String[] aux;
            aux = (lineas.get(0).split("\t"));
            mat2 = new int[lineas.size()][aux.length];
            for (int i = 0; i < mat2.length; i++) {
                String[] a;
                a = (lineas.get(i).split("\t"));
                for (int j = 0; j < mat2[0].length; j++) {
                    mat2[i][j] = Integer.parseInt(a[j]);
                    //System.out.print(" " + (mat2[i][j]));
                }
                //System.out.println("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mat2;
    }

}