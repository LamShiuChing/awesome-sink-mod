package com.awesomesink.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

/**
 * Loads {@code data/<namespace>/shop_catalog/*.json}. Each file is a JSON array of
 * {@code {"item": "...", "price": n, "count": n}} entries; all files merge into one ordered catalog.
 */
public class ShopCatalog extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    public static final ShopCatalog INSTANCE = new ShopCatalog();

    public record Entry(Item item, int price, int count) {}

    private List<Entry> entries = List.of();

    private ShopCatalog() {
        super(GSON, "shop_catalog");
    }

    public int size() {
        return entries.size();
    }

    public Entry get(int index) {
        return entries.isEmpty() ? null : entries.get(Math.floorMod(index, entries.size()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
        List<Entry> parsed = new ArrayList<>();
        for (JsonElement file : files.values()) {
            for (JsonElement element : file.getAsJsonArray()) {
                JsonObject obj = element.getAsJsonObject();
                ResourceLocation id = ResourceLocation.parse(obj.get("item").getAsString());
                int price = obj.get("price").getAsInt();
                int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
                BuiltInRegistries.ITEM.getOptional(id)
                        .ifPresent(item -> parsed.add(new Entry(item, price, count)));
            }
        }
        entries = List.copyOf(parsed);
    }
}
