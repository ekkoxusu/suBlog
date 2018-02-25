package com.su.blog;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "jdbcTypeProperties")
public class jdbcTypeProperties {
    private  String id;
    private  Map<String,String> jdbcTypeToJavaType = new HashMap<>();

    public  Map<String, String> getJdbcTypeToJavaType() {

        return jdbcTypeToJavaType;
    }

    public  String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public void setJdbcTypeToJavaType(Map<String, String> jdbcTypeToJavaType) {
        this.jdbcTypeToJavaType = jdbcTypeToJavaType;
    }
}
