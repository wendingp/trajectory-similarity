package uq.distance;

import uq.entities.Point;
import uq.services.DistanceService;

import java.util.ArrayList;

/**
 * Dynamic Time Warping distance measure.
 *
 * @author uqhsu1, h.wang16, uqdalves wendingp
 */
public class DTWDistanceCalculator implements SequenceDistanceCalculator {

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
        ArrayList<Point> s_clone = DistanceService.clonePointsList(s);

        return getDTW(r_clone, s_clone);
    }

    private double getDTW(ArrayList<Point> r, ArrayList<Point> s) {
//        if (r.size() > s.size() || r.size() < s.size()) {
//            int i = -1;
//        }
        double[][] dist = new double[r.size() + 1][s.size() + 1];

        // initialize the dynamic programming seeds
        for (int i = 0; i <= r.size(); ++i) {
            dist[i][0] = Double.MAX_VALUE;
        }
        for (int j = 0; j <= s.size(); ++j) {
            dist[0][j] = Double.MAX_VALUE;
        }
        dist[r.size()][s.size()] = 0;

        EuclideanDistanceCalculator euclideanDistanceCalculator = new EuclideanDistanceCalculator();
        for (int i = r.size() - 1; i >= 0; --i) {
            for (int j = s.size() - 1; j >= 0; --j) {
                double edd = euclideanDistanceCalculator.getDistance(r.get(i), s.get(j));
                dist[i][j] = edd + Math.min(dist[i + 1][j + 1], Math.min(dist[i + 1][j], dist[i][j + 1]));
            }
        }
        return dist[0][0];
    }

    public String toString() {
        return "DTW";
    }
}
