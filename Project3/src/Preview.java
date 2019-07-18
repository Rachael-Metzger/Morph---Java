// Rachael Metzger, Josh Steinbach
// CS 335
// Project 4
// Due 12/7
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Preview extends JPanel {
    Timer tick_tock;
    private Grid previewGrid;
    private int total_steps, current_step = 0;
    private int size;
    private Point[][] start, end, fresh;
    private JButton StartButton;

    public Preview(int gridSize, int numFrames, int numSeconds, Point[][] s, Point[][] e){
        total_steps = numFrames*numSeconds;
        size = gridSize;
        start = new Point[gridSize][gridSize];
        end = new Point[gridSize][gridSize];
        fresh = new Point[gridSize][gridSize];
        for (int i = 0; i < size; i ++){
            for (int j = 0; j < size; j ++){
                start[i][j] = new Point(s[i][j].get_x(), s[i][j].get_y());
                end[i][j] = new Point(e[i][j].get_x(), e[i][j].get_y());
                fresh[i][j] = new Point(s[i][j].get_x(), s[i][j].get_y());
                if(i == 0 || i == size - 1 || j == 0 || j == size -1){
                    start[i][j].set_moveable(false);
                    end[i][j].set_moveable(false);
                    fresh[i][j].set_moveable(false);
                }
            }
        }
        previewGrid = new Grid(gridSize, gridSize);
        previewGrid.set_points(fresh);
        previewGrid.set_all_colors(Color.black); //needs to be dynamic, just dont know how I want to implement it yet !!!!!!!!!!!!!!!!!!!!!!!!
        tick_tock = new javax.swing.Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try
                {
                    Thread.sleep(750/numFrames);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                if (current_step == total_steps){
                    tick_tock.stop();

                }
                else{
                    //System.out.println("on step: " + current_step);
                    current_step++;
                    next_step();
                    repaint();
                    revalidate();
                }
            }
        });
        tick_tock.start();
        this.add(previewGrid, BorderLayout.NORTH);
    }

    public void next_step(){
        for(int i = 1 ; i <size; i++){
            for(int j = 1 ; j < size; j++){
                float how_close = ((float)current_step/(float)total_steps);
                int x_difference = end[i][j].get_x() - start[i][j].get_x();
                int y_difference = end[i][j].get_y() - start[i][j].get_y();
                int updated_x = start[i][j].get_x() + (int)(how_close*x_difference);
                int updated_y = start[i][j].get_y() + (int)(how_close*y_difference);
//                if(end[i][j].get_x() - start[i][j].get_x() != 0 || end[i][j].get_y() - start[i][j].get_y() != 0){
//                    System.out.println(updated_x);
//                }

                //System.out.println(x_difference + " " + y_difference);
                previewGrid.points[i][j].change_location(updated_x, updated_y);
            }
        }
    }



}
