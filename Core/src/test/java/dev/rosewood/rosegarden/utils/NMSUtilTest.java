package dev.rosewood.rosegarden.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NMSUtilTest {

    private static Server server;

    @BeforeAll
    public static void setup() {
        Server server = mock(Server.class);
        if (Bukkit.getServer() == null)
            Bukkit.setServer(server);
    }

    @Test
    public void testOldVersioning() {
        when(server.getBukkitVersion()).thenReturn("1.21.5-R0.1-SNAPSHOT");
        assertEquals(21, NMSUtil.getVersionNumber());
        assertEquals(5, NMSUtil.getMinorVersionNumber());
        assertEquals(0, NMSUtil.getPatchVersionNumber());
    }

    @Test
    public void testYearBasedVersioning() {
        when(server.getBukkitVersion()).thenReturn("26.1.2-R0.1-SNAPSHOT");
        assertEquals(26, NMSUtil.getVersionNumber());
        assertEquals(1, NMSUtil.getMinorVersionNumber());
        assertEquals(2, NMSUtil.getPatchVersionNumber());
    }

}
