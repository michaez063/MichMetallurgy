package ru.mich.michmetallurgy;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import ru.mich.michmetallurgy.MichMetallurgy;
import ru.mich.michmetallurgy.ModMenuTypes;
import ru.mich.michmetallurgy.BigBlastFurnaceScreen;

@EventBusSubscriber(modid = MichMetallurgy.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BIG_BLAST_FURNACE_MENU.get(), BigBlastFurnaceScreen::new);
        event.register(ModMenuTypes.COKE_OVEN_MENU.get(), CokeOvenScreen::new);
        event.register(ModMenuTypes.PUDDLING_FURNACE_MENU.get(), PuddlingFurnaceScreen::new);
    }
}