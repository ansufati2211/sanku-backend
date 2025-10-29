package com.sanku.sankuapibackend.service;

import com.sanku.sankuapibackend.dto.PacienteDTO;
import com.sanku.sankuapibackend.model.Paciente;
import com.sanku.sankuapibackend.model.TipoDocumento; // Importar si es necesario
import com.sanku.sankuapibackend.model.Usuario; // Importar si es necesario
import com.sanku.sankuapibackend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // Convertir Entidad a DTO
    private PacienteDTO convertToDto(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getIdpaciente());
        dto.setNombre(paciente.getNombre());
        dto.setApellido(paciente.getApellido());
        dto.setFechaNacimiento(paciente.getFechanacimiento());
        dto.setNumeroDocumento(paciente.getNumerodocumento());
        dto.setGenero(paciente.getGenero());
        return dto;
    }

    // Convertir DTO a Entidad
    private Paciente convertToEntity(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setIdpaciente(dto.getId());
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setFechanacimiento(dto.getFechaNacimiento());
        paciente.setNumerodocumento(dto.getNumeroDocumento());
        paciente.setGenero(dto.getGenero());
        // Nota: Las relaciones como 'usuario' y 'tipoDocumento' se manejarían por separado
        return paciente;
    }

    public List<PacienteDTO> getAllPacientes() {
        return pacienteRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Optional<PacienteDTO> getPacienteById(Integer id) {
        return pacienteRepository.findById(id).map(this::convertToDto);
    }

    public PacienteDTO createPaciente(PacienteDTO pacienteDTO) {
        Paciente paciente = convertToEntity(pacienteDTO);
        paciente.setIdpaciente(null); // Asegurarse de que es una nueva entidad
        // Aquí necesitarías lógica para asignar el Usuario y TipoDocumento antes de guardar
        return convertToDto(pacienteRepository.save(paciente));
    }

    public Optional<PacienteDTO> updatePaciente(Integer id, PacienteDTO pacienteDTO) {
        return pacienteRepository.findById(id)
                .map(existingPaciente -> {
                    existingPaciente.setNombre(pacienteDTO.getNombre());
                    existingPaciente.setApellido(pacienteDTO.getApellido());
                    existingPaciente.setFechanacimiento(pacienteDTO.getFechaNacimiento());
                    existingPaciente.setNumerodocumento(pacienteDTO.getNumeroDocumento());
                    existingPaciente.setGenero(pacienteDTO.getGenero());
                    return convertToDto(pacienteRepository.save(existingPaciente));
                });
    }

    public boolean deletePaciente(Integer id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}