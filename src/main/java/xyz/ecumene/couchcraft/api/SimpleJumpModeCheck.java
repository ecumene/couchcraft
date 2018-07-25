package xyz.ecumene.couchcraft.api;

import net.minecraft.client.gui.GuiScreen;
import xyz.ecumene.couchcraft.utils.ControllerCursor;

public class SimpleJumpModeCheck implements JumpModeCheck {
    public GuiScreen checkScreen;

    public SimpleJumpModeCheck(GuiScreen screen){
        this.checkScreen = screen;
    }

    @Override
    public boolean shouldEnterJumpMode(GuiScreen screen, ControllerCursor cursor) {
        return screen == checkScreen;
    }
}
