package com.qi4l.JYso.controllers;

import com.qi4l.JYso.exceptions.IncorrectParamsException;
import com.qi4l.JYso.exceptions.UnSupportedActionTypeException;
import com.qi4l.JYso.exceptions.UnSupportedGadgetTypeException;
import com.qi4l.JYso.exceptions.UnSupportedPayloadTypeException;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;

public interface LdapController {
    void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception;

    void process(String base) throws UnSupportedPayloadTypeException, IncorrectParamsException, UnSupportedGadgetTypeException, UnSupportedActionTypeException;
}
