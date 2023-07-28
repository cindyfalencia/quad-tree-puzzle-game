package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
    private int xCoord;
    private int yCoord;
    private int size; // height or width of the square
    private int level; // the root (outer most block) is at level 0
    private int maxDepth;
    private Color color;

    private Block[] children; // {UR, UL, LL, LR}

    public static Random gen = new Random(4);

    /*
     * These two constructors are here for testing purposes.
     */
    public Block() {}

    public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
        this.xCoord=x;
        this.yCoord=y;
        this.size=size;
        this.level=lvl;
        this.maxDepth = maxD;
        this.color=c;
        this.children = subBlocks;
    }

    /*
     * Creates a random block given its level and a max depth.
     * xCoord, yCoord, size, and highlighted should not be initialized
     * (i.e. they will all be initialized by default)
     */
    public Block(int lvl, int maxDepth) {
        if (lvl > maxDepth) {
            throw new IllegalArgumentException("Level cannot be greater than maxDepth");
        }
        this.level = lvl;
        this.maxDepth = maxDepth;

        if (lvl < maxDepth) {
            double randomNumber = gen.nextDouble();

            if (randomNumber < Math.exp(-0.25 * level)) {
                this.children = new Block[4];
                for (int i = 0; i < 4; i++) {
                    this.children[i] = new Block(lvl+1, maxDepth);
                }
            } else {
                this.children = new Block[0]; // Create an empty array for leaf nodes
                int colorIndex = gen.nextInt(GameColors.BLOCK_COLORS.length);
                this.color = GameColors.BLOCK_COLORS[colorIndex];
            }
        } else {
            this.children = new Block[0]; // Create an empty array for leaf nodes
            int colorIndex = gen.nextInt(GameColors.BLOCK_COLORS.length);
            this.color = GameColors.BLOCK_COLORS[colorIndex];
        }
    }

    /*
     * Updates size and position for the block and all of its sub-blocks, while
     * ensuring consistency between the attributes and the relationship of the
     * blocks.
     *
     *  The size is the height and width of the block. (xCoord, yCoord) are the
     *  coordinates of the top left corner of the block.
     */
    public void updateSizeAndPosition(int size, int xCoord, int yCoord) {
        if (size <= 0 || size % Math.pow(2, this.maxDepth - this.level) != 0 ){
            throw new IllegalArgumentException("Invalid size input");
        }
        this.size = size;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        if (children.length > 0) {
            int newSize = size / 2;
            children[0].updateSizeAndPosition(newSize, xCoord + newSize, yCoord);
            children[1].updateSizeAndPosition(newSize, xCoord, yCoord);
            children[2].updateSizeAndPosition(newSize, xCoord, yCoord + newSize);
            children[3].updateSizeAndPosition(newSize, xCoord + newSize, yCoord + newSize);
        }
    }

    /*
     * Returns a List of blocks to be drawn to get a graphical representation of this block.
     *
     * This includes, for each undivided Block:
     * - one BlockToDraw in the color of the block
     * - another one in the FRAME_COLOR and stroke thickness 3
     *
     * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
     *
     * The order in which the blocks to draw appear in the list does NOT matter.
     */
    public ArrayList<BlockToDraw> getBlocksToDraw() {
        ArrayList<BlockToDraw> blocksToDraw = new ArrayList<>();
        int adjustedSize = size - 3;

        if(children.length == 0){
            blocksToDraw.add(new BlockToDraw(color, xCoord, yCoord, adjustedSize, 0));
            blocksToDraw.add(new BlockToDraw(GameColors.FRAME_COLOR, xCoord, yCoord, size, 3));
        }
        for (Block child : children) {
            blocksToDraw.addAll(child.getBlocksToDraw());
        }
        return blocksToDraw;
    }

    /*
     * This method is provided and you should NOT modify it.
     */
    public BlockToDraw getHighlightedFrame() {
        return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
    }

    /*
     * Return the Block within this Block that includes the given location
     * and is at the given level. If the level specified is lower than
     * the lowest block at the specified location, then return the block
     * at the location with the closest level value.
     *
     * The location is specified by its (x, y) coordinates. The lvl indicates
     * the level of the desired Block. Note that if a Block includes the location
     * (x, y), and that Block is subdivided, then one of its sub-Blocks will
     * contain the location (x, y) too. This is why we need lvl to identify
     * which Block should be returned.
     *
     * Input validation:
     * - this.level <= lvl <= maxDepth (if not throw exception)
     * - if (x,y) is not within this Block, return null.
     */
    public Block getSelectedBlock(int x, int y, int lvl) {
        // Check input
        if (lvl < this.level || lvl > this.maxDepth) {
            throw new IllegalArgumentException("Invalid level input");
        }
        if (x < this.xCoord || x >= this.xCoord + this.size || y < this.yCoord || y >= this.yCoord + this.size) {
            return null;
        }
        // If the level is equal to the current block's level, return the block itself
        if (lvl == this.level) {
            return this;
        }

        // If the block is not subdivided, return the block itself
        if (children.length == 0) {
            return this;
        }

        // Recursively search the appropriate child block
        for (Block child : children) {
            Block selectedBlock;
            try{
                selectedBlock = child.getSelectedBlock(x, y, lvl);
            } catch (IllegalArgumentException e){
                continue;
            }
            if (selectedBlock != null) {
                return selectedBlock;
            }
        }
        // If no matching block is found, return null (this should never happen)
        return null;
    }
    /*
     * Swaps the child Blocks of this Block.
     * If input is 1, swap vertically. If 0, swap horizontally.
     * If this Block has no children, do nothing. The swap
     * should be propagate, effectively implementing a reflection
     * over the x-axis or over the y-axis.
     *
     */
    public void reflect(int direction) {
        if(direction != 0 && direction != 1){
            throw new IllegalArgumentException("Invalid");
        }
        if(this.children.length == 0){
            return;
        }
        Block left, right;
        // swap 0
        int i, j;
        i = 0;
        j = 3 - 2 * direction;

        swap(i, j);

        // swap 2
        i = 2;
        j = 1 + direction * 2;

        swap(i, j);

        for(Block child : children){
            child.reflect(direction);
        }
    }

    private void swap(int i, int j) {
        Block left = this.children[i];
        Block right = this.children[j];
        int x = left.xCoord, y = left.yCoord;

        this.children[i].updateSizeAndPosition(right.size, right.xCoord, right.yCoord);
        this.children[j].updateSizeAndPosition(left.size, x, y);

        this.children[i] = right;
        this.children[j] = left;
    }
    /*
     * Rotate this Block and all its descendants.
     * If the input is 1, rotate clockwise. If 0, rotate
     * counterclockwise. If this Block has no children, do nothing.
     */
    public void rotate(int direction) {
        if (direction != 0 && direction != 1) {
            throw new IllegalArgumentException("Invalid input");
        }
        if (children.length == 0) {
            return;
        }
        switch(direction){
            case 0: { // counter clock wise
                for(int i=0; i<3; i++){
                    swap(3-i, 3-i-1);
                }
                break;
            }
            case 1:{
                for(int i=0;i<3;i++){
                    swap(i, i+1);
                }
                break;
            }
        }
    }

    /*
     * Smash this Block.
     *
     * If this Block can be smashed,
     * randomly generate four new children Blocks for it.
     * (If it already had children Blocks, discard them.)
     * Ensure that the invariants of the Blocks remain satisfied.
     *
     * A Block can be smashed iff it is not the top-level Block
     * and it is not already at the level of the maximum depth.
     *
     * Return True if this Block was smashed and False otherwise.
     *
     */
    public boolean smash() {
        // Check if the Block can be smashed
        if (level == 0 || level >= maxDepth) {
            return false;
        }

        // randomly generate colors for the new children
        Random gen = new Random();
        Color[] colors = {Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE};

        int newSize = size/2;
        int newLevel = level + 1;
        children = new Block[4];

        for(int i=0; i<4; i++){
            Color childColor = colors[gen.nextInt(colors.length)];

            int childX = xCoord + (i%2) * newSize;
            int childY = yCoord + (i/2) * newSize;

            children[i] = new Block(newLevel, maxDepth);
            children[i].updateSizeAndPosition(newSize, childX, childY);
        }
        // set the color of the current Block to null since it has children now
        color = null;
        return true;
    }

    /*
     * Return a two-dimensional array representing this Block as rows and columns of unit cells.
     *
     * Return and array arr where, arr[i] represents the unit cells in row i,
     * arr[i][j] is the color of unit cell in row i and column j.
     *
     * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
     */

    public Color[][] flatten() {
        int gridSize = (int) Math.pow(2, this.maxDepth);
        Color[][] grid = new Color[gridSize][gridSize];

        this._flatten(grid, 0, 0, gridSize);
        return grid;
    }

    private void _flatten(Color[][] grid, int x1, int y1, int gridSize) {
        if (color != null) {
            // fill all cells in the subgrid with its color
            for (int i = 0; i < gridSize; i++)
                for (int j = 0; j < gridSize; j++)
                    grid[y1 + i][x1 + j] = color;
        } else {
            int halfSize = gridSize / 2;
            for (int i = 0; i < 4; i++) {
                int dx = (int) Math.abs(i - 1.5);
                int dy = i / 2;
                this.children[i]._flatten(grid, x1 + halfSize * dx, y1 + halfSize * dy, gridSize / 2);
            }
        }
    }

    // These two get methods have been provided. Do NOT modify them.
    public int getMaxDepth() {
        return this.maxDepth;
    }
    public int getLevel() {
        return this.level;
    }


    /*
     * The next 5 methods are needed to get a text representation of a block.
     * You can use them for debugging. You can modify these methods if you wish.
     */
    public String toString() {
        return String.format("pos=(%d,%d), size=%d, level=%d"
                , this.xCoord, this.yCoord, this.size, this.level);
    }

    public void printBlock() {
        this.printBlockIndented(0);
    }

    private void printBlockIndented(int indentation) {
        String indent = "";
        for (int i=0; i<indentation; i++) {
            indent += "\t";
        }

        if (this.children.length == 0) {
            // it's a leaf. Print the color!
            String colorInfo = GameColors.colorToString(this.color) + ", ";
            System.out.println(indent + colorInfo + this);
        } else {
            System.out.println(indent + this);
            for (Block b : this.children)
                b.printBlockIndented(indentation + 1);
        }
    }

    private static void coloredPrint(String message, Color color) {
        System.out.print(GameColors.colorToANSIColor(color));
        System.out.print(message);
        System.out.print(GameColors.colorToANSIColor(Color.WHITE));
    }

    public static void main(String[] args) {
        Block.gen = new Random(4);


        Block blockDepth2 = new Block(0, 2); // Create a Block with level 0 and maxDepth 2
        blockDepth2.updateSizeAndPosition(16, 0, 0); // Update the size and position
        blockDepth2.printBlock(); // Print the Block tree


//        Block blockDepth3 = new Block(0,3); // Create a Block with level 0 and max depth 3
//        blockDepth3.updateSizeAndPosition(16, 0,0);

//        Block b1 = blockDepth3.getSelectedBlock(2, 15, 1);
//        b1.printBlock();
//
//        Block b2 = blockDepth3.getSelectedBlock(3,5,2);
//        b2.printBlock();

//        Block testRotate = new Block(0, 3);
//        testRotate.rotate(0);
    }
}