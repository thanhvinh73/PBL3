package com.pbl3.musicapplication;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pbl3.musicapplication.algorithm.TrieService;

@SpringBootTest
class MusicapplicationApplicationTests {

	@Autowired
	private TrieService trieService;
	

	@Test
	void contextLoads() {
	}

	@Test
	public void testTrieService() throws IOException {
		// trieService.insert("Pham Thanh Vinh", true);
		// trieService.insert("Pham Duy Tin", true);
		// trieService.insert("Nguyen Hong Chuong", true);
		// trieService.insert("Huynh Hai Dang", true);



		// trieService.delete("Huynh Hai Dang", true);

		for (String x : trieService.showAll(true)) {
			System.out.println(x);
		}
	}

}
