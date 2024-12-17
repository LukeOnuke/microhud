package com.lukeonuke.minihud.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class NameLookupData {
    private Integer id;
    private String name;
    private String uuid;
    private String discordId;
    private String tag;
    private String avatar;
    private String guildId;

    @Override
    public String toString() {
        return "NameLookupData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", discordId='" + discordId + '\'' +
                ", tag='" + tag + '\'' +
                ", avatar='" + avatar + '\'' +
                ", guildId='" + guildId + '\'' +
                '}';
    }
}
