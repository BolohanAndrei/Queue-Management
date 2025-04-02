package Model;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(){
        this.tasks=new LinkedBlockingQueue<>();
        waitingPeriod=new AtomicInteger(0);

    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[tasks.size()]);
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void addTask(Task newTask){
        if(!tasks.isEmpty()){
            int currentTime=0;
            for(Task t:tasks){
                currentTime+=t.getServiceTime();
            }
            newTask.setWaitingTime(currentTime);
        }
            tasks.add(newTask);
            waitingPeriod.addAndGet(newTask.getServiceTime());

    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                Task task = tasks.peek();
                if(task!=null){
                while(task.getRemainingTime() > 0) {
                    Thread.sleep(1000);
                    task.decrementRemainingTime();
                    waitingPeriod.decrementAndGet();
                }
                tasks.take();
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

    }

    public int getSize(){
        return tasks.size();
    }

}
