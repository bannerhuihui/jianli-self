package com.aitalentagent.api.domain;

import java.util.ArrayList;
import java.util.List;

public class EvidenceEntity {

    private String id;
    private String source;
    private String snippet;
    private List<String> capabilityKeys = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public List<String> getCapabilityKeys() {
        return capabilityKeys;
    }

    public void setCapabilityKeys(List<String> capabilityKeys) {
        this.capabilityKeys = capabilityKeys;
    }
}
