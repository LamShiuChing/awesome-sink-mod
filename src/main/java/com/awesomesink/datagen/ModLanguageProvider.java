package com.awesomesink.datagen;

import com.awesomesink.AwesomeSink;
import com.awesomesink.registry.ModBlocks;
import com.awesomesink.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AwesomeSink.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.awesomesink", "AWESOME Sink");
        add(ModBlocks.AWESOME_SINK.get(), "AWESOME Sink");
        add(ModBlocks.AWESOME_SHOP.get(), "AWESOME Shop");
        add(ModItems.COUPON.get(), "FICSIT Coupon");
        add("item.awesomesink.coupon.desc", "Spend in the AWESOME Shop");

        add("screen.awesomesink.points", "Points: %s");
        add("screen.awesomesink.next_coupon", "Next coupon: %s");
        add("screen.awesomesink.price", "Price: %s coupons");
        add("screen.awesomesink.shop_hint", "Shift-right-click block to change item");
        add("screen.awesomesink.balance", "Coupons: %s");

        add("message.awesomesink.shop_empty", "Shop catalog is empty");
        add("message.awesomesink.shop_selected", "Selling %sx %s for %s coupons");

        add("advancement.awesomesink.root.title", "AWESOME");
        add("advancement.awesomesink.root.desc", "Craft an AWESOME Sink");
        add("advancement.awesomesink.coupon.title", "FICSIT Coupon");
        add("advancement.awesomesink.coupon.desc", "Print your first coupon");
    }
}
