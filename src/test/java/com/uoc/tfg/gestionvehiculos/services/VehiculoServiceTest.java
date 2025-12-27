package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.SituacionVehiculo;
import com.uoc.tfg.gestionvehiculos.entities.Vehiculo;
import com.uoc.tfg.gestionvehiculos.enums.TipoCombustible;
import com.uoc.tfg.gestionvehiculos.repositories.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author José Antonio Ruiz Traid
 * @version 1.0
 * @date 12-2025
 */
@ExtendWith(MockitoExtension.class)
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private SituacionVehiculoService situacionVehiculoService;

    @InjectMocks
    private VehiculoService vehiculoService;

    private Vehiculo vehiculo;
    private SituacionVehiculo situacionDisponible;
    private SituacionVehiculo situacionEnRenting;

    @BeforeEach
    void setUp() {
        // SituacionVehiculo entities
        situacionDisponible = new SituacionVehiculo();
        situacionDisponible.setId(1L);
        situacionDisponible.setNombre("DISPONIBLE");
        situacionDisponible.setDescripcion("Vehículo disponible");
        situacionDisponible.setOrden(1);

        situacionEnRenting = new SituacionVehiculo();
        situacionEnRenting.setId(2L);
        situacionEnRenting.setNombre("EN_RENTING");
        situacionEnRenting.setDescripcion("Vehículo en renting");
        situacionEnRenting.setOrden(2);

        // Vehiculo entity
        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setMatricula("1234ABC");
        vehiculo.setMarca("Toyota");
        vehiculo.setModelo("Corolla");
        vehiculo.setAnyoFabricacion(2020);
        vehiculo.setColor("Blanco");
        vehiculo.setKilometros(50000);
        vehiculo.setNumeroBastidor("VIN123456");
        vehiculo.setTipoCombustible(TipoCombustible.GASOLINA);
        vehiculo.setSituacion(situacionDisponible);
        vehiculo.setActivo(true);
    }

    @Test
    void listarActivos_DeberiaRetornarListaDeVehiculos() {
        // Arrange
        List<Vehiculo> vehiculos = Arrays.asList(vehiculo);
        when(vehiculoRepository.findAll()).thenReturn(vehiculos);

        // Act
        List<Vehiculo> resultado = vehiculoService.listarActivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("1234ABC", resultado.get(0).getMatricula());
        assertEquals("Toyota", resultado.get(0).getMarca());
        assertEquals("DISPONIBLE", resultado.get(0).getSituacion().getNombre());
        verify(vehiculoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_CuandoExiste_DeberiaRetornarVehiculo() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        // Act
        Vehiculo resultado = vehiculoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("1234ABC", resultado.getMatricula());
        verify(vehiculoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(vehiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            vehiculoService.obtenerPorId(999L);
        });
        verify(vehiculoRepository, times(1)).findById(999L);
    }

    @Test
    void crear_DeberiaGuardarYRetornarVehiculo() {
        // Arrange
        Vehiculo nuevoVehiculo = new Vehiculo();
        nuevoVehiculo.setMatricula("5678XYZ");
        nuevoVehiculo.setMarca("Honda");
        nuevoVehiculo.setModelo("Civic");
        nuevoVehiculo.setAnyoFabricacion(2021);
        nuevoVehiculo.setColor("Negro");
        nuevoVehiculo.setKilometros(0);
        nuevoVehiculo.setNumeroBastidor("VIN789012");
        nuevoVehiculo.setTipoCombustible(TipoCombustible.DIESEL);

        when(situacionVehiculoService.obtenerPorId(1L)).thenReturn(situacionDisponible);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(invocation -> {
            Vehiculo v = invocation.getArgument(0);
            v.setId(2L);
            v.setActivo(true);
            return v;
        });

        // Act
        Vehiculo resultado = vehiculoService.crear(nuevoVehiculo, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("5678XYZ", resultado.getMatricula());
        assertEquals("Honda", resultado.getMarca());
        assertEquals(situacionDisponible, resultado.getSituacion());
        assertTrue(resultado.getActivo());
        verify(situacionVehiculoService, times(1)).obtenerPorId(1L);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarVehiculo() {
        // Arrange
        Vehiculo vehiculoActualizado = new Vehiculo();
        vehiculoActualizado.setMatricula("1234ABC");
        vehiculoActualizado.setMarca("Toyota");
        vehiculoActualizado.setModelo("Corolla");
        vehiculoActualizado.setAnyoFabricacion(2020);
        vehiculoActualizado.setColor("Rojo");
        vehiculoActualizado.setKilometros(55000);
        vehiculoActualizado.setNumeroBastidor("VIN123456");
        vehiculoActualizado.setTipoCombustible(TipoCombustible.GASOLINA);

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(situacionVehiculoService.obtenerPorId(2L)).thenReturn(situacionEnRenting);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Vehiculo resultado = vehiculoService.actualizar(1L, vehiculoActualizado, 2L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Rojo", resultado.getColor());
        assertEquals(55000, resultado.getKilometros());
        verify(vehiculoRepository, times(1)).findById(1L);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    void desactivar_CuandoExiste_DeberiaDesactivarVehiculo() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // Act
        vehiculoService.desactivar(1L);

        // Assert
        assertFalse(vehiculo.getActivo());
        verify(vehiculoRepository, times(1)).findById(1L);
        verify(vehiculoRepository, times(1)).save(vehiculo);
    }

    @Test
    void cambiarSituacion_CuandoExiste_DeberiaActualizarSituacion() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(situacionVehiculoService.obtenerPorNombre("EN_RENTING")).thenReturn(situacionEnRenting);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // Act
        vehiculoService.cambiarSituacion(1L, "EN_RENTING");

        // Assert
        verify(vehiculoRepository, times(1)).findById(1L);
        verify(situacionVehiculoService, times(1)).obtenerPorNombre("EN_RENTING");
        verify(vehiculoRepository, times(1)).save(vehiculo);
    }

    @Test
    void actualizarKilometros_CuandoExiste_DeberiaActualizarKilometros() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // Act
        vehiculoService.actualizarKilometros(1L, 60000);

        // Assert
        assertEquals(60000, vehiculo.getKilometros());
        verify(vehiculoRepository, times(1)).findById(1L);
        verify(vehiculoRepository, times(1)).save(vehiculo);
    }

    @Test
    void listarActivos_CuandoNoHayVehiculos_DeberiaRetornarListaVacia() {
        // Arrange
        when(vehiculoRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Vehiculo> resultado = vehiculoService.listarActivos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(vehiculoRepository, times(1)).findAll();
    }
}