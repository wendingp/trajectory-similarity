package uq.distance;

import uq.entities.Point;
import uq.services.DistanceService;

import java.util.ArrayList;

public class LCSSForMultiDimensionDistanceCalculator implements SequenceDistanceCalculator {

    double theta;
    double epsilon;

    public LCSSForMultiDimensionDistanceCalculator(double t, double e) {
        theta = t;
        epsilon = e;
    }

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);
        return getLCSS(rClone, sClone);
    }

    private double getLCSS(ArrayList<Point> r, ArrayList<Point> s) {

        double[][] LCSSForMultiDimensionMetric = new double[r.size() + 1][s.size() + 1];
        for (int i = 1; i <= r.size(); i++) {
            for (int j = 1; j <= s.size(); j++) {
                if (subcost(r.get(i - 1), s.get(j - 1), i, j) == 0) {
                    LCSSForMultiDimensionMetric[i][j] = LCSSForMultiDimensionMetric[i - 1][j - 1] + 1;
                } else {
                    LCSSForMultiDimensionMetric[i][j] = Math.max(LCSSForMultiDimensionMetric[i][j - 1],
                            LCSSForMultiDimensionMetric[i - 1][j]);
                }
            }
        }
        return LCSSForMultiDimensionMetric[r.size()][s.size()];
    }

    private int subcost(Point p1, Point p2, int i, int j) {
        if (Math.abs(i - j) > theta) {
            return 1;
        }
        for (int index = 0; index < p1.dimension; index++) {
            if (Math.abs(p1.coordinate[index] - p2.coordinate[index]) > epsilon) {
                return 1;
            }
        }
        return 0;
    }
}
