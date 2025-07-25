package dev.revere.alley.plugin;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.tool.logger.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
@Getter
public final class AlleyContext {
    private static final String SERVICE_IMPL_PACKAGE = "dev.revere.alley";

    private final Alley plugin;
    private final Map<Class<? extends Service>, Service> serviceInstances = new ConcurrentHashMap<>();
    private final Map<Class<? extends Service>, Class<? extends Service>> serviceRegistry = new HashMap<>();
    private final Set<Class<? extends Service>> servicesBeingConstructed = new HashSet<>();

    private ScanResult scanResult;
    private List<Service> sortedServices = Collections.emptyList();

    public AlleyContext(Alley plugin) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin instance cannot be null");
    }

    /**
     * Discovers, instantiates, and initializes all services.
     */
    public void initialize() throws Exception {
        Logger.logPhaseStart("Service Initialization");

        try {
            this.scanResult = new ClassGraph().enableAllInfo().acceptPackages(SERVICE_IMPL_PACKAGE).scan();
        } catch (Exception e) {
            throw new IllegalStateException("Classpath scanning failed.", e);
        }

        List<Class<? extends Service>> implementationClasses = discoverServices(this.scanResult);
        if (implementationClasses.isEmpty()) {
            throw new IllegalStateException("No services found to load.");
        }

        for (Class<? extends Service> implClass : implementationClasses) {
            serviceRegistry.put(getProvidedInterface(implClass), implClass);
        }

        List<Class<? extends Service>> sortedImplClasses = sortServicesByPriority(implementationClasses);
        Logger.info("Service Initialization Order: " +
                sortedImplClasses.stream().map(Class::getSimpleName).collect(Collectors.joining(" -> ")));

        for (Class<? extends Service> implClass : sortedImplClasses) {
            instantiateService(implClass);
        }

        this.sortedServices = sortedImplClasses.stream()
                .map(impl -> serviceInstances.get(getProvidedInterface(impl)))
                .collect(Collectors.toList());

        Logger.logPhaseStart("Service Setup Phase");
        for (Service service : sortedServices) {
            Logger.logTime(service.getClass().getSimpleName() + " Setup", () -> service.setup(this));
        }

        Logger.logPhaseStart("Service Initialization Phase");
        for (Service service : sortedServices) {
            Logger.logTime(service.getClass().getSimpleName() + " Initialization", () -> service.initialize(this));
        }

        Logger.logPhaseComplete("Service Initialization");
    }

    /**
     * Shuts down all managed services in reverse order.
     */
    public void shutdown() {
        Logger.info("--- Service Shutdown Start ---");
        if (sortedServices == null) return;

        List<Service> reversedServices = new ArrayList<>(sortedServices);
        Collections.reverse(reversedServices);

        for (Service service : reversedServices) {
            try {
                service.shutdown(this);
            } catch (Exception e) {
                Logger.logException("Failed to shutdown service " + service.getClass().getSimpleName(), e);
            }
        }
        Logger.info("--- Service Shutdown Complete ---");
    }

    /**
     * Retrieves a managed service by its interface.
     * @param serviceInterface The interface of the service to get.
     * @return An Optional containing the service instance if found.
     */
    public <T extends Service> Optional<T> getService(Class<T> serviceInterface) {
        return Optional.ofNullable(serviceInterface.cast(serviceInstances.get(serviceInterface)));
    }

    @SuppressWarnings("unchecked")
    private void instantiateService(Class<? extends Service> implClass) throws Exception {
        Class<? extends Service> providedInterface = getProvidedInterface(implClass);
        if (serviceInstances.containsKey(providedInterface)) {
            return;
        }

        if (servicesBeingConstructed.contains(implClass)) {
            throw new IllegalStateException("Cyclic Dependency Detected! Cycle involves: " + implClass.getName());
        }
        servicesBeingConstructed.add(implClass);

        Constructor<?> constructor = Arrays.stream(implClass.getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new NoSuchMethodException("No suitable constructor found for " + implClass.getSimpleName()));

        List<Object> dependencies = new ArrayList<>();
        for (Class<?> paramType : constructor.getParameterTypes()) {
            if (Alley.class.isAssignableFrom(paramType)) {
                dependencies.add(this.plugin);
                continue;
            }
            if (Service.class.isAssignableFrom(paramType)) {
                Class<? extends Service> dependencyInterface = (Class<? extends Service>) paramType;
                Class<? extends Service> dependencyImpl = this.serviceRegistry.get(dependencyInterface);
                if (dependencyImpl == null) {
                    throw new ClassNotFoundException("No implementation found for service interface: " + dependencyInterface.getName());
                }

                instantiateService(dependencyImpl);
                dependencies.add(serviceInstances.get(dependencyInterface));
            } else {
                throw new IllegalStateException("Unsupported dependency type in " + implClass.getSimpleName() + ": " + paramType.getSimpleName());
            }
        }

        Service serviceInstance = (Service) constructor.newInstance(dependencies.toArray());
        serviceInstances.put(providedInterface, serviceInstance);

        servicesBeingConstructed.remove(implClass);
    }

    @SuppressWarnings("unchecked")
    private List<Class<? extends Service>> discoverServices(ScanResult scanResult) {
        List<Class<? extends Service>> services = new ArrayList<>();
        for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(dev.revere.alley.plugin.annotation.Service.class.getName())) {
            if (!classInfo.isAbstract() && !classInfo.isInterface()) {
                services.add((Class<? extends Service>) classInfo.loadClass());
            }
        }
        return services;
    }

    private List<Class<? extends Service>> sortServicesByPriority(List<Class<? extends Service>> services) {
        return services.stream()
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(dev.revere.alley.plugin.annotation.Service.class).priority()))
                .collect(Collectors.toList());
    }

    private Class<? extends Service> getProvidedInterface(Class<? extends Service> implClass) {
        return implClass.getAnnotation(dev.revere.alley.plugin.annotation.Service.class).provides();
    }
}