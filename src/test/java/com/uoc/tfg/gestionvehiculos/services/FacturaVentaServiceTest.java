package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.FacturaCompra;
import com.uoc.tfg.gestionvehiculos.entities.FacturaVenta;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.repositories.FacturaVentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
class FacturaVentaServiceTest {

    @Mock
    private FacturaVentaRepository facturaVentaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private VehiculoService vehiculoService;

    @Mock
    private ReservaVentaService reservaVentaService;

    @InjectMocks
    private FacturaVentaService facturaVentaService;

    private FacturaVenta facturaVenta;
    private Vehiculo vehiculo;
    private Cliente cliente;
    private FacturaCompra facturaCompra;

    @BeforeEach
    void setUp() {
        // Cliente
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellidos("Pérez");

        // Factura de compra
        facturaCompra = new FacturaCompra();
        facturaCompra.setId(1L);
        facturaCompra.setImporteTotal(new BigDecimal("15000.00"));

        // Vehiculo
        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setMatricula("1234ABC");
        vehiculo.setMarca("Toyota");
        vehiculo.setModelo("Corolla");
        vehiculo.setFacturaCompra(facturaCompra);

        // Factura de venta
        facturaVenta = new FacturaVenta();
        facturaVenta.setId(1L);
        facturaVenta.setNumeroFactura("FV-2024-001");
        facturaVenta.setFechaFactura(LocalDate.now());
        facturaVenta.setCliente(cliente);
        facturaVenta.setVehiculo(vehiculo);
        facturaVenta.setImporteBase(new BigDecimal("18000.00"));
        facturaVenta.setIva(new BigDecimal("21.00"));
        facturaVenta.setDescuento(new BigDecimal("0.00"));
        facturaVenta.setImporteTotal(new BigDecimal("21780.00"));
        facturaVenta.setActivo(true);
    }

    @Test
    void listarActivas_DeberiaRetornarListaDeFacturas() {
        // Arrange
        List<FacturaVenta> facturas = Arrays.asList(facturaVenta);
        when(facturaVentaRepository.findAll()).thenReturn(facturas);

        // Act
        List<FacturaVenta> resultado = facturaVentaService.listarActivas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("FV-2024-001", resultado.get(0).getNumeroFactura());
        verify(facturaVentaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_CuandoExiste_DeberiaRetornarFactura() {
        // Arrange
        when(facturaVentaRepository.findById(1L)).thenReturn(Optional.of(facturaVenta));

        // Act
        FacturaVenta resultado = facturaVentaService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("FV-2024-001", resultado.getNumeroFactura());
        verify(facturaVentaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorCliente_DeberiaRetornarListaDeFacturas() {
        // Arrange
        List<FacturaVenta> facturas = Arrays.asList(facturaVenta);
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
        when(facturaVentaRepository.findByCliente(cliente)).thenReturn(facturas);

        // Act
        List<FacturaVenta> resultado = facturaVentaService.obtenerPorCliente(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(clienteService, times(1)).obtenerPorId(1L);
        verify(facturaVentaRepository, times(1)).findByCliente(cliente);
    }

    @Test
    void crear_DeberiaGuardarYRetornarFactura() {
        // Arrange
        when(facturaVentaRepository.existsByNumeroFactura("FV-2024-001")).thenReturn(false);
        when(vehiculoService.obtenerPorId(1L)).thenReturn(vehiculo);
        when(facturaVentaRepository.findByVehiculo(vehiculo)).thenReturn(Optional.empty());
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
        when(facturaVentaRepository.save(any(FacturaVenta.class))).thenReturn(facturaVenta);
        when(vehiculoService.cambiarSituacion(1L, "VENDIDO")).thenReturn(vehiculo);

        // Act
        FacturaVenta resultado = facturaVentaService.crear(facturaVenta, 1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("FV-2024-001", resultado.getNumeroFactura());
        verify(vehiculoService, times(1)).obtenerPorId(1L);
        verify(clienteService, times(1)).obtenerPorId(1L);
        verify(facturaVentaRepository, times(1)).save(any(FacturaVenta.class));
        verify(vehiculoService, times(1)).cambiarSituacion(1L, "VENDIDO");
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarFactura() {
        // Arrange
        FacturaVenta facturaActualizada = new FacturaVenta();
        facturaActualizada.setNumeroFactura("FV-2024-001");
        facturaActualizada.setFechaFactura(LocalDate.now());
        facturaActualizada.setImporteBase(new BigDecimal("19000.00"));
        facturaActualizada.setIva(new BigDecimal("21.00"));
        facturaActualizada.setDescuento(new BigDecimal("500.00"));

        when(facturaVentaRepository.findById(1L)).thenReturn(Optional.of(facturaVenta));
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
        when(facturaVentaRepository.save(any(FacturaVenta.class))).thenReturn(facturaVenta);

        // Act
        FacturaVenta resultado = facturaVentaService.actualizar(1L, facturaActualizada, 1L);

        // Assert
        assertNotNull(resultado);
        verify(facturaVentaRepository, times(1)).findById(1L);
        verify(facturaVentaRepository, times(1)).save(any(FacturaVenta.class));
    }

    @Test
    void calcularBeneficio_DeberiaRetornarDiferencia() {
        // Arrange
        when(facturaVentaRepository.findById(1L)).thenReturn(Optional.of(facturaVenta));

        // Act
        BigDecimal beneficio = facturaVentaService.calcularBeneficio(1L);

        // Assert
        assertNotNull(beneficio);
        // 21780 (venta) - 15000 (compra) = 6780
        assertEquals(new BigDecimal("6780.00"), beneficio);
        verify(facturaVentaRepository, times(1)).findById(1L);
    }

    @Test
    void calcularBeneficioTotal_DeberiaRetornarSumaTotal() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusMonths(1);
        LocalDate fin = LocalDate.now();
        List<FacturaVenta> facturas = Arrays.asList(facturaVenta);
        when(facturaVentaRepository.findByFechaFacturaBetween(inicio, fin)).thenReturn(facturas);

        // Act
        BigDecimal beneficioTotal = facturaVentaService.calcularBeneficioTotal(inicio, fin);

        // Assert
        assertNotNull(beneficioTotal);
        assertEquals(new BigDecimal("6780.00"), beneficioTotal);
        verify(facturaVentaRepository, times(1)).findByFechaFacturaBetween(inicio, fin);
    }

    @Test
    void calcularImporteTotal_ConDescuento_DeberiaCalcularCorrectamente() {
        // Arrange
        FacturaVenta factura = new FacturaVenta();
        factura.setImporteBase(new BigDecimal("10000.00"));
        factura.setIva(new BigDecimal("21.00"));
        factura.setDescuento(new BigDecimal("1000.00"));

        // Act
        factura.calcularImporteTotal();

        // Assert
        assertEquals(0, new BigDecimal("10890.00").compareTo(factura.getImporteTotal()));
    }
}