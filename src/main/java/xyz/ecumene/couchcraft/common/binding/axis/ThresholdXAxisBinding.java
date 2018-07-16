package xyz.ecumene.couchcraft.common.binding.axis;

import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;

public class ThresholdXAxisBinding extends ThresholdAxisBinding {

    public ThresholdXAxisBinding(boolean greaterThan, float threshold, AxisBinding axis){
        super(greaterThan, threshold, axis);
    }

    public ThresholdXAxisBinding(float threshold, AxisBinding axis){
        this(true, threshold, axis);
    }

    @Override
    public boolean isPressed() {
        return isXDown();
    }

}
