package dev.rosewood.rosegarden.objects;

public class RosePluginData {

    private final String name;
    private final String version;
    private final String updateVersion;
    private final String website;
    private final String roseGardenVersion;

    public RosePluginData(String name, String version, String updateVersion, String website, String roseGardenVersion) {
        this.name = name;
        this.version = version;
        this.updateVersion = updateVersion;
        this.website = website;
        this.roseGardenVersion = roseGardenVersion;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getUpdateVersion() {
        return this.updateVersion;
    }

    public String getWebsite() {
        return this.website;
    }

    public String getRoseGardenVersion() {
        return this.roseGardenVersion;
    }

}
