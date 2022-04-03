package com.millenium.milleniumserver.payload.requests.expenses;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpensesFilterPayload {
    private List<String> categories;
    private String name;
    private Double minPrice;
    private Double maxPrice;
}
