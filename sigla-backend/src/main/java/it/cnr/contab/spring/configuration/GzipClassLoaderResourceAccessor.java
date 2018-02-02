package it.cnr.contab.spring.configuration;

import liquibase.logging.LogFactory;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class GzipClassLoaderResourceAccessor extends ClassLoaderResourceAccessor {

    @Override
    public Set<InputStream> getResourcesAsStream(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        if (resources == null || !resources.hasMoreElements()) {
            return null;
        }
        Set<String> seenUrls = new HashSet<String>();
        Set<InputStream> returnSet = new HashSet<InputStream>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            if (seenUrls.contains(url.toExternalForm())) {
                continue;
            }
            seenUrls.add(url.toExternalForm());
            LogFactory.getInstance().getLog().debug("Opening "+url.toExternalForm()+" as "+path);

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream resourceAsStream;

            if (path.endsWith(".gz")) {
                InputStream inputStream = connection.getInputStream();
                resourceAsStream = new GZIPInputStream(inputStream);
            } else {
                resourceAsStream = connection.getInputStream();
            }


            if (resourceAsStream != null) {
                returnSet.add(resourceAsStream);
            }
        }

        return returnSet;
    }



}
