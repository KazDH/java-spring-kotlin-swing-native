package com.kgonia.imagez;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.formdev.flatlaf.FlatLightLaf;

@SpringBootApplication
public class ImagezApplication implements CommandLineRunner
{
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args)
    {
        new SpringApplicationBuilder(ImagezApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }


    @Override
    public void run(String... args) throws Exception
    {
        FlatLightLaf.setup();

        System.setProperty("flatlaf.menuBarEmbedded", "false");
        UIManager.put("JFrame.arc", 0);
        UIManager.put("Button.arc", 10);
        UIManager.put("Label.arc", 0);
        UIManager.put("Component.arc", 0);
        UIManager.put("ProgressBar.arc", 0);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("TabbedPane.tabSeparatorsFullHeight", true);
        UIManager.put("TabbedPane.selectedBackground", Color.white);

        JFrame frame = new JFrame("Swing Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Bind window lifecycle to Spring context
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SpringApplication.exit(context, () -> 0);
            }
        });

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        final JButton button = new JButton("Click here");
        contentPane.add(button, BorderLayout.CENTER);

        button.addActionListener(e -> button.setText("Clicked"));

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
