package ru.mich.michmetallurgy;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.mich.michmetallurgy.MichMetallurgy;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MichMetallurgy.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MichMetallurgy.MOD_ID);

    public static final Supplier<RecipeType<BigBlastFurnaceRecipe>> BIG_BLAST_FURNACE_TYPE =
            RECIPE_TYPES.register("big_blast_furnace", () -> new RecipeType<BigBlastFurnaceRecipe>() {
                @Override
                public String toString() {
                    return "big_blast_furnace";
                }
            });

    public static final Supplier<RecipeSerializer<BigBlastFurnaceRecipe>> BIG_BLAST_FURNACE_SERIALIZER =
            RECIPE_SERIALIZERS.register("big_blast_furnace", BigBlastFurnaceRecipe.Serializer::new);
}