package ru.mich.michmetallurgy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class BigBlastFurnaceRecipe implements Recipe<BigBlastFurnaceRecipeInput> {

    private final Ingredient ingredient1;
    private final int count1;
    private final Ingredient ingredient2;
    private final int count2;
    private final ItemStack result;
    private final int cookingTime;
    private final float experience;

    public BigBlastFurnaceRecipe(Ingredient ingredient1, int count1,
                                 Ingredient ingredient2, int count2,
                                 ItemStack result, int cookingTime, float experience) {
        this.ingredient1 = ingredient1;
        this.count1 = count1;
        this.ingredient2 = ingredient2;
        this.count2 = count2;
        this.result = result;
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    @Override
    public boolean matches(BigBlastFurnaceRecipeInput input, Level level) {
        int available1 = 0;
        int available2 = 0;

        // Подсчитываем, сколько нужных предметов лежит в слотах печки
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (this.ingredient1.test(stack)) {
                available1 += stack.getCount();
            } else if (this.ingredient2.test(stack)) {
                available2 += stack.getCount();
            }
        }

        // Рецепт совпадает, если предметов обоих типов хватает
        return available1 >= this.count1 && available2 >= this.count2;
    }

    @Override
    public ItemStack assemble(BigBlastFurnaceRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BIG_BLAST_FURNACE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BIG_BLAST_FURNACE_TYPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.ingredient1);
        list.add(this.ingredient2);
        return list;
    }

    // --- Геттеры для доступа к полям ---

    public Ingredient getIngredient1() { return ingredient1; }
    public int getCount1() { return count1; }
    public Ingredient getIngredient2() { return ingredient2; }
    public int getCount2() { return count2; }
    public ItemStack getResult() { return result; }
    public int getCookingTime() { return this.cookingTime; }
    public float getExperience() { return experience; }

    // --- Сериализация (Codec) для Minecraft 1.21 ---

    public static class Serializer implements RecipeSerializer<BigBlastFurnaceRecipe> {

        // CODEC отвечает за чтение и запись JSON-файлов
        public static final MapCodec<BigBlastFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(BigBlastFurnaceRecipe::getIngredient1),
                Codec.INT.fieldOf("count1").forGetter(BigBlastFurnaceRecipe::getCount1),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient2").forGetter(BigBlastFurnaceRecipe::getIngredient2),
                Codec.INT.fieldOf("count2").forGetter(BigBlastFurnaceRecipe::getCount2),
                ItemStack.CODEC.fieldOf("result").forGetter(BigBlastFurnaceRecipe::getResult),
                Codec.INT.fieldOf("cookingTime").orElse(200).forGetter(BigBlastFurnaceRecipe::getCookingTime),
                Codec.FLOAT.fieldOf("experience").orElse(0.0f).forGetter(BigBlastFurnaceRecipe::getExperience)
        ).apply(instance, BigBlastFurnaceRecipe::new));

        // STREAM_CODEC отвечает за передачу рецепта по сети (от сервера к клиенту)
        public static final StreamCodec<RegistryFriendlyByteBuf, BigBlastFurnaceRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getIngredient1());
                    ByteBufCodecs.VAR_INT.encode(buf, recipe.getCount1());
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getIngredient2());
                    ByteBufCodecs.VAR_INT.encode(buf, recipe.getCount2());
                    ItemStack.STREAM_CODEC.encode(buf, recipe.getResult());
                    ByteBufCodecs.INT.encode(buf, recipe.getCookingTime());
                    ByteBufCodecs.FLOAT.encode(buf, recipe.getExperience());
                },
                buf -> {
                    Ingredient ing1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    int count1 = ByteBufCodecs.VAR_INT.decode(buf);
                    Ingredient ing2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    int count2 = ByteBufCodecs.VAR_INT.decode(buf);
                    ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                    int cookingTime = ByteBufCodecs.INT.decode(buf);
                    float exp = ByteBufCodecs.FLOAT.decode(buf);
                    return new BigBlastFurnaceRecipe(ing1, count1, ing2, count2, result, cookingTime, exp);
                }
        );

        @Override
        public MapCodec<BigBlastFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BigBlastFurnaceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}