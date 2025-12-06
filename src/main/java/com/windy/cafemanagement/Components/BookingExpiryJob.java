package com.windy.cafemanagement.Components;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.windy.cafemanagement.Services.TableService;

@Component
public class BookingExpiryJob {
    private final TableService tableService;

    public BookingExpiryJob(TableService tableService) {
        this.tableService = tableService;
    }

    @Scheduled(fixedRate = 1 * 60 * 1000)
    public void releaseExpiredBookings() {
        tableService.releaseExpiredBookings();
    }
}
