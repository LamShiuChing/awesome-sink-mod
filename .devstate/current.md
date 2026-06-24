# Dev State

## Now
Idle after pushing initial commit to GitHub (origin/main, LamShiuChing/awesome-sink-mod). Last code change: LEFT/RIGHT side-mapping fix (compiles).

## Works
- Sink + Shop vertical slice (in-game verified).
- Per-face I/O side config: enums, FACING, sided capability, C2S packet, cube-net "I/O" overlay. LEFT/RIGHT now match player view.
- Git: initialized, committed (aefebe7), pushed to origin/main.

## Next
1. (optional) runClient to confirm LEFT/RIGHT visually.
2. gitignore `.remember/`, `.devstate/`, `DEVLOG.md`? (asked user)
3. Custom textures + GUI art; in-GUI shop catalog browser; datagen.

## Notes
- Side modes DISABLED/INPUT/OUTPUT; default FRONT=INPUT, BOTTOM=OUTPUT.
- RelativeSide.toDirection/fromDirection share the map → GUI + capability consistent.
- Clean-room from Mekanism concept (MIT); no code copied.
