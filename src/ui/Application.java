package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import applicationLogic.ApplicationLogic;

public class Application {
	static Logger logger = Logger.getLogger(Application.class);
	static BufferedReader sysReader;
	static String log4jProp = "src/log4j.properties";

	/**
	 * Initialize logger
	 */
	static void initLog4j() {
		// Initiate log4j
		// Set log file directory
		//System.out.println(System.getProperty("user.dir"));
		System.setProperty("WORKDIR", System.getProperty("user.dir"));//!!!
		
		// Load log4j configuration
		File log4jPropFile = new File(log4jProp);
		if (log4jPropFile.exists()) {
			// Use property file to configure
			PropertyConfigurator.configure(log4jProp);
		} else {
			// Use BasicConfigurator to configure
			System.err
					.println("*** "
							+ log4jProp
							+ " file not found, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		}
	}

	/**
	 * Entrance of the application
	 * 
	 * @param args
	 *            parameters from command line
	 */
	public static void main(String[] args) {//Èë¿Ú
		initLog4j();
		logger.info("Client launched successfully");
		

		// Application logic
		ApplicationLogic applicationLogic = new ApplicationLogic();

		sysReader = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		while (true) {
			System.out.print("EchoClient> ");
			// Read from system input
			try {
				str = sysReader.readLine();
			} catch (IOException e) {
				System.out.println("Failed to read from system input");
				logger.error("Failed to read from system input");
			}
			if (!str.trim().equals("")) {
				// Get command
				String[] token = str.split(" ");
				String cmd = token[0];
				// Send
				if (cmd.equals("send")) {
					String msg = str.substring(4, str.length()).trim();
					if (!msg.equals("")) {
						applicationLogic.send(msg);
					}
				}
				// connect
				else if (cmd.equals("connect")) {
					int length = token.length;
					if (length == 3) {
						// Get remote server address and port
						String add = token[1];
						try {
							int port = Integer.parseInt(token[2]);
							String REMOTE_IP = add;
							int REMOTE_PORT = port;
							applicationLogic.connect(REMOTE_IP, REMOTE_PORT);
						} catch (NumberFormatException e) {
							System.out.println("Illigal port format");
							logger.warn("Illigal port format");
						}
					}
					// Wrong format
					else {
						System.out
								.println("Connect command should be as follow:\nconnect <address> <port>");
					}
				}
				// disconnect
				else if (cmd.equals("disconnect")) {
					applicationLogic.disconnect();
				}
				// logLevel
				else if (cmd.equals("logLevel")) {
					int length = token.length;
					if (length == 2) {
						// Get logLevel
						String logLevel = token[1];
						applicationLogic.logLevel(logLevel);
					}
					// Wrong format
					else {
						System.out
								.println("logLevel command should be as follow:\nlogLevel <Level> ");
					}
				}
				// help
				else if (cmd.equals("help")) {
					applicationLogic.help();
				}
				// quit
				else if (cmd.equals("quit")) {
					applicationLogic.quit();
					return;
				}
				// other inputs
				else {
					System.out
							.println("Incorrect command, please try again or input 'help'");
				}
			}
		}
	}
}
