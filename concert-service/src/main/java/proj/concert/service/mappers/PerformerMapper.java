package proj.concert.service.mappers;

import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Performer;

public class PerformerMapper {
    private PerformerMapper() {
    }

    public static PerformerDTO toDTO(Performer c) {
        PerformerDTO dto = new PerformerDTO(c.getId(), c.getName(), c.getImageName(), c.getGenre(), c.getBlurb());
        return dto;
    }
}
