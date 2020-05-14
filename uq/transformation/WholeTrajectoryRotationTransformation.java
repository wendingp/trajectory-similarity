/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.transformation;

import uq.entities.Line;
import uq.entities.Point;

import java.util.ArrayList;

public class WholeTrajectoryRotationTransformation implements TransformationInterface {

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        return null;
    }

    public double rotationAngle;

    public WholeTrajectoryRotationTransformation(double RotationAngle) {
        rotationAngle = RotationAngle;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> t) {
        double cos = Math.cos(rotationAngle);
        double sin = Math.sin(rotationAngle);

        ArrayList<Point> ret = new ArrayList<>(t.size());

        for (Point p : t) {
            assert (p.dimension == 2);

            double[] cor = new double[2];
            cor[0] = cos * p.coordinate[0] - sin * p.coordinate[1];
            cor[1] = sin * p.coordinate[0] + cos * p.coordinate[1];

            ret.add(new Point(cor, p.timeLong));
        }

        return ret;
    }

    private ArrayList<Point> generateArray(double choose) {
        ArrayList<Point> result = new ArrayList<>();
        Point[] p;
        if (choose == 1) {
            p = new Point[5];
            p[0] = new Point(new double[] {0, 0});
            p[1] = new Point(new double[] {20, 30});
            p[2] = new Point(new double[] {50, 20});
            p[3] = new Point(new double[] {60, 80});
            p[4] = new Point(new double[] {100, 60});
        } else {
            p = new Point[4];
            p[0] = new Point(new double[] {0, 0});
            p[1] = new Point(new double[] {40, 40});
            p[2] = new Point(new double[] {60, 30});
            p[3] = new Point(new double[] {100, 60});
            // p[4]=new Point(new double[]{100,60});

        }
        return getPoints(result, p);
    }

    private ArrayList<Point> getPoints(ArrayList<Point> result, Point[] p) {
        ArrayList<Line> l = new ArrayList<>();
        for (int i = 0; i < p.length - 1; i++) {
            Line ll = new Line(p[i], p[i + 1]);
            l.add(ll);
        }

        double pieceSize = 100.0 / l.size();

        for (Line line : l) {
            for (int j = 0; j < pieceSize; j++) {
                double[] temp = new double[2];
                temp[0] = j * (line.endPoint.coordinate[1] - line.startPoint.coordinate[0]) / pieceSize
                        + line.startPoint.coordinate[0];
                temp[1] = line.getYByX(temp[0]);

                result.add(new Point(temp));
            }
        }

        return result;
    }
}
