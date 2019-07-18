// Rachael Metzger, Josh Steinbach
// CS 335
// Project 4
// Due 12/7
import java.awt.*;

public class Point {
    private int x_coord;
    private int y_coord;
    private boolean moveable = true;
    private Color c;

    public Point(){
        x_coord = 0;
        y_coord = 0;
        return;
    }

    public Point(int x, int y) {
        x_coord = x;
        y_coord = y;
    }

    public void set_moveable(boolean b){moveable = b;}

    public boolean is_moveable(){return moveable;}

    public int get_x(){return x_coord;}

    public int get_y(){return y_coord;}

    public void change_location(int x, int y){
        x_coord = x;
        y_coord = y;
    }

    public Color get_color() {return c;}

    public void set_color(Color col){
        this.c = col;
    }

    public boolean is_being_clicked(int x, int y, int point_size){
        int n = y_coord - (point_size/2);
        int s = y_coord + (point_size/2);
        int e = x_coord + (point_size/2);
        int w = x_coord - (point_size/2);
        if(x>w && x<e && y>n && y<s){
            return true;
        }
        return false;
    }
}