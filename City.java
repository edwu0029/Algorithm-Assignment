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
        this.fulfilledCommunities = 1;
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
        //Add bidirectional connection from start to end
        //start to end
        if(!connections.containsKey(start)){
            connections.put(start, new ArrayList<Community>());
        }
        connections.get(start).add(end);
        //end to start
        if(!connections.containsKey(end)){
            connections.put(end, new ArrayList<Community>());
        }
        connections.get(end).add(start);
    }
    public void fireStationSolve (){
        connections.forEach((key, value) -> System.out.println(key + ":" + value));
        while (!communitiesFulfilled()){
            //System.out.println("Not all communities are fulfilled");
            if (leafCommunitiesExist()){
                //System.out.println("Leaf communities exists");
                for (Community i: getCommunitiesWithNeighbours(1)){

                    for (Community o: getNeighbours(i)){
                        addFireStation(o);
                    }

                    //System.out.println("set fire station");
                }
            }
            else{

            }
        }

    }

    public Boolean communitiesFulfilled(){
        System.out.println("Fulfilled: "+fulfilledCommunities);
        System.out.println("Total: "+totalCommunities);
        return fulfilledCommunities == totalCommunities;
    }

    public Boolean leafCommunitiesExist(){
        for (Community i: connections.keySet()){
            if (getNeighboursAmount(i) == 1){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Community> getCommunitiesWithNeighbours(int unfulfilledNeighbours){
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
                if (getNeighboursAmount(i) == unfulfilledNeighbours){
                    neighboursUnfulfilled.add(i);
                }
            }
        }
        return neighboursUnfulfilled;
    }

    public int getNeighboursAmount(Community community){
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


    public ArrayList<Community> getNeighbours(Community community){
        ArrayList<Community> neighbours = new ArrayList<Community>();
        if (connections.containsKey(community)){
            System.out.println(getNeighboursAmount(community));
            for (Community i: connections.get(community)){
                if (!i.isFulfilled()){
                    neighbours.add(i);
                }
            }
        }
        return neighbours;
    }

    public void addFireStation(Community community){
        community.setFireStation(true);
        fulfilledCommunities++;
        for (Community i: getNeighbours(community)){
            i.setConnectedToFireStation(true);
            fulfilledCommunities++;
        }
    }
}
