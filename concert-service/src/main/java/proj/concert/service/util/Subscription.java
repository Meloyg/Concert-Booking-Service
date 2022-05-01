package proj.concert.service.util;

import proj.concert.common.dto.ConcertInfoSubscriptionDTO;

import javax.ws.rs.container.AsyncResponse;

public class Subscription {

    ConcertInfoSubscriptionDTO subDto;
    AsyncResponse response;

    public Subscription(ConcertInfoSubscriptionDTO subDto, AsyncResponse response) {
        this.subDto = subDto;
        this.response = response;
    }

    public ConcertInfoSubscriptionDTO getSubDto() {
        return subDto;
    }

    public AsyncResponse getResponse() {
        return response;
    }

}
