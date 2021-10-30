package com.millenium.milleniumserver.payload.requests.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEditRequest {
    @NotNull
    private Integer id;
    @NotNull
    @NotEmpty
    private String newName;
}
