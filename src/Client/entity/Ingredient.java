package src.Client.entity;

import java.util.Objects;

/**
 * The Ingredient class represents an ingredient entity.
 */
public class Ingredient {
    private String name;        // The name of the ingredient

    /**
     * Constructs an Ingredient object with the specified name.
     *
     * @param name The name of the ingredient.
     */
    public Ingredient(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the ingredient.
     *
     * @return The name of the ingredient.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        Ingredient otherIngredient = (Ingredient) obj;
        return Objects.equals(this.name, otherIngredient.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

