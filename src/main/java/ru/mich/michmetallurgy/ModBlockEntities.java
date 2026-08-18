package ru.mich.michmetallurgy;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.mich.michmetallurgy.BigBlastFurnaceBlockEntity;
import ru.mich.michmetallurgy.MichMetallurgy;
import ru.mich.michmetallurgy.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MichMetallurgy.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BigBlastFurnaceBlockEntity>> BIG_BLAST_FURNACE_BE =
            BLOCK_ENTITIES.register("big_blast_furnace_be", () ->
                    BlockEntityType.Builder.of(BigBlastFurnaceBlockEntity::new, ModBlocks.BIG_BLAST_FURNACE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CokeOvenBlockEntity>> COKE_OVEN_BE =
            BLOCK_ENTITIES.register("coke_oven_be", () ->
                    BlockEntityType.Builder.of(CokeOvenBlockEntity::new, ModBlocks.COKE_OVEN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PuddlingFurnaceBlockEntity>> PUDDLING_FURNACE_BE =
            BLOCK_ENTITIES.register("puddling_furnace_be", () ->
                    BlockEntityType.Builder.of(PuddlingFurnaceBlockEntity::new, ModBlocks.PUDDLING_FURNACE.get()).build(null));
}