package dev.slne.surf.icon.generator.launcher;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.supplier.RepositorySystemSupplier;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;

@SuppressWarnings("deprecation")
public final class LibraryLoader {

    private final RepositorySystem repository = new RepositorySystemSupplier().getRepositorySystem();
    private final DefaultRepositorySystemSession session;

    {
        final DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        session.setSystemProperties(System.getProperties());
        session.setChecksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_FAIL);
        session.setLocalRepositoryManager(repository.newLocalRepositoryManager(
            session,
            new LocalRepository("libraries"))
        );
        session.setTransferListener(new AbstractTransferListener() {
            @Override
            public void transferInitiated(TransferEvent event) throws TransferCancelledException {
                System.out.println(
                    "Downloading " + event.getResource().getRepositoryUrl() + event.getResource()
                        .getResourceName());
            }

            @Override
            public void transferFailed(TransferEvent event) {
                System.err.println(
                    "Failed to download " + event.getResource().getRepositoryUrl()
                        + event.getResource()
                        .getResourceName());
            }

            @Override
            public void transferSucceeded(TransferEvent event) {
                System.out.println(
                    "Finished downloading " + event.getResource().getRepositoryUrl()
                        + event.getResource()
                        .getResourceName());
            }

            @Override
            public void transferCorrupted(TransferEvent event) throws TransferCancelledException {
                System.err.println(
                    "Corrupted download " + event.getResource().getRepositoryUrl()
                        + event.getResource()
                        .getResourceName());
            }

            @Override
            public void transferStarted(TransferEvent event) throws TransferCancelledException {
                System.out.println(
                    "Started downloading " + event.getResource().getRepositoryUrl()
                        + event.getResource()
                        .getResourceName());
            }
        });
        session.setReadOnly();

        this.session = session;
    }

    public List<Path> loadLibraries(
        List<String> dependencies
    ) throws DependencyResolutionException {
        RemoteRepository slnePublic = new RemoteRepository.Builder(
            "slne-public",
            "default",
            "https://repo.slne.dev/repository/maven-public/"
        ).setPolicy(new RepositoryPolicy(true, RepositoryPolicy.CHECKSUM_POLICY_WARN,
            RepositoryPolicy.UPDATE_POLICY_ALWAYS)
        ).build();

        RemoteRepository mavenCentral = new RemoteRepository.Builder(
            "central",
            "default",
            "https://repo1.maven.org/maven2/"
        ).setPolicy(new RepositoryPolicy(true, RepositoryPolicy.CHECKSUM_POLICY_WARN,
            RepositoryPolicy.UPDATE_POLICY_ALWAYS)
        ).build();

        List<Dependency> deps = dependencies.stream()
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(depStr -> {
                String[] parts = depStr.split(":", 3);
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Invalid dependency format: " + depStr);
                }
                return new Dependency(
                    new DefaultArtifact("%s:%s:%s".formatted(parts[0], parts[1], parts[2])), null);
            })
            .collect(Collectors.toList());

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setDependencies(deps);
        collectRequest.setRepositories(List.of(mavenCentral, slnePublic));
        DependencyRequest request = new DependencyRequest(collectRequest, null);
        DependencyResult result = repository.resolveDependencies(session, request);

        return result.getArtifactResults().stream()
            .map(artifact -> artifact.getArtifact().getPath())
            .collect(Collectors.toList());
    }

}