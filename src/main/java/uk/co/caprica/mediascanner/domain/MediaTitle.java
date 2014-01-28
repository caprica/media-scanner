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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ComparisonChain;

/**
 *
 */
public final class MediaTitle implements Comparable<MediaTitle> {

    /**
     *
     */
    private final String title;

    /**
     *
     */
    private final Optional<Integer> year;

    /**
     *
     *
     * @param title
     * @param year
     */
    public MediaTitle(String title, Integer year) {
        this.title = title;
        this.year = Optional.fromNullable(year);
    }

    /**
     *
     *
     * @param title
     */
    public MediaTitle(String title) {
        this(title, null);
    }

    /**
     *
     *
     * @return
     */
    public String title() {
        return title;
    }

    /**
     *
     *
     * @return
     */
    public Integer year() {
        return year.orNull();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            title,
            year
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MediaTitle)) {
            return false;
        }
        MediaTitle other = (MediaTitle) obj;
        return
            Objects.equal(title, other.title) &&
            Objects.equal(year , other.year );
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("title", title)
            .add("year" , year )
            .toString();
    }

    @Override
    public int compareTo(MediaTitle o) {
        return ComparisonChain.start()
            .compare(title, o.title)
            .compare(year.or(-1), o.year.or(-1))
            .result();
    }
}