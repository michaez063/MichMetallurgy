package ru.mich.michmetallurgy;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, "michmetallurgy", exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Здесь мы прописываем все блоки, которым нужна обычная текстура со всех сторон
        blockWithItem(ModBlocks.LEAD_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_LEAD_ORE);
        blockWithItem(ModBlocks.TIN_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_TIN_ORE);
        blockWithItem(ModBlocks.LEAD_BLOCK);
        blockWithItem(ModBlocks.TIN_BLOCK);
        blockWithItem(ModBlocks.RAW_LEAD_BLOCK);
        blockWithItem(ModBlocks.RAW_TIN_BLOCK);
        blockWithItem(ModBlocks.ZINC_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_ZINC_ORE);
        blockWithItem(ModBlocks.ZINC_BLOCK);
        blockWithItem(ModBlocks.RAW_ZINC_BLOCK);
        blockWithItem(ModBlocks.BLAST_FURNACE_CASING);
        // Пути к текстурам
        ResourceLocation frontTexture = modLoc("block/big_blast_furnace_front");
        ResourceLocation frontLitTexture = modLoc("block/big_blast_furnace_front_on"); // 1. Новая текстура
        ResourceLocation otherTexture = modLoc("block/big_blast_furnace_side");

// Модель выключенной печи
        var bigBlastFurnaceModel = models().cube(
                "big_blast_furnace",
                otherTexture, // Низ (Down)
                otherTexture, // Верх (Up)
                frontTexture, // Перед / Север (North)
                otherTexture, // Зад / Юг (South)
                otherTexture, // Лево / Запад (West)
                otherTexture  // Право / Восток (East)
        ).texture("particle", otherTexture);

// 2. Модель ВКЛЮЧЕННОЙ (горящей) печи
        var bigBlastFurnaceLitModel = models().cube(
                "big_blast_furnace_on",
                otherTexture,
                otherTexture,
                frontLitTexture, // Текстура с огнем
                otherTexture,
                otherTexture,
                otherTexture
        ).texture("particle", otherTexture);

// 3. Регистрируем блок с учетом поворота (FACING) и горения (LIT)
        getVariantBuilder(ModBlocks.BIG_BLAST_FURNACE.get()).forAllStates(state -> {
            Direction facing = state.getValue(BigBlastFurnaceBlock.FACING);
            boolean lit = state.getValue(BigBlastFurnaceBlock.LIT);


            return ConfiguredModel.builder()
                    .modelFile(lit ? bigBlastFurnaceLitModel : bigBlastFurnaceModel)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });

// Модель предмета в инвентаре (оставляем выключенную)
        simpleBlockItem(ModBlocks.BIG_BLAST_FURNACE.get(), bigBlastFurnaceModel);

        ResourceLocation cokeOvenFrontTexture = modLoc("block/coke_oven_front");
        ResourceLocation cokeOvenFrontLitTexture = modLoc("block/coke_oven_front_on");
        ResourceLocation cokeOvenOtherTexture = modLoc("block/coke_oven_side");

        var cokeOvenModel = models().cube(
                "coke_oven",
                cokeOvenOtherTexture,
                cokeOvenOtherTexture,
                cokeOvenFrontTexture,
                cokeOvenOtherTexture,
                cokeOvenOtherTexture,
                cokeOvenOtherTexture
        ).texture("particle", cokeOvenOtherTexture);

        var cokeOvenLitModel = models().cube(
                "coke_oven_on",
                cokeOvenOtherTexture,
                cokeOvenOtherTexture,
                cokeOvenFrontLitTexture,
                cokeOvenOtherTexture,
                cokeOvenOtherTexture,
                cokeOvenOtherTexture
        ).texture("particle", cokeOvenOtherTexture);

        getVariantBuilder(ModBlocks.COKE_OVEN.get()).forAllStates(state -> {
            Direction facing = state.getValue(CokeOvenBlock.FACING);
            boolean lit = state.getValue(CokeOvenBlock.LIT);

            return ConfiguredModel.builder()
                    .modelFile(lit ? cokeOvenLitModel : cokeOvenModel)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });

        simpleBlockItem(ModBlocks.COKE_OVEN.get(), cokeOvenModel);

        ResourceLocation puddlingFurnaceTexture = modLoc("block/puddling_furnace_side");
        ResourceLocation puddlingFurnaceFrontTexture = modLoc("block/puddling_furnace_front");
        ResourceLocation puddlingFurnaceFrontLitTexture = modLoc("block/puddling_furnace_front_on");

        var puddlingFurnaceModel = models().cube(
                "puddling_furnace",
                puddlingFurnaceTexture,
                puddlingFurnaceTexture,
                puddlingFurnaceFrontTexture,
                puddlingFurnaceTexture,
                puddlingFurnaceTexture,
                puddlingFurnaceTexture
        ).texture("particle", puddlingFurnaceTexture);

        var puddlingFurnaceLitModel = models().cube(
                "puddling_furnace_on",
                puddlingFurnaceTexture,
                puddlingFurnaceTexture,
                puddlingFurnaceFrontLitTexture,
                puddlingFurnaceTexture,
                puddlingFurnaceTexture,
                puddlingFurnaceTexture
        ).texture("particle", puddlingFurnaceTexture);

        getVariantBuilder(ModBlocks.PUDDLING_FURNACE.get()).forAllStates(state -> {
            Direction facing = state.getValue(PuddlingFurnaceBlock.FACING);
            boolean lit = state.getValue(PuddlingFurnaceBlock.LIT);

            return ConfiguredModel.builder()
                    .modelFile(lit ? puddlingFurnaceLitModel : puddlingFurnaceModel)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });

        simpleBlockItem(ModBlocks.PUDDLING_FURNACE.get(), puddlingFurnaceModel);
    }

    // Вспомогательный метод, который делает куб со всех сторон и сразу предмет для инвентаря
    private void blockWithItem(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}