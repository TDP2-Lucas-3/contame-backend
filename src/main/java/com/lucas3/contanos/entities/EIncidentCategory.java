package com.lucas3.contanos.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EIncidentCategory {
    VIA_PUBLICA("Vía Pública"),
    LIMPIEZA("Limpieza"),
    ESPACIOS_VERDES("Espacios verdes y arbolado"),
    USO_ESPACIO("Uso del espacio público"),
    ALUMBRADO("Alumbrado Público"),
    AUTOS("Autos Abandonados");

    private String value;


    EIncidentCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, EIncidentCategory> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(EIncidentCategory state : EIncidentCategory.values())
        {
            lookup.put(state.getValue(), state);
        }
    }

    //This method can be used for reverse lookup purpose
    public static EIncidentCategory get(String value)
    {
        return lookup.get(value);
    }

}
