/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.transformation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

import uq.entities.Point;

/**
 * @author uqhsu1
 */
public class SynchronizedShiftTransformation implements TransformationInterface {
    double shiftRate = 0.7;
    double shiftDistance = 0.000125;
    double curve = 60;

    public SynchronizedShiftTransformation() {
    }

    public SynchronizedShiftTransformation(double ShiftRate, double ShiftDistance, double curve) {
        shiftRate = ShiftRate;
        shiftDistance = ShiftDistance;
        this.curve = curve;
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
                Point temp = getShiftPoint(list.get(i), shiftDistance, curve);
                result.add(temp);
            }
        }

        return result;
    }

    private Point getShiftPoint(Point p, double R, double C) {

        double[] cosC = new double[p.dimension];
        for (int i = 0; i < cosC.length; i++) {
            cosC[i] = R * (Math.cos(C * i % 31));
        }

        double[] temp = new double[p.dimension];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = p.coordinate[i] + cosC[i];
        }

        return new Point(temp, p.timeLong);
    }


    private int[] topN(int allSize, int N, double[] valueList) {
        int[] result = new int[N];

        int[] allSizeList = argSort(valueList, false);

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

    private int[] argSort(double[] list, boolean acsending) {
//        int[] result = new int[list.length];
//        boolean[] mark = new boolean[list.length];
//
//        for (int i = 0; i < mark.length; i++) {
//            mark[i] = true;
//            result[i] = -1;
//        }
//        int count = 0;
//        for (int i = 0; i < list.length; i++) {
//            double max = -1;
//            int index = -1;
//            for (int j = 0; j < list.length; j++) {
//                if (mark[j]) {
//                    if (max == -1) {
//                        max = list[j];
//                        index = j;
//                    } else if (max < list[j]) {
//                        max = list[j];
//                        index = j;
//                    }
//                }
//            }
//            mark[index] = false;
//            result[count] = index;
//            count++;
//        }
//        return result;
        return IntStream.range(0, list.length).boxed()
                .sorted(Comparator.comparingDouble(i -> (acsending ? 1 : -1) * list[i]))
                .mapToInt(ele -> ele).toArray();
    }
}
