package com.takeaway.game.controller;

import com.takeaway.game.model.Player;
import com.takeaway.game.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(tags = {"game"})
public class GameController {

    @Autowired
    GameService gameService;

    @GetMapping("/start/{automaticoInput}")
    @ApiOperation("Start the game")
    public String startGame(@ApiParam("true (automatic) or false (manual)") @PathVariable boolean automaticoInput,
                            @ApiParam("Number to start in case of manual type") @RequestParam(value = "number") Optional<Integer> numberStart) {
        return gameService.prepareGame(automaticoInput, numberStart.orElse(null));
    }

    @PostMapping("/play")
    @ApiOperation("Play the game")
    public ResponseEntity<String> play(@RequestBody Player player) {
        return gameService.play(player);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ApiOperation("Health check")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
