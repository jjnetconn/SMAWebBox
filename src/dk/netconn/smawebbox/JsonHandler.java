package dk.netconn.smawebbox;

import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
import java.util.ArrayList;
import net.sf.json.*;
import net.sf.json.JSONSerializer;
import java.util.Properties;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import net.sf.ezmorph.*;

public class JsonHandler {

	public JSONObject buildJson(Properties conf) throws UnsupportedEncodingException, NoSuchAlgorithmException{
	
            String toEnc = conf.getProperty("password").toString(); // Value to encrypt
            MessageDigest mdEnc = MessageDigest.getInstance("MD5"); 
            mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
            //String md5 = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted 
            
            //RPC={"version": "1.0", "proc": "GetPlantOverview", "id": "1", "format": "JSON"}
		JSONObject json = new JSONObject();
		json.put("version", conf.getProperty("version"));
		json.put("proc", conf.getProperty("proc"));
		json.put("id", conf.getProperty("id"));
		json.put("format", conf.getProperty("format"));
                //json.put("passwd", md5);
                //json.put("params", "");
		
		//System.out.println(json.toString());
		
		return json;
	}
	
	public ArrayList<String[]> readJson(String response){

            ArrayList<String[]> rtnValues = new ArrayList<String[]>();
            JSONObject jsonResponse = (JSONObject) JSONSerializer.toJSON( response );
            JSONArray overViewArray = jsonResponse.getJSONObject("result").getJSONArray("overview");
           
            JSONObject GridPower = overViewArray.getJSONObject(0);
            JSONObject GridEToDay = overViewArray.getJSONObject(1);
            JSONObject GridETotal = overViewArray.getJSONObject(2);
            JSONObject Operations = overViewArray.getJSONObject(3);
            JSONObject Message = overViewArray.getJSONObject(4);
            
            String[] resGridPwr = new String[2];
            String[] resGridDay = new String[2];
            String[] resGridTot = new String[2];
            String[] resOp = new String[2];
            String[] resMsg = new String[2];
            
            resGridPwr[0] = GridPower.get("value").toString();
            resGridPwr[1] = GridPower.get("unit").toString();
            
            resGridDay[0] = GridEToDay.get("value").toString();
            resGridDay[1] = GridEToDay.get("unit").toString();
            
            resGridTot[0] = GridETotal.get("value").toString();
            resGridTot[1] = GridETotal.get("unit").toString();
            
            resGridTot[0] = GridETotal.get("value").toString();
            resGridTot[1] = GridETotal.get("unit").toString();
            
            resOp[0] = Operations.get("value").toString();
            resOp[1] = Operations.get("unit").toString();
            
            resMsg[0] = Message.get("value").toString();
            resMsg[1] = Message.get("unit").toString();
            
            rtnValues.add(resGridPwr);
            rtnValues.add(resGridDay);
            rtnValues.add(resGridTot);
            rtnValues.add(resOp);
            rtnValues.add(resMsg);
            
            return rtnValues;
	}
	
}
