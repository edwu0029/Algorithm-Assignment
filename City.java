import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * [City.java]
 * A program that represents a city of communities and connections.
 *
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, May 6, 2022
 */
public class City {
    /*----- Instance variables -----*/
    private final ArrayList<Community> communities;
    private final HashMap<Community, HashSet<Community>> connections;

    /**
     * City
     * A constructor that constructs a city.
     */
    City() {
        this.communities = new ArrayList<Community>();
        this.connections = new HashMap<Community, HashSet<Community>>();
    }

    /**
     * getCommunities
     * A method that returns a ArrayList of all the communities in this city.
     *
     * @return An ArrayList of all the communities in this city.
     */
    public ArrayList<Community> getCommunities() {
        return communities;
    }

    /**
     * getConnections
     * A method that returns all the connections between communities in this city.
     *
     * @return A HashMap of Hashsets that represents the connections in this city as an adjacency list.
     */
    public HashMap<Community, HashSet<Community>> getConnections() {
        return connections;
    }

    /**
     * addCommunity
     * A method that adds a new community to this city.
     *
     * @param newCommunity The new community that is to be added.
     */
    public void addCommunity(Community newCommunity) {
        communities.add(newCommunity);
        //Add this new community to adjacency list
        if (connections.containsKey(newCommunity) == false) {
            connections.put(newCommunity, new HashSet<Community>());
        }
    }

    /**
     * addConnection
     * A method that adds a connection between two communities in this city.
     *
     * @param end1 One of the community at the end of this connection.
     * @param end2 The other community at the other end of this connection.
     */
    public void addConnection(Community end1, Community end2) {
        //Add BIDIRECTIONAL connection from end1 to end2
        //Add from end1 to end2
        if (connections.containsKey(end1) == false) {
            connections.put(end1, new HashSet<Community>());
        }
        connections.get(end1).add(end2);
        //Add from end2 to end1
        if (connections.containsKey(end2) == false) {
            connections.put(end2, new HashSet<Community>());
        }
        connections.get(end2).add(end1);
    }

    /**
     * findCommunity
     * A method that finds and returns the community in this city with the specified communityID.
     *
     * @param communityID Community identifier
     * @return The community with the specified communityID, or null if not found
     */
    public Community findCommunity(int communityID) {
        for (Community i : communities) {
            if (i.getID() == communityID) {
                return i;
            }
        }
        return null;
    }

    public void fireStationSolve() {
        while (hasUncoveredCommunities()) {
            fireStationPlacer();
        }
    }

    /**
     * fireStationSolve
     * A method that performs one "step" of the fire station placer algorithm.
     */
    public void fireStationPlacer() {
        //If there are isolated communities, then make each of them a fire station
        //Isolated communities: Communities with no uncovered neighbour, but are themselves uncovered
        ArrayList<Community> isolatedCommunities = getCommunitiesWithNeighbour(0);
        for (Community i : isolatedCommunities) {
            addFireStation(i);
        }
        //If there are any leaf communities, make each of their only connected communities a fire station
        //Leaf communities: Communities with ONE uncovered neighbour, but are themselves uncovered
        ArrayList<Community> leafCommunities = getCommunitiesWithNeighbour(1);
        if (leafCommunities.size() > 0) { //There are leaf communities
            //Get leaf communities neighbours
            HashSet<Community> leafNeighbours = new HashSet<Community>(); // Use HashSet because some leaf communities might share the same neighbours
            for (Community i : leafCommunities) {
                //Consider corner case: If a leaf community is attached to another leaf community
                if (leafNeighbours.contains(i) == false) {
                    //Ensures that for the corner case above, only one is marked as a fire station later
                    leafNeighbours.addAll(getUncoveredNeighbours(i));
                }
            }
            //Add a fire station to each of these neighbours
            for (Community i : leafNeighbours) {
                addFireStation(i);
            }
        } else { //If there are NO leaf communities, pick the community with the maximum number of uncovered neighbours
            //Find a non-fire station community with the maximum number of uncovered neighbours, this could be an already covered community
            //If there is a tie, pick any
            Community optimalPick = null;
            int maxUncoveredNeighbours = 0;
            for (Community i : communities) {
                if (i.getFireStation() == false && getUncoveredNeighboursAmount(i) > maxUncoveredNeighbours) {
                    maxUncoveredNeighbours = getUncoveredNeighboursAmount(i);
                    optimalPick = i;
                }
            }
            if (optimalPick != null) { //If there is an optimal pick, add the fire station there
                addFireStation(optimalPick);
            }
        }
    }

    /**
     * getUncoveredNeighboursAmount
     * A method that returns the number of uncovered neighbours a specified community in this city has.
     *
     * @param community The specified community in this city
     * @return The number of uncovered neighbours this community has.
     */
    public int getUncoveredNeighboursAmount(Community community) {
        int uncovered = 0;
        if (connections.containsKey(community)) {
            for (Community i : connections.get(community)) {
                if (i.getCovered() == false) {
                    uncovered = uncovered + 1;
                }
            }
        }
        return uncovered;
    }

    /**
     * getCommunitiesWithNeighbours
     * A method that returns an ArrayList of all the communities in this city with a specified number
     * of uncovered neighbours.
     *
     * @param uncoveredNeighbours The number of uncovered neighbours.
     * @return An ArrayList of all the communities with the specified number of uncovered neighbours.
     */
    public ArrayList<Community> getCommunitiesWithNeighbour(int uncoveredNeighbours) {
        ArrayList<Community> communitiesWithNeighbours = new ArrayList<Community>();
        for (Community i : communities) {
            if (i.getCovered() == false && getUncoveredNeighboursAmount(i) == uncoveredNeighbours) { //Leaf communities have one connection
                communitiesWithNeighbours.add(i);
            }
        }
        return communitiesWithNeighbours;
    }

    /**
     * getUncoveredNeighbours
     * A method that returns a HashSet of all the uncovered neighbors of a specified community.
     *
     * @param community The specified community in this city.
     * @return A HashSet of the uncovered neighbours of the specified community.
     */
    public HashSet<Community> getUncoveredNeighbours(Community community) {
        HashSet<Community> neighbours = new HashSet<Community>();
        if (connections.containsKey(community)) {
            for (Community i : connections.get(community)) {
                if (i.getCovered() == false) {
                    neighbours.add(i);
                }
            }
        }
        return neighbours;
    }

    /**
     * addFireStation
     * A method that adds a fire station to a certain community in this city and updates its neighbours as covered
     * accordingly.
     *
     * @param community The community that a fire station is to be added to.
     */
    public void addFireStation(Community community) {
        //Make the community a fire station
        community.setFireStation(true);
        community.setCovered(true);
        //Update its neighbours as covered
        for (Community i : connections.get(community)) {
            i.setCovered(true);
        }
    }

    /**
     * hasUncoveredCommunities
     * A method that returns whether any uncovered communities in this city exist.
     *
     * @return true if there are still uncovered communities in this city, false if all communities are covered.
     */
    public boolean hasUncoveredCommunities() {
        for (Community i : communities) {
            if (i.getCovered() == false) {
                return true;
            }
        }
        return false;
    }
}