import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** Orchestrated saga for a fund transfer: each step has a compensating action. */
public class SagaDemo {

    interface SagaStep {
        String name();
        void execute(Map<String, Object> ctx);      // local transaction in one service
        void compensate(Map<String, Object> ctx);   // semantic undo (NOT a DB rollback)
    }

    static SagaStep step(String name, Consumer<Map<String, Object>> action, Consumer<Map<String, Object>> undo) {
        return new SagaStep() {
            public String name() { return name; }
            public void execute(Map<String, Object> c) { action.accept(c); }
            public void compensate(Map<String, Object> c) { undo.accept(c); }
        };
    }

    static class SagaOrchestrator {
        private final List<SagaStep> steps;
        SagaOrchestrator(List<SagaStep> steps) { this.steps = steps; }

        boolean run(Map<String, Object> ctx) {
            Deque<SagaStep> completed = new ArrayDeque<>();
            for (SagaStep s : steps) {
                try {
                    System.out.println("  -> " + s.name());
                    s.execute(ctx);
                    completed.push(s);
                } catch (RuntimeException e) {
                    System.out.println("  !! " + s.name() + " failed: " + e.getMessage() + " -> compensating");
                    while (!completed.isEmpty()) {          // reverse order
                        SagaStep done = completed.pop();
                        System.out.println("  <- undo " + done.name());
                        done.compensate(ctx);
                    }
                    return false;
                }
            }
            return true;
        }
    }

    static SagaOrchestrator transferSaga() {
        return new SagaOrchestrator(List.of(
            step("ReserveFunds(source)", c -> c.put("reserved", true),  c -> c.put("reserved", false)),
            step("CreditBeneficiary",    c -> { if (Boolean.TRUE.equals(c.get("beneficiaryFrozen")))
                                                   throw new IllegalStateException("beneficiary account frozen");
                                                c.put("credited", true); },
                                         c -> c.put("credited", false)),
            step("SendConfirmation",     c -> c.put("notified", true),  c -> { })
        ));
    }

    public static void main(String[] args) {
        System.out.println("Scenario 1: happy path");
        Map<String, Object> ok = new HashMap<>();
        System.out.println("  result = " + (transferSaga().run(ok) ? "COMPLETED" : "ROLLED BACK") + " " + ok);

        System.out.println("Scenario 2: beneficiary frozen");
        Map<String, Object> bad = new HashMap<>(Map.of("beneficiaryFrozen", true));
        System.out.println("  result = " + (transferSaga().run(bad) ? "COMPLETED" : "ROLLED BACK") + " reserved=" + bad.get("reserved"));
    }
}
