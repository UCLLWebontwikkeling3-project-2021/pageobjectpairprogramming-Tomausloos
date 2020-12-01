import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.ArrayList;

public class ContactPage extends Page {
    @FindBy(id = "firstName")
    WebElement firstNameField;

    @FindBy(id = "lastName")
    WebElement lastNameField;

    @FindBy(id = "date")
    WebElement dateField;

    @FindBy(id = "hour")
    WebElement hourField;

    @FindBy(id = "phone")
    WebElement phoneField;

    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(className = "alert-danger")
    WebElement errorMessages;

    @FindBy(id = "logininfo")
    WebElement loginInformation;

    @FindBy(id = "#hassucces")
    WebElement succesNotification;

    @FindBy(id = "ContactAdd")
    WebElement submitContactAdd;

    @FindBy(id = "userId")
    WebElement userIdSearchField;

    @FindBy(id = "searchContact")
    WebElement searchContactSubmit;

    @FindBy(className = "contactform")
    WebElement contactForm;

    @FindBy(id = "dateSearch")
    WebElement dateSearchField;

    @FindBy(id = "searchContactbyDate")
    WebElement searchContactByDateSubmit;

    @FindBy(id = "deleteButton")
    WebElement deleteButton;

    @FindBy(className = "searchForm")
    WebElement searchForm;


    public ContactPage(WebDriver driver) {
        super(driver);
        this.driver.get(path+"?command=Contacts");
    }

    public void setFirstNameField(String firstName){
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void setLastNameField(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void setEmailField(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void setDateField(String date){
        dateField.clear();
        dateField.sendKeys(date);
    }

    public void setHourField(String hour){
        hourField.clear();
        hourField.sendKeys(hour);

    }
    public void setPhoneField(String phone){
        phoneField.clear();
        phoneField.sendKeys(phone);
    }

    public void setUserIdSearchField(String userid){
        userIdSearchField.clear();
        userIdSearchField.sendKeys(userid);
    }

    public void setDateSearchField(String date){
        dateSearchField.clear();
        dateSearchField.sendKeys(date);
    }

    public ContactPage submitValid(){
        submitContactAdd.click();
        driver.switchTo().alert().accept();
        return PageFactory.initElements(driver, ContactPage.class);
    }

    public ContactPage submitValidIDsearch(){
        searchContactSubmit.click();
        return PageFactory.initElements(driver, ContactPage.class);
    }

    public void submitInvalidIDsearch(){
        searchContactSubmit.click();
    }

    public void sumbitInvalid(){
        submitContactAdd.click();
    }

    public ContactPage submitValidDateSearch(){
        searchContactByDateSubmit.click();
        return PageFactory.initElements(driver, ContactPage.class);
    }

    public void submitInvalidDateSearch(){
        searchContactByDateSubmit.click();
    }

    public boolean correctLogginInformation(String message){
        return message.equals(loginInformation.getText());
    }

    public boolean hasErrorMessages(String message){
        return (message.equals(errorMessages.getText()));
    }

    public boolean correctSuccesNotification(String message){
        return message.equals(succesNotification.getText());
    }

    public void sendEnterKey(){
        contactForm.sendKeys(Keys.ENTER);
    }
    public boolean hasStickyLastName(String lastname){
        return lastname.equals(lastNameField.getAttribute("value"));
    }

    public boolean hasStickyEmail(String email){
        return email.equals(emailField.getAttribute("value"));
    }

    public boolean hasStickyPhone(String phone){
        return phone.equals(phoneField.getAttribute("value"));
    }

    public boolean hasStickyFirstName(String firstname){
        return firstname.equals(firstNameField.getAttribute("value"));
    }

    public ArrayList<WebElement> getErrorMessages(){
        return (ArrayList<WebElement>) this.driver.findElements(By.cssSelector("p"));
    }

    public ArrayList<WebElement>getTableData(){
        return (ArrayList<WebElement>) this.driver.findElements(By.cssSelector("td"));
    }

    public WebElement getDateSearchTableData(){
        return this.driver.findElement(By.className(".overviewDateSearch"));
    }

    public ContactPage deleteContactSubmit(){
        deleteButton = this.driver.findElement(By.cssSelector(".overview > tbody:nth-child(1) > tr:nth-child(17) > td:nth-child(6) > a:nth-child(1)"));
        deleteButton.click();
        this.driver.switchTo().alert().accept();
        return PageFactory.initElements(driver, ContactPage.class);
    }

    public boolean containsErrorMessage(String message){
        ArrayList<WebElement>errorMsgs = getErrorMessages();
        int teller = 0;
        for(WebElement errormessage : errorMsgs){
            System.out.println("Message " + teller + ": "  + errormessage.getText());
            if(errormessage.getText().equals(message)){
                return true;
            }
            teller++;
        }
        return false;
    }

    public boolean containsTableData(String data){
        ArrayList<WebElement>tdElements = getTableData();
        boolean found = false;
        int teller = 0;
        for(WebElement td : tdElements){
            System.out.println("Tabledata " + teller + ": " + td.getText());
            if(td.getText().contains(data)){
                found = true;
            }
            teller++;
        }
        return found;
    }

    public boolean dateSearchContainsTableData(String data){
        ArrayList<WebElement>tdElements = (ArrayList<WebElement>) getDateSearchTableData().findElements(By.cssSelector("td"));
        boolean found = false;
        int teller = 0;
        for(WebElement td : tdElements){
            System.out.println("Tabledata " + teller + ": " + td.getText());
            if(td.getText().contains(data)){
                found = true;
            }
            teller++;
        }
        return found;
    }








}
