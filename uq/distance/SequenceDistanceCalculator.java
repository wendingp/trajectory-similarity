package uq.distance;

import java.util.*;

import uq.entities.Point;

public interface SequenceDistanceCalculator {
    double getDistance(ArrayList<Point> r, ArrayList<Point> s);
}
