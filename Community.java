/**
 * [Community.java]
 * A program that represents a community.
 *
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, April 25, 2022
 */
public class Community {
    /*----- Instance variables -----*/
    private final int communityID;
    private boolean selected;
    private boolean fireStation;
    private boolean covered; //covered by a fire station or not
    private final Coordinate centre;

    /**
     * Community
     * A constructor that constructs a Community with a specific communityID and centre in the visualizer.
     * 
     * @param communityID The communityID of this community.
     * @param centre A coordinate of the centre of this community in the visualizer.
     */
    Community(int communityID, Coordinate centre) {
        this.communityID = communityID;
        this.centre = centre;
        this.selected = false;
        this.fireStation = false;
    }

    /**
     * getCentre
     * A method that returns the centre of this community in the visualizer.
     * 
     * @return A coordinate of the centre of this community.
     */
    public Coordinate getCentre() {
        return centre;
    }

    /**
     * getID
     * A method that returns the community ID of this community.
     * 
     * @return The communityID of this community.
     */
    public int getID() {
        return communityID;
    }

    /**
     * getSelected
     * A method that returns whether this community has been selected in the visualizer or not.
     * 
     * @return true if this community has been selected, false if it isn't
     */
    public boolean getSelected() {
        return selected;
    }

    /**
     * setSelected
     * A method that sets this communities selected variable.
     * 
     * @param newValue The new value for this communitiy's selected variable
     */
    public void setSelected(boolean newValue) {
        selected = newValue;
    }

    /**
     * getFireStation
     * A method that returns whether this community has a fire station or not.
     * 
     * @return true if this community has a fire station, false if it doesn't
     */
    public boolean getFireStation() {
        return fireStation;
    }

    /**
     * setFireStation
     * A method that sets this communities fireStation variable.
     * 
     * @param newValue The new value for this communitiy's fireStation variable
     */
    public void setFireStation(boolean newValue) {
        fireStation = newValue;
    }

    /**
     * getCovered
     * A method that returns whether this community has been covered or not.
     * 
     * @return true if this community is covered, false if it isn't
     */
    public boolean getCovered() {
        return covered;
    }
    
    /**
     * setCovered
     * A method that sets this communities covered variable.
     * 
     * @param newValue The new value for this communitiy's covered variable
     */
    public void setCovered(boolean newValue) {
        covered = newValue;
    }

}
