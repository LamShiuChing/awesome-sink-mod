package com.awesomesink.client;

import com.awesomesink.AwesomeSink;
import com.awesomesink.item.CouponItem;
import com.awesomesink.registry.ModItems;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;

/** Renders a coupon token's stored amount as a compact K/M/B figure in place of the stack count. */
@EventBusSubscriber(modid = AwesomeSink.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CouponDecorator {
    private CouponDecorator() {}

    @SubscribeEvent
    static void register(RegisterItemDecorationsEvent event) {
        event.register(ModItems.COUPON.get(), CouponDecorator::render);
    }

    static boolean render(GuiGraphics graphics, Font font, ItemStack stack, int x, int y) {
        String text = CouponItem.format(CouponItem.amount(stack));
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 200);
        graphics.drawString(font, text, x + 19 - font.width(text), y + 9, 0xFFFFFFFF, true);
        graphics.pose().popPose();
        return true;
    }
}
