package com.doglandia.medialoader.clientdiscovery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.doglandia.medialoader.resourceserver.ResourceServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tdk10 on 2/22/2016.
 */
public class ClientDiscoverer {

    private static final String TAG = "ClientDiscoverer";

    private static final String LAST_DISCOVERED_HOST = "last_discovered_host";

    public interface OnHostFoundListener{
        void onHostFound(String host);
        void onNoHostFound();
        void onProgressUpdate(int progress);

    }

//    private ResourceServer resourceServer;

    private OnHostFoundListener listener;

    private Context context;

    private OkHttpClient client;

    public ClientDiscoverer(Context context, OnHostFoundListener onHostFoundListener){
        this.context = context;
        this.listener = onHostFoundListener;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(500, TimeUnit.MILLISECONDS);
        client = builder.build();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.contains(LAST_DISCOVERED_HOST)){
            HostCheckTask hostCheckTask = new HostCheckTask();
            hostCheckTask.execute(prefs.getString(LAST_DISCOVERED_HOST,""));
        }else {
            DiscoveryTask discoveryTask = new DiscoveryTask();
            discoveryTask.execute(new Void[0]);
        }

//       NsdManager nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    boolean hostAcceptsRequest(String hostName) throws IOException {

        Request request = new Request.Builder()
                .url("http://"+hostName+":8989/ping")
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if(body.contains("\"status\":200")){
            return true;
        }else{
            return false;
        }
    }

    class HostCheckTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                if(hostAcceptsRequest(params[0])){
                    return params[0];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String host) {
            super.onPostExecute(host);
            if(host != null){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putString(LAST_DISCOVERED_HOST,host).commit();
                listener.onHostFound(host);
            }else{
                DiscoveryTask discoveryTask = new DiscoveryTask();
                discoveryTask.execute(new Void[0]);
            }
        }
    }


    class DiscoveryTask extends AsyncTask<Void, Integer, String>{

        @Override
        protected String doInBackground(Void... params) {

            String hostName = scanSubNet("192.168.0."); // todo

            return hostName;
        }

        @Override
        protected void onPostExecute(String host) {
            super.onPostExecute(host);
            if(host != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putString(LAST_DISCOVERED_HOST, host).commit();
                listener.onHostFound(host);
            }else{
                listener.onNoHostFound();
            }
        }

        private String scanSubNet(String subnet){
//            ArrayList<String> hosts = new ArrayList<>();

            InetAddress inetAddress = null;
            for(int i=1; i<254; i++){
                Log.d(TAG, "Trying: " + subnet + String.valueOf(i));
                try {
                    inetAddress = InetAddress.getByName(subnet + String.valueOf(i));
//                    if(inetAddress.isReachable(200)){
//                        hosts.add(inetAddress.getHostName());
//                        Log.d(TAG, inetAddress.getHostName() + "is reachable");
                        if(hostAcceptsRequest(inetAddress.getHostName())){
                            return inetAddress.getHostName();
                        }
//                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(TAG, "IO exception, failed on address "+inetAddress.getHostName());
//                    e.printStackTrace();
                }
            }

            return null;
        }

    }


}
