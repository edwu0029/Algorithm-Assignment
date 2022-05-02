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
    private ArrayList<Community>communities;
    private HashMap<Community, ArrayList<Community>>adjacencyList;

    City(){
        this.totalCommunities = 0;
        this.communities = new ArrayList<Community>();
        this.adjacencyList = new HashMap<Community, ArrayList<Community>>();
    }
    public ArrayList<Community>getCommunities(){
        return communities;
    }
    public HashMap<Community, ArrayList<Community>> getAdjacencyList(){
        return adjacencyList;
    }
    public void addCommunity(Community newCommunity){
        communities.add(newCommunity);
        totalCommunities = totalCommunities+1;
    }
    public void addEdge(Community start, Community end){
        //NEED to add checker for if start and end Citys are in the graph
        if(!adjacencyList.containsKey(start)){
            adjacencyList.put(start, new ArrayList<Community>());
        }
        adjacencyList.get(start).add(end);
    }
}
