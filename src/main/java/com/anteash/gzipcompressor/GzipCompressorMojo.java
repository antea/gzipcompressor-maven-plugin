package com.anteash.gzipcompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * Apply compression with GZip.
 *
 * @goal compress
 * @phase process-resources
 *
 * @author Federico Russo
 * @created 2012-01-17
 */
@SuppressWarnings("unchecked")
public class GzipCompressorMojo extends MojoSupport {

    @Override
    protected void processFile(final File file) throws Exception {
        if ((file == null) || (!file.exists())) {
            return;
        }
        if (".gz".equalsIgnoreCase(FileUtils.getExtension(file.getName()))) {
            return;
        }
        
        final File gzipped = new File(file.getAbsolutePath() + ".gz");
        getLog().debug(String.format("Compressing %s to %s", file.getName(), gzipped.getName()));
        GZIPOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new GZIPOutputStream(new FileOutputStream(gzipped));
            in = new FileInputStream(file);
            IOUtil.copy(in, out);
        } finally {
            IOUtil.close(in);
            IOUtil.close(out);
        }
    }
}
