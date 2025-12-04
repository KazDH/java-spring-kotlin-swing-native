# Imagez

This is show case of Java desktop application using Jetbrain compose for UI and GraalVM native image to compile it to native executable.

## FAQ

### Will this work on linux/mac?

I don't know. Probably. Code copying .dlls would need to be changed to copy .so or .dylib files instead. 

### Why Java with Kotlin?

Why not? I planed to start with pure swing, then FlatLightLaf. Later I discovered Jetbrain compose multiplatform which looks nice. I was easier for me to configure native in java project than kotlin one.

### Why Spring in Desktop app?

Again, why not? We all know spring. It is not required, but I like it. Look at all those possibilities.


---

## Overview

This project demonstrates how to build a native desktop application using:
- **Java 25** - Main application logic and Spring Boot integration
- **Kotlin** - UI implementation with Jetpack Compose
- **Jetpack Compose for Desktop** - Modern declarative UI framework
- **Spring Boot 4.0** - Application framework (non-web)
- **GraalVM Native Image** - Ahead-of-time compilation to native executable

## Features

- ✅ Native executable compilation with GraalVM
- ✅ Fast startup time
- ✅ Spring Boot integration without web server
- ✅ Modern Compose Desktop UI
- ✅ Automatic DLL/library path configuration for native images
- ✅ Full AWT/Skiko support in native image

## Prerequisites

- **GraalVM 25+** with Native Image support
- **Visual Studio Build Tools** (Windows) or equivalent C compiler
- **Gradle 9.2+**
- **Java 25 toolchain**

## Project Structure

```
imagez/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kgonia/imagez/
│   │   │       └── ImagezApplication.java    # Main Spring Boot application
│   │   ├── kotlin/
│   │   │   └── com/kgonia/imagez/
│   │   │       └── ui/
│   │   │           ├── ComposeUILauncher.kt  # Compose UI launcher
│   │   │           └── ImagezApp.kt          # Main UI composable
│   │   └── resources/
│   │       ├── application.properties
│   │       └── META-INF/
│   │           └── native-image/
│   │               ├── reachability-metadata.json  # Reflection config
│   │               └── org.kgonia/imagez/
│   │                   └── native-image.properties # Native image config
│   └── test/
├── build.gradle                              # Gradle build configuration
└── README.md
```

## Building

### Compile Native Image

```bash
./gradlew nativeCompile
```

The native executable will be generated at:
```
build/native/nativeCompile/imagez.exe  (Windows)
```

### Run from JAR (Development)

```bash
./gradlew bootRun
```

## Technical Details

### Native Image Configuration

The application includes special handling for native image execution:

1. **Java Home Setup** - Automatically configures `java.home` system property for native images
2. **Library Path Management** - Sets up correct paths for AWT/Skiko native libraries
3. **DLL Copy Mechanism** - Copies all required DLLs to `bin/` subdirectory (AWT expects `java.home\bin` structure)

### Key Configuration Files

- **`build.gradle`** - GraalVM Native Image plugin configuration with AWT/Skiko exports
- **`native-image.properties`** - Native image build arguments
- **`reachability-metadata.json`** - Reflection and JNI access configuration for AWT components

### Spring Boot Integration

The application uses Spring Boot in non-web mode:
- No embedded web server
- Headless mode disabled (required for GUI)
- Context initialization before UI launch
- Graceful shutdown on window close

## Startup Process

1. Native image detects execution environment
2. `setupJavaHomeForNativeImage()` configures system properties
3. `copyDllsToBinDirectory()` sets up library structure
4. Spring Boot context initializes
5. Compose Desktop UI launches
6. Application window displays

## Build Output

Typical native image build:
- **Size**: ~78 MB
- **Build Time**: ~1m 44s
- **Startup Time**: ~0.011 seconds
- **Memory**: Peak RSS 4.05GB during build

