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

public class MainActivity extends JFrame{

	private JPanel mainPane;
	private JLabel imgLab;
	private ImageIcon img;
	private Mat matImg;
	private String resultPath = "result.jpg";
	private DetectManhole detectManhole;
	private Size imgSize = new Size();
	private Size setSize = new Size(800,500);
	private JPanel panel;
	private JButton btnNewButton;

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
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);
		imgLab = new JLabel("");
		imgLab.setSize(new Dimension(1000, 600));
		mainPane.add(imgLab, BorderLayout.CENTER);
		
		panel = new JPanel();
		mainPane.add(panel, BorderLayout.EAST);
		
		btnNewButton = new JButton("New button");
		JButton setImgBtn = new JButton("Load Picture");
		setImgBtn.setMinimumSize(new Dimension(91, 21));
		setImgBtn.setMaximumSize(new Dimension(91, 21));
		setImgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				img = null;
				SelectImg();
			}
		});
		panel.setLayout(new MigLayout("", "[97px]", "[53px][44px]"));
		panel.add(btnNewButton, "cell 0 1,grow");
		panel.add(setImgBtn, "cell 0 0,grow");
		
	}
	

	private void SelectImg(){
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("画像ファイル", "png","jpg","bmp","Jpeg","GIF"));
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			matImg = Imgcodecs.imread(file.getPath());
			Imgproc.resize(matImg, matImg, setSize);
			imgLab.setSize((int)setSize.width, (int)setSize.height);
			detectManhole = new DetectManhole(matImg,2);
			Imgcodecs.imwrite(resultPath, detectManhole.origin);
			img = new ImageIcon(resultPath);
			imgLab.setIcon(img);
		}
	}
}
