import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
/**
 * [Visualizer.java]
 * A program that visualizes the graph and graphical input.
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, April 25, 2022
 */
public class Visualizer extends JFrame{
    /*----- Constants -----*/
    final int MAX_X = (int)getToolkit().getScreenSize().getWidth();
    final int MAX_Y = (int)getToolkit().getScreenSize().getHeight();

    /*----- Instance variables -----*/
    private GraphPanel panel;
    private City city;

    private HashMap<Community, Coordinate> coordinates;
    private boolean lockedInput; //Lock user from inputting
    private Community selected;

    Visualizer(City city){
        this.panel = new GraphPanel();
        this.city = city;
        this.coordinates = new HashMap<Community, Coordinate>();
        this.lockedInput = false;

        //Set up JPanel
        this.panel.setBackground(Color.LIGHT_GRAY);
        this.getContentPane().add(BorderLayout.CENTER, panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(MAX_X/2, MAX_Y/2);
        this.setVisible(true);
    }
    /*----- GraphPanel Inner Class -----*/
    private class GraphPanel extends JPanel {
        private InputMouseListener mouseListener;
        private InputKeyListener keyListener;
        GraphPanel(){
            this.mouseListener = new InputMouseListener();
            this.keyListener = new InputKeyListener();
            this.addMouseListener(mouseListener);
            this.addKeyListener(keyListener);

            this.setFocusable(true);
            this.requestFocusInWindow(); 
        }
        @Override
        /**
         * paintComponent
         * A overriden method that draws the graph onto this GraphPanel.
         * @param g The graphics object that will be drawn with.
         */
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            //Draw communities
            ArrayList<Community> communities = city.getCommunities();
            for(Community i: communities){
                Coordinate centre = coordinates.get(i);
                //Draw border
                g.setColor(Color.BLACK);
                g.fillOval(centre.getX()-Const.RADIUS-Const.BORDER, centre.getY()-Const.RADIUS-Const.BORDER, 2*(Const.RADIUS+Const.BORDER), 2*(Const.RADIUS+Const.BORDER));
                if(i.getFireStation()){ //If a fire station, draw orange
                    g.setColor(Color.ORANGE);
                }else if(i.getSelected()){ //If selected, draw blue
                    g.setColor(Color.BLUE);
                }else if(i.getConnectedToFireStation()){ //If connected to fire station, draw gray
                    g.setColor(Color.GRAY);
                }
                else{ //Otherwise, draw white
                    g.setColor(Color.WHITE);
                }
                g.fillOval(centre.getX()-Const.RADIUS, centre.getY()-Const.RADIUS, 2*Const.RADIUS, 2*Const.RADIUS);
            }
            //Draw edges
            HashMap<Community, ArrayList<Community>>connections = city.getConnections();
            g.setColor(Color.BLACK);
            for(Community i:connections.keySet()){
                ArrayList<Community>nextNodes = connections.get(i);
                for(Community j:nextNodes){
                    Coordinate centreI = coordinates.get(i); //Graphical centre of community i
                    Coordinate centreJ = coordinates.get(j); //Graphical centre of community j
                    //Draw edge from node i's centre to node j's centre
                    g.drawLine(centreI.getX(), centreI.getY(), centreJ.getX(), centreJ.getY());
                }
            }

            //Draw instructions text
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            if(lockedInput){
                g.drawString("Input completed!", 10, 20);
            }else{
                g.drawString("Left click to add a community", 10, 20);
                g.drawString("To add a connection, click two existing communities", 10, 40);
                g.drawString("To finish the input, press ENTER on your keyboard", 10, 60);
            }
            this.repaint();
        }
    }
    /*----- MouseListener Inner Class -----*/
    public class InputMouseListener implements MouseListener{

        InputMouseListener(){
            selected = null;
        }
        public void mouseClicked(MouseEvent e){}
        public void mousePressed(MouseEvent e){
            if(lockedInput){ //If input is locked, ignore mouse event
                return;
            }else{
                //Check if mouse click is in community
                Community cityClicked = null;
                for(Community community: coordinates.keySet()){
                    Coordinate centre = coordinates.get(community);
                    int centreX = centre.getX();
                    int centreY = centre.getY();
                    //Check if distance between mouse and city's centre coordinate <= 2*graphical diameter of the city
                    //The 2*diameter is for a buffer to not allow communities to be placed too close to each other
                    if(Math.pow(centreX-e.getX(), 2)+Math.pow(centreY-e.getY(), 2)<Math.pow(4*Const.RADIUS, 2)){
                        cityClicked = community;
                    }
                }
                if(cityClicked==null){ //If no community is clicked, add a community
                    Community newCommunity = new Community();
                    city.addCommunity(newCommunity);
                    coordinates.put(newCommunity, new Coordinate(e.getX(), e.getY()));
                }else{
                    if(selected==null){ //If there is no selected, make this clickedCity selected
                        selected = cityClicked;
                        selected.setSelected(true);
                    }else{ //Otherwise make an edge with selected and clicked cities
                        selected.setSelected(false);
                        city.addConnection(selected, cityClicked);
                        selected = null;
                    }
                }
            }
        }
        public void mouseReleased(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
    }
    /*----- KeyListener Inner Class -----*/
    public class InputKeyListener implements KeyListener{
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode()==KeyEvent.VK_ENTER){ //If enter is typed, lock input
                lockedInput = true;
                city.fireStationSolve();
                //Calls city to solve where to put the fire stations
            }

        }
        public void keyTyped(KeyEvent e){}
        public void keyReleased(KeyEvent e){}
    }
}