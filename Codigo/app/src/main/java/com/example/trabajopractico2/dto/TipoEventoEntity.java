package com.example.trabajopractico2.dto;

class TipoEventoEntity {

    private String type_events;
    private int dni;
    private String description;
    private int id;

    public TipoEventoEntity() {
    }

    public TipoEventoEntity(String type_events, int dni, String description, int id) {
        this.type_events = type_events;
        this.dni = dni;
        this.description = description;
        this.id = id;
    }

    public String getType_events() {
        return type_events;
    }

    public void setType_events(String type_events) {
        this.type_events = type_events;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
