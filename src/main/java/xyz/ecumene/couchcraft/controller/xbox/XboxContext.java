package xyz.ecumene.couchcraft.controller.xbox;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import xyz.ecumene.couchcraft.common.ControllerContext;

public class XboxContext extends ControllerContext {

    public XboxContext(){
        super();
    }

    @Override
    protected void pollInputs(Controller controller) {
        Component[] components = controller.getComponents();

        for(Component component : components) {
            Component.Identifier componentIdentifier = component.getIdentifier();
            String name = componentIdentifier.toString();

            if(name.equals("A")) buttonPoll(button1, component);
            if(name.equals("B")) buttonPoll(button2, component);
            if(name.equals("X")) buttonPoll(button3, component);
            if(name.equals("Y")) buttonPoll(button4, component);

            if(name.equals("x")) axisPollX(leftThumb, component);
            if(name.equals("y")) axisPollY(leftThumb, component);

            if(name.equals("rx")) axisPollX(rightThumb, component);
            if(name.equals("ry")) axisPollY(rightThumb, component);

            if(name.equals("rz")) axisPollX(triggers, component);
            if(name.equals("z"))  axisPollY(triggers, component);

            if(name.equals("Select")) buttonPoll(select, component);
            if(name.equals("Unknown")) buttonPoll(start, component);

            if(name.trim().equals("Left Thumb 3")) buttonPoll(stickDownLeft, component);
            if(name.trim().equals("Right Thumb 3")) buttonPoll(stickDownRight, component);

            if(name.trim().equals("Left Thumb")) buttonPoll(bumperLeft, component);
            if(name.trim().equals("Right Thumb")) buttonPoll(bumperRight, component);

            if(name.equals("pov")) povPoll(povUp, povLeft, povDown, povRight, component.getPollData());
        }
    }
}
