package uq.distance;

import uq.entities.Point;

public class EuclideanDistanceCalculator implements PointDistanceCalculator {

    @Override
    public double getDistance(Point x, Point y) {
        assert (x.dimension == y.dimension);
        double sum = 0;
        for (int i = 0; i < x.dimension; ++i) {
            sum += Math.pow(x.coordinate[i] - y.coordinate[i], 2);
        }
        return Math.sqrt(sum);
    }
}
