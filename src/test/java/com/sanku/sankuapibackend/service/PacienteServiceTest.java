package com.sanku.sankuapibackend.service;

// --- Imports necesarios para JUnit 5 y Mockito ---
import com.sanku.sankuapibackend.dto.PacienteDTO;
import com.sanku.sankuapibackend.model.Paciente;
import com.sanku.sankuapibackend.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional; // Importar Optional

// --- Imports estáticos para hacer el código más legible ---
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase PacienteService.
 * Se utiliza Mockito para simular el comportamiento del Repositorio
 * y probar la lógica de negocio de forma aislada.
 */
@ExtendWith(MockitoExtension.class) // Habilita las anotaciones de Mockito
class PacienteServiceTest {

    @Mock // 1. Crea una simulación (un "mock") del Repositorio. No usará la base de datos real.
    private PacienteRepository pacienteRepository;

    @InjectMocks // 2. Crea una instancia de PacienteService e inyecta el mock anterior en ella.
    private PacienteService pacienteService;

    @Test
    void testGetAllPacientes_ShouldReturnListOfDtos() {
        // --- 1. Arrange: Preparamos el escenario de la prueba ---
        
        // Creamos entidades falsas de Paciente que simularán venir de la base de datos.
        Paciente p1 = new Paciente();
        p1.setIdpaciente(1);
        p1.setNombre("Juan");
        p1.setApellido("Perez");

        Paciente p2 = new Paciente();
        p2.setIdpaciente(2);
        p2.setNombre("Ana");
        p2.setApellido("Gomez");
        
        List<Paciente> listaSimulada = List.of(p1, p2);

        // Le decimos a Mockito: "Cuando alguien llame al método findAll() del repositorio,
        // no vayas a la base de datos, en su lugar, devuelve nuestra listaSimulada".
        when(pacienteRepository.findAll()).thenReturn(listaSimulada);

        // --- 2. Act: Ejecutamos el método que queremos probar ---
        List<PacienteDTO> resultado = pacienteService.getAllPacientes();

        // --- 3. Assert: Verificamos que el resultado es el correcto ---
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(2, resultado.size(), "Deberían devolverse 2 pacientes DTO");
        assertEquals("Juan", resultado.get(0).getNombre(), "El nombre del primer paciente no coincide");
        assertEquals("Ana", resultado.get(1).getNombre(), "El nombre del segundo paciente no coincide");
    }

    @Test
    void testGetPacienteById_ShouldReturnDto_WhenPacienteExists() {
        // Arrange
        Integer pacienteId = 1;
        Paciente pacienteMock = new Paciente();
        pacienteMock.setIdpaciente(pacienteId);
        pacienteMock.setNombre("Carlos");
        
        // Simular que findById devuelve un Optional con el paciente
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteMock));
        
        // Act
        Optional<PacienteDTO> resultado = pacienteService.getPacienteById(pacienteId);
        
        // Assert
        assertTrue(resultado.isPresent(), "Debería encontrarse un paciente DTO");
        assertEquals(pacienteId, resultado.get().getId());
        assertEquals("Carlos", resultado.get().getNombre());
    }

    @Test
    void testGetPacienteById_ShouldReturnEmpty_WhenPacienteDoesNotExist() {
        // Arrange
        Integer pacienteId = 99;
        // Simular que findById devuelve un Optional vacío
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.empty());
        
        // Act
        Optional<PacienteDTO> resultado = pacienteService.getPacienteById(pacienteId);
        
        // Assert
        assertFalse(resultado.isPresent(), "No debería encontrarse un paciente DTO");
    }


    @Test
    void testDeletePaciente_ShouldReturnTrue_WhenPacienteExists() {
        // --- 1. Arrange ---
        Integer pacienteId = 1;
        
        // Simulamos que el paciente con ID 1 sí existe en la base de datos.
        when(pacienteRepository.existsById(pacienteId)).thenReturn(true);
        
        // Como deleteById no devuelve nada (es void), le decimos a Mockito
        // que no haga nada cuando se le llame. Esto nos permite verificar si fue invocado.
        doNothing().when(pacienteRepository).deleteById(pacienteId);

        // --- 2. Act ---
        boolean result = pacienteService.deletePaciente(pacienteId);

        // --- 3. Assert ---
        assertTrue(result, "El método debería devolver true si el paciente se elimina");
        
        // Verificamos que el método deleteById() del repositorio fue llamado
        // exactamente una vez con el ID correcto.
        verify(pacienteRepository, times(1)).deleteById(pacienteId);
    }

    @Test
    void testDeletePaciente_ShouldReturnFalse_WhenPacienteDoesNotExist() {
        // --- 1. Arrange ---
        Integer pacienteId = 99; // Un ID que no existe
        
        // Simulamos que el paciente con ID 99 NO existe.
        when(pacienteRepository.existsById(pacienteId)).thenReturn(false);

        // --- 2. Act ---
        boolean result = pacienteService.deletePaciente(pacienteId);

        // --- 3. Assert ---
        assertFalse(result, "El método debería devolver false si el paciente no existe");
        
        // Verificamos que el método deleteById() NUNCA fue llamado,
        // porque la lógica del servicio no debería intentarlo si el paciente no existe.
        verify(pacienteRepository, never()).deleteById(pacienteId);
    }
}
