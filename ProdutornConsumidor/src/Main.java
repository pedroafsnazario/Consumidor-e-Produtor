import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

class Consumidor extends Thread{
    private int id;

    private ArrayList<Integer> pilha;

    private int Y;

    public Consumidor(int id, ArrayList<Integer> pilha, int Y){
        this.id = id;
        this.pilha = pilha;
        this.Y = Y;
    }

    public void run() {

        while(true) {
            synchronized (this.pilha) {
                if(!this.pilha.isEmpty()) {

                    Integer valor = this.pilha.get(0);
                    this.pilha.remove(valor);
                    this.pilha.notify();
                    System.out.println("c" + id + " consumiu " + valor);


                }
                else {
                    System.out.println("c" + id + " não encontrou valores para retirar");

                    try {
                        this.pilha.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            try {
                sleep(Y);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Produtor extends Thread{
    private int id;

    private ArrayList<Integer> pilha;

    private int X;

    public Produtor(int id, ArrayList<Integer> pilha, int X){
        this.id = id;
        this.pilha = pilha;
        this.X = X;
    }
    public void run(){
        Random rand = new Random();
        while(true){
            int valor = rand.nextInt(1000);
            synchronized (this.pilha){

               if(this.pilha.size() < 10) {

                   this.pilha.add(valor);
                   System.out.println("p" + id + " produziu " + valor);

               }

               else {

                   System.out.println("p" + id + " pilha cheia");

                   try {
                       this.pilha.wait();
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }

                   this.pilha.notify();

               }
            }
            try {
                sleep(X);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> pilha = new ArrayList<Integer>();

        Produtor produtor1 = new Produtor(1, pilha, 3000);
        Produtor produtor2 = new Produtor(2, pilha, 3000);
        Produtor produtor3 = new Produtor(3, pilha, 3000);

        Consumidor consumidor1 = new Consumidor(1, pilha, 6000);
        Consumidor consumidor2 = new Consumidor(2, pilha, 6000);


        produtor1.start();
        produtor2.start();
        produtor3.start();

        consumidor1.start();
        consumidor2.start();

    }
}