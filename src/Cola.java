import java.util.concurrent.Semaphore;

public class Cola {
    Semaphore condicion;
    Semaphore mutex;
    int bloqueados;

    public Cola(Semaphore mutex){
        this.mutex=mutex;
        condicion = new Semaphore(0, true);
        bloqueados=0;
    }

    public void Delay(){    //bloquea a un hilo y devuelve el mutex
        bloqueados++;
        mutex.release();
        try {
            condicion.acquire();
        } catch (InterruptedException e) {
            System.out.println("interrumpen Delay1 de " + Thread.currentThread().getName());
        }
    }

    public void Resume(){       //desbloquea un hilo, si no hay bloqueados no hace nada
        if(bloqueados>0){
            bloqueados--;
            condicion.release();
        }
    }

    public boolean Empty(){ //dice si la cola de bloqueados esta o no vacia
        if(bloqueados>0){
            return false;
        }else{
            return true;
        }
    }
}
