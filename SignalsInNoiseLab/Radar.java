import java.util.Random;

/**
 * The model for radar scan and accumulator
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class Radar
{
    
    // stores whether each cell triggered detection for the current scan of the radar
    private boolean[][] currentScan;
    private boolean[][] prevScan;
    
    // value of each cell is incremented for each scan in which that cell triggers detection 
    private int[][] accumulator;
    
    private int dx,dy;
    
    private int rows, cols;
    
    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;

    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;
    
    // number of scans of the radar since construction
    private int numScans;

    /**
     * Constructor for objects of class Radar
     * 
     */
    public Radar(int dx, int dy)
    {
        // initialize instance variables
        currentScan = new boolean[100][100]; // elements will be set to false
        prevScan = new boolean[100][100]; // elements will be set to false
        accumulator = new int[11][11]; // elements will be set to 0
        
        this.rows = 100;
        this.cols = 100;
        
        this.dx = dx;
        this.dy = dy;
        
        // randomly set the location of the monster (can be explicity set through the
        //  setMonsterLocation method
        monsterLocationRow = (int)(Math.random() * 100);
        monsterLocationCol = (int)(Math.random() * 100);
        
        numScans= 0;
    }
    
    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scan()
    {
        // copies current scan into previous scan
        for (int row = 0; row < currentScan.length; row++)
        {
            for (int col = 0; col < currentScan[0].length; col++)
            {
                prevScan[row][col] = currentScan[row][col];
            }
        }
        
        // zero the current scan grid
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                currentScan[row][col] = false;
            }
        }
        
        // detect the monster
        setMonsterLocation();
        
        // inject noise into the grid
        injectNoise();
        
        for (int row = 0; row < currentScan.length; row++)
        {
            for (int col = 0; col < currentScan[0].length; col++)
            {
                if (prevScan[row][col] == true)
                {
                    for (int curRow = 0; curRow < currentScan.length; curRow++)
                    {
                        for (int curCol = 0; curCol < currentScan[0].length; curCol++)
                        {
                            if (currentScan[curRow][curCol] == true)
                            {
                                if ((Math.abs(curRow - row) <= 5) && (Math.abs(curCol - col) <= 5))
                                {
                                    int x = curRow - row;
                                    int y = curCol - col;
                                    
                                    accumulator[x + 5][y + 5] += 1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the location of the monster
     * 
     * @param   row     the row in which the monster is located
     * @param   col     the column in which the monster is located
     * @pre row and col must be within the bounds of the radar grid
     */
    public void setMonsterLocation()
    {
        // remember the row and col of the monster's location
        monsterLocationRow += dx;
        monsterLocationCol += dy;
        
        if (monsterLocationRow >= 99)
        {
            monsterLocationRow -= 99;
        }
        else if (monsterLocationRow <= 0)
        {
            monsterLocationRow += 99;
        }
        else if (monsterLocationCol >= 99)
        {
            monsterLocationCol -= 99;
        }
        else if (monsterLocationCol <= 0)
        {
            monsterLocationCol += 99;
        }
        
        currentScan[monsterLocationRow][monsterLocationCol] = true;
    }
    
     /**
     * Sets the probability that a given cell will generate a false detection
     * 
     * @param   fraction    the probability that a given cell will generate a flase detection expressed
     *                      as a fraction (must be >= 0 and < 1)
     */
    public void setNoiseFraction(double fraction)
    {
        noiseFraction = fraction;
    }
    
    /**
     * Returns true if the specified location in the radar grid triggered a detection.
     * 
     * @param   row     the row of the location to query for detection
     * @param   col     the column of the location to query for detection
     * @return true if the specified location in the radar grid triggered a detection
     */
    public boolean isDetected(int row, int col)
    {
        return currentScan[row][col];
    }
    
    /**
     * Returns the number of times that the specified location in the radar grid has triggered a
     *  detection since the constructor of the radar object.
     * 
     * @param   row     the row of the location to query for accumulated detections
     * @param   col     the column of the location to query for accumulated detections
     * @return the number of times that the specified location in the radar grid has
     *          triggered a detection since the constructor of the radar object
     */
    public int getAccumulatedDetection(int row, int col)
    {
        return accumulator[row][col];
    }
    
    /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getNumRows()
    {
        return currentScan.length;
    }
    
    /**
     * Returns the number of columns in the radar grid
     * 
     * @return the number of columns in the radar grid
     */
    public int getNumCols()
    {
        return currentScan[0].length;
    }
    
    /**
     * Returns the number of scans that have been performed since the radar object was constructed
     * 
     * @return the number of scans that have been performed since the radar object was constructed
     */
    public int getNumScans()
    {
        return numScans;
    }
    
    public int[] getDxDy()
    {
        int max = 0;
        int[] dxdy = new int[2];
        
        for (int row = 0; row < accumulator.length; row++)
        {
            for (int col = 0; col < accumulator[0].length; col++)
            {
                if (accumulator[row][col] > max)
                {
                    max = accumulator[row][col];
                    
                    dxdy[0] = row - 5;
                    dxdy[1] = col - 5;
                }
            }
        }
        
        return dxdy;
    }
    
    /**
     * Sets cells as falsely triggering detection based on the specified probability
     * 
     */
    private void injectNoise()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                // each cell has the specified probablily of being a false positive
                if(Math.random() < noiseFraction)
                {
                    currentScan[row][col] = true;
                }
            }
        }
    }
    
}
