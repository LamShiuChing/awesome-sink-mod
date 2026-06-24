package com.awesomesink.datagen;

import java.util.function.Consumer;

import com.awesomesink.registry.ModBlocks;
import com.awesomesink.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModAdvancements implements AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper efh) {
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(ModBlocks.AWESOME_SINK.get(),
                        Component.translatable("advancement.awesomesink.root.title"),
                        Component.translatable("advancement.awesomesink.root.desc"),
                        ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"),
                        AdvancementType.TASK, true, true, false)
                .addCriterion("has_sink", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.AWESOME_SINK.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath("awesomesink", "root"), efh);

        Advancement.Builder.advancement()
                .parent(root)
                .display(ModItems.COUPON.get(),
                        Component.translatable("advancement.awesomesink.coupon.title"),
                        Component.translatable("advancement.awesomesink.coupon.desc"),
                        null, AdvancementType.TASK, true, true, false)
                .addCriterion("has_coupon", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COUPON.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath("awesomesink", "coupon"), efh);
    }
}
