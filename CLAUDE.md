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
- 2026-06-23 — UI polish v2: real bold-border fix was the slot sprite (heavy 18x18 dark frame → vanilla 1px dark/white bevel in TexGen.slot); white 32px furnace arrow fill; moved machine slots to y40; moved sink "Next" label under the arrow (was overlapping the I/O button).
- 2026-06-23 — UI polish: removed duplicate renderTooltip in MachineScreen, tightened label spacing, furnace arrow progress sprite, Rate N/t readout (DATA_RATE ContainerData slot).
- 2026-06-23 — Added sink portal particles (while consuming) + advancements (root 'AWESOME'→'FICSIT Coupon') via datagen; pushed c4d4c72.
- 2026-06-23 — Added feedback sounds (vanilla events, no assets): amethyst chime on coupon print, xp-pickup on purchase; pushed 74e4677.
- 2026-06-23 — Added common config (ModConfigSpec: coupon base/factor, sink rate) + shop catalog scrolling; pushed b2f8dc9. Coupon cost + sink rate now tunable.
- 2026-06-23 — Added coupon progress bar to sink GUI (unspent points → next coupon cost); pushed db9f50c.
- 2026-06-23 — Added crafting recipes via datagen RecipeProvider (sink=iron+redstone block, shop=gold+emerald); pushed 6f90182. Machines now survival-obtainable.
- 2026-06-23 — Committed + pushed textures/datagen/shop-browser (5cffa90). Generated resources tracked; datagen .cache gitignored.
- 2026-06-23 — Added procedural textures (TexGen.java), datagen (blockstates/models/lang/loot — loot fixes no-drop bug; switched build to src/generated/resources, deleted hand JSON), and shop catalog browser (S2C catalog sync, C2S buy packet, in-GUI item grid). Compiles; testing in client.
- 2026-06-23 — Untracked local dev notes (.devstate, DEVLOG.md) via .gitignore; pushed. Repo clean of dev scaffolding.
- 2026-06-23 — Fixed LEFT/RIGHT side mapping (swapped clockwise/counter-clockwise so labels match player view). Pushed initial commit to GitHub (origin/main, LamShiuChing/awesome-sink-mod).
