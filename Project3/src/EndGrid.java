// Rachael Metzger, Josh Stainbach
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class EndGrid extends JPanel {
    private int rows;
    private int columns;
    private int width = 500;
    private int height = 500;
    private int block_width;
    private int block_height;
    private int border = 10;
    private int point_size = 10;
    private EndPoint[][] endPoints;
    private Point[][] Point;
    private Grid grid;
    private boolean dragging = false;
    int[] point_being_dragged = new int[2];
    Color point_color = Color.black;
    Color clicked_color = Color.red;

    // Paint component --------------------------------------------------
    public void paint(Graphics g){
        super.paint(g);
        this.draw_main_lines(g, Color.black);
        this.draw_south_west_lines(g, Color.black);
        this.draw_points(g);
    }


    public EndGrid(int r, int c){
        this.setMaximumSize(new Dimension(width,height));
        this.setPreferredSize(new Dimension(width,height));
        this.rows = r;
        this.columns = c;
        endPoints = new EndPoint[rows][columns];
        this.initialize_points();
        this.point_being_dragged[0] = 0;
        this.point_being_dragged[1] = 0;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("a click");
                endPoints[point_being_dragged[0]][point_being_dragged[1]].set_color(point_color);
                point_being_dragged = point_clicked_on(e);

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging){
                    endPoints[point_being_dragged[0]][point_being_dragged[1]].change_location(e.getX(),e.getY());
                    repaint();
                    EndGrid.super.revalidate();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    public int[] point_clicked_on(MouseEvent e){
        int[] rval = new int[2];
        for (int i = 1; i < rows-1; i++){
            for (int j = 1; j < columns-1; j++ ){
                if(endPoints[i][j].is_being_clicked(e.getX(),e.getY(),point_size)){
                    //handle when clicked
                    endPoints[i][j].set_color(clicked_color);
                    //System.out.println("point: [" + i + "," + j + "] being clicked");
                    this.dragging = true;
                    rval[0] = i;
                    rval[1] = j;
                    repaint();
                    return rval;
                }
                else {
//                    public void set_EndPoint_color_on_click(int x,int y){
//                        endPoints[x][y].set_color(clicked_color);
//
//                    }

                }
            }
        }
        rval[0] = 0;
        rval[1] = 0;
        return rval;
    }
//    public void set_EndPoint_color_on_click( int x, int y){
//        endPoints[x][y].set_color(clicked_color);
//
//    }

    public void initialize_points(){
        block_width = (width-border)/columns;
        block_height = (height-border)/rows;
        int x;
        int y;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                x = border + j*block_width;
                y = border + i*block_height;
                endPoints[i][j] = new EndPoint(x,y);
                endPoints[i][j].set_color(point_color);
                if(i == 0 || i == rows - 1 || j == 0 || j == columns -1){
                    endPoints[i][j].set_moveable(false);
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
                current_x = endPoints[i][j].get_x();
                current_y = endPoints[i][j].get_y();

                //draw north line
                g.drawLine(current_x, current_y, endPoints[i+1][j].get_x(), endPoints[i+1][j].get_y());

                //draw west line
                g.drawLine(current_x, current_y, endPoints[i][j+1].get_x(), endPoints[i][j+1].get_y());

                //draw diag line
                g.drawLine(current_x, current_y, endPoints[i+1][j+1].get_x(), endPoints[i+1][j+1].get_y());
            }
        }
    }

    public void draw_south_west_lines(Graphics g, Color c){
        g.setColor(c);

        //draw east lines
        for(int i = 0; i < rows - 1; i++){
            g.drawLine(endPoints[i][columns-1].get_x(), endPoints[i][columns-1].get_y(), endPoints[i+1][columns-1].get_x(), endPoints[i+1][columns-1].get_y());
        }

        //draw west lines
        for(int j = 0; j < columns - 1; j++){
            g.drawLine(endPoints[rows-1][j].get_x(), endPoints[rows-1][j].get_y(), endPoints[rows-1][j+1].get_x(), endPoints[rows-1][j+1].get_y());
        }
    }

    public void draw_points(Graphics g){
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (endPoints[i][j].is_moveable()) {
                    g.setColor(endPoints[i][j].get_color());
                    g.fillRect(endPoints[i][j].get_x() - (point_size / 2), endPoints[i][j].get_y() - (point_size / 2), point_size, point_size);
                }
            }
        }
    }


}
