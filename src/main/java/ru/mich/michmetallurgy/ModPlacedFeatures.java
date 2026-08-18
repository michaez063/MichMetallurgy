package ru.mich.michmetallurgy;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> ORE_LEAD_PLACED_KEY = registerKey("lead_ore_placed");
    public static final ResourceKey<PlacedFeature> ORE_TIN_PLACED_KEY = registerKey("tin_ore_placed");
    public static final ResourceKey<PlacedFeature> ORE_ZINC_PLACED_KEY = registerKey("zinc_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Свинец: 8 жил на чанк, спавн от Y=-16 до Y=48 (равномерно)
        register(context, ORE_LEAD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ORE_LEAD_KEY),
                OrePlacement.commonOrePlacement(10,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(48))));

        // Олово: 12 жил на чанк, спавн от Y=0 до Y=96
        register(context, ORE_TIN_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ORE_TIN_KEY),
                OrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(96))));

        register(context, ORE_ZINC_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ZINC_ORE_KEY),
                OrePlacement.commonOrePlacement(12, // 12 жил на чанк
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("michmetallurgy", name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 net.minecraft.core.Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}

// Вспомогательный класс для удобной настройки размещений
class OrePlacement {
    public static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier height) {
        return List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int countPerChunk, PlacementModifier height) {
        return orePlacement(CountPlacement.of(countPerChunk), height);
    }
}