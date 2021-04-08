package org.me.gcu.equakestartercode;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadData {

    public static void GetXmlFromUrl(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               try {
                   URL url = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");

                   Log.e("DownloadData","StartingDownload");
                   URLConnection oc = url.openConnection();

                   File path = new File("newdata.xml");
                   FileOutputStream fos = new FileOutputStream(path,true);

                   InputStream is = oc.getInputStream();
                   BufferedInputStream bis = new BufferedInputStream(is);
                   Log.d("DownloadData","connection is now opened");
                   BufferedOutputStream bos = new BufferedOutputStream(fos);
                   Log.d("DownloadData","file output stream and buffered output stream are open");

                   byte data [] = new byte[1024];
                   int count;
                   while((count = bis.read(data)) != -1){
                       bos.write(data,0,count);
                   }
                   bos.flush();
                   bos.close();
                   is.close();
                   fos.close();
                   bis.close();

                   Log.d("DownloadData","Download finished \nFile output stream and buffered outputstream are closed");



               } catch (IOException e) {
                   e.printStackTrace();
               }
            }
        });
        thread.start();

    }

}
