/*
 * This file is part of media-scanner.
 *
 * media-scanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * media-scanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with media-scanner.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2014 Caprica Software Limited.
 */

package uk.co.caprica.mediascanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import uk.co.caprica.mediascanner.domain.MediaEntry;
import uk.co.caprica.mediascanner.domain.MediaSet;
import uk.co.caprica.mediascanner.domain.MediaTitle;
import uk.co.caprica.mediascanner.meta.MetaProvider;
import uk.co.caprica.mediascanner.progress.MediaScannerProgress;
import uk.co.caprica.mediascanner.progress.MediaScannerProgressAdapter;
import uk.co.caprica.mediascanner.title.DefaultTitleProvider;
import uk.co.caprica.mediascanner.title.TitleProvider;

import com.google.common.collect.ImmutableList;

// FIXME this probably should e.g. scan meta in FILESYSTEM order - e.g. to minimise impact of switching network drives all the time

/**
 *
 */
public final class MediaScanner {

    /**
     *
     */
    private static final int INITIAL_RESULT_SIZE = 500;

    /**
     *
     */
    private final ServiceLoader<TitleProvider> titleProviderServiceLoader = ServiceLoader.load(TitleProvider.class);

    /**
     *
     */
    private final DefaultTitleProvider defaultTitleProvider = new DefaultTitleProvider();

    /**
     *
     */
    private final ServiceLoader<MetaProvider> metaProviderServiceLoader = ServiceLoader.load(MetaProvider.class);

    /**
     *
     */
    private final List<File> directories = new ArrayList<>();

    /**
     *
     */
    private boolean followLinks;

    /**
     * File matching pattern.
     * <p>
     * For example: <code>glob:**&#47;*.mp3</code>
     */
    private String matching = "glob:**/*.*";

    /**
     *
     */
    private Object context;

    /**
     *
     */
    private MediaScannerProgress progress = new MediaScannerProgressAdapter();

    /**
     *
     */
    private MediaSet mediaSet;

    /**
     *
     */
    private List<Path> failures;

    /**
     *
     */
    private MediaScanner() {
    }

    /**
     *
     *
     * @return
     */
    public static MediaScanner create() {
        return new MediaScanner();
    }

    /**
     *
     *
     * @param directories
     * @return
     */
    public MediaScanner directory(File... directories) {
        this.directories.addAll(Arrays.asList(directories));
        return this;
    }

    /**
     *
     *
     * @param directories
     * @return
     */
    public MediaScanner directory(String... directories) {
        File[] files = new File[directories.length];
        for (int i = 0; i < directories.length; i++) {
            files[i] = new File(directories[i]);
        }
        return directory(files);
    }

    /**
     *
     *
     * @param directories
     * @return
     */
    public MediaScanner directories(Collection<String> directories) {
        String[] sa = new String[directories.size()];
        return directory(directories.toArray(sa));
    }

    /**
     *
     *
     * @return
     */
    public MediaScanner followLinks() {
       this.followLinks = true;
       return this;
    }

    /**
     *
     *
     * @param matching
     * @return
     */
    public MediaScanner matching(String matching) {
        this.matching = matching;
        return this;
    }

    /**
     *
     *
     * @param context
     * @return
     */
    public MediaScanner context(Object context) {
        this.context = context;
        return this;
    }

    /**
     *
     *
     * @param progress
     * @return
     */
    public MediaScanner progress(MediaScannerProgress progress) {
        this.progress = progress;
        return this;
    }

    /**
     *
     *
     * @return
     * @throws IOException
     */
    public MediaScanner findMedia() throws IOException {
        List<MediaEntry> entries = new ArrayList<>(INITIAL_RESULT_SIZE);
        List<Path>failures = new ArrayList<>();
        for (File directory : directories) {
            List<Path> files = new MediaTreeVisitor(directory, followLinks, matching).visit().result();
            for (Path file : files) {
                try {
                    MediaEntry entry = new MediaEntry(file, getTitle(file));
                    entries.add(entry);
                    progress.found(entry);
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                    failures.add(file);
                }
            }
        }
        Collections.sort(entries);
        this.mediaSet = new MediaSet(entries);
        this.failures = ImmutableList.copyOf(failures);
        return this;
    }

    /**
     *
     *
     * @return
     */
    public MediaScanner collectMeta() {
        int i = 0;
        for (MediaEntry entry : mediaSet) {
            i++;
            progress.beforeGetMeta(i, mediaSet.size(), entry);
            Iterator<MetaProvider> it = metaProviderServiceLoader.iterator();
            while (it.hasNext()) {
                it.next().addMeta(entry, context);
            }
            progress.afterGetMeta(i, mediaSet.size(), entry);
        }
        return this;
    }

    /**
     *
     *
     * @return
     */
    public MediaSet mediaSet() {
        return mediaSet;
    }

    /**
     *
     *
     * @return
     */
    public List<Path> failures() {
        return failures;
    }

    /**
     *
     *
     * @param file
     * @return
     */
    private MediaTitle getTitle(Path file) {
        MediaTitle mediaTitle = null;
        Iterator<TitleProvider> it = titleProviderServiceLoader.iterator();
        while (it.hasNext()) {
            mediaTitle = it.next().getTitle(file);
            if (mediaTitle != null) {
                break;
            }
        }
        if (mediaTitle == null) {
            mediaTitle = defaultTitleProvider.getTitle(file);
        }
        return mediaTitle;
    }
}
