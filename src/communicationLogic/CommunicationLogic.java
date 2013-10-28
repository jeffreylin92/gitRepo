package communicationLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import applicationLogic.ApplicationLogic;

public class CommunicationLogic {
	Logger logger = Logger.getLogger(ApplicationLogic.class);
	static Socket socket = null;
	static int REMOTE_PORT = 7777;
	static String REMOTE_IP = "127.0.0.1";
	static BufferedReader socketReader = null;
	static PrintWriter socketPrinter = null;
	static InputStream socketInputStream = null;
	static OutputStream socketOutputStream = null;
	static int BUF_SIZE = 10000;

	/**
	 * See if the communication is connected to a server.
	 * 
	 * @return true or false
	 */
	public boolean isConnected() {
		if (socket != null) {
			if (socket.isConnected()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tries to establish a TCP-connection to the echo server based on the given
	 * server address and the port number of the echo service.
	 * 
	 * @param remote_ip2
	 *            IP address of the remote server
	 * @param remote_port2
	 *            Port of the remote server
	 */
	public void connect(String remote_ip2, int remote_port2) {
		// Connect to remote server
		try {
			socket = new Socket(REMOTE_IP, REMOTE_PORT);
		} catch (UnknownHostException e) {
			System.out.println("Uknown host");
			logger.error("Uknown host");
			return;
		} catch (IOException e) {
			System.out.println("Failed to connect to the target");
			logger.error("Failed to connect to the target");
			return;
		}
		// initiate socket reader
		try {
			socketReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e1) {
			System.out.println("Failed to get socket reader");
			logger.error("Failed to get socket reader");
			return;
		}
		// initiate socket writer
		try {
			socketPrinter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Failed to get socket writer");
			logger.error("Failed to get socket writer");
			return;
		}
		// using byte stream
		// // Get input stream
		// try {
		// socketInputStream=socket.getInputStream();
		// } catch (IOException e) {
		// System.out.println("Failed to get socket input stream");
		// logger.error("Failed to get socket input stream");
		// }
		// // Get output stream
		// try {
		// socketOutputStream=socket.getOutputStream();
		// } catch (IOException e) {
		// System.out.println("Failed to get Output stream");
		// logger.error("Failed to get Output stream");
		// }
		return;
	}

	/**
	 * Block the thread to read message from the server.
	 */
	public void receive() {
		// Receive from server
		String recMsg = null;
		try {
			recMsg = socketReader.readLine();
			logger.info("receive " + recMsg);
		} catch (IOException e) {
			logger.error("Failed to receive from server");
			return;
		}
		System.out.println("EchoClient> " + recMsg);
		logger.info(recMsg);
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
		// send
		if (socket != null) {
			if (socket.isConnected()) {
				// using reader and writer
				socketPrinter.println(msg);
				socketPrinter.flush();
				logger.info("send " + msg);

				// Receive from server
				String recMsg = null;
				try {
					recMsg = socketReader.readLine();
					logger.info("receive " + recMsg);
				} catch (IOException e) {
					logger.error("Failed to receive from server");
					return;
				}
				System.out.println("EchoClient> " + recMsg);
			} else {
				System.out.println("Error! Not connected!");
				logger.warn("Attemped to send message but not connected");
			}
		} else {
			System.out.println("Please connect to a server");
			logger.warn("Please connect to a server");
		}
		return;
	}

	/**
	 * Tries to disconnect from the connected server.
	 */
	public void disconnect() {
		// Close everything
		if (socket != null) {
			if (socket.isConnected()) {
				if (socketPrinter != null) {
					socketPrinter.close();
					socketPrinter = null;
				}
				if (socketReader != null) {
					try {
						socketReader.close();
						socketReader = null;
					} catch (IOException e) {
						logger.error("Failed to close socket reader");
					}
				}
				if (socketInputStream != null) {
					try {
						socketInputStream.close();
						socketInputStream = null;
					} catch (IOException e) {
						logger.error("Failed to close socketInputStream");
					}
				}
				if(socketOutputStream!=null){
					try {
						socketOutputStream.close();
						socketOutputStream=null;
					} catch (IOException e) {
						logger.error("Failed to close socketInputStream");
					}
				}
				try {
					socket.close();
					socket = null;
				} catch (IOException e) {
					logger.error("Failed to close socket");
				}
				System.out.println("You have terminated the connection to server successfully.");
			} else {
				System.out.println("You are not connected to any server.");
			}
		} else {
			System.out.println("You are not connected to any server.");
		}

		return;
	}
}
