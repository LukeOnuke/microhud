package com.lukeonuke.minihud.data.type;

import lombok.Getter;

@Getter
public class GeolocationResponse {
    String ip;
    String city;
    String country_name;
    String latitude;
    String longitude;
}
