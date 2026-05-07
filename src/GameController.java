import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private final GameModel model;
    private final GameView view;
    private final Timer gameTimer;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.gameTimer = new Timer(16, new GameLoop());
        setupInput();
    }

    public void startGame() {
        view.setVisible(true);
        gameTimer.start();
    }

    private void setupInput() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        handleRotateLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        handleRotateRight();
                        break;
                    case KeyEvent.VK_SPACE:
                        handleFire();
                        break;
                    case KeyEvent.VK_E:
                        handleUpgrade();
                        break;
                    case KeyEvent.VK_F:
                        handleUpgradeDamage();
                        break;
                    case KeyEvent.VK_C:
                        handleUpgradeCooldown();
                        break;
                    case KeyEvent.VK_R:
                        handleRestart();
                        break;
                    default:
                        break;
                }
            }
        });
        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    private void handleRotateLeft() {
        model.rotateTower(-5.0);
    }

    private void handleRotateRight() {
        model.rotateTower(5.0);
    }

    private void handleFire() {
        model.fireTower();
    }

    private void handleUpgrade() {
        model.upgradeTower();
    }

    private void handleUpgradeDamage() {
        model.upgradeDamage();
    }

    private void handleUpgradeCooldown() {
        model.upgradeCooldown();
    }

    private void handleRestart() {
        if (model.isGameOver()) {
            model.initializeGame();
            view.setTitle("Tower Defense Game");
            view.render();
            gameTimer.start();
        }
    }

    private class GameLoop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!model.isGameOver()) {
                model.updateGameState();
                view.render();
            } else {
                gameTimer.stop();
                view.showGameOver();
            }
        }
    }

    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView(model);
        GameController controller = new GameController(model, view);
        controller.startGame();
    }
}
