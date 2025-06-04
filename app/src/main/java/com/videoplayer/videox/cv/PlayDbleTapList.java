package com.videoplayer.videox.cv;


public interface PlayDbleTapList {

    /* renamed from: com.videoplayer.videox.cv.PlayDbleTapList$CC */
    public static final class C4905CC {
        public static void $default$onDoubleTapFinished(PlayDbleTapList playerDoubleTapListener) {
        }

        public static void $default$onDoubleTapProgressDown(PlayDbleTapList playerDoubleTapListener, float f, float f2) {
        }

        public static void $default$onDoubleTapProgressUp(PlayDbleTapList playerDoubleTapListener, float f, float f2) {
        }

        public static void $default$onDoubleTapStarted(PlayDbleTapList playerDoubleTapListener, float f, float f2) {
        }
    }

    void onDoubleTapFinished();

    void onDoubleTapProgressDown(float f, float f2);

    void onDoubleTapProgressUp(float f, float f2);

    void onDoubleTapStarted(float f, float f2);
}
