package com.takeaway.game.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Player implements Serializable {

    private static final long serialVersionUID = 3134592436804106224L;

    private Integer id;

    private Integer number;

}
