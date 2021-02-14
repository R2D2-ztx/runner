package org.academiadecodigo.cachealots.runner;

import org.academiadecodigo.bootcamp.Sound;
import org.academiadecodigo.cachealots.runner.character.Character;
import org.academiadecodigo.cachealots.runner.character.CharacterType;
import org.academiadecodigo.cachealots.runner.blocks.Block;
import org.academiadecodigo.cachealots.runner.blocks.BlockFactory;
import org.academiadecodigo.cachealots.runner.grid.Grid;
import org.academiadecodigo.cachealots.runner.grid.Movement;
import org.academiadecodigo.cachealots.runner.movingGFX.*;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.graphics.Text;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.Iterator;

public class Game {

    public static Grid grid;
    public static int timer = -1;
    public static int delay = 30;
    public static int level = 0;
    public static int blockToLvUp = 0;

    private int levelUpFrames;

    private boolean running;
    private boolean gameOver;

    private boolean showingLevelUpSprite;
    public Character character;

    private Keyboard keyboard;
    private RunnerKeyboardHandler handler;

    private Rectangle rectangleHideLeft;
    private Movement movement;
    private MovingGround ground;

    private CloudBackground cloudBackground;

    private CloudFactory cloudFactory;
    private BlockFactory blockFactory;

    private Text levelHUD;
    private Text scoreHUD;


    private Picture levelUpPicture;
    private Picture gameOverLogo;

    private Sound whatIsLoveMusic = new Sound("/resources/what is love.wav");
    private Sound funkNaruto = new Sound("/resources/Sadness.wav");

    public void initTools() {

        grid = new Grid();

        rectangleHideLeft = new Rectangle(0,0,grid.PADDING, grid.getHeight()+ grid.PADDING);
        rectangleHideLeft.setColor(Color.WHITE);

        character = new Character(CharacterType.MARIO, grid);
        grid.init();

        movement = new Movement(grid, character);
        handler = new RunnerKeyboardHandler(grid,this);
        handler.setMovement(movement);
        keyboard = new Keyboard(handler);
        keyboard.addEventListener(KeyboardEvent.KEY_SPACE, KeyboardEventType.KEY_PRESSED);

    }

    public void init() {

        character = new Character(CharacterType.MARIO, grid);

        cloudBackground = new CloudBackground(grid);
        cloudFactory = new CloudFactory();
        blockFactory = new BlockFactory();
        ground = new MovingGround(grid);

        rectangleHideLeft = new Rectangle(0, 0, grid.PADDING, grid.getHeight() + grid.PADDING);
        rectangleHideLeft.setColor(Color.WHITE);

        gameOverLogo = new Picture(grid.CELL_SIZE * 4.7, grid.CELL_SIZE * 1.5, "resources/gameover2.png");
        levelUpPicture = new Picture(grid.CELL_SIZE * 11, grid.CELL_SIZE * 2, "resources/pikachu-meme.png");

        levelHUD = new Text(grid.CELL_SIZE, grid.CELL_SIZE, "");
        scoreHUD = new Text(grid.CELL_SIZE, grid.CELL_SIZE * 2, "");



    }

    public boolean isRunning() {
        return running;
    }

    public void start() throws InterruptedException {
        running = true;
        initTools();
        init();
        cloudBackground.show();
        ground.drawGround();
        whatIsLoveMusic.play(true);

        levelHUD.draw();
        scoreHUD.draw();

        while (true) {

            //Game Clock for all movements
            Thread.sleep(delay);

            if(running){

                timer++;

                levelHUD.setText("Level: " + level);
                levelHUD.draw();

                scoreHUD.setText("Score: " + blockFactory.getBlockCounter());
                scoreHUD.draw();

                double x = (Math.ceil(Math.random() * 4)) * 45;
                if (timer % x == 0) blockFactory.create();

                if (timer % 8 == 0) cloudBackground.show();
                if (timer % 2 == 0) ground.drawGround();
                if (timer % 3 == 0) collisionDetector();
                if (230 < character.getSprite().getY()) character.setSingleJump(true);

                character.getSprite().draw();
                rectangleHideLeft.fill();

                blockFactory.removeOffscreenBlocks();

                levelUp(blockFactory.getBlockCounter());

                moveAll();

            } else {

                if(gameOver) {
                    gameOverLogo.draw();
                    whatIsLoveMusic.stop();
                    funkNaruto.play(true);
                    System.out.println("Game Over");
                    System.out.println("Your Score: " + blockFactory.getBlockCounter());
                    gameOver = false;
                }

            }

        }

    }

    public void reset() throws InterruptedException {

        Iterator<Cloud> itClouds = cloudFactory.iterator();
        while (itClouds.hasNext()) itClouds.next().getSprite().delete();
        cloudFactory.clearCloudList();


        Iterator<Block> itBlocks = blockFactory.iterator();
        while (itBlocks.hasNext()) itBlocks.next().getSprite().delete();
        blockFactory.clearBlockList();

        cloudBackground.resetSprite();
        cloudBackground.initSprite();

        ground.reset();
        gameOverLogo.delete();

        character.getSprite().delete();
        character.resetSprite();
        levelUpPicture.delete();

        levelHUD.delete();
        scoreHUD.delete();


        init();
        running = true;
        funkNaruto.stop();
        whatIsLoveMusic.play(true);


        timer = -1;
        delay = 30;
        level= 0;
        blockToLvUp = 0;

    }

    public void moveAll(){
        Iterator<Block> it = blockFactory.iterator();
        while (it.hasNext()) {
            Block b = it.next();
            if(b.isOnScreen()) b.move();
        }
        character.moveFlow();
    }

    private void collisionDetector() {


        for (int Xcharacter = character.getSprite().getX(); Xcharacter < (character.getSprite().getX() + character.getSprite().getWidth()); Xcharacter++) {
            for (int Ycharacter = character.getSprite().getY(); Ycharacter < (character.getSprite().getY() + character.getSprite().getHeight()); Ycharacter++) {

                for (Iterator<Block> it = blockFactory.iterator(); it.hasNext(); ) {
                    Block block = it.next();

                    for (int Xblock = block.getX(); Xblock < (block.getX() + block.getWidth()); Xblock++) {
                        for (int Yblock = block.getY(); Yblock < (block.getY() + block.getHeight()); Yblock++) {
                            if (Xcharacter == Xblock && Ycharacter == Yblock) {
                                gameOver = true;
                                running = false;
                            }
                        }
                    }
                }
            }
        }
    }

    private void levelUp(int score) throws InterruptedException {

        if(score == blockToLvUp ) {
            System.out.println("You are in level " + level);
            level++;
            delay -= 2;
            blockToLvUp += 7;


            /*
            if(showingLevelUpSprite){
                levelUpFrames++;
                if(levelUpFrames == 10){
                    levelUpPicture.delete();
                    showingLevelUpSprite = false;
                }
                return;

            }*/
            if(blockToLvUp > 8) {
                levelUpPicture.draw();
                //showingLevelUpSprite = true;
                Thread.sleep(300);
                levelUpPicture.delete();
            }


        }
    }


}
