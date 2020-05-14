package uq.transformation;

import uq.entities.Point;

import java.util.ArrayList;

/**
 * Re-sample the trajectory. Select every sampleRate point from the list.
 *
 * @author uqdalves
 */
public class DifferentSampleRateTransformation implements TransformationInterface {
    public int sampleRate = 0; // every sampleRate points

    public DifferentSampleRateTransformation() {
    }

    public DifferentSampleRateTransformation(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        return getDiffSampleRateTransformation(list);
    }

    /**
     * sample the trajectory: get every sampleRate points
     */
    private ArrayList<Point> getDiffSampleRateTransformation(ArrayList<Point> list) {
        ArrayList<Point> samples = new ArrayList<>();
        for (int i = 0; i < list.size(); i += sampleRate) {
            samples.add(list.get(i));
        }
        return samples;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list, ArrayList<Point> escapeList) {
        return null;
    }
}
