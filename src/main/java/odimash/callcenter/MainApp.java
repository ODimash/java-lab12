package odimash.callcenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApp {
    public static void main(String[] args) {
        CallCenter callCenter = new CallCenter(3);  // 3 operators

        ExecutorService executorService = Executors.newFixedThreadPool(10);  // 10 clients
        for (int i = 0; i < 10; i++) {
            Client client = new Client(callCenter, "Client-" + (i + 1));
            executorService.execute(client);
        }

        executorService.shutdown();
    }
}
