/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.transformation;

import uq.entities.Point;

import java.util.ArrayList;

/**
 * @author uqhsu1 wendingp
 */
public interface TransformationInterface {
    ArrayList<Point> getTransformation(ArrayList<Point> list);

    ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList);
}
