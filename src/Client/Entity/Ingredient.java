package src.Client.Entity;

public enum Ingredient {
    Whisky("Whisky"),
    AngosturaBitters("Angostura Bitters"),
    SimpleSyrup("Simple Syrup"),
    EggWhite("Egg White"),
    Lemon("Lemon"),
    Amaretto("Amaretto"),
    Ice("Ice"),
    Orange("Orange"),
    ;

    Ingredient(String name) {
    }

    public String getName() {
        return this.name();
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

