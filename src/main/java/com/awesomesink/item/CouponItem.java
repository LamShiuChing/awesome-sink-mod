package com.awesomesink.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;

/** FICSIT Coupon — currency printed by the AWESOME Sink, spent in the AWESOME Shop. */
public class CouponItem extends Item {
    public CouponItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(net.minecraft.world.item.ItemStack stack, TooltipContext context,
                               List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.awesomesink.coupon.desc").withStyle(ChatFormatting.GRAY));
    }
}
