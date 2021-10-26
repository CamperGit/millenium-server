package com.millenium.milleniumserver.services.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.repos.expenses.CategoriesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoriesService {
    private CategoriesRepo categoriesRepo;

    public Category createNewCategory(String name, TeamEntity teamEntity) {
        Category category = new Category(name, new ArrayList<>(), teamEntity);
        return categoriesRepo.save(category);
    }

    @Autowired
    public void setCategoriesRepo(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }
}
