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
public class DeletePointTransformation implements TransformationInterface {
    public double deleteRate = 0.1;

    public DeletePointTransformation() {
    }

    public DeletePointTransformation(double DeletePointRate) {
        deleteRate = DeletePointRate;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        return null;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        ArrayList<Point> Result = new ArrayList<>();

        int deleteCount = (int) (list.size() * deleteRate);

        if (list.size() - deleteCount <= escapeList.size()) {
            return escapeList;
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

        int[] deleteList = topN(list.size(), deleteCount, value);

        for (int item : deleteList) {
            mark[item] = true;
        }

        for (int i = 0; i < mark.length; i++) {
            if (!mark[i]) {
                Result.add(list.get(i));
            }
        }

        return Result;
    }

    private int[] topN(int allSize, int N, double[] valueList) {
        int[] result = new int[N];

        int[] allSizeList = sort(valueList);

        if (N >= 0) {
            System.arraycopy(allSizeList, 0, result, 0, N);
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
