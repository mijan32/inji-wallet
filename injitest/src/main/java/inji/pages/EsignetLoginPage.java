package inji.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

public class ESignetLoginPage extends BasePage {


    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeStaticText[`label == \"“Inji” Wants to Use “mosip.net” to Sign In\"`]")
    private WebElement iosSignInPermissionPopup;

    @iOSXCUITFindBy(accessibility = "Continue")
    private WebElement iosContinueButton;

    @AndroidFindBy(xpath = "//*[@resource-id='login_with_otp']")
    @iOSXCUITFindBy(accessibility = "Login with OTP")
    private WebElement esignetLoginButton;

    @AndroidFindBy(xpath = "//*[contains(@text,'Login with e-Signet')]")
    @iOSXCUITFindBy(xpath = "//*[contains(@text,'Login with e-Signet')]")
    private WebElement esignetLoginHeader;

    @AndroidFindBy(xpath = "//android.view.View[@resource-id=\"navbar-header\"]/android.widget.Image[1]")
    @iOSXCUITFindBy(xpath = "//android.view.View[@resource-id=\"navbar-header\"]/android.widget.Image[1]")
    private WebElement ESignetLogo;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"Otp_mosip-vid\"]")
    @iOSXCUITFindBy(className = "XCUIElementTypeTextField")
    private WebElement enterIdTextBox;

    @AndroidFindBy(xpath = "//android.widget.Button[@text=\"Get OTP\"]")
    @iOSXCUITFindBy(accessibility = "Get OTP")
    private WebElement getOtpButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.Button\").instance(1)")
    @iOSXCUITFindBy(accessibility = "Verify")
    private WebElement verifyButton;

    @AndroidFindBy(xpath = "//*[contains(@text,'OTP has been sent to your registered Mobile Number')]")
    @iOSXCUITFindBy(xpath = "//*[contains(@text,'OTP has been sent to your registered Mobile Number')]")
    private WebElement otpSendMessage;

    @AndroidFindBy(className = "android.view.ViewGroup")
    private WebElement redirectingPage;

    @AndroidFindBy(accessibility = "progressingLogo")
    private WebElement progressingLogo;

    @AndroidFindBy(accessibility = "loaderTitle")
    private WebElement loadingPageHeader;

    @iOSXCUITFindBy(accessibility = "Done")
    private WebElement DoneButton;

    @AndroidFindBy(accessibility = "loaderSubTitle")
    private WebElement settingUpTextOrDownloadingCredentials;

    @AndroidFindBy(xpath = "//*[@text=\"OTP is invalid\"]")
    private WebElement invalidOtpText;

    @AndroidFindBy(uiAutomator = "UiSelector().className(\"android.widget.TextView\").instance(1)")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText)[5]")
    private WebElement loginTextHeader;

    @AndroidFindBy(uiAutomator = "UiSelector().className(\"android.widget.TextView\").instance(2)")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText)[6]")
    private WebElement pleaseEnterUinHeaderText;

    @AndroidFindBy(uiAutomator = "UiSelector().className(\"android.widget.TextView\").instance(5)")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText)[9]")
    private WebElement dontHaveAccountText;

    @AndroidFindBy(uiAutomator = "UiSelector().className(\"android.widget.TextView\").instance(6)")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText)[10]")
    private WebElement signUpwithUnifiedLoginText;

    @AndroidFindBy(accessibility = "Close tab")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Cancel\"]")
    private WebElement CloseTab;

    @AndroidFindBy(accessibility = "credentialTypeHeading-MOSIPVerifiableCredential")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-MOSIPVerifiableCredential")
    private WebElement credentialTypeHeadingMOSIPVerifiableCredential;

    @AndroidFindBy(id = "android:id/button1")
    @iOSXCUITFindBy(xpath = "//*[contains(@text,'CONTINUE')]")
    private WebElement continuePopupButton;

    public ESignetLoginPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isLoadingPageTextLoaded() {
        return isElementVisible(loadingPageHeader, "Check if loading page header is displayed");
    }

    public boolean isSettingUpTextDisplayed() {
        return isElementVisible(settingUpTextOrDownloadingCredentials, "Check if 'Setting up' or 'Downloading credentials' text is displayed");
    }

    public boolean isDownloadingCredentialsTextDisplayed() {
        return isElementVisible(settingUpTextOrDownloadingCredentials, "Check if 'Downloading credentials' text is displayed");
    }

    public boolean isOtpHasSendMessageDisplayed() {
        return isElementVisible(otpSendMessage, "Check if OTP sent message is displayed");
    }

    public boolean isEsignetLoginPageDisplayed() {
        return isElementVisible(esignetLoginHeader, "Check if Esignet login page header is displayed");
    }

    public void clickOnEsignetLoginWithOtpButton() {
        click(continuePopupButton, "Click on 'Continue' button in Esignet login popup");
        //click(esignetLoginButton, "Click on Esignet login button");
    }

    public OtpVerificationPage setEnterIdTextBox(String uinOrVid) {
        enterText(enterIdTextBox, uinOrVid, "Enter UIN or VID in Enter ID textbox");
        return new OtpVerificationPage(driver);
    }

    public boolean isESignetLogoDisplayed() {
        return isElementVisible(ESignetLogo, "Check if ESignet page is landed and ESignet logo is displayed");
    }

    public boolean isProgressingLogoDisplayed() {
        return redirectingPage.isDisplayed(); // Description can't be added here unless you wrap this in a method.
    }

    public void clickOnGetOtpButton() {
        click(getOtpButton, "Click on 'Get OTP' button");
    }

    public void clickOnVerifyButton() {
        ((HidesKeyboard) driver).hideKeyboard();
        click(verifyButton, "Click on 'Verify' button after entering OTP");
    }

    public void clickOnVerifyButtonIos() {
        click(verifyButton, "Click on 'Verify' button for iOS after entering OTP");
    }

    public boolean isInvalidOtpMessageDisplayed() {
        return isElementVisible(invalidOtpText, "Check if 'Invalid OTP' message is displayed");
    }

    private static final Map<String, Map<String, String>> LANGUAGE_TEXT_MAP = new HashMap<>();

    static {
        Map<String, String> loginHeader = new HashMap<>();
        loginHeader.put("English", "Login");
        loginHeader.put("Tamil", "eSignet மூலம் உள்நுழையவும்");
        loginHeader.put("Kannada", "ಇಸಿಗ್ನೆಟ್ ಮೂಲಕ ಲಾಗಿನ್ ಮಾಡಿ");
        loginHeader.put("Hindi", "ईसिग्नेट से लॉगिन करें");
        loginHeader.put("HindiIos", "ईसिग्नेट से लॉगिन करें");
        loginHeader.put("Arabic", "تسجيل الدخول باستخدام eSignet");
        LANGUAGE_TEXT_MAP.put("LoginHeader", loginHeader);

        Map<String, String> pleaseEnterUIN = new HashMap<>();
        pleaseEnterUIN.put("English", "Please enter your UIN/VID");
        pleaseEnterUIN.put("Tamil", "உங்கள் UIN/VIDஐ உள்ளிடவும்");
        pleaseEnterUIN.put("TamilIos", "உங்கள் UIN/VIDஐ உள்ளிடவும்");
        pleaseEnterUIN.put("Kannada", "ದಯವಿಟ್ಟು ನಿಮ್ಮ UIN/VID ಅನ್ನು ನಮೂದಿಸಿ");
        pleaseEnterUIN.put("Hindi", "कृपया अपना यूआईएन/वीआईडी \u200B\u200Bदर्ज करें");
        pleaseEnterUIN.put("HindiIos", "अपना यूआईएन या वीआईडी \u200B\u200Bदर्ज करें");
        pleaseEnterUIN.put("Arabic", "الرجاء إدخال UIN/VID الخاص بك");
        LANGUAGE_TEXT_MAP.put("PleaseEnterUIN", pleaseEnterUIN);
    }

    public boolean verifyLanguageText(String key, String language, String actualText) {
        Map<String, String> languageMap = LANGUAGE_TEXT_MAP.get(key);
        if (languageMap == null) return false;

        String expectedText = languageMap.get(language);
        if (expectedText == null) return false;

        return actualText.equalsIgnoreCase(expectedText);
    }

    public boolean verifyLoginHeader(String language) {
        return verifyLanguageText("LoginHeader", language, getText(loginTextHeader));
    }

    public boolean verifyLanguagePleaseEnterUinHeaderTextDisplayed(String language) {
        return verifyLanguageText("PleaseEnterUIN", language, getText(pleaseEnterUinHeaderText));
    }

    public void clickOnCloseButton() {
        click(CloseTab, "Click on close button in ID selection popup");
    }

    public String getText() {
        return getText(enterIdTextBox, "Get text from Enter ID textbox");
    }

    public void clickOnCredentialTypeHeadingMOSIPVerifiableCredential() {
        click(credentialTypeHeadingMOSIPVerifiableCredential, "Click on Credential Type heading - MOSIP Verifiable Credential");
    }
}