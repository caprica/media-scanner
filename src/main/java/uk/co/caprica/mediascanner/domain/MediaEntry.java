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

package uk.co.caprica.mediascanner.domain;

import java.nio.file.Path;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Multimap;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 *
 */
public final class MediaEntry implements Comparable<MediaEntry> {

    /**
     *
     */
    private final Path file;

    /**
     *
     */
    private final MediaTitle title;

    /**
     *
     */
    private final Multimap<String, Object> meta = ArrayListMultimap.create();

    /**
     *
     *
     * @param file
     * @param title
     */
    public MediaEntry(Path file, MediaTitle title) {
        this.file = file;
        this.title = title;
    }

    /**
     *
     *
     * @param key
     * @param type
     * @return
     */
    public <T> T value(String key, Class<T> type) {
        List<T> values = values(key, type);
        return !values.isEmpty() ? values.get(0) : null;
    }

    /**
     *
     *
     * @param key
     * @param type
     * @return
     */
    public <T> List<T> values(String key, Class<T> type) {
        return (List<T>) meta.get(key);
    }

    /**
     *
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        meta.put(key, value);
    }

    /**
     *
     *
     * @return
     */
    public Path file() {
        return file;
    }

    /**
     *
     *
     * @return
     */
    public MediaTitle title() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            file,
            title,
            meta
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MediaEntry)) {
            return false;
        }
        MediaEntry other = (MediaEntry) obj;
        return
            Objects.equal(file , other.file ) &&
            Objects.equal(title, other.title) &&
            Objects.equal(meta , other.meta );
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("file" , file )
            .add("title", title)
            .add("meta" , meta )
            .toString();
    }

    @Override
    public int compareTo(MediaEntry o) {
        return ComparisonChain.start()
            .compare(title, o.title)
            .result();
    }
}
