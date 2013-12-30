package dk.netconn.smawebbox;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class Main {	
    public static UI DisplayUI;
    public static Properties prop = new Properties();
    public static JsonHandler jsonHandler = new JsonHandler();
    
    //public static JSONObject jsRequest = jsonHandler.buildJson(prop);
    
    public static void main(String[] args) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException{
        prop.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        
        if(Boolean.parseBoolean(prop.getProperty("debug"))){ 
            RPCServer server = new RPCServer(); 
            try { 
                server.main(prop); 
            } catch (Exception e) { 
            } 
        }

        DisplayUI =  new UI(prop, jsonHandler);

    }
}
