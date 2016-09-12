package de.zayzn.preparerelease;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final String FILE_DESCRIPTION = "description.txt";
    private static final String FILE_TAGS = "tags.txt";

    private static final int LIMIT_DESCRIPTION = 5000;
    private static final int LIMIT_TAGS = 500;

    public static void main(String[] args) throws Exception {

        final File workingDir = new File(Paths.get("").toFile().getAbsolutePath()); // I blame Oracle!
        File sourceDir = null;

        out.println("\nzayzn Release Preparation Tool - Community Edition | reddit.com/u/zayzn\n");

        /*
         * Check for valid source directory.
         */
        if (args.length != 1) {
            out.println("Argument required.");
            exit(-1);
        } else {
            sourceDir = new File(args[0]);
            if (!sourceDir.exists()) {
                out.println("The source directory does not exist.");
                exit(-1);
            } else if (!isSubDirectory(workingDir, sourceDir)) {
                out.println("The source directory must be a child of the working directory.");
                exit(-1);
            }
        }

        /*
         * Prepare the target directory.
         */
        final File productsDir = new File("products");
        final File targetDir = new File(productsDir, sourceDir.getParentFile().getParentFile().getName() + " "
                + sourceDir.getParentFile().getName() + " " + sourceDir.getName());
        if (targetDir.exists()) { // ... delete target directory
            Files.walk(targetDir.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
                    .forEach(File::delete);
        }
        targetDir.mkdirs();

        /*
         * Display working parameters.
         */
        out.println("Working directory: " + workingDir.getAbsolutePath());
        out.println("Target directory: " + targetDir.getAbsolutePath());
        out.println("Source directory: " + sourceDir.getAbsolutePath());

        out.println("\nCreating product...\n");

        /*
         * Copy video file.
         */
        final String videoFileName = sourceDir.getName() + ".mp4";
        final File videoFile = new File(sourceDir, videoFileName);
        Files.copy(videoFile.toPath(), new File(targetDir, videoFileName).toPath());

        /*
         * Copy thumbnail.
         */
        final String thumbnailFileName = sourceDir.getName() + ".png";
        final File thumbnailFile = new File(sourceDir, thumbnailFileName);
        Files.copy(thumbnailFile.toPath(), new File(targetDir, thumbnailFileName).toPath());

        /*
         * Compile description.
         */

        int length;

        length = writeLines(stripEmptyLines(readLines(FILE_DESCRIPTION, sourceDir, workingDir), 1), new File(targetDir, FILE_DESCRIPTION),
                "\n");
        if (length > LIMIT_DESCRIPTION) {
            out.print("WARNING! ");
        }
        out.println("Description length: " + length + "/" + LIMIT_DESCRIPTION + " characters.");

        /*
         * Compile tags.
         */
        length = writeLines(removeDuplicates(stripEmptyLines(readLines(FILE_TAGS, sourceDir, workingDir), 0)),
                new File(targetDir, FILE_TAGS), ",");
        if (length > LIMIT_TAGS) {
            out.print("WARNING! ");
        }
        out.println("Tags length: " + length + "/" + LIMIT_TAGS + " characters.");

        /*
         * Exit
         */
        out.println("\nProduct creation finished.\n");
        try {
            Desktop.getDesktop().open(targetDir);
        } catch (final IOException e) {}
    }

    private static List<String> readLines(String fileName, File sourceDir, File workingDir) throws IOException {
        final List<String> r = new ArrayList<>();

        File currentDir = sourceDir;

        do {
            final File f = new File(currentDir, fileName);
            if (f.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String l;
                    while ((l = br.readLine()) != null) {
                        r.add(l);
                    }
                    r.add("");
                }
            } else {
                out.println("No " + fileName + " exists in " + currentDir.getAbsolutePath());
            }
            currentDir = currentDir.getParentFile();
        } while (!currentDir.equals(workingDir.getParentFile()));

        return r;
    }

    private static List<String> stripEmptyLines(List<String> lines, int keep) {
        final List<String> r = new ArrayList<>();

        final Iterator<String> i = lines.iterator();
        String prev = null;
        int n = 0;
        while (i.hasNext()) {
            final String s = i.next().trim();
            if (s.isEmpty() && n < keep) {
                n++;
                r.add("");
            } else if (!s.isEmpty()) {
                n = 0;
                r.add(s);
            }
            prev = s;
        }

        return r;
    }

    private static List<String> removeDuplicates(List<String> lines) {
        final Set<String> set = new LinkedHashSet<>();
        lines.stream().forEach((s) -> set.add(s));
        return new ArrayList<>(Arrays.asList(set.toArray(new String[0])));
    }

    private static int writeLines(List<String> lines, File outFile, String separator) throws IOException {
        int r = 0;

        try (PrintStream o = new PrintStream(new FileOutputStream(outFile))) {
            for (int i = 0; i < lines.size(); i++) {
                final String s = lines.get(i).trim();
                r += i == 0 ? s.length() : s.length() + separator.length();
                o.print(s);
                if (i != lines.size() - 1) {
                    o.print(separator);
                }
            }
        }

        return r;
    }

    private static boolean isSubDirectory(File base, File child) throws IOException {
        base = base.getCanonicalFile();
        child = child.getCanonicalFile();

        File parentFile = child;
        while (parentFile != null) {
            if (base.equals(parentFile)) { return true; }
            parentFile = parentFile.getParentFile();
        }
        return false;
    }

}
