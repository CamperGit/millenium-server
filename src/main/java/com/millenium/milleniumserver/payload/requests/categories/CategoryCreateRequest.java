package com.millenium.milleniumserver.payload.requests.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
public class CategoryCreateRequest {
    @NotNull
    private Integer teamId;
    @NotNull
    @NotBlank
    private String name;
}
