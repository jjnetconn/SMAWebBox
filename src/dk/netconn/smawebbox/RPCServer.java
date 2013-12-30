package dk.netconn.smawebbox;
  
import java.io.IOException; 
import java.io.OutputStream; 
import java.net.InetAddress;
import java.net.InetSocketAddress; 

import com.sun.net.httpserver.HttpServer; 
import com.sun.net.httpserver.HttpExchange; 
import com.sun.net.httpserver.HttpHandler; 

import java.util.Properties; 
  
public class RPCServer { 
  
    public void main(Properties conf) throws Exception { 
    	
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLocalHost(), 8000), 0);
        server.createContext("/test", new TestHandler()); 
        server.createContext("/rpc", new RPCHandler()); 
        server.setExecutor(null); // creates a default executor 
        server.start(); 
    } 
  
    class TestHandler implements HttpHandler { 
        @Override
        public void handle(HttpExchange t) throws IOException { 
              
            String response = "This is the response"; 
            t.sendResponseHeaders(200, response.length()); 
            try (OutputStream os = t.getResponseBody()) { 
                os.write(response.getBytes()); 
            } 
        } 
    } 
      
    class RPCHandler implements HttpHandler { 
        @Override
        public void handle(HttpExchange t) throws IOException { 
              
                String response = "{\n" + 
                                "\"version\": \"1.0\",\n" + 
                                "\"proc\": \"GetPlantOverview\",\n" + 
                                "\"id\": \"1\",\n" + 
                                "\"result\":\n" + 
                                "{\n" + 
                                "\"overview\":\n" + 
                                "[\n" + 
                                "{\n" + 
                                "\"meta\": \"GriPwr\",\n" + 
                                "\"name\": \"Momentanleistung\",\n" + 
                                "\"value\": \"4250\",\n" + 
                                "\"unit\": \"W\"\n" + 
                                "},\n" + 
                                "{\n" + 
                                "\"meta\": \"GriEgyTdy\",\n" + 
                                "\"name\": \"Tagesenergie\",\n" + 
                                "\"value\": \"45.23\",\n" + 
                                "\"unit\": \"kWh\"\n" + 
                                "},\n" + 
                                "{\n" + 
                                "\"meta\": \"GriEgyTot\",\n" + 
                                "\"name\": \"Gesamtenergie\",\n" + 
                                "\"value\": \"7821\",\n" + 
                                "\"unit\": \"kWh\"\n" + 
                                "},\n" + 
                                "{\n" + 
                                "\"meta\": \"OpStt\",\n" + 
                                "\"name\": \"Status\",\n" + 
                                "\"value\": \"MPP\",\n" + 
                                "\"unit\": null\n" + 
                                "},\n" + 
                                "{\n" + 
                                "\"meta\": \"Msg\",\n" + 
                                "\"name\": \"Fehler\",\n" + 
                                "\"value\": null,\n" + 
                                "\"unit\": null\n" + 
                                "}\n" + 
                                "]\n" + 
                                "}\n" + 
                                "} "; 
              
                t.sendResponseHeaders(200, response.length()); 
                try (OutputStream os = t.getResponseBody()) { 
                    os.write(response.getBytes()); 
            } 
        } 
    } 
}
