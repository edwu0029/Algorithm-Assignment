import java.util.Objects;
/**
 * [Coordinate.java]
 * A program that represents a coordinate pair.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, April 25, 2022
 */
public class Coordinate {
    /*----- Instance variables -----*/
    private int x;
    private int y;
    Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    /*----- Overriden methods -----*/
    @Override
    public int hashCode(){
        //Custom hash code for Coordinate objects
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
