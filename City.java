import java.util.HashMap;
import java.util.ArrayList;
/**
 * [Graph.java]
 * A program that represents a graph of communities and connections.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, April 25, 2022
 */
public class City {
    /*----- Instance variables -----*/
    private int totalCommunities;
    private int fulfilledCommunities;
    private ArrayList<Community>communities;
    private HashMap<Community, ArrayList<Community>> connections;

    City(){
        this.totalCommunities = 0;
        this.fulfilledCommunities = 0;
        this.communities = new ArrayList<Community>();
        this.connections = new HashMap<Community, ArrayList<Community>>();
    }
    public ArrayList<Community>getCommunities(){
        return communities;
    }
    public HashMap<Community, ArrayList<Community>> getConnections(){
        return connections;
    }
    public void addCommunity(Community newCommunity){
        communities.add(newCommunity);
        totalCommunities = totalCommunities+1;
    }
    public void addConnection(Community start, Community end){
        //NEED to add checker for if start and end Citys are in the graph
        if(!connections.containsKey(start)){
            connections.put(start, new ArrayList<Community>());
        }
        connections.get(start).add(end);
    }
    public void fireStationSolve (){
        while (!communitiesFulfilled()){
            if (leafCommunitiesExist()){
                for (Community i: getCommunities(1)){
                    i.setFireStation(true);
                }
            }
            else{

            }
        }

    }

    public Boolean communitiesFulfilled(){
        return fulfilledCommunities == communities.size();
    }

    public Boolean leafCommunitiesExist(){
        for (Community i: connections.keySet()){
            if (getUnfulfilledNeighbours(i) == 1){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Community> getCommunities(int unfulfilledNeighbours){
        ArrayList<Community> neighboursUnfulfilled = new ArrayList<Community>();

        if (unfulfilledNeighbours == 0){
            for (Community i: this.communities){
                if (!connections.containsKey(i)){
                    neighboursUnfulfilled.add(i);
                }
            }
        }
        else{
            for (Community i: this.communities){
                if (getUnfulfilledNeighbours(i) == unfulfilledNeighbours){
                    neighboursUnfulfilled.add(i);
                }
            }
        }

        return neighboursUnfulfilled;

    }

    public int getUnfulfilledNeighbours(Community community){
        if (!connections.containsKey(community)){
            return 0;
        }
        else{
            int unfulfilled = 0;
            for (Community i: connections.get(community)){
                if (!i.isFulfilled()){
                    unfulfilled++;
                }
            }
            return unfulfilled;
        }

    }
}
