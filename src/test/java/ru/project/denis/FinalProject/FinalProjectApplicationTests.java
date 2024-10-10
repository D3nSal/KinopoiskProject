package ru.project.denis.FinalProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.project.denis.FinalProject.models.Film;
import ru.project.denis.FinalProject.repositories.FilmsRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class FinalProjectApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private FilmsRepository filmsRepository;

	@Container
	private static MySQLContainer<?> mySQL = new MySQLContainer<>(DockerImageName.parse("mysql:latest"));

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQL::getJdbcUrl);
		registry.add("spring.datasource.username", mySQL::getUsername);
		registry.add("spring.datasource.password", mySQL::getPassword);
	}

	@Test
	public void getFilmsTest() throws Exception {
		 MvcResult result = mvc.perform(get("/films?imdbId=tt15398776"))
				.andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON)
				)
				.andReturn();
		 String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		 assertNotNull(content);
		 assertTrue(content.contains("Оппенгеймер"));
	}

	@Test
	public void saveFilmsTest() throws Exception {
		mvc.perform(post("/films?imdbId=tt15398776"))
				.andExpect(status().isOk())
				.andReturn();
		List<Film> filmsFromBD = filmsRepository.findAll();
		assertEquals(1, filmsFromBD.size());
		assertEquals("Оппенгеймер", filmsFromBD.getFirst().getNameRu());
	}

	@Test
	public void getFilmsFromBDTest() throws Exception {
		filmsRepository.save(new Film(4664634, "Оппенгеймер", 2023, 8.1, null, 175));
		MvcResult result = mvc.perform(get("/films/fromBD"))
				.andExpect(status().isOk())
				.andReturn();
		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertNotNull(content);
		assertTrue(content.contains("Оппенгеймер"));
	}

}
