package com.anteash.gzipcompressor;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Common class for mojos.
 *
 * @author David Bernard
 * @created 2007-08-29
 */
public abstract class MojoSupport extends AbstractMojo {

    private static final String[] EMPTY_STRING_ARRAY = {};
    /**
     * Javascript source directory. (result will be put to outputDirectory).
     * This allow project with "src/main/js" structure.
     *
     * @parameter expression="${project.build.outputDirectory}"
     */
    private File sourceDirectory;
    /**
     * list of additional excludes
     *
     * @parameter
     */
    private List<String> excludes;
    /**
     * list of additional includes
     *
     * @parameter
     */
    private List<String> includes;

    public void execute() throws MojoExecutionException {
        try {
            processDir(sourceDirectory);
        } catch (RuntimeException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new MojoExecutionException("wrap: " + exc.getMessage(), exc);
        }
    }

    protected void processDir(final File srcRoot) throws IOException {
        if ((srcRoot == null) || (!srcRoot.exists())) {
            return;
        }
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(srcRoot);

        if ((includes != null) && !includes.isEmpty()) {
            scanner.setIncludes(includes.toArray(new String[0]));
        }
        if ((excludes != null) && !excludes.isEmpty()) {
            scanner.setExcludes(excludes.toArray(EMPTY_STRING_ARRAY));
        }
        scanner.addDefaultExcludes();
        scanner.scan();
        for (final String name : scanner.getIncludedFiles()) {
            processFile(new File(srcRoot, name));
        }
    }

    protected abstract void processFile(final File file) throws IOException;
}
