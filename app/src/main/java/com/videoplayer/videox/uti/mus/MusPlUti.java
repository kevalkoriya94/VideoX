package com.videoplayer.videox.uti.mus;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;


import com.videoplayer.videox.db.datasource.MusicDatabaseControl;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.uti.vid.VidPlayUti;


public class MusPlUti {
    public static MusicInfo getMusicInfoFromUri(Context context, Uri uri) {
        String realPathFromURI = getRealPathFromURI(context, uri);
        MusicInfo musicByFilePath = realPathFromURI != null ? MusicDatabaseControl.getInstance().getMusicByFilePath(realPathFromURI) : null;
        if (musicByFilePath != null) {
            return musicByFilePath;
        }
        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setUri(uri.toString());
        musicInfo.setDisplayName(VidPlayUti.getFileName(context, uri));
        return musicInfo;
    }

    private static String getRealPathFromURI(Context context, Uri uri) {
        try {
            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            if (query == null) {
                return uri.getPath();
            }
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex("_data"));
            query.close();
            return string;
        } catch (Exception unused) {
            return null;
        }
    }

    public static int dpToPx(int i) {
        return (int) (i * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Bitmap getThumbnailOfSong(Context context, String str, int i) {
        int dpToPx = dpToPx(i);
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, Uri.parse(str));
            byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
            if (embeddedPicture != null) {
                return decodeSampledBitmapFromResource(embeddedPicture, dpToPx, dpToPx);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] bArr, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static String getTimeFormatted(long j) {
        String str;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        String str2 = i > 0 ? i + ":" : "";
        if (i3 < 10) {
            str = "0" + i3;
        } else {
            str = "" + i3;
        }
        return str2 + i2 + ":" + str;
    }
}
