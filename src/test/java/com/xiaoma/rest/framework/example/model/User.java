package com.xiaoma.rest.framework.example.model;

import javafx.scene.input.RotateEvent;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * @author <a href="mailto:ergal@163.com">vincent.omg</a>
 * @version 1.0
 * @date 16/5/20
 * @since 1.0
 */
public class User {

    private Integer id;

    private String name;

    private String description;

    private Integer status;

    private long distance;

    private float height;

    private double deposits;

    private boolean isMale;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public double getDeposits() {
        return deposits;
    }

    public void setDeposits(double deposits) {
        this.deposits = deposits;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
