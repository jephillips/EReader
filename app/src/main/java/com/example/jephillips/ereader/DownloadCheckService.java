package com.example.jephillips.ereader;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

public class DownloadCheckService extends IntentService {

    private static final String OUR_BOOK_DATE = "20150403";
    private static final String UPDATE_FILENAME = "book.zip";
    public static final String UPDATE_BASEDIR = "updates";

    public DownloadCheckService() {
        super("DownloadCheckService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String url = getUpdateUrl();
            if (url != null) {
                File book = download(url);
                File updateDir = new File(getFilesDir(), UPDATE_BASEDIR);

                updateDir.mkdirs();
                unzip(book, updateDir);
                book.delete();
                EventBus.getDefault().post(new BookUpdatedEvent());

            }
        }
        catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Exception downloading update", e);
        }

    }

    private String getUpdateUrl() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://commonsware.com")
                .build();

        BookUpdateInterface updateInterface = restAdapter.create(BookUpdateInterface.class);
        BookUpdateInfo info = updateInterface.update();
        if (info.updatedOn.compareTo(OUR_BOOK_DATE) > 0) {
            return (info.updateURL);
        }

        return(null);
    }

    private File download(String url) throws MalformedURLException, IOException {

        File output = new File(getFilesDir(), UPDATE_FILENAME);

        if(output.exists()) {
            output.delete();
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        FileOutputStream fos = new FileOutputStream(output.getPath());
        BufferedOutputStream out = new BufferedOutputStream(fos);

        try {
            InputStream in = connection.getInputStream();
            byte[] buffer = new byte[16384];
            int len = 0;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } finally {
            fos.getFD().sync();
            out.close();
            connection.disconnect();
        }
        return output;
    }

    private static void unzip(File src, File dest) throws IOException {
        InputStream inputStream = new FileInputStream(src);
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
        ZipEntry zipEntry;
        dest.mkdirs();

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            byte[] buffer = new byte[16384];
            int count;
            FileOutputStream fileOutputStream = new FileOutputStream
                    (new File(dest, zipEntry.getName()));
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);

            try {
                while ((count = zipInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.flush();
            } finally {
                fileOutputStream.getFD().sync();
                outputStream.close();
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
    }
}
