package com.qi4l.JYso.web;

import com.qi4l.JYso.HTTPServer;
import com.qi4l.JYso.LdapServer;
import com.qi4l.JYso.LdapsServer;
import com.qi4l.JYso.RMIServer;
import com.qi4l.JYso.gadgets.Config.Config;
import com.qi4l.JYso.web.config.JYsoWebPasswordProvider;
import com.qi4l.JYso.web.config.WebPasswordGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.EnumSet;
import java.util.concurrent.CountDownLatch;

import static com.qi4l.JYso.gadgets.Config.Config.logo;
import static org.fusesource.jansi.Ansi.ansi;

public class JYsoWebApplication {

    private static final Logger log = LogManager.getLogger(JYsoWebApplication.class);

    public static String webPassword;
    public static boolean jndiServersStarted = false;

    public static void start(String[] args) {
        logo();

        webPassword = WebPasswordGenerator.generatePassword();
        JYsoWebPasswordProvider.setPassword(webPassword);

        Config.applyCmdArgs(filterWebArgs(args));

        int webPort = 8080;
        for (int i = 0; i < args.length; i++) {
            if ("-wP".equals(args[i]) || "--webPort".equals(args[i])) {
                if (i + 1 < args.length) {
                    webPort = Integer.parseInt(args[i + 1]);
                }
            }
        }

        System.out.println(ansi().render("@|green [+]|@ Web GUI starting >>  http://127.0.0.1:" + webPort));
        System.out.println(ansi().render("@|yellow [+]|@ ============================================"));
        System.out.println(ansi().render("@|yellow [+]|@   Username: qi"));
        System.out.println(ansi().render("@|yellow [+]|@   Password: " + webPassword));
        System.out.println(ansi().render("@|yellow [+]|@ ============================================"));

        copyToClipboard(webPassword);

        startJndiServers(args);

        try {
            Server server = new Server(webPort);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            context.addFilter(new FilterHolder(new ApiAuthFilter()), "/api/*", EnumSet.of(DispatcherType.REQUEST));

            context.addServlet(new ServletHolder(new AuthServlet()), "/api/auth/login");

            context.addServlet(new ServletHolder(new JettyApiServlet()), "/api/*");

            context.addFilter(new FilterHolder(new SpaFallbackFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

            context.addServlet(new ServletHolder(new StaticResourceServlet()), "/");

            server.start();
            System.out.println(ansi().render("@|green [+]|@ Web GUI started on http://127.0.0.1:" + webPort));

            CountDownLatch latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down...");
                try {
                    server.stop();
                } catch (Exception e) {
                    log.error("Stop error", e);
                }
                latch.countDown();
            }));
            latch.await();
        } catch (Exception e) {
            log.error("Failed to start web server", e);
        }
    }

    private static void copyToClipboard(String text) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(text), null
            );
            System.out.println(ansi().render("@|cyan [+]|@ Password copied to clipboard"));
        } catch (HeadlessException e) {
        } catch (Exception e) {
            System.out.println(ansi().render("@|yellow [!]|@ Could not copy to clipboard: " + e.getMessage()));
        }
    }

    private static String[] filterWebArgs(String[] args) {
        java.util.List<String> filtered = new java.util.ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-w".equals(arg) || "--web".equals(arg)) {
            } else if ("-wP".equals(arg) || "--webPort".equals(arg)) {
                i++;
            } else if ("--with-jndi".equals(arg)) {
            } else {
                filtered.add(arg);
            }
        }
        return filtered.toArray(new String[0]);
    }

    private static void startJndiServers(String[] args) {
        for (String arg : args) {
            if ("--with-jndi".equals(arg)) {
                jndiServersStarted = true;
                new Thread(() -> {
                    try { LdapServer.start(); } catch (Exception e) { log.error("LDAP start failed", e); }
                }, "ldap-server").start();
                new Thread(() -> {
                    try { HTTPServer.start(); } catch (Exception e) { log.error("HTTP start failed", e); }
                }, "http-server").start();
                new Thread(() -> {
                    try { RMIServer.start(); } catch (Exception e) { log.error("RMI start failed", e); }
                }, "rmi-server").start();
                if (Config.TLSProxy) {
                    new Thread(() -> {
                        try { LdapsServer.start(); } catch (Exception e) { log.error("LDAPS start failed", e); }
                    }, "ldaps-server").start();
                }
                break;
            }
        }
    }
}
