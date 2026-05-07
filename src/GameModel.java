import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private int waveNumber;
    private int coins;
    private boolean gameOver;
    private double towerRotation;
    private int towerUpgradeLevel;
    private long lastShotTime;
    private static final long SHOT_COOLDOWN = 1000;

    private final List<Enemy> enemies;
    private final List<Projectile> projectiles;

    public GameModel() {
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
        initializeGame();
    }

    public void initializeGame() {
        waveNumber = 1;
        coins = 0;
        gameOver = false;
        towerRotation = 0.0;
        towerUpgradeLevel = 0;
        lastShotTime = 0;
        enemies.clear();
        projectiles.clear();
        spawnWave();
    }

    public void updateGameState() {
        updateEnemies();
        updateProjectiles();
        checkCollisions();
        checkWaveCompletion();
    }

    public void spawnWave() {
        enemies.clear();
        int count = 4 + waveNumber;
        double spacing = 1000.0 / (count + 1);
        for (int i = 0; i < count; i++) {
            double initialProgress = -spacing * (count - 1 - i);
            enemies.add(new Enemy(initialProgress));
        }
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public boolean upgradeTower() {
        int cost = getUpgradeCost();
        if (coins >= cost) {
            coins -= cost;
            towerUpgradeLevel++;
            return true;
        }
        return false;
    }

    public int getUpgradeCost() {
        return 50 + (towerUpgradeLevel * 50);
    }

    public void fireTower() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= SHOT_COOLDOWN) {
            int shots = 1 + towerUpgradeLevel;
            double baseAngle = towerRotation - 90;
            double spreadStep = 12.0;
            double startAngle = baseAngle - spreadStep * (shots - 1) / 2.0;
            for (int i = 0; i < shots; i++) {
                double angle = startAngle + spreadStep * i;
                projectiles.add(new Projectile(angle));
            }
            lastShotTime = currentTime;
        }
    }

    public void rotateTower(double deltaAngle) {
        towerRotation += deltaAngle;
        if (towerRotation < 0) {
            towerRotation += 360;
        } else if (towerRotation >= 360) {
            towerRotation -= 360;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getCoins() {
        return coins;
    }

    public double getTowerRotation() {
        return towerRotation;
    }

    public int getTowerUpgradeLevel() {
        return towerUpgradeLevel;
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.updatePosition(waveNumber);
            if (enemy.hasReachedEnd()) {
                gameOver = true;
            }
        }
    }

    private void updateProjectiles() {
        for (Projectile projectile : projectiles) {
            projectile.updatePosition();
        }
        projectiles.removeIf(Projectile::isOffScreen);
    }

    private void checkCollisions() {
        for (Projectile projectile : new ArrayList<>(projectiles)) {
            for (Enemy enemy : new ArrayList<>(enemies)) {
                if (projectile.collidesWith(enemy)) {
                    enemy.setAlive(false);
                    projectile.setActive(false);
                    addCoins(5);
                }
            }
        }
        enemies.removeIf(enemy -> !enemy.isAlive());
        projectiles.removeIf(projectile -> !projectile.isActive());
    }

    private void checkWaveCompletion() {
        if (enemies.isEmpty() && !gameOver) {
            addCoins(100);
            waveNumber++;
            spawnWave();
        }
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public List<Projectile> getProjectiles() {
        return new ArrayList<>(projectiles);
    }

    public static class Enemy {
        private boolean alive;
        private double progress;

        public Enemy(double progress) {
            this.alive = true;
            this.progress = progress;
        }

        public void updatePosition(int waveNumber) {
            double speed = 1.0 + (waveNumber * 0.05);
            progress += speed;
        }

        public boolean hasReachedEnd() {
            return progress >= 1000.0;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        public double getProgress() {
            return progress;
        }
    }

    public static class Projectile {
        private final double angle;
        private double distance;
        private boolean active;

        public Projectile(double angle) {
            this.angle = angle;
            this.distance = 0.0;
            this.active = true;
        }

        public void updatePosition() {
            distance += 50.0;
            if (distance > 5000.0) {
                active = false;
            }
        }

        public boolean isOffScreen() {
            return !active;
        }

        public boolean collidesWith(Enemy enemy) {
            if (!enemy.isAlive() || enemy.getProgress() < 0) {
                return false;
            }
            double radians = Math.toRadians(angle);
            double projX = 400 + distance * Math.cos(radians);
            double projY = 400 + distance * Math.sin(radians);
            double enemyX = 50 + (enemy.progress / 1000.0) * 700;
            double enemyY = 165;
            double dx = projX - enemyX;
            double dy = projY - enemyY;
            return Math.sqrt(dx * dx + dy * dy) < 30;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public double getDistance() {
            return distance;
        }

        public double getAngle() {
            return angle;
        }
    }
}
