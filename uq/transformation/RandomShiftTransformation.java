/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.transformation;

import java.util.ArrayList;

import uq.entities.Point;

/**
 * @author uqhsu1
 */
public class RandomShiftTransformation implements TransformationInterface {
    double shiftRate = 0.3;
    double shiftDistance = 0.000125;

    public RandomShiftTransformation() {
    }

    public RandomShiftTransformation(double ShiftRate, double ShiftDistance) {
        shiftRate = ShiftRate;
        shiftDistance = ShiftDistance;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        return null;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        ArrayList<Point> result = new ArrayList<>();

        int shiftCount = (int) (list.size() * shiftRate);

        if (list.size() - shiftCount <= escapeList.size()) {
            shiftCount = list.size() - escapeList.size();
        }

        double[] value = new double[list.size()];
        boolean[] mark = new boolean[list.size()];

        for (int i = 0; i < list.size(); i++) {
            value[i] = Math.random();
            mark[i] = false;
        }

        for (int i = 0; i < list.size(); i++) {
            Point temp = list.get(i);
            for (Point point : escapeList) {
                if (temp.isSame(point)) {
                    value[i] = -1;
                }
            }
        }

        int[] shiftList = topN(list.size(), shiftCount, value);

        for (int item : shiftList) {
            mark[item] = true;
        }

        for (int i = 0; i < mark.length; i++) {
            if (!mark[i]) {
                result.add(list.get(i));
            } else {
                Point temp = getShiftPoint(list.get(i), shiftDistance);
                result.add(temp);
            }
        }

        return result;
    }

    private Point getShiftPoint(Point p, double shiftDistance) {
        //double r=R*Math.random();

        double[] cosCurve = new double[p.dimension];
        for (int i = 0; i < cosCurve.length; i++) {
            cosCurve[i] = 2 * (Math.random() - 0.5) * shiftDistance;
        }

        double[] temp = new double[p.dimension];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = p.coordinate[i] + cosCurve[i];
        }

        return new Point(temp, p.timeLong);
    }


    private int[] topN(int allSize, int N, double[] valueList) {
        int[] result = new int[N];

        int[] allSizeList = sort(valueList);

        System.arraycopy(allSizeList, 0, result, 0, N);

        for (int i = 0; i < N; i++) {
            int min = result[i];
            int minIndex = i;
            for (int j = i + 1; j < N; j++) {
                if (min > result[j]) {
                    min = result[j];
                    minIndex = j;
                }
            }
            int temp = result[i];
            result[i] = min;
            result[minIndex] = temp;
        }

        return result;
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

}
