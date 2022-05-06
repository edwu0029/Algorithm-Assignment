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
    }
}
