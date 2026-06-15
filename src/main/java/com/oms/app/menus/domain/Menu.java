package com.oms.app.menus.domain;

import com.oms.app.menus.domain.vo.MenuId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Menu {
    @EmbeddedId
    private MenuId id;

    private String name;
    private String description;
    private String icon;
    private Boolean active;


}
