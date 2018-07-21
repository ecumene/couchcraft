package xyz.ecumene.couchcraft.utils;

import cpw.mods.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class FieldHelper<C, T> {
    public Class<?> target;
    public String srgName;
    public String prettyName;
    public Field field;

    public FieldHelper(Class<? extends C> target, String srgName, String prettyName) {
        this.target = target;
        this.srgName = srgName;
        this.prettyName = prettyName;
    }

    private boolean ensureHaveField() {
        if (field == null) {
            if(ReflectionHelper.findField(target, srgName, prettyName).getName() == null) return false;
            field = ReflectionHelper.findField(target, srgName, prettyName);
            if(field == null) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public T get(C binding) throws IllegalAccessException {
        if(ensureHaveField())
            return (T) field.get(binding);
        return null;
    }

    public void set(C binding, T value) throws IllegalAccessException {
        if(ensureHaveField())
            field.set(binding, value);
    }
}
