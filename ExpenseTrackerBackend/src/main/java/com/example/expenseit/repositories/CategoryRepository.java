package com.example.expenseit.repositories;

import com.example.expenseit.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> findAllByUserId_UserId(Integer userId);

    Category findByCategoryIdAndUserId_UserId(Integer categoryId, Integer userId);

    List<Category> deleteByCategoryIdAndUserId_UserId(Integer categoryId, Integer clientId);

    Category findByTitleAndUserId_UserId(String title, Integer userId);
}
