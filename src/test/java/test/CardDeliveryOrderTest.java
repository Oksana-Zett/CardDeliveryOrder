package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import data.DataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(secondMeetingDate);
        $(byText("Запланировать")).click();
        $(byText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible, Duration.ofSeconds(15));
        $(byText("Перепланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldCityEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys("");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldCitySubjectCenter() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldCityNotSubjectCenter() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(DataGenerator.generateInvalidCity("ru"));
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldDateEarly() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 2;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Заказ на выбранную дату невозможен")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNameLatin() {
        var validUser = DataGenerator.Registration.generateUser("en");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNameNumber() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(DataGenerator.generateRandomDigit("ru"));
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNameSpecialSymbol() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(DataGenerator.generateRandomSpecSymbols());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNameEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys("");
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNameHaveLetterIsYo() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(DataGenerator.generateNameWithYo());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldPhoneLetterCyrillic() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getName());
        String actual = $("[data-test-id='phone'] input.input__control").getAttribute("value");
        Assertions.assertEquals("+", actual);
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле заполнено некорректно")).shouldBe(visible);
    }

    @Test
    public void shouldPhoneEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys("");
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldPhoneWithoutPlus() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone().substring(1));
        String actual = $("[data-test-id='phone'] input.input__control").getAttribute("value");
        Assertions.assertEquals(validUser.getPhone().substring(0, 2) + " " + validUser.getPhone().substring(3, 6) + " " + validUser.getPhone().substring(7, 10) + " " + validUser.getPhone().substring(11, 13)
                + " " + validUser.getPhone().substring(14, 16), actual);
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldPhoneWithPlusBetweenNumeral() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone().substring(0, 5) + "+" + validUser.getPhone().substring(5, 16));
        String actual = $("[data-test-id='phone'] input.input__control").getAttribute("value");
        Assertions.assertEquals(validUser.getPhone().substring(0, 2) + " " + validUser.getPhone().substring(3, 6) + " " + validUser.getPhone().substring(7, 10) + " " + validUser.getPhone().substring(11, 13)
                + " " + validUser.getPhone().substring(14, 16), actual);
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле заполнено некорректно")).shouldBe(visible);
    }

    @Test
    public void shouldPhoneWithPlusAfterNumeral() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone() + "+");
        String actual = $("[data-test-id='phone'] input.input__control").getAttribute("value");
        Assertions.assertEquals(validUser.getPhone().substring(0, 2) + " " + validUser.getPhone().substring(3, 6) + " " + validUser.getPhone().substring(7, 10) + " " + validUser.getPhone().substring(11, 13)
                + " " + validUser.getPhone().substring(14, 16), actual);
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле заполнено некорректно")).shouldBe(visible);
    }

    @Test
    public void shouldPhoneSpecialSymbol() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(DataGenerator.generateRandomSpecSymbols());
        String actual = $("[data-test-id='phone'] input.input__control").getAttribute("value");
        Assertions.assertEquals("+", actual);
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле заполнено некорректно")).shouldBe(visible);
    }

    @Test
    public void shouldPhoneOneNumeral() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone().substring(5, 6));
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле заполнено некорректно")).shouldBe(visible);
    }

    @Test
    public void shouldPhoneTwelveNumeral() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone() + validUser.getPhone().substring(4, 5));
        String actual = $("[data-test-id='phone'] input.input__control").getAttribute("value");
        Assertions.assertEquals(validUser.getPhone().substring(0, 2) + " " + validUser.getPhone().substring(3, 6) + " " + validUser.getPhone().substring(7, 10) + " " + validUser.getPhone().substring(11, 13)
                + " " + validUser.getPhone().substring(14, 16), actual);
        $(".checkbox__box").click();
        $(byText("Запланировать")).click();
        $(withText("Поле заполнено некорректно")).shouldBe(visible);
    }

    @Test
    public void shouldConsentFlagNotSet() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").sendKeys(validUser.getCity());
        $$("[class=menu-item__control]").contains(text(validUser.getCity()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").sendKeys(validUser.getName());
        $("[data-test-id='phone'] input").sendKeys(validUser.getPhone());
        $(byText("Запланировать")).click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text").shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}

