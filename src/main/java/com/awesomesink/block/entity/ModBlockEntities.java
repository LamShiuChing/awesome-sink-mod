package com.awesomesink.block.entity;

import java.util.function.Supplier;

import com.awesomesink.AwesomeSink;
import com.awesomesink.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AwesomeSink.MODID);

    public static final Supplier<BlockEntityType<AwesomeSinkBlockEntity>> AWESOME_SINK =
            BLOCK_ENTITIES.register("awesome_sink", () -> BlockEntityType.Builder.of(
                    AwesomeSinkBlockEntity::new, ModBlocks.AWESOME_SINK.get()).build(null));

    public static final Supplier<BlockEntityType<AwesomeShopBlockEntity>> AWESOME_SHOP =
            BLOCK_ENTITIES.register("awesome_shop", () -> BlockEntityType.Builder.of(
                    AwesomeShopBlockEntity::new, ModBlocks.AWESOME_SHOP.get()).build(null));

    private ModBlockEntities() {}
}
