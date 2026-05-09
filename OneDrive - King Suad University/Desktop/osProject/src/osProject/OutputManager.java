package osProject;

import java.util.List;

public class OutputManager {

    public static void printNonPreemptiveReport(List<Process> processes) {
        printGanttChartForNonPreemptive(processes);
        printProcessTable(processes);
        printAverages(processes);
    }

    public static void printRoundRobinReport(List<Process> processes, List<String> ganttChart) {
        System.out.println("\n---- GANTT CHART ----");
        for (String entry : ganttChart) {
            System.out.print(entry);
        }
        System.out.println();

        printProcessTable(processes);
        printAverages(processes);
    }

    private static void printGanttChartForNonPreemptive(List<Process> processes) {
        System.out.println("\n---- GANTT CHART ----");

        for (Process p : processes) {
            System.out.print("| P" + p.processId + " ");
        }
        System.out.println("|");

        for (Process p : processes) {
            System.out.print(p.startTime + "    ");
        }

        if (!processes.isEmpty()) {
            System.out.print(processes.get(processes.size() - 1).terminationTime);
        }

        System.out.println();
    }

    private static void printProcessTable(List<Process> processes) {
        System.out.println("\n---- PROCESS TABLE ----");
        System.out.println("ID\tBurst\tStart\tTermination\tWaiting\tTurnaround");

        for (Process p : processes) {
            System.out.println(
                    "P" + p.processId + "\t" +
                    p.burstTime + "\t" +
                    p.startTime + "\t" +
                    p.terminationTime + "\t\t" +
                    p.waitingTime + "\t" +
                    p.turnaroundTime
            );
        }
    }

    private static void printAverages(List<Process> processes) {
        double totalWaiting = 0;
        double totalTurnaround = 0;

        for (Process p : processes) {
            totalWaiting += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
        }

        System.out.println("\n---- PERFORMANCE METRICS ----");
        System.out.printf("Average Waiting Time: %.2f ms%n", totalWaiting / processes.size());
        System.out.printf("Average Turnaround Time: %.2f ms%n", totalTurnaround / processes.size());
    }
}