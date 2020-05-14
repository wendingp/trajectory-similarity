package uq.distance;

import uq.entities.Point;
import uq.services.DistanceService;

import java.util.ArrayList;
import java.util.List;

public class EuclideanDistanceForMultiDimensionTimeSeriesCalculator implements SequenceDistanceCalculator {

    EuclideanDistanceCalculator EDC;

    public EuclideanDistanceForMultiDimensionTimeSeriesCalculator() {
        EDC = new EuclideanDistanceCalculator();
    }

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        return getEDC(rClone, sClone);
    }

    private double getEDC(ArrayList<Point> r, ArrayList<Point> s) {

        if (r.size() == 0 && s.size() == 0) {
            return 0;
        }
        if (r.size() == 0 || s.size() == 0) {
            return Double.MAX_VALUE;
        }

        List<Point> longT;
        List<Point> shortT;
        if (r.size() < s.size()) {
            shortT = r;
            longT = s;
        } else {
            shortT = s;
            longT = r;
        }
        int k = shortT.size();
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < longT.size() - shortT.size() + 1; i++) {
            double tempResult = 0;
            for (int j = 0; j < k; j++) {
                tempResult += EDC.getDistance(shortT.get(j), longT.get(j + i));
            }
            tempResult /= k;
            minDistance = Math.min(minDistance, tempResult);
        }
        return minDistance;
    }

    @Override
    public String toString() {
        return "ED with MultiDimension";
    }

}
