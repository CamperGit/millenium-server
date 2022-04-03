package com.millenium.milleniumserver.payload.requests.expenses;

import com.millenium.milleniumserver.entity.expenses.Category;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExpensesFilterPayload {
    private Integer categoryId;
    private String name;
    private Double minPrice;
    private Double maxPrice;
}
