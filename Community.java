/**
 * [Community.java]
 * A program that represents a community.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, April 25, 2022
 */
public class Community{
    /*----- Instance variables -----*/
    private final int communityID;
    private boolean selected;
    private boolean fireStation;
    private boolean covered; //covered by a firestation or not

    Community(int communityID){
        this.communityID = communityID;

        this.selected = false;
        this.fireStation = false;
    }
    public int getID(){
        return communityID;
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
    public boolean getCovered(){
        return covered;
    }
    public void setCovered(boolean newValue){
        covered = newValue;
    }

}
