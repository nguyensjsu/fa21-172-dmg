package com.example.backoffice;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserCommand {
    private String userID;
}
