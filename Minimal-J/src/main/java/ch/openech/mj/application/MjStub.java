package ch.openech.mj.application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

import ch.openech.mj.model.Index;

public class MjStub implements DbService {

	public static final Logger LOGGER = Logger.getLogger(MjStub.class.getName());

	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStrem;
	private final String url;
	private final int port;
	
	public MjStub(String url, int port) {
		this.url = url;
		this.port = port;
	}

	private void init() {
		if (socket != null && socket.isConnected() && !socket.isClosed()) {
			return;
		}
		if (!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException x) {
				LOGGER.warning("Could not close connection: " + x.getMessage());
			}
		}
		try {
			socket = new Socket(url, port);
			outputStrem = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException x) {
			throw new RuntimeException("Could not create Socket: " + x.getMessage());
		}
	}
	
	public <T> T read(Class<T> clazz, int id, Integer time) {
		try {
			init();
			outputStrem.writeObject("read");
			outputStrem.writeObject(clazz.getName());
			outputStrem.writeObject(id);
			outputStrem.writeObject(time);
			return (T) inputStream.readObject();
		} catch (IOException | ClassNotFoundException x) {
			throw new RuntimeException("Read from server failed: " + x.getMessage());
		}
	}
		
	public <T> T read(Class<T> clazz, int id) {
		return read(clazz, id, null);
	}
	
	public List<Integer> readVersions(int id) {
		
	}

	public <T> List<T> find(Class<T> clazz, Index index, Object query) {
		
	}

	public <T> T find(Class<T> clazz, Index.Unique index, Object query) {
		
	}
	
	public Object execute(String command, Object... args) {
		
	}
	
}
