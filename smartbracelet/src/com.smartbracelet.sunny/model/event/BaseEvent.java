package com.smartbracelet.sunny.model.event;

/**
 * Created by sunny on 2015/11/20.
 * to accecpt evnetbus event base class
 */
public class BaseEvent {
    private int id;
    private String eventName;
    private EventType mEventType;

    public void setEventType(EventType mEventType) {
        this.mEventType = mEventType;
    }

    public EventType getEventType() {
        return mEventType;
    }

    public BaseEvent() {
    }

    public BaseEvent(EventType mEventType) {
        this.mEventType = mEventType;
    }

    public enum EventType {
        LOGIN,
        LOGINOUT,
        REGISTER,
        CAHNGE_INFO,
        BLOOTH_PRESSURE_VISIBLE,
        BLOOTH_PRESSURE_GONE,
        BREATH_PRESSURE_VISIBLE,
        BREATH_PRESSURE_GONE,
        HEART_RATE_VISIBLE,
        HEART_RATE_GONE,
        MOOD_VISIBLE,
        MOOD_GONE,
        TIRED_VISIBLE,
        TIRED_GONE,

        //请求网络
        STEP,
        BLOOTH_PRESSURE,
        BREATH,
        HEART_RATE,
        TIRED,
        MOOD
    }
}
