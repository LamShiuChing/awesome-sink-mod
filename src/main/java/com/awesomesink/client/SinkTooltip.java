package com.awesomesink.client;

import com.awesomesink.AwesomeSink;
import com.awesomesink.data.SinkValuation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = AwesomeSink.MODID, value = Dist.CLIENT)
public final class SinkTooltip {
    private SinkTooltip() {}

    @SubscribeEvent
    static void onTooltip(ItemTooltipEvent event) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = event.getItemStack();
        if (mc.level == null || stack.isEmpty()) {
            return;
        }
        int value = SinkValuation.value(stack, mc.level.getRecipeManager(), mc.level.registryAccess());
        if (value > 0) {
            event.getToolTip().add(Component.translatable("tooltip.awesomesink.sink_value", value)
                    .withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    @SubscribeEvent
    static void onRecipesUpdated(RecipesUpdatedEvent event) {
        SinkValuation.invalidate();
    }
}
