package BusinessLogic;
import GUI.SimulationFrame;
import Model.Server;
import Model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

import static java.lang.Integer.MIN_VALUE;

public class SimulationManager implements Runnable {
    //read form UI
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int minArrivalTime;
    private int maxArrivalTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy;

    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;
    private List<Task> totalTasks;

    private double averageWaitingTime=0;
    private double averageServiceTime=0;
    public int peakHour=0;
    public int maxPeakHour=MIN_VALUE;

    public SimulationManager(SimulationFrame frame) throws IOException {
        this.frame = frame;
        try {
            this.timeLimit = frame.getTimeLimit();
            if (timeLimit <= 0) {
                throw new IllegalArgumentException("Time limit must be greater than 0");
            }

            this.minProcessingTime = frame.getMinServiceTime();
            if (minProcessingTime <= 0) {
                throw new IllegalArgumentException("Minimum service time must be greater than 0");
            }

            this.maxProcessingTime = frame.getMaxServiceTime();
            if (maxProcessingTime <= 0 || maxProcessingTime < minProcessingTime || maxProcessingTime > timeLimit) {
                throw new IllegalArgumentException("Maximum service time must be greater than 0 and greater than or equal to minimum service time and less than or equal to time limit");
            }

            this.minArrivalTime = frame.getMinArrivalTime();
            if (minArrivalTime < 0) {
                throw new IllegalArgumentException("Minimum arrival time must be non-negative");
            }

            this.maxArrivalTime = frame.getMaxArrivalTime();
            if (maxArrivalTime < 0 || maxArrivalTime < minArrivalTime || maxArrivalTime > timeLimit) {
                throw new IllegalArgumentException("Maximum arrival time must be non-negative and greater than or equal to minimum arrival time and less than or equal to time limit");
            }

            this.selectionPolicy = SelectionPolicy.valueOf(frame.getStrategy());
            this.numberOfServers = frame.getNumberOfServers();
            if (numberOfServers <= 0) {
                throw new IllegalArgumentException("Number of servers must be greater than 0");
            }

            this.numberOfClients = frame.getNumberOfClients();
            if (numberOfClients <= 0) {
                throw new IllegalArgumentException("Number of clients must be greater than 0");
            }

            generateNRandomTasks();
            scheduler = new Scheduler(numberOfServers, numberOfClients);
            scheduler.changeStrategy(selectionPolicy);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input: " + e.getMessage());
        }
    }

    private void generateNRandomTasks(){
        Random random=new Random();
        generatedTasks=new ArrayList<Task>();
        for(int i=0;i<numberOfClients;i++){
            int processingTime= random.nextInt(maxProcessingTime-minProcessingTime+1)+minProcessingTime;
            int arrivalTime= random.nextInt(maxArrivalTime-minArrivalTime+1)+minArrivalTime;
            Task t=new Task(i+1,arrivalTime,processingTime);
            generatedTasks.add(t);
            averageServiceTime+=processingTime;
        }
        averageServiceTime/=numberOfClients;
        Collections.sort(generatedTasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getArrivalTime()-o2.getArrivalTime();
            }
        });
        totalTasks=new ArrayList<Task>(generatedTasks);
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getAverageServiceTime() {
        return averageServiceTime;
    }

    public void getPeakHour(int time) {
        int max=0;
        for(Server server:scheduler.getServers()){
            max+=server.getSize();
        }
        if(max>peakHour && max>maxPeakHour){
            this.maxPeakHour=max;
            this.peakHour=time;
        }
    }

    @Override
    public void run(){
        try(BufferedWriter writer=new BufferedWriter(new FileWriter("test3.txt"))){
        int currentTime=0;
        while(currentTime<=timeLimit){
            for(Task t:generatedTasks){
                if(t.getArrivalTime()==currentTime){
                    scheduler.dispatchTask(t);
                    averageWaitingTime+=t.getWaitingTime();
                }
            }

            int auxTime=currentTime;
            getPeakHour(currentTime);
            generatedTasks.removeIf(task->task.getArrivalTime()==auxTime);
            updateLog(currentTime,writer);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime++;

        }
        averageWaitingTime/=numberOfClients;
        frame.updateOutput("Simulation finished\n");
        resultsToFile(writer);
    }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void updateLog(int currentTime, BufferedWriter writer) throws IOException {
        for(Server server:scheduler.getServers()){
            Task[] tasks=server.getTasks();
            int waitedTime=0;
            for(Task task:tasks){
                if(task!=null) {
                    task.setWaitingTime(waitedTime);
                    waitedTime += task.getRemainingTime();
                }
            }
        }

        StringBuilder log = new StringBuilder();
        log.append("Time: ").append(currentTime).append("\n");
        log.append("Waiting Clients:\n");
        for (Task task : generatedTasks) {
            log.append("Client ID: ").append(task.getID()).append(", Arrival Time: ").append(task.getArrivalTime()).append(", Service Time: ").append(task.getServiceTime()).append("\n");
        }
        log.append("Servers:\n");
        for(int i=0;i<scheduler.getServers().size();i++){
            Server server=scheduler.getServers().get(i);
            log.append("Queue ").append(i+1).append(":\n");
            if(server.getTasks().length==0){
                log.append("CLosed\n");
            }
            else{
                for(Task task:server.getTasks()){
                    if(task==null)continue;
                    if(task.getRemainingTime()==0)continue;
                    log.append("Client ID: ").append(task.getID()).append(", Arrival Time: ").append(task.getArrivalTime()).append(", Service Time: ").append(task.getServiceTime()).append("\n");
                }
            }
            log.append("\n");
        }
        frame.updateOutput(log.toString());
        writer.write(log.toString());
        writer.newLine();

    }

    private void resultsToFile(BufferedWriter writer) throws IOException {
            writer.write("Average waiting time: "+getAverageWaitingTime()+"\n");
            writer.write("Average service time: "+getAverageServiceTime()+"\n");
             writer.write("Peak hour: " + peakHour + "\n");
             writer.close();

    }
}
