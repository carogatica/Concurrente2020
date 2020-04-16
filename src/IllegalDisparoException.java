public class IllegalDisparoException extends Exception {
    public IllegalDisparoException(){
        super("No se puede disparar");
    }

    public IllegalDisparoException(String s) {
        super(s);
    }
}
