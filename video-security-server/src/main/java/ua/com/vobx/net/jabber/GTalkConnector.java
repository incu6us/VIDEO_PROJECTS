package ua.com.vobx.net.jabber;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

public class GTalkConnector {

	private String login = "ast1.reports@gmail.com";
	private String password = "HASp3qw4";
	private String receipient = "vyacheslavpryimak@gmail.com";

	private static ConnectionConfiguration connConfig;
	private static XMPPConnection connection;
	private ChatManager chatmanager;
	private Chat newChat;
	private MessageListener customMessageListner;
	private static Presence presence;

	private boolean sendNotifications = true;
	private String sendNotificationsPattern = "send alarm"; // send allarm
															// on|off

	public void connect() {
		connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
		connection = new XMPPConnection(connConfig);

		try {
			connection.connect();
			System.out.println("Connected to " + connection.getHost());
		} catch (XMPPException ex) {
			// ex.printStackTrace();
			System.out.println("Failed to connect to " + connection.getHost());
			try {
				Thread.sleep(5000);
				if(!connection.isConnected()){
					connect();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			connection.login(login, password);
			System.out.println("Logged in as " + connection.getUser());

			chatmanager = connection.getChatManager();

			chatmanager.addChatListener(new ChatManagerListener() {

				public void chatCreated(Chat arg0, boolean arg1) {
					arg0.addMessageListener(new CustomMessageListener());
				}
			});

			customMessageListner = new CustomMessageListener();
			newChat = chatmanager.createChat(receipient, customMessageListner);

			Thread ping = new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub

					presence = new Presence(Presence.Type.available);

					while (true) {
						try {
							if(!presence.isAvailable()){
								connection.sendPacket(presence);
								System.out.println("set status 'isAvail'");
							}
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch (Exception e) {
							e.printStackTrace();
							disconnect();
							connect();
						}
					}
				}
			});
			ping.setDaemon(true);
			ping.start();

		} catch (XMPPException ex) {
			// ex.printStackTrace();
			System.out.println("Failed to log in as " + connection.getUser());
		}
	}

	public void sendMessage(String message) {

		if (sendNotifications) {
			try {
				newChat.sendMessage(message);
				System.out.println("Message Sent...");
			} catch (XMPPException e) {
				System.out.println("Error Delivering block");
				disconnect();
				connect();
				try {
					newChat.sendMessage(message);
				} catch (XMPPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Message Sent...");
			}
		}
	}

	public void disconnect() {
		presence = new Presence(Presence.Type.unavailable);
		connection.disconnect();
	}

	class CustomMessageListener implements MessageListener {

		public void processMessage(Chat chat, Message message) {
			if (!message.getBody().equals(null)) {
				String from = message.getFrom();
				String body = message.getBody();
				System.out.println(String.format("Received message '%1$s' from %2$s", body, from));

				if (message.getBody().toLowerCase().trim().equals(sendNotificationsPattern + " on")) {
					sendNotifications = true;
				} else if (message.getBody().toLowerCase().trim().equals(sendNotificationsPattern + " off")) {
					sendNotifications = false;
				}
			}
		}

	}

}
