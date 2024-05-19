package src.client.entity;

import java.util.Objects;

/**
 * The Ingredient class represents an ingredient entity.
 */
public class Ingredient {
    private String name;        // The name of the ingredient
    private boolean alcoholic;  // A boolean indicating if the ingredient contains alcohol or not
    private int frequency;      // The number of recipes using the ingredient

    /**
     * Constructor for ingredient.
     * @param name the name of the ingredient.
     * @param alcoholic a boolean indicating if the ingredient contains alcohol or not.
     * @param frequency the number of recipes using the ingredient.
     */
    public Ingredient(String name, boolean alcoholic, int frequency) {
        this.name = name;
        this.alcoholic = alcoholic;
        this.frequency = frequency;
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
     * Gets the boolean indicating if the ingredient contains alcohol or not.
     *
     * @return A boolean indicating if the ingredient contains alcohol or not.
     */
    public boolean getAlcoholic() {
        return alcoholic;
    }

    /**
     * Gets the number of recipes using the ingredient.
     *
     * @return The number of recipes using the ingredient.
     * TODO - not used
     */
    public int getFrequency(){
        return frequency;
    }

    /**
     * Compares two ingredients for equality.
     *
     * @param obj The object to compare to.
     * @return True if the ingredients are equal, false otherwise.
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
     * Hash code for the ingredient.
     *
     * @return The hash code of the ingredient.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * String representation of the ingredient.
     *
     * @return The name of the ingredient.
     */
    @Override
    public String toString() {
        return name;
    }
}