package dev.rosewood.rosegarden.utils;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NMSUtilTest {

    @Test
    public void testOldVersioning() {
        testVersion("1.21.5-R0.1-SNAPSHOT", 21, 5, 0);
    }

    @Test
    public void testYearBasedVersioning() {
        testVersion("26.1.2-R0.1-SNAPSHOT", 26, 1, 2);
    }

    private static void testVersion(String version, int major, int minor, int patch) {
        Server server = mock();
        when(server.getBukkitVersion()).thenReturn(version);

        try {
            Field serverField = Bukkit.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(null, server);
        } catch (ReflectiveOperationException e) {
            fail(e);
        }

        NMSUtil.initialize();

        assertEquals(major, NMSUtil.getVersionNumber());
        assertEquals(minor, NMSUtil.getMinorVersionNumber());
        assertEquals(patch, NMSUtil.getPatchVersionNumber());
    }

}
