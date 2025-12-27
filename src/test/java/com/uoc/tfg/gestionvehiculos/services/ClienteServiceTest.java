package com.uoc.tfg.gestionvehiculos.services;

import com.uoc.tfg.gestionvehiculos.entities.Cliente;
import com.uoc.tfg.gestionvehiculos.enums.TipoCliente;
import com.uoc.tfg.gestionvehiculos.repositories.ClienteRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteParticular;
    private Cliente clienteEmpresa;

    @BeforeEach
    void setUp() {
        // Cliente particular
        clienteParticular = new Cliente();
        clienteParticular.setId(1L);
        clienteParticular.setTipoCliente(TipoCliente.PARTICULAR);
        clienteParticular.setDocumento("12345678A");
        clienteParticular.setNombre("Juan");
        clienteParticular.setApellidos("Pérez García");
        clienteParticular.setEmail("juan@example.com");
        clienteParticular.setTelefono("666123456");
        clienteParticular.setDireccion("Calle Test 123");
        clienteParticular.setActivo(true);

        // Cliente empresa
        clienteEmpresa = new Cliente();
        clienteEmpresa.setId(2L);
        clienteEmpresa.setTipoCliente(TipoCliente.EMPRESA);
        clienteEmpresa.setDocumento("B12345678");
        clienteEmpresa.setRazonSocial("Empresa Test S.L.");
        clienteEmpresa.setEmail("empresa@test.com");
        clienteEmpresa.setTelefono("666999888");
        clienteEmpresa.setDireccion("Polígono Industrial 1");
        clienteEmpresa.setActivo(true);
    }

    @Test
    void listarActivos_DeberiaRetornarListaDeClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(clienteParticular, clienteEmpresa);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<Cliente> resultado = clienteService.listarActivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("Empresa Test S.L.", resultado.get(1).getRazonSocial());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_CuandoExiste_DeberiaRetornarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteParticular));

        // Act
        Cliente resultado = clienteService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("12345678A", resultado.getDocumento());
        assertEquals(TipoCliente.PARTICULAR, resultado.getTipoCliente());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            clienteService.obtenerPorId(999L);
        });
        verify(clienteRepository, times(1)).findById(999L);
    }

    @Test
    void crear_ClienteParticular_DeberiaGuardarYRetornarCliente() {
        // Arrange
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setTipoCliente(TipoCliente.PARTICULAR);
        nuevoCliente.setDocumento("87654321B");
        nuevoCliente.setNombre("María");
        nuevoCliente.setApellidos("López Sánchez");
        nuevoCliente.setEmail("maria@example.com");
        nuevoCliente.setTelefono("666777888");
        nuevoCliente.setDireccion("Avenida Principal 456");

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente c = invocation.getArgument(0);
            c.setId(3L);
            c.setActivo(true);
            return c;
        });

        // Act
        Cliente resultado = clienteService.crear(nuevoCliente);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("María", resultado.getNombre());
        assertEquals("87654321B", resultado.getDocumento());
        assertEquals(TipoCliente.PARTICULAR, resultado.getTipoCliente());
        assertTrue(resultado.getActivo());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void crear_ClienteEmpresa_DeberiaGuardarYRetornarCliente() {
        // Arrange
        Cliente nuevaEmpresa = new Cliente();
        nuevaEmpresa.setTipoCliente(TipoCliente.EMPRESA);
        nuevaEmpresa.setDocumento("B87654321");
        nuevaEmpresa.setRazonSocial("Nueva Empresa S.A.");
        nuevaEmpresa.setEmail("nueva@empresa.com");
        nuevaEmpresa.setTelefono("666555444");
        nuevaEmpresa.setDireccion("Calle Comercial 789");

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente c = invocation.getArgument(0);
            c.setId(4L);
            c.setActivo(true);
            return c;
        });

        // Act
        Cliente resultado = clienteService.crear(nuevaEmpresa);

        // Assert
        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals("Nueva Empresa S.A.", resultado.getRazonSocial());
        assertEquals("B87654321", resultado.getDocumento());
        assertEquals(TipoCliente.EMPRESA, resultado.getTipoCliente());
        assertTrue(resultado.getActivo());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarCliente() {
        // Arrange
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setTipoCliente(TipoCliente.PARTICULAR);
        clienteActualizado.setDocumento("12345678A");
        clienteActualizado.setNombre("Juan");
        clienteActualizado.setApellidos("Pérez García");
        clienteActualizado.setEmail("juan.nuevo@example.com"); // Email actualizado
        clienteActualizado.setTelefono("666000111"); // Teléfono actualizado
        clienteActualizado.setDireccion("Nueva Calle 999");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteParticular));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cliente resultado = clienteService.actualizar(1L, clienteActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("juan.nuevo@example.com", resultado.getEmail());
        assertEquals("666000111", resultado.getTelefono());
        assertEquals("Nueva Calle 999", resultado.getDireccion());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void desactivar_CuandoExiste_DeberiaDesactivarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteParticular));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteParticular);

        // Act
        clienteService.desactivar(1L);

        // Assert
        assertFalse(clienteParticular.getActivo());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void listarActivos_CuandoNoHayClientes_DeberiaRetornarListaVacia() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Cliente> resultado = clienteService.listarActivos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void buscarPorDocumento_CuandoExiste_DeberiaRetornarCliente() {
        // Arrange
        when(clienteRepository.findByDocumento("12345678A")).thenReturn(Optional.of(clienteParticular));

        // Act
        Optional<Cliente> resultado = clienteRepository.findByDocumento("12345678A");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("12345678A", resultado.get().getDocumento());
        assertEquals("Juan", resultado.get().getNombre());
    }

    @Test
    void buscarPorDocumento_CuandoNoExiste_DeberiaRetornarEmpty() {
        // Arrange
        when(clienteRepository.findByDocumento("99999999Z")).thenReturn(Optional.empty());

        // Act
        Optional<Cliente> resultado = clienteRepository.findByDocumento("99999999Z");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void listarPorTipoCliente_DeberiaRetornarClientesFiltrados() {
        // Arrange
        List<Cliente> clientesParticulares = Arrays.asList(clienteParticular);
        when(clienteRepository.findByTipoCliente(TipoCliente.PARTICULAR)).thenReturn(clientesParticulares);

        // Act
        List<Cliente> resultado = clienteRepository.findByTipoCliente(TipoCliente.PARTICULAR);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(TipoCliente.PARTICULAR, resultado.get(0).getTipoCliente());
        assertEquals("Juan", resultado.get(0).getNombre());
    }
}