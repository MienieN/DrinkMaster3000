package src.client.entity;

import java.util.Objects;

/**
 * The Ingredient class represents an ingredient entity.
 */
public class Ingredient {
    private String name;        // The name of the ingredient
    private boolean alcoholic;
    private int frequency;

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


    public boolean getAlcoholic() {
        return alcoholic;
    }

    public int getFrequency(){
        return frequency;
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
    @Override
    public String toString() {
        return name;
    }
}

