import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

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

    // TODO account for communities with no connection
    // TODO write code for when there is no leaf community

    // Pick starting points randomly and start the one community algorithm?

    public void fireStationSolve (){
        //connections.forEach((key, value) -> System.out.println(key + ":" + value));
        while (!communitiesFulfilled()){

            //fulfillIsolated();

            if (leafCommunitiesExists()){
                ArrayList <Community> leafCommunities = getCommunitiesWithNeighbours(1);
                HashSet<Community> leafNeighbours = new HashSet<Community>(); // Use HashSet because some leaf communities might share the same neighbours
                for (Community i: leafCommunities){
                    leafNeighbours.add(getNeighbour(i));
                }
                fulfill(new ArrayList<>(leafNeighbours));

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

    public Boolean leafCommunitiesExists(){
        for (Community i: connections.keySet()){
            if (getNeighboursAmount(i) == 1){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Community> getCommunitiesWithNeighbours(int unfulfilledNeighbours){
        // Returns communities with specified number of unfulfilled neighbours
        ArrayList<Community> neighboursUnfulfilled = new ArrayList<Community>();

        for (Community i: this.communities){
            if (getNeighboursAmount(i) == unfulfilledNeighbours){
                neighboursUnfulfilled.add(i);
            }
        }

        return neighboursUnfulfilled;
    }

    public int getNeighboursAmount(Community community){
        // Returns the number of unfulfilled neighbours
        if (connections.containsKey(community)){
            int unfulfilled = 0;
            for (Community i: connections.get(community)){
                if (!i.isFulfilled()){
                    unfulfilled++;
                }
            }
            return unfulfilled;
        }
        else{
            return 0;
        }
    }

    public Community getNeighbour(Community community){
        // Returns neighbour for leaf community
        return getNeighbours(community).get(0);
    }

    public ArrayList<Community> getNeighbours(Community community){
        // Returns unfulfilled neighbours
        ArrayList<Community> neighbours = new ArrayList<Community>();
        if (connections.containsKey(community)){
            for (Community i: connections.get(community)){
                if (!i.isFulfilled()){
                    neighbours.add(i);
                }
            }
        }
        return neighbours;
    }

    public void fulfillIsolated(){
        for (Community i: this.communities){
            if (getNeighboursAmount(i) == 0){
                i.setFireStation(true);
                fulfilledCommunities++;
            }
        }
    }
    public void fulfill(ArrayList <Community> communities){
        // Add fire station
        for (Community i: communities){
            i.setFireStation(true);
            fulfilledCommunities++;
        }
        // Set neighbours as connect to fire station
        for (Community i: communities){
            for (Community o: getNeighbours(i)){
                o.setConnectedToFireStation(true);
                fulfilledCommunities++;
            }
        }
    }
}



