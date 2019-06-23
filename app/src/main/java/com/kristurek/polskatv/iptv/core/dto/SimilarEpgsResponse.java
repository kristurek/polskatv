package com.kristurek.polskatv.iptv.core.dto;


import com.kristurek.polskatv.iptv.core.dto.common.Epg;

import java.util.List;

public class SimilarEpgsResponse {

    private List<Epg> epgs;

    public List<Epg> getEpgs() {
        return epgs;
    }

    public void setEpgs(List<Epg> epgs) {
        this.epgs = epgs;
    }

    @Override
    public String toString() {
        return "SimilarEpgsResponse{" +
                "epgs=" + epgs +
                '}';
    }
}
