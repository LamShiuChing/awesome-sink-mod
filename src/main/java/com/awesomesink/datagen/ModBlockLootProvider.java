package com.awesomesink.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.awesomesink.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

/** Generates block loot tables so the machines drop themselves when broken. */
public class ModBlockLootProvider extends LootTableProvider {
    public ModBlockLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(),
                List.of(new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)), registries);
    }

    private static class Blocks extends BlockLootSubProvider {
        protected Blocks(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            dropSelf(ModBlocks.AWESOME_SINK.get());
            dropSelf(ModBlocks.AWESOME_SHOP.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(ModBlocks.AWESOME_SINK.get(), ModBlocks.AWESOME_SHOP.get());
        }
    }
}
