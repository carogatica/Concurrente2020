public class Operaciones {

    public Operaciones() {
    }

    public int[][] and(int[][] a, int[][] b) {
        if (a == null || b == null) throw new NullPointerException("matriz nula!");
        else if (a.length != b.length || a[0].length != b[0].length)
            throw new IndexOutOfBoundsException("diferente formato operacion imposible!");

        int[][] resultado = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            resultado[i] = and(a[i], b[i]);
        }
        return resultado;
    }

    public int[] and(int[] a, int[] b) {
        if (a == null || b == null) throw new NullPointerException("vector nulo!");
        else if (a.length != b.length) throw new IndexOutOfBoundsException("diferente formato operacion imposible!");

        int[] resultado = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            resultado[i] = (a[i] != 0 && b[i] != 0) ? 1 : 0;
        }
        return resultado;
    }

    public int multiplicar(int[] a, int[] b) {
        if (a == null || b == null) throw new NullPointerException("vector nulo!");
        else if (a.length != b.length) throw new IndexOutOfBoundsException("diferente formato operacion imposible!");

        int resultado = 0;
        for (int i = 0; i < a.length; i++) {
            resultado += a[i] * b[i];
        }
        return resultado;
    }

    public int[][] multiplicar(int[][] a, int[][] b) { //dim(a)=nxm dim(b)=mxk dim(r)=nxk
        if (a == null || b == null) throw new NullPointerException("matriz nula!");
        if (a[0].length != b.length) throw new IndexOutOfBoundsException("diferente formato operacion imposible!");

        int[][] resultado = new int[a.length][b[0].length];
        int[][] bt = traspuesta(b);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < bt.length; j++) {
                resultado[i][j] = multiplicar(a[i], bt[j]);
            }
        }
        return resultado;
    }

    public int[] multiplicar(int[][] a, int[] b) { //dim(a)=nxm dim(b)=mx1 dim(r)=nx1
        int[][] bc = traspuesta(b);
        return toVector(multiplicar(a, bc));
    }


    public int[][] traspuesta(int[][] mat) {
        if (mat == null) throw new NullPointerException("matriz nula!");
        int[][] traspuesta = new int[mat[0].length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                traspuesta[j][i] = mat[i][j];
            }
        }
        return traspuesta;
    }

    public int[][] traspuesta(int[] vect) { //dim(vect) = n -> dim(r) = nx1
        if (vect == null) throw new NullPointerException("vector nulo!");
        int[][] traspuesta = new int[vect.length][1];
        for (int i = 0; i < vect.length; i++) {
            traspuesta[i][0] = vect[i];
        }
        return traspuesta;
    }

    public int[] toVector(int[][] mat) {
    if(mat == null)throw new NullPointerException("matriz nula!");
    int[] vect;
    if(mat.length == 1){ //es matriz fila
        vect = new int[mat[0].length];
        for(int i = 0; i < mat[0].length; i++) {
            vect[i] = mat[0][i];
        }
    }
    else if (mat[0].length == 1){ // es matriz columna
        vect = new int[mat.length];
        for(int i = 0; i < mat.length; i++) {
            vect[i] = mat[i][0];
        }
    }
    else {throw new IndexOutOfBoundsException("diferente formato operacion imposible!");}

    return vect;
    }

    public int[] sumar(int[] a, int[] b){
        if(a == null || b == null)throw new NullPointerException("vector nulo!");
        if (a.length != b.length) throw new IndexOutOfBoundsException("diferente formato operacion imposible!");
        int[] resultado = new int[a.length];
        for(int i=0; i < a.length; i++){
            resultado[i] = a[i] + b[i];
        }
        return resultado;
    }

    public int[] multiplicarXEscalar(int[] a, int e){
        if(a == null)throw new NullPointerException("vector nulo!");
        int[] resultado = new int[a.length];
        for(int i=0; i < a.length; i++){
            resultado[i] = a[i] * e;
        }
        return resultado;
    }


    public int[] complementar(int[] a){
        if(a == null)throw new NullPointerException("vector nulo!");
        int[] complemento = new int[a.length];
        for(int i=0; i < a.length; i++){
            if(a[i]==0){
                complemento[i]=1;
            }else complemento[i]=0;
        }
        return complemento;
    }

}