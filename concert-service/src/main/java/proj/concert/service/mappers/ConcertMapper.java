package proj.concert.service.mappers;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.service.domain.Concert;

public class ConcertMapper {
    private ConcertMapper() {
    }

    public static ConcertDTO toDTO(Concert c) {
        ConcertDTO dto = new ConcertDTO(c.getId(), c.getTitle(), c.getImageName(), c.getBlurb());
        c.getPerformers().forEach(performer -> dto.getPerformers().add(PerformerMapper.toDTO(performer)));
        dto.getDates().addAll(c.getDates());
        return dto;
    }
}
