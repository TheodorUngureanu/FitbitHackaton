package org.adragomir.backend.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.adragomir.backend.app.controller.EventController;
import org.adragomir.backend.app.model.Event;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Async
public class EventService {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public EventService(JdbcTemplate jdbcTemplate, ObjectMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public CompletableFuture<Boolean> saveEvent(Event event) {
        return CompletableFuture.supplyAsync(() -> {
            return jdbcTemplate.execute((Connection conn) -> {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO events (data) VALUES (?)");
                log.info("CEVA saveEvent:", ps.toString());
                PGobject jsonbObj = new PGobject();
                jsonbObj.setType("jsonb");
                try {
                    jsonbObj.setValue(mapper.writeValueAsString(event));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                ps.setObject(1, jsonbObj);
                return ps.execute();
            });
        });
    }

    public CompletableFuture<List<String>> getData() {
        return CompletableFuture.supplyAsync(() -> {
            return jdbcTemplate.execute((Connection conn) -> {
                PreparedStatement ps = conn.prepareStatement("select * from EVENTS");
                log.info("CEVA get data:", ps.toString());
                ResultSet resultSet = ps.executeQuery();
                log.info("CEVA get data2:", resultSet);

                List<String> lista = new ArrayList<>();
                while(resultSet.next()){
//                    lista.add(resultSet.getString(2));
                    String[] parts = resultSet.getString(2).split("\"");
                    lista.add("Temperature " + resultSet.getString(1) + "--> " + parts[13] +
                               "  Timestamp -->" + parts[16]);
                }

                log.info("DEBUG {}", lista.toString());

//                PGobject jsonbObj = new PGobject();
//                jsonbObj.setType("jsonb");
//                try {
//                    lista.add(jsonbObj.getValue());
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                ps.setObject(1, jsonbObj);
//                return ps.execute();
                return lista;
            });
        });
    }

}
