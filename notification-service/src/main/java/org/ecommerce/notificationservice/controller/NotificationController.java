package org.ecommerce.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.ecommerce.notificationservice.record.NotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    @PostMapping
    public ResponseEntity<String> sendNotification(
            @RequestBody NotificationRequest request) {
        System.out.println(
                "Notification received : " + request.message());
        return ResponseEntity.ok("Notification Sent");
    }

}
