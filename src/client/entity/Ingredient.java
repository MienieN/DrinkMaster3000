package src.client.entity;

import java.util.Objects;

/**
 * Class that represents an ingredient entity.
 */
public class Ingredient {
    private String name;        // The name of the ingredient
    private boolean alcoholic;  // Whether the ingredient is alcoholic

    /**
     * Constructs an Ingredient object with the specified name.
     *
     * @param name The name of the ingredient.
     */
    public Ingredient(String name, boolean alcoholic) {
        this.name = name;
        this.alcoholic = alcoholic;
    }

    /**
     * TODO not used at the moment
     * Constructs an Ingredient object with the specified name.
     *
     * @param name The name of the ingredient.
     */
    public Ingredient(String name){
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

    /**
     * Compares this ingredient to the specified object.
     *
     * @param obj The object to compare this ingredient to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        Ingredient otherIngredient = (Ingredient) obj;
        return Objects.equals(this.name, otherIngredient.getName());
    }

    /**
     * Returns the hash code of the ingredient.
     *
     * @return The hash code of the ingredient.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns the string representation of the ingredient.
     *
     * @return The string representation of the ingredient.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns whether the ingredient is alcoholic.
     *
     * @return True if the ingredient is alcoholic, false otherwise.
     */
    public boolean getAlcoholic() {
        return alcoholic;
    }
}

