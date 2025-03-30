package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;


import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@Service
@RequiredArgsConstructor
public class SeleniumService {
    private final WebDriver webDriver;

    //@Before
    public void setup(){
        webDriver.get("http://localhost:8080");
        
    }
    @Test
    public void testProductDetailPage() {
        // 1️⃣ Mở trang chi tiết sản phẩm
        webDriver.get("https://example.com/product/123");  // Thay bằng URL thực tế

        // 2️⃣ Kiểm tra thông tin sản phẩm hiển thị đúng
        WebElement productName = webDriver.findElement(By.id("product-name"));
        WebElement productPrice = webDriver.findElement(By.id("product-price"));
        WebElement productDescription = webDriver.findElement(By.id("product-description"));

        assertNotNull(productName);
        assertNotNull(productPrice);
        assertNotNull(productDescription);
        System.out.println("✅ Thông tin sản phẩm hiển thị đúng");

        // 3️⃣ Thêm sản phẩm vào giỏ hàng
        WebElement addToCartButton = webDriver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        // 4️⃣ Kiểm tra giỏ hàng
        webDriver.get("https://example.com/cart");  // Chuyển đến trang giỏ hàng
        WebElement cartProduct = webDriver.findElement(By.xpath("//div[@class='cart-item'][contains(.,'Tên Sản Phẩm')]"));
        assertNotNull(cartProduct);
        System.out.println("✅ Sản phẩm đã được thêm vào giỏ hàng");

        // 5️⃣ Kiểm tra nút Mua ngay
        WebElement buyNowButton = webDriver.findElement(By.id("buy-now"));
        buyNowButton.click();
        assertEquals("https://example.com/checkout", webDriver.getCurrentUrl());
        System.out.println("✅ Nút Mua ngay chuyển đúng đến trang thanh toán");
    }
    //*[@id="sll2-normal-pdp-main"]/div/div/div/div[2]/section/section[2]/div/div[5]/div/div/button[2]

    @Test
    public void testQuantityButtons() {
        // 1️⃣ Kiểm tra nút (+) và (-)
        WebElement quantityInput = webDriver.findElement(By.id("product-quantity"));
        WebElement increaseButton = webDriver.findElement(By.id("increase-qty"));
        WebElement decreaseButton = webDriver.findElement(By.id("decrease-qty"));

        increaseButton.click();
        assertEquals("2", quantityInput.getAttribute("value"), "❌ Nút (+) không hoạt động đúng");

        decreaseButton.click();
        assertEquals("1", quantityInput.getAttribute("value"), "❌ Nút (-) không hoạt động đúng");

        System.out.println("✅ Kiểm tra nút (+) và (-) thành công");
    }

    @Test
    public void testStockLimit() {
        // 2️⃣ Kiểm tra số lượng hàng còn lại
        WebElement stockLabel = webDriver.findElement(By.id("stock-available"));
        int stock = Integer.parseInt(stockLabel.getText().replaceAll("[^0-9]", ""));

        WebElement quantityInput = webDriver.findElement(By.id("product-quantity"));
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(stock + 1)); // Nhập số lớn hơn tồn kho

        WebElement addToCartButton = webDriver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement errorMessage = webDriver.findElement(By.id("error-message"));
        assertTrue(errorMessage.isDisplayed(), "❌ Không hiển thị thông báo lỗi khi nhập số lượng vượt quá tồn kho");

        System.out.println("✅ Kiểm tra số lượng hàng còn lại thành công");
    }
    @Test
    public void testProductVariationSelection() {
        // 4️⃣ Kiểm tra chọn kích thước, màu sắc, kiểu dáng
        WebElement sizeDropdown = webDriver.findElement(By.id("size-options"));
        WebElement colorDropdown = webDriver.findElement(By.id("color-options"));
        WebElement productDescription = webDriver.findElement(By.id("product-description"));

        sizeDropdown.sendKeys("L"); // Chọn kích thước L
        colorDropdown.sendKeys("Red"); // Chọn màu đỏ

        String descriptionText = productDescription.getText();
        assertTrue(descriptionText.contains("L") && descriptionText.contains("Red"), "❌ Thông tin sản phẩm không thay đổi theo biến thể");

        System.out.println("✅ Kiểm tra biến thể sản phẩm thành công");
    }

    @Test
    public void testOutOfStockVariation() {
        // 5️⃣ Kiểm tra nếu biến thể hết hàng, có hiển thị "Hết hàng" không?
        WebElement sizeDropdown = webDriver.findElement(By.id("size-options"));
        WebElement colorDropdown = webDriver.findElement(By.id("color-options"));

        sizeDropdown.sendKeys("XL"); // Chọn kích thước XL
        colorDropdown.sendKeys("Blue"); // Chọn màu xanh

        WebElement outOfStockMessage = webDriver.findElement(By.id("out-of-stock-message"));
        assertTrue(outOfStockMessage.isDisplayed(), "❌ Không hiển thị thông báo hết hàng");

        System.out.println("✅ Kiểm tra biến thể hết hàng thành công");
    }

    // kiểm tra khi bấm mua deal sốc
}
