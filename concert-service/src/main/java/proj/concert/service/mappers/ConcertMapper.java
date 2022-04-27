package proj.concert.service.mappers;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.ConcertSummaryDTO;
import proj.concert.service.domain.Concert;

import java.util.ArrayList;
import java.util.List;

public class ConcertMapper {
    private ConcertMapper() {
    }

    public static ConcertDTO toDTO(Concert c) {
        ConcertDTO dto = new ConcertDTO(c.getId(), c.getTitle(), c.getImageName(), c.getBlurb());
        c.getPerformers().forEach(performer -> dto.getPerformers().add(PerformerMapper.toDTO(performer)));
        dto.getDates().addAll(c.getDates());
        return dto;
    }

    public static List<ConcertDTO> listToDTO(List<Concert> concerts) {
        List<ConcertDTO> dtoList = new ArrayList<>();
        for (Concert c : concerts) {
            dtoList.add(ConcertMapper.toDTO(c));
        }
        return dtoList;
    }

    public static List<ConcertSummaryDTO> listToConcertSummaryDTO (List<Concert> concerts){
        List<ConcertSummaryDTO> concertSummaryDTOList = new ArrayList<>();
        for (Concert c : concerts) {
            concertSummaryDTOList.add( new ConcertSummaryDTO(c.getId(), c.getTitle(), c.getImageName()));
        }
        return concertSummaryDTOList;
    }
}
