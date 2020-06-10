package com.example.demo.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

/**
 * @author mramach
 *
 */
@Component
public class ApplicationService {

    public String getHostname() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

}
