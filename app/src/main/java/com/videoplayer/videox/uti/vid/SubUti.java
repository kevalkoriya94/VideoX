package com.videoplayer.videox.uti.vid;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import androidx.documentfile.provider.DocumentFile;

import com.google.android.exoplayer2.util.MimeTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class SubUti {
    public static Uri convertToUTF(Context context, Uri uri) {
        return uri;
    }

    public static void cacheSubtitleFileUri(Context context, long j, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("SUBTITLE_URI", 0).edit();
        edit.putString(String.valueOf(j), str);
        edit.apply();
    }

    public static String getSubtitleFileUri(Context context, long j) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SUBTITLE_URI", 0);
        return sharedPreferences != null ? sharedPreferences.getString(String.valueOf(j), "") : "";
    }

    public static String getSubtitleMime(Uri uri) {
        String path = uri.getPath();
        return TextUtils.isEmpty(path) ? "application/x-subrip" : (path.endsWith(".ssa") || path.endsWith(".ass")) ? MimeTypes.TEXT_SSA : path.endsWith(".vtt") ? MimeTypes.TEXT_VTT : (path.endsWith(".ttml") || path.endsWith(".xml") || path.endsWith(".dfxp")) ? MimeTypes.APPLICATION_TTML : "application/x-subrip";
    }

    public static String getSubtitleLanguage(Uri uri) {
        String path = uri.getPath();
        if (TextUtils.isEmpty(path) || !path.endsWith(".srt")) {
            return "en";
        }
        int lastIndexOf = path.lastIndexOf(".");
        int i = lastIndexOf;
        int i2 = i;
        while (i >= 0) {
            i2 = path.indexOf(".", i);
            if (i2 != lastIndexOf) {
                break;
            }
            i--;
        }
        int i3 = lastIndexOf - i2;
        return (i3 < 2 || i3 > 6) ? "en" : path.substring(i2 + 1, lastIndexOf);
    }

    public static DocumentFile findUriInScope(Context context, Uri uri, Uri uri2) {
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, uri);
        String[] trailFromUri = getTrailFromUri(uri);
        String[] trailFromUri2 = getTrailFromUri(uri2);
        int i = 0;
        while (i < trailFromUri2.length) {
            if (i >= trailFromUri.length) {
                fromTreeUri = fromTreeUri.findFile(trailFromUri2[i]);
                if (fromTreeUri == null) {
                    return null;
                }
            } else if (!trailFromUri[i].equals(trailFromUri2[i])) {
                return null;
            }
            i++;
            if (i == trailFromUri2.length) {
                return fromTreeUri;
            }
        }
        return null;
    }

    public static DocumentFile findDocInScope(DocumentFile documentFile, DocumentFile documentFile2) {
        for (DocumentFile documentFile3 : documentFile.listFiles()) {
            if (documentFile3.isDirectory()) {
                DocumentFile findDocInScope = findDocInScope(documentFile3, documentFile2);
                if (findDocInScope != null) {
                    return findDocInScope;
                }
            } else if (documentFile2.length() == documentFile3.length() && documentFile2.getName().equals(documentFile3.getName())) {
                return documentFile3;
            }
        }
        return null;
    }

    public static String[] getTrailFromUri(Uri uri) {
        String[] split = uri.getPath().split(":");
        return split.length > 1 ? split[1].split("/") : new String[0];
    }

    private static String getFileBaseName(String str) {
        return str.indexOf(".") > 0 ? str.substring(0, str.lastIndexOf(".")) : str;
    }

    public static DocumentFile findSubtitle(DocumentFile documentFile) {
        return findSubtitle(documentFile, documentFile.getParentFile());
    }

    public static DocumentFile findSubtitle(DocumentFile documentFile, DocumentFile documentFile2) {
        String fileBaseName = getFileBaseName(documentFile.getName());
        if (documentFile2 == null || !documentFile2.isDirectory()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (DocumentFile documentFile3 : documentFile2.listFiles()) {
            if (isSubtitleFile(documentFile3)) {
                arrayList.add(documentFile3);
            }
            if (isVideoFile(documentFile3)) {
                i++;
            }
        }
        if (i == 1 && arrayList.size() == 1) {
            return (DocumentFile) arrayList.get(0);
        }
        if (arrayList.size() < 1) {
            return null;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            DocumentFile documentFile4 = (DocumentFile) it.next();
            if (documentFile4.getName().startsWith(fileBaseName + '.')) {
                return documentFile4;
            }
        }
        return null;
    }

    public static boolean isVideoFile(DocumentFile documentFile) {
        return documentFile.isFile() && documentFile.getType().startsWith("video/");
    }

    public static boolean isSubtitleFile(DocumentFile documentFile) {
        if (!documentFile.isFile()) {
            return false;
        }
        String name = documentFile.getName();
        return name.endsWith(".srt") || name.endsWith(".ssa") || name.endsWith(".ass") || name.endsWith(".vtt") || name.endsWith(".ttml");
    }

    public static void clearCache(Context context) {
        try {
            for (File file : context.getCacheDir().listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
