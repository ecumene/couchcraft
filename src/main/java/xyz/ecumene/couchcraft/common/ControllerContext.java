package xyz.ecumene.couchcraft.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;
import xyz.ecumene.couchcraft.utils.ControllerGameUtils;
import xyz.ecumene.couchcraft.utils.GuiButtonTargets;

import javax.vecmath.Vector2f;

/**
 * Stores controller values to avoid polling multiple times every tick
 */
public abstract class ControllerContext {

    public static boolean controllerGUIInteractMode = true;
    private static boolean lastControllerMode = false;
    public int animationState;

    public GuiButtonTargets targets;

    public AxisBinding rightThumb, leftThumb, triggers;

    public ButtonBinding button1, button2, button3, button4;
    public ButtonBinding start, select;
    public ButtonBinding stickDownLeft, stickDownRight;
    public ButtonBinding povUp, povDown, povLeft, povRight;
    public ButtonBinding bumperLeft, bumperRight;

    //TODO: Fix this?
    public static int targetMouseX, targetMouseY;

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

        targets = new GuiButtonTargets();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiOpened(GuiOpenEvent event){
        targets.clearTargets();
        targets.setTargetDB(event.gui);
        try {
            targets.searchTargets();
            targets.chooseFirst();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderCursorOnGui(GuiScreenEvent.DrawScreenEvent.Post post){
        if(targets != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(CouchcraftMod.guiIcons);
            if (animationState >= 0) {
                int animationX = animationState % 16;
                int animationY = (int) Math.floor((double) animationState / 16.0D);
                post.gui.drawTexturedModalRect(targetMouseX, targetMouseY, animationX * 16, animationY * 16, 16, 16);
            }
        }
    }

    /**
     * Updates all fields with a poll of controller values
     * @param controller The controller to poll
     */
    public void poll(Controller controller) {
        controller.poll();
        pollInputs(controller);

        // TODO: Move most of this to ControllerGameInput, it belongs there...
        // Leave only poll related things

        if(Mouse.getDX()>0.15f | Mouse.getDY()>0.15f | Mouse.getDWheel()>0.1f)
            controllerGUIInteractMode = false;

        if(Minecraft.getMinecraft().currentScreen != null) {
            if(controllerGUIInteractMode){
                animationState = 1;
                Mouse.setGrabbed(true);
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                Mouse.setCursorPosition(targetMouseX*scaledresolution.getScaleFactor(), Minecraft.getMinecraft().displayHeight-targetMouseY*scaledresolution.getScaleFactor());
                if(button1.justPressed) ControllerGameUtils.mouseClick(0, Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY);
                if(button1.pressed) animationState = 2;
                if(button1.justRelease) ControllerGameUtils.mouseMovedOrUp(Minecraft.getMinecraft().currentScreen, targetMouseX, targetMouseY);
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

        if(controllerGUIInteractMode != lastControllerMode && !controllerGUIInteractMode)
                Mouse.setGrabbed(false);

        lastControllerMode = controllerGUIInteractMode;
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
