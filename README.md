# Low Input Lag (Fabric, Minecraft 1.21.4, Yarn 1.21.4+build.8)

Client-only mod that applies camera look as late as possible in every frame.

## What it does
Vanilla already applies mouse deltas per rendered frame (not per tick), but at the
start of the frame. This mod adds a "late latch": right before the world is rendered
it (optionally) drains the input queue and calls `Mouse.updateMouse` again, so mouse
movement that arrived in the meantime is visible in THIS frame instead of the next.
It also optionally disables camera smoothing and scales sensitivity.

Classes:
- `GameRendererMixin` - injects at HEAD of `GameRenderer.renderWorld` (late latch).
- `MouseMixin` - injects at HEAD of `Mouse.updateMouse` (smoothing off, multiplier).
- `LowInputLagConfig` - JSON config. `LowInputLagMod` - entrypoint.

No double application: vanilla zeroes the cursor deltas after each `updateMouse`.

## Build
Needs JDK 21 and Gradle 8.11+ (Loom 1.9).
```
gradle wrapper --gradle-version 8.12   # once, or just open the folder in IntelliJ
./gradlew build
```
Jar: `build/libs/lowinputlag-1.0.0.jar`. Fabric API is NOT required.

## Install (Zalith Launcher)
Create a 1.21.4 Fabric instance (Java 21), put the jar into that instance's `mods` folder.

## Config (`config/lowinputlag.json`)
- `enabled` (true) - master switch
- `applyEveryFrame` (true) - late latch before world render
- `pollEventsBeforeLook` (true) - glfwPollEvents before the late latch; turn off if you see glitches
- `sensitivityMultiplier` (1.0) - extra multiplier on top of vanilla
- `disableSmoothing` (true) - forces cinematic camera off in-game
- `debugLog` (false)

## Testing checklist
- Menu / inventory: cursor moves normally
- In-game: camera follows finger immediately
- Sensitivity feels the same as without the mod (multiplier 1.0)
- No drift when standing still
- Works with VSync on and off

## Limitations
Android compositor, SurfaceView/VSync and the launcher's input bridge still add
latency that a Fabric mod cannot remove. Gain may be small (up to roughly one frame).
