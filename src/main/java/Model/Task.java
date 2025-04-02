package Model;

public class Task {
    private int ID;
    private int arrivalTime;
    private volatile int serviceTime;
    private int waitingTime;
    private volatile int remainingTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitingTime = 0;
        this.remainingTime = serviceTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getID() {
        return ID;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public synchronized void decrementRemainingTime() {
        if(this.remainingTime > 0) {
            this.remainingTime--;
            this.serviceTime--;
        }
    }

}
