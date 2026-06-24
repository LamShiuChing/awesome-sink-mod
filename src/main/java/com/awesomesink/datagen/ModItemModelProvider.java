package com.awesomesink.datagen;

import com.awesomesink.AwesomeSink;
import com.awesomesink.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper efh) {
        super(output, AwesomeSink.MODID, efh);
    }

    @Override
    protected void registerModels() {
        withExistingParent("awesome_sink", modLoc("block/awesome_sink"));
        withExistingParent("awesome_shop", modLoc("block/awesome_shop"));
        basicItem(ModItems.COUPON.get());
    }
}
