package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

import static java.lang.Integer.MAX_VALUE;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task T){
        int min=MAX_VALUE;
        for(Server s : servers){
            if(s.getSize()<min){
                min=s.getSize();
            }
        }
        for(Server s : servers){
            if(s.getSize()==min){
                s.addTask(T);
                break;
            }
        }
    }
}
