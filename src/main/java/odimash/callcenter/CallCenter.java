package odimash.callcenter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CallCenter {
    private final BlockingQueue<Operator> operators;

    public CallCenter(int numberOfOperators) {
        operators = new LinkedBlockingQueue<>(numberOfOperators);
        for (int i = 0; i < numberOfOperators; i++) {
            operators.add(new Operator("Operator-" + (i + 1)));
        }
    }

    public Operator getAvailableOperator() throws InterruptedException {
        return operators.take();
    }

    public void releaseOperator(Operator operator) {
        try {
            operators.put(operator);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
