package ru.mich.michmetallurgy;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import ru.mich.michmetallurgy.MichMetallurgy;
import ru.mich.michmetallurgy.BigBlastFurnaceRecipe;

public class BigBlastFurnaceRecipeBuilder {
    private final Ingredient ingredient1;
    private final int count1;
    private final Ingredient ingredient2;
    private final int count2;
    private final ItemStack result;
    private final int cookingTime;
    private final float experience;

    public BigBlastFurnaceRecipeBuilder(Ingredient ingredient1, int count1,
                                        Ingredient ingredient2, int count2,
                                        ItemLike result, int resultCount,
                                        int cookingTime, float experience) {
        this.ingredient1 = ingredient1;
        this.count1 = count1;
        this.ingredient2 = ingredient2;
        this.count2 = count2;
        this.result = new ItemStack(result, resultCount);
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    public static BigBlastFurnaceRecipeBuilder recipe(Ingredient ingredient1, int count1,
                                                      Ingredient ingredient2, int count2,
                                                      ItemLike result, int resultCount,
                                                      int cookingTime, float experience) {
        return new BigBlastFurnaceRecipeBuilder(ingredient1, count1, ingredient2, count2, result, resultCount, cookingTime, experience);
    }

    public void save(RecipeOutput recipeOutput, String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, name);

        // Создаем экземпляр рецепта.
        // Убедись, что порядок параметров в конструкторе BigBlastFurnaceRecipe совпадает с твоим классом рецепта.
        BigBlastFurnaceRecipe recipe = new BigBlastFurnaceRecipe(
                ingredient1, count1,
                ingredient2, count2,
                result,
                cookingTime,
                experience
        );

        recipeOutput.accept(id, recipe, null);
    }
}