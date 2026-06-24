package com.awesomesink.registry;

import java.util.function.Supplier;

import com.awesomesink.AwesomeSink;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AwesomeSink.MODID);

    public static final Supplier<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.awesomesink"))
            .icon(() -> new ItemStack(ModItems.AWESOME_SINK.get()))
            .displayItems((params, output) -> {
                output.accept(ModItems.AWESOME_SINK.get());
                output.accept(ModItems.AWESOME_SHOP.get());
                output.accept(ModItems.COUPON.get());
            })
            .build());

    private ModCreativeTab() {}
}
