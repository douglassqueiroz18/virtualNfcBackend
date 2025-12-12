package com.virtualnfc.backendproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pagbank")
public class PagBankConfig {

    private String token;
    private String publicKeyUrl;
    private String ordersUrl;

    // getters e setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPublicKeyUrl() { return publicKeyUrl; }
    public void setPublicKeyUrl(String publicKeyUrl) { this.publicKeyUrl = publicKeyUrl; }

    public String getOrdersUrl() { return ordersUrl; }
    public void setOrdersUrl(String ordersUrl) { this.ordersUrl = ordersUrl; }
}
