import java.util.ArrayList;

public class Process {
    private int id;
    private int arrivalTime;
    public int startTime;
    public int ioOutTime;
    public int numOfQ1 = 1;
    public int numOfQ2 = 0;
    public int numOfQ3 = 1;
    private ArrayList<Integer> cpuBurst = new ArrayList<>();
    private ArrayList<Integer> ioBurst = new ArrayList<>();

    public Process(int id, int arrivalTime, ArrayList<Integer> cpuBurst, ArrayList<Integer> ioBurst) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.startTime = arrivalTime;
        this.cpuBurst = cpuBurst;
        this.ioBurst = ioBurst;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public ArrayList<Integer> getCpuBurst() {
        return cpuBurst;
    }

    public void setCpuBurst(ArrayList<Integer> cpuBurst) {
        this.cpuBurst = cpuBurst;
    }

    public ArrayList<Integer> getIoBurst() {
        return ioBurst;
    }

    public void setIoBurst(ArrayList<Integer> ioBurst) {
        this.ioBurst = ioBurst;
    }

    public void decrementBurstTime(int decNumber, int index){
        int decrement = this.cpuBurst.get(index) - decNumber;
        this.cpuBurst.set(index, decrement);
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", ioOutTime=" + ioOutTime +
                ", numOfQ1=" + numOfQ1 +
                ", numOfQ2=" + numOfQ2 +
                ", numOfQ3=" + numOfQ3 +
                ", cpuBurst=" + cpuBurst +
                ", ioBurst=" + ioBurst +
                '}';
    }
}
