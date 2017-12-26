package com.nxllxn.plantuml.config;

import java.util.Set;


public class ClassScannerConfiguration {
    private String rootPackage;

    private Set<String> includePackages;

    private Set<String> excludePackages;

    private boolean isScanSubPackages;

    private boolean isScanInnerType;

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public Set<String> getIncludePackages() {
        return includePackages;
    }

    public void setIncludePackages(Set<String> includePackages) {
        this.includePackages = includePackages;
    }

    public Set<String> getExcludePackages() {
        return excludePackages;
    }

    public void setExcludePackages(Set<String> excludePackages) {
        this.excludePackages = excludePackages;
    }

    public boolean isScanSubPackages() {
        return isScanSubPackages;
    }

    public void setScanSubPackages(boolean scanSubPackages) {
        isScanSubPackages = scanSubPackages;
    }

    public boolean isScanInnerType() {
        return isScanInnerType;
    }

    public void setScanInnerType(boolean scanInnerType) {
        isScanInnerType = scanInnerType;
    }
}
