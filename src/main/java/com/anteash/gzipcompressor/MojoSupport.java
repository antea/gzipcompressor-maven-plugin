package com.anteash.gzipcompressor;

import java.io.File;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Common class for mojos.
 *
 * @author David Bernard
 * @created 2007-08-29
 */
@SuppressWarnings("unchecked")
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

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            processDir(sourceDirectory);
//            getLog().info(String.format("nb warnings: %d, nb errors: %d", jsErrorReporter_.getWarningCnt(), jsErrorReporter_.getErrorCnt()));
//            if (failOnWarning && (jsErrorReporter_.getWarningCnt() > 0)) {
//                throw new MojoFailureException("warnings on " + this.getClass().getSimpleName() + "=> failure ! (see log)");
//            }
        } catch (RuntimeException exc) {
            throw exc;
        } catch (MojoFailureException exc) {
            throw exc;
        } catch (MojoExecutionException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new MojoExecutionException("wrap: " + exc.getMessage(), exc);
        }
    }

    protected void processDir(final File srcRoot) throws Exception {
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
//            jsErrorReporter_.setDefaultFileName("..." + src.toFile().getAbsolutePath().substring(project.getBasedir().getAbsolutePath().length()));
            processFile(new File(srcRoot, name));
        }
    }

    protected abstract void processFile(final File file) throws Exception;
}
