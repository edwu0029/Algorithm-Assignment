import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
/**
 * [FireStationPlanner.java]
 * A program that launches the fire station planner.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, May 6, 2022
 */
public class FireStationPlanner{
    public static void main(String args[]){
        City city = new City();
        Visualizer v = new Visualizer(city);
        //Wait until graphical input finishes
        while(v.getLockedInput()==false){
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(InterruptedException e){
                System.out.println("ERROR: Problem when delaying.");
                
            }
        }
        //Start solver
        while(city.hasUncoveredCommunities()){ //Keep doing one step of the solver until it is completed
            city.fireStationPlacer(); //Perform one step of the solver
            //Delay for visualization for the completed step
            try{
                TimeUnit.MILLISECONDS.sleep(1000);
            }catch(InterruptedException e){
                System.out.println("ERROR: Problem when delaying.");
            }
        }
    }
}
