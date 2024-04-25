package dev.rosewood.rosegarden.objects;

public class RosePluginData {

    public final String name;
    public final String version;
    public final String updateVersion;
    public final String website;
    public final String roseGardenVersion;

    public RosePluginData(String name,
                          String version,
                          String updateVersion,
                          String website,
                          String roseGardenVersion) {
        this.name = name;
        this.version = version;
        this.updateVersion = updateVersion;
        this.website = website;
        this.roseGardenVersion = roseGardenVersion;
    }

    public String name() {
        return this.name;
    }

    public String version() {
        return this.version;
    }

    public String updateVersion() {
        return this.updateVersion;
    }

    public String website() {
        return this.website;
    }

    public String roseGardenVersion() {
        return this.roseGardenVersion;
    }

}
