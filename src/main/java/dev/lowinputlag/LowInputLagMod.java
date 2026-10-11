package dev.lowinputlag;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LowInputLagMod implements ClientModInitializer {
    public static final String MOD_ID = "lowinputlag";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LowInputLagConfig.load();
        LOGGER.info("[{}] loaded (enabled={}, applyEveryFrame={}, pollEventsBeforeLook={})",
                MOD_ID,
                LowInputLagConfig.get().enabled,
                LowInputLagConfig.get().applyEveryFrame,
                LowInputLagConfig.get().pollEventsBeforeLook);
    }
}
