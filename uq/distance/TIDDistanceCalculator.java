/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.distance;

import java.util.ArrayList;

import uq.entities.Point;
import uq.services.DistanceService;

/**
 * @author uqhsu1
 */
public class TIDDistanceCalculator implements SequenceDistanceCalculator {
    ArrayList<Point> r = null;
    ArrayList<Point> s = null;
    double[] angle = new double[] {0, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340};

    @Override
    public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
        // make sure the original objects will not be changed
        ArrayList<Point> rClone = DistanceService.clonePointsList(r);
        ArrayList<Point> sClone = DistanceService.clonePointsList(s);

        return getTID(rClone, sClone);
    }

    private double getTID(ArrayList<Point> rr, ArrayList<Point> ss) {
        r = this.normalization(rr);
        s = this.normalization(ss);

        DTWDistanceCalculator d = new DTWDistanceCalculator();
        double[] tempResult = new double[16];
        for (int i = 0; i < 16; i++) {
            ArrayList<Point> tt = getTransformation(angle[i]);
            tempResult[i] = d.getDistance(tt, s);

        }

        return min(tempResult);

    }

    private ArrayList<Point> getTransformation(double rotationAngle) {
        double cos = Math.cos(rotationAngle);
        double sin = Math.sin(rotationAngle);

        ArrayList<Point> ret = new ArrayList<>(r.size());

        for (int i = 0; i < r.size(); i++) {
            assert (r.get(i).dimension == 2);

            double[] cor = new double[2];
            cor[0] = cos * r.get(i).coordinate[0] - sin * r.get(i).coordinate[1];
            cor[1] = sin * r.get(i).coordinate[0] + cos * r.get(i).coordinate[1];

            ret.add(new Point(cor, r.get(i).timeLong));
        }

        return ret;
    }

    private double min(double[] r) {
        double min = r[0];
        for (double v : r) {
            if (v < min) {
                min = v;
            }
        }

        return min;
    }

    private int[] sort(double[] list) {
        int[] result = new int[list.length];
        boolean[] mark = new boolean[list.length];

        for (int i = 0; i < mark.length; i++) {
            mark[i] = true;
            result[i] = -1;
        }
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            double max = -1;
            int index = -1;
            for (int j = 0; j < list.length; j++) {
                if (mark[j]) {
                    if (max == -1) {
                        max = list[j];
                        index = j;
                    } else if (max < list[j]) {
                        max = list[j];
                        index = j;
                    }
                }
            }
            mark[index] = false;
            result[count] = index;
            count++;
        }
        return result;
    }

    private ArrayList<Point> normalization(ArrayList<Point> t) {
        ArrayList<Point> result = new ArrayList<>();

        if (t.size() == 0) {
            return result;
        }

        double[] mean = new double[t.get(0).dimension];
        for (Point item : t) {
            for (int j = 0; j < t.get(0).dimension; j++) {
                mean[j] += item.coordinate[j];
            }
        }
        for (int i = 0; i < mean.length; i++) {
            mean[i] /= t.size();
        }

        double[] standardDeviation = new double[t.get(0).dimension];
        for (Point value : t) {
            for (int j = 0; j < t.get(0).dimension; j++) {
                standardDeviation[j] += Math.pow(value.coordinate[j] - mean[j], 2);
            }
        }
        for (int i = 0; i < standardDeviation.length; i++) {
            standardDeviation[i] = Math.sqrt(standardDeviation[i]);
        }

        for (Point point : t) {
            double[] tempCoordinate = point.coordinate;
            for (int j = 0; j < t.get(0).dimension; j++) {
                tempCoordinate[j] = (tempCoordinate[j] - mean[j]) / standardDeviation[j];
            }
            result.add(new Point(tempCoordinate));
        }
        return result;
    }

}
