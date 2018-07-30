package xyz.ecumene.couchcraft.common.cursor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.util.HashMap;
import java.util.Map;

public class GuiCursorTargets<T> {
    public Map<Vector2f, T> targets;
    public GuiScreen targetDB;

    public boolean newGuiFlag;
    public boolean moveLeft, moveRight, moveDown, moveUp;

    public Vector2f target;
    public T button;

    public static int targetX, targetY;
    public GuiButton currentTarget;

    public GuiCursorTargets(){
        targets = new HashMap<>();
    }

    public void tick(){
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

    public void chooseFirst(){
        if(targets.size() > 0)
            this.target = (Vector2f)targets.keySet().toArray()[0]; // TODO: Oof, more like choose random
    }

    public void setTargetDB(GuiScreen screen) {
        this.targetDB = screen;
        newGuiFlag = true;
    }

    public void clearTargets(){
        targets.clear();
    }

    /**TODO: Determine current target somehow else?*/
    private static Vector2f mouseCoords = new Vector2f();
    public static Vector2f getCurrentMouse() {
        mouseCoords.set(targetX, targetY);
        return mouseCoords;
    }

    public Vector2f getNextTarget(){
        return target == null  ? new Vector2f(targetDB.width/2, targetDB.height/2) : target;
    }

    public Vector2f closer(Vector2f currentPosition, Vector2f t1, Vector2f t2){
        Vector2f distance1 = new Vector2f(t1);
        distance1.sub(currentPosition);
        Vector2f distance2 = new Vector2f(t2);
        distance2.sub(currentPosition);

        if (distance1.length() < distance2.length()) return t1;
        else return t2;
    }

    public void moveUp(){
        if(targets.size() != 0) {
            Vector2f currentTarget = getCurrentMouse();
            Vector2f closest = new Vector2f(-1, -1);
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y < currentTarget.y)
                    closest = closer(currentTarget, screenTarget, closest);
            if (!closest.equals(new Vector2f(-1, -1))) {
                this.target = closest;
                this.button = targets.get(target);
            } else lowerMost();
        }
    }

    public void moveDown(){
        if(targets.size() != 0) {
            Vector2f currentTarget = getCurrentMouse();
            Vector2f closest = new Vector2f(-1, -1);
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y > currentTarget.y)
                    closest = closer(currentTarget, screenTarget, closest);
            if (!closest.equals(new Vector2f(-1, -1))) {
                this.target = closest;
                this.button = targets.get(target);//TODO: Itr via hashmap itr for reduced lookups
            } else upperMost();
        }
    }

    public void moveRight(){
        if(targets.size() != 0) {
            Vector2f currentTarget = getCurrentMouse();
            Vector2f toTheRight = new Vector2f(-1, -1);
            boolean moved = false;
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y == currentTarget.y && screenTarget.x > currentTarget.x)
                    toTheRight = closer(currentTarget, screenTarget, toTheRight);
            if (!toTheRight.equals(new Vector2f(-1, -1))) {
                this.target = toTheRight;
                this.button = targets.get(target);
                moved = true;
            } else {
                Vector2f closest = new Vector2f(-1, -1);
                for (Vector2f screenTarget : targets.keySet())
                    if (screenTarget.x > currentTarget.x)
                        closest = closer(currentTarget, screenTarget, closest);
                if (!closest.equals(new Vector2f(-1, -1))) {
                    this.target = closest;
                    this.button = targets.get(target);
                    moved = true;
                }
            }
            if (!moved) leftMost();
        }
    }

    public void moveLeft(){
        if(targets.size() != 0) {
            Vector2f currentTarget = getCurrentMouse();
            Vector2f toTheLeft = new Vector2f(-1, -1);
            boolean moved = false;
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y == currentTarget.y && screenTarget.x < currentTarget.x)
                    toTheLeft = closer(currentTarget, screenTarget, toTheLeft);
            if (!toTheLeft.equals(new Vector2f(-1, -1))) {
                this.target = toTheLeft;
                this.button = targets.get(target);
                moved = true;
            } else {
                Vector2f closest = new Vector2f(-1, -1);
                for (Vector2f screenTarget : targets.keySet())
                    if (screenTarget.x < currentTarget.x)
                        closest = closer(currentTarget, screenTarget, closest);
                if (!closest.equals(new Vector2f(-1, -1))) {
                    this.target = closest;
                    this.button = targets.get(target);
                    moved = true;
                }
            }
            if (!moved) rightMost();
        }
    }

    public void leftMost(){
        if(targets.size() != 0) {
            Vector2f currentTarget = getCurrentMouse();
            Vector2f toTheLeft = target;
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y == currentTarget.y && screenTarget.x < toTheLeft.x)
                    toTheLeft = screenTarget;
            if (!toTheLeft.equals(target))
                this.target = toTheLeft;
        }
    }

    public void rightMost(){
        if(targets.size() != 0) {
            Vector2f currentTarget = getCurrentMouse();
            Vector2f toTheRight = target;
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y == currentTarget.y && screenTarget.x > toTheRight.x)
                    toTheRight = screenTarget;
            if (!toTheRight.equals(target))
                this.target = toTheRight;
        }
    }

    public void upperMost(){
        if(targets.size() != 0) {
            Vector2f highest = target;
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y < highest.y)
                    highest = screenTarget;
            if (!highest.equals(target))
                this.target = highest;
        }
    }

    public void lowerMost(){
        if(targets.size() != 0) {
            Vector2f lowest = target;
            for (Vector2f screenTarget : targets.keySet())
                if (screenTarget.y > lowest.y)
                    lowest = screenTarget;
            if (!lowest.equals(target))
                this.target = lowest;
        }
    }
}
