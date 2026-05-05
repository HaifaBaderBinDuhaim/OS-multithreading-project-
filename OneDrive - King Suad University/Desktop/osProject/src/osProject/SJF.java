package osProject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class SJF {

    public static List<Process> runSJF(Queue<Process> readyQueue) {
        List<Process> processes = new ArrayList<>(readyQueue);        //convert the Queue to list to be easy at sorting

        processes.sort(
        	    Comparator.comparingInt((Process p) -> p.burstTime)
        	              .thenComparingInt(p -> p.processId)
        	);    // sort processes based on burst time needed وقت التشغيل اللي تحتاجه

        int currentTime = 0;

        for (Process p : processes) {
            p.state = "RUNNING";
            p.startTime = currentTime;

            currentTime += p.burstTime;

            p.terminationTime = currentTime;
            p.turnaroundTime = p.terminationTime; 
            p.waitingTime = p.turnaroundTime - p.burstTime;

            p.state = "TERMINATED";
        }

        return processes;
    }
}