package org.example;

public enum CurrentDistrict {
    MAADI("Maadi"),
    DOKKI("Dokki"),
    FAISAL("Faisal"),
    NASR_CITY("Nasr City"),
    HELIOPOLIS("Heliopolis");

    private final String displayName;

    CurrentDistrict(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}