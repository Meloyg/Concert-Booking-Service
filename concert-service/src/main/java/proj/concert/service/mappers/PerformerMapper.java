package proj.concert.service.mappers;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

import java.util.*;

/**
 * Mapper class for converting between Performer objects and PerformerDTO
 * objects.
 */
public class PerformerMapper {

    public static PerformerDTO toDTO(Performer c) {
        return new PerformerDTO(c.getId(), c.getName(), c.getImageName(), c.getGenre(), c.getBlurb());
    }

    public static List<PerformerDTO> listToDTO(List<Performer> performers) {
        List<PerformerDTO> dtoList = new ArrayList<>();
        for (Performer p : performers) {
            dtoList.add(PerformerMapper.toDTO(p));
        }
        return dtoList;
    }
}
