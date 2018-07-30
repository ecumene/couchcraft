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

    public AxisBinding hoverAxis;
    public float hoverThreshold = 0.35f;
    public float axisSensitivity = 3.6f;
    public int targetMouseX, targetMouseY;

    public boolean mouseDetatchPossible = true, enableMouseInteraction = true;

    public ControllerCursor(){
    }

    public void tick(ButtonBinding guiLeft, ButtonBinding guiRight, AxisBinding scroll){
        if(mouseDetatchPossible && Mouse.getDX()>0.15f | Mouse.getDY()>0.15f | Mouse.getDWheel()>0.1f)
            controllerGUIInteractMode = false;

        if(Minecraft.getMinecraft().currentScreen != null) {
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

            if(hoverThreshold < Math.abs(hoverAxis.x))
                targetMouseX += hoverAxis.x * axisSensitivity;
            if(hoverThreshold < Math.abs(hoverAxis.y))
                targetMouseY += hoverAxis.y * axisSensitivity;
        }

        if(controllerGUIInteractMode != lastControllerMode && !controllerGUIInteractMode)
            if(enableMouseInteraction) Mouse.setGrabbed(false);

        lastControllerMode = controllerGUIInteractMode;
    }

    public void renderCursorOnGui(GuiScreen screen){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(CouchcraftMod.guiIcons);
        if (animationState >= 0) {
            int animationX = animationState % 16;
            int animationY = (int) Math.floor((double) animationState / 16.0D);
            screen.drawTexturedModalRect(targetMouseX, targetMouseY, animationX * 16, animationY * 16, 16, 16);
        }
    }
}
