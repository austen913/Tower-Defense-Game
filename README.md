# Tower Defense Game - Design Specification

## Model (Game Logic & State)

### Game State
- Current wave number (starts at 1)
- Player coins (starts at 0)
- Game over flag
- Tower state: rotation angle, upgrade level (0 = 1 shot, 1 = 2 shots, 2 = 3 shots, etc.)

### Enemy System
- Wave structure: Wave N contains (4 + N) enemies
- Enemies spawn from start of circular track and move to the end
- Enemy speed increases each wave: base_speed × (1 + wave × speed_multiplier)
- Enemies are destroyed if hit by tower shot
- Game over if any enemy reaches the end of track

### Tower System
- Single tower positioned at center of circular track
- Can rotate 360 degrees (controlled by player)
- Can fire projectiles that travel outward
- Upgrades add additional shots per fire event

### Currency & Progression
- Defeating an enemy: +5 coins
- Defeating a wave (all enemies eliminated): +100 coins bonus
- Coins used to purchase tower upgrades

### Win/Loss Conditions
- **Loss**: Immediate game over if any enemy reaches end of track
- **Win**: Endless mode; score determined by highest wave reached

## View (Display & Rendering)

### Main Game Canvas
- Circular track positioned around the screen's center
- Tower sprite at center of screen
- Enemies rendered on track
- Projectiles rendered traveling from tower outward

### HUD (Top Left Corner)
- Wave number display
- Current coins display
- Format example: "Wave: 5  Coins: 250"

### Minimal Design
- No pause menu
- No difficulty settings
- Clean, simple graphics

## Controller (Input & Game Loop)

### Input Handling
- **Arrow Keys (Left/Right)**: Rotate tower left/right
- **Space Bar**: Fire tower (all current shots simultaneously)

### Game Loop
- Update enemy positions
- Check for collisions (projectiles vs enemies)
- Remove destroyed enemies; check if wave complete
- Remove projectiles that leave screen bounds
- Render all entities
- Process input

### Coordination
- Translate keyboard input to tower rotation and firing
- Trigger enemy wave spawning
- Calculate score updates
- Detect and handle game over condition

## "Done" for this week
Enemies move on the track towards the end. Tower is able to fire bullets. E upgrades the tower and adds and extra shot if the player has enough money. The enemies die if they are shot giving the player money. There are multiple waves that get harder.