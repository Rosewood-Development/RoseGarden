package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.Optional;

/**
 * An action that plays a sound to a player.
 */
public class PlaySoundAction extends AbstractAction {

    public static final String ID = "play-sound";

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

        if (section.isConfigurationSection(ID)) {
            this.sound = section.getString(ID + ".sound");
            this.volume = (float) (section.contains(ID + ".volume") ? section.getDouble(ID + ".volume") : 1.0);
            this.pitch = (float) (section.contains(ID + ".pitch") ? section.getDouble(ID + ".pitch") : 1.0);
        } else {
            this.sound = section.contains(ID) ? section.getString(ID) : null;
            this.volume = 1.0F;
            this.pitch = 1.0F;
        }
    }

    @Override
    public void write(ConfigurationSection section) {
        if (this.sound == null)
            return;

        if (this.volume != 1.0 || this.pitch != 1.0) {
            section.set(ID + ".sound", this.sound);
            if (this.volume != 1.0)
                section.set(ID + ".volume", this.volume);

            if (this.pitch != 1.0)
                section.set(ID + ".pitch", this.pitch);
        } else {
            section.set(ID, this.sound);
        }
    }

    @Override
    public void run(Context context) {
        Optional<Player> player = context.get(Parameters.PLAYER);
        if (!player.isPresent() || this.sound == null)
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
