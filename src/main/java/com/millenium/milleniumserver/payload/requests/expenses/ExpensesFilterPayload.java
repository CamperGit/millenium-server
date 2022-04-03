package com.millenium.milleniumserver.payload.requests.expenses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.enums.ExpensePriority;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExpensesFilterPayload {
    private Integer categoryId;
    private LocalDateTime createDateAtBottom;
    private LocalDateTime createDateAtTop;
    private List<ExpensePriority> priorityIn;
    private String name;
    private Double minPrice;
    private Double maxPrice;
}
