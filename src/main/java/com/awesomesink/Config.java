package com.awesomesink;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Common (server-authoritative) config: coupon economy and sink throughput. */
public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue COUPON_BASE_COST = BUILDER
            .comment("Points cost of each of the first three coupons, and the base term of later coupons.")
            .defineInRange("couponBaseCost", 1000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue COUPON_COST_STEP = BUILDER
            .comment("Linear escalation: each printed coupon raises the next coupon's cost by this many points.")
            .defineInRange("couponCostStep", 250, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.LongValue COUPON_MAX_COST = BUILDER
            .comment("Cap on a coupon's cost (keeps late coupons attainable). 0 = uncapped.")
            .defineInRange("couponMaxCost", 1_000_000L, 0L, Long.MAX_VALUE);

    public static final ModConfigSpec.IntValue SINK_CONSUME_PER_TICK = BUILDER
            .comment("Items the sink consumes from its input each tick.")
            .defineInRange("sinkConsumePerTick", 8, 1, 1024);

    public static final ModConfigSpec.IntValue SINK_DEFAULT_VALUE = BUILDER
            .comment("Base value for leaf items (no recipe), multiplied by the item's rarity weight. 0 = reject leaf items.")
            .defineInRange("sinkDefaultValue", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue RARITY_COMMON = BUILDER
            .defineInRange("rarityWeightCommon", 1, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue RARITY_UNCOMMON = BUILDER
            .defineInRange("rarityWeightUncommon", 8, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue RARITY_RARE = BUILDER
            .defineInRange("rarityWeightRare", 64, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue RARITY_EPIC = BUILDER
            .defineInRange("rarityWeightEpic", 512, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue NON_STACKABLE_MULT = BUILDER
            .comment("Value multiplier for items that don't stack (tools, totems, etc.).")
            .defineInRange("nonStackableMultiplier", 4, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {}
}
