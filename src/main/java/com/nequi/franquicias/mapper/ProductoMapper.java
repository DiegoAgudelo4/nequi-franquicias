package com.nequi.franquicias.mapper;

import com.nequi.franquicias.dto.producto.ProductoRequestDTO;
import com.nequi.franquicias.dto.producto.ProductoResponseDTO;
import com.nequi.franquicias.dto.sucursal.SucursalRequestDTO;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoResponseDTO toResponseDTO(Producto producto, Sucursal sucursal) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setStock(producto.getStock());
        dto.setActive(producto.isActive());

        SucursalRequestDTO sucursalDTO = new SucursalRequestDTO();
        sucursalDTO.setIdSucursal(sucursal.getIdSucursal());
        sucursalDTO.setNombre(sucursal.getNombre());
        sucursalDTO.setActive(sucursal.isActive());

        dto.setSucursal(sucursalDTO);
        return dto;
    }

    public Producto toEntity(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setStock(dto.getStock());
        producto.setActive(dto.isActive());
        producto.setIdSucursal(dto.getIdSucursal());
        return producto;
    }
}
