// Rachael Metzger, Josh Steinbach
// CS 335
// Project 4
// Due 12/7
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageObj extends JLabel {
    private float tempBright = 0, brightScale;
    public BufferedImage bim = null;
    public BufferedImage filteredbim = null;
    private boolean showfiltered=false, usedBrightness = false;

    public ImageObj(BufferedImage img) {
        bim = img;
        filteredbim = img;
    }

    public void setImage(BufferedImage img) {
        if (img == null) return;
        bim = img;
        filteredbim = img;
    }

    public void showImage() {
        if (bim == null) return;
        else if (showfiltered == true) return;
        else{
            // showfiltered = false
        }
        //showfiltered=false;
        this.repaint();
    }

    public BufferedImage get_orig(){
        return bim;
    }

    public BufferedImage get_chng(){
        return filteredbim;
    }

    public void brightenImg(int brightness){
        showfiltered = true;
        usedBrightness = true;
        BufferedImage result = new BufferedImage(bim.getWidth(), bim.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < bim.getHeight(); j++){
            for (int i = 0; i < bim.getWidth(); i++){

                int color = bim.getRGB(j,i);
                Color col = new Color(color);
                col.getRed();
                col.getBlue();
                col.getGreen();

                int b = col.getBlue() + brightness;
                int g = col.getGreen() + brightness;
                int r = col.getRed() + brightness;

                r = clamp(r, 0, 255);
                g = clamp(g, 0, 255);
                b = clamp(b, 0, 255);
                Color newcol = new Color(r,g,b);
                newcol.getRGB();

                result.setRGB(j,i,newcol.getRGB());

            }
        }
        filteredbim = result;

    }

    public void fade(BufferedImage start, BufferedImage end, int total_steps, int current_step){
        int color_start0 = start.getRGB(0,0);
        Color col_start0 = new Color(color_start0);
        BufferedImage result = new BufferedImage(bim.getWidth(), bim.getHeight(),BufferedImage.TYPE_INT_RGB);

        if (total_steps == current_step){
            for (int j = 0; j < bim.getHeight(); j++) {
                for (int i = 0; i < bim.getWidth(); i++) {
                    int color = end.getRGB(j,i);
                    Color col = new Color(color);
                    col.getRed();
                    col.getBlue();
                    col.getGreen();

                    int b = col.getBlue();
                    int g = col.getGreen();
                    int r = col.getRed();

                    r = clamp(r, 0, 255);
                    g = clamp(g, 0, 255);
                    b = clamp(b, 0, 255);
                    Color newcol = new Color(r,g,b);
                    newcol.getRGB();

                    result.setRGB(j,i,newcol.getRGB());
                }
            }
        }
        else {
            for (int j = 0; j < bim.getHeight(); j++) {
                for (int i = 0; i < bim.getWidth(); i++) {

                    int color_start = start.getRGB(j, i);
                    Color col_start = new Color(color_start);

                    int color_end = end.getRGB(j, i);
                    Color col_end = new Color(color_end);

                    float r_diff = (col_end.getRed() - col_start.getRed()) / total_steps;
                    float g_diff = (col_end.getGreen() - col_start.getGreen()) / total_steps;
                    float b_diff = (col_end.getBlue() - col_start.getBlue()) / total_steps;

                    float r_add = current_step * r_diff + (float) 0.5;
                    float g_add = current_step * g_diff + (float) 0.5;
                    float b_add = current_step * b_diff + (float) 0.5;

                    int b = col_start.getBlue() + (int) b_add;
                    int g = col_start.getGreen() + (int) g_add;
                    int r = col_start.getRed() + (int) r_add;

                    r = clamp(r, 0, 255);
                    g = clamp(g, 0, 255);
                    b = clamp(b, 0, 255);
                    Color newcol = new Color(r, g, b);
                    newcol.getRGB();

                    result.setRGB(j, i, newcol.getRGB());

                }
            }
        }

        filteredbim = result;
    }

     public static int clamp( int val, int min, int max){
        if (val < min)
            return min;
        if (val > max)
            return max;
        return val;
     }

    public void paintComponent(Graphics g) {
        Graphics2D big = (Graphics2D) g;
        if (showfiltered)
            big.drawImage(filteredbim, 0, 0, this);
        else
            big.drawImage(bim, 0, 0, this);
    }


}
