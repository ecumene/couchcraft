package xyz.ecumene.couchcraft.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import xyz.ecumene.couchcraft.common.ControllerContext;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonTargets {
    public List<Vector2f> targets;
    public GuiScreen targetDB;

    static FieldHelper<GuiScreen, List> fieldButtonList = new FieldHelper<>(GuiScreen.class, "field_146292_n", "buttonList");

    public boolean moveLeft, moveRight, moveDown, moveUp;
    public Vector2f target;

    public void tick() {
        if(Minecraft.getMinecraft().currentScreen != null) { // Accessing buttons that aren't on screen doesn't make sense, go away ;)
            Vector2f currentTarget = getCurrentTarget();

            //TODO: Multipress?
            //TODO: Not judging distance based on center in all cases, but instead button size as well
            if (moveRight) {
                Vector2f closest = new Vector2f(-1, -1);
                for (Vector2f screenTarget : targets) {
                    if (screenTarget.x > currentTarget.x) {
                        Vector2f distance1 = new Vector2f(screenTarget);
                        distance1.sub(getCurrentTarget());
                        Vector2f distance2 = new Vector2f(closest);
                        distance2.sub(getCurrentTarget());

                        if (distance1.length() < distance2.length()) {
                            closest = screenTarget;
                        }
                    }
                }
                if (!closest.equals(new Vector2f(-1, -1)))
                    this.target = closest;
            }
            if (moveLeft) {
                Vector2f closest = new Vector2f(-1, -1);
                for (Vector2f screenTarget : targets) {
                    if (screenTarget.x < currentTarget.x) {
                        Vector2f distance1 = new Vector2f(screenTarget);
                        distance1.sub(getCurrentTarget());
                        Vector2f distance2 = new Vector2f(closest);
                        distance2.sub(getCurrentTarget());

                        if (distance1.length() < distance2.length()) {
                            closest = screenTarget;
                        }
                    }
                }
                if (!closest.equals(new Vector2f(-1, -1)))
                    this.target = closest;
            }
            if (moveUp) {
                Vector2f closest = new Vector2f(-1, -1);
                for (Vector2f screenTarget : targets) {
                    if (screenTarget.y < currentTarget.y) {
                        Vector2f distance1 = new Vector2f(screenTarget);
                        distance1.sub(getCurrentTarget());
                        Vector2f distance2 = new Vector2f(closest);
                        distance2.sub(getCurrentTarget());

                        if (distance1.length() < distance2.length()) {
                            closest = screenTarget;
                        }
                    }
                }
                if (!closest.equals(new Vector2f(-1, -1)))
                    this.target = closest;
            }
            if (moveDown) {
                Vector2f closest = new Vector2f(-1, -1);
                for (Vector2f screenTarget : targets) {
                    if (screenTarget.y > currentTarget.y) {
                        Vector2f distance1 = new Vector2f(screenTarget);
                        distance1.sub(getCurrentTarget());
                        Vector2f distance2 = new Vector2f(closest);
                        distance2.sub(getCurrentTarget());

                        if (distance1.length() < distance2.length()) {
                            closest = screenTarget;
                        }
                    }
                }
                if (!closest.equals(new Vector2f(-1, -1)))
                    this.target = closest;
            }

            if (Mouse.getX() == 0 && Mouse.getY() == 0)
                chooseFirst();
        }
    }

    private static Vector2f mouseCoords = new Vector2f();

    public static Vector2f getCurrentTarget() {
        mouseCoords.set(ControllerContext.targetMouseX, ControllerContext.targetMouseY);
        return mouseCoords;
    }

    public Vector2f getNextTarget(){
        return target == null  ? new Vector2f(targetDB.width/2, targetDB.height/2) : target;
    }

    public GuiButtonTargets(){
        targets = new ArrayList<>();
    }

    public void setTargetDB(GuiScreen screen) {
        this.targetDB = screen;
     }

    public void chooseFirst(){
        if(targets.size() > 0)
            this.target = targets.get(0);
    }

    public void searchTargets() throws IllegalAccessException{
        if(Minecraft.getMinecraft().currentScreen != null && targetDB != null) { // Accessing buttons that aren't on screen doesn't make sense, go away ;)
            clearTargets();
            for (GuiButton button : ((List<GuiButton>) fieldButtonList.get(targetDB))) {
                if (button.visible)
                    targets.add(new Vector2f(button.xPosition + (button.width / 2f), button.yPosition + (button.height / 2f)));
            }
        }
    }

    public void clearTargets(){
        targets.clear();
    }

}
