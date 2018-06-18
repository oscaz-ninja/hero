package ninja.oscaz.hero;

import org.junit.jupiter.api.BeforeEach;

public class HeroTestHelper {

    protected Hero hero;

    @BeforeEach
    public void resetHero() {
        this.hero = Hero.createDefault();
    }

}
