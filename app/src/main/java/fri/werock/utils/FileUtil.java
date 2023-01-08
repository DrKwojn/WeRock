package fri.werock.utils;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtil {
    public static MultipartBody.Part fileRequestBody(InputStream inputStream, String filename, String extension, String mediaType) {
        File file = null;
        try {
            file = File.createTempFile(filename, extension);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copyStream(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestBody);
    }
}
