package xyz.ecumene.couchcraft.controller.xbox;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import xyz.ecumene.couchcraft.common.ControllerGameInput;
import xyz.ecumene.couchcraft.common.ControllerContext;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;
import xyz.ecumene.couchcraft.common.binding.axis.ThresholdXAxisBinding;
import xyz.ecumene.couchcraft.common.binding.axis.ThresholdYAxisBinding;
import xyz.ecumene.couchcraft.common.cursor.ControllerCursor;

public class XboxControllerGameInput extends ControllerGameInput {
    protected ControllerContext controllerContext;
    public ControllerCursor cursor;

    public ButtonBinding mouseUp, mouseDown, mouseRight, mouseLeft;

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

        cursor = new ControllerCursor();
        this.cursor.hoverAxis = controllerContext.leftThumb;

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        cursor.control(mouseUp, mouseDown, mouseRight, mouseLeft);
        cursor.tick(controllerContext.button1, controllerContext.button2, controllerContext.leftThumb);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiScreenOpened(GuiOpenEvent event){
        cursor.refreshTargets(event.gui);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderCursorOnGui(GuiScreenEvent.DrawScreenEvent.Post post){
        cursor.renderCursorOnGui(post.gui);
    }

    @Override
    public void nonPlayerRelatedResponsibilities() {
        if(mouseUp != null) mouseUp.onPoll();
        if(mouseDown != null) mouseDown.onPoll();
        if(mouseRight != null) mouseRight.onPoll();
        if(mouseLeft != null) mouseLeft.onPoll();

        if(mouseUp != null) mouseUp.onTick();
        if(mouseDown != null) mouseDown.onTick();
        if(mouseRight != null) mouseRight.onTick();
        if(mouseLeft != null) mouseLeft.onTick();

        super.nonPlayerRelatedResponsibilities();
    }

    @Override
    public void updatePlayerMoveState() {
        super.updatePlayerMoveState();
    }
}
