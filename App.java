import javax.swing.*;
public class App 
{
    public static void main(String[] args) throws Exception 
    {
        int boardwidth=600;
        int boardheight=boardwidth; //the title bar also is a part of this size
        JFrame frame=new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardwidth,boardwidth);
        frame.setLocationRelativeTo(null); //this will open at the centre of the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        snakegame SnakeGame=new snakegame(boardwidth, boardheight);
        frame.add(SnakeGame);
        frame.pack(); // this will place the JPanel inside the frame with the full dimensions
        SnakeGame.requestFocus(); 
    }
}
