package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.entities.ContratoRenting;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.EstadoContrato;
import com.uoc.tfg.gestionvehiculos.repositories.ContratoRentingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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
@MockitoSettings(strictness = Strictness.LENIENT)
class ContratoRentingServiceTest {

    @Mock
    private ContratoRentingRepository contratoRepository;

    @Mock
    private VehiculoService vehiculoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private Vehiculo vehiculo;

    @InjectMocks
    private ContratoRentingService contratoService;

    private ContratoRenting contrato;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Cliente
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellidos("Pérez");

        // Vehiculo
        when(vehiculo.getId()).thenReturn(1L);
        when(vehiculo.getMatricula()).thenReturn("1234ABC");
        when(vehiculo.getMarca()).thenReturn("Toyota");
        when(vehiculo.getModelo()).thenReturn("Corolla");
        when(vehiculo.estaDisponibleParaRenting()).thenReturn(true);

        // Contrato
        contrato = new ContratoRenting();
        contrato.setId(1L);
        contrato.setNumeroContrato("CR-2024-001");
        contrato.setVehiculo(vehiculo);
        contrato.setCliente(cliente);
        contrato.setFechaInicio(LocalDate.now());
        contrato.setFechaFin(LocalDate.now().plusMonths(12));
        contrato.setCuotaMensual(new BigDecimal("500.00"));
        contrato.setDuracionMeses(12);
        contrato.setKilometrosIncluidos(15000);
        contrato.setCosteKmExtra(new BigDecimal("0.15"));
        contrato.setEstado(EstadoContrato.ACTIVO);
        contrato.setActivo(true);
    }

    @Test
    void listarActivos_DeberiaRetornarListaDeContratos() {
        // Arrange
        List<ContratoRenting> contratos = Arrays.asList(contrato);
        when(contratoRepository.findAll()).thenReturn(contratos);

        // Act
        List<ContratoRenting> resultado = contratoService.listarActivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(EstadoContrato.ACTIVO, resultado.get(0).getEstado());
        verify(contratoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_CuandoExiste_DeberiaRetornarContrato() {
        // Arrange
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));

        // Act
        ContratoRenting resultado = contratoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("CR-2024-001", resultado.getNumeroContrato());
        verify(contratoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorNumero_CuandoExiste_DeberiaRetornarContrato() {
        // Arrange
        when(contratoRepository.findByNumeroContrato("CR-2024-001"))
                .thenReturn(Optional.of(contrato));

        // Act
        ContratoRenting resultado = contratoService.obtenerPorNumero("CR-2024-001");

        // Assert
        assertNotNull(resultado);
        assertEquals("CR-2024-001", resultado.getNumeroContrato());
        verify(contratoRepository, times(1)).findByNumeroContrato("CR-2024-001");
    }

    @Test
    void obtenerPorCliente_DeberiaRetornarListaDeContratos() {
        // Arrange
        List<ContratoRenting> contratos = Arrays.asList(contrato);
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
        when(contratoRepository.findByCliente(cliente)).thenReturn(contratos);

        // Act
        List<ContratoRenting> resultado = contratoService.obtenerPorCliente(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(clienteService, times(1)).obtenerPorId(1L);
        verify(contratoRepository, times(1)).findByCliente(cliente);
    }

    @Test
    void obtenerPorVehiculo_DeberiaRetornarListaDeContratos() {
        // Arrange
        List<ContratoRenting> contratos = Arrays.asList(contrato);
        when(vehiculoService.obtenerPorId(1L)).thenReturn(vehiculo);
        when(contratoRepository.findByVehiculo(vehiculo)).thenReturn(contratos);

        // Act
        List<ContratoRenting> resultado = contratoService.obtenerPorVehiculo(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(vehiculoService, times(1)).obtenerPorId(1L);
        verify(contratoRepository, times(1)).findByVehiculo(vehiculo);
    }

    @Test
    void crear_DeberiaCrearContratoYGenerarCuotas() {
        // Arrange
        when(contratoRepository.existsByNumeroContrato("CR-2024-001")).thenReturn(false);
        when(vehiculoService.obtenerPorId(1L)).thenReturn(vehiculo);
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
        when(contratoRepository.save(any(ContratoRenting.class))).thenReturn(contrato);
        when(vehiculoService.cambiarSituacion(1L, "EN_RENTING")).thenReturn(vehiculo);

        // Act
        ContratoRenting resultado = contratoService.crear(contrato, 1L, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoContrato.ACTIVO, resultado.getEstado());
        verify(contratoRepository, times(1)).existsByNumeroContrato("CR-2024-001");
        verify(vehiculoService, times(1)).obtenerPorId(1L);
        verify(clienteService, times(1)).obtenerPorId(1L);
        verify(vehiculoService, times(1)).cambiarSituacion(1L, "EN_RENTING");
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarContrato() {
        // Arrange
        ContratoRenting contratoActualizado = new ContratoRenting();
        contratoActualizado.setNumeroContrato("CR-2024-001");
        contratoActualizado.setFechaInicio(LocalDate.now());
        contratoActualizado.setFechaFin(LocalDate.now().plusMonths(24));
        contratoActualizado.setCuotaMensual(new BigDecimal("600.00"));
        contratoActualizado.setKilometrosIncluidos(20000);
        contratoActualizado.setCosteKmExtra(new BigDecimal("0.20"));

        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
        when(vehiculoService.obtenerPorId(1L)).thenReturn(vehiculo);
        when(contratoRepository.save(any(ContratoRenting.class))).thenReturn(contrato);

        // Act
        ContratoRenting resultado = contratoService.actualizar(1L, contratoActualizado, 1L, 1L);

        // Assert
        assertNotNull(resultado);
        verify(contratoRepository, times(1)).findById(1L);
        verify(contratoRepository, times(1)).save(any(ContratoRenting.class));
    }

    @Test
    void finalizar_CuandoExiste_DeberiaFinalizarContrato() {
        // Arrange
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        when(contratoRepository.save(any(ContratoRenting.class))).thenReturn(contrato);
        when(vehiculoService.cambiarSituacion(1L, "DISPONIBLE")).thenReturn(vehiculo);

        // Act
        contratoService.finalizar(1L);

        // Assert
        assertEquals(EstadoContrato.FINALIZADO, contrato.getEstado());
        verify(contratoRepository, times(1)).findById(1L);
        verify(contratoRepository, times(1)).save(contrato);
        verify(vehiculoService, times(1)).cambiarSituacion(1L, "DISPONIBLE");
    }

    @Test
    void cancelar_CuandoExiste_DeberiaCancelarContrato() {
        // Arrange
        when(contratoRepository.findById(1L)).thenReturn(Optional.of(contrato));
        when(contratoRepository.save(any(ContratoRenting.class))).thenReturn(contrato);
        when(vehiculoService.cambiarSituacion(1L, "DISPONIBLE")).thenReturn(vehiculo);

        // Act
        contratoService.cancelar(1L, "Cliente solicitó cancelación");

        // Assert
        assertEquals(EstadoContrato.CANCELADO, contrato.getEstado());
        verify(contratoRepository, times(1)).findById(1L);
        verify(contratoRepository, times(1)).save(contrato);
        verify(vehiculoService, times(1)).cambiarSituacion(1L, "DISPONIBLE");
    }

    @Test
    void obtenerPorEstado_DeberiaRetornarContratosFiltrados() {
        // Arrange
        List<ContratoRenting> contratos = Arrays.asList(contrato);
        when(contratoRepository.findByEstado(EstadoContrato.ACTIVO)).thenReturn(contratos);

        // Act
        List<ContratoRenting> resultado = contratoService.obtenerPorEstado(EstadoContrato.ACTIVO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(EstadoContrato.ACTIVO, resultado.get(0).getEstado());
        verify(contratoRepository, times(1)).findByEstado(EstadoContrato.ACTIVO);
    }

    @Test
    void obtenerProximosAVencer_DeberiaRetornarContratosProximos() {
        // Arrange
        LocalDate fechaLimite = LocalDate.now().plusDays(30);
        List<ContratoRenting> contratos = Arrays.asList(contrato);
        when(contratoRepository.findByFechaFinBefore(any(LocalDate.class))).thenReturn(contratos);

        // Act
        List<ContratoRenting> resultado = contratoService.obtenerProximosAVencer(30);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(contratoRepository, times(1)).findByFechaFinBefore(any(LocalDate.class));
    }
}