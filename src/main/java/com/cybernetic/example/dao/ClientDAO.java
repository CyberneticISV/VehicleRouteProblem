package com.cybernetic.example.dao;

import com.cybernetic.example.entities.Client;

import java.util.HashMap;
import java.util.Map;

public class ClientDAO {
    private static Map<String, Client> clientMap = new HashMap<>();

    static {
        int clientId = 0;
        //TODO:DEPO (TEMPORARY)
        clientMap.put(Integer.toString(clientId), new Client(Integer.toString(clientId), "3.141257", "101.627438"));

        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.170417", "101.697239"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.120180", "101.676486"));
/*        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.171271", "101.659408"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.164101", "101.692416"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.150884", "101.663927"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.132920", "101.675814"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.120277", "101.677696"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.136244", "101.708199"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.157226", "101.705118"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.160966", "101.697112"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.126259", "101.643646"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.152472", "101.712219"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.151735", "101.665994"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.160715", "101.739747"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.150363", "101.706311"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.117287", "101.675390"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.150363", "101.706311"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.118270", "101.595194"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.127778", "101.622675"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.094823", "101.614300"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.129285", "101.620516"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.175605", "101.659517"));
        clientMap.put(Integer.toString(++clientId), new Client(Integer.toString(clientId), "3.170979", "101.665914"));*/
   }

    public ClientDAO() {
    }

    public Client getClientById(String id) {
        return clientMap.get(id);
    }

    public Map<String, Client> getAllClients() {
        return clientMap;
    }
}
