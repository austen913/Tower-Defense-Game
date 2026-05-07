import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameView extends JFrame {
    private final GameModel model;
    private final GamePanel gamePanel;

    public GameView(GameModel model) {
        this.model = model;
        this.gamePanel = new GamePanel();
        initializeWindow();
    }

    private void initializeWindow() {
        setTitle("Tower Defense Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setFocusable(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void render() {
        gamePanel.repaint();
    }

    public void showGameOver() {
        setTitle("Game Over - Tower Defense Game");
    }

    private class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBackground(g);
            drawTower(g);
            drawEnemies(g);
            drawProjectiles(g);
            drawHUD(g);
        }

        private void drawBackground(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(50, 150, 700, 30);

            g.setColor(Color.GREEN);
            g.fillOval(45, 145, 10, 10);
            g.fillOval(750, 145, 10, 10);
        }

        private void drawTower(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GRAY);
            
            int centerX = 400;
            int centerY = 400;
            
            AffineTransform old = g2d.getTransform();
            g2d.translate(centerX, centerY);
            g2d.rotate(Math.toRadians(model.getTowerRotation() + 90));
            g2d.fillRect(-50, -50, 100, 100);
            g2d.setTransform(old);
        }

        private void drawEnemies(Graphics g) {
            g.setColor(Color.RED);
            for (GameModel.Enemy enemy : model.getEnemies()) {
                double progress = enemy.getProgress();
                if (progress < 0) {
                    continue;
                }
                double x = 50 + (progress / 1000.0) * 700;
                g.fillOval((int) x - 20, 145, 40, 40);
            }
        }

        private void drawProjectiles(Graphics g) {
            g.setColor(Color.YELLOW);
            for (GameModel.Projectile projectile : model.getProjectiles()) {
                double angle = projectile.getAngle();
                double distance = projectile.getDistance();
                double radians = Math.toRadians(angle);
                double x = 400 + distance * Math.cos(radians);
                double y = 400 + distance * Math.sin(radians);
                g.fillOval((int) x - 6, (int) y - 6, 12, 12);
            }
        }

        private void drawHUD(Graphics g) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            g.drawString("Wave: " + model.getWaveNumber() + "  Coins: " + model.getCoins(), 20, 30);
            if (model.isGameOver()) {
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
                g.drawString("GAME OVER", 300, 320);
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                g.drawString("Press R to restart", 280, 360);
            }
        }
    }
}
