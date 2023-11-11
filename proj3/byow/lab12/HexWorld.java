package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final Random random = new Random(575);
    private static TETile[][] tiles = new TETile[WIDTH][HEIGHT];
    
    public static void addHexagon(int sideLength, int xPos, int yPos, TETile sample) {
        int tmpX = xPos, tmpY = yPos;
        int eachRow = sideLength;
        for (int i = 0; i < sideLength; i ++) {
            for (int j = 0; j < eachRow; j ++) {
                tiles[tmpX + j][tmpY] = sample;
            }
            eachRow += 2;
            tmpX -= 1;
            tmpY += 1;
        }
        
        for (int i = 0; i < sideLength; i ++) {
            tmpX += 1;
            eachRow -= 2;
            for (int j = 0; j < eachRow; j ++) {
                tiles[tmpX + j][tmpY] = sample;
            }
            tmpY += 1;
        }
    }
    
    public static void fillAll() {
        for (int i = 0; i < HEIGHT; i ++) {
            for (int j = 0; j < WIDTH; j ++) {
                tiles[j][i] = Tileset.NOTHING;
            }
        }
    }
    
    private static TETile specifiedTile() {
        return Tileset.FLOWER;
    }
    
    private static TETile randomTile() {
        int tileNum = random.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.WATER;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.SAND;
            case 4: return TETile.colorVariant(Tileset.MOUNTAIN, 100, 100, 100, random);
            default: return Tileset.NOTHING;
        }
    }

    public static void generateGraph() {
        addHexagon(3, WIDTH / 2 - 2, 2, randomTile());
        
        addHexagon(3, WIDTH / 2 - 7, 5, randomTile());
        addHexagon(3, WIDTH / 2 + 3, 5, randomTile());
        
        addHexagon(3, WIDTH / 2 - 12, 8, randomTile());
        addHexagon(3, WIDTH / 2 - 2, 8, randomTile());
        addHexagon(3, WIDTH / 2 + 8, 8, randomTile());

        addHexagon(3, WIDTH / 2 - 7, 11, randomTile());
        addHexagon(3, WIDTH / 2 + 3, 11, randomTile());

        addHexagon(3, WIDTH / 2 - 12, 14, randomTile());
        addHexagon(3, WIDTH / 2 - 2, 14, randomTile());
        addHexagon(3, WIDTH / 2 + 8, 14, randomTile());

        addHexagon(3, WIDTH / 2 - 7, 17, randomTile());
        addHexagon(3, WIDTH / 2 + 3, 17, randomTile());

        addHexagon(3, WIDTH / 2 - 12, 20, randomTile());
        addHexagon(3, WIDTH / 2 - 2, 20, randomTile());
        addHexagon(3, WIDTH / 2 + 8, 20, randomTile());

        addHexagon(3, WIDTH / 2 - 7, 23, randomTile());
        addHexagon(3, WIDTH / 2 + 3, 23, randomTile());

        addHexagon(3, WIDTH / 2 - 2, 26, randomTile());
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        
        fillAll();
        generateGraph();
        ter.renderFrame(tiles);
    }
}
