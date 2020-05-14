/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.distance;

import java.util.ArrayList;

import uq.entities.Point;
import uq.services.DistanceService;

/**
 * @author Administrator
 */
public class STLCSSDistanceCalculator implements SequenceDistanceCalculator {

    double Distance;
    double Time;
    long startTime1 = 0L;
    long startTime2 = 0L;

    public STLCSSDistanceCalculator(double distanceThreshold, long timeIntervalThreshold) {
        Distance = distanceThreshold;
        Time = timeIntervalThreshold;
    }

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        // Time range (parameters - given)
        Time = getTimeEnd(rClone, sClone);
        startTime1 = rClone.get(0).timeLong;
        startTime2 = sClone.get(0).timeLong;

        return getSTLCSS(rClone, sClone);
    }

    private double getSTLCSS(ArrayList<Point> r, ArrayList<Point> s) {

        double[][] LCSSMetric = new double[r.size() + 1][s.size() + 1];

        for (int i = 0; i <= r.size(); i++) {
            LCSSMetric[i][0] = 0;
        }
        for (int i = 0; i <= s.size(); i++) {
            LCSSMetric[0][i] = 0;
        }

        for (int i = 1; i <= r.size(); i++) {
            for (int j = 1; j <= s.size(); j++) {
                if (subcost(r.get(i - 1), s.get(j - 1)) == 1) {
                    LCSSMetric[i][j] = LCSSMetric[i - 1][j - 1] + 1;
                } else {
                    LCSSMetric[i][j] = Math.max(LCSSMetric[i][j - 1], LCSSMetric[i - 1][j]);
                }
            }
        }

        double lcss = LCSSMetric[r.size()][s.size()];

        return 1 - (lcss / Math.min(r.size(), s.size()));
    }

    private int subcost(Point p1, Point p2) {
        boolean isSame = true;
        for (int i = 0; i < p1.dimension; i++) {
            if (Math.abs(p1.coordinate[i] - p2.coordinate[i]) > Distance) {
                isSame = false;
                break;
            }
        }

        if (Math.abs((p1.timeLong - startTime1) - (p2.timeLong - startTime2)) > Time) {
            isSame = false;
        }

        if (isSame) {
            return 1;
        }
        return 0;
    }

    /**
     * Get final time period tn
     */
    private long getTimeEnd(ArrayList<Point> r, ArrayList<Point> s) {
        // Get the trajectory with earliest last point
        return Math.min(s.get(s.size() - 1).timeLong, r.get(r.size() - 1).timeLong);
    }
}
