package com.videoplayer.videox.uti.down;

import java.util.regex.Pattern;


public class PatProv {
    private static Pattern getDefaultSrcPattern;
    private static Pattern getHDPattern;
    private static Pattern getHDPattern2;
    private static Pattern getIDPattern;
    private static Pattern getSDPattern;
    private static Pattern getSDPattern2;

    private PatProv() {
    }

    public static Pattern getIDPatternInstance() {
        if (getIDPattern == null) {
            getIDPattern = Pattern.compile("videoID:(\\w+)");
        }
        return getIDPattern;
    }

    public static Pattern getSDPatternInstance() {
        if (getSDPattern == null) {
            getSDPattern = Pattern.compile("sd_src_no_ratelimit:\"([^\"]+)\"");
        }
        return getSDPattern;
    }

    public static Pattern getHDPatternInstance() {
        if (getHDPattern == null) {
            getHDPattern = Pattern.compile("hd_src:\"([^\"]+)\"");
        }
        return getHDPattern;
    }

    public static Pattern getSDPatternInstance2() {
        if (getSDPattern2 == null) {
            getSDPattern2 = Pattern.compile("sd_src:\"([^\"]+)\"");
        }
        return getSDPattern2;
    }

    public static Pattern getHDPatternInstance2() {
        if (getHDPattern2 == null) {
            getHDPattern2 = Pattern.compile("hd_src_no_ratelimit:\"([^\"]+)\"");
        }
        return getHDPattern2;
    }

    public static Pattern getDefaultSrcPatternInstance() {
        if (getDefaultSrcPattern == null) {
            getDefaultSrcPattern = Pattern.compile("\"src\":\"([^\"]+)\"");
        }
        return getDefaultSrcPattern;
    }
}
