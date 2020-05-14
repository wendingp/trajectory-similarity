/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.transformation;

import uq.entities.Point;

import java.util.ArrayList;

/**
 * @author uqhsu1
 */
public class RamerDouglasPeuchkerAlgorithm {
    private final ArrayList<Point> originalPoints = null;
    //
    // threshold should be decide carefully
    //
    private double threshold = 1;

    public RamerDouglasPeuchkerAlgorithm() {
    }

    public RamerDouglasPeuchkerAlgorithm(double t) {
        threshold = t;
    }

    public ArrayList<Point> getKeyPoints(ArrayList<Point> points) {
        ArrayList<Point> keyPoints = new ArrayList<>();
        /*
         * if(points.size()==0){ return new ArrayList<Point>(); }else
         * if(points.size()==1){ return points; }
         */
        double maxDistance = 0;
        int maxDistanceIndex = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            double distance = PerpendicularDistance(points.get(0), points.get(points.size() - 1), points.get(i));
            if (distance > maxDistance) {
                maxDistanceIndex = i;
                maxDistance = distance;
            }
        }

        if (maxDistance > threshold) {
            ArrayList<Point> leftPoints = getArrayList(points, 0, maxDistanceIndex + 1);
            ArrayList<Point> rightPoints = getArrayList(points, maxDistanceIndex, points.size() - maxDistanceIndex);
            ArrayList<Point> leftKeyPoints = getKeyPoints(leftPoints);
            ArrayList<Point> rightKeyPoints = getKeyPoints(rightPoints);
            keyPoints = mergeResult(leftKeyPoints, rightKeyPoints);
        } else {
            keyPoints.add(points.get(0));
            keyPoints.add(points.get(points.size() - 1));
        }
        return keyPoints;
    }

    private double PerpendicularDistance(Point start, Point end, Point p) {

        assert (start.dimension == 2);
        assert (end.dimension == 2);
        assert (p.dimension == 2);

        double x1 = start.coordinate[0];
        double y1 = start.coordinate[1];
        double x2 = end.coordinate[0];
        double y2 = end.coordinate[1];
        double x3 = p.coordinate[0];
        double y3 = p.coordinate[1];

        if (x1 == x2) {
            return Math.abs(x3 - x1);
        }

        double k = (y1 - y2) / (x1 - x2);
        double b = (x1 * y2 - x2 * y1) / (x1 - x2);

        double A = k, B = -1, C = b;

        return Math.abs((A * x3 + B * y3 + C) / Math.sqrt(A * A + B * B));
    }

    private ArrayList<Point> mergeResult(ArrayList<Point> left, ArrayList<Point> right) {
        ArrayList<Point> result = new ArrayList<>(left);

        for (int i = 1; i < right.size(); i++) { // TODO i = 1 bug?
            result.add(right.get(i));
        }

        return result;
    }

    private ArrayList<Point> getArrayList(ArrayList<Point> list, int start, int length) {
        ArrayList<Point> subListCopy = new ArrayList<>();
        for (int i = start; i < start + length; i++) {
            subListCopy.add(list.get(i));
        }
        // return list.subList(start, start + length).clone();
        return subListCopy;
    }

    public void setThreshold(double t) {
        threshold = t;
    }
}
