package com.millenium.milleniumserver.payload.requests.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDeleteRequest {
    @NotNull
    private Integer id;
    @NotNull
    private Boolean deleteExpenses;
}
