package com.kgonia.imagez;

import com.kgonia.imagez.ui.ComposeUILauncherKt;
import org.springframework.boot.SpringApplication;
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

        // Initialize Spring Boot context without web server
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ImagezApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);

        System.out.println("Spring context initialized, launching Compose UI...");

        // Launch Kotlin Compose UI
        ComposeUILauncherKt.launchComposeUI(context);
    }
}
