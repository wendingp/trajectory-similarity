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
public interface TransformationInterface {
    ArrayList<Point> getTransformation(ArrayList<Point> list);

    ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList);
}
