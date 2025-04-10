import java.util.Arrays;

public class BoardConfig {
    public final int dimensionCount;
    public final int sideLength;
    public final int blockLength;
    public final int[] dimensions;

    public BoardConfig(int dimensionCount, int blockLength) {
        this.dimensionCount = dimensionCount;
        this.sideLength = (int) Math.pow(blockLength, dimensionCount);
        this.blockLength = blockLength;
        dimensions = new int[dimensionCount];
        Arrays.fill(dimensions, sideLength);
    }
}