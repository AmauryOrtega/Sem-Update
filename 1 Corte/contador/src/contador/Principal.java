package contador;

/**
 * @author aroc
 */
public class Principal {

    public static void main(String[] args) {
        hiloCiclo1.setPriority(Thread.MAX_PRIORITY);
        hiloCiclo2.setPriority(Thread.MIN_PRIORITY);
        
        hiloCiclo1.start();
        hiloCiclo2.start();
    }

    static Thread hiloCiclo1 = new Thread() {

        @Override
        public void run() {
            super.run();
            //Hilo 1
            for (int i = 0; i < 1000; i++) {
                
                try {
                    this.sleep(100);
                } catch (InterruptedException ex) {
                    System.out.println("LOG: " + ex);;
                }
                
                System.out.println("Hilo1: " + i);
            }
        }

    };

    static Thread hiloCiclo2 = new Thread() {

        @Override
        public void run() {
            super.run();
            ///Hilo 2
            for (int i2 = 0; i2 < 1000; i2++) {
                System.out.println("Hilo2: " + i2);
            }
        }

    };
}
