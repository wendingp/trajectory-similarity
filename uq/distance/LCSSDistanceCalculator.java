package uq.distance;

import uq.entities.Point;
import uq.services.DistanceService;

import java.util.ArrayList;

public class LCSSDistanceCalculator implements SequenceDistanceCalculator {

    private final double threshold;

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        return getLCSS(rClone, sClone);
    }

    public LCSSDistanceCalculator(double threshold) {
        this.threshold = threshold;
    }

    private double getLCSS(ArrayList<Point> r, ArrayList<Point> s) {
        double[][] LCSSMetric = new double[r.size() + 1][s.size() + 1];
        for (int i = 1; i <= r.size(); i++) {
            for (int j = 1; j <= s.size(); j++) {
                if (subcost(r.get(i - 1), s.get(j - 1)) == 0) {
                    LCSSMetric[i][j] = LCSSMetric[i - 1][j - 1] + 1;
                } else {
                    LCSSMetric[i][j] = Math.max(LCSSMetric[i][j - 1], LCSSMetric[i - 1][j]);
                }
            }
        }

        double tempR = LCSSMetric[r.size()][s.size()];

        // double result = 1 - (tempR / Math.min(r.size(), s.size())); // TODO ?

        return (Math.max(r.size(), s.size()) - tempR) / Math.max(r.size(), s.size());
    }

    private int subcost(Point p1, Point p2) {
        boolean isSame = true;
        for (int i = 0; i < p1.dimension; i++) {
            if (Math.abs(p1.coordinate[i] - p2.coordinate[i]) > threshold) {
                isSame = false;
                break;
            }
        }

        if (isSame) {
            return 0;
        }
        return 1;
    }

    public String toString() {
        return "LCSS";
    }
}
