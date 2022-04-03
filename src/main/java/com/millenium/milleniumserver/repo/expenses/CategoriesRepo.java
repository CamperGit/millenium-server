package com.millenium.milleniumserver.repo.expenses;

import com.millenium.milleniumserver.entity.expenses.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepo extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
}
