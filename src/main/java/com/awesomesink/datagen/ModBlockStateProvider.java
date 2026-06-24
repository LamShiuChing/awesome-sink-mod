package com.awesomesink.datagen;

import com.awesomesink.AwesomeSink;
import com.awesomesink.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper efh) {
        super(output, AwesomeSink.MODID, efh);
    }

    @Override
    protected void registerStatesAndModels() {
        machine(ModBlocks.AWESOME_SINK.get());
        machine(ModBlocks.AWESOME_SHOP.get());
    }

    private void machine(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        horizontalBlock(block, models().cubeAll(name, blockTexture(block)));
    }
}
