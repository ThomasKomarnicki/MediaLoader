package com.doglandia.medialoader.model;

import java.util.List;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class ResourceGroup {

    private String name;
    private String groupName;

    private List<Resource> resourceList;

    public String getName() {
        return name;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }
}
