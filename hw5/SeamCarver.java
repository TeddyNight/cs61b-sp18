import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    /**
     * @return current picture
     */
    public Picture picture() {
        return new Picture(picture);
    }

    /**
     * @return width of current picture
     */
    public int width() {
        return picture.width();
    }

    /**
     * @return height of current picture
     */
    public int height() {
        return picture().height();
    }

    private boolean isValidPixel(int x, int y) {
        return x >= 0 && y >= 0 && x < width() && y < height();
    }

    /**
     * @param x column
     * @param y row
     * @return energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        if (!isValidPixel(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        return xGradient(x, y) + yGradient(x, y);
    }

    private Color getColor(int x, int y) {
        if (x == -1) {
            x = width() - 1;
        } else if (x == width()) {
            x = 0;
        }
        if (y == -1) {
            y = height() - 1;
        } else if (y == height()) {
            y = 0;
        }
        return picture.get(x, y);
    }

    private double yGradient(int x, int y) {
        Color up = getColor(x, y - 1);
        Color down = getColor(x, y + 1);
        return Math.pow(up.getRed() - down.getRed(), 2)
                + Math.pow(up.getBlue() - down.getBlue(), 2)
                + Math.pow(up.getGreen() - down.getGreen(), 2);
    }

    private double xGradient(int x, int y) {
        Color left = getColor(x - 1, y);
        Color right = getColor(x + 1, y);
        return Math.pow(left.getRed() - right.getRed(), 2)
                + Math.pow(left.getBlue() - right.getBlue(), 2)
                + Math.pow(left.getGreen() - right.getGreen(), 2);
    }

    private int minimumPath(double[][] M, int x, int y) {
        int minX = x;
        double min = M[x][y - 1];
        if (x - 1 >= 0 && M[x - 1][y - 1] < min) {
            minX = x - 1;
            min = M[x - 1][y - 1];
        }
        if (x + 1 < width() && M[x + 1][y - 1] < min) {
            minX = x + 1;
            min = M[x + 1][y - 1];
        }
        return minX;
    }

    /**
     * @return sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        int width = width();
        int height = height();
        double[][] M = new double[width][height];
        int[][] edgeTo = new int[width][height];
        for (int i = 0; i < width; i++) {
            M[i][0] = energy(i, 0);
        }
        for (int j = 1; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int path = minimumPath(M, i, j);
                edgeTo[i][j] = path;
                M[i][j] = energy(i, j) + M[path][j - 1];
            }
        }
        int[] seam = new int[height];
        for (int i = 0; i < width; i++) {
            if (M[i][height - 1] <= M[seam[height - 1]][height - 1]) {
                seam[height - 1] = i;
            }
        }
        for (int i = height - 2; i >= 0; i--) {
            seam[i] = edgeTo[seam[i + 1]][i + 1];
        }
        return seam;
    }

    /**
     * @return sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        int height = height();
        int width = width();
        Picture old = picture;
        picture = new Picture(height, width);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture.set(j, i, old.get(i, j));
            }
        }
        int[] seam = findVerticalSeam();
        picture = old;
        return seam;
    }

    private boolean isValidSeam(int[] seam) {
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i - 1] - seam[i]) != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * remove horizontal seam from picture
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width() || isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    /**
     * remove vertical seam from picture
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height() || isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        picture = SeamRemover.removeVerticalSeam(picture, seam);
    }
}
