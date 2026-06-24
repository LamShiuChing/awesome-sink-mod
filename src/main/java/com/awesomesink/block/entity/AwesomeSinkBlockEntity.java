package com.awesomesink.block.entity;

import com.awesomesink.Config;
import com.awesomesink.data.AwesomePointsData;
import com.awesomesink.data.SinkValues;
import com.awesomesink.menu.AwesomeSinkMenu;
import com.awesomesink.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AwesomeSinkBlockEntity extends AbstractMachineBlockEntity {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int DATA_POINTS = 0;
    public static final int DATA_NEXT_COST = 1;
    public static final int DATA_RATE = 2;

    private int lastConsumed;
    private final int[] clientData = new int[3];
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (level instanceof ServerLevel server) {
                if (index == DATA_RATE) {
                    return lastConsumed;
                }
                long value = index == DATA_POINTS
                        ? AwesomePointsData.get(server).points()
                        : AwesomePointsData.get(server).nextCouponCost();
                return (int) Math.min(value, Integer.MAX_VALUE);
            }
            return clientData[index];
        }

        @Override
        public void set(int index, int value) {
            clientData[index] = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public AwesomeSinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AWESOME_SINK.get(), pos, state, 1, 1,
                (slot, stack) -> SinkValues.INSTANCE.get(stack.getItem()) > 0);
    }

    public ContainerData data() {
        return data;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AwesomeSinkBlockEntity be) {
        be.tick((ServerLevel) level);
    }

    private void tick(ServerLevel level) {
        AwesomePointsData points = AwesomePointsData.get(level);
        lastConsumed = sinkInput(level, points);
        printCoupons(points);
    }

    private int sinkInput(ServerLevel level, AwesomePointsData points) {
        ItemStack input = inventory.getStackInSlot(SLOT_INPUT);
        int value = SinkValues.INSTANCE.get(input.getItem());
        if (input.isEmpty() || value <= 0) {
            return 0;
        }
        int taken = Math.min(input.getCount(), Config.SINK_CONSUME_PER_TICK.get());
        points.addPoints((long) value * taken);
        input.shrink(taken);
        inventory.setStackInSlot(SLOT_INPUT, input);
        level.sendParticles(ParticleTypes.PORTAL,
                worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5,
                4, 0.25, 0.1, 0.25, 0.02);
        return taken;
    }

    private void printCoupons(AwesomePointsData points) {
        ItemStack output = inventory.getStackInSlot(SLOT_OUTPUT);
        if (!output.isEmpty() && !output.is(ModItems.COUPON.get())) {
            return;
        }
        int space = (output.isEmpty() ? new ItemStack(ModItems.COUPON.get()).getMaxStackSize() : output.getMaxStackSize())
                - output.getCount();
        int printed = 0;
        while (printed < space && points.tryPrintCoupon()) {
            printed++;
        }
        if (printed > 0) {
            if (output.isEmpty()) {
                inventory.setStackInSlot(SLOT_OUTPUT, new ItemStack(ModItems.COUPON.get(), printed));
            } else {
                output.grow(printed);
                inventory.setStackInSlot(SLOT_OUTPUT, output);
            }
            level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.6F, 1.4F);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.awesomesink.awesome_sink");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AwesomeSinkMenu(id, playerInventory, this, data);
    }
}
