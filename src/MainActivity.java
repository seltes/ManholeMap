import java.awt.BorderLayout;
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
import org.opencv.imgcodecs.Imgcodecs;

public class MainActivity extends JFrame{

	private JPanel mainPane;
	private JLabel imgLab;
	private ImageIcon img;
	private Mat matImg;
	private String resultPath = "/ManholeMap/Img/result.jpg";
	private DetectManhole detectManhole;
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
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 840, 460);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);
		JButton setImgBtn = new JButton("Load Picture");
		imgLab = new JLabel("");
		mainPane.add(imgLab, BorderLayout.CENTER);
		setImgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				img = null;
				SelectImg();
			}
		});
		mainPane.add(setImgBtn, BorderLayout.EAST);
		
	}
	

	private void SelectImg(){
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("画像ファイル", "png","jpg","bmp","Jpeg","GIF"));
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			matImg = Imgcodecs.imread(file.getPath());
			detectManhole = new DetectManhole(matImg,0);
			Imgcodecs.imwrite(resultPath, detectManhole.origin);
			img = new ImageIcon(resultPath);
			imgLab.setIcon(img);
		}
	}
}
