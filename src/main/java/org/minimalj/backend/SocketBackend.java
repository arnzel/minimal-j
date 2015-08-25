package org.minimalj.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.minimalj.transaction.StreamConsumer;
import org.minimalj.transaction.StreamProducer;
import org.minimalj.transaction.Transaction;
import org.minimalj.transaction.persistence.DelegatePersistence;
import org.minimalj.util.SerializationContainer;

public class SocketBackend extends Backend {
	private final String url;
	private final int port;
	
	private final DelegatePersistence persistence;

	public SocketBackend(String url, int port) {
		this.url = url;
		this.port = port;
		this.persistence = new DelegatePersistence(this);
	}
	
	@Override
	public Persistence getPersistence() {
		return persistence;
	}

	@Override
	public <T> T execute(Transaction<T> transaction) {
		try (Socket socket = new Socket(url, port)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
				oos.writeObject(transaction);
				try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
					return readResult(ois);
				}
			}
		} catch (Exception c) {
			throw new RuntimeException("Couldn't connect to " + url + ":" + port);
		}
	}
	
	protected <T> T readResult(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		Object wrappedResult = ois.readObject();
		@SuppressWarnings("unchecked")
		T result =  (T) SerializationContainer.unwrap(wrappedResult);
		return result;
	}
	
	@Override
	public <T extends Serializable> T execute(StreamConsumer<T> streamConsumer, InputStream inputStream) {
		try (Socket socket = new Socket(url, port)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
				oos.writeObject(streamConsumer);
				sendStream(oos, inputStream);
				try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
					return readResult(ois);
				}
			}
		} catch (Exception c) {
			throw new RuntimeException("Couldn't connect to " + url + ":" + port);
		}
	}
	
	// send data from frontend to backend (import of data)
	private void sendStream(ObjectOutputStream oos, InputStream inputStream) throws IOException {
		int b;
		while ((b = inputStream.read()) >= 0) {
			oos.write(b);
		}
		oos.flush();
	}

	@Override
	public <T extends Serializable> T execute(StreamProducer<T> streamProducer, OutputStream outputStream) {
		try (Socket socket = new Socket(url, port)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
				oos.writeObject(streamProducer);
				try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
					receiveStream(ois, outputStream);
					return readResult(ois);
				}
			}
		} catch (Exception c) {
			throw new RuntimeException("Couldn't connect to " + url + ":" + port);
		}
	}

	// send data from backend to frontend (export data)
	private void receiveStream(ObjectInputStream ois, OutputStream outputStream) throws IOException {
		int b;
		while ((b = ois.read()) >= 0) {
			outputStream.write(b);
		}
		return;
	}

}