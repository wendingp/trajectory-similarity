/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.distance;

import uq.entities.Line;
import uq.entities.Point;
import uq.entities.Polygon;
import uq.services.DistanceService;

import java.util.ArrayList;

import static uq.distance.LIPDistanceCalculator.getLines;

/**
 * @author uqhsu1, wendingp
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author uqhsu1
 */
public class STLIPDistanceCalculator implements SequenceDistanceCalculator {

    ArrayList<Polygon> polygon = null;
    ArrayList<Double> weight = null;
    ArrayList<Double> TLIP = null;
    double K = 0.5;

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        return getSTLIP(rClone, sClone);
    }

    public void setK(double k) {
        K = k;
    }

    private double getSTLIP(ArrayList<Point> r, ArrayList<Point> s) {
        double result = 0;
        polygon = getPolygon(r, s);
        for (int i = 0; i < polygon.size(); i++) {
            result += (polygon.get(i).getArea() * weight.get(i)) * (1 + K * TLIP.get(i));
        }
        return result;
    }

    private double getLength(ArrayList<Point> p) {
        double result = 0;

        EuclideanDistanceCalculator edc = new EuclideanDistanceCalculator();

        for (int i = 0; i < p.size() - 1; i++) {
            result += edc.getDistance(p.get(i), p.get(i + 1));
        }

        return result;
    }

    private ArrayList<Polygon> getPolygon(ArrayList<Point> r, ArrayList<Point> s) {

        ArrayList<Polygon> result = new ArrayList<>();

        weight = new ArrayList<>();
        TLIP = new ArrayList<>();

        double lengthR = getLength(r);
        double lengthS = getLength(s);

        assert (r.size() > 1);
        assert (s.size() > 1);

        double durationR = r.get(r.size() - 1).timeLong - r.get(0).timeLong;
        double durationS = s.get(s.size() - 1).timeLong - s.get(0).timeLong;

        ArrayList<Line> rl = getPolyline(r);
        ArrayList<Line> sl = getPolyline(s);

        ArrayList<Point> intersections = new ArrayList<>();
        ArrayList<twoInt> index = new ArrayList<>();

        boolean[] used = new boolean[sl.size()];

        for (int i = 0; i < rl.size(); i++) {
            for (int j = 0; j < sl.size(); j++) {
                if (used[j]) {
                    continue;
                }

                Point inter = rl.get(i).getIntersection(sl.get(j));
                if (inter == null) {
                    continue;
                }
                double x = inter.coordinate[0];
                double y = inter.coordinate[1];

                double r1x = r.get(i).coordinate[0];
                double r1y = r.get(i).coordinate[1];

                double r2x = r.get(i + 1).coordinate[0];
                double r2y = r.get(i + 1).coordinate[1];

                if (r1x > r2x) {
                    double temp = r1x;
                    r1x = r2x;
                    r2x = temp;
                }

                if (r1y > r2y) {
                    double temp = r1y;
                    r1y = r2y;
                    r2y = temp;
                }

                double s1x = s.get(j).coordinate[0];
                double s1y = s.get(j).coordinate[1];

                double s2x = s.get(j + 1).coordinate[0];
                double s2y = s.get(j + 1).coordinate[1];

                if (s1x > s2x) {
                    double temp = s1x;
                    s1x = s2x;
                    s2x = temp;
                }

                if (s1y > s2y) {
                    double temp = s1y;
                    s1y = s2y;
                    s2y = temp;
                }

                if (x >= r1x && x <= r2x && y >= r1y && y <= r2y && x >= s1x && x < s2x && y >= s1y && y <= s2y) {
                    double[] temp = new double[2];
                    temp[0] = x;
                    temp[1] = y;
                    Point tempP = new Point(temp);
                    twoInt tempI = new twoInt(i, j);

                    intersections.add(tempP);
                    index.add(tempI);
                    for (int k = 0; k <= j; k++) {
                        used[k] = true;
                    }

                    if (intersections.size() == 1) {
                        ArrayList<Point> tempPolyPoints = new ArrayList<>();

                        for (int ii = 0; ii <= i; ii++) {
                            tempPolyPoints.add(r.get(ii));
                        }

                        tempPolyPoints.add(tempP);

                        for (int ii = j; ii >= 0; ii--) {
                            tempPolyPoints.add(s.get(ii));
                        }

                        ArrayList<Point> tempR = new ArrayList<>();
                        for (int ii = 0; ii <= i; ii++) {
                            tempR.add(r.get(ii));
                        }

                        ArrayList<Point> tempS = new ArrayList<>();
                        for (int ii = 0; ii <= j; ii++) {
                            tempS.add(s.get(ii));
                        }

                        double lengthRSub = getLength(tempR);
                        double lengthSSub = getLength(tempS);

                        double weightV = (lengthRSub + lengthSSub) / (lengthR + lengthS);

                        double durationRTemp = r.get(i).timeLong - r.get(0).timeLong;
                        double durationSTemp = s.get(j).timeLong - s.get(0).timeLong;

                        double tlip = Math
                                .abs(1 - (2 * Math.max(durationRTemp, durationSTemp)) / (durationR + durationS));

                        TLIP.add(tlip);
                        weight.add(weightV);
                        result.add(new Polygon(tempPolyPoints));
                    } else {
                        ArrayList<Point> tempPolyPoints = new ArrayList<>();

                        tempPolyPoints.add(intersections.get(intersections.size() - 2));

                        for (int ii = index.get(index.size() - 2).x + 1; ii <= i; ii++) {
                            tempPolyPoints.add(r.get(ii));
                        }

                        tempPolyPoints.add(tempP);

                        for (int ii = j; ii >= index.get(index.size() - 2).y + 1; ii--) {
                            tempPolyPoints.add(s.get(ii));
                        }

                        ArrayList<Point> tempR = new ArrayList<>();
                        for (int ii = index.get(index.size() - 2).x + 1; ii <= i; ii++) {
                            tempR.add(r.get(ii));
                        }

                        ArrayList<Point> tempS = new ArrayList<>();
                        for (int ii = index.get(index.size() - 2).y + 1; ii <= j; ii++) {
                            tempS.add(s.get(ii));
                        }

                        double lengthRSub = getLength(tempR);
                        double lengthSSub = getLength(tempS);

                        double weightV = (lengthRSub + lengthSSub) / (lengthR + lengthS);

                        double durationRTemp = r.get(i).timeLong - r.get(index.get(index.size() - 2).x + 1).timeLong;
                        double durationSTemp = s.get(j).timeLong - s.get(index.get(index.size() - 2).y + 1).timeLong;

                        double tlip = Math
                                .abs(1 - (2 * Math.max(durationRTemp, durationSTemp)) / (durationR + durationS));

                        TLIP.add(tlip);
                        weight.add(weightV);
                        result.add(new Polygon(tempPolyPoints));
                    }
                }

            }
        }

        return result;

    }

    private ArrayList<Line> getPolyline(ArrayList<Point> r) {
        return getLines(r);
    }

}
