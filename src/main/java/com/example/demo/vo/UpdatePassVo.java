package com.example.demo.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UpdatePassVo {

    @NotNull
    @Length(min = 3, max = 12)
    private String oldPass;

    @NotNull
    @Length(min = 3, max = 12)
    private String newPass;

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    @Override
    public String toString() {
        return "UpdatePassVo{" +
                "oldPass='" + oldPass + '\'' +
                ", newPass='" + newPass + '\'' +
                '}';
    }
}
