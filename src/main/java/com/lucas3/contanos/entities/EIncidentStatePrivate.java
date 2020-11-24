package com.lucas3.contanos.entities;

import java.util.HashMap;
import java.util.Map;

public enum EIncidentStatePrivate {
    INSPECCION("Inspección"),
    ASIGNADO_PROVEEDOR("Asignado a proveedor"),
    PRESUPUESTO_APROBADO("Presupuesto aprobado"),
    RESUELTO("Incidente resuelto"),
    RECHAZO_PRESUPUESTO(" Rechazo por falta de presupuesto"),
    PENDIENTE_OBRA("Pendiente de obra integral");

    private String value;

    EIncidentStatePrivate(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, EIncidentStatePrivate> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(EIncidentStatePrivate state : EIncidentStatePrivate.values())
        {
            lookup.put(state.getValue(), state);
        }
    }

    //This method can be used for reverse lookup purpose
    public static EIncidentStatePrivate get(String value)
    {
        return lookup.get(value);
    }

}
