package osProject;

import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {

        // 1️⃣ قراءة الملف
        Queue<Process> jobQueue = JobReader.readJobs("job.txt");

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
                p.turnaroundTime
            );
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
}