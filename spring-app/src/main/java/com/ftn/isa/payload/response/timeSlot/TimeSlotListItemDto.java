package com.ftn.isa.payload.response.timeSlot;

import com.ftn.isa.data.CompanyModel;
import com.ftn.isa.data.TimeSlotModel;
import com.ftn.isa.payload.response.company.CompanyListItemDto;

import java.time.LocalDateTime;

public record TimeSlotListItemDto(
        int id,
        LocalDateTime dateTime
) {
    public static TimeSlotListItemDto convert(TimeSlotModel model) {
        return new TimeSlotListItemDto(model.getId(), model.getDateTime());
    }
}
