import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 550;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;   //number of game units
    static final int DELAY = 110;
    static int x[] = new int[GAME_UNITS];
    static int y[] = new int[GAME_UNITS];

    //Initial size of the snake
    int bodyParts = 6;
    int applesEaten;

    //Location of the apple
    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    //Panel of the game
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    // start the game
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    //paint the components
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // all the designing work here -> designing of panel , snake and apple
    public void draw(Graphics g){
//        g.setColor(Color.WHITE);
//        for(int i =0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
//            g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
//            g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
//        }
        if(running){
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            for(int i= 0;i<bodyParts;i++){
                if(i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }else{
                    g.setColor(new Color(45,180,0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD,30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

        }
        else {
            gameOver(g);
        }
    }

    //Generate the coordinate of the new apple
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    // coordinate change on movement
    public void move(){
        for(int i =bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    // after eating apple :)
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollision(){
        //checks if head collides with body
        for(int i=bodyParts;i>0;i--){
            if(x[0] == x[i] & y[0] ==y[i]){
                running = false;
            }
        }
        //checks if head touches left border
        if(x[0]<0){
            running = false;
        }
        //checks if head touches right border
        if(x[0]>SCREEN_WIDTH){
            running = false;
        }
        //checks if head touches top border
        if(y[0] < 0){
            running = false;
        }
        //checks if head touches bottom border
        if(y[0] > SCREEN_WIDTH){
            running = false;
        }
        if(!running){
            timer.stop();
        }

    }

    //Game overt screen
    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game over'"))/2, SCREEN_HEIGHT/2);
        gameOverScore(g);

    }
    public void gameOverScore(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

    }


    // Get the game moving
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();

    }

    //taking the input from user to run move left , right , up or down
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){

                //if key pressed is left -> if current direction is not right -> turn left
                //coz we can't let the snake turn 180 degrees

                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }
    }
}
