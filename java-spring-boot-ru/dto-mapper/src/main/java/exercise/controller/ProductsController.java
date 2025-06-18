package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @Autowired
    private ProductMapper productMapper;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> index() {
        var products = productRepository.findAll();

        return products.stream()
            .map(product -> productMapper.map(product))
            .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO show(@PathVariable Long id) {
        var product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.map(product);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO productCreateDTO) {
        var product = productMapper.map(productCreateDTO);
        productRepository.save(product);
        return productMapper.map(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdateDTO) {
        var product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productMapper.update(productUpdateDTO, product);
        productRepository.save(product);
        return productMapper.map(product);
    }
    // END
}
