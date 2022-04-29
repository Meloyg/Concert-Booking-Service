package proj.concert.service.mappers;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

import java.util.*;

public class SeatMapper {
    public static SeatDTO toDTO(Seat seat) {
        return new SeatDTO(seat.getLabel(), seat.getPrice());
    }

    public static List<SeatDTO> listToDTO(List<Seat> seatList) {
        List<SeatDTO> seatDTOList = new ArrayList<>();
        for (Seat s : seatList) {
            seatDTOList.add(SeatMapper.toDTO(s));
        }
        return seatDTOList;
    }
}
