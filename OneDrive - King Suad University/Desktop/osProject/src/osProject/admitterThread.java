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
            synchronized (jobQueue) { // to ensure no one else can edit job queue at the same time
                if (!jobQueue.isEmpty()) {
                    Process process = jobQueue.peek();

                    if (memory.allocate(process.memoryRequired)) {
                        jobQueue.poll(); // delete procces from jobQueue
                        ReadyQueue.insert(process); // add the deleted procces to ReadtQueue
                        System.out.println("[Admitter] Admitted P" + process.processId);
                    }
                }
            }

            // Check if all processes are done
            if (jobQueue.isEmpty() && ReadyQueue.isEmpty()) {
                break;
            }

            try {
                Thread.sleep(10); // the thread will wait 10 ms between checks
            } catch (InterruptedException e) {
                break;
            }
        }

        System.out.println("[Admitter] Thread terminated");
    }
}
