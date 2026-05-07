package osProject;

import java.util.ArrayList;
import java.util.List;

public class RoundRobin {
    private static final int TIME_QUANTUM = 5;

    public static List<Process> runRR(readyQueue readyQueue, memoryManager memory) {
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;

        System.out.println("\nRound Robin Scheduling (Time Quantum = " + TIME_QUANTUM + " ms)\n");

        while (!readyQueue.isEmpty()) {
            Process p = readyQueue.remove();

            if (p.startTime == -1) {
                p.startTime = currentTime;
            }

            int executeTime = Math.min(p.remainingTime, TIME_QUANTUM);

            System.out.println("Time " + currentTime + "-" + (currentTime + executeTime) + ": P" + p.processId);

            currentTime += executeTime;
            p.remainingTime -= executeTime;

            if (p.remainingTime == 0) {
                p.terminationTime = currentTime;
                p.turnaroundTime = p.terminationTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
                p.state = "TERMINATED";
                memory.deallocate(p.memoryRequired);
                completedProcesses.add(p);
                System.out.println("P" + p.processId + " completed");
            } else {
                readyQueue.insert(p);
            }
        }

        return completedProcesses;
    }
}