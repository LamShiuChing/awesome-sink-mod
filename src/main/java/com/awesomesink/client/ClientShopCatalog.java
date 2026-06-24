package com.awesomesink.client;

import java.util.List;

import com.awesomesink.data.ShopCatalog;

/** Client-side copy of the shop catalog, pushed from the server on join / datapack reload. */
public final class ClientShopCatalog {
    private static List<ShopCatalog.Entry> entries = List.of();

    private ClientShopCatalog() {}

    public static void set(List<ShopCatalog.Entry> received) {
        entries = List.copyOf(received);
    }

    public static List<ShopCatalog.Entry> entries() {
        return entries;
    }
}
