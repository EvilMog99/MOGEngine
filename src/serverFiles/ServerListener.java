package serverFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerListener extends Thread
{
	ConnectionManager manager;
	
	public Socket socket;
	private ObjectOutputStream sender;
	private ObjectInputStream receiver;
	
	public boolean inUse = false;
	public int id;
	public static int numberMade = 0;
	
	
	public PlayerData playerData;
	public boolean setPlayerData = false;
	
	public DataPacket dataPacket;//to store received data
	public MessageToClientFs messageOut;
	
	private int nextRequiredModIndex = 0;//index of the next mod that the server will tell the client it needs

	//for mod receiving
	public List<SendObject> bufferedRecievedObjects;
//	boolean nextSendMessageWillBeFile = false;
	
	public AdminClientData adminClientData;
	private int confirmCommand = AdminServerCommandsFc.NoCommand;
	
	//for mod sending
	private boolean requestedMod = false;
	private String requestedModName;
	private int requestedModNextFileIndex;//next file the server will send to the client
	
	private int indexOfRequestedMod = -1;//index of requested mod on server
	private boolean sendObjectReady = false;
	private List<SendObject> bufferedSendObjects;
	
	//file receiving
//	private boolean nextReceivedMessageWillBeFile = false;
	

	public ServerListener(ServerSocket serverSocket, ConnectionManager manager) throws IOException
	{
		this.manager = manager;
		messageOut = new MessageToClientFs("", false, null, confirmCommand);
		bufferedRecievedObjects = new ArrayList<SendObject>();
		adminClientData = new AdminClientData();		
		
		dataPacket = new DataPacket();
		
		System.out.println("about to make listener");
		socket = serverSocket.accept();
		System.out.println("opening listener");
		sender = new ObjectOutputStream(socket.getOutputStream());
		receiver = new ObjectInputStream(socket.getInputStream());
		
		inUse = true;
		id = ++numberMade;
		
		this.start();
	}
	
	public void send(String message, boolean isAdmin, AdminClientData adminClientData)
	{
		try {
			messageOut = new MessageToClientFs(message, isAdmin, adminClientData, confirmCommand);
			
			if (requestedMod && sendObjectReady)//add in next file to send to client
			{
				messageOut.fileObject = new SendObject(bufferedSendObjects.get(requestedModNextFileIndex));
				messageOut.fileObject.setSaveOnArrival(true);
				System.out.println("Sending " + playerData.getUsername() + " file for " + messageOut.fileObject.modFileName_ref 
						+ " mod, file " + (requestedModNextFileIndex + 1) + "/" + bufferedSendObjects.size());
				System.out.println("File length: " + messageOut.fileObject.modFile.length);
				requestedModNextFileIndex++;
				messageOut.fileSet = true;
				if (requestedModNextFileIndex >= bufferedSendObjects.size())
				{
					requestedMod = false;
				}
			}
			
			sender.writeObject(messageOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		MessageToServerFc[] fromClient = {new MessageToServerFc("", false, null), new MessageToServerFc("", false, null)};
		int bufferInUse = 0;
		int lastBuffer = 0;
		
		try {
			
			do {
				
				try {
					fromClient[bufferInUse] = (MessageToServerFc)receiver.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (fromClient[bufferInUse] == null) 
				{
					break;
				}
				
				//process received message
				dataPacket.readMessage(fromClient[lastBuffer].message.toCharArray());
				dataPacket.setAdminServerCommandsFc(fromClient[lastBuffer]);
				//System.out.println("Server Received String----------------------------------------------------------------");
				//System.out.println("Server's rec message: " + ((SimpleMessageString)fromClient[lastBuffer]).message);
				
				if (fromClient[lastBuffer].fileSet)
				{
					fromClient[lastBuffer].fileSet = false;
					bufferedRecievedObjects.add(fromClient[lastBuffer].fileObject);
					System.out.println("Server Received File----------------------------------------------------------------");
				}
				
				if (fromClient[lastBuffer].requestingMod)
				{
					if (!requestedMod)//if a mod isn't currently being requested?
					{
						requestedModName = fromClient[lastBuffer].requestedModName;
						requestedModNextFileIndex = 0;
						requestedMod = true;
						sendObjectReady = false;
						System.out.println("Server received request for: " + requestedModName);
						//will now send one file back with every returned message until it has sent all file for selected mod once
					}
				}
				
				//buffer important info from client here
				//System.out.println("Server receieved: " + fromClient);
				//sender.println("hey client");
				
				if (bufferInUse == 0)
				{
					bufferInUse = 1;
					lastBuffer = 0;
				}
				else
				{
					bufferInUse = 0;
					lastBuffer = 1;
				}
				
			}
			while (fromClient[bufferInUse].message != "quit" && inUse);

			sender.close();
			receiver.close();
			socket.close();
			
			inUse = false;
			
			manager.allConnections.remove(id - 1);
			
			System.out.println("ended connection with client " + id);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void close()
	{
		try {
			sender.close();
			receiver.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			System.out.println("Ended connection with client");
			e.printStackTrace();
		}
	}
	
	//all getters and setters
	public void setConfirmCommand(int confirmCommand) {
		this.confirmCommand = confirmCommand;
	}
	
	public int getNextRequiredModIndex() {
		return nextRequiredModIndex;
	}

	public void setNextRequiredModIndex(int nextRequiredModIndex) {
		this.nextRequiredModIndex = nextRequiredModIndex;
	}
	
	public void setBufferedSendObjects(List<SendObject> bufferedSendObjects) {
		sendObjectReady = true;
		this.bufferedSendObjects = bufferedSendObjects;
	}
	public String getRequestedModName() {
		return requestedModName;
	}
	public boolean isRequestedMod() {
		return requestedMod;
	}
	public boolean isSendObjectReady() {
		return sendObjectReady;
	}
}
