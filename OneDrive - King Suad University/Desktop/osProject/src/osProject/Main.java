package osProject;

import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        // 1️⃣ قراءة الملف
        Queue<Process> jobQueue = JobReader.readJobs("job.txt");
        System.out.println("\n===== CPU Scheduler Simulator =====");
        System.out.println("1-Shortest Job First (SJF)");
        System.out.println("2-Round Robin (RR)");
        System.out.println("3-Priority Scheduling (Non-preemptive)");
        System.out.print("Choose algorithm (1-3): ");
        int choice = read.nextInt();

        switch (choice) {
            case 1:
                runSJF(jobQueue);
                break;

            case 2:
                runRR(jobQueue);
                break;

            case 3:
    runPriority(jobQueue);
                break;
            default:
                System.out.println("Invalid choice");
        }
        read.close();

        // 2️⃣ طباعة قبل الترتيب
        System.out.println("Before SJF:");
        for (Process p : jobQueue) {
            System.out.println("P" + p.processId + " (Burst=" + p.burstTime + ")");
        }

        // 3️⃣ تنفيذ SJF
        List<Process> result = SJF.runSJF(jobQueue);

        // 4️⃣ طباعة بعد الترتيب
        System.out.println("\nAfter SJF (Sorted by Burst Time):");
        for (Process p : result) {
            System.out.println("P" + p.processId + " (Burst=" + p.burstTime + ")");
        }

        // 5️⃣ جدول العمليات
        System.out.println("\nProcess Table:");
        System.out.println("ID\tBurst\tStart\tEnd\tWaiting\tTurnaround");

        for (Process p : result) {
            System.out.println(
                    p.processId + "\t" +
                            p.burstTime + "\t" +
                            p.startTime + "\t" +
                            p.terminationTime + "\t" +
                            p.waitingTime + "\t" +
                            p.turnaroundTime);
        }

        // 6️⃣ Gantt Chart
        System.out.println("\nGantt Chart:");

        int time = 0;
        for (Process p : result) {
            System.out.print("| P" + p.processId + " ");
        }
        System.out.println("|");

        System.out.print("0");
        for (Process p : result) {
            time += p.burstTime;
            System.out.print("   " + time);
        }
    }

    private static void runSJF(Queue<Process> jobQueue) {
        // 2️⃣ طباعة قبل الترتيب
        System.out.println("Before SJF:");
        for (Process p : jobQueue) {
            System.out.println("P" + p.processId + " (Burst=" + p.burstTime + ")");
        }
        // 3️⃣ تنفيذ SJF
        List<Process> result = SJF.runSJF(jobQueue);

        // 4️⃣ طباعة بعد الترتيب

        System.out.println("\nAfter SJF (Sorted by Burst Time):");
        for (Process p : result) {
            System.out.println("P" + p.processId + " (Burst=" + p.burstTime + ")");
        }
        // 5️⃣ جدول العمليات
        System.out.println("\nProcess Table:");
        System.out.println("ID\tBurst\tStart\tEnd\tWaiting\tTurnaround");

        for (Process p : result) {
            System.out.println(
                    p.processId + "\t" +
                            p.burstTime + "\t" +
                            p.startTime + "\t" +
                            p.terminationTime + "\t" +
                            p.waitingTime + "\t" +
                            p.turnaroundTime);
        }

        // 6️⃣ Gantt Chart
        System.out.println("\nGantt Chart:");

        int time = 0;
        for (Process p : result) {
            System.out.print("| P" + p.processId + " ");
        }
        System.out.println("|");

        System.out.print("0");
        for (Process p : result) {
            time += p.burstTime;
            System.out.print("   " + time);
        }

    }

    private static void runRR(Queue<Process> jobQueue) {

        memoryManager memory = new memoryManager();
        readyQueue readyQueue = new readyQueue();

        admitterThread admitter = new admitterThread(jobQueue, readyQueue, memory);
        admitter.start();

        try {
            admitter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Process> results = RoundRobin.runRR(readyQueue, memory);

        System.out.println("\nProcess Table:");
        System.out.println("ID\tBurst\tStart\tEnd\tWaiting\tTurnaround");

        double totalWaiting = 0;
        double totalTurnaround = 0;

        for (Process p : results) {
            System.out.println(p.processId + "\t" +
                    p.burstTime + "\t" +
                    p.startTime + "\t" +
                    p.terminationTime + "\t" +
                    p.waitingTime + "\t" +
                    p.turnaroundTime);

            totalWaiting += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
        }

        double avgWaiting = totalWaiting / results.size();
        double avgTurnaround = totalTurnaround / results.size();

        System.out.println("\nAverage Waiting Time: " + avgWaiting);
        System.out.println("Average Turnaround Time: " + avgTurnaround);

    }
    private static void runPriority(Queue<Process> jobQueue) {

    memoryManager memory = new memoryManager();
    readyQueue readyQueue = new readyQueue();

    admitterThread admitter = new admitterThread(jobQueue, readyQueue, memory);
    admitter.start();

    try {
        admitter.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    PriorityScheduler ps = new PriorityScheduler();
    List<Process> results = ps.runPriority(readyQueue, memory);

    System.out.println("\nProcess Table:");
    System.out.println("ID\tBurst\tStart\tEnd\tWaiting\tTurnaround");

    double totalWaiting = 0;
    double totalTurnaround = 0;

    for (Process p : results) {
        System.out.println(p.processId + "\t" +
                p.burstTime + "\t" +
                p.startTime + "\t" +
                p.terminationTime + "\t" +
                p.waitingTime + "\t" +
                p.turnaroundTime);

        totalWaiting += p.waitingTime;
        totalTurnaround += p.turnaroundTime;
    }

    double avgWaiting = totalWaiting / results.size();
    double avgTurnaround = totalTurnaround / results.size();

    System.out.println("\nAverage Waiting Time: " + avgWaiting);
    System.out.println("Average Turnaround Time: " + avgTurnaround);

    System.out.println("\nStarvation Report:");
    ps.printStarved();
}


}
