import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int dotSize = 10;
    private static final int allDots = 900;
    private static final int RAND_POS = 29;
    private static final int DELAY = 140;
    private final int[] x = new int[allDots];
    private final int[] y = new int[allDots];
    private int dots;
    private int apple_x;
    private int apple_y;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private int score = 0;
    private Timer timer;

    public SnakeGame() {
        addKeyListener(this);
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initGame();
    }

    public void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * dotSize;
            y[z] = 50;
        }
        locateApple();
        timer = new Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inGame) {
                    checkApple();
                    checkCollision();
                    move();
                }
                repaint();
            }
        });
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.setColor(Color.red);
            g.fillOval(apple_x, apple_y, dotSize, dotSize);
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[z], y[z], dotSize, dotSize);
                } else {
                    g.setColor(Color.yellow);
                    g.fillRect(x[z], y[z], dotSize, dotSize);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 20, 30);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game Over", 150, 200);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 220, 250);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Press 'R' to Restart", 170, 300);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (leftDirection) {
            x[0] -= dotSize;
        }
        if (rightDirection) {
            x[0] += dotSize;
        }
        if (upDirection) {
            y[0] -= dotSize;
        }
        if (downDirection) {
            y[0] += dotSize;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        if (y[0] >= HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] >= WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        Random rand = new Random();
        int r = rand.nextInt(RAND_POS);
        apple_x = ((r * dotSize));
        r = rand.nextInt(RAND_POS);
        apple_y = ((r * dotSize));
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if ((key == KeyEvent.VK_UP) && (!downDirection)) {
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
        if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_R && !inGame) {
            restartGame();
        }
    }

    public void keyTyped(KeyEvent e) {
    }
    
    private void restartGame() {
        inGame = true;
        score = 0;
        dots = 3;
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
        initGame(); // Initialize the game again
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SnakeGame snakeGame = new SnakeGame();
        frame.getContentPane().add(snakeGame);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add a window listener to handle focus issues when restarting
        frame.addWindowListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                // Request focus when the window gains focus (important for key events)
                snakeGame.requestFocusInWindow();
            }
        });
    }
}
