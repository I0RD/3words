import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class AdvancedWindow
{

    public  JPanel panel;
    private  JButton getByWordsButton;
    private  JButton getByLatLongButton;
    private  JTextField wordsTextField;
    private  JTextField longitudeText;
    private  JTextField latitudeText;
    private  JCheckBox plCheckBox;
    private JLabel UrlLabel;
    private JLabel ResponseLabel;
    private JButton RandomLatLongButton;
    private  String[] str;

    public AdvancedWindow()
    {
        getByLatLongButton.addActionListener(event->get3Words());
        getByWordsButton.addActionListener(event->getWorld());
        RandomLatLongButton.addActionListener(event-> {
            try {
                randomLatLong();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args)
    {
       JFrame jFrame=new JFrame("3Words API");
       jFrame.setContentPane(new AdvancedWindow().panel);
       jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       jFrame.pack();
       jFrame.setVisible(true);
    }
    public void get3Words() {
        try {
            String longitude=longitudeText.getText();
            String latitude=latitudeText.getText();
            String spec;
            if(plCheckBox.isSelected())
            {
                spec="https://api.what3words.com/v3/convert-to-3wa?"+ "coordinates="+latitude+"%2C"+longitude+"&language=pl&key=F8QT0PCM";
            }
            else
            {
                spec="https://api.what3words.com/v3/convert-to-3wa?"+ "coordinates="+latitude+"%2C"+longitude+"&key=F8QT0PCM";
            }
            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());}

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            String resault="";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                resault+=output;
            }
            String resaults[]=resault.split("\"");
            String words=resaults[31];
            String wordsurl=resaults[39];
            conn.disconnect();
            wordsurl=wordsurl.replaceAll("\\\\","");
            goWebsite(UrlLabel,wordsurl,wordsurl);
            wordsTextField.setText(words);
        } catch (Exception e) {e.printStackTrace();}
    }
    private  void getWorld()
    {
        try {
            String words=wordsTextField.getText();
            words= URLEncoder.encode(words, StandardCharsets.UTF_8);
            String spec="https://api.what3words.com/v3/convert-to-coordinates?"+ "words="+words+"&key=F8QT0PCM";
            URL url = new URL(spec);
            System.out.println(spec);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());}

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            String resault="";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                resault+=output;
            }
           String resaults[]=resault.split("\"");
            String lon=resaults[26].substring(1,resaults[26].length()-1);
            String lat=resaults[28].substring(1,resaults[28].length()-2);;
            String wordsurl=resaults[39];
            wordsurl=wordsurl.replaceAll("\\\\","");
            goWebsite(UrlLabel,wordsurl,wordsurl);
            latitudeText.setText(lat);
            longitudeText.setText(lon);
            conn.disconnect();
        } catch (Exception e) {e.printStackTrace();}
    }
    private void randomLatLong() throws IOException {
        try
        {
        int startLon=14;
        int endLon=24;
        int startLat=49;
        double endLat=55;
        double randLon=new Random().nextDouble();
        double randLat=new Random().nextDouble();
        double randomLon=startLon+((randLon*(endLon-randLon))/10);
        double randomLat=startLat+((randLat*(endLat-randLat))/10);
        latitudeText.setText(""+randomLat);
        longitudeText.setText(""+randomLon);
        String spec;
            if(plCheckBox.isSelected())
            {
                spec="https://api.what3words.com/v3/convert-to-3wa?"+ "coordinates="+randomLat+"%2C"+randomLon+"&language=pl&key=F8QT0PCM";
            }
            else
            {
                spec="https://api.what3words.com/v3/convert-to-3wa?"+ "coordinates="+randomLat+"%2C"+randomLon+"&key=F8QT0PCM";
            }
            URL url = new URL(spec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output;
        String resault="";
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            resault+=output;
        }
        String resaults[]=resault.split("\"");
        String words=resaults[31];
        String wordsurl=resaults[39];
        conn.disconnect();
        wordsurl=wordsurl.replaceAll("\\\\","");
        goWebsite(UrlLabel,wordsurl,wordsurl);
        wordsTextField.setText(words);
    }
        catch (Exception e)
        {e.printStackTrace();}
    }
    private void goWebsite(JLabel website, final String url, String text)
    {
        website.setText("<html> Website : <a href=\"\">"+text+"</a></html>");
        website.setCursor(new Cursor(Cursor.HAND_CURSOR));
       for(MouseListener ms:website.getMouseListeners())
       {website.removeMouseListener(ms);}
           website.addMouseListener(new MouseAdapter()
           {
               @Override
               public void mouseClicked(MouseEvent e)
               {
                   try
                   {
                       Desktop.getDesktop().browse(new URI(url));
                   } catch (URISyntaxException | IOException ex) {ex.printStackTrace();}
               }
           });
    }
}
