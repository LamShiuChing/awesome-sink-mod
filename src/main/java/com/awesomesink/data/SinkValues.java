package com.awesomesink.data;

import java.util.HashMap;
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
 * Loads {@code data/<namespace>/sink_values/*.json}. Each file is a JSON object mapping
 * item id -> point value; files merge. Items absent from the map are worth 0 (rejected).
 */
public class SinkValues extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    public static final SinkValues INSTANCE = new SinkValues();

    private Map<Item, Integer> values = Map.of();

    private SinkValues() {
        super(GSON, "sink_values");
    }

    public int get(Item item) {
        return values.getOrDefault(item, 0);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
        Map<Item, Integer> parsed = new HashMap<>();
        for (JsonElement file : files.values()) {
            for (Map.Entry<String, JsonElement> entry : file.getAsJsonObject().entrySet()) {
                ResourceLocation id = ResourceLocation.parse(entry.getKey());
                BuiltInRegistries.ITEM.getOptional(id)
                        .ifPresent(item -> parsed.put(item, entry.getValue().getAsInt()));
            }
        }
        values = Map.copyOf(parsed);
    }
}
