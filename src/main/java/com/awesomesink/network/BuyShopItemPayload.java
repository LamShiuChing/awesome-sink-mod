package com.awesomesink.network;

import com.awesomesink.AwesomeSink;
import com.awesomesink.block.entity.AwesomeShopBlockEntity;
import com.awesomesink.data.ShopCatalog;
import com.awesomesink.registry.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Client → server: buy catalog entry {@code index} from the shop at {@code pos}, paying coupons from the player's inventory. */
public record BuyShopItemPayload(BlockPos pos, int index) implements CustomPacketPayload {
    public static final Type<BuyShopItemPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AwesomeSink.MODID, "buy_shop_item"));

    public static final StreamCodec<ByteBuf, BuyShopItemPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BuyShopItemPayload::pos,
            ByteBufCodecs.VAR_INT, BuyShopItemPayload::index,
            BuyShopItemPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BuyShopItemPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)
                || !(player.level().getBlockEntity(payload.pos()) instanceof AwesomeShopBlockEntity)
                || player.distanceToSqr(Vec3.atCenterOf(payload.pos())) > 64) {
            return;
        }
        if (payload.index() < 0 || payload.index() >= ShopCatalog.INSTANCE.size()) {
            return;
        }
        ShopCatalog.Entry entry = ShopCatalog.INSTANCE.get(payload.index());
        if (countCoupons(player) < entry.price()) {
            return;
        }
        removeCoupons(player, entry.price());
        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(entry.item(), entry.count()));
    }

    private static int countCoupons(ServerPlayer player) {
        return player.getInventory().items.stream()
                .filter(s -> s.is(ModItems.COUPON.get())).mapToInt(ItemStack::getCount).sum();
    }

    private static void removeCoupons(ServerPlayer player, int amount) {
        Inventory inv = player.getInventory();
        for (int slot = 0; slot < inv.items.size() && amount > 0; slot++) {
            ItemStack stack = inv.items.get(slot);
            if (stack.is(ModItems.COUPON.get())) {
                amount -= stack.split(amount).getCount();
            }
        }
        inv.setChanged();
    }
}
