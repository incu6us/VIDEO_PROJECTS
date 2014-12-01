package ua.com.vobx.video;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ua.com.vobx.net.jabber.GTalkConnector;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

public class WebCamMotionDetection {

	public WebCamMotionDetection(Webcam webcam) {
		final GTalkConnector gTalk = new GTalkConnector();

		gTalk.connect();
		
		WebcamMotionDetector motionDetection = new WebcamMotionDetector(webcam, 10, 5, 1000);
		motionDetection.addMotionListener(new WebcamMotionListener() {
			
			public void motionDetected(WebcamMotionEvent wme) {
				// TODO Auto-generated method stub
				
				DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
				
				System.out.println("motion");
				gTalk.sendMessage(df.format(new Date().getTime())+" - Motion detected!!!");
			}
		});
		motionDetection.start();
	}
}
