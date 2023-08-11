package com.c3transcribe.transcribeapi.api.filesio;


/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Sample code that copies files in a similar manner to the cp(1) program.
 */

@Service
public class FilesCopyManager {

    private static final Logger logger = LoggerFactory.getLogger(FilesCopyManager.class);

    @Autowired
    TreeCopierVisitor treeCopierVisitor;





/*    static void usage() {
        // -r for recursive
        // -i is for interactive
        // -p is for preserve source attributes

        logger.warn(String.format("java FilesCopyManager [-ip] source... target"));
        logger.warn(String.format("java FilesCopyManager -r [-ip] source-dir... target"));
        System.exit(1);
    }*/

    public  void copy(String[] args) throws IOException {
        boolean recursive = false;
        boolean prompt = false;
        boolean preserve = false;

        // process options
        int argi = 0;
        while (argi < args.length) {
            String arg = args[argi];
            if (!arg.startsWith("-"))
                break;
            if (arg.length() < 2)
                usage();
            for (int i=1; i<arg.length(); i++) {
                char c = arg.charAt(i);
                switch (c) {
                    case 'r' : recursive = true; break;
                    case 'i' : prompt = true; break;
                    case 'p' : preserve = true; break;
                    default : usage();
                }
            }
            argi++;
        }

        // remaining arguments are the source files(s) and the target location
        int remaining = args.length - argi;
        if (remaining < 2)
            usage();
        Path[] source = new Path[remaining-1];
        int i=0;
        while (remaining > 1) {
            source[i++] = Paths.get(args[argi++]);
            remaining--;
        }
        Path target = Paths.get(args[argi]);

        // check if target is a directory
        boolean isDir = Files.isDirectory(target);

        // copy each source file/directory to target
        boolean finalRecursive = recursive;
        boolean finalPrompt = prompt;
        boolean finalPreserve = preserve;
        Arrays.stream(source).forEach(
                path -> {
                    Path dest = (isDir) ? target.resolve(path.getFileName()) : target;

                    if (finalRecursive) {
                        // follow links when copying files
                        EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                        TreeCopierVisitor tc = new TreeCopierVisitor(path, dest, finalPrompt, finalPreserve);
                        try {
                            Files.walkFileTree(path, opts, Integer.MAX_VALUE, tc);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // not recursive so source must not be a directory
                        if (Files.isDirectory(path)) {
                            logger.warn(" {} is a directory ", path);
                        } else {
                            treeCopierVisitor.copyFileWithPreserveOrOverWrite(path, dest, finalPrompt, finalPreserve);
                        }
                    }
                }
        );
        /*for (i=0; i<source.length; i++) {
            Path dest = (isDir) ? target.resolve(source[i].getFileName()) : target;

            if (recursive) {
                // follow links when copying files
                EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                TreeCopierVisitor tc = new TreeCopierVisitor(source[i], dest, prompt, preserve);
                Files.walkFileTree(source[i], opts, Integer.MAX_VALUE, tc);
            } else {
                // not recursive so source must not be a directory
                if (Files.isDirectory(source[i])) {
                    logger.error(String.format("%s: is a directory%n", source[i]);
                    continue;
                }
                copyFileWithPreserveOrOverWrite(source[i], dest, prompt, preserve);
            }
        }*/
    }
}
