package com.awesomesink.block.entity;

import java.util.EnumMap;
import java.util.function.BiPredicate;

import com.awesomesink.block.AbstractMachineBlock;
import com.awesomesink.block.RelativeSide;
import com.awesomesink.block.SideMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/** Shared inventory, drop logic, and per-face side configuration for the machines. */
public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider {
    protected final MachineInventory inventory;
    private final EnumMap<RelativeSide, SideMode> sideConfig = defaultSideConfig();

    protected AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                         int inputs, int outputs, BiPredicate<Integer, ItemStack> validInput) {
        super(type, pos, state);
        this.inventory = new MachineInventory(inputs, outputs, validInput, this::setChanged);
    }

    private static EnumMap<RelativeSide, SideMode> defaultSideConfig() {
        EnumMap<RelativeSide, SideMode> map = new EnumMap<>(RelativeSide.class);
        for (RelativeSide side : RelativeSide.values()) {
            map.put(side, SideMode.DISABLED);
        }
        map.put(RelativeSide.FRONT, SideMode.INPUT);
        map.put(RelativeSide.BOTTOM, SideMode.OUTPUT);
        return map;
    }

    public MachineInventory inventory() {
        return inventory;
    }

    public SideMode sideMode(RelativeSide side) {
        return sideConfig.get(side);
    }

    /** Capability handler for {@code side}: full handler for a null side, a one-way view per face mode, or null if disabled. */
    @Nullable
    public IItemHandler handlerFor(@Nullable Direction side) {
        if (side == null) {
            return inventory;
        }
        SideMode mode = sideConfig.get(RelativeSide.fromDirection(facing(), side));
        return mode == SideMode.DISABLED ? null
                : new DirectionalItemHandler(inventory, mode.allowsInsert(), mode.allowsExtract());
    }

    public void cycleSide(RelativeSide side) {
        sideConfig.put(side, sideConfig.get(side).next());
        setChanged();
        if (level != null) {
            level.invalidateCapabilities(worldPosition);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private Direction facing() {
        return getBlockState().getValue(AbstractMachineBlock.FACING);
    }

    public void dropContents() {
        if (level == null) {
            return;
        }
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                    inventory.getStackInSlot(slot));
        }
    }

    private void saveSideConfig(CompoundTag tag) {
        byte[] modes = new byte[RelativeSide.values().length];
        sideConfig.forEach((side, mode) -> modes[side.ordinal()] = (byte) mode.ordinal());
        tag.putByteArray("sides", modes);
    }

    private void loadSideConfig(CompoundTag tag) {
        if (!tag.contains("sides")) {
            return;
        }
        byte[] modes = tag.getByteArray("sides");
        for (RelativeSide side : RelativeSide.values()) {
            sideConfig.put(side, SideMode.values()[modes[side.ordinal()]]);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        saveSideConfig(tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        loadSideConfig(tag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveSideConfig(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadSideConfig(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
