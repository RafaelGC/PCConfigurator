/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import es.upv.inf.Product;

/**
 *
 * @author Rafa
 */
public class CategoryNames {
    public static String getName(Product.Category category) {
        if (category == Product.Category.CASE) {
            return "Torre";
        }
        else if (category == Product.Category.CPU) {
            return "Procesador";
        }
        else if (category == Product.Category.DVD_WRITER) {
            return "Lector DVD";
        }
        else if (category == Product.Category.FAN) {
            return "Ventilador";
        }
        else if (category == Product.Category.GPU) {
            return "Tarjeta gráfica";
        }
        else if (category == Product.Category.HDD) {
            return "Disco duro";
        }
        else if (category == Product.Category.HDD_SSD) {
            return "Disco SSD";
        }
        else if (category == Product.Category.KEYBOARD) {
            return "Teclado";
        }
        else if (category == Product.Category.MOTHERBOARD) {
            return "Placa base";
        }
        else if (category == Product.Category.MOUSE) {
            return "Ratón";
        }
        else if (category == Product.Category.MULTIREADER) {
            return "Multilector";
        }
        else if (category == Product.Category.POWER_SUPPLY) {
            return "Fuente de alimentación";
        }
        else if (category == Product.Category.RAM) {
            return "RAM";
        }
        else if (category == Product.Category.SCREEN) {
            return "Pantalla";
        }
        else if (category == Product.Category.SPEAKER) {
            return "Altavoces";
        }
        return "Desconocido";
    }
}
