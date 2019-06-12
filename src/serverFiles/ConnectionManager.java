package serverFiles;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager extends Thread
{
	ServerSocket serverSocket;
	
	List<ServerListener> allConnections;
	
	boolean endServer = false;
	
	public ConnectionManager() 
	{
	}
	
	public ConnectionManager(int port) 
	{
		try {
			
			serverSocket = new ServerSocket(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		allConnections = new ArrayList<ServerListener>();
		
		this.start();
	}
	
	
	public void run()
	{
		try {
			
			while (!endServer)
			{
				allConnections.add(new ServerListener(serverSocket, this));
			
				System.out.println("Added new client");
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			System.out.println("Failed to create new connection");
			e.printStackTrace();
		}
	}
	

	public void close()
	{
		for (int i = 0; i < allConnections.size(); i++)
		{
			allConnections.get(i).inUse = false;
			allConnections.get(i).close();
		}
		
		System.out.println("stopped con manager");
		
		try {
			
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
