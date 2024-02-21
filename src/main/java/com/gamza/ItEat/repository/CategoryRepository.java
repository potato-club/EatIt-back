package com.gamza.ItEat.repository;

import com.gamza.ItEat.entity.CategoryEntity;
import com.gamza.ItEat.enums.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    CategoryEntity findByCategoryName(CategoryName category);
}
