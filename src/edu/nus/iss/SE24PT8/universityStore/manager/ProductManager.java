/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.SE24PT8.universityStore.manager;

import edu.nus.iss.SE24PT8.universityStore.domain.Category;
import edu.nus.iss.SE24PT8.universityStore.domain.Product;
import edu.nus.iss.SE24PT8.universityStore.util.DataAdapter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author misitesawn
 */
public class ProductManager implements IManager{
    //test git

    private static ProductManager Instance = null;
    private ArrayList<Product> productList;

    private static CategoryManager categoryManager;

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public static ProductManager getInstance() {
        if (Instance == null) {
            Instance = new ProductManager();
        }
        return Instance;

    }

    /**
     * Read all product List from file Set each product Category
     *
     *
     */
    private ProductManager() {
        categoryManager = CategoryManager.getInstance();
        productList = new ArrayList<>();
        productList = DataAdapter.loadProducts();
        setProductCategory();

    }

    /**
     * Set Category of each Products
     *
     *
     */
    private void setProductCategory() {

        Category category;
        Iterator<Product> i = productList.iterator();
        while (i.hasNext()) {
            Product product = i.next();
            if (product.getCategory() == null) {
                category = categoryManager.getCategory(product.getCategoryCode());
                product.setCategory(category);
            }
        }
    }

    /**
     * Add new Product to ProductList
     *
     * @param product
     *
     */
    
    //0-Product ID
    //  1-Product Name
    ///  2-Product Description
    //  3-QuantityAvailable
    //  4-Price
    //  5-Barcode Number
    // 6-Reorder Quantity
    //  7-Order Quantity
    public void addNewProduct(String productNmae, String briefDesp, int qty, double price, String barCode, int reorderQty, int orderQty, Category category) {
        // new Product(productId, productName, briefDesp, 0, 0, barcode, 0, 0);
        Product product;
        System.out.println(getProductCountInCategory(category));
        String productId = category.getCategoryCode() + "/" + Integer.toString(getProductCountInCategory(category) + 1);

        product = new Product(productId, productNmae, briefDesp, qty, price, barCode, reorderQty, orderQty, category);
        productList.add(product);

        saveData();

    }
    
    /**
     * Get the number of products in a Category
     * @reuturn Product Count 
     * @param category
     */
    public int getProductCountInCategory(Category category){
        int count = 0;
        if ( getProductsByCategory(category).size() > 0) 
               count = getProductsByCategory(category).size();
        return count;
    }

    /**
     * Write Product To File
     * Must call after add, update, delete product item
     *
     */
    public void saveData() {
        DataAdapter.writeProducts(productList);
    }

    /**
     * Get all products in a Category
     *
     * @param category
     * @return Products
     */
    public ArrayList<Product> getProductsByCategory(Category category) {

        ArrayList<Product> products = new ArrayList<>();
        Iterator <Product> i = productList.iterator();
        while (i.hasNext()){
            Product product  = i.next();
            //need to test equals methods
            if (product.getCategory().equals(category)){
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Return boolean check if the bar code already exists
     *
     * @param barcode
     * @return boolean
     */
    public boolean isvalidBarCode(String barcode) {
        Iterator<Product> i = productList.iterator();
        while (i.hasNext()) {
            Product product = i.next();
            if (product.getBarcode().equalsIgnoreCase(barcode)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return boolean check if the bar code already exists
     *
     * @param barcode
     * @return boolean
     */
//    public Product getProduct(String barcode) {
//
//        Iterator<Product> i = this.productList.iterator();
//        while (i.hasNext()) {
//            Product product = i.next();
//            if (product.getBarcode().equalsIgnoreCase(barcode)) {
//                return product;
//            }
//        }
//
//        return null;
//    }
    
    
    public Product getProductByID(String producID){
        for(Product product:productList){
            if(product.getProductId().equalsIgnoreCase(producID)){
                return product;
            }
        }
        return null;
    }//end get PRoductby ID
    
    public Product getProductByBarcode(String barcode){
        for(Product product:productList){
            if(product.getBarcode().equalsIgnoreCase(barcode)){
                return product;
            }
        }
        return null;
    }
    
    
    
    
    
    
    
    

    /**
     * Return List of Product that have lower inventory quantity
     *
     * @return Product List
     */
    public ArrayList<Product> getLowerInventoryProducts() {
        ArrayList<Product> lowerInventoroyProducts = new ArrayList<>();
        Iterator<Product> i = this.productList.iterator();
        while (i.hasNext()) {
            Product product = i.next();
            if (product.getQty() <= product.getReorderQty()) {
                lowerInventoroyProducts.add(product);
            }
        }

        return lowerInventoroyProducts;
    }

    /**
     * Return List of Product that have lower inventory quantity
     * Call by Transaction during checkout
     * @param productList
     * @return Product List
     */
    public ArrayList<Product> getLowerInventoryProducts(ArrayList<Product> productList) {
        ArrayList<Product> lowerInventoroyProducts = new ArrayList<>();
        Iterator<Product> i = productList.iterator();
        while (i.hasNext()) {
            Product product = i.next();
            if (product.getQty() <= product.getReorderQty()) {
                lowerInventoroyProducts.add(product);
            }
        }

        return lowerInventoroyProducts;
    }

    
     @Override
    public void getRelatedObjects() {
       CategoryManager mgrCategory=CategoryManager.getInstance();
       
       for(Product product:productList){
          product.setCategory(mgrCategory.getCategory(product.getCategoryCode()));
       }
    }
}
