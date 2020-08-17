package com.hao.annotetion.task;

import javax.lang.model.element.Element;

public class BindInfo {

    /**
     * 注解使用的类对象
     */
    private Class<?> destination;
    /**
     * 节点（Activity）
     */
    private Element element;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 类型
     */
    private Type type;

    public static BindInfo build(Class bindClass, String path, Type type) {
        return new BindInfo(bindClass, path, type);
    }

    public BindInfo(Class<?> destination, String path) {
        this(destination, path, Type.CLASS);
    }

    public BindInfo(Class<?> destination, String path, Type type) {
        this(destination, null, path, type);
    }

    public BindInfo(Class<?> destination, Element element, String path, Type type) {
        this.destination = destination;
        this.element = element;
        this.path = path;
        this.type = type;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BindInfo{" +
                "destination=" + destination +
                ", element=" + element +
                ", path='" + path + '\'' +
                ", type=" + type +
                '}';
    }
}

