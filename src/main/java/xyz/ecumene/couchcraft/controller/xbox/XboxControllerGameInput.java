package xyz.ecumene.couchcraft.controller.xbox;

import xyz.ecumene.couchcraft.common.ControllerGameInput;
import xyz.ecumene.couchcraft.common.ControllerContext;
import xyz.ecumene.couchcraft.common.binding.axis.ThresholdXAxisBinding;
import xyz.ecumene.couchcraft.common.binding.axis.ThresholdYAxisBinding;

public class XboxControllerGameInput extends ControllerGameInput {
    protected ControllerContext controllerContext;

    public XboxControllerGameInput(ControllerContext controllerContext) {
        super(controllerContext);
        this.controllerContext = controllerContext;

        this.jumpBinding = controllerContext.button1;
        this.exitGUIBinding = controllerContext.button2;
        this.inventoryGUIBinding = controllerContext.button4;

        this.movement = controllerContext.leftThumb;
        this.look = controllerContext.rightThumb;

        this.attack = new ThresholdXAxisBinding(0.45f, controllerContext.triggers);
        this.build = new ThresholdYAxisBinding(0.45f, controllerContext.triggers);

        this.mouseUp = new ThresholdYAxisBinding(false,-0.45f, controllerContext.leftThumb);
        this.mouseDown = new ThresholdYAxisBinding(0.45f, controllerContext.leftThumb);
        this.mouseLeft = new ThresholdXAxisBinding(false, -0.45f, controllerContext.leftThumb);
        this.mouseRight = new ThresholdXAxisBinding(0.45f, controllerContext.leftThumb);

        this.sneakBinding = controllerContext.stickDownLeft;
        this.hotbarLeft = controllerContext.bumperLeft;
        this.hotbarRight = controllerContext.bumperRight;

        attachInputInterceptors();
    }

    @Override
    public void nonPlayerRelatedResponsibilities() {
        super.nonPlayerRelatedResponsibilities();
    }

    @Override
    public void updatePlayerMoveState() {
        super.updatePlayerMoveState();
    }
}
