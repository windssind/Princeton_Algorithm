import edu.princeton.cs.algs4.Picture;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class SeamCarver {

    // create a seam carver object based on the given picture
    private Picture picture;
    private int cur_RGB[][]; // store cur_picture color infomation as long as its height and length

    /**
     * accept a picture , and caculate its energy and store it in pixel_energy
     *
     * @param picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = picture;

        int height = picture.height();
        int width = picture.width();

        // 更新cur_RGB表
        cur_RGB = new int[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                cur_RGB[i][j] = picture.getRGB(j, i);
            }
        }
    }

    // current picture
    public Picture picture() {
        int cur_height = cur_RGB.length;
        int cur_width = cur_RGB[0].length;
        Picture cur_picture = new Picture(cur_width, cur_height);
        for (int i = 0; i < cur_height; ++i) {
            for (int j = 0; j < cur_width; ++j) {
                cur_picture.setRGB(j, i, cur_RGB[i][j]);
            }
        }
        return cur_picture;
    }

    // width of current picture
    public int width() {
        return cur_RGB[0].length;
    }

    // height of current picture
    public int height() {
        return cur_RGB.length;
    }

    // energy of pixel at column x and row y

    /**
     * @param x col
     * @param y row
     * @return
     */
    public double energy(int x, int y) {
        int height = cur_RGB.length;
        int width = cur_RGB[0].length;
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException();
        if (y == 0 || y == height - 1 || x == 0 || x == width - 1) return 1000;
        int left = cur_RGB[y][x - 1];
        int right = cur_RGB[y][x + 1];
        int upper = cur_RGB[y - 1][x];
        int lower = cur_RGB[y + 1][x];
        return gradient(left, right, upper, lower);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double pixel_energy[][] = new double[height()][width()]; // no need to caculate it again
        double distanceTo[][] = new double[height()][width()];
        update_Energy(pixel_energy);
        // 初始化distanceTo数组
        for (int i = 0; i < height(); ++i) {
            distanceTo[i][0] = 0;
        }
        for (int i = 0; i < height(); ++i) {
            for (int j = 1; j < width(); ++j) {
                distanceTo[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int j = 1; j < width(); ++j) {
            for (int i = 0; i < height(); ++i) {
                if (i - 1 >= 0) // 左上角
                    relax(pixel_energy, distanceTo, i - 1, j - 1, i, j);
                // 左边
                relax(pixel_energy, distanceTo, i, j - 1, i, j);
                // 左下角
                if (i + 1 < height())
                    relax(pixel_energy, distanceTo, i + 1, j - 1, i, j);
            }
        }

        // 在最后一列中找到最小的距离
        int min_row = -1;
        double min_value = Double.MAX_VALUE;
        for (int j = 0; j < height(); ++j) {
            if (distanceTo[j][width() - 1] < min_value) {
                min_row = j;
                min_value = distanceTo[j][width() - 1];
            }
        }

        int[] seam = new int[width()];
        seam[width() - 1] = min_row;
        // 构建返回的seam
        for (int j = width() - 2; j >= 0; --j) {
            int minRow = seam[j + 1]; // 先假定中间的行最小
            if (seam[j + 1] - 1 >= 0 && distanceTo[seam[j + 1] - 1][j] < distanceTo[seam[j
                    + 1]][j]) {
                minRow = seam[j + 1] - 1;
            }
            if (seam[j + 1] + 1 < height()
                    && distanceTo[seam[j + 1] + 1][j] < distanceTo[minRow][j]) {
                minRow = seam[j + 1] + 1;
            }
            seam[j] = minRow;
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double pixel_energy[][] = new double[height()][width()]; // no need to caculate it again
        double distanceTo[][] = new double[height()][width()];
        update_Energy(pixel_energy);
        // 初始化distanceTo数组
        for (int j = 0; j < width(); ++j) {
            distanceTo[0][j] = 0;
        }
        for (int i = 1; i < height(); ++i) {
            for (int j = 0; j < width(); ++j) {
                distanceTo[i][j] = Double.MAX_VALUE;
            }
        }

        for (int i = 1; i < height(); ++i) {
            for (int j = 0; j < width(); ++j) {
                if (j - 1 >= 0) // 左上角
                    relax(pixel_energy, distanceTo, i - 1, j - 1, i, j);
                // 上面
                relax(pixel_energy, distanceTo, i - 1, j, i, j);
                if (j + 1 < width())
                    relax(pixel_energy, distanceTo, i - 1, j + 1, i, j);
            }
        }

        // 在最后一行中找到最小的距离
        int min_col = -1;
        double min_value = Double.MAX_VALUE;
        for (int j = 0; j < width(); ++j) {
            if (distanceTo[height() - 1][j] < min_value) {
                min_col = j;
                min_value = distanceTo[height() - 1][j];
            }
        }

        int seam[] = new int[height()];
        seam[height() - 1] = min_col;

        // 构建返回的seam , 利用性质， 到达某个点的最短路径的上一个必然也是三个选项中最小的，因为最后一步大家都是公平竞争
        for (int i = height() - 2; i >= 0; --i) {
            int minCol = seam[i + 1]; // 先假定中间的行最小
            if (seam[i + 1] - 1 >= 0 && distanceTo[i][seam[i + 1] - 1] < distanceTo[i][seam[i
                    + 1]]) {
                minCol = seam[i + 1] - 1;
            }
            if (seam[i + 1] + 1 < width()
                    && distanceTo[i][seam[i + 1] + 1] < distanceTo[i][minCol]) {
                minCol = seam[i + 1] + 1;
            }
            seam[i] = minCol;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validate(seam, false);
        update_cur_RGB(seam, false);
    }

    /**
     * store new color infomation in cur_RGB
     *
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        validate(seam, true);
        update_cur_RGB(seam, true);
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

    /**
     * given four RGB color , return its energy
     *
     * @return
     */
    private double gradient(int color_x_l, int color_x_r, int color_y_u, int color_y_d) {
        int R_x_l = (color_x_l & 0X00FF0000) >> 16;
        int G_x_l = (color_x_l & 0X0000FF00) >> 8;
        int B_x_l = color_x_l & 0X000000FF;

        int R_x_r = (color_x_r & 0X00FF0000) >> 16;
        int G_x_r = (color_x_r & 0X0000FF00) >> 8;
        int B_x_r = color_x_r & 0X000000FF;

        int R_y_u = (color_y_u & 0X00FF0000) >> 16;
        int G_y_u = (color_y_u & 0X0000FF00) >> 8;
        int B_y_u = color_y_u & 0X000000FF;

        int R_y_d = (color_y_d & 0X00FF0000) >> 16;
        int G_y_d = (color_y_d & 0X0000FF00) >> 8;
        int B_y_d = color_y_d & 0X000000FF;

        return Math.sqrt(square_difference(R_x_l, R_x_r) + square_difference(G_x_l, G_x_r)
                                 + square_difference(B_x_l, B_x_r) + square_difference(R_y_u, R_y_d)
                                 + square_difference(G_y_d, G_y_u) + square_difference(B_y_d,
                                                                                       B_y_u));
    }

    private double square_difference(int num_1, int num_2) {
        return Math.pow((double) num_1 - (double) num_2, 2);
    }

    // 从左往右标号

    /**
     * relax a vertex
     *
     * @param pre
     * @param cur
     */
    private void relax(double pixel_energy[][], double distanceTo[][], int pre_row, int pre_col,
                       int cur_row, int cur_col) {
        if (distanceTo[pre_row][pre_col] + pixel_energy[cur_row][cur_col]
                < distanceTo[cur_row][cur_col]) {
            distanceTo[cur_row][cur_col] = distanceTo[pre_row][pre_col]
                    + pixel_energy[cur_row][cur_col];
        }
    }


    private void update_cur_RGB(int seam[], boolean vertical) {
        int new_cur_RGB[][];
        if (vertical) {
            int height = cur_RGB.length;
            int width = cur_RGB[0].length - 1;
            new_cur_RGB = new int[height][width];
            for (int i = 0; i < height; ++i) {
                for (int j = 0, ptr = 0; ptr < width; ++j) {
                    if (j == seam[i]) continue;
                    new_cur_RGB[i][ptr] = cur_RGB[i][j];
                    ++ptr;
                }
            }
        }
        else {
            int height = cur_RGB.length - 1;
            int width = cur_RGB[0].length;
            new_cur_RGB = new int[height][width]; // 这是一个非常消耗时间的操作
            for (int j = 0; j < width; ++j) {
                for (int i = 0, ptr = 0; ptr < height; ++i) {
                    if (i == seam[j]) continue;
                    new_cur_RGB[ptr][j] = cur_RGB[i][j];
                    ++ptr;
                }
            }
        }
        cur_RGB = new_cur_RGB;
    }

    private void update_Energy(double pixel_energy[][]) { // no need to caculate it again) {
        int height = cur_RGB.length;
        int width = cur_RGB[0].length;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                pixel_energy[i][j] = energy(j, i);
            }
        }
    }

    /**
     * call by remove
     */
    private void validate(int seam[], boolean vertical) {
        if (seam == null) throw new IllegalArgumentException();
        if (vertical && (seam.length != cur_RGB.length || cur_RGB[0].length <= 1))
            throw new IllegalArgumentException();
        else if ((!vertical) && (seam.length != cur_RGB[0].length || cur_RGB.length <= 1))
            throw new IllegalArgumentException();
        int len = seam.length;
        for (int i = 0; i < len; ++i) {
            if (vertical && (seam[i] < 0 || seam[i] >= cur_RGB[0].length || (i >= 1
                    && Math.abs(seam[i] - seam[i - 1]) > 1)))
                throw new IllegalArgumentException();
            else if ((!vertical) && (seam[i] < 0 || seam[i] >= cur_RGB.length || (i >= 1
                    && Math.abs(seam[i] - seam[i - 1]) > 1)))
                throw new IllegalArgumentException();
        }
    }

}
