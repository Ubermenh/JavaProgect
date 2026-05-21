import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends Frame implements KeyListener, Runnable, ActionListener {
    int size = 600;
    int windWidth = size, windHeight = size;
    int cellSize = 20;
    int birm = 20;

    int[] x = new int[size * size / cellSize / cellSize];
    int[] y = new int[size * size / cellSize / cellSize];

    int len = 3;
    int dir = KeyEvent.VK_RIGHT;

    int foodX, foodY;

    boolean started = false;
    boolean gameOver = false;

    Button startButton = new Button("Start");
    Button restartButton = new Button("Restart");

    Random random = new Random();

    public SnakeGame() {
        setTitle("Snake");
        setSize(windWidth, windHeight);
        setResizable(false);
        setLayout(null);

        startButton.setBounds(windWidth / 2 - 50, windHeight / 2 - 20, 100, 40);
        restartButton.setBounds(windWidth / 2 - 50, windHeight / 2 + 40, 100, 40);

        add(startButton);
        add(restartButton);

        startButton.addActionListener(this);
        restartButton.addActionListener(this);

        addKeyListener(this);
        startButton.addKeyListener(this);
        restartButton.addKeyListener(this);

        restartButton.setVisible(false);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        resetGame();

        setVisible(true);

        new Thread(this).start();
    }

    void resetGame() {
        len = 3;
        dir = KeyEvent.VK_RIGHT;
        gameOver = false;

        x[0] = 100; y[0] = 100;
        x[1] = 80;  y[1] = 100;
        x[2] = 60;  y[2] = 100;

        spawnFood();
    }

    void spawnFood() {
        foodX = birm +
            random.nextInt((windWidth - birm * 2) / cellSize) * cellSize;
        foodY = birm + 40 +
        random.nextInt((windHeight - birm * 2 - 40) / cellSize) * cellSize;
    }

    public void paint(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, windWidth, windHeight);

        if (!started) {
            startButton.setVisible(true);
            restartButton.setVisible(false);
            return;
        }

        if (gameOver) {
            startButton.setVisible(false);
            restartButton.setVisible(true);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", windWidth / 2 - 80, windHeight / 2);
            g.drawString("Ваш счёт: "
                + Integer.toString(len * 10), windWidth / 2 - 80, windHeight / 2 - 30);
            return;
        }

        startButton.setVisible(false);
        restartButton.setVisible(false);

        g.setColor(Color.BLACK);
        g.drawRect(birm, birm + 40, windWidth - birm * 2, windHeight - birm * 2 - 40);

        g.setColor(Color.RED);
        g.fillOval(foodX, foodY, cellSize, cellSize);

        g.setColor(Color.BLUE);
        for (int i = 0; i < len; i++) {
            g.fillRect(x[i], y[i], cellSize, cellSize);
        }
    }

    void move() {
        for (int i = len - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (dir == KeyEvent.VK_UP) y[0] -= cellSize;
        if (dir == KeyEvent.VK_DOWN) y[0] += cellSize;
        if (dir == KeyEvent.VK_LEFT) x[0] -= cellSize;
        if (dir == KeyEvent.VK_RIGHT) x[0] += cellSize;

        if (x[0] < birm || x[0] >= windWidth - birm ||
            y[0] < birm + 40 || y[0] >= windHeight - birm) {
            gameOver = true;
        }

        for (int i = 1; i < len; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                gameOver = true;
            }
        }

        if (x[0] == foodX && y[0] == foodY) {
            len++;
            spawnFood();
        }
    }

    public void run() {
        while (true) {
            if (started && !gameOver) {
                move();
            }

            repaint();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            started = true;
            gameOver = false;

            startButton.setVisible(false);
            restartButton.setVisible(false);

            requestFocus();
        }

        if (e.getSource() == restartButton) {
            resetGame();
            started = true;

            startButton.setVisible(false);
            restartButton.setVisible(false);

            requestFocus();
        }
    }

    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (k == KeyEvent.VK_UP && dir != KeyEvent.VK_DOWN) dir = k;
        if (k == KeyEvent.VK_DOWN && dir != KeyEvent.VK_UP) dir = k;
        if (k == KeyEvent.VK_LEFT && dir != KeyEvent.VK_RIGHT) dir = k;
        if (k == KeyEvent.VK_RIGHT && dir != KeyEvent.VK_LEFT) dir = k;
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new SnakeGame();
    }
}
