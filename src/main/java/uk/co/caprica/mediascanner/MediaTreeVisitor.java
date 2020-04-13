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
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;

/**
 *
 */
final class MediaTreeVisitor implements FileVisitor<Path> {

    /**
     *
     */
    private final Path root;

    /**
     *
     */
    private final boolean followLinks;

    /**
     *
     */
    private final PathMatcher matcher;

    /**
     *
     */
    private final List<Path> result = new ArrayList<>(100);

    /**
     *
     */
    private final List<Path> failures = new ArrayList<>();

    /**
     *
     *
     * @param root
     * @param followLinks
     * @param matching
     */
    MediaTreeVisitor(File root, boolean followLinks, String matching) {
        this.root = root.toPath();
        this.followLinks = followLinks;
        this.matcher = FileSystems.getDefault().getPathMatcher(matching);
    }

    /**
     *
     * @return
     * @throws IOException
     */
    MediaTreeVisitor visit() throws IOException {
        Set<FileVisitOption> options = new HashSet<>(1);
        if (followLinks) {
            options.add(FileVisitOption.FOLLOW_LINKS);
        }
        Files.walkFileTree(root, options, Integer.MAX_VALUE, this);
        return this;
    }

    /**
     *
     *
     * @return
     */
    List<Path> result() {
        return ImmutableList.copyOf(result);
    }

    /**
     *
     *
     * @return
     */
    List<Path> failures() {
        return ImmutableList.copyOf(failures);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (matcher.matches(file)) {
            result.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        if (matcher.matches(file)) {
            failures.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
