# Dev State

## Now
Compiling the new side-config feature (`compileJava`, bg ba0z2v2k2). Then runClient to test cube-net I/O.

## Works
- Sink + Shop vertical slice (verified in-game earlier).
- New (unverified): per-face side configuration — RelativeSide/SideMode, block FACING + blockstate variants, sided capability (DirectionalItemHandler), C2S SetSideModePayload, "I/O" cube-net overlay on MachineScreen, BE NBT+client sync via getUpdateTag/getUpdatePacket.

## Next
1. Confirm compile; fix 1.21.1 API gaps (suspect: GuiGraphics.renderOutline, getUpdateTag override sig).
2. runClient: place machine, open GUI, click I/O, cycle faces, verify hopper respects modes.
3. Custom textures + GUI art; in-GUI shop catalog browser.

## Notes
- Side modes: DISABLED(gray)/INPUT(blue)/OUTPUT(orange); default FRONT=INPUT, BOTTOM=OUTPUT.
- Clean-room from Mekanism concept (MIT) — no code copied.
- Facing relative sides via RelativeSide.toDirection/fromDirection.
