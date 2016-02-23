package com.doglandia.medialoader.clientdiscovery;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
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

    public interface OnHostFoundListener{
        void onHostFound(String host);
        void onProgressUpdate(int progress);
    }

//    private ResourceServer resourceServer;

    private OnHostFoundListener listener;

    public ClientDiscoverer(Context context, OnHostFoundListener onHostFoundListener){
        this.listener = onHostFoundListener;

        DiscoveryTask discoveryTask = new DiscoveryTask();
        discoveryTask.execute(new Void[0]);

//       NsdManager nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
//        nsdManager.discoverServices("media_loader", NsdManager.PROTOCOL_DNS_SD, new NsdManager.DiscoveryListener() {
//            @Override
//            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
//
//            }
//
//            @Override
//            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
//
//            }
//
//            @Override
//            public void onDiscoveryStarted(String serviceType) {
//
//            }
//
//            @Override
//            public void onDiscoveryStopped(String serviceType) {
//
//            }
//
//            @Override
//            public void onServiceFound(NsdServiceInfo serviceInfo) {
//                serviceInfo.
//            }
//
//            @Override
//            public void onServiceLost(NsdServiceInfo serviceInfo) {
//
//            }
//        });
    }


    class DiscoveryTask extends AsyncTask<Void, Integer, String>{

        private OkHttpClient client;

        DiscoveryTask(){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(2, TimeUnit.SECONDS);
            client = builder.build();
        }

        @Override
        protected String doInBackground(Void... params) {

            String hostName = scanSubNet("192.168.1.");

            

            return hostName;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onHostFound(s);
        }

        private String scanSubNet(String subnet){
            ArrayList<String> hosts = new ArrayList<>();

            InetAddress inetAddress = null;
            for(int i=1; i<254; i++){
                Log.d(TAG, "Trying: " + subnet + String.valueOf(i));
                try {
                    inetAddress = InetAddress.getByName(subnet + String.valueOf(i));
                    if(inetAddress.isReachable(200)){
                        hosts.add(inetAddress.getHostName());
                        Log.d(TAG, inetAddress.getHostName() + "is reachable");
                        if(hostAcceptsRequest(inetAddress.getHostName())){
                            return inetAddress.getHostName();
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        private boolean hostAcceptsRequest(String hostName) throws IOException {

            Request request = new Request.Builder()
                    .url(hostName+":8080")
                    .build();
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            if(body.contains("\"status\":200")){
                return true;
            }else{
                return false;
            }
        }
    }
}
