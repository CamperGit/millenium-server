package com.millenium.milleniumserver.repos.expenses;

import com.millenium.milleniumserver.entity.expenses.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepo extends JpaRepository<Category, Integer> {
}
