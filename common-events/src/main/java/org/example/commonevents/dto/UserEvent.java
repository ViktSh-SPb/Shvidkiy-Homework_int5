package org.example.commonevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Viktor Shvidkiy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    private String operation;
    private String email;
}
