package co.simplon.library;

import co.simplon.library.controller.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LibraryApplicationTests {

    @Autowired
    private MainController mainController;

    @Test
    void contextLoads() {
        assertThat(this.mainController).isNotNull();
    }


}
