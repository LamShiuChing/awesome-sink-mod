package com.awesomesink.registry;

import java.util.function.Supplier;

import com.awesomesink.AwesomeSink;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, AwesomeSink.MODID);

    /** Number of FICSIT Coupons a single coupon token represents. */
    public static final Supplier<DataComponentType<Long>> COUPON_AMOUNT = COMPONENTS.register("coupon_amount",
            () -> DataComponentType.<Long>builder()
                    .persistent(Codec.LONG)
                    .networkSynchronized(ByteBufCodecs.VAR_LONG)
                    .build());

    private ModDataComponents() {}
}
