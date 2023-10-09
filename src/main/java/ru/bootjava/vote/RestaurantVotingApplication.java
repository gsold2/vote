package ru.bootjava.vote;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class RestaurantVotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }
}
