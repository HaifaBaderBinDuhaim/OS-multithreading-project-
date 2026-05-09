package osProject;

import java.util.Queue;

public class admitterThread extends Thread {
    private Queue<Process> jobQueue;
    private readyQueue ReadyQueue;
    private memoryManager memory;

    admitterThread(Queue<Process> jobQueue, readyQueue ReadyQueue, memoryManager memory) {
        this.jobQueue = jobQueue;
        this.ReadyQueue = ReadyQueue;
        this.memory = memory;
    }

    public void run() {
        System.out.println("Admitter Thread started");

        while (true) {
            synchronized (jobQueue) {
                if (!jobQueue.isEmpty()) {
                    Process process = jobQueue.peek();

                    if (memory.allocate(process.memoryRequired)) {
                        jobQueue.poll();
                        process.state = "READY";
                        ReadyQueue.insert(process);
                        System.out.println("[Admitter] Admitted P" + process.processId);
                    }
                } else {
                    break;
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }

        System.out.println("[Admitter] Thread terminated");
    }
}