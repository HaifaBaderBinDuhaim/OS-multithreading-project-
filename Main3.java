import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    // Global Queues
    public static Queue<PCB> jobQueue = new LinkedBlockingQueue<>();
    public static Queue<PCB> readyQueue = new LinkedBlockingQueue<>();
    public static List<PCB> finishedProcesses = new ArrayList<>();

    public static int globalClock = 0; // simulation time

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("===== CPU Scheduler Simulation =====");
        System.out.println("Choose Scheduling Algorithm:");
        System.out.println("1) Shortest Job First (SJF)");
        System.out.println("2) Round Robin (RR)");
        System.out.println("3) Priority Scheduling");
        System.out.print("Enter choice: ");

        int choice = input.nextInt();

        // -----------------------------
        // Start Thread 1 (Job Loader)
        // -----------------------------
        Thread jobLoader = new Thread(new JobLoaderThread(jobQueue));
        jobLoader.start();

        // -----------------------------
        // Start Thread 2 (Ready Queue Loader)
        // -----------------------------
        Thread readyLoader = new Thread(new ReadyQueueLoaderThread(jobQueue, readyQueue));
        readyLoader.start();

        try {
            jobLoader.join();   // wait until job.txt is fully loaded
            readyLoader.join(); // wait until all jobs are admitted
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll jobs loaded into Ready Queue.");
        System.out.println("Starting selected scheduling algorithm...\n");

        // -----------------------------
        // Run Selected Scheduling Algorithm
        // -----------------------------
        GanttChart gantt = new GanttChart();
        Scheduler scheduler = null;

        switch (choice) {
            case 1:
                scheduler = new SJF();
                break;

            case 2:
                scheduler = new RoundRobin();
                break;

            case 3:
                scheduler = new PriorityScheduler();
                break;

            default:
                System.out.println("Invalid choice.");
                System.exit(0);
        }

        // Run the selected scheduler
        finishedProcesses = scheduler.run(readyQueue, gantt);

        // -----------------------------
        // Output Results
        // -----------------------------
        System.out.println("\n===== Gantt Chart =====");
        gantt.printChart();

        System.out.println("\n===== Process Table =====");
        printProcessTable(finishedProcesses);

        System.out.println("\n===== Performance Metrics =====");
        printMetrics(finishedProcesses);

        if (choice == 3) {
            System.out.println("\n===== Starvation Report =====");
            ((PriorityScheduler) scheduler).printStarvedProcesses();
        }

        System.out.println("\nSimulation Completed.");
    }

    // ---------------------------------------------------------
    // Print Table of Processes
    // ---------------------------------------------------------
    public static void printProcessTable(List<PCB> processes) {
        System.out.println("PID\tBurst\tStart\tFinish\tWaiting\tTurnaround");

        for (PCB p : processes) {
            System.out.println(
                    p.processID + "\t" +
                    p.burstTime + "\t" +
                    p.startTime + "\t" +
                    p.finishTime + "\t" +
                    p.waitingTime + "\t" +
                    p.turnaroundTime
            );
        }
    }

    // ---------------------------------------------------------
    // Print Metrics
    // ---------------------------------------------------------
    public static void printMetrics(List<PCB> processes) {
        double totalWT = 0, totalTT = 0;

        for (PCB p : processes) {
            totalWT += p.waitingTime;
            totalTT += p.turnaroundTime;
        }

        System.out.println("Average Waiting Time: " + (totalWT / processes.size()));
        System.out.println("Average Turnaround Time: " + (totalTT / processes.size()));
    }
}

