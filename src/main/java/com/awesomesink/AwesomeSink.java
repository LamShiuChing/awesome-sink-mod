package com.awesomesink;

import com.awesomesink.block.entity.ModBlockEntities;
import com.awesomesink.data.ShopCatalog;
import com.awesomesink.data.SinkValues;
import com.awesomesink.network.ModNetwork;
import com.awesomesink.network.ShopSync;
import com.awesomesink.registry.ModBlocks;
import com.awesomesink.registry.ModCreativeTab;
import com.awesomesink.registry.ModItems;
import com.awesomesink.registry.ModMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Mod(AwesomeSink.MODID)
public final class AwesomeSink {
    public static final String MODID = "awesomesink";

    public AwesomeSink(IEventBus modBus) {
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModMenus.MENUS.register(modBus);
        ModCreativeTab.TABS.register(modBus);

        modBus.addListener(this::registerCapabilities);
        modBus.addListener(ModNetwork::register);
        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        NeoForge.EVENT_BUS.addListener(ShopSync::onDatapackSync);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.AWESOME_SINK.get(),
                (be, side) -> be.handlerFor(side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.AWESOME_SHOP.get(),
                (be, side) -> be.handlerFor(side));
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(SinkValues.INSTANCE);
        event.addListener(ShopCatalog.INSTANCE);
    }
}
