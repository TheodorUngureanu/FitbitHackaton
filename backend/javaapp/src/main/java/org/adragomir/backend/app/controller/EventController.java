package org.adragomir.backend.app.controller;

import org.adragomir.backend.app.model.Event;
import org.adragomir.backend.app.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    EventService eventService;

    @Async
    @RequestMapping(path = "/event", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity> ingestEventAsync(@RequestBody Event event) {
        log.info("Got event POST", event);
        return eventService.saveEvent(event)
            .<ResponseEntity>thenApply(ev -> ResponseEntity.ok().build())
            .exceptionally(e -> {
                log.error("Error saving event {}, error was: {}", event, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            });
    }


    @RequestMapping(path = "/event", method = RequestMethod.GET)
    public List<String> ingestEventAsync2() {
        log.info("Got event GET");
        try {
            return eventService.getData()
    //                .<ResponseEntity>thenApply(ev -> ResponseEntity.ok().build())
    //                .exceptionally(e -> {
    //                    log.error("Error saving event {}, error was: {}", event, e);
    //                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //                });
                     .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
