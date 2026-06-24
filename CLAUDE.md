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
- 2026-06-24 — Rebalanced coupon cost: quadratic escalation outran point income, so switched to linear `base + step·couponsPrinted` (default +250) with a config cap (1M). Coupons stay attainable late-game. Config: couponCostStep, couponMaxCost (replaced couponCostFactor).
- 2026-06-24 — Curated sink_values anchors for hard leaves + iconic items, scaled high (nether star 120k, beacon 180k, elytra 300k, netherite block 600k, dragon egg 1M). Formula propagates upward; boss/treasure leaves can't be auto-detected so they're anchored in JSON.
- 2026-06-23 — Valuation perf + tuning: built result→recipes index (O(1) lookups, generic over recipe types incl smelting/stonecutting via getIngredients) instead of scanning all recipes; config-tunable rarity weights (1/8/64/512), non-stackable ×4 multiplier, leaf base. Memoized + cycle-guarded.
- 2026-06-23 — Sink now accepts ANY item; value derived by formula (SinkValuation): raw=rarity-scaled base (sinkDefaultValue×1/4/16/64), crafted=ceil(Σ cheapest ingredient values/output count), recursive+memoized+cycle-guarded. JSON sink_values = optional override, synced to client. Item tooltip shows sink value (ItemTooltipEvent).
- 2026-06-23 — Committed + pushed GUI polish (44f2321): vanilla slot bevel, no bold tooltips, white 32px furnace arrow, items/tick, roomier layout.
- 2026-06-23 — UI polish v2: real bold-border fix was the slot sprite (heavy 18x18 dark frame → vanilla 1px dark/white bevel in TexGen.slot); white 32px furnace arrow fill; moved machine slots to y40; moved sink "Next" label under the arrow (was overlapping the I/O button).
- 2026-06-23 — Added sink portal particles (while consuming) + advancements (root 'AWESOME'→'FICSIT Coupon') via datagen; pushed c4d4c72.
- 2026-06-23 — Added feedback sounds (vanilla events, no assets): amethyst chime on coupon print, xp-pickup on purchase; pushed 74e4677.
- 2026-06-23 — Added common config (ModConfigSpec: coupon base/factor, sink rate) + shop catalog scrolling; pushed b2f8dc9. Coupon cost + sink rate now tunable.
- 2026-06-23 — Added coupon progress bar to sink GUI (unspent points → next coupon cost); pushed db9f50c.
