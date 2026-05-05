package osProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class JobReader {

    public static Queue<Process> readJobs(String fileName) {  
        Queue<Process> jobQueue = new LinkedList<>();       // create a queue and read process object

        try {
            File file = new File(fileName);                  // will read from external file 
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {                // to read as long as there is a next line
                String line = scanner.nextLine().trim();  // to remove the spaces

                if (line.isEmpty()) {                          // to ignore empty line
                    continue;
                }
                
                                 //  the format of data in the file is like 1:25:4;500

                String[] mainParts = line.split(";");                /*mainParts[0] = "1:25:4"
                                                                       mainParts[1] = "500"*/                                
                String memoryPart = mainParts[1];

                String[] processParts = mainParts[0].split(":");   // to split this "1:25:4"

                int processId = Integer.parseInt(processParts[0]);    // process id
                int burstTime = Integer.parseInt(processParts[1]);    // burst time of process
                int priority = Integer.parseInt(processParts[2]);     // its priority
                int memoryRequired = Integer.parseInt(memoryPart);    // memory needed for process

                Process process = new Process(processId, burstTime, priority, memoryRequired);
                jobQueue.add(process);
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: job.txt file not found.");
        }

        return jobQueue;
    }
}