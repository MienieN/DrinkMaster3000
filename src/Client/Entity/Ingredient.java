package src.Client.Entity;

public class Ingredient {
    private String name;

    Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String[] getIngredientNames(){
        String[] ingredientNames = new String[Ingredient.values().length];
        for (int i = 0; i < ingredientNames.length; i++) {
            ingredientNames[i] = Ingredient.values()[i].getName();
        }
        return ingredientNames;
    }
    public static Ingredient getIngredient(String name){
        return Ingredient.valueOf(name);
    }
}

