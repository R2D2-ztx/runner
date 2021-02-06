package org.academiadecodigo.cachealots.runner;

import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Grid {

    //State


    //-- Constants

    private final int PADDING = 10;
    private final int CELL_SIZE = 30;


    //-- Properties

    private int cols;
    private int rows;
    private Rectangle rectangle;
    Picture backGround;


    //Behavior

    //-- Constructor

    public Grid(int cols, int rows){
        this.cols = cols;
        this.rows = rows;


        backGround = new Picture(PADDING, PADDING, "resources/backGround.png");
        rectangle = new Rectangle(PADDING, PADDING, backGround.getWidth(), backGround.getHeight());
    }


    //-- Main game methods

    //-- Initialize: show grid on the screen
    public void init(){
        //rectangle.setColor(Color.CYAN);
        backGround.draw();
        rectangle.draw();

        //outline.setColor(Color.DARK_GRAY);
        //outline.draw();
    }


    //TODO: have specific methods to create characterBlock, cloudBlock, obstacleBlock
    public Block makeBlock(){

        //TODO: replace with call to BlockFactory.createBlock()
        return new Block();

    }




    //-- Getters

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public int getPadding() {
        return PADDING;
    }

    //-- Other methods

    //--- X & Y

    public int getX(){
        return rectangle.getX() + PADDING;
    }

    public int getY(){
        return rectangle.getY() + PADDING;
    }


    //--- Width & Height

    public int getWidth(){
        return rectangle.getWidth();
    }

    public int getHeight(){
        return rectangle.getHeight();
    }


    //--- Aux methods to calculate cells to pixels

    public int colsToX(int cols){
        return (cols * CELL_SIZE) + PADDING;
    }

    public int rowsToY(int rows){
        return (rows * CELL_SIZE) + PADDING;
    }



}//end of Grid class