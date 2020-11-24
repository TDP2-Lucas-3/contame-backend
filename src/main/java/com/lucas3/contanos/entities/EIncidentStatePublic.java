package com.lucas3.contanos.entities;

import java.util.HashMap;
import java.util.Map;

public enum EIncidentStatePublic {
    INGRESADO("Ingresado"),
    ACEPTADO("Aceptado"),
    EN_PROCESO("En proceso"),
    RESUELTO("Resuelto"),
    INVALIDO("Inválido"),
    PENDIENTE_OBRA("Pendiente de obra mayor");

    private String value;

    EIncidentStatePublic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, EIncidentStatePublic> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(EIncidentStatePublic state : EIncidentStatePublic.values())
        {
            lookup.put(state.getValue(), state);
        }
    }

    //This method can be used for reverse lookup purpose
    public static EIncidentStatePublic get(String value)
    {
        return lookup.get(value);
    }


}
