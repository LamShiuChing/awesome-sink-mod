package com.awesomesink.network;

import java.util.HashMap;
import java.util.Map;

import com.awesomesink.AwesomeSink;
import com.awesomesink.data.SinkValuation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Server → client: the JSON sink-value overrides, so client tooltips match the sink. */
public record SyncSinkOverridesPayload(Map<Item, Integer> overrides) implements CustomPacketPayload {
    public static final Type<SyncSinkOverridesPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AwesomeSink.MODID, "sync_sink_overrides"));

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Item, Integer>> MAP_CODEC =
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.registry(Registries.ITEM), ByteBufCodecs.VAR_INT);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSinkOverridesPayload> STREAM_CODEC =
            MAP_CODEC.map(SyncSinkOverridesPayload::new, SyncSinkOverridesPayload::overrides);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncSinkOverridesPayload payload, IPayloadContext context) {
        SinkValuation.setOverrides(payload.overrides());
    }
}
