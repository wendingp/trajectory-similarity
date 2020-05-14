/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.distance;

import uq.entities.Line;
import uq.entities.Point;
import uq.entities.ThreeDLine;
import uq.services.DistanceService;

import java.util.ArrayList;

/**
 * @author Administrator
 */
public class FrechetDistanceCalculator implements SequenceDistanceCalculator {

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // colon first so that make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        return getFrechet(rClone, sClone);
    }

    private double getFrechet(ArrayList<Point> r, ArrayList<Point> s) {
        assert (r.size() > 1 && s.size() > 1);
        double dR = r.get(r.size() - 1).timeLong - r.get(0).timeLong;
        double dS = s.get(s.size() - 1).timeLong - s.get(0).timeLong;

        double duration = Math.min(dR, dS);

        double calculateTimeInterval = duration / 1000;
        final int CALCULATE_COUNT = 1000;

        ArrayList<ThreeDLine> r3d = getThreeDLine(r);
        ArrayList<ThreeDLine> s3d = getThreeDLine(s);
        long rStartTime = r.get(0).timeLong;
        long sStartTime = s.get(0).timeLong;

        double max = 0;
        EuclideanDistanceCalculator euclidean = new EuclideanDistanceCalculator();

        for (int i = 0; i < CALCULATE_COUNT; i++) {
            Point rCurrentPoint;
            Point sCurrentPoint;

            int rCurrentLine = 0;
            int sCurrentLine = 0;

            for (int j = 0; j < r.size() - 1; j++) {
                if (rStartTime + i * calculateTimeInterval >= r.get(j).timeLong
                        && rStartTime + i * calculateTimeInterval <= r.get(j + 1).timeLong) {
                    rCurrentLine = j;
                    break;
                }
            }

            for (int j = 0; j < s.size() - 1; j++) {
                if (sStartTime + i * calculateTimeInterval >= s.get(j).timeLong
                        && sStartTime + i * calculateTimeInterval <= s.get(j + 1).timeLong) {
                    sCurrentLine = j;
                    break;
                }
            }

            rCurrentPoint = r3d.get(rCurrentLine).getPointByTime((long) (rStartTime + i * calculateTimeInterval));
            sCurrentPoint = s3d.get(sCurrentLine).getPointByTime((long) (sStartTime + i * calculateTimeInterval));

            max = Math.max(max, euclidean.getDistance(rCurrentPoint, sCurrentPoint));
        }

        return max;
    }

    private ArrayList<Line> getPolyline(ArrayList<Point> r) {
        ArrayList<Line> result = new ArrayList<>();

        if (r.size() < 2) {
            return result;
        }

        for (int i = 0; i < r.size() - 1; ++i) {
            result.add(new Line(r.get(i), r.get(i + 1)));
        }

        return result;
    }

    private ArrayList<ThreeDLine> getThreeDLine(ArrayList<Point> r) {
        ArrayList<ThreeDLine> result = new ArrayList<>();

        if (r.size() < 2) {
            return result;
        }

        for (int i = 0; i < r.size() - 1; i++) {
            result.add(new ThreeDLine(r.get(i), r.get(i + 1)));
        }

        return result;
    }

}
