package com.framework.socket.core.domain;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sdy
 * @description
 * @date 2024/12/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private Long userId;
    private String username;
    private Channel channel;

    public User(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}
