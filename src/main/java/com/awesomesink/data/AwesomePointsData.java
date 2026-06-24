package com.awesomesink.data;

import com.awesomesink.Config;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Global, world-shared sink progress: accumulated points and number of coupons printed.
 * Coupon cost escalates like Satisfactory: first {@link #BASE_COST} per coupon for the
 * first three, then {@code FACTOR * (ceil(n/3) - 1)^2 + BASE_COST}.
 */
public class AwesomePointsData extends SavedData {
    private static final String NAME = "awesomesink_points";

    private long points;
    private int couponsPrinted;

    public static AwesomePointsData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage()
                .computeIfAbsent(new Factory<>(AwesomePointsData::new, AwesomePointsData::load), NAME);
    }

    public long points() {
        return points;
    }

    public int couponsPrinted() {
        return couponsPrinted;
    }

    public long nextCouponCost() {
        long base = Config.COUPON_BASE_COST.get();
        int n = couponsPrinted + 1;
        if (n <= 3) {
            return base;
        }
        long k = (long) Math.ceil(n / 3.0) - 1;
        return (long) Config.COUPON_COST_FACTOR.get() * k * k + base;
    }

    public void addPoints(long amount) {
        if (amount <= 0) {
            return;
        }
        points += amount;
        setDirty();
    }

    /** Deducts the next coupon's cost if affordable and records it. Returns true if a coupon was printed. */
    public boolean tryPrintCoupon() {
        long cost = nextCouponCost();
        if (points < cost) {
            return false;
        }
        points -= cost;
        couponsPrinted++;
        setDirty();
        return true;
    }

    public static AwesomePointsData load(CompoundTag tag, HolderLookup.Provider registries) {
        AwesomePointsData data = new AwesomePointsData();
        data.points = tag.getLong("points");
        data.couponsPrinted = tag.getInt("couponsPrinted");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putLong("points", points);
        tag.putInt("couponsPrinted", couponsPrinted);
        return tag;
    }
}
