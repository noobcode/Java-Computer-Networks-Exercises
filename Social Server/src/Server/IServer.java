package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Client.IClient;

public interface IServer extends Remote {
	public void registerForCallback(String user, IClient c) throws RemoteException;
	public void follow(String username, String toFollow) throws RemoteException;  	
}
