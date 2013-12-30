/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.netconn.smawebbox;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import net.sf.json.JSONObject;

/**
 *
 * @author NetConn
 */
public class UI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3081735962304727971L;
	private HttpHandler httphandler;
    private JsonHandler jsonHandler;
    private Properties prop;
    private final JSONObject jsRequest;
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private ArrayList<String[]> results = new ArrayList<String[]>();
    private int i;

    //Frame
    private JFrame Frame;
    
    
    //Constructor
    public UI(Properties prop, JsonHandler jsonHandler) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException{    
        httphandler = new HttpHandler(prop);
        this.prop = prop;
        this.jsonHandler = jsonHandler;
        jsRequest = jsonHandler.buildJson(prop);
        //results = jsonHandler.readJson(httphandler.GetData(prop, jsRequest));
        httphandler.Dispose();
//        httphandler.GetData(jsRequest);
        
        //hide mouse cursor
        
        Frame = setupDisplay();
        Frame.revalidate();
        Frame.repaint();
        
        i = 1;
        updateResult();
        Run();
    }
    
    
    private void Run(){
        Timer DisplayTimer = new Timer(10000, listener);
        DisplayTimer.start();
    }
    
    private void updateResult() throws IOException{
        try{
        	results = jsonHandler.readJson(httphandler.GetData(prop, jsRequest));
        }
        catch(Exception e){
        	System.out.println(e.getMessage());
        }
        httphandler.Dispose();
    }
    
    private final ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if( i > 4){ i=1;}    
                if(i == 1){
                        Frame.getContentPane().removeAll();
                        Frame.getContentPane().add(currentPanel(results));
                        Frame.revalidate();
                        Frame.repaint();

                    }
                    if(i == 2){
                        Frame.getContentPane().removeAll();
                        Frame.getContentPane().add(dailyPanel(results));
                        Frame.revalidate();
                        Frame.repaint();

                    }
                    if(i == 3){
                        Frame.getContentPane().removeAll();
                        Frame.getContentPane().add(totalPanel(results));
                        Frame.revalidate();
                        Frame.repaint();

                    }
                    if(i == 4){
                        Frame.getContentPane().removeAll();
                        Frame.getContentPane().add(co2Panel(results));
                        Frame.revalidate();
                        Frame.repaint();
                            try {
								updateResult();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

                    }                       
                    i++;
                
            
        }
    };
    
    private JFrame setupDisplay(){

        JFrame frame = new JFrame();
        frame.setTitle("NetConn - SMA Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        //hide cursor
        frame.setCursor(getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(0,0),null));
//        frame.setAlwaysOnTop(true);
        JPanel Splash = new JPanel();
        JLabel SplashText = new JLabel("Henter data...");
        Splash.add(SplashText);

        frame.add(Splash, BorderLayout.CENTER);
        
        if (!device.isFullScreenSupported ())
        {
        	frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            //throw new UnsupportedOperationException ("Fullscreen mode is unsupported.");
        }
        else{
        	device.setFullScreenWindow (frame);
        }
        return frame;
    }
    
   private JPanel currentPanel(ArrayList<String[]> results){
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        //Create JPanel and add to Frame
        JPanel panel = new JPanel();
        panel.setLayout(gridbag);
        
        JLabel Text = new JLabel();
        JLabel Value = new JLabel();
        
        //Set size and Add Label
        Text.setFont(new java.awt.Font("Arial Black", 0, 128));
        Value.setFont(new java.awt.Font("Arial Black", 0, 128));
        
        gridbag.setConstraints(Text, gbc);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        
        gridbag.setConstraints(Value, gbc);

        Text.setText("Solceller nu");
        Value.setText(results.get(0)[0] + " " + results.get(0)[1]);
        
        panel.add(Text, gbc);
        panel.add(Value, gbc);

        return panel;
   }
   private JPanel dailyPanel(ArrayList<String[]> results){
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        //Create JPanel and add to Frame
        JPanel panel = new JPanel();
        panel.setLayout(gridbag);
        
        JLabel Text = new JLabel();
        JLabel Value = new JLabel();
        
        //Set size and Add Label
        Text.setFont(new java.awt.Font("Arial Black", 0, 128));
        Value.setFont(new java.awt.Font("Arial Black", 0, 128));
        
        gridbag.setConstraints(Text, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(Value, gbc);

        Text.setText("Solceller i dag");
        Value.setText(results.get(1)[0] + " " + results.get(1)[1]);

        panel.add(Text, gbc);
        panel.add(Value, gbc);

        return panel;
   }
   private JPanel totalPanel(ArrayList<String[]> results){     
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        //Create JPanel and add to Frame
        JPanel panel = new JPanel();
        panel.setLayout(gridbag);
        
        JLabel Text = new JLabel();
        JLabel Value = new JLabel();
        
        //Set size and Add Label
        Text.setFont(new java.awt.Font("Arial Black", 0, 128));
        Value.setFont(new java.awt.Font("Arial Black", 0, 128));
        
        gridbag.setConstraints(Text, gbc);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        
        gridbag.setConstraints(Value, gbc);

        Text.setText("Solceller totalt");
        Value.setText(results.get(2)[0] + " " + results.get(2)[1]);

        panel.add(Text, gbc);
        panel.add(Value, gbc);

        return panel;    
   }
   private JPanel co2Panel(ArrayList<String[]> results){
       GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        //Create JPanel and add to Frame
        JPanel panel = new JPanel();
        panel.setLayout(gridbag);
        
        JLabel Text = new JLabel();
        JLabel Value = new JLabel();
        
        //Set size and Add Label
        Text.setFont(new java.awt.Font("Arial Black", 0, 128));
        Value.setFont(new java.awt.Font("Arial Black", 0, 128));
        
        gridbag.setConstraints(Text, gbc);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        
        gridbag.setConstraints(Value, gbc);

        Text.setText("CO2 Besparelse");
        Value.setText(calcCO2(results.get(2)[0]) + " " + "ton");

        panel.add(Text, gbc);
        panel.add(Value, gbc);

        return panel;
   
   }
   
   private BigDecimal calcCO2(String total){
    
//    try{
//    	production = Double.parseDouble(total);
//    }
//    catch(Exception ex){
//    }
	   BigDecimal production = new BigDecimal(total);	
	   BigDecimal co2Factor = new BigDecimal(0.0005925);
	   BigDecimal co2Reduct = production.multiply(co2Factor);
    
    return co2Reduct.setScale(3, BigDecimal.ROUND_CEILING);
   }
}
