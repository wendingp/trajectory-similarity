package uq.entities;

import java.util.Date;

public class Point implements Cloneable {

    public Date time = null;
    public double[] coordinate = null;
    public int dimension = 0;
    public String timeString = "";
    public long timeLong = 0L;


    public Point() {
    }

    public Point(double[] x, long t) {
        coordinate = new double[x.length];
        System.arraycopy(x, 0, coordinate, 0, x.length);
        dimension = x.length;
        timeLong = t;
        time = new Date(timeLong);
        timeString = time.toString();
    }

    public Point(double[] x) {
        coordinate = new double[x.length];
        System.arraycopy(x, 0, coordinate, 0, x.length);
        dimension = x.length;
    }

    public Point(double[] x, String s) {
        coordinate = new double[x.length];
        System.arraycopy(x, 0, coordinate, 0, x.length);
        dimension = x.length;
        timeString = s;
    }

    public Point(double[] x, Date y) {
        time = y;
        coordinate = new double[x.length];
        System.arraycopy(x, 0, coordinate, 0, x.length);
        dimension = x.length;
        timeString = y.toString();
        timeLong = y.getTime();
    }

    public void setDimension(int d) {
        dimension = d;
    }

    public boolean isSame(Point p) {
        final double EPSILON = 1e-7;
        if (p.dimension != dimension) {
            return false;
        }
        for (int i = 0; i < p.dimension; i++) {
            if (Math.abs(coordinate[i] - p.coordinate[i]) > EPSILON) {
//            if (this.coordinate[i] != p.coordinate[i]) { // fixed
                return false;
            }
        }
        return timeString.compareTo(p.timeString) == 0;
    }

    @Override
    public String toString() {
        return coordinate[0] + "-" + coordinate[1];
    }


    public double distanceTo(Point another) {
        double distance = 0.0;
        for (int count = 0; count < coordinate.length; ++count) {
            distance += Math.pow(coordinate[count] - another.coordinate[count], 2);
        }
        return Math.sqrt(distance);
    }

    /**
     * Makes an identical copy of this element
     */
    @Override
    public Point clone() {
        return new Point(coordinate, timeLong);
    }
}
