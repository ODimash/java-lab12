package odimash.callcenter;

import java.util.concurrent.ThreadLocalRandom;

public class Client implements Runnable {
    private final CallCenter callCenter;
    private final String name;

    public Client(CallCenter callCenter, String name) {
        this.callCenter = callCenter;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(name + " is calling...");
                Operator operator = callCenter.getAvailableOperator();
                System.out.println(name + " is being served by " + operator.getName());
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
                System.out.println(name + " has finished the call.");
                callCenter.releaseOperator(operator);
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));  // Client waits before calling again
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
