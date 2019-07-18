// Rachael Metzger, Josh Steinbach
// CS 335
// Project 4
// Due 12/7
// Morph
// ---------------------------------------------
// test
// The code below is an image
// morphing interface.
// In this design, there will be two panels
// that will show a 20x20 triangle grid that
// have a movable point on the inner most
// corner of each grid space. There will be
// will be two buttons to select the left and
// right panel, a morph button, and a preview
// morph button. We will also implement a
// JSliders that will allow the user to adjust
// the grid size, brightness, animation speed,
// and FPS. The Morph button will transition
// colors of the pixels to the final image
// but we have not found a solution to how
// warp the images themselves. we also struggled
// implementing the boundaries to each point.
// ---------------------------------------------
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Morph extends JFrame {
    // Container
    private Container c;
    // JPanel
    private JPanel settingsPanel, morph1Panel, morph2Panel;
    // JButton
    private JButton LeftImage, RightImage, StartMorph, PreviewMorph;
    // JLabel
    private JLabel gridSizeLabel, FPSLabel, animationTimeLabel, BrightLabel;
    // JSlider
    private JSlider GridSlider, FPSSlider, animationTimeSlider, BrightSlider;
    // JMenuBar
    private JMenuBar bar;
    // JMenu
    private JMenu fileMenu;
    // JMenuItem
    private JMenuItem fileOpen, fileExit, fileReset;
    // ints
    private int gridSizeVal = 5, FPSVal = 15, animationTimeVal = 5, BrightVal = 0;
    // Grid Class
    private Grid leftGrid, rightGrid;
    // BufferedImage
    private BufferedImage Leftimage,Rightimage,image,LeftCopy, RightCopy;
    // ImageObj Class
    private ImageObj leftView, rightView;
    // image button toggle
    private boolean leftButton = true, rightButton = false;

    private int[] left_point_being_dragged = new int[2];
    private int[] right_point_being_dragged = new int[2];
    private Color point_color = Color.black;  // need to find out how to link these between classes
    private Color clicked_color = Color.red;


    public Morph(){
        // starting points for clicking and dragging ---------------
        left_point_being_dragged[0] = 0;
        left_point_being_dragged[1] = 0;
        right_point_being_dragged[0] = 0;
        right_point_being_dragged[1] = 0;

        // Allocate panels -----------------------------------------
        c = getContentPane();
        settingsPanel = new JPanel();
        morph1Panel = new JPanel();
        morph2Panel = new JPanel();

        // Set Panel Sizes -----------------------------------------
        settingsPanel.setPreferredSize(new Dimension(1000,200));
        morph1Panel.setPreferredSize(new Dimension(500,500));
        morph2Panel.setPreferredSize(new Dimension(500,500));

        // Grid Layout ---------------------------------------------
        GridLayout grid = new GridLayout(3,6);
        settingsPanel.setLayout(grid);

        // JMenu file -------------------------------------------------
        final JFileChooser fileChooser = new JFileChooser(".");
        bar = new JMenuBar();
        this.setJMenuBar(bar);
        fileMenu = new JMenu("File");
        fileOpen = new JMenuItem("Open");
        fileExit = new JMenuItem("Exit");
        fileReset = new JMenuItem("Reset");
        fileOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVAl = fileChooser.showOpenDialog(Morph.this);
                if (returnVAl == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    try {
                        image = ImageIO.read(file);
                    } catch (IOException e1) {
                    }
                }

                if (leftButton == true){
                    Leftimage = image;
                    leftGrid.load_image(Leftimage);
                    repaint();
                    validate();
                }
                if (rightButton == true){
                    Rightimage = image;
                    rightGrid.load_image(Rightimage);
                    repaint();
                    validate();

                }
            }
        });

        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        fileReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // resets the grid as it originally looked
                LeftImage.setBackground(null);
                RightImage.setBackground(null);
                Leftimage = null;
                Rightimage = null;
                left_point_being_dragged[0] = 0;
                left_point_being_dragged[1] = 0;
                right_point_being_dragged[0] = 0;
                right_point_being_dragged[1] = 0;
                GridSlider.setValue(10);
                gridSizeLabel.setText("Grid Points: 10x10");
                FPSVal = 15;
                FPSLabel.setText("FPS: " + FPSVal);
                FPSSlider.setValue(15);
                animationTimeVal = 5;
                animationTimeSlider.setValue(5);
                animationTimeLabel.setText("Animation Time: " + animationTimeVal);
                BrightVal = 0;
                BrightLabel.setText("Brightness: " + BrightVal);
                BrightSlider.setValue(0);

                leftGrid.removeAll();
                c.remove(leftGrid);
                leftGrid = new Grid(gridSizeVal, gridSizeVal);
                add_left_action_handler();
                c.add(leftGrid, BorderLayout.WEST);
                repaint();
                validate();
                // Repaint Right Panel
                rightGrid.removeAll();
                c.remove(rightGrid);
                rightGrid = new Grid(gridSizeVal, gridSizeVal);
                add_right_action_handler();
                c.add(rightGrid, BorderLayout.EAST);
                repaint();
                validate();
            }
        });
        fileMenu.add(fileOpen);
        fileMenu.add(fileReset);
        fileMenu.add(fileExit);
        bar.add(fileMenu);

        // Settings Panel Setup ---------------------------------------
        // JButtons----------------------------------------------------
        LeftImage = new JButton("Left Morph Image");
        RightImage = new JButton("Right Morph Image");
        StartMorph = new JButton("Start Morph");
        PreviewMorph = new JButton("Preview Morph");
        LeftImage.setBackground(Color.lightGray);
        LeftImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // select left panel to insert picture
                // highlight button when clicked on
                leftButton = true;
                if (leftButton == true) {
                    LeftImage.setBackground(Color.lightGray);
                    RightImage.setBackground(null);
                    rightButton = false;
                }
            }
        });
        RightImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // select right panel to insert picture
                // highlight button when clicked on
                rightButton = true;
                if (rightButton == true) {
                    LeftImage.setBackground(null);
                    RightImage.setBackground(Color.lightGray);
                    leftButton = false;
                }

            }
        });
        StartMorph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // start morph
                if (leftGrid.has_an_image() && rightGrid.has_an_image()){
                    JFrame morphWindow = new JFrame("Morph");
                    JPanel m_panel = new MorphWindow(gridSizeVal, FPSVal, animationTimeVal, leftGrid.points, rightGrid.points, leftGrid.image_o, rightGrid.image_o); //really need to add speed variable here     !!!!!!!!!!!!!!!!!!!!!!!!!!!!1
                    morphWindow.add(m_panel);
                    morphWindow.setDefaultCloseOperation(morphWindow.DISPOSE_ON_CLOSE);
                    morphWindow.setPreferredSize(new Dimension(550,550));
                    morphWindow.setVisible(true);
                    morphWindow.pack();
                }
                else{
                    System.out.println("Images need to be set for right and left frame!!!");
                }

            }
        });
        PreviewMorph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // preview morph image
                JFrame previewMorph = new JFrame("Preview Morph");
                JPanel prev_panel = new Preview(gridSizeVal, FPSVal, animationTimeVal, leftGrid.points, rightGrid.points); //really need to add speed variable here     !!!!!!!!!!!!!!!!!!!!!!!!!!!!1
                previewMorph.add(prev_panel);
                previewMorph.setDefaultCloseOperation(previewMorph.DISPOSE_ON_CLOSE);
                previewMorph.setPreferredSize(new Dimension(550,550));
                previewMorph.setVisible(true);
                previewMorph.pack();

            }
        });
        // JLabel------------------------------------------------------
        gridSizeLabel = new JLabel("Grid Points: " + (gridSizeVal) + "x" + (gridSizeVal));
        FPSLabel = new JLabel("FPS: " + FPSVal);
        animationTimeLabel = new JLabel("Animation Time: " + animationTimeVal);
        BrightLabel = new JLabel("Brightness: " + BrightVal);

        // JSlider-----------------------------------------------------
        GridSlider = new JSlider(SwingConstants.HORIZONTAL,5,20,gridSizeVal );
        GridSlider.setMajorTickSpacing(3);
        GridSlider.setPaintTicks(true);
        GridSlider.setPaintLabels(true);
        GridSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                gridSizeVal = GridSlider.getValue() + 2;
                gridSizeLabel.setText("Grid Points: " + (gridSizeVal - 2) + "x" + (gridSizeVal - 2));
                // Repaint Left panel

                if (leftGrid.has_an_image()){
                    ImageObj the_ugly_ass_left_image = leftGrid.get_the_effing_imag_object();
                    leftGrid.removeAll();
                    c.remove(leftGrid);
                    leftGrid = new Grid(gridSizeVal, gridSizeVal);
                    leftGrid.set_the_fucking_image_object(the_ugly_ass_left_image);
                }
                else{
                    leftGrid.removeAll();
                    c.remove(leftGrid);
                    leftGrid = new Grid(gridSizeVal, gridSizeVal);
                }

                add_left_action_handler();
                c.add(leftGrid, BorderLayout.WEST);
                repaint();
                validate();
                // Repaint Right Panel

                if (rightGrid.has_an_image()){
                    ImageObj the_ugly_ass_right_image = rightGrid.get_the_effing_imag_object();
                    rightGrid.removeAll();
                    c.remove(rightGrid);
                    rightGrid = new Grid(gridSizeVal, gridSizeVal);
                    rightGrid.set_the_fucking_image_object(the_ugly_ass_right_image);
                }
                else{
                    rightGrid.removeAll();
                    c.remove(rightGrid);
                    rightGrid = new Grid(gridSizeVal, gridSizeVal);
                }

                add_right_action_handler();
                c.add(rightGrid, BorderLayout.EAST);
                repaint();
                validate();
            }
        });
        FPSSlider = new JSlider(SwingConstants.HORIZONTAL, 5,30, FPSVal);
        FPSSlider.setMajorTickSpacing(5);
        FPSSlider.setMinorTickSpacing(1);
        FPSSlider.setPaintTicks(true);
        FPSSlider.setPaintLabels(true);
        FPSSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                FPSVal = FPSSlider.getValue();
                FPSLabel.setText("FPS: " + FPSVal);
            }
        });

        animationTimeSlider = new JSlider(SwingConstants.HORIZONTAL, 1,10,5);
        animationTimeSlider.setMajorTickSpacing(2);
        animationTimeSlider.setMinorTickSpacing(1);
        animationTimeSlider.setPaintLabels(true);
        animationTimeSlider.setPaintTicks(true);
        animationTimeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                animationTimeVal = animationTimeSlider.getValue();
                animationTimeLabel.setText("Animation Time: " + animationTimeVal);
            }
        });

        BrightSlider = new JSlider(SwingConstants.HORIZONTAL, -255,255, 0);
        BrightSlider.setMajorTickSpacing(102);
        BrightSlider.setPaintLabels(true);
        BrightSlider.setPaintTicks(true);
        BrightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                BrightVal = BrightSlider.getValue();
                BrightLabel.setText("Brightness: " + BrightVal);

                // Repaint Left panel
                if (leftButton == true) {
                    leftGrid.image_o.brightenImg(BrightVal);
                    leftGrid.repaint();
                    repaint();
                    validate();
                }
                if (rightButton == true) {
                    // Repaint Right Panel
                    rightGrid.image_o.brightenImg(BrightVal);
                    repaint();
                    validate();
                }
            }
        });

        // add to settingsPanel----------------------------------------
        settingsPanel.add(LeftImage);
        settingsPanel.add(RightImage);
        settingsPanel.add(StartMorph);
        settingsPanel.add(PreviewMorph);
        gridSizeLabel.setHorizontalAlignment(JLabel.CENTER);
        settingsPanel.add(gridSizeLabel);
        settingsPanel.add(GridSlider);
        FPSLabel.setHorizontalAlignment(JLabel.CENTER);
        settingsPanel.add(FPSLabel);
        settingsPanel.add(FPSSlider);
        animationTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        settingsPanel.add(animationTimeLabel);
        settingsPanel.add(animationTimeSlider);
        BrightLabel.setHorizontalAlignment(JLabel.CENTER);
        settingsPanel.add(BrightLabel);
        settingsPanel.add(BrightSlider);

        // add grids to morph panels ----------------------------------
        gridSizeVal = gridSizeVal+2;
        leftGrid = new Grid(gridSizeVal, gridSizeVal);
        leftGrid.set_dimensions(new Dimension(550,500));
        rightGrid = new Grid(gridSizeVal, gridSizeVal);
        rightGrid.set_dimensions(new Dimension(500,500));

        // add action handlers to 2 grids ----------------------------
        add_left_action_handler();
        add_right_action_handler();

        // add to Container--------------------------------------------
        c.add(leftGrid, BorderLayout.WEST);  // fixed that here, seems to work fine
        c.add(rightGrid, BorderLayout.EAST);
        c.add(settingsPanel, BorderLayout.SOUTH);

        // Container settings------------------------------------------
        this.setName("Morph");
        this.setSize(1000,650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
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

    public void add_left_action_handler(){
        leftGrid.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) {
                leftGrid.points[left_point_being_dragged[0]][left_point_being_dragged[1]].set_color(point_color);
                rightGrid.points[right_point_being_dragged[0]][right_point_being_dragged[1]].set_color(point_color);
                rightGrid.points[left_point_being_dragged[0]][left_point_being_dragged[1]].set_color(point_color);
                leftGrid.points[right_point_being_dragged[0]][right_point_being_dragged[1]].set_color(point_color);
                left_point_being_dragged = leftGrid.point_clicked_on(e);
                rightGrid.points[left_point_being_dragged[0]][left_point_being_dragged[1]].set_color(clicked_color);
                leftGrid.repaint();
                rightGrid.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        leftGrid.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (left_point_being_dragged[0] != 0) {
                    int x_pos = e.getX();
                    int y_pos = e.getY();
                    if (x_pos>leftGrid.points[gridSizeVal-1][gridSizeVal-1].get_x()){
                        x_pos = leftGrid.points[gridSizeVal-1][gridSizeVal-1].get_x();
                    }
                    else if (x_pos<leftGrid.border){
                        x_pos = leftGrid.border;
                    }
                    if (y_pos>leftGrid.points[gridSizeVal-1][gridSizeVal-1].get_y()){
                        y_pos = leftGrid.points[gridSizeVal-1][gridSizeVal-1].get_y();
                    }
                    else if (y_pos<leftGrid.border){
                        y_pos = leftGrid.border;
                    }

                    leftGrid.points[left_point_being_dragged[0]][left_point_being_dragged[1]].change_location(x_pos, y_pos);
                }
                leftGrid.repaint();
                revalidate();
            }

            @Override
            public void mouseMoved(MouseEvent e) { }
        });
    }

    public void add_right_action_handler(){
        rightGrid.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) {
                rightGrid.points[right_point_being_dragged[0]][right_point_being_dragged[1]].set_color(point_color);
                leftGrid.points[left_point_being_dragged[0]][left_point_being_dragged[1]].set_color(point_color);
                leftGrid.points[right_point_being_dragged[0]][right_point_being_dragged[1]].set_color(point_color);
                rightGrid.points[left_point_being_dragged[0]][left_point_being_dragged[1]].set_color(point_color);
                right_point_being_dragged = rightGrid.point_clicked_on(e);
                leftGrid.points[right_point_being_dragged[0]][right_point_being_dragged[1]].set_color(clicked_color);
                rightGrid.repaint();
                leftGrid.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        rightGrid.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(right_point_being_dragged[0] != 0) {
                    int x_pos = e.getX();
                    int y_pos = e.getY();
                    if (x_pos>rightGrid.points[gridSizeVal-1][gridSizeVal-1].get_x()){
                        x_pos = rightGrid.points[gridSizeVal-1][gridSizeVal-1].get_x();
                    }
                    else if (x_pos<rightGrid.border){
                        x_pos = rightGrid.border;
                    }
                    if (y_pos>rightGrid.points[gridSizeVal-1][gridSizeVal-1].get_y()){
                        y_pos = rightGrid.points[gridSizeVal-1][gridSizeVal-1].get_y();
                    }
                    else if (y_pos<rightGrid.border){
                        y_pos = rightGrid.border;
                    }
                    rightGrid.points[right_point_being_dragged[0]][right_point_being_dragged[1]].change_location(x_pos, y_pos);
                }
                rightGrid.repaint();
                revalidate();
            }

            @Override
            public void mouseMoved(MouseEvent e) { }
        });
    }

    // Window ---------------------------------------------------------
    public static void main(String args[]){
        Morph M = new Morph();
        M.pack();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
