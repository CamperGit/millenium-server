package com.millenium.milleniumserver.payload.requests.expenses;

import com.millenium.milleniumserver.enums.ExpensePriority;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ExpenseEditRequest {
    @NotNull
    private Long expenseId;
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    @Size(max = 500)
    private String description;
    @NotNull
    private ExpensePriority priority;
    @NotNull
    private Integer categoryId;
    private Double fixedPrice;
    private Double minPrice;
    private Double maxPrice;
}
