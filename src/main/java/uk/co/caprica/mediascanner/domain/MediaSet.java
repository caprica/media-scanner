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

import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 *
 */
public final class MediaSet implements Iterable<MediaEntry> {

    /**
     *
     */
    private final Collection<MediaEntry> entries;

    /**
     *
     *
     * @param entries
     */
    public MediaSet(Collection<MediaEntry> entries) {
        this.entries = ImmutableList.copyOf(entries);
    }

    /**
     *
     *
     * @return
     */
    public Collection<MediaEntry> entries() {
        return entries;
    }

    /**
     *
     *
     * @return
     */
    public int size() {
        return entries.size();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            entries
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MediaSet)) {
            return false;
        }
        MediaSet other = (MediaSet) obj;
        return
            Objects.equal(entries, other.entries);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("entries", entries.size())
            .toString();
    }

    @Override
    public Iterator<MediaEntry> iterator() {
        return Iterators.unmodifiableIterator(entries.iterator());
    }
}
