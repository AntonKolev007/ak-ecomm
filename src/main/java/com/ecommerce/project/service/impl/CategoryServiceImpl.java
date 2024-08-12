package com.ecommerce.project.service.impl;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponseDTO getAllCategories(Integer pageNumber,
                                                Integer pageSize,
                                                String sortBy,
                                                String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent(); //categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("No categories initialized!");
        } else {
            List<CategoryRequestDTO> categoryRequestDTOS =
                    categories.stream()
                            .map(cat -> modelMapper.map(cat, CategoryRequestDTO.class))
                            .toList();
            CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

            categoryResponseDTO.setContent(categoryRequestDTOS);
            categoryResponseDTO.setPageNumber(categoryPage.getNumber());
            categoryResponseDTO.setPageSize(categoryPage.getSize());
            categoryResponseDTO.setTotalElements(categoryPage.getTotalElements());
            categoryResponseDTO.setTotalPages(categoryPage.getTotalPages());
            categoryResponseDTO.setLastPages(categoryPage.isLast());
            return categoryResponseDTO;
        }
    }

    @Override
    public CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = modelMapper.map(categoryRequestDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());

        if (categoryFromDB != null) {
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists!");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO deleteCategory(Long categoryId) {

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId) {
        Category category = modelMapper.map(categoryRequestDTO, Category.class);
        Category categoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        category.setCategoryId(categoryId);
        categoryFromDB = categoryRepository.save(category);

        return modelMapper.map(categoryFromDB, CategoryRequestDTO.class);
    }
}
