package pl.lazyteam.pricebreaker.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    ProductController productController;

    @Autowired
    private MockMvc mockMvc;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() {
        if (!setUpIsDone) {
            productController = mock(ProductController.class);
            ProductInfo productInfo = new ProductInfo();
            productInfo.setProductName("test");
            productInfo.setProductId("1");
            productInfo.setId((long)1);
            productController.add(productInfo);
            productInfo = new ProductInfo();
            productInfo.setProductName("test2");
            productController.add(productInfo);
        }
        setUpIsDone = true;
    }

    @Test
    public void getProductById() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("test");
        productInfo.setProductId("1");
        productInfo.setId((long)1);
        productController.add(productInfo);
    }

    @Test
    public void getAllProducts() throws Exception {
        //productController = mock(ProductController.class);
        List<ProductInfo> productInfos=productController.getAllProducts(null);
        assertEquals(productInfos.size(),0);

    }

    @Test
    public void delete() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("test");
        productInfo.setProductId("1");
        productInfo.setId((long)1);
        productController.add(productInfo);
        this.mockMvc.perform(get("/products/delete/{id}",1)).andExpect(status().is3xxRedirection());
    }

    @Test
    public void add() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("test");
        productInfo.setProductId("1");
        productInfo.setId((long)1);
    }

    @Test
    public void searchForm() throws Exception {
        //productController = mock(ProductController.class);
        this.mockMvc.perform(get("/search")).andExpect(status().isOk())
                .andExpect(view().name("/search"));

    }

    @Test
    public void search() throws Exception {
        //productController = mock(ProductController.class);
        this.mockMvc.perform(get("/search")).andExpect(status().isOk())
                .andExpect(view().name("/search"));
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(productController).isNotNull();
    }

}