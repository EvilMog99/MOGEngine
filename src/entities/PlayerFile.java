package entities;

import java.io.Serializable;

public class PlayerFile implements Serializable
{
	private String rnd1, rnd2, rnd3, rnd4;
	private String username;
	
	public PlayerFile(String username, String rnd1, String rnd2, String rnd3, String rnd4)
	{
		this.username = username;
		this.rnd1 = rnd1;
		this.rnd2 = rnd2;
		this.rnd3 = rnd3;
		this.rnd4 = rnd4;
	}

	
	//getters and setters
	
	public String getUsername() {
		return username;
	}
	
	public String getRnd1() {
		return rnd1;
	}
	
	public String getRnd2() {
		return rnd2;
	}
	
	public String getRnd3() {
		return rnd3;
	}
	
	public String getRnd4() {
		return rnd4;
	}
	
	
}
