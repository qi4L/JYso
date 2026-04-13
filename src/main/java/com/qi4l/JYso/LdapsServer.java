package com.qi4l.JYso;

import com.qi4l.JYso.gadgets.Config.Config;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.util.ssl.KeyStoreKeyManager;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static org.fusesource.jansi.Ansi.ansi;

public class LdapsServer {
    private static final Logger log = LogManager.getLogger(LdapsServer.class);
    private final String certFile;
    private final String keyPass;

    public LdapsServer(String certFile, String keyPass) {
        this.certFile = certFile;
        this.keyPass = keyPass;
    }

    public static void start() {
        System.out.println(ansi().render("@|green [+]|@ LDAPS Server Start Listening on >> " + Config.ldapsPort + "..."));
        new LdapsServer(Config.certFile, Config.keyPass).run();
    }

    public void run() {
        // 设置JDK信任证书
        System.setProperty("javax.net.ssl.trustStore", certFile);
        System.setProperty("javax.net.ssl.trustStorePassword", keyPass);

        try {
            SSLUtil serverSSLUtil = new SSLUtil(
                    new KeyStoreKeyManager(certFile, keyPass.toCharArray()),
                    new TrustAllTrustManager()
            );
            SSLUtil clientSSLUtil = new SSLUtil(new TrustAllTrustManager());

            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=com");
            config.setListenerConfigs(InMemoryListenerConfig.createLDAPSConfig(
                    "listen-ldaps",
                    null,
                    Integer.parseInt(String.valueOf(Config.ldapsPort)),
                    serverSSLUtil.createSSLServerSocketFactory(),
                    clientSSLUtil.createSSLSocketFactory()
            ));
            config.addInMemoryOperationInterceptor(new LdapServer());

            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            ds.startListening();
            System.out.println(ansi().render("@|green [+]|@ LDAPS Server Start Listening on >> " + Config.ldapsPort + "..."));
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }
}