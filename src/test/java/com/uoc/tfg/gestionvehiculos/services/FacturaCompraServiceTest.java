package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.FacturaCompra;
import com.uoc.tfg.gestionvehiculos.entities.Proveedor;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.repositories.FacturaCompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@ExtendWith(MockitoExtension.class)
class FacturaCompraServiceTest {

    @Mock
    private FacturaCompraRepository facturaCompraRepository;

    @Mock
    private VehiculoService vehiculoService;

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private FacturaCompraService facturaCompraService;

    private FacturaCompra facturaCompra;
    private Vehiculo vehiculo;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        // Proveedor
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombreComercial("Proveedor Test S.L.");
        proveedor.setCif("B12345678");

        // Vehiculo
        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setMatricula("1234ABC");
        vehiculo.setMarca("Toyota");
        vehiculo.setModelo("Corolla");

        // Factura de compra
        facturaCompra = new FacturaCompra();
        facturaCompra.setId(1L);
        facturaCompra.setNumeroFactura("FC-2024-001");
        facturaCompra.setFechaFactura(LocalDate.now());
        facturaCompra.setProveedor(proveedor);
        facturaCompra.setVehiculo(vehiculo);
        facturaCompra.setImporteBase(new BigDecimal("15000.00"));
        facturaCompra.setIva(new BigDecimal("21.00"));
        facturaCompra.setImporteTotal(new BigDecimal("18150.00"));
        facturaCompra.setActivo(true);
    }

    @Test
    void listarActivas_DeberiaRetornarListaDeFacturas() {
        // Arrange
        List<FacturaCompra> facturas = Arrays.asList(facturaCompra);
        when(facturaCompraRepository.findAll()).thenReturn(facturas);

        // Act
        List<FacturaCompra> resultado = facturaCompraService.listarActivas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("FC-2024-001", resultado.get(0).getNumeroFactura());
        verify(facturaCompraRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_CuandoExiste_DeberiaRetornarFactura() {
        // Arrange
        when(facturaCompraRepository.findById(1L)).thenReturn(Optional.of(facturaCompra));

        // Act
        FacturaCompra resultado = facturaCompraService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("FC-2024-001", resultado.getNumeroFactura());
        verify(facturaCompraRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorNumero_CuandoExiste_DeberiaRetornarFactura() {
        // Arrange
        when(facturaCompraRepository.findByNumeroFactura("FC-2024-001"))
                .thenReturn(Optional.of(facturaCompra));

        // Act
        FacturaCompra resultado = facturaCompraService.obtenerPorNumero("FC-2024-001");

        // Assert
        assertNotNull(resultado);
        assertEquals("FC-2024-001", resultado.getNumeroFactura());
        verify(facturaCompraRepository, times(1)).findByNumeroFactura("FC-2024-001");
    }

    @Test
    void obtenerPorProveedor_DeberiaRetornarListaDeFacturas() {
        // Arrange
        List<FacturaCompra> facturas = Arrays.asList(facturaCompra);
        when(proveedorService.obtenerPorId(1L)).thenReturn(proveedor);
        when(facturaCompraRepository.findByProveedor(proveedor)).thenReturn(facturas);

        // Act
        List<FacturaCompra> resultado = facturaCompraService.obtenerPorProveedor(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(proveedorService, times(1)).obtenerPorId(1L);
        verify(facturaCompraRepository, times(1)).findByProveedor(proveedor);
    }

    @Test
    void crear_DeberiaGuardarYRetornarFactura() {
        // Arrange
        when(facturaCompraRepository.existsByNumeroFactura("FC-2024-001")).thenReturn(false);
        when(facturaCompraRepository.findByVehiculo(vehiculo)).thenReturn(Optional.empty());
        when(proveedorService.obtenerPorId(1L)).thenReturn(proveedor);
        when(vehiculoService.obtenerPorId(1L)).thenReturn(vehiculo);
        when(facturaCompraRepository.save(any(FacturaCompra.class))).thenReturn(facturaCompra);

        // Act
        FacturaCompra resultado = facturaCompraService.crear(facturaCompra, 1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("FC-2024-001", resultado.getNumeroFactura());
        assertEquals(0, new BigDecimal("18150.00").compareTo(resultado.getImporteTotal()));
        verify(facturaCompraRepository, times(1)).existsByNumeroFactura("FC-2024-001");
        verify(proveedorService, times(1)).obtenerPorId(1L);
        verify(vehiculoService, times(1)).obtenerPorId(1L);
        verify(facturaCompraRepository, times(1)).save(any(FacturaCompra.class));
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarFactura() {
        // Arrange
        FacturaCompra facturaActualizada = new FacturaCompra();
        facturaActualizada.setNumeroFactura("FC-2024-001");
        facturaActualizada.setFechaFactura(LocalDate.now());
        facturaActualizada.setImporteBase(new BigDecimal("16000.00"));
        facturaActualizada.setIva(new BigDecimal("21.00"));
        facturaActualizada.setObservaciones("Factura actualizada");

        when(facturaCompraRepository.findById(1L)).thenReturn(Optional.of(facturaCompra));
        when(proveedorService.obtenerPorId(1L)).thenReturn(proveedor);
        when(vehiculoService.obtenerPorId(1L)).thenReturn(vehiculo);
        when(facturaCompraRepository.save(any(FacturaCompra.class))).thenReturn(facturaCompra);

        // Act
        FacturaCompra resultado = facturaCompraService.actualizar(1L, facturaActualizada, 1L, 1L);

        // Assert
        assertNotNull(resultado);
        verify(facturaCompraRepository, times(1)).findById(1L);
        verify(facturaCompraRepository, times(1)).save(any(FacturaCompra.class));
    }

    @Test
    void obtenerPorFechas_DeberiaRetornarFacturasFiltradas() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusMonths(1);
        LocalDate fin = LocalDate.now();
        List<FacturaCompra> facturas = Arrays.asList(facturaCompra);
        when(facturaCompraRepository.findByFechaFacturaBetween(inicio, fin)).thenReturn(facturas);

        // Act
        List<FacturaCompra> resultado = facturaCompraService.obtenerPorFechas(inicio, fin);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(facturaCompraRepository, times(1)).findByFechaFacturaBetween(inicio, fin);
    }

    @Test
    void calcularImporteTotal_DeberiaCalcularCorrectamente() {
        // Arrange
        FacturaCompra factura = new FacturaCompra();
        factura.setImporteBase(new BigDecimal("10000.00"));
        factura.setIva(new BigDecimal("21.00"));

        // Act
        factura.calcularImporteTotal();

        // Assert
        assertEquals(0, new BigDecimal("12100.00").compareTo(factura.getImporteTotal()));
    }
}