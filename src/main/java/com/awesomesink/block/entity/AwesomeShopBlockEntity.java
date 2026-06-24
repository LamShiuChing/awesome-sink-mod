package com.awesomesink.block.entity;

import com.awesomesink.data.ShopCatalog;
import com.awesomesink.item.CouponItem;
import com.awesomesink.menu.AwesomeShopMenu;
import com.awesomesink.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AwesomeShopBlockEntity extends AbstractMachineBlockEntity {
    public static final int SLOT_COUPONS = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int DATA_PRICE = 0;
    public static final int DATA_SELECTION = 1;

    private int selection;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == DATA_SELECTION) {
                return selection;
            }
            ShopCatalog.Entry entry = ShopCatalog.INSTANCE.get(selection);
            return entry == null ? 0 : entry.price();
        }

        @Override
        public void set(int index, int value) {
            if (index == DATA_SELECTION) {
                selection = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public AwesomeShopBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AWESOME_SHOP.get(), pos, state, 1, 1,
                (slot, stack) -> stack.is(ModItems.COUPON.get()));
    }

    public ContainerData data() {
        return data;
    }

    public void cycleSelection(Player player) {
        int size = ShopCatalog.INSTANCE.size();
        if (size == 0) {
            player.displayClientMessage(Component.translatable("message.awesomesink.shop_empty"), true);
            return;
        }
        selection = Math.floorMod(selection + 1, size);
        setChanged();
        ShopCatalog.Entry entry = ShopCatalog.INSTANCE.get(selection);
        player.displayClientMessage(Component.translatable("message.awesomesink.shop_selected",
                entry.count(), Component.translatable(entry.item().getDescriptionId()), entry.price()), true);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AwesomeShopBlockEntity be) {
        be.vend();
    }

    private void vend() {
        ShopCatalog.Entry entry = ShopCatalog.INSTANCE.get(selection);
        if (entry == null) {
            return;
        }
        ItemStack coupons = inventory.getStackInSlot(SLOT_COUPONS);
        if (coupons.isEmpty() || CouponItem.amount(coupons) < entry.price()) {
            return;
        }
        ItemStack output = inventory.getStackInSlot(SLOT_OUTPUT);
        boolean fits = output.isEmpty()
                || (output.is(entry.item()) && output.getCount() + entry.count() <= output.getMaxStackSize());
        if (!fits) {
            return;
        }
        if (CouponItem.amount(coupons) <= entry.price()) {
            inventory.setStackInSlot(SLOT_COUPONS, ItemStack.EMPTY);
        } else {
            CouponItem.add(coupons, -entry.price());
            inventory.setStackInSlot(SLOT_COUPONS, coupons);
        }
        if (output.isEmpty()) {
            inventory.setStackInSlot(SLOT_OUTPUT, new ItemStack(entry.item(), entry.count()));
        } else {
            output.grow(entry.count());
            inventory.setStackInSlot(SLOT_OUTPUT, output);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("selection", selection);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        selection = tag.getInt("selection");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.awesomesink.awesome_shop");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AwesomeShopMenu(id, playerInventory, this, data);
    }
}
