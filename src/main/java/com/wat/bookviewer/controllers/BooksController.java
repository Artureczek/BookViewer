package com.wat.bookviewer.controllers;

import com.codahale.metrics.annotation.Timed;
import groovy.util.logging.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by akielbiewski on 28.10.2016.
 */

@Slf4j
@RestController
public class BooksController {

    /*@Timed
    @Transactional
    @RequestMapping(value = "/messages/received", method = RequestMethod.POST)
    public MessageResponse getReceivedMessages(@RequestParam Integer page, @RequestParam Integer offset, @RequestBody MessageRequest data) {
        Page<VKomunikat> messages = komunikatService.findAllReceived(page, offset, data);

        return new MessageResponse<>(messages.getContent(), (int) messages.getTotalElements());*//*
    }*/


}
