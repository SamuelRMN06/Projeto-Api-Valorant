package com.valorant.api.DTO;

import java.util.List;

public record SkinResponseDTO(
        List<SkinChromaDTO> chromas
) {
}
