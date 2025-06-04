package com.videoplayer.videox.uti;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import com.appizona.yehiahd.fastsave.FastSave;
import com.videoplayer.videox.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;


public abstract class VidContSea extends Thread {
    private static final String AUDIO = "audio";
    private static final String LENGTH = "content-length";
    private static final String METACAFE = "metacafe.com";
    private static final String MYSPACE = "myspace.com";
    private static final String TWITTER = "twitter.com";
    private static final String VIDEO = "video";
    private static final String YOUTUBE = "youtube.com";
    private final Context context;
    private int numLinksInspected = 0;
    private final String page;
    private final String title;
    private final String url;

    public abstract void onFinishedInspectingURL(boolean finishedAll);

    public abstract void onStartInspectingURL();

    public abstract void onVideoFound(String size, String type, String link, String name, String page, boolean chunked, String website, boolean audio);

    public VidContSea(Context context, String url, String page, String title) {
        this.context = context;
        this.url = url;
        this.page = page;
        this.title = title;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean z;
        String headerField;
        String lowerCase = this.url.toLowerCase();
        String[] stringArray = this.context.getResources().getStringArray(R.array.videourl_filters);
        int length = stringArray.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else {
                if (lowerCase.contains(stringArray[i])) {
                    z = true;
                    break;
                }
                i++;
            }
        }
        if (z) {
            this.numLinksInspected++;
            onStartInspectingURL();
            URLConnection uRLConnection = null;
            try {
                uRLConnection = new URL(this.url).openConnection();
                uRLConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (uRLConnection != null && (headerField = uRLConnection.getHeaderField("content-type")) != null) {
                String lowerCase2 = headerField.toLowerCase();
                if (lowerCase2.contains("video") || lowerCase2.contains("audio")) {
                    addVideoToList(uRLConnection, this.page, this.title, lowerCase2);
                } else if (lowerCase2.equals("application/x-mpegurl") || lowerCase2.equals("application/vnd.apple.mpegurl")) {
                    addVideosToListFromM3U8(uRLConnection, this.page, this.title);
                }
            }
            int i2 = this.numLinksInspected - 1;
            this.numLinksInspected = i2;
            onFinishedInspectingURL(i2 <= 0);
        }
    }

    private void addVideoToList(URLConnection uCon, String page, String title, String contentType) {
        try {
            String size = uCon.getHeaderField(LENGTH);
            String link = uCon.getHeaderField("Location");
            if (link == null) {
                link = uCon.getURL().toString();
            }

            String host = new URL(page).getHost();
            String website = null;
            boolean chunked = false;
            String type;
            boolean audio = false;

            if (host.contains(TWITTER) && contentType.equals("video/mp2t")) {
                return;
            }

            String name = VIDEO;
            if (title != null) {
                if (contentType.contains(AUDIO)) {
                    name = "[AUDIO ONLY]" + title;
                } else {
                    name = title;
                }
            } else if (contentType.contains(AUDIO)) {
                name = AUDIO;
            }

            if (host.contains(YOUTUBE) || (new URL(link).getHost().contains("googlevideo.com")
            )) {

                int r = link.lastIndexOf("&range");
                if (r > 0) {
                    link = link.substring(0, r);
                }
                URLConnection ytCon;
                ytCon = new URL(link).openConnection();
                ytCon.connect();
                size = ytCon.getHeaderField(LENGTH);

                if (host.contains(YOUTUBE)) {
                    URL embededURL = new URL("http://www.youtube.com/oembed?url=" + page +
                            "&format=json");
                    try {
                        String jSonString;
                        InputStream in = embededURL.openStream();

                        InputStreamReader inReader = new InputStreamReader(in, Charset
                                .defaultCharset());
                        StringBuilder sb = new StringBuilder();
                        char[] buffer = new char[1024];
                        int read;
                        while ((read = inReader.read(buffer)) != -1) {
                            sb.append(buffer, 0, read);
                        }
                        jSonString = sb.toString();

                        name = new JSONObject(jSonString).getString("title");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (contentType.contains(VIDEO)) {
                        name = "[VIDEO ONLY]" + name;
                    } else if (contentType.contains(AUDIO)) {
                        name = "[AUDIO ONLY]" + name;
                    }
                    website = YOUTUBE;
                }
            } else if (host.contains("dailymotion.com")) {
                chunked = true;
                website = "dailymotion.com";
                link = link.replaceAll("(frag\\()+(\\d+)+(\\))", "FRAGMENT");
                size = null;
            } else if (host.contains("vimeo.com") && link.endsWith("m4s")) {
                chunked = true;
                website = "vimeo.com";
                link = link.replaceAll("(segment-)+(\\d+)", "SEGMENT");
                size = null;
            } else if (host.contains("facebook.com") && link.contains("bytestart")) {
                int b = link.lastIndexOf("&bytestart");
                int f = link.indexOf("fbcdn");
                if (b > 0) {
                    link = "https://video.xx." + link.substring(f, b);
                }
                URLConnection fbCon;
                fbCon = new URL(link).openConnection();
                fbCon.connect();
                size = fbCon.getHeaderField(LENGTH);
                website = "facebook.com";

                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(link, new HashMap<>());
                    retriever.release();
                    audio = false;
                } catch (RuntimeException ex) {
                    audio = true;
                }
            } else if (host.contains("instagram.com")) {
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(link, new HashMap<>());
                    retriever.release();
                    audio = false;
                } catch (RuntimeException ex) {
                    audio = true;
                }
            }

            switch (contentType) {
                case "video/webm":
                case "audio/webm":
                    type = "webm";
                    break;
                case "video/mp2t":
                    type = "ts";
                    break;
                case "video/mp4":
                default:
                    type = "mp4";
                    break;
            }

            onVideoFound(size, type, link, name, page, chunked, website, audio);
        } catch (IOException e) {
            //nada
        }
    }

    private void addVideosToListFromM3U8(URLConnection uCon, String page, String title) {
        String str;
        String str2;
        String str3 = TWITTER;
        try {
            String host = new URL(page).getHost();
            if (!host.contains(TWITTER) && !host.contains(METACAFE) && !host.contains(MYSPACE)) {
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
            String str4 = title != null ? title : "video";
            if (host.contains(TWITTER)) {
                str = FastSave.getInstance().getString("twimg", "");
                str2 = "ts";
            } else if (host.contains(METACAFE)) {
                String url = uCon.getURL().toString();
                str = url.substring(0, url.lastIndexOf("/") + 1);
                str2 = "mp4";
                str3 = METACAFE;
            } else if (host.contains(MYSPACE)) {
                onVideoFound(null, "ts", uCon.getURL().toString(), str4, page, true, MYSPACE, false);
                return;
            } else {
                str3 = null;
                str = null;
                str2 = null;
            }
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return;
                }
                if (readLine.endsWith(".m3u8")) {
                    onVideoFound(null, str2, str + readLine, str4, page, true, str3, false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
