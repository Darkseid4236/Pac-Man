import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Pac-Man");
        //in this hame, the wall png has dimesiono of 32x32px
        //21 rows(0-20), 19() columns, 0 indexed
        int rowcount=21;
        int colcount=19;
        int tileSize= 32;
        int width= tileSize * colcount;
        int height= tileSize * rowcount;
        frame.setSize(width, height);
        //frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        //now need a panel to draw on
        PacMan pacmanGame=new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}
