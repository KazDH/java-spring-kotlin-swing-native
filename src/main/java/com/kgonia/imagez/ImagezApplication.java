package com.kgonia.imagez;

import com.kgonia.imagez.ui.ComposeUILauncherKt;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring Boot application with Kotlin Compose Desktop UI.
 * Java handles business logic, Kotlin handles UI.
 */
@SpringBootApplication
public class ImagezApplication {

    public static void main(String[] args) {
        // CRITICAL: Configure java.home FIRST before any other code runs
        // This is required for AWT/Skiko to locate native libraries like jawt.dll
        setupJavaHomeForNativeImage();
        copyDllsToBinDirectory();

        // Initialize Spring Boot context without web server
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ImagezApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);

        System.out.println("Spring context initialized, launching Compose UI...");

        // Launch Kotlin Compose UI
        ComposeUILauncherKt.launchComposeUI(context);
    }

    /**
     * Set up java.home system property for native image execution.
     * Must be called before any AWT/Skiko initialization.
     */
    private static void setupJavaHomeForNativeImage() {
        String javaHome = System.getProperty("java.home");
        boolean isNativeImage = org.graalvm.nativeimage.ImageInfo.inImageCode();

        // Check if we're running as a native image (imagecode will be non-null)
        // or if java.home is not set
        boolean isJavaHomeToSet = isNativeImage && (javaHome == null || javaHome.isEmpty());

        if (isJavaHomeToSet) {
            // Get the directory containing the executable
            String currentDir = System.getProperty("user.dir");

            // Set java.home to current directory BEFORE any AWT/Skiko initialization
            System.setProperty("java.home", currentDir);

            // Also set the library paths to include both current and bin directories
            java.io.File binDir = new java.io.File(currentDir, "bin");
            String libraryPath = currentDir + java.io.File.pathSeparator + binDir.getAbsolutePath();
            System.setProperty("java.library.path", libraryPath);
            System.setProperty("sun.boot.library.path", binDir.getAbsolutePath());

            System.out.println("Native image detected. Configured paths:");
            System.out.println("  java.home: " + currentDir);
            System.out.println("  java.library.path: " + libraryPath);
            System.out.println("  sun.boot.library.path: " + System.getProperty("sun.boot.library.path"));
        }
    }

    /**
     * Copy DLL files from working directory to bin subdirectory.
     * AWT expects DLLs in java.home\bin directory structure.
     */
    private static void copyDllsToBinDirectory() {
        boolean isNativeImage = org.graalvm.nativeimage.ImageInfo.inImageCode();

        if (isNativeImage) {
            String currentDir = System.getProperty("user.dir");

            // AWT expects DLLs in java.home\bin, so we need to create this structure
            java.io.File binDir = new java.io.File(currentDir, "bin");
            boolean binCreated = binDir.exists() || binDir.mkdirs();

            if (binCreated) {
                // Copy all DLL files from current directory to bin directory
                java.io.File currentDirFile = new java.io.File(currentDir);
                java.io.File[] dllFiles = currentDirFile.listFiles((d, name) -> name.toLowerCase().endsWith(".dll"));

                if (dllFiles != null) {
                    for (java.io.File dll : dllFiles) {
                        java.io.File targetDll = new java.io.File(binDir, dll.getName());
                        if (!targetDll.exists()) {
                            try {
                                java.nio.file.Files.copy(
                                    dll.toPath(),
                                    targetDll.toPath(),
                                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                                );
                                System.out.println("Copied " + dll.getName() + " to bin directory");
                            } catch (java.io.IOException e) {
                                System.err.println("Failed to copy " + dll.getName() + ": " + e.getMessage());
                            }
                        }
                    }
                }
            } else {
                System.err.println("Failed to create bin directory: " + binDir.getAbsolutePath());
            }
        }
    }
}
