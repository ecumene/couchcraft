package xyz.ecumene.couchcraft.utils;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Slot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ControllerGameUtils {



    public static void moveHotbarSelectorSnapLeft(){
        //TODO: Simplify loops into one
        //TODO: Quickly scroll through via holding?
        int nearestIndex = -1;
        int currentIndex = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
        for(int i = currentIndex-1; i >= 0; i--) {
            Slot slot = ((ArrayList<Slot>)Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots).get(36+i);
            if (slot != null && slot.getHasStack()) {
                nearestIndex = i;
                break;
            }
        }
        if(nearestIndex == -1) {
            for(int j = 8-(currentIndex); j >= 0; j--) {
                Slot slot = ((ArrayList<Slot>)Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots).get(36+currentIndex+j);
                if (slot != null && slot.getHasStack()) {
                    nearestIndex = currentIndex + j;
                    break;
                }
            }
        }
        if(nearestIndex != -1)
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = nearestIndex;
    }
    public static void moveHotbarSelectorSnapRight(){
        int nearestIndex = -1;
        int currentIndex = Minecraft.getMinecraft().thePlayer.inventory.currentItem;

            for(int j = 0; j < 8-currentIndex; j++) {
                Slot slot = ((ArrayList<Slot>)Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots).get(36+currentIndex+1+j);
                if (slot != null && slot.getHasStack()) {
                    nearestIndex = currentIndex +1+ j;
                    break;
                }
            }

        if(nearestIndex == -1) {
            for(int i = 0; i < currentIndex+1; i++) {
                Slot slot = ((ArrayList<Slot>)Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots).get(36+i);
                if (slot != null && slot.getHasStack()) {
                    nearestIndex = i;
                    break;
                }
            }
        }
        if(nearestIndex != -1)
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = nearestIndex;
    }

    public static FieldHelper<GuiScreen, Integer> fieldEventButton = new FieldHelper<>(GuiScreen.class, "field_146287_f", "eventButton");
    public static FieldHelper<GuiScreen, Long> fieldLastMouseEvent = new FieldHelper<>(GuiScreen.class, "field_146288_g", "lastMouseEvent");
    public static Method methodMouseClicked;
    public static Method methodMouseMovedOrUp;
    public static Method methodMouseClickMove;

    public static void mouseClick(int button, GuiScreen screen, int x, int y) {
        try {
            fieldEventButton.set(screen, button);
            fieldLastMouseEvent.set(screen, Minecraft.getSystemTime());
            methodMouseClicked = ReflectionHelper.findMethod(GuiScreen.class, screen, new String[]{"func_73864_a", "mouseClicked"}, int.class, int.class, int.class);
            methodMouseClicked.invoke(screen, x, y, button);
        } catch(IllegalAccessException|InvocationTargetException e){
            System.err.println("This error is likely caused by Reflection or LWJGL");
            e.printStackTrace();
        }
    }

    public static void mouseMovedOrUp(GuiScreen screen, int x, int y, int button){
        try {
            fieldEventButton.set(screen, -1);
            methodMouseMovedOrUp = ReflectionHelper.findMethod(GuiScreen.class, screen, new String[]{"func_146286_b", "mouseMovedOrUp"}, int.class, int.class, int.class);
            methodMouseMovedOrUp.invoke(screen, x, y, button);
        } catch(IllegalAccessException|InvocationTargetException e){
            System.err.println("This error is likely caused by Reflection or LWJGL");
            e.printStackTrace();
        }
    }

    public static void mouseClickMove(GuiScreen screen, int button, int x, int y){
        try {
            methodMouseClickMove = ReflectionHelper.findMethod(GuiScreen.class, screen, new String[]{"func_146273_a", "mouseClickMove"}, int.class, int.class, int.class, long.class);
            long l = Minecraft.getSystemTime() - fieldLastMouseEvent.get(screen);
            methodMouseClickMove.invoke(screen, x, y, button, l);
        } catch(IllegalAccessException|InvocationTargetException e){
            System.err.println("This error is likely caused by Reflection or LWJGL");
            e.printStackTrace();
        }
    }
}
