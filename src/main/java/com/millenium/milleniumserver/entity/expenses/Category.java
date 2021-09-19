package com.millenium.milleniumserver.entity.expenses;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "categories", schema = "millenium")
public class Category {
    private Integer categoryId;
    private String name;
    @JsonManagedReference
    private List<Expense> expenses;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    public Integer getCategoryId() {
        return categoryId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "category")
    public List<Expense> getExpenses() {
        return expenses;
    }
}
