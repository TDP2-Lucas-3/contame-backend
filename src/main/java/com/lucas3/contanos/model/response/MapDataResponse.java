package com.lucas3.contanos.model.response;

import java.util.List;

public class MapDataResponse {

    private List<IncidentResponse> incidents;

    private List<HoodData> hoodRanking;

    public MapDataResponse() {
    }

    public MapDataResponse(List<IncidentResponse> incidents, List<HoodData> hoodRanking) {
        this.incidents = incidents;
        this.hoodRanking = hoodRanking;
    }

    public List<IncidentResponse> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<IncidentResponse> incidents) {
        this.incidents = incidents;
    }

    public List<HoodData> getHoodRanking() {
        return hoodRanking;
    }

    public void setHoodRanking(List<HoodData> hoodRanking) {
        this.hoodRanking = hoodRanking;
    }
}
