import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //---------------------------------------------------------------------------------------
        //Part 2 - Reading from file part

        ArrayList<Process> processes = new ArrayList<>();
        ArrayList<Process> AllProcesses = new ArrayList<>();

        File file2 = new File("workload.txt");
        Scanner sc = new Scanner(file2);

        if(!file2.exists()){
            System.out.println("There is no file");
            System.exit(0);
        }
        while(sc.hasNextLine()){
            ArrayList<Integer> cpuBurst = new ArrayList<>();
            ArrayList<Integer> ioBurst = new ArrayList<>();
            String line = sc.nextLine();
            String[] values = line.split(" ");
            int id = Integer.parseInt(values[0]);
            int arrivalTime = Integer.parseInt(values[1]);
            for(int i = 2; i < values.length; i = i+2){
                cpuBurst.add(Integer.parseInt(values[i]));
            }
            for(int i = 3; i < values.length; i = i+2){
                ioBurst.add(Integer.parseInt(values[i]));
            }
            Process p = new Process(id, arrivalTime, cpuBurst, ioBurst);
            processes.add(p);
            AllProcesses.add(p);
        }

        //------------------------------------------------------------------------------------------------------
        // Part 2
        //Q1 Variables
        ArrayList<Process> output = new ArrayList<>();
        ArrayList <Process> Q1 = new ArrayList<>();
        int quantum = 10;
        int numOfQuantum = 0;
        //----------------------------------------------------------------------
        //Q2 Variables
        ArrayList <Process> Q2 =new ArrayList<>();
        ArrayList<Process> processes2 = new ArrayList<>();
        ArrayList<Process> output2 = new ArrayList<>();
        int quantum2 = 10;
        int numOfQuantum2 = 0;
        //---------------------------------------------------------------------
        //Q3 Variables
        ArrayList <Process> Q3 =new ArrayList<>();
        ArrayList<Process> output3 = new ArrayList<>();
        ArrayList<Process> processes3 = new ArrayList<>();
        Process oldProcess = null;
        //---------------------------------------------------------------------
        //Q4 Variables
        ArrayList <Process> Q4 =new ArrayList<>();
        ArrayList<Process> processes4 = new ArrayList<>();
        ArrayList <Process> output4 = new ArrayList<>();
        //--------------------------------------------------------------------
        ArrayList <ForGanttChart> totalOutput = new ArrayList<>();
        ArrayList<Process> io = new ArrayList<>();
        int idleTime = 0;


        int currentTime = 0;
        int previousCurrentTime = -1;

        Collections.sort(processes, (p1, p2) -> p1.getArrivalTime() - p2.getArrivalTime());
        Process p = processes.remove(0);
        Q1.add(p);
        idleTime = p.getArrivalTime();
        int startTime = p.getArrivalTime();
        currentTime = p.getArrivalTime();

        while(!allBurstNull(AllProcesses)){
            if(previousCurrentTime != currentTime){
                for(int i=0; i < processes.size(); i++){
                    if(processes.get(i).getArrivalTime() == currentTime){
                        Q1.add(processes.get(i));
                    }
                }

                for(int i=0; i < processes2.size(); i++){
                    if(processes2.get(i).getArrivalTime() == currentTime){
                        Q2.add(processes2.get(i));
                    }
                }

                for(int i=0; i < processes3.size(); i++){
                    if(processes3.get(i).getArrivalTime() == currentTime){
                        Q3.add(processes3.get(i));
                    }
                }

                for(int i=0; i < processes4.size(); i++){
                    if(processes4.get(i).getArrivalTime() == currentTime){
                        Q4.add(processes4.get(i));
                    }
                }
            }

            for (int i=0; i < io.size(); i++){
                if(io.get(i).ioOutTime == currentTime){
                    io.remove(i);
                }
            }

            previousCurrentTime = currentTime;

            if(!Q1.isEmpty()){
                Process process = Q1.get(0);
                if(process.getCpuBurst().get(0) > 0 && numOfQuantum < quantum && process.numOfQ1 <= 10){
                    process.decrementBurstTime(1, 0);
                    numOfQuantum++;
                    currentTime++;
                }else if(process.getCpuBurst().get(0) > 0 && numOfQuantum < quantum && process.numOfQ1 > 10){
                    Q2.add(process);
                    processes2.add(process);
                    processes.remove(process);
                    Q1.remove(0);
                }else if(process.getCpuBurst().get(0) > 0 && numOfQuantum >= quantum && process.numOfQ1 <= 10){
                    output.add(process);
                    totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q1"));
                    Q1.add(process);
                    process.numOfQ1++;
                    Q1.remove(0);
                    numOfQuantum = 0;
                }else if(process.getCpuBurst().get(0) > 0 && numOfQuantum >= quantum && process.numOfQ1 > 10){
                    Q2.add(process);
                    processes2.add(process);
                    processes.remove(process);
                    process.numOfQ1++;
                    Q1.remove(0);
                    numOfQuantum = 0;
                }else if(process.getCpuBurst().get(0) == 0){
                    if(!process.getIoBurst().isEmpty()){
                        int ioValue = process.getIoBurst().remove(0);
                        io.add(process);
                        process.ioOutTime = currentTime+ioValue;
                        process.setArrivalTime(currentTime+ioValue);
                        process.numOfQ1++;
                    }
                    output.add(process);
                    totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q1"));
                    process.getCpuBurst().remove(0);
                    Q1.remove(0);
                    numOfQuantum = 0;
                }
                numOfQuantum2 = 0;
            }else {
                if(!Q2.isEmpty()){
                    Process process = Q2.get(0);
                    if(process.getCpuBurst().get(0) > 0 && numOfQuantum2 == 0 && process.numOfQ2 <= 10){
                        process.decrementBurstTime(1, 0);
                        numOfQuantum2++;
                        currentTime++;
                        process.numOfQ2++;
                        output2.add(process);
                        totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q2"));
                        if(process.getCpuBurst().get(0) == 0){
                            if(!process.getIoBurst().isEmpty()) {
                                int ioValue = process.getIoBurst().remove(0);
                                io.add(process);
                                process.ioOutTime = currentTime + ioValue;
                                process.setArrivalTime(currentTime + ioValue);
                                process.numOfQ2++;
                            }
                            totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q2"));
                            process.getCpuBurst().remove(0);
                            Q2.remove(0);
                            numOfQuantum2 = 0;
                        }
                    }else if(process.getCpuBurst().get(0) > 0 && numOfQuantum2 < quantum2 && process.numOfQ2 <= 10){
                        process.decrementBurstTime(1, 0);
                        numOfQuantum2++;
                        currentTime++;
                        totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q2"));
                        if(process.getCpuBurst().get(0) == 0){
                            if(!process.getIoBurst().isEmpty()){
                                int ioValue = process.getIoBurst().remove(0);
                                io.add(process);
                                process.ioOutTime = currentTime+ioValue;
                                process.setArrivalTime(currentTime+ioValue);
                                process.numOfQ2++;
                            }
                            totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q2"));
                            process.getCpuBurst().remove(0);
                            Q2.remove(0);
                            numOfQuantum2 = 0;
                        }
                    }else if(process.getCpuBurst().get(0) > 0 && numOfQuantum2 < quantum2 && process.numOfQ2 > 10){
                        Q3.add(process);
                        processes3.add(process);
                        processes2.remove(process);
                        Q2.remove(0);
                        //currentTime++;
                    }else if(process.getCpuBurst().get(0) > 0 && numOfQuantum2 >= quantum2 && process.numOfQ2 <= 10){
                        /*output2.add(process);
                        totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q2"));*/
                        Q2.add(process);
                        Q2.remove(0);
                        numOfQuantum2 = 0;
                    }else if(process.getCpuBurst().get(0) > 0 && numOfQuantum2 >= quantum2 && process.numOfQ2 > 10){
                        Q3.add(process);
                        processes3.add(process);
                        processes2.remove(process);
                        Q2.remove(0);
                        numOfQuantum2 = 0;
                    }
                }else {
                    /*System.out.println("-------------- All processes -------------");
                    System.out.println("Current Time = " + currentTime);
                    for(int i=0; i<Q3.size(); i++){
                        System.out.println(Q3.get(i));
                    }*/
                    if(!Q3.isEmpty()){
                        Collections.sort(Q3, (p1, p2) -> p1.getCpuBurst().get(0) - p2.getCpuBurst().get(0));
                        Process process = Q3.get(0);

                        if(oldProcess == null){
                            process.numOfQ3++;
                            oldProcess = process;
                            if(process.getCpuBurst().get(0) > 0){
                                output3.add(oldProcess);
                                totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q3"));
                                process.decrementBurstTime(1, 0);
                                currentTime++;
                                if(process.getCpuBurst().get(0) == 0){
                                    if(!process.getIoBurst().isEmpty()){
                                        int ioValue = process.getIoBurst().remove(0);
                                        io.add(process);
                                        process.ioOutTime = currentTime+ioValue;
                                        process.setArrivalTime(currentTime+ioValue);
                                    }
                                    //output3.add(process);
                                    totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q3"));
                                    process.getCpuBurst().remove(0);
                                    Q3.remove(0);
                                }
                            }
                        }else if(oldProcess == process){
                            if(process.getCpuBurst().get(0) > 0 && process.numOfQ3 <= 3){
                                output3.add(oldProcess);
                                totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q3"));
                                process.decrementBurstTime(1, 0);
                                currentTime++;
                                if(process.getCpuBurst().get(0) == 0){
                                    if(!process.getIoBurst().isEmpty()){
                                        int ioValue = process.getIoBurst().remove(0);
                                        io.add(process);
                                        process.ioOutTime = currentTime+ioValue;
                                        process.setArrivalTime(currentTime+ioValue);
                                    }
                                    //output3.add(process);
                                    totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q3"));
                                    process.getCpuBurst().remove(0);
                                    Q3.remove(0);
                                }
                            }else if(process.getCpuBurst().get(0) > 0 && process.numOfQ3 > 3){
                                Q4.add(process);
                                processes3.remove(process);
                                processes4.add(process);
                                Q3.remove(0);
                                //currentTime++;
                            }
                        }else{
                            oldProcess = process;
                            process.numOfQ3++;
                            if(process.getCpuBurst().get(0) > 0 && process.numOfQ3 <= 3){
                                output3.add(oldProcess);
                                totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q3"));
                                process.decrementBurstTime(1, 0);
                                currentTime++;
                                if(process.getCpuBurst().get(0) == 0){
                                    if(!process.getIoBurst().isEmpty()){
                                        int ioValue = process.getIoBurst().remove(0);
                                        io.add(process);
                                        process.ioOutTime = currentTime+ioValue;
                                        process.setArrivalTime(currentTime+ioValue);
                                    }
                                    //output3.add(process);
                                    totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q3"));
                                    process.getCpuBurst().remove(0);
                                    Q3.remove(0);
                                }
                            }else if(process.getCpuBurst().get(0) > 0 && process.numOfQ3 > 3){
                                Q4.add(process);
                                processes3.remove(process);
                                processes4.add(process);
                                Q3.remove(0);
                                //currentTime++;
                            }
                        }
                        //currentTime++;
                    }else {
                        if (!Q4.isEmpty()) {
                            Process process = Q4.get(0);
                            if (process.getCpuBurst().get(0) > 0) {
                                process.decrementBurstTime(1, 0);
                                currentTime++;
                            } else {
                                if (!process.getIoBurst().isEmpty()) {
                                    int ioValue = process.getIoBurst().remove(0);
                                    io.add(process);
                                    process.ioOutTime = currentTime + ioValue;
                                    process.setArrivalTime(currentTime + ioValue);
                                }
                                output4.add(process);
                                totalOutput.add(new ForGanttChart(process.getId(), currentTime, "Q4"));
                                process.getCpuBurst().remove(0);
                                Q4.remove(process);
                            }
                        } else {
                            currentTime++;
                            idleTime++;
                        }
                    }
                }

            }
        }

        //-----------------------------------------------------------
        if(!output.isEmpty()){
            System.out.println("Output for Q1:");
            for(int i=0; i < output.size(); i++){
                if(i == output.size()-1){
                    System.out.print("P" +output.get(i).getId());
                }else{
                    System.out.print("P" +output.get(i).getId() + " -> ");
                }
            }
        }
        //-----------------------------------------------------------
        if(!output2.isEmpty()){
            System.out.println("\nOutput for Q2:");
            for(int i=0; i < output2.size(); i++){
                if(i == output2.size()-1){
                    System.out.print("P" +output2.get(i).getId());
                }else{
                    System.out.print("P" +output2.get(i).getId() + " -> ");
                }
            }
        }
        //-----------------------------------------------------------
        ArrayList<Process> finalOutput3 = new ArrayList<>();
        if(!output3.isEmpty()) {
            finalOutput3.add(output3.get(0));
            int j = 0;

            for (int i = 1; i < output3.size(); i++) {
                if (output3.get(i) != finalOutput3.get(j)) {
                    finalOutput3.add(output3.get(i));
                    j++;
                }
            }

            System.out.println("\nOutput for Q3:");
            for (int i = 0; i < finalOutput3.size(); i++) {
                if(i == finalOutput3.size()-1){
                    System.out.print("P" +finalOutput3.get(i).getId());
                }else{
                    System.out.print("P" +finalOutput3.get(i).getId() + " -> ");
                }
            }
        }
        //-----------------------------------------------------------
        if(!output4.isEmpty()){
            System.out.println("\nOutput for Q4:");
            for(int i=0; i < output4.size(); i++){
                if(i == output4.size()-1){
                    System.out.print("P" +output4.get(i).getId());
                }else{
                    System.out.print("P" + output4.get(i).getId() + " -> ");
                }
            }
        }
        //-----------------------------------------------------------

        ArrayList<ForGanttChart> finalOutputAll = new ArrayList<>();
        finalOutputAll.add(totalOutput.get(0));
        int k=0;

        for(int i=1; i<totalOutput.size(); i++){
            if(totalOutput.get(i).id == finalOutputAll.get(k).id){
                finalOutputAll.set(k, totalOutput.get(i));
            }else{
                k++;
                finalOutputAll.add(totalOutput.get(i));
            }
        }

        System.out.println("\n\nGantt Chart:");
        System.out.print("|" + startTime +"|");
        for(int i=0; i < finalOutputAll.size(); i++){
            System.out.print("   P" + finalOutputAll.get(i).id + "   |" +finalOutputAll.get(i).startTime + "|");
        }
        //-----------------------------------------------------------
        System.out.println("\n\nI/O values:");
        for (int i=0; i < io.size(); i++){
            System.out.print(io.get(i).getId() + " -> ");
        }

        double cpuUtlization = (double) (currentTime-idleTime)/currentTime * 100;
        System.out.printf("\n\nCPU Utlization = %.2f", cpuUtlization);
        System.out.println("%");
    }


    public static boolean allBurstNull(ArrayList<Process> processes){
        boolean flag = true;
        for(int i=0; i<processes.size();i++){
            if(!processes.get(i).getCpuBurst().isEmpty()){
                flag = false;
            }
        }
        return flag;
    }
}
