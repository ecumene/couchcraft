package xyz.ecumene.couchcraft.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.ecumene.couchcraft.common.CouchcraftMod;
import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;

import javax.vecmath.Vector2f;

public class ControllerCursor {
    public static boolean controllerGUIInteractMode = true;
    private static boolean lastControllerMode = false;
    public int animationState;

    public GuiButtonTargets targets;

    public int targetMouseX, targetMouseY;

    public boolean mouseDetatchPossible = true, enableMouseInteraction = true;

    public ControllerCursor(){
        targets = new GuiButtonTargets();
    }

    public void tick(ButtonBinding guiLeft, ButtonBinding guiRight, AxisBinding scroll){
        GuiButtonTargets.targetX = targetMouseX;
        GuiButtonTargets.targetY = targetMouseY;

        if(mouseDetatchPossible && Mouse.getDX()>0.15f | Mouse.getDY()>0.15f | Mouse.getDWheel()>0.1f)
            controllerGUIInteractMode = false;

        if(Minecraft.getMinecraft().currentScreen != null) {
            if(controllerGUIInteractMode){
                animationState = 1;
                Mouse.setGrabbed(true);
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                Mouse.setCursorPosition(targetMouseX*scaledresolution.getScaleFactor(), Minecraft.getMinecraft().displayHeight-targetMouseY*scaledresolution.getScaleFactor());
                if(guiLeft.justPressed) ControllerGameUtils.mouseClick(0, Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY);
                if(guiRight.justPressed) ControllerGameUtils.mouseClick(1, Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY);
                //TODO: Scroll Wheel Axis

                if(guiLeft.pressed | guiRight.pressed) animationState = 2;
                if(guiLeft.justRelease | guiRight.justRelease) ControllerGameUtils.mouseMovedOrUp(Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY);
            } else animationState = -1;

            try {
                targets.searchTargets();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            targets.tick();
            Vector2f target = targets.getNextTarget();
            targetMouseX = (int) target.x;
            targetMouseY = (int) target.y;
        }

        if(controllerGUIInteractMode != lastControllerMode && !controllerGUIInteractMode)
            if(enableMouseInteraction) Mouse.setGrabbed(false);

        lastControllerMode = controllerGUIInteractMode;
    }

    public void control(ButtonBinding mouseUp, ButtonBinding mouseDown, ButtonBinding mouseRight, ButtonBinding mouseLeft){
        if(controllerGUIInteractMode){
            if(mouseUp != null) {
                targets.moveUp = mouseUp.justPressed;
                if(mouseUp.ticksDown > 13) targets.moveUp = mouseUp.ticksDown % 5 == 0;
            }
            if(mouseDown != null) {
                targets.moveDown = mouseDown.justPressed;
                if(mouseDown.ticksDown > 13) targets.moveDown = mouseDown.ticksDown % 5 == 0;
            }
            if(mouseRight != null) {
                targets.moveRight = mouseRight.justPressed;
                if(mouseRight.ticksDown > 13) targets.moveRight = mouseRight.ticksDown % 5 == 0;
            }
            if(mouseLeft != null) {
                targets.moveLeft = mouseLeft.justPressed;
                if(mouseLeft.ticksDown > 13) targets.moveLeft = mouseLeft.ticksDown % 5 == 0;
            }
        }
    }

    public void renderCursorOnGui(GuiScreen screen){
        if(targets != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(CouchcraftMod.guiIcons);
            if (animationState >= 0) {
                int animationX = animationState % 16;
                int animationY = (int) Math.floor((double) animationState / 16.0D);
                screen.drawTexturedModalRect(targetMouseX, targetMouseY, animationX * 16, animationY * 16, 16, 16);
            }
        }
    }

    public void refreshTargets(GuiScreen screen){
        targets.clearTargets();
        targets.setTargetDB(screen);
    }
}
