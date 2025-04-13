package org.example.requests;

import org.example.conf.Configuration;

public class Endpoint {
    private String endpoint;
    private String version;

    public Endpoint(String endpoint) {
        this.endpoint = endpoint;
        this.version = Configuration.getProperty("version");
    }

    public String build() {
        String finalEndpoint = endpoint;
        if (this.version != null) {
            finalEndpoint = finalEndpoint + "/" + version;
        }
        return finalEndpoint;
    }
}
