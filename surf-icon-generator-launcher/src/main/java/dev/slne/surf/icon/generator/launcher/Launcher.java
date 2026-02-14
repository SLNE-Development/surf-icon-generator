package dev.slne.surf.icon.generator.launcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import org.eclipse.aether.resolution.DependencyResolutionException;

public class Launcher {

    private static List<String> dependencies = new ArrayList<>();

    static {
        dependencies.add("org.openjfx:javafx-controls:25.0.2");
        dependencies.add("dev.slne.surf:surf-api-standalone:1.21.11-2.59.1");
        dependencies.add("com.github.retrooper:packetevents-netty-common:2.11.0");
    }

    static void main(String[] args) {
        if (Path.of("").toAbsolutePath().toString().contains("!")) {
            System.err.println(
                "surf-icon-generator may not run in a directory containing '!'. Please rename the affected folder.");
            System.exit(1);
        }

        final URL[] classpathUrls = setupClasspath();
        final ClassLoader parentClassLoader = ClassLoader.getSystemClassLoader();

        final URLClassLoader classLoader = new URLClassLoader(
            classpathUrls,
            parentClassLoader
        );

        final LauncherThread runThread = new LauncherThread(
            classLoader,
            "dev.slne.surf.icon.generator.Main"
        );

        runThread.setContextClassLoader(classLoader);
        runThread.start();
    }

    private static URL[] setupClasspath() {
        final URL appUrl = getAppUrl();
        final JarFile appJar;

        try {
            appJar = new JarFile(new File(appUrl.getFile()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to open app.jar as JarFile.", e);
        }

        final URL[][] urls = {
            {appUrl},
            loadLibraries()
        };

        return Stream.of(urls)
            .filter(Objects::nonNull)
            .flatMap(Stream::of)
            .filter(Objects::nonNull)
            .toArray(URL[]::new);
    }

    private static URL getAppUrl() {
        String localPath = System.getProperty("launcher.appJarPath");
        if (localPath != null) {
            try {
                return Path.of(localPath).toUri().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        final URL resource = Launcher.class.getResource("/app.jar.disabled");
        if (resource == null) {
            throw new RuntimeException("Failed to locate app.jar.disabled in resources.");
        }

        try {
            final File tempJar = File.createTempFile("app", ".jar");

            try (final InputStream inputStream = resource.openStream()) {
                Files.copy(inputStream, tempJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return tempJar.toURI().toURL();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary app.jar from resource.", e);
        }
    }

    private static URL[] loadLibraries() {
        LibraryLoader libraryLoader = new LibraryLoader();
        List<Path> paths;

        try {
            paths = libraryLoader.loadLibraries(dependencies);
        } catch (DependencyResolutionException e) {
            throw new RuntimeException(e);
        }

        return paths.stream()
            .filter(Objects::nonNull)
            .map(path -> {
                try {
                    return path.toUri().toURL();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .toArray(URL[]::new);
    }

}
