package fi.om.municipalityinitiative.util;

import com.google.common.collect.Maps;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import java.util.Enumeration;
import java.util.Map;

public class FakeSession implements HttpSession {

    Map<String, Object> attributes = Maps.newHashMap();

    @Override
    public long getCreationTime() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String getId() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public long getLastAccessedTime() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ServletContext getServletContext() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int getMaxInactiveInterval() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Object getValue(String name) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Enumeration getAttributeNames() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String[] getValueNames() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void removeValue(String name) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void invalidate() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean isNew() {
        throw new RuntimeException("Not implemented");
    }
}
