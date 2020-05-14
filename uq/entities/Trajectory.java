package uq.entities;

import java.util.ArrayList;

/**
 * @author uqdalves
 */
public class Trajectory {
    // the list of Points that compose the trajectory
    private ArrayList<Point> pointsList = new ArrayList<>();

    /**
     * The list of Points of this trajectory.
     */
    public ArrayList<Point> getPointsList() {
        return pointsList;
    }

    public void setPointsList(ArrayList<Point> pointsList) {
        this.pointsList = pointsList;
    }

    /**
     * Add a Point to the trajectory (end).
     */
    public void addPoint(Point point) {
        pointsList.add(point);
    }

    /**
     * The number of points in this trajectory.
     */
    public int numberOfPoints() {
        return pointsList.size();
    }

    /**
     * Makes an identical copy of this element
     */
    @Override
    public Trajectory clone() {
        Trajectory tClone = new Trajectory();

        for (Point p : pointsList) {
            Point newP = p.clone();
            tClone.addPoint(newP);
        }

        return tClone;
    }
}
