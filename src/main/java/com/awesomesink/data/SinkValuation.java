package com.awesomesink.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.awesomesink.Config;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;

/**
 * Sink value from production cost (Satisfactory-style). A {@code result -> recipes} index is built
 * once per recipe set so each lookup is O(1); values recurse to leaves and are memoized + cycle-guarded.
 *
 * <p>Value = max(rarity-scaled base, cheapest recipe's summed ingredient values / output count),
 * times a non-stackable multiplier. JSON {@code sink_values} entries override the result entirely.
 */
public final class SinkValuation {
    private static final int MAX_DEPTH = 8;

    private record Entry(List<Ingredient> ingredients, int count) {}

    private static volatile Map<Item, Integer> overrides = Map.of();
    private static volatile Map<Item, List<Entry>> index;
    private static final Map<Item, Integer> cache = new ConcurrentHashMap<>();
    private static final Object LOCK = new Object();

    private SinkValuation() {}

    public static void setOverrides(Map<Item, Integer> map) {
        overrides = Map.copyOf(map);
        invalidate();
    }

    @Nullable
    public static Integer overrideOf(Item item) {
        return overrides.get(item);
    }

    public static void invalidate() {
        cache.clear();
        index = null;
    }

    public static int value(ItemStack stack, RecipeManager recipes, RegistryAccess access) {
        Integer override = overrides.get(stack.getItem());
        if (override != null) {
            return override;
        }
        ensureIndex(recipes, access);
        return compute(stack, new HashSet<>(), 0);
    }

    private static void ensureIndex(RecipeManager recipes, RegistryAccess access) {
        if (index != null) {
            return;
        }
        synchronized (LOCK) {
            if (index == null) {
                index = buildIndex(recipes, access);
            }
        }
    }

    private static Map<Item, List<Entry>> buildIndex(RecipeManager recipes, RegistryAccess access) {
        Map<Item, List<Entry>> idx = new HashMap<>();
        for (RecipeHolder<?> holder : recipes.getRecipes()) {
            Recipe<?> recipe = holder.value();
            List<Ingredient> ingredients = recipe.getIngredients();
            if (ingredients.isEmpty()) {
                continue;
            }
            ItemStack result = recipe.getResultItem(access);
            if (result == null || result.isEmpty()) {
                continue;
            }
            idx.computeIfAbsent(result.getItem(), k -> new ArrayList<>(2))
                    .add(new Entry(ingredients, Math.max(1, result.getCount())));
        }
        return idx;
    }

    private static int compute(ItemStack stack, Set<Item> visiting, int depth) {
        Item item = stack.getItem();
        Integer cached = cache.get(item);
        if (cached != null) {
            return cached;
        }
        int base = baseValue(stack);
        if (depth >= MAX_DEPTH || !visiting.add(item)) {
            return base;
        }
        int value = base;
        List<Entry> entries = index.get(item);
        if (entries != null) {
            int cheapest = Integer.MAX_VALUE;
            for (Entry entry : entries) {
                long sum = 0;
                for (Ingredient ingredient : entry.ingredients()) {
                    if (!ingredient.isEmpty()) {
                        sum += cheapestOption(ingredient, visiting, depth + 1);
                    }
                }
                cheapest = Math.min(cheapest, Mth.ceil(sum / (double) entry.count()));
            }
            if (cheapest != Integer.MAX_VALUE) {
                value = Math.max(base, cheapest);
            }
        }
        visiting.remove(item);
        int result = stack.getMaxStackSize() == 1 ? value * Config.NON_STACKABLE_MULT.get() : value;
        cache.put(item, result);
        return result;
    }

    private static int cheapestOption(Ingredient ingredient, Set<Item> visiting, int depth) {
        int min = Integer.MAX_VALUE;
        for (ItemStack option : ingredient.getItems()) {
            Integer override = overrides.get(option.getItem());
            min = Math.min(min, override != null ? override : compute(option, visiting, depth));
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    private static int baseValue(ItemStack stack) {
        int base = Config.SINK_DEFAULT_VALUE.get();
        return base * switch (stack.getRarity()) {
            case COMMON -> Config.RARITY_COMMON.get();
            case UNCOMMON -> Config.RARITY_UNCOMMON.get();
            case RARE -> Config.RARITY_RARE.get();
            case EPIC -> Config.RARITY_EPIC.get();
        };
    }
}
