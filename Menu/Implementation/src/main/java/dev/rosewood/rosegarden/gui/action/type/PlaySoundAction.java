package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import java.util.Optional;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * An action that plays a sound to a player.<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: play_sound
 *         sound: minecraft:block.note_block.pling
 *     }
 * </pre>
 * OR<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: play_sound
 *         sound: minecraft:block.note_block.pling
 *         volume: 1.0
 *         pitch: 1.0
 *     }
 * </pre>
 */
public class PlaySoundAction extends AbstractAction {

    public static final String ID = "play_sound";
    public static final String SOUND = "sound";
    public static final String VOLUME = "volume";
    public static final String PITCH = "pitch";

    protected final String sound;
    protected final float volume;
    protected final float pitch;

    // Code Constructors

    public PlaySoundAction(String sound) {
        super(ID);

        this.sound = sound;
        this.volume = 1.0F;
        this.pitch = 1.0F;
    }

    public PlaySoundAction(String sound, float volume, float pitch) {
        super(ID);

        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    // Config Constructor

    public PlaySoundAction(ConfigurationSection section) {
        super(ID, section);

        this.sound = section.getString(SOUND);
        this.volume = (float) section.getDouble(VOLUME, 1.0);
        this.pitch = (float) section.getDouble(PITCH, 1.0);
    }

    @Override
    public void write(ConfigurationSection section) {
        if (this.sound == null)
            return;

        section.set(SOUND, this.sound);

        if (this.volume != 1.0)
            section.set(VOLUME, this.volume);

        if (this.pitch != 1.0)
            section.set(PITCH, this.pitch);
    }

    @Override
    public void run(Context context) {
        Optional<Player> player = context.get(Parameters.PLAYER);
        if (player.isEmpty() || this.sound == null)
            return;

        player.get().playSound(player.get(), this.sound, this.volume, this.pitch);
    }

    public String getSound() {
        return this.sound;
    }

    // Static Constructors

    public static PlaySoundAction of(Sound sound) {
        return new PlaySoundAction(sound.getKey().toString());
    }

    public static PlaySoundAction of(Sound sound, float volume, float pitch) {
        return new PlaySoundAction(sound.getKey().toString(), volume, pitch);
    }

}
