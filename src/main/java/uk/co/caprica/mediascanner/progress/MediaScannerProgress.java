package uk.co.caprica.mediascanner.progress;

import uk.co.caprica.mediascanner.domain.MediaEntry;

public interface MediaScannerProgress {

    void found(MediaEntry entry);

    void beforeGetMeta(int current, int total, MediaEntry entry);

    void afterGetMeta(int current, int total, MediaEntry entry);
}
