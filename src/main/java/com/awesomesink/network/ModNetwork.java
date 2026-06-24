package com.awesomesink.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetwork {
    private ModNetwork() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SetSideModePayload.TYPE, SetSideModePayload.STREAM_CODEC, SetSideModePayload::handle);
        registrar.playToServer(BuyShopItemPayload.TYPE, BuyShopItemPayload.STREAM_CODEC, BuyShopItemPayload::handle);
        registrar.playToClient(SyncShopCatalogPayload.TYPE, SyncShopCatalogPayload.STREAM_CODEC, SyncShopCatalogPayload::handle);
        registrar.playToClient(SyncSinkOverridesPayload.TYPE, SyncSinkOverridesPayload.STREAM_CODEC, SyncSinkOverridesPayload::handle);
    }
}
