package com.awesomesink.network;

import java.util.List;

import com.awesomesink.AwesomeSink;
import com.awesomesink.client.ClientShopCatalog;
import com.awesomesink.data.ShopCatalog;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Server → client: the full shop catalog, sent on join and datapack reload. */
public record SyncShopCatalogPayload(List<ShopCatalog.Entry> entries) implements CustomPacketPayload {
    public static final Type<SyncShopCatalogPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AwesomeSink.MODID, "sync_shop_catalog"));

    private static final StreamCodec<RegistryFriendlyByteBuf, ShopCatalog.Entry> ENTRY_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ITEM), ShopCatalog.Entry::item,
            ByteBufCodecs.VAR_INT, ShopCatalog.Entry::price,
            ByteBufCodecs.VAR_INT, ShopCatalog.Entry::count,
            ShopCatalog.Entry::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncShopCatalogPayload> STREAM_CODEC =
            ENTRY_CODEC.apply(ByteBufCodecs.list())
                    .map(SyncShopCatalogPayload::new, SyncShopCatalogPayload::entries);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncShopCatalogPayload payload, IPayloadContext context) {
        ClientShopCatalog.set(payload.entries());
    }
}
