package proj.concert.service.mappers;

import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Performer;

import java.util.ArrayList;
import java.util.List;

public class PerformerMapper {
    private PerformerMapper() {
    }

    public static PerformerDTO toDTO(Performer c) {
        PerformerDTO dto = new PerformerDTO(c.getId(), c.getName(), c.getImageName(), c.getGenre(), c.getBlurb());
        return dto;
    }

    public static List<PerformerDTO> listToDTO(List<Performer> performers) {
        List<PerformerDTO> dtoList = new ArrayList<>();
        for (Performer p : performers){
            dtoList.add(PerformerMapper.toDTO(p));
        }
        return dtoList;
    }
}
