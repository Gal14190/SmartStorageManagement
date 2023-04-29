package edu.umich.eecs.april.apriltag.helper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import edu.umich.eecs.april.apriltag.model.Model;

public class LiveServer extends Thread {
    private final String ip = "10.0.0.15";
    private final int PORT = 4000;
    private final String PATH = "update";
    private final String KEY = "id";

    public LiveServer() {
    }

    @Override
    public void run() {
        super.run();
        if(Model.isDetected()) {
            int id = Model.getItemIdDetected();
            if(id != -1)
                this.update(id);
        }
    }

    public void update(int id) {
        String API_URL = String.format("http://%s:%d/%s?%s=%d", ip, PORT, PATH, KEY, id);

        try {
            java.net.URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "*");

            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // Handle your response here
                String jsonResponse = response.toString();
            } else {
                // Handle HTTP error
            }

            conn.disconnect();
        }catch (Exception e) {
            Log.e("LiveServer", e.getMessage());
        }
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }
}
