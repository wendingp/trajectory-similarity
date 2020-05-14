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
public class WholeTrajectoryTranslationTransformation implements TransformationInterface {
    double reductionRatio = 0.5;

    public WholeTrajectoryTranslationTransformation() {
    }

    public WholeTrajectoryTranslationTransformation(double ReductionRatio) {
        reductionRatio = ReductionRatio;
    }

    //è®©x,yå��æ ‡ç­‰æ¯�?ä¾‹ç¼©æ�?¾
    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        return null;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        ArrayList<Point> result = new ArrayList<>();

        assert (list.get(0).dimension == 2);
        result.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            double lengthX = (list.get(i).coordinate[0] - list.get(0).coordinate[0]) * reductionRatio;
            double lengthY = (list.get(i).coordinate[1] - list.get(0).coordinate[1]) * reductionRatio;
            double[] cood = new double[]{list.get(0).coordinate[0] + lengthX, list.get(0).coordinate[1] + lengthY};
            result.add(new Point(cood, list.get(i).timeLong));
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
        return new Point(temp, p.timeString);
    }
}
