package com.lukeonuke.minihud.renderer.module;

import com.lukeonuke.minihud.renderer.MicroHudRenderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MHDDateTimeRendererModule implements MicroHudRendererModule{
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
    LocalDateTime date;

    @Override
    public String getName() {
        return "Date and time";
    }

    @Override
    public String render(float deltaTick) {
        date = LocalDateTime.now();
        return dateTimeFormatter.format(date);
    }
}
