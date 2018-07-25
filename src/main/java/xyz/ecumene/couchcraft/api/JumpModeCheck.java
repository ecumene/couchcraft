package xyz.ecumene.couchcraft.api;

import net.minecraft.client.gui.GuiScreen;
import xyz.ecumene.couchcraft.utils.ControllerCursor;

public interface JumpModeCheck {
    boolean shouldEnterJumpMode(GuiScreen screen, ControllerCursor cursor);
}
