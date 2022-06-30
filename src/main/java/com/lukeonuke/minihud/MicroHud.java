package com.lukeonuke.minihud;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicroHud implements ModInitializer {
    @Override
    public void onInitialize() {
        Logger logger = LoggerFactory.getLogger("microhud");
        logger.info("Microhud initialised");
    }
}
