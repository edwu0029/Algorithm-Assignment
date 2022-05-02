/**
 * [Community.java]
 * A program that represents a community.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, April 25, 2022
 */
public class Community{
    /*----- Class variables -----*/
    private static int nextCommunityID = 0;

    /*----- Instance variables -----*/
    private final int cityID;
    private boolean selected;
    private boolean fireStation;

    Community(){
        this.cityID = nextCommunityID;
        nextCommunityID = nextCommunityID+1; //Update for next community ID 

        this.selected = false;
        this.fireStation = false;
    }
    public int getcity(){
        return cityID;
    }
    public boolean getSelected(){
        return selected;
    }
    public void setSelected(boolean newValue){
        selected = newValue;
    }
    public boolean getFireStation(){
        return fireStation;
    }
    public void setFireStation(boolean newValue){
        fireStation = newValue;
    }
}