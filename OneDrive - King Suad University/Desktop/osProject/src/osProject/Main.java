package osProject;

import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    	Scanner read = new Scanner(System.in);

    	int choice;

    	do {

    	    Queue<Process> jobQueue = JobReader.readJobs("job.txt");

    	    System.out.println("\n---- CPU Scheduler Simulator ----");
    	    System.out.println("1- Shortest Job First (SJF)");
    	    System.out.println("2- Round Robin (RR)");
    	    System.out.println("3- Priority Scheduling (Non-preemptive)");
    	    System.out.println("4- Exit");
    	    System.out.print("Choose algorithm (1-4): ");

    	    choice = read.nextInt();

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

    	        case 4:
    	            System.out.println("Program terminated.");
    	            break;

    	        default:
    	            System.out.println("Invalid choice.");
    	    }

    	} while (choice != 4);

    	read.close();
    }

    private static void runSJF(Queue<Process> jobQueue) {
        List<Process> result = SJF.runSJF(jobQueue);
        OutputManager.printNonPreemptiveReport(result);
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
        OutputManager.printRoundRobinReport(results, RoundRobin.getGanttChart());
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

        OutputManager.printNonPreemptiveReport(results);
        ps.printStarved();
    }
}