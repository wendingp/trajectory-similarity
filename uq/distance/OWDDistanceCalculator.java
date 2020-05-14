/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.distance;

import uq.entities.Line;
import uq.entities.Point;
import uq.services.DistanceService;

import java.util.ArrayList;

import static uq.distance.LIPDistanceCalculator.getLines;

/**
 * @author uqhsu1
 */
public class OWDDistanceCalculator implements SequenceDistanceCalculator {

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        return getOWD(rClone, sClone);
    }

    private ArrayList<Line> getPolyline(ArrayList<Point> r) {
        return getLines(r);
    }

    private double getOWD(ArrayList<Point> r, ArrayList<Point> s) {
        ArrayList<Line> rl = getPolyline(r);
        ArrayList<Line> sl = getPolyline(s);

        double rOWD = OWD(r, s);
        double sOWD = OWD(s, r);

        return (rOWD + sOWD) / 2;

    }

    private double OWD(ArrayList<Point> t, ArrayList<Point> tt) {
        ArrayList<Line> ttl = getPolyline(tt);

        double owd = 0;
        for (Point point : t) {
            owd += minDistance(point, tt, ttl);
        }

        return owd / t.size();
    }

    private double minDistance(Point p, ArrayList<Point> t, ArrayList<Line> l) {
        EuclideanDistanceCalculator ed = new EuclideanDistanceCalculator();
        double min = ed.getDistance(p, t.get(0));

        for (Point point : t) {
            double temp = ed.getDistance(p, point);
            if (temp < min) {
                min = temp;
            }
        }

        for (Line line : l) {
            double temp = line.getPointLineDistance(p);
            if (temp < min) {
                min = temp;
            }
        }

        return min;
    }
}
