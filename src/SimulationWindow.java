import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationWindow {

    public static JLabel info_label;
    public static boolean toogleRealMap = true;
    public static boolean toogleAI;
    public static AutoAlgo1 algo1;
    public JLabel info_label2;
    boolean toogleStop = true;
    private JFrame frame;
    public SimulationWindow() {
        this.initialize();
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final SimulationWindow window = new SimulationWindow();
                    window.frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initialize() {
        this.frame = new JFrame();
        this.frame.setSize(1800, 700);
        this.frame.setTitle("Drone Simulator");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.getContentPane().setLayout(null);



        /*
         * Stop\Resume
         */

        final JButton stopBtn = new JButton("Start/Pause");
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (SimulationWindow.this.toogleStop) {
                    CPU.stopAllCPUS();
                } else {
                    CPU.resumeAllCPUS();
                }
                SimulationWindow.this.toogleStop = !SimulationWindow.this.toogleStop;
            }
        });
        stopBtn.setBounds(1300, 0, 170, 50);
        this.frame.getContentPane().add(stopBtn);
        /*
         * Speeds
         */


        final JButton speedBtn1 = new JButton("speedUp");
        speedBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.speedUp();
            }
        });
        speedBtn1.setBounds(1300, 100, 100, 50);
        this.frame.getContentPane().add(speedBtn1);

        final JButton speedBtn2 = new JButton("speedDown");
        speedBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.speedDown();
            }
        });
        speedBtn2.setBounds(1400, 100, 100, 50);
        this.frame.getContentPane().add(speedBtn2);

        /*
         * Spins
         */

        final JButton spinBtn1 = new JButton("spin180");
        spinBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(180);
            }
        });
        spinBtn1.setBounds(1300, 200, 100, 50);
        this.frame.getContentPane().add(spinBtn1);

        final JButton spinBtn2 = new JButton("spin90");
        spinBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(90);
            }
        });
        spinBtn2.setBounds(1400, 200, 100, 50);
        this.frame.getContentPane().add(spinBtn2);

        final JButton spinBtn3 = new JButton("spin60");
        spinBtn3.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(60);
            }
        });
        spinBtn3.setBounds(1500, 200, 100, 50);
        this.frame.getContentPane().add(spinBtn3);

        final JButton spinBtn4 = new JButton("spin45");
        spinBtn4.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(60);
            }
        });
        spinBtn4.setBounds(1300, 300, 100, 50);
        this.frame.getContentPane().add(spinBtn4);

        final JButton spinBtn5 = new JButton("spin30");
        spinBtn5.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(30);
            }
        });
        spinBtn5.setBounds(1400, 300, 100, 50);
        this.frame.getContentPane().add(spinBtn5);

        final JButton spinBtn6 = new JButton("spin-30");
        spinBtn6.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(-30);
            }
        });
        spinBtn6.setBounds(1500, 300, 100, 50);
        this.frame.getContentPane().add(spinBtn6);

        final JButton spinBtn7 = new JButton("spin-45");
        spinBtn7.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(-45);
            }
        });
        spinBtn7.setBounds(1600, 300, 100, 50);
        this.frame.getContentPane().add(spinBtn7);

        final JButton spinBtn8 = new JButton("spin-60");
        spinBtn8.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.algo1.spinBy(-60);
            }
        });
        spinBtn8.setBounds(1700, 300, 100, 50);
        this.frame.getContentPane().add(spinBtn8);

        /*
         * Toogle real map
         */

        final JButton toogleMapBtn = new JButton("toogle Map");
        toogleMapBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.toogleRealMap = !SimulationWindow.toogleRealMap;
            }
        });
        toogleMapBtn.setBounds(1300, 400, 120, 50);
        this.frame.getContentPane().add(toogleMapBtn);

        /*
         * Toogle AI
         */

        final JButton toogleAIBtn = new JButton("toogle AI");
        toogleAIBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                SimulationWindow.toogleAI = !SimulationWindow.toogleAI;
            }
        });
        toogleAIBtn.setBounds(1400, 400, 120, 50);
        this.frame.getContentPane().add(toogleAIBtn);

        /*
         * Info label
         */


        SimulationWindow.info_label = new JLabel();
        SimulationWindow.info_label.setBounds(1300, 500, 300, 200);
        this.frame.getContentPane().add(SimulationWindow.info_label);

        /*
         * Info label
         */


        this.info_label2 = new JLabel();
        this.info_label2.setBounds(1400, 450, 300, 200);
        this.frame.getContentPane().add(this.info_label2);

        this.main();
    }

    public void main() {
        final int map_num = 4;
        final Point[] startPoints = {
                new Point(100, 50),
                new Point(50, 60),
                new Point(73, 68),
                new Point(84, 73),
                new Point(92, 100)};

        final Map map = new Map("E:\\Repos\\DroneSimulator\\Maps\\p1" + map_num + ".png", startPoints[map_num - 1]);

        SimulationWindow.algo1 = new AutoAlgo1(map);

        final Painter painter = new Painter(SimulationWindow.algo1);
        painter.setBounds(0, 0, 2000, 2000);
        this.frame.getContentPane().add(painter);

        final CPU painterCPU = new CPU(200, "painter"); // 60 FPS painter
        painterCPU.addFunction(this.frame::repaint);
        painterCPU.play();

        SimulationWindow.algo1.play();

        final CPU updatesCPU = new CPU(60, "updates");
        updatesCPU.addFunction(SimulationWindow.algo1.drone::update);
        updatesCPU.play();

        final CPU infoCPU = new CPU(6, "update_info");
        infoCPU.addFunction(this::updateInfo);
        infoCPU.play();
    }

    public void updateInfo(final int deltaTime) {
        SimulationWindow.info_label.setText(SimulationWindow.algo1.drone.getInfoHTML());
        this.info_label2.setText("<html>" + SimulationWindow.algo1.counter + " <BR>isRisky:" + SimulationWindow.algo1.is_risky +
                "<BR>" + SimulationWindow.algo1.risky_dis + "</html>");

    }
}
