package src.client.entity;

import java.util.Objects;

public class Instructions {

    private String instruction;        // The instructions


    public Instructions(String instruction) {
        this.instruction = instruction;

    }

    public String getInstruction() {
        return instruction;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        src.client.entity.Instructions otherInstructions = (src.client.entity.Instructions) obj;
        return Objects.equals(this.instruction, otherInstructions.getInstruction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(instruction);
    }

    @Override
    public String toString() {
        return instruction;
    }
}
