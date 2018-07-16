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

    public boolean newGuiFlag;

    public void tick() {
        if(Minecraft.getMinecraft().currentScreen != null) { // Accessing buttons that aren't on screen doesn't make sense, go away ;)
            if(newGuiFlag) {
                chooseFirst();
                newGuiFlag = false;
            }

            //TODO: Multipress?
            if (moveRight) moveRight();
            if (moveLeft) moveLeft();
            if (moveUp) moveUp();
            if (moveDown) moveDown();

            if (Mouse.getX() == 0 && Mouse.getY() == 0)
                chooseFirst();
        }
    }

    public void moveUp(){
        Vector2f currentTarget = getCurrentMouse();
        Vector2f closest = new Vector2f(-1, -1);
        for (Vector2f screenTarget : targets)
            if (screenTarget.y < currentTarget.y)
                closest = closer(currentTarget, screenTarget, closest);
        if (!closest.equals(new Vector2f(-1, -1)))
            this.target = closest;
        else lowerMost();
    }

    public void moveDown(){
        Vector2f currentTarget = getCurrentMouse();
        Vector2f closest = new Vector2f(-1, -1);
        for (Vector2f screenTarget : targets)
            if (screenTarget.y > currentTarget.y)
                closest = closer(currentTarget, screenTarget, closest);
        if (!closest.equals(new Vector2f(-1, -1)))
            this.target = closest;
        else upperMost();
    }

    public void moveRight(){
        Vector2f currentTarget = getCurrentMouse();
        Vector2f toTheRight = new Vector2f(-1, -1);
        boolean moved = false;
        for (Vector2f screenTarget : targets)
            if (screenTarget.y == currentTarget.y && screenTarget.x > currentTarget.x)
                toTheRight = closer(currentTarget, screenTarget, toTheRight);
        if (!toTheRight.equals(new Vector2f(-1, -1))) {
            this.target = toTheRight;
            moved = true;
        }
        else {
            Vector2f closest = new Vector2f(-1, -1);
            for (Vector2f screenTarget : targets)
                if (screenTarget.x > currentTarget.x)
                    closest = closer(currentTarget, screenTarget, closest);
            if (!closest.equals(new Vector2f(-1, -1))) {
                this.target = closest;
                moved = true;
            }
        }
        if(!moved) leftMost();
    }

    public void moveLeft(){
        Vector2f currentTarget = getCurrentMouse();
        Vector2f toTheLeft = new Vector2f(-1, -1);
        boolean moved = false;
        for (Vector2f screenTarget : targets)
            if (screenTarget.y == currentTarget.y && screenTarget.x < currentTarget.x)
                toTheLeft = closer(currentTarget, screenTarget, toTheLeft);
        if (!toTheLeft.equals(new Vector2f(-1, -1))) {
            this.target = toTheLeft;
            moved = true;
        }
        else {
            Vector2f closest = new Vector2f(-1, -1);
            for (Vector2f screenTarget : targets)
                if (screenTarget.x < currentTarget.x)
                    closest = closer(currentTarget, screenTarget, closest);
            if (!closest.equals(new Vector2f(-1, -1))) {
                this.target = closest;
                moved = true;
            }
        }
        if(!moved) rightMost();
    }

    public void leftMost(){
        Vector2f currentTarget = getCurrentMouse();
        Vector2f toTheLeft = target;
        for (Vector2f screenTarget : targets)
            if (screenTarget.y == currentTarget.y && screenTarget.x < toTheLeft.x)
                toTheLeft = screenTarget;
        if (!toTheLeft.equals(target))
            this.target = toTheLeft;
    }

    public void rightMost(){
        Vector2f currentTarget = getCurrentMouse();
        Vector2f toTheRight = target;
        for (Vector2f screenTarget : targets)
            if (screenTarget.y == currentTarget.y && screenTarget.x > toTheRight.x)
                toTheRight = screenTarget;
        if (!toTheRight.equals(target))
            this.target = toTheRight;
    }

    public void upperMost(){
        Vector2f highest = target;
        for (Vector2f screenTarget : targets)
            if (screenTarget.y < highest.y)
                highest = screenTarget;
        if (!highest.equals(target))
            this.target = highest;
    }

    public void lowerMost(){
        Vector2f lowest = target;
        for (Vector2f screenTarget : targets)
            if (screenTarget.y > lowest.y)
                lowest = screenTarget;
        if (!lowest.equals(target))
            this.target = lowest;
    }

    public Vector2f closer(Vector2f currentPosition, Vector2f t1, Vector2f t2){
        Vector2f distance1 = new Vector2f(t1);
        distance1.sub(currentPosition);
        Vector2f distance2 = new Vector2f(t2);
        distance2.sub(currentPosition);

        if (distance1.length() < distance2.length()) return t1;
        else return t2;
    }

    private static Vector2f mouseCoords = new Vector2f();

    public static Vector2f getCurrentMouse() {
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
        newGuiFlag = true;
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
