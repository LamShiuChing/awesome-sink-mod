package com.awesomesink.item;

import java.util.List;

import com.awesomesink.registry.ModDataComponents;
import com.awesomesink.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * FICSIT Coupon — a value token: a single item whose data component stores how many coupons it
 * represents (so amounts can far exceed the 99 stack cap). Rendered as a K/M/B figure.
 */
public class CouponItem extends Item {
    private static final String[] SUFFIXES = {"K", "M", "B", "T", "Q"};

    public CouponItem(Properties props) {
        super(props);
    }

    public static long amount(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.COUPON_AMOUNT.get(), 1L);
    }

    public static ItemStack of(long amount) {
        ItemStack stack = new ItemStack(ModItems.COUPON.get());
        stack.set(ModDataComponents.COUPON_AMOUNT.get(), amount);
        return stack;
    }

    public static void add(ItemStack stack, long delta) {
        stack.set(ModDataComponents.COUPON_AMOUNT.get(), amount(stack) + delta);
    }

    /** Compact human figure: 950 -> "950", 1500 -> "1.5K", 2_300_000 -> "2.3M". */
    public static String format(long value) {
        if (value < 1000) {
            return Long.toString(value);
        }
        double scaled = value;
        int suffix = -1;
        while (scaled >= 1000 && suffix < SUFFIXES.length - 1) {
            scaled /= 1000;
            suffix++;
        }
        return (scaled >= 100 ? String.format("%.0f%s", scaled, SUFFIXES[suffix])
                : String.format("%.1f%s", scaled, SUFFIXES[suffix]));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.awesomesink.coupon_amount", format(amount(stack)))
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.awesomesink.coupon.desc").withStyle(ChatFormatting.GRAY));
    }
}
