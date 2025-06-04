package com.videoplayer.videox.db.entity.video;


public class VideoDownload {
    private int id;
    private String name;
    private String path;
    private long receivedBytes;
    private long totalBytes;

    public long getTotalBytes() {
        long j = this.totalBytes;
        if (j == 0) {
            return 1L;
        }
        return j;
    }

    public void setTotalBytes(long j) {
        this.totalBytes = j;
    }

    public long getReceivedBytes() {
        long j = this.receivedBytes;
        if (j == 0) {
            return 1L;
        }
        return j;
    }

    public void setReceivedBytes(long j) {
        this.receivedBytes = j;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass() && ((VideoDownload) obj).getId() == this.id;
    }
}
