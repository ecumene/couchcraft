package xyz.ecumene.couchcraft.common.cursor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import xyz.ecumene.couchcraft.common.ControllerContext;
import xyz.ecumene.couchcraft.utils.FieldHelper;

import javax.vecmath.Vector2f;
import java.util.*;


public class GuiButtonTargets extends GuiCursorTargets<GuiButton> {
    static FieldHelper<GuiScreen, List> fieldButtonList = new FieldHelper<>(GuiScreen.class, "field_146292_n", "buttonList");

    public GuiButtonTargets(){
        super();
    }

    public void searchButtons() throws IllegalAccessException{
        if(Minecraft.getMinecraft().currentScreen != null && targetDB != null) { // Accessing buttons that aren't on screen doesn't make sense, go away ;)
            clearTargets();
            for (GuiButton button : ((List<GuiButton>) fieldButtonList.get(targetDB))) {
                if (button.visible)
                    targets.put(new Vector2f(button.xPosition + (button.width / 2f), button.yPosition + (button.height / 2f)), button);
            }
        }
    }
}
