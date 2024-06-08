package com.baexxbin.wishrise.member.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Embeddable
@Getter
public class Information {
    private String nickname;
    private String password;
    private String email;
    private Integer point;
}
