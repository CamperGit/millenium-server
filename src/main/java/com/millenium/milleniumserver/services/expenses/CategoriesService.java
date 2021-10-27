package com.millenium.milleniumserver.services.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.repos.expenses.CategoriesRepo;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoriesService {
    private CategoriesRepo categoriesRepo;
    private TeamEntityService teamEntityService;

    public Category createNewCategory(String name, TeamEntity teamEntity) {
        Category category = new Category(name, new ArrayList<>(), teamEntity);
        return categoriesRepo.save(category);
    }

    public Category findCategoryById(Integer categoryId) {
        return categoriesRepo.getById(categoryId);
    }

    @Autowired
    public void setCategoriesRepo(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }
}
