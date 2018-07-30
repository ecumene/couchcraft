package xyz.ecumene.couchcraft.common.cursor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;

import javax.vecmath.Vector2f;
import java.util.List;

public class GuiSlotTargets extends GuiCursorTargets<Slot> {
    public GuiSlotTargets(){
        super();
    }

    public void searchSlots(){
        if(Minecraft.getMinecraft().currentScreen != null && targetDB != null) { // Accessing buttons that aren't on screen doesn't make sense, go away ;)
            clearTargets();

            //TODO: Determine xSize and ySize via GuiContainer + reflection

            System.out.println(targetDB.width);

            if(targetDB instanceof GuiInventory)
                for(Slot s : (List<Slot>)((GuiInventory) targetDB).inventorySlots.inventorySlots) {
                    int xSize = 176;
                    int ySize = 166;
                    targets.put(new Vector2f(((targetDB.width - xSize)/2) + s.xDisplayPosition + 8, ((targetDB.height - ySize)/2) + s.yDisplayPosition + 8), s);
                }
        }
    }
}
