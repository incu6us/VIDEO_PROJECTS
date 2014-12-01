package ua.com.vobx.video;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class WebCamPanel {

	public WebCamPanel(Webcam webcam){
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		JFrame window = new JFrame("Webcam Panel");
		window.add(panel);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
}
