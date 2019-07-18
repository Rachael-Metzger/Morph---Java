// Rachael Metzger, Josh Steinbach
// CS 335
// Project 4
// Due 12/7
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Grid extends JPanel {
    private int rows;
    private int columns;
    private int width = 500;
    private int height = 500;
    private int block_width;
    private int block_height;
    public int border = 0;
    private int point_size = 10;
    public Point[][] points;
    private boolean dragging = false;
    int[] point_being_dragged = new int[2];
    public ImageObj image_o;
    private boolean has_image = false;
    private boolean disp_new = false;
    Color line_color = Color.black;
    Color point_color = Color.black;
    Color clicked_color = Color.red;

    // Paint component --------------------------------------------------
    public void paint(Graphics g){
        super.paint(g);
        if (has_image){
            g.drawImage(image_o.get_chng(),0,0,null);
        }
        this.draw_main_lines(g, line_color);
        this.draw_south_west_lines(g, line_color);
        this.draw_points(g);
    }

    public ImageObj get_the_effing_imag_object(){
        return image_o;
    }

    public void set_the_fucking_image_object(ImageObj im){
        image_o = im;
        has_image = true;
    }

    public void set_filtered_image(BufferedImage b){
        image_o = new ImageObj(b);
        has_image = true;
    }

    public boolean has_an_image(){
        return has_image;
    }


    public Grid(int r, int c){
        this.setMaximumSize(new Dimension(width,height));
        this.setPreferredSize(new Dimension(width,height));
        this.rows = r;
        this.columns = c;
        points = new Point[rows][columns];
        this.initialize_points();
        this.point_being_dragged[0] = 0;
        this.point_being_dragged[1] = 0;
    }

    public void load_image(BufferedImage image){
        image_o = new ImageObj(readImage("src/res/" + image));
        image_o.setImage(image);
        image_o.showImage();
        has_image = true;
    }

    public int[] point_clicked_on(MouseEvent e){
        int[] rval = new int[2];
        for (int i = 1; i < rows-1; i++){
            for (int j = 1; j < columns-1; j++ ){
                if(points[i][j].is_being_clicked(e.getX(),e.getY(),point_size)){
                    //handle when clicked
//                    endGrid = new EndGrid(i,j);
                    points[i][j].set_color(clicked_color);
//                    endGrid.set_EndPoint_color_on_click (i,j);
                    //endPoints[i][j].set_color(clicked_color);
                    //System.out.println("point: [" + i + "," + j + "] being clicked");
                    this.dragging = true;
                    rval[0] = i;
                    rval[1] = j;
                    repaint();
                    return rval;
                }
            }
        }
        rval[0] = 0;
        rval[1] = 0;
        return rval;
    }

    public void set_Point_color_on_click(int x, int y){
        points[x][y].set_color(clicked_color);

    }

    public void set_all_colors(Color c){
        for(int i = 1 ; i < rows-1; i++){
            for(int j = 1 ; j < columns-1; j++){
                this.points[i][j].set_color(c);
            }
        }
    }

    public void set_points(Point[][] p){
        points = p;
    }

    public void initialize_points(){
        block_width = (width-border)/(columns-1);
        block_height = (height-border)/(rows-1);
        int x;
        int y;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                x = border + j*block_width;
                y = border + i*block_height;
                points[i][j] = new Point(x,y);
                points[i][j].set_color(point_color);
                if(i == 0 || i == rows - 1 || j == 0 || j == columns -1){
                    points[i][j].set_moveable(false);
                }
            }
        }
    }

    public void draw_main_lines(Graphics g, Color c){
        int current_x;
        int current_y;
        g.setColor(c);
        for(int i = 0 ; i < rows -1; i++){
            for(int j = 0 ; j < columns -1; j++){
                //get point coordinates
                current_x = points[i][j].get_x();
                current_y = points[i][j].get_y();

                //draw north line
                g.drawLine(current_x, current_y, points[i+1][j].get_x(), points[i+1][j].get_y());

                //draw west line
                g.drawLine(current_x, current_y, points[i][j+1].get_x(), points[i][j+1].get_y());

                //draw diag line
                g.drawLine(current_x, current_y, points[i+1][j+1].get_x(), points[i+1][j+1].get_y());
            }
        }
    }

    public void draw_south_west_lines(Graphics g, Color c){
        g.setColor(c);

        //draw east lines
        for(int i = 0; i < rows - 1; i++){
            g.drawLine(points[i][columns-1].get_x(), points[i][columns-1].get_y(), points[i+1][columns-1].get_x(), points[i+1][columns-1].get_y());
        }

        //draw west lines
        for(int j = 0; j < columns - 1; j++){
            g.drawLine(points[rows-1][j].get_x(), points[rows-1][j].get_y(), points[rows-1][j+1].get_x(), points[rows-1][j+1].get_y());
        }
    }

    public void draw_points(Graphics g){
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (points[i][j].is_moveable()) {
                    g.setColor(points[i][j].get_color());
                    g.fillRect(points[i][j].get_x() - (point_size / 2), points[i][j].get_y() - (point_size / 2), point_size, point_size);
                }
//                g.setColor(points[i][j].get_color());
//                g.fillRect(points[i][j].get_x() - (point_size / 2), points[i][j].get_y() - (point_size / 2), point_size, point_size);
            }
        }
    }

    public void set_dimensions(Dimension d){
        this.setMaximumSize(d);
        this.setPreferredSize(d);
        this.setMinimumSize(d);
    }

    public BufferedImage readImage(String file) {

        Image image = Toolkit.getDefaultToolkit().getImage(file);
        MediaTracker tracker = new MediaTracker(new Component() {
        });
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
        BufferedImage bim = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage(image, 0, 0, this);
        return bim;
    }


}
