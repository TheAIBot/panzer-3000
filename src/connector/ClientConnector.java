package connector;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.List;

import org.jspace.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.corba.se.spi.activation.Server;
import com.sun.swing.internal.plaf.metal.resources.metal;

import Logger.Log;
import engine.*;

public class ClientConnector implements Runnable{

	public SecureRemoteSpace 	privateServerConnections;
	public SecureRemoteSpace 	updateSpace;
	public int 					connectionId;
	public String				username;
	public int 					numberOfClients;
	private KeyPair				keyPair;
	private String				salt;
	
	public void connectToServer(String ipaddress, String username, KeyPair keyPair, String salt) throws UnknownHostException, IOException, InterruptedException {
		this.username = username;
		this.keyPair = keyPair;
		this.salt = salt;
		updateSpace		= new SecureRemoteSpace("tcp://" + ipaddress + ":9001/updateSpace?keep");
		List<Object[]> tuples = updateSpace.queryAll(new FormalField(Object.class), new FormalField(Object.class));
		
		updateSpace.put(new ActualField(username), new ActualField(salt));
		
		Object[] tuple1 = updateSpace.query(new ActualField("numClients"), new FormalField(Integer.class));
		numberOfClients = (int) tuple1[1];
		Object[] tuple 	= updateSpace.get(new FormalField(Integer.class), new ActualField(username));
		connectionId   	= (int) tuple[0];
		privateServerConnections = new SecureRemoteSpace("tcp://" + ipaddress + ":9001/clientSpace" + connectionId + "?keep");
		privateServerConnections.put("connected", connectionId);
	}
	
	public Object[] receiveWalls() throws InterruptedException {
		return updateSpace.get(new ActualField("walls"), new FormalField(byte[].class));
	}
	
	public Object[] recieveUpdates() throws InterruptedException {
		return updateSpace.get(new ActualField(connectionId), new FormalField(byte[].class), 
				new FormalField(byte[].class), new FormalField(byte[].class));
	}
	
	
	public void sendUserInput(Input input) {
		input.id = connectionId;
		privateServerConnections.put(input);		
	}

	@Override
	public void run() {
		try {
			connectToServer("localhost", username, keyPair, salt);			
		} catch (Exception e) { 
			Log.exception(e);
		}
	}
	
	

	//sendUserInputs(Input inputs);
	//recieveUpdates();
}
