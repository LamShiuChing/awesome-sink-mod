package com.awesomesink.network;

import com.awesomesink.data.ShopCatalog;
import com.awesomesink.data.SinkValues;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Pushes the shop catalog and sink-value overrides to clients on login and datapack reload. */
public final class ShopSync {
    private ShopSync() {}

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        send(event, new SyncShopCatalogPayload(ShopCatalog.INSTANCE.all()));
        send(event, new SyncSinkOverridesPayload(SinkValues.INSTANCE.all()));
    }

    private static void send(OnDatapackSyncEvent event, CustomPacketPayload payload) {
        if (event.getPlayer() != null) {
            PacketDistributor.sendToPlayer(event.getPlayer(), payload);
        } else {
            PacketDistributor.sendToAllPlayers(payload);
        }
    }
}
