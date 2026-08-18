package ru.mich.michmetallurgy;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.mich.michmetallurgy.MichMetallurgy;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MichMetallurgy.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<BigBlastFurnaceMenu>> BIG_BLAST_FURNACE_MENU =
            MENUS.register("big_blast_furnace_menu", () -> IMenuTypeExtension.create(BigBlastFurnaceMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<CokeOvenMenu>> COKE_OVEN_MENU =
            MENUS.register("coke_oven_menu", () -> IMenuTypeExtension.create(CokeOvenMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<PuddlingFurnaceMenu>> PUDDLING_FURNACE_MENU =
            MENUS.register("puddling_furnace_menu", () -> IMenuTypeExtension.create(PuddlingFurnaceMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus); // или имя твоего DeferredRegister
    }
}