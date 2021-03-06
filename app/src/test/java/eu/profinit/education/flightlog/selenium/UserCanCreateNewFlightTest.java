package eu.profinit.education.flightlog.selenium;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.selenium.driver.DriverFactory;
import eu.profinit.education.flightlog.selenium.questions.ActiveFlightsAfterCreationQuestion;
import eu.profinit.education.flightlog.selenium.tasks.FillOutFlightInfo;
import eu.profinit.education.flightlog.selenium.tasks.StartWithCreateNewFlightPage;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.IOException;

import static eu.profinit.education.flightlog.selenium.pageObjects.NewFlight.ACTIVE_FLIGHTS_TAB;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.hasItem;

@RunWith(SerenityRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional()
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = IntegrationTestConfig.class)
public class UserCanCreateNewFlightTest {

    @Autowired
    ServletContext context;

    @Rule
    public SpringIntegrationMethodRule springIntegrationMethodRule = new SpringIntegrationMethodRule();

    private Actor james = Actor.named("Kuba");
    private WebDriver driver;

    @Before
    public void before() throws IOException {
        driver = new DriverFactory().getDriver();
        givenThat(james).can(BrowseTheWeb.with(driver));
    }

    @Test
    @Ignore // won't run correctly in the pipeline for some reason
    public void shouldBeAbleToCreateNewFlight() {
        givenThat(james).wasAbleTo(StartWithCreateNewFlightPage.createNewFlightPage());
        when(james).attemptsTo(FillOutFlightInfo.withDefault());

        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.alertIsPresent());
        // there should be the success alert
        driver.switchTo().alert().accept();

        james.attemptsTo(Click.on(ACTIVE_FLIGHTS_TAB));

        then(james).should(seeThat(ActiveFlightsAfterCreationQuestion.theActiveFlights(), hasItem("12:30:00 OK-B123 L-13A Blaník Spoustová, Kamila Tažné letadlo  ")));
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
