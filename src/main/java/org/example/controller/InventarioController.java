package org.example.controller;

import org.example.model.Productos;
import org.example.service.ProductoService;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class InventarioController {

    private final ProductoService productoService = new ProductoService();

    public List<Productos> listarTodos() {
        try {
            return productoService.listarTodos();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean agregarProducto(String nombre, String tipo, String marca,
                                   String precio, String stock, JPanel panel) {
        try {
            productoService.agregar(nombre, tipo, marca, precio, stock);
            JOptionPane.showMessageDialog(panel, "Producto agregado exitosamente.");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void eliminarProducto(Integer id, JPanel panel) {
        try {
            productoService.eliminar(id);
            JOptionPane.showMessageDialog(panel, "Producto eliminado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}