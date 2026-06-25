import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class snakegame extends JPanel implements ActionListener, KeyListener
{
    private class Tile
    {
        int x;
        int y;
        Tile(int x , int y )
        {
            this.x=x;
            this.y=y;
        }
    }
    int boardwidth, boardheight;
    int tilesize=25;
    Tile snakehead;
    ArrayList<Tile> snakebody;

    //for the food
    Tile food;
    Random random; //to randomly place food on the screen

    //game logic
    Timer gameloop; //to set timer for the looping of the game
    int vx; //velocity in x direction
    int vy; //velocity in y direction
    boolean over = false;
    boolean pause= false;
    int score=0;
    int highs=0; //saving high score

    snakegame (int boardwidth, int boardheight) 
    {
        this.boardwidth = boardheight;
        this.boardheight = boardheight; //this. is used to distinguish between parameter and a member of a class
        setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        snakehead = new Tile(5,5);
        snakebody = new ArrayList<Tile>();
        food = new Tile(10,10);
        random = new Random();
        placefood(); 

        vx = 0;
        vy = 0;

        gameloop = new Timer(100, this);
        gameloop.start();

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
        if (pause)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD,40));
            String text="PAUSED";
            FontMetrics m=getFontMetrics(g.getFont());
            g.drawString(text, (boardwidth-m.stringWidth(text))/2,boardheight/2);
        }
    }
    public void draw(Graphics g) 
    {
        //for making gridlines
        for(int i=0; i<(boardwidth/tilesize);i++){
            g.drawLine(i*tilesize,0, i*tilesize, boardheight); //vertical lines
            g.drawLine(0, i*tilesize, boardwidth, i*tilesize); //horizontal lines

        }
        //draw food for the snake
        g.setColor(Color.red);
        g.fillRect(food.x*tilesize, food.y*tilesize, tilesize, tilesize);
        //for the snake head
        g.setColor(Color.green);
        g.fillRect(snakehead.x*tilesize, snakehead.y*tilesize, tilesize, tilesize);
        //snake body
        for(int i=0; i<snakebody.size(); i++){
            Tile snakepart = snakebody.get(i);
            g.fillRect(snakepart.x*tilesize, snakepart.y*tilesize, tilesize, tilesize);

        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (over)
        {
            g.setColor(Color.red);
            g.drawString("Game Over    Score :  "+ String.valueOf(snakebody.size()), tilesize-16, tilesize);
        }
        else 
        {
            g.drawString("Score : "+ String.valueOf(snakebody.size()), tilesize-16,tilesize);
        }
    }
    public void placefood() 
    {
        food.x = random.nextInt(boardwidth/tilesize); //600/25=24 so x will be a random no. from 0 to 24
        food.y = random.nextInt(boardheight/tilesize); //same as above as x
    }
    public boolean collision(Tile t1, Tile t2)
    {
        return t1.x==t2.x && t1.y==t2.y;

    }

    public void move() 
    {
        //eating food
        if (collision(snakehead, food)) 
        {
            snakebody.add(new Tile(food.x, food.y));
            placefood();
        }
        //snake body and iterate backwards
        for (int i= snakebody.size()-1;i>=0;i--) 
        {
            Tile snakepart = snakebody.get(i);
            if (i==0) 
            {
                snakepart.x=snakehead.x;
                snakepart.y=snakehead.y;
            }
            else 
            {
                Tile prevsnakepart=snakebody.get(i-1);
                snakepart.x=prevsnakepart.x;
                snakepart.y=prevsnakepart.y;

            }
        }


        //for the snake head
        snakehead.x += vx;
        snakehead.y += vy;

        //for the game over 
        for (int i=0; i < snakebody.size(); i++) //for collisions with its own body
        {
            Tile snakepart = snakebody.get(i);
            if (collision(snakehead, snakepart)) 
            {
                over=true;

            }
        }
        if (snakehead.x*tilesize<0 || snakehead.x*tilesize > boardwidth || snakehead.y*tilesize < 0 || snakehead.y*tilesize > boardheight)
        {
            over = true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        move();//this will update the x and y position of the snake
        repaint(); //so every 100 miliseconds we are going to call this actionPerformed which will repaint and repaints will call draw over and over again
        if(over) 
        {
            gameloop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) 
    {
        
        if (e.getKeyCode()==KeyEvent.VK_UP && vy!=1)
        {
            vx=0;
            vy=-1;
        }
        else if ((e.getKeyCode()==KeyEvent.VK_DOWN && vy!=-1)) 
        {
            vx=0;
            vy=1;
        }   
        else if (e.getKeyCode()==KeyEvent.VK_LEFT && vx != 1) 
        {
            vx=-1;
            vy=0;
        }
        else if (e.getKeyCode()==KeyEvent.VK_RIGHT && vx != -1)
        {
            vx=1;
            vy=0;
        }
        else if (e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            pause = !pause;
            if(pause)
            {
                gameloop.stop();
            }
            else 
            {
                gameloop.start();
            }
            repaint();
        }
    }
    
    //we dont need these but we need to define them
    @Override
    public void keyTyped(KeyEvent e) {}
  
    @Override
    public void keyReleased(KeyEvent e) {}
}
