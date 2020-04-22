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
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SimulationWindow window = new SimulationWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initialize() {
        frame = new JFrame();
        frame.setSize(1800, 700);
        frame.setTitle("Drone Simulator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // <editor-fold defaultstate="collapsed" desc="-----Buttons-----">
        /*
         * Stop\Resume
         */

        JButton stopBtn = new JButton("Start/Pause");
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (toogleStop) {
                    CPU.stopAllCPUS();
                } else {
                    CPU.resumeAllCPUS();
                }
                toogleStop = !toogleStop;
            }
        });
        stopBtn.setBounds(1300, 0, 170, 50);
        frame.getContentPane().add(stopBtn);
        /*
         * Speeds
         */

        JButton speedBtn1 = new JButton("speedUp");
        speedBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.speedUp();
            }
        });
        speedBtn1.setBounds(1300, 100, 100, 50);
        frame.getContentPane().add(speedBtn1);

        JButton speedBtn2 = new JButton("speedDown");
        speedBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.speedDown();
            }
        });
        speedBtn2.setBounds(1400, 100, 100, 50);
        frame.getContentPane().add(speedBtn2);

        /*
         * Spins
         */

        JButton spinBtn1 = new JButton("spin180");
        spinBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.spinBy(180);
            }
        });
        spinBtn1.setBounds(1300, 200, 100, 50);
        frame.getContentPane().add(spinBtn1);

        JButton spinBtn2 = new JButton("spin90");
        spinBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.spinBy(90);
            }
        });
        spinBtn2.setBounds(1400, 200, 100, 50);
        frame.getContentPane().add(spinBtn2);

        JButton spinBtn3 = new JButton("spin60");
        spinBtn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.spinBy(60);
            }
        });
        spinBtn3.setBounds(1500, 200, 100, 50);
        frame.getContentPane().add(spinBtn3);

        JButton spinBtn4 = new JButton("spin45");
        spinBtn4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.spinBy(60);
            }
        });
        spinBtn4.setBounds(1300, 300, 100, 50);
        frame.getContentPane().add(spinBtn4);

        JButton spinBtn5 = new JButton("spin30");
        spinBtn5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.spinBy(30);
            }
        });
        spinBtn5.setBounds(1400, 300, 100, 50);
        frame.getContentPane().add(spinBtn5);

        JButton spinBtn6 = new JButton("spin-30");
        spinBtn6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.spinBy(-30);
            }
        });
        spinBtn6.setBounds(1500, 300, 100, 50);
        frame.getContentPane().add(spinBtn6);

        JButton spinBtn7 = new JButton("spin-45");
        spinBtn7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.algo1.spinBy(-45);
            }
        });
        spinBtn7.setBounds(1600, 300, 100, 50);
        frame.getContentPane().add(spinBtn7);

        JButton spinBtn8 = new JButton("spin-60");
        spinBtn8.addActionListener(e -> SimulationWindow.algo1.spinBy(-60));
        spinBtn8.setBounds(1700, 300, 100, 50);
        frame.getContentPane().add(spinBtn8);

        /*
         * Toogle real map
         */

        JButton toogleMapBtn = new JButton("toogle Map");
        toogleMapBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationWindow.toogleRealMap = !SimulationWindow.toogleRealMap;
            }
        });
        toogleMapBtn.setBounds(1300, 400, 120, 50);
        frame.getContentPane().add(toogleMapBtn);

        /*
         * Toogle AI
         */

        JButton toogleAIBtn = new JButton("toogle AI");
        toogleAIBtn.addActionListener(e -> SimulationWindow.toogleAI = !SimulationWindow.toogleAI);
        toogleAIBtn.setBounds(1400, 400, 120, 50);
        frame.getContentPane().add(toogleAIBtn);

        /*
         * Info label
         */


        SimulationWindow.info_label = new JLabel();
        SimulationWindow.info_label.setBounds(1300, 500, 300, 200);
        frame.getContentPane().add(SimulationWindow.info_label);

        /*
         * Info label
         */


        info_label2 = new JLabel();
        info_label2.setBounds(1400, 450, 300, 200);
        frame.getContentPane().add(info_label2);

        // </editor-fold>

        main();
    }

    public void main() {
        final int map_num = 5;

        // Drone start point for each map
        Point[] startPoints = {
                new Point(100, 50),
                new Point(50, 60),
                new Point(73, 68),
                new Point(84, 73),
                new Point(92, 100),
                new Point(80, 40)
        };

        Map map = new Map("E:\\Repos\\DroneSimulator\\Maps\\p1" + map_num + ".png", startPoints[map_num - 1]);

        algo1 = new AutoAlgo1(map);


        Painter painter = new Painter(algo1);
        painter.setBounds(0, 0, 2000, 2000);
        frame.getContentPane().add(painter);

        CPU painterCPU = new CPU(200, "painter"); // 60 FPS painter
        painterCPU.addFunction(frame::repaint);
        painterCPU.play();

        algo1.play();

        // Move painter
        CPU updatesCPU = new CPU(60, "updates");
        updatesCPU.addFunction(SimulationWindow.algo1.drone::update);
        updatesCPU.play();

        // Infos  update
        CPU infoCPU = new CPU(6, "update_info");
        infoCPU.addFunction(this::updateInfo);
        infoCPU.play();

    }

    public void updateInfo(int deltaTime) {
        SimulationWindow.info_label.setText(algo1.drone.getInfoHTML());
        info_label2.setText("<html>" + SimulationWindow.algo1.counter + " <BR>isRisky:" + algo1.is_risky +
                "<BR>" + SimulationWindow.algo1.risky_dis + "</html>");

    }
}
