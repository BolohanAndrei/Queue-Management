package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

import static java.lang.Integer.MAX_VALUE;

public class ShortestTimeStrategy implements Strategy {
    public void addTask(List<Server> servers, Task t){
        int min=MAX_VALUE;
        for(Server s : servers){
            if(s.getWaitingPeriod().get()<min){
                min=s.getWaitingPeriod().get();
            }
        }
        for(Server s : servers){
            if(s.getWaitingPeriod().get()==min){
                s.addTask(t);
                break;
            }
        }
    }
}
