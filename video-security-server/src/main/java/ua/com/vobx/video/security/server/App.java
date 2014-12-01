package ua.com.vobx.video.security.server;

import java.io.IOException;

import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.security.BasicAuthentication;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.webapp.WebAppContext;

import ua.com.vobx.video.WebCamMotionDetection;
import ua.com.vobx.video.WebCamPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamStreamer;
import com.github.sarxos.webcam.ds.vlcj.VlcjDriver;

public class App {

	static {
		Webcam.setDriver(new VlcjDriver());
	}

	public static void main(String[] args) throws Exception {

		Webcam webcam = Webcam.getWebcams().get(0);

		/*
		 * Webcam Panel
		 */
		new WebCamPanel(webcam);

		/*
		 * Motion detector
		 */
		new WebCamMotionDetection(webcam);

		/*
		 * Webcam Streaming
		 */
		WebcamStreamer webCamStreamer = new WebcamStreamer(5555, webcam, 10, true);
		webCamStreamer.start();

		/*
		 * jetty
		 */

		Server jetty = new Server(8080);

		WebAppContext rootContext = new WebAppContext("src/main/webapp", "/");
		rootContext.setSecurityHandler(basicAuth("incu6us", "666666", "Privet!"));
		jetty.setHandler(rootContext);

		jetty.start();
		jetty.join();
	}

	// HTTP security
	private static final SecurityHandler basicAuth(String username, String password, String realm) {
		HashLoginService l = new HashLoginService();
		l.putUser(username, Credential.getCredential(password), new String[] { "user" });
		l.setName(realm);
		Constraint constraint = new Constraint();
		constraint.setName(Constraint.__BASIC_AUTH);
		constraint.setRoles(new String[] { "user" });
		constraint.setAuthenticate(true);
		ConstraintMapping cm = new ConstraintMapping();
		cm.setConstraint(constraint);
		cm.setPathSpec("/*");
		ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
		csh.setAuthenticator(new BasicAuthenticator());
		csh.setRealmName("myrealm");
		csh.addConstraintMapping(cm);
		csh.setLoginService(l);
		
		return csh;
	}

}
