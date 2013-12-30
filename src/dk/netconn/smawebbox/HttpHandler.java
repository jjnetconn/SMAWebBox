package dk.netconn.smawebbox;

import java.io.IOException;
import java.net.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import net.sf.json.*;

public class HttpHandler {
            public URLConnection connection;
    
        public HttpHandler(Properties prop) throws IOException{
            connection = Connect(prop);
        }
        
        public HttpHandler(Properties prop, JSONObject jsRequest) throws IOException{
            connection = Connect(prop);
            GetData(prop, jsRequest);
        }
        
        public URLConnection Connect(Properties prop) throws IOException{

        	URL url;
        	
        	if(Boolean.parseBoolean(prop.getProperty("debug"))){ 
        	    url = new URL("http", "192.168.0.25", 8000, "/rpc");
            }
        	else{
        		url = new URL("http", prop.getProperty("endpoint"), Integer.parseInt(prop.getProperty("rpcport")), "/rpc");
        	}
            URLConnection conn = url.openConnection();
            
            
            try {
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.connect();
                        
		} catch (IOException ex) {
			
			System.out.println(ex);
			
		}
            return conn;
        }
        
	public String GetData(Properties prop, JSONObject jsRequest) throws IOException
	{       
            DataOutputStream dataOut;
            if(connection == null){
                connection = Connect(prop);
            }
            dataOut = new DataOutputStream(connection.getOutputStream());

		try{
			//this.dataOut.writeBytes(jsRequest.toString());
            dataOut.writeBytes("RPC=" + jsRequest.toString());
			dataOut.flush();
			dataOut.close();
		}
		catch(IOException ex){
			System.out.println(ex.getMessage());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		
		String output;
                StringBuilder rtnString = new StringBuilder();
		if(Boolean.parseBoolean(prop.getProperty("debug"))){
                    System.out.println("Output from Server .... \n");
                }
		while ((output = br.readLine()) != null) {
			rtnString.append(output);
			if(Boolean.parseBoolean(prop.getProperty("debug"))){
                        System.out.println(output);
                        }
		}
                
                return rtnString.toString();
	}
        
        public void Dispose() throws IOException{
            connection = null;
        }
}
