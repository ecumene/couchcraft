package xyz.ecumene.couchcraft.common.binding;

import xyz.ecumene.couchcraft.common.ControllerContext;

public class ButtonBinding {
    public boolean pressed, lastPressed;
    public boolean justPressed, justRelease;
    public int pressTime;
    public int ticksDown;

    public void onPoll(){
        justPressed = false;
        justRelease = false;
        if(pressed && !lastPressed) justPressed = true;
        if(!pressed && lastPressed) justRelease = true;
        lastPressed = pressed;

        if(justPressed) ControllerContext.controllerGUIInteractMode = true;
    }

    public void onTick(){
        if(justPressed) pressTime++;
        if(pressed) ticksDown++;
        else ticksDown = 0;
    }
}
