package xyz.ecumene.couchcraft.common.binding.axis;

import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;

/**
 * A class for creating a button bindings from axis bindings
 * 
 * @author ecumene
 */
public class ThresholdAxisBinding extends ButtonBinding {
    /**
     * The original axis binding
     */
    public AxisBinding binding;
    /**
     * Threshold for which it is considered to be a button press
     */
    public float threshold;
    /**
     * Should the press signal be greater than or less than threshold
     */
    public boolean greaterThan;

    /**
     * @param greaterThan Should the press signal be greater than or less than threshold
     * @param threshold The value for which either axis (X or Y) is above (or below, see greaterThan): the button is considered pressed
     * @param axis The axis (X and Y)
     */
    public ThresholdAxisBinding(boolean greaterThan, float threshold, AxisBinding axis){
        this.greaterThan = greaterThan;
        this.binding = axis;
        this.threshold = threshold;
    }

    @Override
    public void onPoll() {
        super.onPoll();
        pressed = isPressed();
    }

    public boolean isPressed(){
        return isXDown() | isYDown();
    }

    /** @return If the X axis is over the threshold */
    public boolean isXDown(){
        return greaterThan ? binding.x > threshold : binding.x < threshold;
    }

    /** @return If the Y axis is over the threshold */
    public boolean isYDown(){
        return greaterThan ? binding.y > threshold : binding.y < threshold;
    }

    public AxisBinding getBinding() {
        return binding;
    }
}
