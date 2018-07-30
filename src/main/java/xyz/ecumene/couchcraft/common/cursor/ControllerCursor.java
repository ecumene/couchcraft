package xyz.ecumene.couchcraft.common.cursor;

import cpw.mods.fml.client.config.GuiSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.ecumene.couchcraft.api.JumpModeCheck;
import xyz.ecumene.couchcraft.common.CouchcraftMod;
import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;
import xyz.ecumene.couchcraft.utils.ControllerGameUtils;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class ControllerCursor {
    public static boolean controllerGUIInteractMode = true;
    private static boolean lastControllerMode = false;
    public int animationState;

    //TODO: Combine jump target classes (Button and Slot)
    public GuiSlotTargets targets;

    public AxisBinding hoverAxis;
    public float hoverThreshold = 0.35f;
    public float axisSensitivity = 3.6f;
    public int targetMouseX, targetMouseY;

    public List<JumpModeCheck> jumpModeList;

    public boolean mouseDetatchPossible = true, enableMouseInteraction = true, hoverMode = true;

    public ControllerCursor(){
        targets = new GuiSlotTargets();

        jumpModeList = new ArrayList<>();
        //jumpModeList.add((screen, cursor) ->  screen instanceof GuiInventory );
    }

    public void tick(ButtonBinding guiLeft, ButtonBinding guiRight, AxisBinding scroll){
        GuiSlotTargets.targetX = targetMouseX;
        GuiSlotTargets.targetY = targetMouseY;

        //Minecraft.getMinecraft().gameSettings.touchscreen = controllerGUIInteractMode;

        if(mouseDetatchPossible && Mouse.getDX()>0.15f | Mouse.getDY()>0.15f | Mouse.getDWheel()>0.1f)
            controllerGUIInteractMode = false;

        if(Minecraft.getMinecraft().currentScreen != null) {
            if(targets.newGuiFlag && hoverMode) {
                targetMouseX = Minecraft.getMinecraft().currentScreen.width/2;
                targetMouseY = Minecraft.getMinecraft().currentScreen.height/2;
            }

            if(controllerGUIInteractMode){
                animationState = 1;
                Mouse.setGrabbed(true);

                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

                if(targetMouseX*scaledresolution.getScaleFactor() > Minecraft.getMinecraft().displayWidth - 16*scaledresolution.getScaleFactor()) targetMouseX = Minecraft.getMinecraft().currentScreen.width - 16;
                if(targetMouseY*scaledresolution.getScaleFactor() > Minecraft.getMinecraft().displayHeight - 16*scaledresolution.getScaleFactor()) targetMouseY = Minecraft.getMinecraft().currentScreen.height - 16;
                if(targetMouseX < 0) targetMouseX = 0;
                if(targetMouseY < 0) targetMouseY = 0;

                Mouse.setCursorPosition(targetMouseX*scaledresolution.getScaleFactor(), Minecraft.getMinecraft().displayHeight-targetMouseY*scaledresolution.getScaleFactor());

                if(guiLeft.justPressed) ControllerGameUtils.mouseClick(0, Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY);
                if(guiRight.justPressed) ControllerGameUtils.mouseClick(1, Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY);
                //TODO: Scroll Wheel Axis

                if(guiLeft.pressed | guiRight.pressed) animationState = 2;

                if(guiLeft.pressed)
                    ControllerGameUtils.mouseClickMove(Minecraft.getMinecraft().currentScreen, 0, targetMouseX, targetMouseY);
                if(guiRight.pressed)
                    ControllerGameUtils.mouseClickMove(Minecraft.getMinecraft().currentScreen, 1, targetMouseX, targetMouseY);

                if(guiLeft.justRelease) ControllerGameUtils.mouseMovedOrUp(Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY, 0);
                if(guiRight.justRelease) ControllerGameUtils.mouseMovedOrUp(Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY, 1);
            } else animationState = -1;


                targets.searchSlots();
            targets.tick();

            if(hoverMode) {
                if(hoverThreshold < Math.abs(hoverAxis.x))
                    targetMouseX += hoverAxis.x * axisSensitivity;
                if(hoverThreshold < Math.abs(hoverAxis.y))
                    targetMouseY += hoverAxis.y * axisSensitivity;
            } /*else {
                if(targets.button instanceof GuiSlider){
                    Vector2f target = targets.getNextTarget();
                    targetMouseX = (int) target.x;
                    targetMouseY = (int) target.y;

                    //TODO: Fix sliders

                    GuiSlider slider = (GuiSlider) targets.button;
                    targetMouseX += (slider.sliderValue-0.5f) * slider.width;
                }*/ //Haha gone
            else {
                Vector2f target = targets.getNextTarget();
                targetMouseX = (int) target.x;
                targetMouseY = (int) target.y;
            }
        }

        if(controllerGUIInteractMode != lastControllerMode && !controllerGUIInteractMode)
            if(enableMouseInteraction) Mouse.setGrabbed(false);

        lastControllerMode = controllerGUIInteractMode;
    }

    public boolean shouldEnterJumpMode(GuiScreen screen){
        for(JumpModeCheck check : jumpModeList)
            if(check.shouldEnterJumpMode(screen,this)) return true;
        return false;
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

        hoverMode = !shouldEnterJumpMode(screen);
        System.out.println(hoverMode);
    }
}
