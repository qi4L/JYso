package com.qi4l.JYso;

import com.qi4l.JYso.gadgets.Config.Config;
import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.fusesource.jansi.Ansi.ansi;

public class TLSProxy {
    private final String localAddr;
    private final String remoteAddr;
    private final String certFile;

    public TLSProxy(String localAddr, String remoteAddr, String certFile) {
        this.localAddr = localAddr;
        this.remoteAddr = remoteAddr;
        this.certFile = certFile;
    }

    public static void start() {
        System.out.println(ansi().render("@|green [+]|@ LDAPS Server Start Listening on >> " + Config.TLSPort + "..."));
        new TLSProxy(Config.ip + ":" + Config.TLSPort, Config.ip + ":" + Config.ldapPort, Config.certFile).run();
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

            ExecutorService executorService = Executors.newCachedThreadPool();
            while (true) {
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                executorService.submit(() -> handleConnection(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SSLServerSocketFactory createSSLServerSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance("JKS");

            try (InputStream keyInput = Files.newInputStream(Paths.get(certFile))) {
                keyStore.load(keyInput, Config.keyPass.toCharArray());
            }

            keyManagerFactory.init(keyStore, Config.keyPass.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            return sslContext.getServerSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleConnection(SSLSocket clientSocket) {
        String[] remoteAddressParts = remoteAddr.split(":");
        Socket remoteSocket = null;

        try {
            // 修复：使用 remoteAddr 而不是 localAddr
            remoteSocket = new Socket(remoteAddressParts[0], Integer.parseInt(remoteAddressParts[1]));

            ExecutorService executorService = Executors.newCachedThreadPool();

            // 使用标志跟踪连接状态
            final boolean[] connectionClosed = {false};

            Socket finalRemoteSocket1 = remoteSocket;
            executorService.submit(() -> {
                try {
                    forwardData(clientSocket.getInputStream(), finalRemoteSocket1.getOutputStream());
                } catch (IOException e) {
                    // 正常关闭连接时可能会抛出异常，不打印堆栈跟踪
                    if (!connectionClosed[0]) {
                        System.err.println("Client to remote error: " + e.getMessage());
                    }
                } finally {
                    closeConnection(clientSocket, finalRemoteSocket1, connectionClosed);
                }
            });

            Socket finalRemoteSocket = remoteSocket;
            executorService.submit(() -> {
                try {
                    forwardData(finalRemoteSocket.getInputStream(), clientSocket.getOutputStream());
                } catch (IOException e) {
                    // 正常关闭连接时可能会抛出异常，不打印堆栈跟踪
                    if (!connectionClosed[0]) {
                        System.err.println("Remote to client error: " + e.getMessage());
                    }
                } finally {
                    closeConnection(clientSocket, finalRemoteSocket, connectionClosed);
                }
            });

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            if (remoteSocket != null) {
                try {
                    remoteSocket.close();
                } catch (IOException ex) {
                    // 忽略关闭异常
                }
            }
            try {
                clientSocket.close();
            } catch (IOException ex) {
                // 忽略关闭异常
            }
        }
    }

    private void forwardData(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;

        try {
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
            }
        } finally {
            // 当读取结束时，关闭输出流
            try {
                output.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }

    private void closeConnection(SSLSocket clientSocket, Socket remoteSocket, boolean[] connectionClosed) {
        // 使用标志确保连接只关闭一次
        if (connectionClosed[0]) {
            return;
        }
        connectionClosed[0] = true;

        // 关闭远程连接
        if (remoteSocket != null && !remoteSocket.isClosed()) {
            try {
                remoteSocket.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }

        // 关闭客户端连接
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }
}