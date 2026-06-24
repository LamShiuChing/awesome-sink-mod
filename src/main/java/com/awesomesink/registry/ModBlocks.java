package com.awesomesink.registry;

import com.awesomesink.AwesomeSink;
import com.awesomesink.block.AwesomeShopBlock;
import com.awesomesink.block.AwesomeSinkBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AwesomeSink.MODID);

    public static final DeferredBlock<AwesomeSinkBlock> AWESOME_SINK = BLOCKS.register("awesome_sink",
            () -> new AwesomeSinkBlock(machineProps()));

    public static final DeferredBlock<AwesomeShopBlock> AWESOME_SHOP = BLOCKS.register("awesome_shop",
            () -> new AwesomeShopBlock(machineProps()));

    private static BlockBehaviour.Properties machineProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .strength(3.5F)
                .requiresCorrectToolForDrops()
                .noOcclusion();
    }

    private ModBlocks() {}
}
