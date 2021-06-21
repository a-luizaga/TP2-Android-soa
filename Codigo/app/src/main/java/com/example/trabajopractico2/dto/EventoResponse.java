package com.example.trabajopractico2.dto;

public class EventoResponse {

    private Boolean success;
    private String env;
    private TipoEventoEntity event;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public TipoEventoEntity getEvent() {
        return event;
    }

    public void setEvent(TipoEventoEntity event) {
        this.event = event;
    }
}
