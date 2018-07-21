package xyz.ecumene.couchcraft.common;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class GameInput extends MovementInput {
    public void nonPlayerRelatedResponsibilities(){
    }

    public void escapeGUI(){
        if(Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.closeScreen();
    }
}
