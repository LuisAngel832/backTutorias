package com.codespace.tutorias.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
public class Tutorado extends Usuario {

    private boolean recordatorio = false;

    public Tutorado() {
        this.setRol(EnumRol.TUTORADO);
    }
}
