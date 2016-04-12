import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Yasu on 2016/01/18.
 */
public class DetectManhole {
    public Mat img;
    public Mat origin;
    private Mat filterMat = new Mat();
    private Point point = new Point(-1,-1);
    private int rep = 3;
    public int mode;

    //Mode : 0:Edge 1:Ellipse 2 Full
    //img : モノクロ   origin : カラー
    public DetectManhole(Mat inputImg,int mode){
    	Imgproc.cvtColor(inputImg,this.img, Imgproc.COLOR_RGB2GRAY);
    	this.origin = inputImg;
    	this.mode = mode;
    }
    
    public DetectManhole(Mat inputImg, Mat origin,int mode) {
        this.img = inputImg;
        this.origin = origin;
        this.mode = mode;
        Detection();
    }

	private void Detection() {
        if(mode >0) {
//      Cannyフィルタ
            Imgproc.Canny(img, img, 10, 60);
//      クロージング処理
//            Imgproc.morphologyEx(img, origin, MORPH_CLOSE, filterMat,point, rep);
            Imgproc.dilate(img,img,filterMat,point,rep);
            Imgproc.erode(img, img, filterMat, point, rep - 2);
            if(mode <2){
                origin =img;
            }
            ellipseDetect();
        }
        else{
//      Cannyフィルタ
            Imgproc.Canny(img, img, 10, 60);
//      クロージング処理
//            Imgproc.morphologyEx(img, origin, MORPH_CLOSE, filterMat,point, rep);
            Imgproc.dilate(img, img, filterMat, point, rep);
            Imgproc.erode(img, origin, filterMat, point, rep - 2);
       }
//       origin = img;
    }


    private void ellipseDetect() {

        Mat hierarchy = Mat.zeros(new Size(5, 5), CvType.CV_8UC1);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        //一番外側のみでOK
        Imgproc.findContours(img, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_L1);
        img = Mat.zeros(new Size(this.img.width(), this.img.height()), CvType.CV_8UC3);
        Scalar color = new Scalar(255, 255, 255);

        Imgproc.drawContours(img, contours, -1, color, 1);

        int i = 0;
        for (i = 0; i < contours.size(); i++) {
            Size count = contours.get(i).size();
            if (count.height < 80 || count.height > 400) {
                continue;
            }
            MatOfPoint ptmat = contours.get(i);
            color = new Scalar(255, 0, 0);
            MatOfPoint2f ptmat2 = new MatOfPoint2f(ptmat.toArray());
            RotatedRect rot = Imgproc.fitEllipse(ptmat2);
            Size size = rot.boundingRect().size();
            if(mode >1) {
                if (checkEllipse(size, rot)) {
                    Imgproc.circle(origin, rot.center, 5, color, -1);
                    color = new Scalar(0, 255, 0);
                    Imgproc.ellipse(origin, rot, color, 2);
                }
            }
            else{
                Imgproc.circle(img, rot.center, 5, color, -1);
                color = new Scalar(0, 255, 0);
                Imgproc.ellipse(img, rot, color, 2);
                origin = img;
            }
        }
    }

    boolean checkEllipse(Size size,RotatedRect rot){
        int thre1 = (((int)rot.center.x /50) -6)*1000;
        int thre2 = (((int)rot.center.x / 50) - 6) * 5000;
//        return (size.height - size.width>100 && size.height - size.width<200 && size.area()<20000);
        return ((rot.angle <= 10 || (rot.angle <=180 && rot.angle >=170)) && size.height>size.width && size.height < img.size().height*3/4);
    }
}

