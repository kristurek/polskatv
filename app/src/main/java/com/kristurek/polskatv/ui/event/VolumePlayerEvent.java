package com.kristurek.polskatv.ui.event;

import com.kristurek.polskatv.ui.arch.Event;

public class VolumePlayerEvent implements Event {

    private float volume;

    public VolumePlayerEvent(float volume) {
        this.volume = volume;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VolumePlayerEvent{");
        sb.append("volume=").append(volume);
        sb.append('}');
        return sb.toString();
    }
}
