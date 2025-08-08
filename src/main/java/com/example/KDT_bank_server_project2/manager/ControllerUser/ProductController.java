package com.example.KDT_bank_server_project2.manager.ControllerUser;


import com.example.KDT_bank_server_project2.manager.DtoUser.ApiResponseUser;
import com.example.KDT_bank_server_project2.manager.DtoUser.ProductCreateRequestDto;
import com.example.KDT_bank_server_project2.manager.DtoUser.ProductResponseDto;
import com.example.KDT_bank_server_project2.manager.EntityUser.Product;
import com.example.KDT_bank_server_project2.manager.ServiceUser.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 생성
    @PostMapping
    public ResponseEntity<ApiResponseUser<ProductResponseDto>> createProduct(@Valid @RequestBody ProductCreateRequestDto requestDto) {
        try {
            Product product = convertToEntity(requestDto);
            Product createdProduct = productService.createProduct(product);
            ProductResponseDto responseDto = new ProductResponseDto(createdProduct);

            return ResponseEntity.ok(ApiResponseUser.success("상품이 성공적으로 생성되었습니다.", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseUser.error(e.getMessage()));
        }
    }

    // 모든 상품 조회
    @GetMapping
    public ResponseEntity<ApiResponseUser<List<ProductResponseDto>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> responseDtos = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 활성 상품만 조회
    @GetMapping("/active")
    public ResponseEntity<ApiResponseUser<List<ProductResponseDto>>> getActiveProducts() {
        List<Product> products = productService.getActiveProducts();
        List<ProductResponseDto> responseDtos = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // ID로 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseUser<ProductResponseDto>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            ProductResponseDto responseDto = new ProductResponseDto(product.get());
            return ResponseEntity.ok(ApiResponseUser.success(responseDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 카테고리별 상품 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponseUser<List<ProductResponseDto>>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductResponseDto> responseDtos = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // 상품 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponseUser<List<ProductResponseDto>>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        List<ProductResponseDto> responseDtos = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseUser.success(responseDtos));
    }

    // DTO -> Entity 변환
    private Product convertToEntity(ProductCreateRequestDto dto) {
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setProductDetail(dto.getProductDetail());
        product.setCategory(dto.getCategory());
        product.setProductCategory(dto.getProductCategory());
        product.setMaxRate(dto.getMaxRate());
        product.setMinRate(dto.getMinRate());
        product.setLimitMoney(dto.getLimitMoney());
        return product;
    }
}
