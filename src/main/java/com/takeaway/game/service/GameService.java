package com.takeaway.game.service;

import com.takeaway.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class GameService {
    private final RestTemplate restTemplate;

    @Value("${number.random.max}")
    private Integer limitRangeNumber;

    @Value("${game.endpoint.player.second}")
    private String endpointSecondPlayer;

    @Value("${game.endpoint.play}")
    private String endpointPlayGame;

    Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    ConfigurableApplicationContext context;

    public GameService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public String prepareGame(boolean automaticoInput, Integer numberStart) {
        if (discoverSecondPlayer(true)) {
            Player player = new Player();
            if (automaticoInput) {
                player.setNumber(getRandomNumber());
            } else {
                player.setNumber(numberStart);
            }

            JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
            jmsTemplate.convertAndSend("start", player);

            logger.info("The game has been started, Enjoy!");
            return "The game has been started!";
        } else {

            return "There is no second player active yet!";
        }

    }

    @JmsListener(destination = "start")
    public void startGame(Player player) {
        logger.info("Sending number: " + player.getNumber());
        restTemplate.postForObject(endpointPlayGame, player, Player.class);
    }

    public boolean discoverSecondPlayer(boolean logInfo) {
        if (logInfo) {
            logger.info("Discovering second player...");
        }
        try {
            restTemplate.getForObject(endpointSecondPlayer, String.class);
        } catch (Exception ex) {
            logger.error("There is no second player active. The game has been ended");
            return false;
        }
        if (logInfo) {
            logger.info("Second player discovered!");
        }

        return true;
    }

    private int getRandomNumber() {

        Random r = new Random();
        return r.nextInt(limitRangeNumber);

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResponseEntity<String> play(Player player) {

        if (validatePlayer(player)) {
            int currentNumber = player.getNumber();

            logger.info("Received number: " + currentNumber);

            if (currentNumber <= 0) {
                logger.error("Error, something went wrong.");
            } else {

                if (currentNumber % 3 == 0) {
                    logger.info("Value added: 0");
                    player.setNumber(currentNumber / 3);
                } else if ((currentNumber + 1) % 3 == 0) {
                    logger.info("Value added: 1");
                    player.setNumber((currentNumber + 1) / 3);
                } else {
                    logger.info("Value added: -1");
                    player.setNumber((currentNumber - 1) / 3);
                }

                if (player.getNumber() == 1) {
                    logger.info("You are the winner!!");
                } else {

                    logger.info("Sending number: " + player.getNumber());

                    if (discoverSecondPlayer(false)) {
                        restTemplate.postForObject(endpointPlayGame, player, Player.class);
                    } else {
                        logger.error("Ops, looks like the second player is not active anymore!");
                        logger.error("The game has been ended!!");
                        return new ResponseEntity(HttpStatus.NOT_FOUND);
                    }

                }

            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);

    }

    private boolean validatePlayer(Player player) {
        if (player == null || player.getNumber() == null) {
            logger.error("Ops, looks like the information of the Player is invalid!");
            logger.error("The game has been ended!!");
            return false;
        }
        return true;
    }
}
