/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Rafa
 */
public class Price {
    public static char SYMBOL = '€';
    public static double TAX = 21.d;

    public double price, tax, totalPrice;

    public Price(double price) {
        this.price = round2(price);
        this.tax = round2((TAX / 100) * price);
        this.totalPrice = round2(price + tax);
    }
    
    public double getTax() {
        return tax;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public double getPrice() {
        return price;
    }
    
    @Override
    public String toString() {
        return price +""+ SYMBOL + ", IVA: " + tax + SYMBOL + " (" + TAX + "%)" + " TOTAL: " + totalPrice + SYMBOL;
    }
    
    private static double round2(double num) {
        return Math.round(num*100)/100.d;
    }
}
