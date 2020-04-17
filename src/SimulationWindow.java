import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.*;

import javax.swing.*;

public class SimulationWindow {

	private JFrame frame;

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

	public SimulationWindow() {
		initialize();
	}
	public static int pixels;
	public static JLabel info_label;
	public static boolean return_home = false;
	boolean toogleStop = true;
	long elapsedTime = 0L;
	private void initialize() {
		frame = new JFrame();
		frame.setSize(1800,700);
		frame.setTitle("Drone Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		new java.util.Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				CPU.stopAllCPUS();
			}
		},1000*300);

		JButton stopBtn = new JButton("Start/Pause");
		stopBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(toogleStop) {
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
		 * Stop\Resume
		 */

		JButton pixelButton = new JButton("Get pixels");
		pixelButton.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
                    CPU.stopAllCPUS();
			  }
		});
		pixelButton.setBounds(1500, 0, 170, 50);
		frame.getContentPane().add(pixelButton);

		/*
		 * Toogle real map
		 */
		
		JButton toogleMapBtn = new JButton("toogle Map");
		toogleMapBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  toogleRealMap = !toogleRealMap;
			  }
		});
		toogleMapBtn.setBounds(1300, 400, 120, 50);
		frame.getContentPane().add(toogleMapBtn);
		
		/*
		 * Toogle AI
		 */
		
		JButton toogleAIBtn = new JButton("toogle AI");
		toogleAIBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  toogleAI = !toogleAI;
			  }
		});
		toogleAIBtn.setBounds(1400, 400, 120, 50);
		frame.getContentPane().add(toogleAIBtn);

		/*
		 * Info label 
		 */
		
		
		info_label = new JLabel();
		info_label.setBounds(1300, 500, 300, 200);
		frame.getContentPane().add(info_label);
		
		/*
		 * Info label 
		 */
		
		
		info_label2 = new JLabel();
		info_label2.setBounds(1400, 450, 300, 200);
		frame.getContentPane().add(info_label2);
		
		main();
	}
	public JLabel info_label2;
	public static boolean toogleRealMap = true;
	public static boolean toogleAI = false;
	
	public static AutoAlgo1 algo1;
	
	
	public void main() {
		int map_num = 4;
		Point[] startPoints = {
				new Point(100,50),
				new Point(50,60),
				new Point(73,68),
				new Point(84,73),
				new Point(92,100)};
		
		Map map = new Map("./Maps/90-right.png",startPoints[map_num-1]);
		
		algo1 = new AutoAlgo1(map);
		
		Painter painter = new Painter(algo1);
		painter.setBounds(0, 0, 2000, 2000);
		frame.getContentPane().add(painter);
		
		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(frame::repaint);
		painterCPU.play();
		
		algo1.play();
		
		CPU updatesCPU = new CPU(60,"updates");
		updatesCPU.addFunction(algo1.drone::update);
		updatesCPU.play();
		
		CPU infoCPU = new CPU(6,"update_info");
		infoCPU.addFunction(this::updateInfo);
		infoCPU.play();
	}
	
	public void updateInfo(int deltaTime) {
		info_label.setText(algo1.drone.getInfoHTML());
		info_label2.setText("<html>" + algo1.counter + " <BR>isRisky:" + algo1.is_risky +
				"<BR>" + algo1.risky_dis + "<BR> paintExplored:  " + algo1.paintExplored + "" +
				"<BR> paintBlocked:" + algo1.paintBlocked + "<BR> paintUnexplored:" + algo1.paintUnexplored +
				"<BR> paintVisited:" + algo1.paintVisited + "<BR> time:" + elapsedTime + "</html>");

	}

	public void stopCPUS() {
		CPU.stopAllCPUS();
	}
	
	public void resumseCPUS() {
		CPU.stopAllCPUS();
	}
}
