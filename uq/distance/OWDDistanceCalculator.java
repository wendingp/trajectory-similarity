/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.distance;

import java.util.ArrayList;

import uq.entities.Line;
import uq.entities.Point;
import uq.services.DistanceService;

import static uq.distance.LIPDistanceCalculator.getLines;

/**
 * @author uqhsu1
 */
public class OWDDistanceCalculator implements SequenceDistanceCalculator {

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);

        return getOWD(r_clone, s_clone);
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
        for (int i = 0; i < t.size(); i++) {
            owd += minDistance(t.get(i), tt, ttl);
        }

        return owd / t.size();
    }

    private double minDistance(Point p, ArrayList<Point> t, ArrayList<Line> l) {
        EuclideanDistanceCalculator ed = new EuclideanDistanceCalculator();
        double min = ed.getDistance(p, t.get(0));

        for (int i = 0; i < t.size(); i++) {
            double temp = ed.getDistance(p, t.get(i));
            if (temp < min) {
                min = temp;
            }
        }

        for (int i = 0; i < l.size(); i++) {
            double temp = l.get(i).getPointLineDistance(p);
            if (temp < min) {
                min = temp;
            }
        }

        return min;
    }

}
