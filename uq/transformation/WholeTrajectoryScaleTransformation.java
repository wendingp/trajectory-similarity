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
public class WholeTrajectoryScaleTransformation implements TransformationInterface {
    double translation;

    public WholeTrajectoryScaleTransformation() {
    }

    public WholeTrajectoryScaleTransformation(double timeRatio) {
        translation = timeRatio;
    }

    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        return null;
    }

    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        ArrayList<Point> result = new ArrayList<>();
        long startTime = list.get(0).timeLong;
        assert (list.get(0).dimension == 2);

        result.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            double timeDouble = (list.get(i).timeLong - list.get(0).timeLong) * translation + startTime;
            Point temp = new Point(list.get(i).coordinate, (long) timeDouble);
            result.add(temp);
        }
        return result;
    }
}
