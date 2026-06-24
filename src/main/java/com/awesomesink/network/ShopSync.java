package com.awesomesink.network;

import com.awesomesink.data.ShopCatalog;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Pushes the shop catalog to clients on login and on datapack reload. */
public final class ShopSync {
    private ShopSync() {}

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        SyncShopCatalogPayload payload = new SyncShopCatalogPayload(ShopCatalog.INSTANCE.all());
        if (event.getPlayer() != null) {
            PacketDistributor.sendToPlayer(event.getPlayer(), payload);
        } else {
            PacketDistributor.sendToAllPlayers(payload);
        }
    }
}
