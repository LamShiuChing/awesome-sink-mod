package com.awesomesink;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Common (server-authoritative) config: coupon economy and sink throughput. */
public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue COUPON_BASE_COST = BUILDER
            .comment("Points cost of each of the first three coupons, and the base term of later coupons.")
            .defineInRange("couponBaseCost", 1000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue COUPON_COST_FACTOR = BUILDER
            .comment("Escalation factor: coupon n costs factor*(ceil(n/3)-1)^2 + base.")
            .defineInRange("couponCostFactor", 500, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SINK_CONSUME_PER_TICK = BUILDER
            .comment("Items the sink consumes from its input each tick.")
            .defineInRange("sinkConsumePerTick", 8, 1, 1024);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {}
}
