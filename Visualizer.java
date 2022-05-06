import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * [Visualizer.java]
 * A program that visualizes the city, communities and graphical input.
 *
 * @author Edward Wu and Yi Chun Jin
 * @version 1.0, May 6, 2022
 */
public class Visualizer extends JFrame {
    /*----- Screen Size Constants -----*/
    final int MAX_X = (int) getToolkit().getScreenSize().getWidth();
    final int MAX_Y = (int) getToolkit().getScreenSize().getHeight();

    /*----- Instance variables -----*/
    private final CityPanel panel;
    private final City city;
    ArrayList<Community> communities;
    private final HashMap<Community, HashSet<Community>> connections;
    private boolean lockedInput; //Lock user from inputting
    private Community selected;
    private int nextCommunityID = 1;

    /**
     * Visualizer
     * A constructor that constructs a visualizer for a specified city.s
     * 
     * @param city The city which this visualizer will display
     */
    Visualizer(City city) {
        this.panel = new CityPanel();
        this.add(panel);
        this.city = city;
        this.communities = city.getCommunities();
        this.connections = city.getConnections();
        this.lockedInput = false;

        this.loadTemplate();

        //Set up JPanel
        this.panel.setBackground(Color.LIGHT_GRAY);
        this.getContentPane().add(BorderLayout.CENTER, panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(MAX_X / 2, MAX_Y / 2);
        this.setVisible(true);
    }

    /**
     * getLockedInput
     * A method that returns whether the visualizer has locked input or not.
     *
     * @return true if input is locked, false if input is still going on.
     */
    public boolean getLockedInput() {
        return lockedInput;
    }

    /**
     * loadTemplate
     * A method that loads a template/demo into the visualizer.
     */
    public void loadTemplate() {
        String message = "Enter the name of the template file you want to load. ";
        String defaultTemplate = "demo1.txt";
        String fileName = JOptionPane.showInputDialog(message, defaultTemplate);
        try {
            if (fileName != null) { // If user did not click on cancel
                Scanner fileInput = new Scanner(new File(fileName));
                int numberOfCommunities = fileInput.nextInt();
                int numberOfConnections = fileInput.nextInt();
                //Get community locations from template
                for (int i = 0; i < numberOfCommunities; i++) {
                    int x = fileInput.nextInt();
                    int y = fileInput.nextInt();
                    Community newCommunity = new Community(nextCommunityID, new Coordinate(x, y));
                    city.addCommunity(newCommunity);
                    nextCommunityID = nextCommunityID + 1;
                }
                //Get connections from template
                for (int i = 0; i < numberOfConnections; i++) {
                    int communityID1 = fileInput.nextInt();
                    int communityID2 = fileInput.nextInt();
                    Community c1 = city.findCommunity(communityID1);
                    Community c2 = city.findCommunity(communityID2);
                    city.addConnection(c1, c2);
                }
                fileInput.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading in template");
            JOptionPane.showMessageDialog(null, "An error has occurred while trying to load template file " + fileName + ". ", "Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    /*----- CityPanel Inner Class -----*/
    private class CityPanel extends JPanel {
        private final InputMouseListener mouseListener;
        private final InputKeyListener keyListener;

        /**
         * CityPanel
         * A constructor that constructs a CityPanel.
         */
        CityPanel() {
            this.mouseListener = new InputMouseListener();
            this.keyListener = new InputKeyListener();
            this.addMouseListener(mouseListener);
            this.addKeyListener(keyListener);

            //Set this CityPanel as visible in the Visualizer
            this.setFocusable(true);
            this.requestFocusInWindow();
        }

        /**
         * paintComponent
         * A overriden method that draws the city onto this CityPanel.
         *
         * @param g The graphics object that will be drawn with.
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //Draw connections

            g.setColor(Color.BLACK);
            for (Community i : connections.keySet()) {
                HashSet<Community> nextNodes = connections.get(i);
                for (Community j : nextNodes) {
                    Coordinate centreI = i.getCentre(); //Graphical centre of community i
                    Coordinate centreJ = j.getCentre(); //Graphical centre of community j
                    //Draw edge from node i's centre to node j's centre
                    g.drawLine(centreI.getX(), centreI.getY(), centreJ.getX(), centreJ.getY());
                }
            }
            //Draw communities
            for (Community i : communities) {
                Coordinate centre = i.getCentre();
                //Draw border for communities
                g.setColor(Color.BLACK);
                g.fillOval(centre.getX() - Const.RADIUS - Const.BORDER, centre.getY() - Const.RADIUS - Const.BORDER, 2 * (Const.RADIUS + Const.BORDER), 2 * (Const.RADIUS + Const.BORDER));
                if (i.getFireStation()) { //If a fire station, draw orange
                    g.setColor(Color.ORANGE);
                } else if (i.getSelected()) { //If selected, draw blue
                    g.setColor(Color.BLUE);
                } else if (i.getCovered()) { //If connected to fire station, draw gray
                    g.setColor(Color.GRAY);
                } else { //Otherwise, draw white
                    g.setColor(Color.WHITE);
                }
                g.fillOval(centre.getX() - Const.RADIUS, centre.getY() - Const.RADIUS, 2 * Const.RADIUS, 2 * Const.RADIUS);
            }
            //Draw instructions text
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            if (lockedInput) {
                if (city.hasUncoveredCommunities()) {
                    g.drawString("press SHIFT to show next step, press ENTER to skip to final result. ", 10, 20);
                } else {
                    g.drawString("All communities covered!", 10, 20);
                }
            } else {
                g.drawString("Left click to add a community.", 10, 20);
                g.drawString("To add a connection, click two existing communities.", 10, 40);
                g.drawString("When finished, press N to show steps. ", 10, 60);
            }
            this.repaint();
        }
    }

    /*----- MouseListener Inner Class -----*/
    public class InputMouseListener implements MouseListener {
        /**
         * InputMouseListener
         * A constructor that constructs a graphical input mouse listener.
         */
        InputMouseListener() {
            selected = null;
        }

        public void mouseClicked(MouseEvent e) {
        }

        /**
         * mousePressed
         * A method that adds a community at the location of mouse press, or adds a connection depending on if mouse
         * press was in a community.
         *
         * @param e The MouseEvent triggered by the mouse press.
         */
        public void mousePressed(MouseEvent e) {
            if (lockedInput == false) { //If input is locked, ignore mouse event
                //Check if mouse click is in community
                Community cityClicked = null;
                for (Community community : communities) {
                    Coordinate centre = community.getCentre();
                    int centreX = centre.getX();
                    int centreY = centre.getY();
                    //Check if distance between mouse and city's centre coordinate <= 2*graphical diameter of the city
                    if (Math.pow(centreX - e.getX(), 2) + Math.pow(centreY - e.getY(), 2) < Math.pow(2 * Const.RADIUS, 2)) {
                        cityClicked = community;
                    }
                }
                if (cityClicked == null) { //If no community is clicked, add a community
                    Community newCommunity = new Community(nextCommunityID, new Coordinate(e.getX(), e.getY()));
                    nextCommunityID = nextCommunityID + 1;
                    city.addCommunity(newCommunity);
                } else {
                    if (selected == null) { //If there is no selected, make this clickedCity selected
                        selected = cityClicked;
                        selected.setSelected(true);
                    } else { //Otherwise, make an edge with selected and clicked cities
                        selected.setSelected(false);
                        city.addConnection(selected, cityClicked);
                        selected = null;
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    /*----- KeyListener Inner Class -----*/
    public class InputKeyListener implements KeyListener {
        /**
         * keyPressed
         * A method that the visualizer input if ENTER is pressed.
         *
         * @param e The KeyEvent triggered by a key press.
         */
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { //If enter is typed, lock input
                lockedInput = true;
                city.fireStationSolve();
            }
            if (e.getKeyCode() == KeyEvent.VK_N) {
                lockedInput = true;
                city.fireStationPlacer();
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }
}