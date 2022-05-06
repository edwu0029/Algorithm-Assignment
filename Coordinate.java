import java.util.Objects;
/**
 * [Coordinate.java]
 * A program that represents a coordinate pair.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, May 6, 2022
 */
public class Coordinate{
    /*----- Instance variables -----*/
    private int x;
    private int y;

    /**
     * Coordinate
     * A constructor that constructs a coordinate pair with a specified x value and y value.
     * @param x The x value of this coordinate
     * @param y The y value of this coordinate
     */
    Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    /**
     * getX
     * A method that returns the x value of this coordinate.
     * @return The x value of this coordinate.
     */
    public int getX(){
        return x;
    }
    /**
     * getY
     * A method that returns the y value of this coordinate.
     * @return The y value of this coordinate.
     */
    public int getY(){
        return y;
    }
    /*----- Overriden methods -----*/
    /**
     * hashCode
     * A overriden method that hashes this coordinate object into a integer.
     * @return A integer that represents this coordinate object.
     */
    @Override
    public int hashCode(){
        //Custom hash code for Coordinate objects
        return Objects.hash(x, y);
    }
}
