package uq.transformation;

import uq.entities.Point;

import java.util.ArrayList;

/**
 * Shifts the time period of a trajectory, make it starts from time t =
 * startTime
 *
 * @author uqdalves
 */
public class TimeShiftTransformation implements TransformationInterface {
    long newStartTime;

    public TimeShiftTransformation(long newStartTime) {
        this.newStartTime = newStartTime;
    }

    /**
     * Returns a copy of the trajectory with time period shifted to start at t =
     * zero
     */
    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        ArrayList<Point> result = new ArrayList<>();
        long oldStartTime = list.get(0).timeLong;
        for (Point point : list) {
            // Makes a copy of the Point and shift the time
            Point newPoint = point.clone();
            newPoint.timeLong = point.timeLong - oldStartTime + newStartTime;
            result.add(newPoint);
        }
        return result;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        throw new UnsupportedOperationException();
    }
}
