package com.millenium.milleniumserver.payload.requests.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
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
    @NotBlank
    private String name;
    @NotNull
    private Integer teamId;
}
