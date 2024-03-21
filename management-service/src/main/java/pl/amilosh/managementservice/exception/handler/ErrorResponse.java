package pl.amilosh.managementservice.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String errorMessage;
    private String errorCode;
}
