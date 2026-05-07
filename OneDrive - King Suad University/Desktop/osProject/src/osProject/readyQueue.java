package osProject;

import java.util.Queue;
import java.util.LinkedList;

public class readyQueue {
    private Queue<Process> queue;

    readyQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void insert(Process process) {
        queue.add(process);
        notify();
    }

    public synchronized Process remove() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }
}
