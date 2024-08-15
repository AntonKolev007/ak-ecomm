package com.ecommerce.project.service.impl;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.request.CartRequestDTO;
import com.ecommerce.project.payload.request.ProductRequestDTO;
import com.ecommerce.project.payload.response.ProductResponseDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.service.FileService;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final FileService fileService;
    @Value("${project.image}")
    private String path;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository, CartRepository cartRepository, CartService cartService, FileService fileService,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductRequestDTO addProduct(Long categoryId, ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category",
                                "categoryId",
                                categoryId));
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productRequestDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productRequestDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice()
                    - (((product.getDiscount()) * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductRequestDTO.class);
        } else {
            throw new APIException("Product already exist!");
        }
    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProduct = productRepository.findAll(pageDetails);
        List<Product> products = pageProduct.getContent();

        List<ProductRequestDTO> productRequestDTOS = products.stream()
                .map(prod -> modelMapper.map(prod, ProductRequestDTO.class))
                .toList();

        if (products.isEmpty()) {
            throw new APIException("No products initialized");
        }
        return getProductResponseDTO(pageProduct, productRequestDTOS);
    }

    @Override
    public ProductResponseDTO searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category",
                                "categoryId",
                                categoryId));
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProduct = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);
        List<Product> products = pageProduct.getContent();

        if (products.isEmpty()) {
            throw new APIException("No products in " + category.getCategoryName() + "!");
        }

        List<ProductRequestDTO> productRequestDTOS = products.stream()
                .map(prod -> modelMapper.map(prod, ProductRequestDTO.class))
                .toList();
        return getProductResponseDTO(pageProduct, productRequestDTOS);
    }

    @Override
    public ProductResponseDTO searchProductByKeyword(String keyWord,
                                                     Integer pageNumber,
                                                     Integer pageSize,
                                                     String sortBy,
                                                     String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productsByKeyWord = productRepository.findByProductNameLikeIgnoreCase('%' + keyWord + '%', pageDetails);

        List<Product> products = productsByKeyWord.getContent();
        List<ProductRequestDTO> productRequestDTOS = products.stream()
                .map(prod -> modelMapper.map(prod, ProductRequestDTO.class))
                .toList();
        if (products.isEmpty()) {
            throw new APIException("No product with keyword: " + keyWord);
        }
        return getProductResponseDTO(productsByKeyWord, productRequestDTOS);
    }

    @Override
    public ProductRequestDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product",
                                "productID",
                                productId));
        Product product = modelMapper.map(productRequestDTO, Product.class);

        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        double specialPrice = product.getPrice()
                - (((product.getDiscount()) * 0.01) * product.getPrice());
        productFromDB.setSpecialPrice(specialPrice);
        Product updatedProduct = productRepository.save(productFromDB);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartRequestDTO> cartDTOs = carts.stream().map(cart -> {
            CartRequestDTO cartDTO = modelMapper.map(cart, CartRequestDTO.class);

            List<ProductRequestDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductRequestDTO.class))
                    .collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;
        }).toList();

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(updatedProduct, ProductRequestDTO.class);
    }

    @Override
    public ProductRequestDTO deleteProduct(Long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductRequestDTO.class);
    }

    @Override
    public ProductRequestDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product",
                        "productId",
                        productId));
        String fileName = fileService.uploadImage(path, image);
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDB);
        return modelMapper.map(updatedProduct, ProductRequestDTO.class);
    }

    @Override
    public List<ProductRequestDTO> getFeaturedProducts() {
        List<Product> featuredProducts = productRepository.findTop3ByOrderBySpecialPriceDesc();
        return featuredProducts.stream()
                .map(product -> modelMapper.map(product, ProductRequestDTO.class))
                .collect(Collectors.toList());
    }

    @NotNull
    private ProductResponseDTO getProductResponseDTO(Page<Product> productsByKeyWord, List<ProductRequestDTO> productRequestDTOS) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        productResponseDTO.setPageNumber(productsByKeyWord.getNumber());
        productResponseDTO.setPageSize(productsByKeyWord.getSize());
        productResponseDTO.setTotalElements(productsByKeyWord.getTotalElements());
        productResponseDTO.setTotalPages(productsByKeyWord.getTotalPages());
        productResponseDTO.setLastPage(productsByKeyWord.isLast());
        return productResponseDTO;
    }
}
