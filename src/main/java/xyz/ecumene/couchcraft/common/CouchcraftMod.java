package xyz.ecumene.couchcraft.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;
import xyz.ecumene.couchcraft.controller.xbox.XboxContext;
import xyz.ecumene.couchcraft.controller.xbox.XboxControllerGameInput;

@Mod(modid = CouchcraftMod.MODID, name = CouchcraftMod.MODNAME, version = CouchcraftMod.MODVERSION)
public class CouchcraftMod {
    public static final String MODID = "couchcraft";
    public static final String MODNAME = "Couchcraft";
    public static final String MODVERSION = "0.0";

    public static ResourceLocation guiIcons;

    public Controller controller;
    public ControllerContext controllerContext;
    public GameInput input;

    public CouchcraftMod(){
        FMLCommonHandler.instance().bus().register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        controllerContext = new XboxContext();
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        if(controllers.length!=0&&controllers[0] != null)
            controller = controllers[0];
        input = new XboxControllerGameInput(controllerContext);

        guiIcons = new ResourceLocation (MODID, "textures/gui/icons.png");
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(!(Minecraft.getMinecraft().thePlayer.movementInput instanceof MovementInputMultiplexer)) {
            Minecraft.getMinecraft().thePlayer.movementInput
                    = new MovementInputMultiplexer(new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings),
                    input);
        }
        controllerContext.tickInputs();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.type == TickEvent.Type.CLIENT)
            performControllerPollAndActions();
    }

    private void performControllerPollAndActions(){
        if(controller != null) {
            if(controllerContext!=null)controllerContext.poll(controller);
            if(input!=null)input.nonPlayerRelatedResponsibilities();
        }
    }
}
