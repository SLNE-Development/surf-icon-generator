package dev.slne.surf.icon.generator.launcher;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

final class LauncherThread extends Thread {

    private final ClassLoader classLoader;
    private final String mainClassName;

    LauncherThread(ClassLoader classLoader, String mainClassName) {
        super("LauncherThread");

        this.classLoader = classLoader;
        this.mainClassName = mainClassName;

        setDaemon(false);
    }

    @Override
    public void run() {
        try {
            final Class<?> mainClass = Class.forName(mainClassName, true, classLoader);

            final VarHandle instance = MethodHandles.privateLookupIn(
                mainClass,
                MethodHandles.lookup()
            ).findStaticVarHandle(mainClass, "INSTANCE", mainClass);

            final Object mainInstance = instance.get();

            final MethodHandle mainMethod = MethodHandles.publicLookup()
                .findVirtual(mainClass, "run", MethodType.methodType(void.class))
                .bindTo(mainInstance);

            mainMethod.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
