package com.financas.infrastructure.persistence.repository;

import com.financas.domain.model.Category;
import com.financas.domain.repository.CategoryRepository;
import com.financas.infrastructure.persistence.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository jpa;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category c) {
        return mapper.toDomain(jpa.save(mapper.toEntity(c)));
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Category> findByUserId(UUID userId) {
        return jpa.findByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
