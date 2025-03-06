package com.qi4l.JYso;

import com.qi4l.JYso.gadgets.Config.Config;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.fusesource.jansi.Ansi.ansi;

public class TLSProxy {
    private final String localAddr;
    private final String remoteAddr;
    private final String certFile;
    private final String keyFile;

    public TLSProxy(String localAddr, String remoteAddr, String certFile, String keyFile) {
        this.localAddr = localAddr;
        this.remoteAddr = remoteAddr;
        this.certFile = certFile;
        this.keyFile = keyFile;
    }

    public static void start() {
        System.out.println(ansi().render("@|green [+]|@ LDAPS Server Start Listening on >>" + Config.TLSProxy + "..."));
        new TLSProxy(Config.ip + ":" + Config.TLSProxy, Config.ip + ":" + Config.ldapPort, Config.certFile, Config.keyFile).run();
    }

    public void run() {
        SSLServerSocketFactory sslServerSocketFactory = createSSLServerSocketFactory();
        if (sslServerSocketFactory == null) {
            System.err.println("Failed to create SSLServerSocketFactory");
            return;
        }

        try (SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket()) {
            String[] addressParts = localAddr.split(":");
            serverSocket.bind(new InetSocketAddress(addressParts[0], Integer.parseInt(addressParts[1])));
            System.out.println("TLS Proxy started on " + localAddr);

            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                System.out.println("New connection from " + clientSocket.getRemoteSocketAddress());
                executorService.submit(() -> handleConnection(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SSLServerSocketFactory createSSLServerSocketFactory() {
        try {
            SSLContext        sslContext        = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore          keyStore          = KeyStore.getInstance("JKS");

            try (InputStream keyInput = new FileInputStream(certFile)) {
                keyStore.load(keyInput, "".toCharArray());
            }

            keyManagerFactory.init(keyStore, "".toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            return sslContext.getServerSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleConnection(SSLSocket clientSocket) {
        try (Socket remoteSocket = new Socket(remoteAddr, getPort(remoteAddr))) {
            System.out.println("Connected to " + remoteAddr);
            ExecutorService executorService = Executors.newCachedThreadPool();

            executorService.submit(() -> {
                try {
                    forwardData(clientSocket.getInputStream(), remoteSocket.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            executorService.submit(() -> {
                try {
                    forwardData(remoteSocket.getInputStream(), clientSocket.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void forwardData(InputStream input, OutputStream output) {
        try {
            byte[] buffer = new byte[8192];
            int    bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getPort(String address) {
        return Integer.parseInt(address.substring(address.lastIndexOf(':') + 1));
    }
}
