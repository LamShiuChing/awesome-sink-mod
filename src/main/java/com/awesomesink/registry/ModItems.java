package com.awesomesink.registry;

import com.awesomesink.AwesomeSink;
import com.awesomesink.item.CouponItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AwesomeSink.MODID);

    public static final DeferredItem<CouponItem> COUPON = ITEMS.register("coupon",
            () -> new CouponItem(new Item.Properties()));

    public static final DeferredItem<BlockItem> AWESOME_SINK = ITEMS.registerSimpleBlockItem(ModBlocks.AWESOME_SINK);
    public static final DeferredItem<BlockItem> AWESOME_SHOP = ITEMS.registerSimpleBlockItem(ModBlocks.AWESOME_SHOP);

    private ModItems() {}
}
