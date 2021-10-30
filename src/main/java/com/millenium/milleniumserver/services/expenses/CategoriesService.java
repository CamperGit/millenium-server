package com.millenium.milleniumserver.services.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.entity.expenses.Expense;
import com.millenium.milleniumserver.payload.responses.categories.CategoryDeleteResponse;
import com.millenium.milleniumserver.repos.expenses.CategoriesRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoriesService {
    private CategoriesRepo categoriesRepo;

    public Category updateCategory(Integer categoryId, String newName) {
        Category category = categoriesRepo.getById(categoryId);
        category.setName(newName);
        Category updatedCategory = categoriesRepo.save(category);
        Hibernate.initialize(updatedCategory.getExpenses());
        return updatedCategory;
    }

    public Category createNewCategory(String name, TeamEntity teamEntity) {
        Category category = new Category(name, new ArrayList<>(), teamEntity);
        return categoriesRepo.save(category);
    }

    public CategoryDeleteResponse deleteCategory(Integer categoryId, boolean deleteExpenses) {
        CategoryDeleteResponse response = new CategoryDeleteResponse();
        Category categoryToDelete = categoriesRepo.getById(categoryId);
        Hibernate.initialize(categoryToDelete.getExpenses());
        if (!deleteExpenses) {
            Category emptyCategory = findEmptyCategoryInTeam(categoryToDelete.getTeam());
            Hibernate.initialize(emptyCategory.getExpenses());
            List<Expense> expenses = categoryToDelete.getExpenses();
            expenses.forEach(exp -> exp.setCategory(emptyCategory));
            emptyCategory.getExpenses().addAll(expenses);
            categoriesRepo.save(emptyCategory);
            response.setEmptyCategory(emptyCategory);
        }
        categoriesRepo.delete(categoryToDelete);
        response.setDeletedCategory(categoryToDelete);
        return response;
    }

    public Category findEmptyCategoryInTeam(TeamEntity team) {
        List<Category> categories = team.getCategories();
        for (Category category : categories) {
            if (category.getName().equals("EMPTY")) {
                return category;
            }
        }
        throw new IllegalStateException("Team doesn't have empty category");
    }

    public Category findCategoryById(Integer categoryId) {
        return categoriesRepo.getById(categoryId);
    }

    @Autowired
    public void setCategoriesRepo(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }
}
