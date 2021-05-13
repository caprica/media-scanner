package uk.co.caprica.mediascanner.progress;

import uk.co.caprica.mediascanner.domain.MediaEntry;

public class MediaScannerProgressAdapter implements MediaScannerProgress {

    @Override
    public void found(MediaEntry entry) {
    }

    @Override
    public void beforeGetMeta(int current, int total, MediaEntry entry) {
    }

    @Override
    public void afterGetMeta(int current, int total, MediaEntry entry) {
    }
}
