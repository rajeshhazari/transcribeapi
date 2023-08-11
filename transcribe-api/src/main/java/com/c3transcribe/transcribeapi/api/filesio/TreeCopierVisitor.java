package com.c3transcribe.transcribeapi.api.filesio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * A {@code FileVisitor} that copies a file-tree ("cp -r")
 */

@Service
  public class TreeCopierVisitor implements FileVisitor<Path> {

    private static final Logger logger = LoggerFactory.getLogger(TreeCopierVisitor.class);

    private final Path source;
    private final Path target;
    private final boolean prompt;
    private final boolean preserve;

    TreeCopierVisitor(Path source, Path target, boolean prompt, boolean preserve) {
        this.source = source;
        this.target = target;
        this.prompt = prompt;
        this.preserve = preserve;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        // before visiting entries in a directory we copy the directory
        // (okay if directory already exists).
        CopyOption[] options = (preserve) ?
                new CopyOption[] { COPY_ATTRIBUTES } : new CopyOption[0];

        Path newDir = target.resolve(source.relativize(dir));
        try {
            Files.copy(dir, newDir, options);
        } catch (FileAlreadyExistsException x) {
            // ignore
        } catch (IOException x) {
            logger.error("Unable to create: {} : {}", newDir, x);
            return SKIP_SUBTREE;
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        copyFileWithPreserveOrOverWrite(file, target.resolve(source.relativize(file)),
                prompt, preserve);
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        // fix up modification time of directory when done
        if (exc == null && preserve) {
            Path newDir = target.resolve(source.relativize(dir));
            try {
                FileTime time = Files.getLastModifiedTime(dir);
                Files.setLastModifiedTime(newDir, time);
            } catch (IOException x) {
                logger.error("Unable to copy all attributes to:{} : {}", newDir, x);
            }
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        if (exc instanceof FileSystemLoopException) {
            logger.error("cycle detected: {} " , file);
        } else {
            logger.error("Unable to copy: {] : {} ", file, exc);
        }
        return CONTINUE;
    }

    /**
     * Copy source file to target location. If {@code prompt} is true then
     * overWrite user to overwrite target if it exists. The {@code preserve}
     * parameter determines if file attributes should be copied/preserved.
     */
     void copyFileWithPreserveOrOverWrite(Path source, Path target, boolean overWrite, boolean preserve) {
        CopyOption[] defaultOptions = new CopyOption[] {COPY_ATTRIBUTES , REPLACE_EXISTING, ATOMIC_MOVE } ;
        CopyOption[] options = (preserve) ? new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING } : defaultOptions;
        options = overWrite ? new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING}  : defaultOptions;

        if ( Files.notExists(target)) {
            try {
                Files.copy(source, target, options);
            } catch (IOException x) {
                logger.error(String.format("Unable to copy: %s: %s%n", source, x));
            }
        } else if( overWrite &&  Files.exists(target) ) {
            try {
                Files.copy(source, target, options);
            } catch (IOException x) {
                logger.error(String.format("Unable to copy: %s: %s%n", source, x));
            }
        }
    }
}
