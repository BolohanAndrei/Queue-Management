package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler( int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        servers=new ArrayList<Server>();

        for(int i=0;i<maxNoServers;i++){
            Server server=new Server();
            Thread thread=new Thread(server);
            servers.add(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if(policy==SelectionPolicy.SHORTEST_QUEUE){
            strategy=new ShortestQueueStrategy();
        }
        else if(policy==SelectionPolicy.SHORTEST_TIME){
            strategy=new ShortestTimeStrategy();
        }
    }

    public void dispatchTask(Task t){
        strategy.addTask(servers,t);
    }

    public List<Server> getServers() {
        return servers;
    }
}
