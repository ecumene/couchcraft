package xyz.ecumene.couchcraft.common.binding;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class MultiKeyBinding extends KeyBindingInterceptor {
    public KeyBinding keyBinding;
    public ButtonBinding buttonBinding;

    public MultiKeyBinding(ButtonBinding buttonBinding, KeyBinding keyBinding) {
        super(keyBinding);
        this.keyBinding = keyBinding;
        this.buttonBinding = buttonBinding;
        setInterceptionActive(false);
    }

    @Override
    public boolean isPressed() {
        try {
            /**
             * TODO: Fix true/false click on old keybinding, separate Key & Button from Multi key binding, instead of
             * mimicking the original key binding but mutating it when the button is pressed.. Maybe come back with a
             * fresh head ;)
             */

            copyKeyCodeToOriginal();
            copyClickInfoFromOriginal();

            boolean pressed = fieldPressed.get(keyBinding);
            fieldPressed.set(this, buttonBinding.pressed | pressed);

            if (buttonBinding.pressTime == 0) {
                int pressTime = fieldPressTime.get(this);
                if (pressTime == 0) {
                    return false;
                } else {
                    fieldPressTime.set(this, --pressTime);
                    return true;
                }
            } else {
                fieldPressTime.set(this, --buttonBinding.pressTime);
                return true;
            }

        } catch (IllegalAccessException e){
            e.printStackTrace();
        }

        return false;
    }
}
