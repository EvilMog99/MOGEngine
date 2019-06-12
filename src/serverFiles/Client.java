package serverFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import modComponents.IntegratedModData;

public class Client extends Thread
{
	public Socket socket;
	//normal messages
	public ObjectOutputStream sender;
	public ObjectInputStream receiver;
	public int userID = -1;
	
	public boolean inUse = false;
	
	public DataPacket[] dataPacket;
	private int updatedPacket = 0;
	
	private MessageToServerFc messageOut;
	public AdminServerCommandsFc adminServerCommandsFc;
	//file sending
//	private boolean nextSendMessageWillBeFile = false;
	
		//mod uploading variables
	private int objListIndex = 0;
	public List<SendObject> allModFiles_clientUpload = new ArrayList<SendObject>();
	private boolean uploadClientMod = false;
	

	//file receiving
	public boolean receivedModIsReady = false;
	public boolean requestSent = false;
	public ModStorage receivedMod;
//	private boolean receivedMessageWillBeFile = false;
	
	private boolean retreivedWorldParameters = false;
	private int[][] allEntityAnimationFrames;
	
	
	public Client(String ip, int port, DataPacket dp1, DataPacket dp2)
	{
		adminServerCommandsFc = new AdminServerCommandsFc();
		messageOut = new MessageToServerFc("", true, adminServerCommandsFc);
		
		dataPacket = new DataPacket[2];
		
		dataPacket[0] = dp1;
		dataPacket[1] = dp2;
		
		try {
//			System.out.println("connecting to ip: " + ip + " port: " + port);
			socket = new Socket(ip, port);
		
			sender = new ObjectOutputStream(socket.getOutputStream());
			receiver = new ObjectInputStream(socket.getInputStream());

			this.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//send client's input data to the server
	public MessageToServerFc prepareClientMessage(int[] inputCommands)
	{
		messageOut = new MessageToServerFc("", true, adminServerCommandsFc);
		messageOut.message = "C";

		for (int command : inputCommands)
		{
			messageOut.message += "n";
			messageOut.message += command;
		}
		
		messageOut.message += "e";
		
		if (uploadClientMod && allModFiles_clientUpload.size() > 0)
		{
			messageOut.message = "F";
			
			messageOut.fileObject = allModFiles_clientUpload.get(0);
			messageOut.fileObject.maxNumberOfFiles = allModFiles_clientUpload.size();
			allModFiles_clientUpload.remove(0);
			messageOut.fileSet = true;
			
			messageOut.message = "e";
			
			
			if(allModFiles_clientUpload.size() == 0)
			{
				uploadClientMod = false;
				messageOut.message = "V";
				messageOut.message = "e";
			}
		}

		if (receivedModIsReady && !requestSent)
		{
			if (!receivedMod.isFinishedLoadingMod())
			{
				requestSent = true;
				messageOut.requestingMod = true;
				messageOut.requestedModName = receivedMod.getModRefName();
			}
		}
		return messageOut;
	}
	
	public void send(MessageToServerFc message)
	{
		try {
			//System.out.println("Client's sent message command id: " + message.commandID);
			sender.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.close();
			System.out.println("Close client: " + this.userID);
		}
	}
	
//	public void sendFile(Object file)
//	{
//		try {
//			sender.writeObject(file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void run()
	{
		
		try {
			
			inUse = true;
			int currentPacketWriteTo;
			
			MessageToClientFs fromServer_object = new MessageToClientFs("", false, null, AdminServerCommandsFc.NoCommand);
			AdminClientData tempAdminClientData;
			String fromServer_string = "";
			
			do
			{
				fromServer_object = (MessageToClientFs)recieveMessage(receiver);
				
				fromServer_string = fromServer_object.message;
				//System.out.println("client received string------------------------------------------------------------");
				
				//buffer important data from server into the available data packet
				if (!dataPacket[0].beingRead)
				{
					currentPacketWriteTo = 0;
				}
				else 
				{
					currentPacketWriteTo = 1;
				}
				
				dataPacket[currentPacketWriteTo].beingUpdated = true;
				dataPacket[currentPacketWriteTo].readMessage(fromServer_string.toCharArray());
				dataPacket[currentPacketWriteTo].setAdminClientData(fromServer_object);
				dataPacket[currentPacketWriteTo].setServerConfirmCommand(fromServer_object.confirmCommand);
				updatedPacket = currentPacketWriteTo;
				dataPacket[currentPacketWriteTo].beingUpdated = false;
				
				if (!retreivedWorldParameters)
				{
					if (dataPacket[currentPacketWriteTo].getWorldLengthX() != -1)
					{
						System.out.println("Received world data");
						allEntityAnimationFrames = new int[dataPacket[currentPacketWriteTo].getWorldLengthX()][dataPacket[currentPacketWriteTo].getWorldLengthY()];
						for (int i = 0; i < allEntityAnimationFrames.length; i++)
						{
							for (int j = 0; j < allEntityAnimationFrames[i].length; j++)
							{
								allEntityAnimationFrames[i][j] = 0;
							}
						}
					}
					retreivedWorldParameters = true;
				}
				
				if (fromServer_object.fileSet && receivedModIsReady && requestSent)//if client is ready to receive mod files
				{
					fromServer_object.fileSet = false;
					
					if (fromServer_object.fileObject.modFileName_ref.equals(receivedMod.getModRefName()))
					{
						receivedMod.addFile_clientDownload(fromServer_object.fileObject);
						System.out.println("Client received file " + fromServer_object.fileObject.fileName 
								+ " mod file " + receivedMod.getAllModFiles_clientDownload().size() + "/" + receivedMod.getMaxNumberOfFiles() 
								+ " from server");
						
						if (receivedMod.isFinishedLoadingMod())
						{
							requestSent = false;
							System.out.println("Received all files for " + fromServer_object.fileObject.modFileName_ref + " mod from server");
							//mod will be saved in GameRunner
						}
					}
				}
				
				//System.out.println("Client receieved: " + fromServer);
				
				
				//nextSendMessageWillBeFile = dataPacket[updatedPacket].isNextSendMessageWillBeFile();
			} while ( fromServer_object != null && inUse);
			
			
			sender.close();
			receiver.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			close();
		}
		
	}
	
	private Object recieveMessage(ObjectInputStream oIS)
	{
		try {
			return oIS.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	public void close()
	{
		inUse = false;
		
		try {
			sender.close();
			receiver.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			System.out.println("Ended connection with server");
			e.printStackTrace();
		}
		
		this.interrupt();
	}
	
	//all getters and setters
	
	public int getUpdatedPacket() {
		return updatedPacket;
	}

	public boolean isUploadClientMod() {
		return uploadClientMod;
	}

	public void setUploadClientMod(boolean uploadClientMod) {
		this.uploadClientMod = uploadClientMod;
	}

	public int getAllEntityAnimations(int x, int y) {
		return allEntityAnimationFrames[x][y];
	}

	public void setAllEntityAnimations(int allEntityAnimationFrameNo, int x, int y) {
		this.allEntityAnimationFrames[x][y] = allEntityAnimationFrameNo;
	}

	public boolean isRetreivedWorldParameters() {
		return retreivedWorldParameters;
	}

	public void setRetreivedWorldParameters(boolean retreivedWorldParameters) {
		this.retreivedWorldParameters = retreivedWorldParameters;
	}
}
