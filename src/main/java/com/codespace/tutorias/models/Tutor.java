package com.codespace.tutorias.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
public class Tutor extends Usuario {

    public Tutor() {
        this.setRol(EnumRol.TUTOR);
    }
}
