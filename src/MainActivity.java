import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.miginfocom.swing.MigLayout;

public class MainActivity extends JFrame {

	private JPanel mainPane;
	private JLabel imgLab;
	private ImageIcon img;
	private String originPath = "origin.png";
	private String resultPath = "result.png";
	private Mat matImg;
	private Mat grayImg;
	private DetectManhole detectManhole;
	private Size setSize = new Size(800.0, 500.0);
	private JPanel panel;
	private JButton edgeBtn;
	private JButton ellipseBtn;
	private JButton manholeBtn;
	private JButton btnOrigin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainActivity frame = new MainActivity();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainActivity() {
		setPreferredSize(new Dimension(1000, 700));
		setSize(new Dimension(921, 522));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 840, 460);
		img = new ImageIcon();
		initialize_Display();

	}

	private void SelectImg() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("画像ファイル", "png", "jpg", "bmp", "Jpeg", "GIF"));
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			matImg = Imgcodecs.imread(file.getPath());
			grayImg = new Mat();
			Imgproc.cvtColor(matImg, grayImg, Imgproc.COLOR_BGR2GRAY);
			outputImg(-1);
		}
	}
	private void outputImg(int mode) {
		if (mode >= 0) {
			detectManhole = new DetectManhole(grayImg, mode);
			Imgcodecs.imwrite(resultPath, detectManhole.origin);
			img = new ImageIcon(resultPath);
		}else {
			Imgcodecs.imwrite(originPath, matImg);
			img = new ImageIcon(originPath);
		}
		if (img == null) {
			System.out.println("Can't Load Image!!");
		}
	}

	private void initialize_Display() {
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);
		imgLab = new JLabel("");
		imgLab.setSize(new Dimension((int) setSize.width, (int) setSize.height));
		mainPane.add(imgLab, BorderLayout.CENTER);

		panel = new JPanel();
		mainPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new MigLayout("", "[97px]", "[53px][44px][][][][][][][][][]"));
		JButton setImgBtn = new JButton("Load Picture");
		setImgBtn.setPreferredSize(new Dimension(100, 40));
		setImgBtn.setMinimumSize(new Dimension(100, 40));
		setImgBtn.setMaximumSize(new Dimension(100, 40));
		panel.add(setImgBtn, "cell 0 3,grow");
		
		btnOrigin = new JButton("Origin");
		btnOrigin.setMinimumSize(new Dimension(100, 40));
		btnOrigin.setMaximumSize(new Dimension(100, 40));
		panel.add(btnOrigin, "cell 0 7");

		edgeBtn = new JButton("Edge");
		edgeBtn.setMinimumSize(new Dimension(100, 40));
		edgeBtn.setMaximumSize(new Dimension(100, 40));
		edgeBtn.setPreferredSize(new Dimension(100, 40));
		panel.add(edgeBtn, "cell 0 8,grow");

		ellipseBtn = new JButton("Ellipse");
		ellipseBtn.setPreferredSize(new Dimension(100, 40));
		ellipseBtn.setMinimumSize(new Dimension(100, 40));
		ellipseBtn.setMaximumSize(new Dimension(100, 40));
		panel.add(ellipseBtn, "cell 0 9");

		manholeBtn = new JButton("Manhole");
		manholeBtn.setPreferredSize(new Dimension(100, 40));
		manholeBtn.setMinimumSize(new Dimension(100, 40));
		manholeBtn.setMaximumSize(new Dimension(100, 40));
		panel.add(manholeBtn, "cell 0 10");

		setImgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imgLab.removeAll();;
				SelectImg();
				imgLab.setIcon(img);
			}
		});

		btnOrigin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputImg(-1);
				imgLab.setIcon(img);
			}
		});
		
		edgeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputImg(-1);
				outputImg(0);
				imgLab.setIcon(img);
			}
		});

		ellipseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputImg(-1);
				outputImg(1);
				imgLab.setIcon(img);
			}
		});

		manholeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputImg(-1);
				outputImg(2);
				imgLab.setIcon(img);
			}
		});
	}
}
