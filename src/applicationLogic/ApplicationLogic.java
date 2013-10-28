package applicationLogic;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import communicationLogic.CommunicationLogic;

public class ApplicationLogic {
	static Logger logger = Logger.getLogger(ApplicationLogic.class);
	CommunicationLogic communicationLogic = null;

	/**
	 * Constructor of ApplicationLogic. Instantiate the communication logic
	 */
	public ApplicationLogic() {
		communicationLogic = new CommunicationLogic();
	}

	/**
	 * Tries to establish a TCP-connection to the echo server based on the given
	 * server address and the port number of the echo service.
	 * 
	 * @param remote_ip
	 *            IP address of the remote server
	 * @param remote_port
	 *            Port of the remote server
	 */
	public void connect(String remote_ip, int remote_port) {
		if (!communicationLogic.isConnected()) {
			communicationLogic.connect(remote_ip, remote_port);
			if(communicationLogic.isConnected()){
				communicationLogic.receive();
			}
		} else {
			System.out
					.println("You are already connected, if you want to reconnect, please disconnect first");
		}
		return;
	}

	/**
	 * Tears down the active connection to the server and exits the program
	 * execution.
	 */
	public void quit() {
		communicationLogic.disconnect();
		System.out.println("EchoClient> Application exit!");
		logger.info("Client quit");
		return;
	}
	
	/**
	 * Sets the logger to the specified log level
	 * @param loglevel
	 */
	public void logLevel(String loglevel) {
		if(loglevel.equals("ALL")){
			logger.setLevel(Level.ALL);
			System.out.println("EchoClient> current log status: ALL");
		}
		else if(loglevel.equals("DEBUG")){
			logger.setLevel(Level.DEBUG);
			System.out.println("EchoClient> current log status: DEBUG");
		}
		else if(loglevel.equals("INFO")){
			logger.setLevel(Level.INFO);
			System.out.println("EchoClient> current log status: INFO");
		}
		else if(loglevel.equals("WARN")){
			logger.setLevel(Level.WARN);
			System.out.println("EchoClient> current log status: WARN");
		}
		else if(loglevel.equals("ERROR")){
			logger.setLevel(Level.ERROR);
			System.out.println("EchoClient> current log status: ERROR");
		}
		else if(loglevel.equals("FATAL")){
			logger.setLevel(Level.FATAL);
			System.out.println("EchoClient> current log status: FATAL");
		}
		else if(loglevel.equals("OFF")){
			logger.setLevel(Level.OFF);
			System.out.println("EchoClient> current log status: OFF");
		}
		else
			System.out.println("Your input level is not existed! ");
		return;
	}

	/**
	 * Sends text message to the echo server according to the communication
	 * protocol.
	 * 
	 * @param msg
	 *            Content of the message
	 */
	public void send(String msg) {
		communicationLogic.send(msg);
		return;
	}

	/**
	 * Tries to disconnect from the connected server.
	 */
	public void disconnect() {
		communicationLogic.disconnect();
		return;
	}

	/**
	 * Prints all the commands to the screen.
	 */
	public void help() {
		System.out.println(helpTxt);
		return;
	}

	static String helpTxt = "\n"
			+ "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"
			+ "command                   |   information\n"
			+ "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"
			+ "connect <address> <port>  |   Tries to establish a TCP-connection\n"
			+ "                          |   to the echo server based on the given\n"
			+ "                          |   server address and the port number of\n"
			+ "                          |   the echo service.\n"
			+ "--------------------------|------------------------------------------\n"
			+ "disconnect                |   Tries to disconnect from the connected\n"
			+ "                          |   server.\n"
			+ "--------------------------|------------------------------------------\n"
			+ "send <message>            |   Sends text message to the echo server \n"
			+ "                          |   according to the communication protocol\n"
			+ "--------------------------|------------------------------------------\n"
			+ "logLevel <level>          |   Sets the logger to the specified log \n"
			+ "                          |   level.\n"
			+ "--------------------------|------------------------------------------\n"
			+ "help                      |\n"
			+ "--------------------------|------------------------------------------\n"
			+ "quit                      |   Tears down the active connection to the\n"
			+ "                          |   server and exits the program execution.\n"
			+ "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";
}
