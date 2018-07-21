package xyz.ecumene.couchcraft.common.binding.axis;

import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;

/**
 * An implemenetation of ThresholdAxisBinding but only using the Y axis
 */
public class ThresholdYAxisBinding extends ThresholdAxisBinding {

    public ThresholdYAxisBinding(boolean greaterThan, float threshold, AxisBinding axis){
        super(greaterThan, threshold, axis);
    }

    public ThresholdYAxisBinding(float threshold, AxisBinding axis){
        this(true, threshold, axis);
    }

    @Override
    public boolean isPressed() {
        return isYDown();
    }

}
