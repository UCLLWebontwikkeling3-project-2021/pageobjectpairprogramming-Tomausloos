import domain.service.PersonDb;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;


import java.util.Arrays;

import static org.junit.Assert.*;

public class ContactTest {
    private WebDriver driver;
    private String path = "http://localhost:8080/Corona_war_exploded/Servlet";

    @Before
    public void setUp() {
        //System.setProperty("webdriver.chrome.driver", "/Users/.../web3pers/chromedriver");
        // windows: gebruik dubbele \\ om pad aan te geven
        // hint: zoek een werkende test op van web 2 ...
        System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
        driver = new ChromeDriver();

    }

    @After
    public void clean() {
        driver.quit();
    }


    @Test
    public void contactPageForUserTomIsDisplayedAfterLogginInAndNavigatingToContacPage(){
        logInUserTom();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.correctLogginInformation("Logged in as Tom"));
    }

    @Test
    public void test_AddContactForUserTom_NoFieldsFilledIn_ErrorMessagesDisplayed(){
        logInUserTom();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);

        contactPage.sumbitInvalid();
        driver.switchTo().alert().accept();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.containsErrorMessage("No firstname given"));
        assertTrue(contactPage.containsErrorMessage("No last name given"));
        assertTrue(contactPage.containsErrorMessage("No phone number given"));
        assertTrue(contactPage.containsErrorMessage("No email given"));
        assertTrue(contactPage.containsErrorMessage("No valid date given"));
        assertTrue(contactPage.containsErrorMessage("No valid hour given -> (format hh:mm)"));

    }

    @Test
    public void test_AddContactForUserTom_FirstnameandLastnameFilledIn_ErrorMessagsDisplayedAndNamesRemain(){
        logInUserTom();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);

        contactPage.setFirstNameField("Jan");
        contactPage.setLastNameField("Jaap");
        contactPage.sumbitInvalid();
        driver.switchTo().alert().accept();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.containsErrorMessage("No phone number given"));
        assertTrue(contactPage.containsErrorMessage("No email given"));
        assertTrue(contactPage.containsErrorMessage("No valid date given"));
        assertTrue(contactPage.containsErrorMessage("No valid hour given -> (format hh:mm)"));
        assertTrue(contactPage.hasStickyFirstName("Jan"));
        assertTrue(contactPage.hasStickyLastName("Jaap"));
    }

    @Test
    public void test_AddContactForUserTom_EmailFilledIn_ErrorMessageDisplayedAndEmailRemains(){
        logInUserTom();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);

        contactPage.setEmailField("jan.jaap@gmail.com");
        contactPage.sumbitInvalid();
        driver.switchTo().alert().accept();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.containsErrorMessage("No firstname given"));
        assertTrue(contactPage.containsErrorMessage("No last name given"));
        assertTrue(contactPage.containsErrorMessage("No phone number given"));
        assertTrue(contactPage.containsErrorMessage("No valid date given"));
        assertTrue(contactPage.containsErrorMessage("No valid hour given -> (format hh:mm)"));
        assertTrue(contactPage.hasStickyEmail("jan.jaap@gmail.com"));
    }

    @Test
    public void test_AddContactWithValidInputFields_AddsContactToContactOverview() throws InterruptedException {
        logInUserTom();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);

        contactPage.setFirstNameField("Jan");
        contactPage.setLastNameField("Jaap");
        contactPage.setDateField("27-11-2020");
        contactPage.setHourField("12:00");
        contactPage.setPhoneField("0489736483");
        contactPage.setEmailField("jan.jaap@gmail.com");

        contactPage = contactPage.submitValid();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.containsTableData("Jan Jaap"));
    }

    @Test
    public void test_AdminUser_CanSearchContactsOfPersonOnId(){
        logInAdmin();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);
        assertTrue(contactPage.userIdSearchField.isDisplayed());

        // SEARCH ALL CONTACTS FOR USER TOM
        contactPage.setUserIdSearchField("9");
        contactPage = contactPage.submitValidIDsearch();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.containsTableData("9"));
        assertTrue(contactPage.containsTableData("Pieterjan"));
        assertTrue(contactPage.containsTableData("Steffen"));
        assertTrue(contactPage.containsTableData("Dirk"));
        assertTrue(contactPage.containsTableData("Jan"));

    }

    @Test
    public void test_AdminUser_GetsErrorMessageIfSubmitSearchWithEmptyId(){
        logInAdmin();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);
        assertTrue(contactPage.userIdSearchField.isDisplayed());

        contactPage.setUserIdSearchField("");
        contactPage.submitInvalidIDsearch();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.hasErrorMessages("Enter a valid user ID"));

    }

    @Test
    public void test_AdminUser_GetsErrorMessageIfSubmitDateSearchWitchEmptyDate(){
        logInAdmin();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);
        assertTrue(contactPage.dateSearchField.isDisplayed());

        contactPage.setDateSearchField("");
        contactPage.submitInvalidDateSearch();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.hasErrorMessages("Enter a valid date please!"));

    }

    @Test
    public void test_AdminUser_CanSearchContactsOnDate_And_Sees_Correct_Contacts(){
        logInAdmin();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);
        assertTrue(contactPage.dateSearchField.isDisplayed());

        contactPage.setDateSearchField("11/09/2020");
        contactPage = contactPage.submitValidDateSearch();
        assertEquals("Contacts", driver.getTitle());
        assertTrue(contactPage.containsTableData("Sam"));
        assertTrue(contactPage.containsTableData("Anouk"));


    }

    @Test
    public void test_Z_AdminUser_AbleToDeleteContactAndContactIsDeleted(){
        logInAdmin();
        ContactPage contactPage = PageFactory.initElements(driver, ContactPage.class);
        assertTrue(contactPage.deleteButton.isDisplayed());

        contactPage = contactPage.deleteContactSubmit();
        assertEquals("Contacts", driver.getTitle());
        assertFalse(contactPage.containsTableData("jan.jaap@gmail.com"));

    }


    public void logInUserTom(){
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);
        homePage.setEmailField("tom.ausloos@hotmail.com");
        homePage.setPasswordField("test");
        homePage.submitValidLogin();
        PageFactory.initElements(driver, ContactPage.class);
    }

    public void logInAdmin(){
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);
        homePage.setEmailField("admin@coronatrace.be");
        homePage.setPasswordField("t");
        homePage.submitValidLogin();
        PageFactory.initElements(driver, ContactPage.class);
    }

}
