import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * [Graph.java]
 * A program that represents a graph of communities and connections.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, April 25, 2022
 */
public class City {
    /*----- Instance variables -----*/
    private ArrayList<Community>communities;
    private HashMap<Community, HashSet<Community>> connections;

    City(){
        this.communities = new ArrayList<Community>();
        this.connections = new HashMap<Community, HashSet<Community>>();
    }
    public ArrayList<Community>getCommunities(){
        return communities;
    }
    public HashMap<Community, HashSet<Community>> getConnections(){
        return connections;
    }
    public void addCommunity(Community newCommunity){
        communities.add(newCommunity);
    }
    public void addConnection(Community start, Community end){
        //Add bidirectional connection from start to end
        //Add from start to end
        if(!connections.containsKey(start)){
            connections.put(start, new HashSet<Community>());
        }
        connections.get(start).add(end);
        //Add from end to start
        if(!connections.containsKey(end)){
            connections.put(end, new HashSet<Community>());
        }
        connections.get(end).add(start);
    }
    public void fireStationSolve(){
        while(hasUncoveredCommunities()){
            //If there are isolated communities, then make each of them a fire station
            //Isolated communities: Communities with no uncovered neighbour, but are themselves uncovered
            ArrayList<Community>isolatedCommunities = getCommunitiesWithNeighbour(0);
            for(Community i: isolatedCommunities){
                addFireStation(i);
            }
            //If there are any leaf communities, make each of their only connected communities a fire station
            //Leaf communities: Communities with ONE uncovered neighbour, but are themselves uncovered
            ArrayList<Community>leafCommunities = getCommunitiesWithNeighbour(1);
            if(leafCommunities.size()>0){ //There are leaf communities
                //Get leaf communities neighbours
                HashSet<Community>leafNeighbours = new HashSet<Community>(); // Use HashSet because some leaf communities might share the same neighbours
                for(Community i: leafCommunities){
                    //Consider corner case: If a leaf community is attached to another leaf community
                    if(!leafNeighbours.contains(i)){
                        //Ensures that for the corner case above, only one is marked as a fire station later
                        leafNeighbours.addAll(getUncoveredNeighbours(i));
                    }
                }
                //Add a fire station to each of these neighbours
                for(Community i: leafNeighbours){
                    addFireStation(i);
                }
                //Consider corner case: If a leaf node is connected to another leaf node
            } else { //If there are NO leaf communities, pick the community with the maximum number of uncovered neighbours
                //Find a non-fire station community with the maximum number of uncovered neighbours, this could be a already covered community
                //If there is a tie, pick any
                Community optimalPick = null;
                int maxUncoveredNeighbours = 0;
                for(Community i: communities){
                    if(!i.getFireStation() && getUncoveredNeighboursAmount(i)>maxUncoveredNeighbours){
                        maxUncoveredNeighbours = getUncoveredNeighboursAmount(i);
                        optimalPick = i;
                    }
                }
                if(optimalPick!=null){ //If there is a optimal pick, add the fire station there
                    addFireStation(optimalPick);
                }
            }
        }
    }
    public int getUncoveredNeighboursAmount(Community community){
        int uncovered = 0;
        if(connections.containsKey(community)){
            for(Community i: connections.get(community)){
                if(!i.getCovered()){
                    uncovered = uncovered+1;
                }
            }
        }
        return uncovered;
    }
    public ArrayList<Community> getCommunitiesWithNeighbour(int uncoveredNeighbours){
        ArrayList<Community>communitiesWithNeighbours = new ArrayList<Community>();
        for(Community i: communities){
            if(!i.getCovered() && getUncoveredNeighboursAmount(i)==uncoveredNeighbours){ //Leaf communities have one connection
                communitiesWithNeighbours.add(i);
            }
        }
        return communitiesWithNeighbours;
    }
    public HashSet<Community> getUncoveredNeighbours(Community community){
        HashSet<Community> neighbours = new HashSet<Community>();
        if(connections.containsKey(community)){
            for(Community i: connections.get(community)){
                if(!i.getCovered()){
                    neighbours.add(i);
                }
            }
        }
        return neighbours;
    }
    public void addFireStation(Community community){
        if(community==null){
            return;
        }
        //Make the community a firestation
        community.setFireStation(true);
        community.setCovered(true);
        //Update its neighbours as covered
        for(Community i: connections.get(community)){
            i.setCovered(true);
        }
    }
    public boolean hasUncoveredCommunities(){
        for(Community i: communities){
            if(!i.getCovered()){
                return true;
            }
        }
        return false;
    }
    
}



