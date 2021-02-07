package org.academiadecodigo.cachealots.runner.blocks;

import org.academiadecodigo.cachealots.runner.grid.Grid;
import org.academiadecodigo.cachealots.runner.grid.Position;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;


public class Block extends Rectangle{  // class with the characteristics of the running blocks

    private Rectangle obstacle;
    private Position pos;
    //private BlockType type;

    public Block(Grid grid, int col, int row){
        this.pos = pos;
        this.grid = grid;
        Rectangle obstacle  = new Rectangle(grid.getWidth() - 1), (grid.getHeight() - (2.5 * grid.getCellSize())) + grid.getY(), grid.getCellSize(), grid.getCellSize();


        //this.type = type;
    }


    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }



}//end of Block class
