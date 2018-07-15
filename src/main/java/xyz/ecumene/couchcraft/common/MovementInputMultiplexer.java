package xyz.ecumene.couchcraft.common;

import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.ArrayList;

public class MovementInputMultiplexer extends MovementInput {
    public List<MovementInput> inputs;

    public MovementInputMultiplexer(MovementInput ... movementInputs){
        this.inputs = Arrays.asList(movementInputs);
    }

    @Override
    public void updatePlayerMoveState() {
        super.updatePlayerMoveState();

        moveForward = 0;
        moveStrafe = 0;

        jump = false;
        sneak = false;

        for(MovementInput input : inputs) {
            input.updatePlayerMoveState();
            moveForward += input.moveForward;
            moveStrafe += input.moveStrafe;

            if(input.jump) this.jump = true;
            if(input.sneak) this.sneak = true;
        }
    }
}
