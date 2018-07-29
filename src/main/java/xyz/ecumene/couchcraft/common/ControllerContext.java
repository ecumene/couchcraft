package xyz.ecumene.couchcraft.common;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;

/**
 * Stores controller values to avoid polling multiple times every tick
 */
public abstract class ControllerContext {

    public AxisBinding rightThumb, leftThumb, triggers;

    public ButtonBinding button1, button2, button3, button4;
    public ButtonBinding start, select;
    public ButtonBinding stickDownLeft, stickDownRight;
    public ButtonBinding povUp, povDown, povLeft, povRight;
    public ButtonBinding bumperLeft, bumperRight;

    public ControllerContext(){
        rightThumb = new AxisBinding();
        leftThumb  = new AxisBinding();
        triggers = new AxisBinding();

        button1 = new ButtonBinding();
        button2 = new ButtonBinding();
        button3 = new ButtonBinding();
        button4 = new ButtonBinding();

        start = new ButtonBinding();
        select = new ButtonBinding();

        stickDownLeft = new ButtonBinding();
        stickDownRight = new ButtonBinding();

        povUp = new ButtonBinding();
        povDown = new ButtonBinding();
        povLeft = new ButtonBinding();
        povRight = new ButtonBinding();

        bumperLeft = new ButtonBinding();
        bumperRight = new ButtonBinding();
    }

    /**
     * Updates all fields with a poll of controller values
     * @param controller The controller to poll
     */
    public void poll(Controller controller) {
        controller.poll();
        pollInputs(controller);

        //TODO: Check if already done this once per tick
        button1.onPoll();
        button2.onPoll();
        button3.onPoll();
        button4.onPoll();
        start.onPoll();
        select.onPoll();
        stickDownLeft.onPoll();
        stickDownRight.onPoll();
        povUp.onPoll();
        povDown.onPoll();
        povLeft.onPoll();
        povRight.onPoll();
        bumperLeft.onPoll();
        bumperRight.onPoll();
    }

    public void tickInputs(){
        button1.onTick();
        button2.onTick();
        button3.onTick();
        button4.onTick();
        start.onTick();
        select.onTick();
        stickDownLeft.onTick();
        stickDownRight.onTick();
        povUp.onTick();
        povDown.onTick();
        povLeft.onTick();
        povRight.onTick();
        bumperLeft.onTick();
        bumperRight.onTick();
    }

    protected abstract void pollInputs(Controller controller);

    protected void buttonPoll(ButtonBinding binding, Component component){
        binding.pressed = component.getPollData() != 0.0f;
    }
    protected void axisPollX(AxisBinding binding, Component component){
        binding.x = component.getPollData();
    }
    protected void axisPollY(AxisBinding binding, Component component){
        binding.y = component.getPollData();
    }
    protected void povPoll(ButtonBinding buttonUp, ButtonBinding buttonLeft, ButtonBinding buttonDown, ButtonBinding buttonRight, float pov){
        buttonUp.pressed = false;
        buttonDown.pressed = false;
        buttonLeft.pressed = false;
        buttonRight.pressed = false;

        buttonUp.pressed = pov == 0.25f;
        buttonRight.pressed = pov == 0.50f;
        buttonDown.pressed = pov == 0.75f;
        buttonLeft.pressed = pov == 1;
    }
}
