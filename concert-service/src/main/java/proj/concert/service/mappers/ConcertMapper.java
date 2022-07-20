package proj.concert.service.mappers;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

import java.util.*;

/**
 * Mapper class for Concert domain object and ConcertDTO/ConcertSummaryDTO
 * object.
 */
public class ConcertMapper {

    public static ConcertDTO toDTO(Concert c) {
        ConcertDTO dto = new ConcertDTO(c.getId(), c.getTitle(), c.getImageName(), c.getBlurb());
        c.getPerformers()
                .forEach(performer -> dto.getPerformers()
                        .add(PerformerMapper.toDTO(performer)));
        dto.getDates()
                .addAll(c.getDates());
        return dto;
    }

    public static List<ConcertDTO> listToDTO(List<Concert> concerts) {
        List<ConcertDTO> dtoList = new ArrayList<>();
        for (Concert c : concerts) {
            dtoList.add(ConcertMapper.toDTO(c));
        }
        return dtoList;
    }

    public static List<ConcertSummaryDTO> listToConcertSummaryDTO(List<Concert> concerts) {
        List<ConcertSummaryDTO> concertSummaryDTOList = new ArrayList<>();
        for (Concert c : concerts) {
            concertSummaryDTOList.add(new ConcertSummaryDTO(c.getId(), c.getTitle(), c.getImageName()));
        }
        return concertSummaryDTOList;
    }
}
