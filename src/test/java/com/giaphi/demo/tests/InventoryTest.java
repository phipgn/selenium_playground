package com.giaphi.demo.tests;

import com.giaphi.demo.pages.InventoryItemPage;
import com.giaphi.demo.pages.InventoryPage;
import com.giaphi.demo.pages.LoginPage;
import com.giaphi.demo.pages.ProductItem;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InventoryTest extends BaseTest {
    private InventoryPage inventoryPage;
    private LoginPage loginPage;
    private WebDriver driver;

    @BeforeMethod
    public void BeforeMethod() {
        driver = setUpDriver();
        loginPage = new LoginPage(driver);
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
    }

    @Test
    public void test_verifyUIElements() {
        Assert.assertTrue(inventoryPage.pageHeader().isDisplayed());
        Assert.assertTrue(inventoryPage.getCartLink().isDisplayed());
        Assert.assertTrue(inventoryPage.getSortProduct().isDisplayed());
        Assert.assertTrue(inventoryPage.getBurgerButton().isDisplayed());
        Assert.assertTrue(inventoryPage.getFooter().isDisplayed());
    }

    @Test
    public void test_clickOnProductItemSuccessfully() {
        int itemIndex = 3;
        
        List<ProductItem> items = inventoryPage.getProductItems();
        ProductItem productItem = items.get(itemIndex);

        String expectedTitle = productItem.getTitle();
        String expectedDesc = productItem.getDescription();
        String expectedPrice = productItem.getPrice();
        String expectedImage = productItem.getImage();

        // click on title item to open inventory item page
        //InventoryItemPage inventoryItemPage = productItem.click();

        // click on image item to open inventory item page
        InventoryItemPage inventoryItemPage = productItem.clickImage();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory-item.html"), "Did not navigate to the item detail page.");

        String actualTitle = inventoryItemPage.getTitle();
        String actualDesc = inventoryItemPage.getDescription();
        String actualPrice = inventoryItemPage.getPrice();
        String actualImage = inventoryItemPage.getItemImage();

        Assert.assertEquals(actualTitle, expectedTitle);
        Assert.assertEquals(actualDesc, expectedDesc);
        Assert.assertEquals(actualPrice, expectedPrice);
        Assert.assertEquals(actualImage, expectedImage);
    }

    @Test
    public void test_clickOnAddToCartOnProductItem_001() {
        Assert.assertEquals(0, inventoryPage.getCartItemsCount());
        
        List<ProductItem> items = inventoryPage.getProductItems();
        ProductItem productItem_2 = items.get(2);
        productItem_2.clickAddToCartBtn();
        Assert.assertEquals(1, inventoryPage.getCartItemsCount());

        ProductItem productItem_4 = items.get(4);
        productItem_4.clickAddToCartBtn();
        Assert.assertEquals(2, inventoryPage.getCartItemsCount());
    
        productItem_2.clickRemoveBtn();
        Assert.assertEquals(1, inventoryPage.getCartItemsCount());

        productItem_4.clickRemoveBtn();
        Assert.assertEquals(0, inventoryPage.getCartItemsCount());
    }

    @Test
    public void test_clickOnAddToCartOnProductItem_002() {
        Assert.assertEquals(0, inventoryPage.getCartItemsCount());
        List<ProductItem> items = inventoryPage.getProductItems();

        int count = 0;
        for(ProductItem item : items) {
            item.clickAddToCartBtn();
            count++;
            Assert.assertEquals(count, inventoryPage.getCartItemsCount());
        }

        for(ProductItem item : items) {
            item.clickRemoveBtn();
            count--;
            Assert.assertEquals(count, inventoryPage.getCartItemsCount());
        }

        Assert.assertEquals(0, inventoryPage.getCartItemsCount());
    }

    @Test
    public void test_clickOnAddToCartAndClickOnTitleItem(){
        int itemIndex = 1;

        List<ProductItem> items = inventoryPage.getProductItems();
        ProductItem productItem = items.get(itemIndex);
        productItem.clickAddToCartBtn();

        InventoryItemPage inventoryItemPage = productItem.click();
        Assert.assertTrue(inventoryItemPage.getRemoveBtn().isDisplayed());
        Assert.assertFalse(inventoryItemPage.isAddToCartBtnDisplayed());
    }

    // @Test
    // public void test_clickOnAddToCartOnInventoryItemDetailPage(){
    //     int itemIndex = 1;
    //     List<ProductItem> items = inventoryPage.getProductItems();
    //     ProductItem productItem = items.get(itemIndex);
    //     InventoryItemPage inventoryItemPage = productItem.click();
    //     inventoryItemPage.clickAddToCartBtn();
    //     String removeTxt = inventoryItemPage.getRemoveBtn();
    //     String cartBadge = inventoryItemPage.getCartBadge();
    //     Assert.assertEquals(removeTxt,"Remove");
    //     Assert.assertEquals(cartBadge,"1");
    // }

    @AfterMethod
    public void AfterMethod() {
        driver.quit();
    }
}
