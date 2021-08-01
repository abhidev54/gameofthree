package com.takeaway.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.takeaway.game.controller.GameController;
import com.takeaway.game.service.GameService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameofthreeApplicationTest {

	@Autowired
	private GameController gameRestEndpoint;

	@Autowired
	private GameService gameService;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(gameRestEndpoint)
				.build();
	}

	@Test
	public void testGameStartUp() {
		if (gameService.discoverSecondPlayer(false)) {
			try {
				mockMvc.perform(MockMvcRequestBuilders.get("/start/true"))
						.andExpect(MockMvcResultMatchers.content().string("The game has been started!"))
						.andExpect(MockMvcResultMatchers.status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testGameStartDown() {
		if (!gameService.discoverSecondPlayer(false)) {
			try {
				mockMvc.perform(MockMvcRequestBuilders.get("/start/true"))
						.andExpect(MockMvcResultMatchers.content().string("There is no second player active yet!"))
						.andExpect(MockMvcResultMatchers.status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPlayGameWithInvalidPlayer() {

		try {
			//Some invalid Json
			String json = "{\n" +
					"  \"title\": \"Hello\",\n" +
					"  \"value\": \"Gameofthree\"\n" +
					"}";
			mockMvc.perform(MockMvcRequestBuilders.post("/play").contentType(MediaType.APPLICATION_JSON).content(json))
					.andExpect(MockMvcResultMatchers.status().isBadRequest());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPlayGameWithSecondPlayerDown() {

		try {
			if (!gameService.discoverSecondPlayer(false)) {

				String json = "{\n" +
						"  \"id\": 1,\n" +
						"  \"number\": 9\n" +
						"}";
				mockMvc.perform(MockMvcRequestBuilders.post("/play").contentType(MediaType.APPLICATION_JSON).content(json))
						.andExpect(MockMvcResultMatchers.status().isNotFound());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPlayGameWinnerPlayer() {

		try {
			String json = "{\n" +
					"  \"id\": 1,\n" +
					"  \"number\": 3\n" +
					"}";
			mockMvc.perform(MockMvcRequestBuilders.post("/play").contentType(MediaType.APPLICATION_JSON).content(json))
					.andExpect(MockMvcResultMatchers.status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHealth() {
		try {
			mockMvc.perform(MockMvcRequestBuilders.get("/health"))
					.andExpect(MockMvcResultMatchers.status().isOk()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

