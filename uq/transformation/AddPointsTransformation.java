/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.transformation;

import java.util.ArrayList;

import uq.entities.Point;

/**
 * @author uqhsu1 wendingp
 */
public class AddPointsTransformation implements TransformationInterface {
    private final double DEFAULT_ADD_RATE = 0.25;
    public double addRate;

    public AddPointsTransformation() {
        addRate = DEFAULT_ADD_RATE;
    }

    public AddPointsTransformation(double AddPointsRate) {
        addRate = AddPointsRate;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        ArrayList<Point> result = new ArrayList<>();

        int addPointCount = (int) (list.size() * addRate);

        if (list.size() < 2) {
            return list;
        }
        if (addPointCount < 1) {
            addPointCount = 1;
        }
        if (addPointCount >= list.size()) {
            addPointCount = list.size() - 1;
        }

        int[] valueList = topN(list.size() - 1, addPointCount);

        for (int i = 0; i < list.size(); i++) {
            result.add(list.get(i));
            for (int value : valueList) {
                if (value == i) {
                    Point temp = getMidPoint((Point) list.get(i), (Point) list.get(i + 1));
                    result.add(temp);
                }
            }
        }

        return result;
    }

    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        return null;
    }

    private Point getMidPoint(Point p, Point q) {

        double[] pCoordinate = p.coordinate;
        double[] qCoordinate = q.coordinate;

        double[] cood = new double[p.dimension];

        for (int i = 0; i < cood.length; i++) {
            cood[i] = (pCoordinate[i] + qCoordinate[i]) / 2;
        }

        long t1 = p.timeLong;
        long t2 = q.timeLong;

        if (t1 > t2) {
            long temp = t1;
            t1 = t2;
            t2 = temp;
        }

        long timeLong = t1 + (t2 - t1) / 2;

        return new Point(cood, timeLong);
    }

    private int[] topN(int allSize, int N) {
        int[] result = new int[N];

        double[] valueList = new double[allSize];
        for (int i = 0; i < valueList.length; i++) {
            valueList[i] = Math.random();
        }
        int[] allSizeList = sort(valueList);

        for (int i = 0; i < N; i++) {
            result[i] = allSizeList[i];
        }

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
