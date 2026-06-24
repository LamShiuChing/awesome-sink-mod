package com.awesomesink.network;

import com.awesomesink.AwesomeSink;
import com.awesomesink.block.RelativeSide;
import com.awesomesink.block.entity.AbstractMachineBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Client → server: cycle the {@link SideMode} of one face of the machine at {@code pos}. */
public record SetSideModePayload(BlockPos pos, RelativeSide side) implements CustomPacketPayload {
    public static final Type<SetSideModePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AwesomeSink.MODID, "set_side_mode"));

    private static final StreamCodec<ByteBuf, RelativeSide> SIDE_CODEC =
            ByteBufCodecs.VAR_INT.map(i -> RelativeSide.values()[i], RelativeSide::ordinal);

    public static final StreamCodec<ByteBuf, SetSideModePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SetSideModePayload::pos,
            SIDE_CODEC, SetSideModePayload::side,
            SetSideModePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetSideModePayload payload, IPayloadContext context) {
        Player player = context.player();
        if (player.level().getBlockEntity(payload.pos()) instanceof AbstractMachineBlockEntity be
                && player.distanceToSqr(Vec3.atCenterOf(payload.pos())) <= 64) {
            be.cycleSide(payload.side());
        }
    }
}
