package com.example.demo.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdatePassVo {

    @NotNull
    @Size(min = 3, max = 12)
    private String oldPass;

    @NotNull
    @Size(min = 3, max = 12)
    private String newPass;
}
