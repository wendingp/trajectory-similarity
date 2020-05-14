package uq.distance;

import uq.entities.Point;
import uq.services.DistanceService;

import java.util.ArrayList;

public class ERPDistanceCalculator implements SequenceDistanceCalculator {

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        return getERP(rClone, sClone);
    }

    private ArrayList<Point> normalization(ArrayList<Point> t) {
        ArrayList<Point> result = new ArrayList<>();

        if (t.size() == 0) {
            return result;
        }
        assert (t.get(0).dimension == 1);

        double mean = 0;
        for (Point item : t) {
            mean += item.coordinate[0];
        }
        mean /= t.size();

        double standardDeviation = 0;
        for (Point value : t) {
            standardDeviation += Math.pow(value.coordinate[0] - mean, 2);
        }
        standardDeviation = Math.sqrt(standardDeviation);

        for (Point point : t) {

            double[] tempCoordinate = point.coordinate;
            tempCoordinate[0] = (tempCoordinate[0] - mean) / standardDeviation;
            Point temp = new Point(tempCoordinate);
            result.add(temp);
        }

        return result;
    }

    private double getERP(ArrayList<Point> rO, ArrayList<Point> sO) {

        ArrayList<Point> r = normalization(rO);
        ArrayList<Point> s = normalization(sO);

        double[][] erpMetric = new double[r.size() + 1][s.size() + 1];

        double rAB = 0;
        for (Point value : r) {
            rAB += Math.abs(value.coordinate[0]);
        }

        double sAB = 0;
        for (Point point : s) {
            sAB += Math.abs(point.coordinate[0]);
        }

        for (int i = 0; i <= r.size(); i++) {
            erpMetric[i][0] = sAB;
        }
        for (int i = 0; i <= s.size(); i++) {
            erpMetric[0][i] = rAB;
        }

        erpMetric[0][0] = 0;

        for (int i = 1; i <= r.size(); i++) {
            for (int j = 1; j <= s.size(); j++) {
                erpMetric[i][j] = min(erpMetric[i - 1][j - 1] + subcost(r.get(i - 1), s.get(j - 1)),
                        erpMetric[i - 1][j] + Math.abs(r.get(i - 1).coordinate[0]),
                        erpMetric[i][j - 1] + Math.abs(s.get(j - 1).coordinate[0]));
            }
        }

        return erpMetric[r.size()][s.size()];
    }

    private double min(double a, double b, double c) {
        if (a <= b && a <= c) {
            return a;
        }
        return Math.min(b, c);
    }

    private double subcost(Point p1, Point p2) {
        return Math.abs(p1.coordinate[0] - p2.coordinate[0]);
    }

    public String toString() {
        return "ERP";
    }
}
