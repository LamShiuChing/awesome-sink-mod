# AWESOME Sink — NeoForge mod

Minecraft mod recreating Satisfactory's AWESOME Sink + Shop: dump items into the Sink for points, auto-print FICSIT Coupons, spend them in the Shop.

## Tech stack
- Minecraft 1.21.1, NeoForge 21.1.234, ModDevGradle 2.0.141, Java 21
- Mod id: `awesomesink`, base package `com.awesomesink`

## Layout
- `registry/` — DeferredRegister holders (blocks, items, menus, creative tab)
- `block/` + `block/entity/` — Sink & Shop blocks, block entities, shared `MachineInventory` (input/output-restricted handler) + `AbstractMachineBlockEntity`
- `menu/` — server/client `AbstractContainerMenu`s + shared `MachineMenu`
- `client/` — screens (use a vanilla container texture as placeholder) + `ClientSetup`
- `data/` — `AwesomePointsData` (global SavedData: points + couponsPrinted), `SinkValues` + `ShopCatalog` (datapack JSON reload listeners)
- resources: `data/awesomesink/sink_values/*.json` (item→points), `data/awesomesink/shop_catalog/*.json` (reward/price/count)

## Mechanics
- Points are **global/world-shared** (mirrors the game). Coupon cost escalates: first 3 = 1000, then `500·(⌈n/3⌉−1)²+1000` (`AwesomePointsData`, constants `BASE_COST`/`FACTOR`).
- Sink ticks: drains input (≤8/tick), adds `value·count` points, prints coupons into output while affordable.
- Shop ticks (vending): consumes `price` coupons from input, dispenses `count`×item to output. Shift-right-click block cycles the selected catalog entry.

## Build / run
- `./gradlew compileJava` — compile
- `./gradlew runClient` — launch dev client
- `./gradlew runServer` — dev server
- Game/run dir: `run/`

## Conventions
- Restricted I/O lives in `MachineInventory` (slots `[0,inputs)` = inputs); block entities move items via `setStackInSlot`.
- New sink values / shop items are data-driven — add JSON, no code.

## Known placeholders / next steps
- GUI uses vanilla `dispenser.png` + vanilla block/item textures; no custom art yet.
- Shop has no in-GUI catalog browser (selection via shift-right-click + chat); catalog isn't synced to client.
- Points displayed in GUI are clamped to `int`.

## Recent Changes
- 2026-06-24 — Fixed: couldn't take items out of input slots via GUI. MachineInventory.extractItem blocked all input extraction (meant for automation), but that also blocked the player. Removed it — per-face automation rules already live in DirectionalItemHandler. Pushed e9a6250; session end.
- 2026-06-24 — Coupon is now a value token: DataComponentType<Long> coupon_amount; sink prints into one accumulating token (no 99 cap), CouponDecorator (RegisterItemDecorationsEvent) renders amount as K/M/B; shop/buy sum & spend amounts across tokens. Tokens don't vanilla-stack (each is a wallet); shop totals them. Pushed 40a533b.
- 2026-06-24 — Fixed items/tick rate readout: was reset in tick() then re-accumulated, but consume-on-insert could add before the BE tick → wiped to 0. Now snapshots prev-tick accumulator (rate=consumed; consumed=0). Confirmed K/M/B display needs a stored amount + IItemDecorator (AE2/SophBackpacks pattern), not >99 stacks.
- 2026-06-24 — Unbounded sink intake via consume-on-insert: MachineInventory.InputConsumer awards points the instant items are inserted (stores nothing) → no throughput cap. Reverted oversized input buffer that crashed (MC hard-caps item stacks at [1;99]; >99 fails to save). GUI-placed items still drained per tick. Removed sinkConsumePerTick config.
- 2026-06-24 — Fixed I/O overlay overlapping JEI (moved inside GUI panel, dark backdrop). Added neighbor update on cycleSide so pipes (Pipez) re-scan when a face is enabled (cap invalidation alone didn't trigger their reconnect). Testing with JEI/Pipez/Cobble Gen in run/mods.
- 2026-06-24 — Committed + pushed valuation + economy batch (656c764): recipe-cost SinkValuation, intake-all, tooltips, high anchors, linear coupon curve.
- 2026-06-24 — Rebalanced coupon cost: quadratic escalation outran point income, so switched to linear `base + step·couponsPrinted` (default +250) with a config cap (1M). Coupons stay attainable late-game. Config: couponCostStep, couponMaxCost (replaced couponCostFactor).
- 2026-06-24 — Curated sink_values anchors for hard leaves + iconic items, scaled high (nether star 120k, beacon 180k, elytra 300k, netherite block 600k, dragon egg 1M). Formula propagates upward; boss/treasure leaves can't be auto-detected so they're anchored in JSON.
- 2026-06-23 — Valuation perf + tuning: built result→recipes index (O(1) lookups, generic over recipe types incl smelting/stonecutting via getIngredients) instead of scanning all recipes; config-tunable rarity weights (1/8/64/512), non-stackable ×4 multiplier, leaf base. Memoized + cycle-guarded.
- 2026-06-23 — Sink now accepts ANY item; value derived by formula (SinkValuation): raw=rarity-scaled base (sinkDefaultValue×1/4/16/64), crafted=ceil(Σ cheapest ingredient values/output count), recursive+memoized+cycle-guarded. JSON sink_values = optional override, synced to client. Item tooltip shows sink value (ItemTooltipEvent).
