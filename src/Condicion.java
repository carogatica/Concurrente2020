public class Condicion {
    private boolean condicion;

    public Condicion(boolean condicion) {
        this.condicion = condicion;
    }

    // nada
    public void setCondicion(boolean condicion) {
        this.condicion = condicion;
        if (condicion) {
            Tarea.finalizar();
        }
    }

    public boolean getCondicion() {
        return condicion;
    }

}
