import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;

public class PacMan extends JPanel implements ActionListener, KeyListener{
    class Block{
        int x;
        int y;
        int wid;
        int hei;
        Image image;
        Image prevImage;
        //since during the game position odf blocks will change, so we need to maintaintheir start positions
        int startX;
        int startY;
        char direction='U';
        int velocityX=0;
        int velocityY=0;
        boolean scared=false;
        Block(Image image, int x, int y, int wid, int hei,Image prevImgae,boolean scared){
            this.image=image;
            this.x=x;
            this.y=y;
            this.wid=wid;
            this.hei=hei;
            this.startX=x;
            this.startY=y;
            //this.prevImage=prevImage;
            this.scared=scared;

        }

        void updateDirection(char direction){
            char prevDirection=this.direction;
            this.direction=direction;
            updateVelocity();
            this.x+=this.velocityX;
            this.y+=this.velocityY;
            for(Block wall:walls){
                if(collision(this, wall)){
                    this.x-=this.velocityX;
                    this.y-=this.velocityY;
                    this.direction=prevDirection;
                    updateVelocity();
                }
            }
            //change direction only when possible
            //like if going left and  wall on up
            //it shouldn't face in up direction

        }

        void updateVelocity(){
            if(this.direction=='U'){
                this.velocityX=0;
                this.velocityY=-tileSize/16;
            }
            else if(this.direction=='D'){
                this.velocityX=0;
                this.velocityY=tileSize/16;
            }
            else if(this.direction=='L'){
                this.velocityX=-tileSize/16;
                this.velocityY=0;
            }
            else if(this.direction=='R'){
                this.velocityX=tileSize/16;
                this.velocityY=0;
            }
            
        }

        void reset(){
            this.x=this.startX;
            this.y=this.startY;
        }
    
    }
    private Timer scareTimer;
    private Timer blinkTimer;
    private int rowcount=21;
    private int colcount=19;
    private int tileSize= 32;
    private int width= tileSize * colcount;
    private int height= tileSize * rowcount;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    private Image scaredGhostImage;
    private Image scaredResetImage;

   private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "XaXX XXX X XXX XXaX",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "XaXXXXXX X XXXXXXaX",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };
    private String[] tileMapl2 = {
    "XXXXXXXXXXXXXXXXXXX",
    "X        X        X",
    "X XXXX X X X XXXX X",
    "X X             X X",
    "X X X XXXXXXX X X X",
    "X X X       X X X X",
    "X X XXXXXXX X X X X",
    "O X         X X X O",
    "X X XX     XX X X X",
    "O   X   P      X  O",
    "X X XXXXXXX XXXXX X",
    "O X         X   X O",
    "X X XXXXXXX X X X X",
    "X X     b     X X X",
    "X XXXX X X X XXXX X",
    "X     r     o     X",
    "X XXXXX XXXXXXX X X",
    "X       X       X X",
    "X XXXXXXXXXXXXX X X",
    "X     p           X",
    "XXXXXXXXXXXXXXXXXXX"
};
    private String[] tileMapl3 = {
    "XXXXXXXXXXXXXXXXXXX",
    "X   X     X     X X",
    "X X X XXX X XXX X X",
    "X X             X X",
    "X X X XXXXXXX X X X",
    "X X X       X X X X",
    "X X X XXXXX X X X X",
    "O X X X   X X X X O",
    "X X X X   X X X X X",
    "O     X   P     X O",
    "X X X XXXXXXX X X X",
    "O X X       X X X O",
    "X X X XXXXXXX X X X",
    "X X     b     X X X",
    "X X XXX X XXX X X X",
    "X X   r     o X X X",
    "X XXXXX X XXXXX X X",
    "X       X       X X",
    "X XXXXXXXXXXXXX X X",
    "X   p             X",
    "XXXXXXXXXXXXXXXXXXX"
};



    HashSet<Block> walls;
    HashSet<Block> ghosts;
    HashSet<Block> foods;
    HashSet<Block> powfoods;
    Block pacman;

    Timer gameLoop;
    char[] directions={'D','L','R','U'};//directions for ghost
    Random random=new Random();
    //to be started after loading map
    int score=0;
    int lives=3;
    int level=1;
    boolean gameOver=false;

    PacMan(){//constructor
        setPreferredSize(new Dimension(width,height));
        setBackground(Color.BLACK);
        addKeyListener(this);
        //now making sure that JPanel listens to key
        setFocusable(true);//this will make sure that this panel is focused and listens to key events

        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();//getting the loation of stored images, ./ means stored in the same folder as this java file
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        scaredGhostImage = new ImageIcon(getClass().getResource("./scaredGhost.png")).getImage();
        scaredResetImage = new ImageIcon(getClass().getResource("./download.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();

        //X- WALL, O-EMPTY, Nothing- food, P- pacman, p,b,r,o-ghosts
        //creating the game board
        //stroing these in hashset so that we can iterate
        loadMap(tileMap);
        for(Block ghost:ghosts){
            char newDirection=directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
            //dircetion give, now move in move function
        }
        gameLoop=new Timer(15,this);//here 'this' will call actionlistener
        //which will go to last and call repaint every 50 ms i.e. 1000/50 = 20 fps
        gameLoop.start();//now add keylsitener
    }

//this one runs once at the start only
    public void loadMap(String[] tileMap){//drawing blocks
        walls=new HashSet<Block>();
        ghosts=new HashSet<Block>(); 
        foods=new HashSet<Block>();
        powfoods=new HashSet<Block>();

        for(int r=0;r<rowcount;r++){
            for(int c=0;c<colcount;c++){
                String row= tileMap[r];
                char tile=row.charAt(c);
                int x= c*tileSize;
                int y= r*tileSize;
                if(tile=='X'){//this only gives a image, but to draw, use different function
                    Block wall= new Block(wallImage, x,y,tileSize,tileSize,wallImage,false);
                    walls.add(wall);
                }
                else if(tile=='b'){
                    Block ghost =new Block(blueGhostImage,x,y,tileSize,tileSize,blueGhostImage,false);
                    ghosts.add(ghost); 
                }
                else if(tile=='o'){
                    Block ghost =new Block(orangeGhostImage,x,y,tileSize,tileSize,orangeGhostImage,false);
                    ghosts.add(ghost); 
                }
                else if(tile=='p'){
                    Block ghost =new Block(pinkGhostImage,x,y,tileSize,tileSize,pinkGhostImage,false);
                    ghosts.add(ghost); 
                }
                else if(tile=='r'){
                    Block ghost =new Block(redGhostImage,x,y,tileSize,tileSize,redGhostImage,false);
                    ghosts.add(ghost); 
                }
                else if(tile=='P'){
                    pacman=new Block(pacmanRightImage,x,y,tileSize,tileSize,pacmanRightImage,false);
                }
                else if(tile==' '){
                    Block food =new Block(null,x+14,y+14,4,4,null,false); 
                    foods.add(food);
                }
                else if(tile=='a'){
                    Block powfood =new Block(null,x+14,y+14,14,14,null,false); 
                    powfoods.add(powfood);
                }
            }
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //here, super invokes same funtion paintComponent in JPanel
        draw(g);
    }

    public void draw(Graphics g){
        //g.fillRect(pacman.x,pacman.y,pacman.wid,pacman.hei);//just an example
        //g.setColor(Color.BLACK);
        g.drawImage(pacman.image,pacman.x,pacman.y,pacman.wid,pacman.hei,null);

        for(Block ghost:ghosts){
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.wid,ghost.hei,null);
        }
        for(Block wall:walls){
            g.drawImage(wall.image,wall.x,wall.y,wall.wid,wall.hei,null);
        }
        g.setColor(Color.white);
        for(Block food:foods){
            g.fillRoundRect(food.x,food.y,food.wid,food.hei,4,4);
        }
        for(Block powfood:powfoods){
            g.fillRoundRect(powfood.x,powfood.y,powfood.wid,powfood.hei,14,14);
        }

        //score
        g.setFont(new Font("Arial",Font.PLAIN,18));
        if(gameOver && level!=3){
            g.drawString("Bye Bye Bye "+String.valueOf(score),tileSize/2,tileSize/2);
        }
        else if(gameOver && level==3){
            g.drawString("You Win "+String.valueOf(score),tileSize/2,tileSize/2);
        }
        else {
            g.drawString("x "+String.valueOf(lives)+" Score "+ String.valueOf(score),tileSize/2,tileSize/2);
        }

    }

    public void move(){
        pacman.x+=pacman.velocityX;
        pacman.y+=pacman.velocityY;//to be put before repaint

        for(Block wall:walls){
            if(collision(pacman, wall)){
                pacman.x-=pacman.velocityX;
                pacman.y-=pacman.velocityY;
                break;
                //if there is a collision, then take a tep back
                //altogether this function cancels any movement
            }
        }
        Block foodEaten=null;
        for(Block food:foods){
            if(collision(pacman,food)){
                foodEaten=food;
                score+=10;

            }
        }
        foods.remove(foodEaten);

        Block powfoodEaten=null;
        for(Block powfood:powfoods){
            if(collision(pacman,powfood)){
                powfoodEaten=powfood;
                score+=10;
                powFoodEaten();
            }
        }
        powfoods.remove(powfoodEaten);

        if(foods.isEmpty()){//next level
            level++;
            if(level==2)loadMap(tileMapl2);
            else if(level==3)loadMap(tileMapl3);
            else{
                gameOver=true;
                gameLoop.stop();
            }
            resetPositions();
        }
//teleportation
        if(pacman.y<0){
            pacman.y=height+pacman.hei;         
        }
        else if(pacman.y+pacman.hei>=height){
            pacman.y=0;         
        }
        if(pacman.x<=0){
            pacman.x=width-pacman.wid;         
        }
        else if(pacman.x+pacman.wid>=width){
            pacman.x=0;         
        }

        for(Block ghost:ghosts){
            if(collision(pacman,ghost) && !ghost.scared){
                lives--;
                resetPositions();
            }
            if(collision(pacman, ghost) && ghost.scared){
                score+=50;
                ghost.image=ghost.prevImage;
                ghost.scared=false;
                ghost.reset();
                ghost.updateDirection(directions[random.nextInt(4)]);
            }
            if(ghost.y==tileSize*9 && ghost.direction!='U' && ghost.direction!='D'){
                ghost.updateDirection('U');

            }
            ghost.x+=ghost.velocityX;
            ghost.y+=ghost.velocityY;
            for(Block wall:walls){
                if(collision(ghost,wall) || ghost.x<0 || ghost.wid+ghost.x>=width){
                    ghost.x-=ghost.velocityX;
                    ghost.y-=ghost.velocityY;
                    char newDirection=directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
            //teleportation
            if(ghost.y<=0){
                ghost.y=height+ghost.hei;         
            }
            else if(ghost.y+ghost.hei>=height){
                ghost.y=0;         
            }
            if(ghost.x<=0){
                ghost.x=width-ghost.wid;         
            }
            else if(ghost.x+ghost.wid>=width){
                ghost.x=0;         
            }
        }
        

    }
 // Timer to revert ghost images after 5 seconds
    /*public void powFoodEaten() {
        // Set ghosts to scared mode
        ghostScared = true;
        for (Block ghost : ghosts) { // Save current image
            ghost.prevImage = ghost.image; // Save the current image
            ghost.image = scaredGhostImage; // Set scared image
        }

        // Stop any previous scare timer before starting a new one
        if (scareTimer != null && scareTimer.isRunning()) {
            scareTimer.stop();
        }

        // Create a new timer to revert ghost images after 5 seconds (5000 ms)
        if(level==1)scareTimer = new Timer(7000, e -> {
            for (Block ghost : ghosts) {
                if (ghost.image == scaredGhostImage) {
                    ghost.image = ghost.prevImage; // Revert to original image
                }
            }
            ghostScared = false;
        });
        if(level==2)scareTimer = new Timer(5000, e -> {
            for (Block ghost : ghosts) {
                if (ghost.image == scaredGhostImage) {
                    ghost.image = ghost.prevImage; // Revert to original image
                } // Reset scared state
            }
            ghostScared = false;
        });
        if(level==3)scareTimer = new Timer(4000, e -> {
            for (Block ghost : ghosts) {
                if (ghost.image == scaredGhostImage) {
                    ghost.image = ghost.prevImage; // Revert to original image
                }
            }
            ghostScared = false;
        });
        scareTimer.setRepeats(false); // Run only once
        scareTimer.start();           // Start the timer

    }*/
public void powFoodEaten() {
    

    for (Block ghost : ghosts) {
        ghost.prevImage = ghost.image;
        ghost.image = scaredGhostImage;
        ghost.scared = true;
    }

    if (scareTimer != null && scareTimer.isRunning()) scareTimer.stop();
    if (blinkTimer != null && blinkTimer.isRunning()) blinkTimer.stop();

    int scareDuration = switch (level) {
        case 1 -> 7000;
        case 2 -> 5000;
        case 3 -> 4000;
        default -> 5000;
    };

    // Start the main scare timer
    scareTimer = new Timer(scareDuration, e -> {
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop(); // Stop blinking
        }
        for (Block ghost : ghosts) {
            if (ghost.image == scaredGhostImage || ghost.image == scaredResetImage) {
                ghost.image = ghost.prevImage;
                ghost.scared = false; // Reset scared state
            }
        }
        
    });
    scareTimer.setRepeats(false);
    scareTimer.start();

    // Start blinking in last 1500ms
    blinkTimer = new Timer(300, new ActionListener() {
        boolean toggle = false;

        public void actionPerformed(ActionEvent e) {
            for (Block ghost : ghosts) {
                if (ghost.image == scaredGhostImage || ghost.image == scaredResetImage) {
                    ghost.image = toggle ? scaredResetImage : scaredGhostImage;
                }
            }
            toggle = !toggle;
        }
    });

    // Start the blink timer after (scareDuration - 1500) ms
    new Timer(scareDuration - 1500, e -> {
        blinkTimer.start();
    }) {{
        setRepeats(false);
        start();
    }};
}


    public boolean collision(Block a, Block b){
        return a.x<b.x+b.wid &&
                a.x+a.wid>b.x &&
                a.y<b.y+b.hei &&
                a.y+a.hei>b.y;
    }
    public void resetPositions(){
        if(lives==0){
            gameOver=true;            
            gameLoop.stop();
            return;
        }
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;
        //gameLoop.setInitialDelay(1000);
        //gameLoop.;

        for(Block ghost:ghosts){
            ghost.reset();
            char newDirection=directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }        
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();//needed to update our bprad for ghsts nd pacman positions
        //after a fixed time, so make timeloop
        if(gameOver)gameLoop.stop();
    }


    @Override
    public void keyTyped(KeyEvent e) {//gives the cahracter of the key presses,but since arrows used, leav eit empty
    }


    @Override
    public void keyPressed(KeyEvent e) {//as long as that key is pressed, thi sis activated, but we dont want this for now
    }


    @Override
    public void keyReleased(KeyEvent e) {//only needed
        //System.out.println("KeyEvent:"+ e.getKeyCode());

        if(gameOver){
            loadMap(tileMap);
            lives=3;
            resetPositions();
            score=0;
            gameOver=false;
            gameLoop.start();
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }
        if(pacman.direction=='U'){
            pacman.image=pacmanUpImage;
        }
        else if(pacman.direction=='D'){
            pacman.image=pacmanDownImage;
        }
        else if(pacman.direction=='L'){
            pacman.image=pacmanLeftImage;
        }
        else if(pacman.direction=='R'){
            pacman.image=pacmanRightImage;
        }
    }
}
