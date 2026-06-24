package com.awesomesink.client;

import com.awesomesink.AwesomeSink;
import com.awesomesink.registry.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = AwesomeSink.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.AWESOME_SINK.get(), AwesomeSinkScreen::new);
        event.register(ModMenus.AWESOME_SHOP.get(), AwesomeShopScreen::new);
    }
}
