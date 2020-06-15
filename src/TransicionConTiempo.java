import java.util.Calendar;

public class TransicionConTiempo{
    private int ID;
    private long limiteInf;
    private long limiteSup;
    private long inicioSensibilizado;

    public TransicionConTiempo(int ID,long LimiteInf,long LimiteSup){
        this.ID = ID;
        this.limiteInf =LimiteInf;
        this.limiteSup =LimiteSup;

    }

    public TransicionConTiempo(int ID,long LimiteInf){
        this.ID = ID;
        this.limiteInf =LimiteInf;
        this.limiteSup =999999999;

    }

    public long getLimiteInf() {
        return limiteInf;
    }

    public long getLimiteSup() {
        return limiteSup;
    }

    public boolean estaAdentroDeVentana(){ //devuelve true si esta dentro de la ventana de tiempo para disparar la transicion
        if(inicioSensibilizado==0) return false;
        long diferencia = diferencia();
        if (diferencia> limiteInf && diferencia< limiteSup) {
            return true;
        }
        else{return false;}
    }
    public long diferencia(){ //calcula la diferencia entre el tiempo actual y el tiempo en que se sensibilizo la transicion
        long dif = Calendar.getInstance().getTimeInMillis() - inicioSensibilizado;
        return dif;
    }

    public boolean estaAntes(){ //devuelve true si se esta antes de la ventana de tiempo o false si esta despues
        return (diferencia()<limiteInf);
    }

    public void setInicioSensibilizado() {
        this.inicioSensibilizado = Calendar.getInstance().getTimeInMillis();
    }

    public long cuantoDormir() { //devuelve cuanto tiempo debe dormir para que al despertarse la transicion este habilitada
        return getLimiteInf()-diferencia();
    }

    public int getID(){
        return ID;
    }
}
