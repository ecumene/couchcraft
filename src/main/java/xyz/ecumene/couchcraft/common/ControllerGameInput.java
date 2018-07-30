package xyz.ecumene.couchcraft.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C16PacketClientStatus;
import xyz.ecumene.couchcraft.common.binding.AxisBinding;
import xyz.ecumene.couchcraft.common.binding.ButtonBinding;
import xyz.ecumene.couchcraft.common.binding.MultiKeyBinding;
import xyz.ecumene.couchcraft.utils.ControllerGameUtils;

public class ControllerGameInput extends GameInput {
    public ButtonBinding jumpBinding, sneakBinding;
    public AxisBinding movement, look;
    public ButtonBinding inventoryGUIBinding, exitGUIBinding;
    public ButtonBinding attack, build, drop;
    public ButtonBinding hotbarRight, hotbarLeft;

    public float deadzone = 0.25f;
    public float sensitivity = 6.4f;

    protected ControllerContext controllerContext;

    public ControllerGameInput(ControllerContext controllerContext){
        super();
        this.controllerContext = controllerContext;
    }

    public void attachInputInterceptors(){
        if(attack!=null)Minecraft.getMinecraft().gameSettings.keyBindAttack = new MultiKeyBinding(attack, Minecraft.getMinecraft().gameSettings.keyBindAttack);
        if(build!=null)Minecraft.getMinecraft().gameSettings.keyBindUseItem = new MultiKeyBinding(build, Minecraft.getMinecraft().gameSettings.keyBindUseItem);
        if(drop!=null)Minecraft.getMinecraft().gameSettings.keyBindDrop = new MultiKeyBinding(drop, Minecraft.getMinecraft().gameSettings.keyBindDrop);
        if(sneakBinding !=null)Minecraft.getMinecraft().gameSettings.keyBindSneak = new MultiKeyBinding(sneakBinding, Minecraft.getMinecraft().gameSettings.keyBindSneak);
    }

    @Override
    public void nonPlayerRelatedResponsibilities(){
        //TODO: Support for multiple polls per tick? It could happen probably, or someone could manually poll

        // REMIND: Only tick and poll the multikeybindings here, as the context class won't do that.
        if(attack!=null)attack.onPoll();
        if(build!=null)build.onPoll();
        if(sneakBinding !=null) sneakBinding.onPoll();

        if(attack!=null)attack.onTick();
        if(build!=null)build.onTick();
        if(drop!=null)drop.onTick();
        if(sneakBinding !=null) sneakBinding.onTick();

        if(exitGUIBinding.pressed)
            escapeGUI();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updatePlayerMoveState() {
        super.updatePlayerMoveState();
        if(Minecraft.getMinecraft().inGameHasFocus && controllerContext != null) {
            if(inventoryGUIBinding != null){
                if(inventoryGUIBinding.justPressed) {
                    if(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
                        this.escapeGUI();
                    } else {
                        if (Minecraft.getMinecraft().playerController.func_110738_j()) {
                            Minecraft.getMinecraft().thePlayer.func_110322_i();
                        } else {
                            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                            Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().thePlayer));
                        }
                    }
                }
            }

            if(Minecraft.getMinecraft().inGameHasFocus) {
                if (jumpBinding != null) jump = jumpBinding.pressed;
                if (sneakBinding != null) sneak = sneakBinding.pressed;

                //TODO: Fix min/max rotation vertical
                if (look != null) {
                    if (Math.abs(look.x) > deadzone)
                        Minecraft.getMinecraft().thePlayer.rotationYaw += look.x * sensitivity;
                    if (Math.abs(look.y) > deadzone)
                        Minecraft.getMinecraft().thePlayer.rotationPitch += look.y * sensitivity;
                }

                if (hotbarLeft != null)
                    if (hotbarLeft.justPressed)
                        ControllerGameUtils.moveHotbarSelectorSnapLeft();
                if (hotbarRight != null)
                    if (hotbarRight.justPressed)
                        ControllerGameUtils.moveHotbarSelectorSnapRight();

                if (movement != null) {
                    if (Math.abs(movement.y) > deadzone)
                        moveForward = -movement.y;
                    else moveForward = 0;
                    if (Math.abs(movement.x) > deadzone)
                        moveStrafe = -movement.x;
                    else moveStrafe = 0;

                    if (this.sneak) {
                        this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
                        this.moveForward = (float) ((double) this.moveForward * 0.3D);
                    }
                }
            }
        }
    }

    public ControllerContext getControllerContext() {
        return controllerContext;
    }
}
