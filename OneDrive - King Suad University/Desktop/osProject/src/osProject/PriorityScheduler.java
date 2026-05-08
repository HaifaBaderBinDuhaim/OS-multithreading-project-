package osProject;

import java.util.ArrayList;
import java.util.List;

public class PriorityScheduler {

    private List<Process> starvedProcesses = new ArrayList<>();

    public List<Process> runPriority(readyQueue readyQueue, memoryManager memory) {

        List<Process> completed = new ArrayList<>();
        int currentTime = 0;

        System.out.println("\nPriority Scheduling (Non‑Preemptive)\n");

        while (!readyQueue.isEmpty()) {

            // Aging every 4 ms
            applyAging(readyQueue, currentTime);

            // Pick highest priority (lowest number)
            Process selected = getHighestPriority(readyQueue);

            if (selected.startTime == -1) {
                selected.startTime = currentTime;
            }

            selected.state = "RUNNING";

            System.out.println("Time " + currentTime + "-" + (currentTime + selected.burstTime)
                    + ": P" + selected.processId + " (Priority=" + selected.priority + ")");

            currentTime += selected.burstTime;

            selected.terminationTime = currentTime;
            selected.turnaroundTime = selected.terminationTime;
            selected.waitingTime = selected.turnaroundTime - selected.burstTime;
            selected.state = "TERMINATED";

            memory.deallocate(selected.memoryRequired);
            completed.add(selected);

            // Check starvation for remaining processes
            detectStarvation(readyQueue, currentTime);
        }

        return completed;
    }

    // ---------------------------------------------------------
    // Pick highest priority process
    // ---------------------------------------------------------
    private Process getHighestPriority(readyQueue rq) {
        Process best = null;

        List<Process> temp = new ArrayList<>();

        // Remove all to inspect
        while (!rq.isEmpty()) {
            temp.add(rq.remove());
        }

        // Find best
        for (Process p : temp) {
            if (best == null || p.priority < best.priority) {
                best = p;
            }
        }

        // Return others back to queue
        for (Process p : temp) {
            if (p != best) rq.insert(p);
        }

        return best;
    }

    // ---------------------------------------------------------
    // Aging: every 4 ms decrease priority number
    // ---------------------------------------------------------
    private void applyAging(readyQueue rq, int currentTime) {
        if (currentTime % 4 != 0) return;

        List<Process> temp = new ArrayList<>();

        while (!rq.isEmpty()) {
            temp.add(rq.remove());
        }

        for (Process p : temp) {
            if (p.priority > 1) {
                p.priority--;
            }
        }

        for (Process p : temp) rq.insert(p);
    }

    // ---------------------------------------------------------
    // Starvation detection
    // ---------------------------------------------------------
    private void detectStarvation(readyQueue rq, int currentTime) {
        int N = rq.size();
        int threshold = N * 5;

        List<Process> temp = new ArrayList<>();

        while (!rq.isEmpty()) {
            temp.add(rq.remove());
        }

        for (Process p : temp) {
            int waiting = currentTime - p.startTime;
            if (waiting > threshold && !starvedProcesses.contains(p)) {
                starvedProcesses.add(p);
            }
        }

        for (Process p : temp) rq.insert(p);
    }

    // ---------------------------------------------------------
    // Print starved processes
    // ---------------------------------------------------------
    public void printStarved() {
        if (starvedProcesses.isEmpty()) {
            System.out.println("No processes suffered from starvation.");
            return;
        }

        System.out.println("Starved Processes:");
        for (Process p : starvedProcesses) {
            System.out.println("P" + p.processId + " (Priority=" + p.priority + ")");
        }
    }
}

