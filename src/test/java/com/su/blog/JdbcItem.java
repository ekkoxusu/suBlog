package com.su.blog;

import java.io.Serializable;

public class JdbcItem implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    private String name;
    private String typeName;
    private String comment;
    /**
     * 首字母大写的name
     */
    private String upName;
    public JdbcItem() {
    }

    public JdbcItem(String name,String upName, String typeName, String comment) {
        this.name = name;
        this.typeName = typeName;
        this.comment = comment;
        this.upName = upName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpName() {
        return upName;
    }

    public void setUpName(String upName) {
        this.upName = upName;
    }

    @Override
    public String toString() {
        return "JdbcItem{" +
                "name='" + name + '\'' +
                ", typeName='" + typeName + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
