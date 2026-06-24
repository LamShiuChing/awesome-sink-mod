package com.awesomesink.datagen;

import java.util.concurrent.CompletableFuture;

import com.awesomesink.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AWESOME_SINK.get())
                .pattern("III").pattern("IRI").pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('R', Blocks.REDSTONE_BLOCK)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AWESOME_SHOP.get())
                .pattern("GGG").pattern("GEG").pattern("GGG")
                .define('G', Items.GOLD_INGOT)
                .define('E', Items.EMERALD)
                .unlockedBy("has_emerald", has(Items.EMERALD))
                .save(output);
    }
}
