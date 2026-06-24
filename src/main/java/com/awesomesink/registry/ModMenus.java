package com.awesomesink.registry;

import java.util.function.Supplier;

import com.awesomesink.AwesomeSink;
import com.awesomesink.menu.AwesomeShopMenu;
import com.awesomesink.menu.AwesomeSinkMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, AwesomeSink.MODID);

    public static final Supplier<MenuType<AwesomeSinkMenu>> AWESOME_SINK =
            MENUS.register("awesome_sink", () -> IMenuTypeExtension.create(AwesomeSinkMenu::new));

    public static final Supplier<MenuType<AwesomeShopMenu>> AWESOME_SHOP =
            MENUS.register("awesome_shop", () -> IMenuTypeExtension.create(AwesomeShopMenu::new));

    private ModMenus() {}
}
