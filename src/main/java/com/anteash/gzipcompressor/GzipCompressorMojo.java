package com.anteash.gzipcompressor;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * Apply compression with GZip.
 * @author Federico Russo
 * @goal compress
 * @phase prepare-package
 * @created 2012-01-17
 */
public class GzipCompressorMojo extends MojoSupport {

    @Override
    protected void processFile(final File file) throws IOException {
        if ((file == null) || (!file.exists())) {
            return;
        }
        if (".gz".equalsIgnoreCase(FileUtils.getExtension(file.getName()))) {
            return;
        }

        final File gzipped = new File(file.getAbsolutePath() + ".gz");
        getLog().debug(String.format("Compressing %s to %s", file.getName(), gzipped.getName()));
        try (GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(gzipped));
             FileInputStream in = new FileInputStream(file)) {
            IOUtil.copy(in, out);
        }
    }
}
