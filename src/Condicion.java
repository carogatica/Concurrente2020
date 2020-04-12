public class Condicion {
    private boolean condicion;

    public Condicion(boolean condicion) {
        this.condicion = condicion;
    }

    public void setCondicion(boolean condicion) {
        this.condicion = condicion;
        if(condicion){
           h1.finalizar();
           h2.finalizar();
           h3.finalizar();
           h4.finalizar();
           h5.finalizar();
           h6.finalizar();
           h7.finalizar();
           h8.finalizar();
        }
    }
    public boolean getCondicion(){
        return condicion;
    }

}

