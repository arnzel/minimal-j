package ch.openech.mj.application;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MjServer {

	private final DbService serviceDelegate;
	private final int port;
	private final ThreadPoolExecutor executor;
	
	public MjServer(DbService serviceDelegate, int port) {
		this.serviceDelegate = serviceDelegate;
		this.port = port;
		this.executor = new ThreadPoolExecutor(10, 30, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public void run() {
		Thread.currentThread().setName("MjServer");
		ServerSocket serverSocket = new ServerSocket(port);
		while (true) {
			Socket socket = serverSocket.accept();
			MjServerRunnable runnable = new MjServerRunnable(socket);
			executor.execute(runnable);
		}
	}
	
	private static class MjServerRunnable implements Runnable {
		private final Socket socket;

		public MjServerRunnable(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			
		}
	}
	
	// interface kann komplette Objekte hin und her schieben oder auch einen Stream (von Objekten?)
	
//	public static void main(String[] args) {
//		new MjServer(9000).run();
//	}
}
