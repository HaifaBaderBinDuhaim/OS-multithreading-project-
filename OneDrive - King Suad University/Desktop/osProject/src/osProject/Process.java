package osProject;

public class Process {
    int processId;
    int burstTime;
    int remainingTime;
    int priority;
    int memoryRequired;

    String state;
    int startTime = -1;
    int terminationTime;
    int waitingTime;
    int turnaroundTime;

    public Process(int processId, int burstTime, int priority, int memoryRequired) {
        this.processId = processId;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.memoryRequired = memoryRequired;
        this.state = "NEW";
    }
}