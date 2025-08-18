package inji.pages;

import inji.utils.InjiWalletConfigManager;
import inji.utils.IosUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class AddNewCardPage extends BasePage {
    private static final String mosipIssuer = InjiWalletConfigManager.getproperty("mosip.issuer");
    private static final String mosipIssuerCredentialType = InjiWalletConfigManager.getproperty("mosip.issuer.credentialType");
    private static final String stayProtectedIssuer = InjiWalletConfigManager.getproperty("stayProtected.issuer");
    private static final String stayProtectedIssuerCredentialType = InjiWalletConfigManager.getproperty("stayProtected.issuer.credentialType");


    @AndroidFindBy(accessibility = "title")
    @iOSXCUITFindBy(accessibility = "title")
    private WebElement addNewCardHeader;


    @AndroidFindBy(accessibility = "goBack")
    @iOSXCUITFindBy(accessibility = "goBack")
    private WebElement backButton;

    @iOSXCUITFindBy(accessibility = "Continue")
    private WebElement continueButton;

    @iOSXCUITFindBy(accessibility = "Cancel")
    private WebElement cancelButton;

    @AndroidFindBy(accessibility = "issuersScreenDescription")
    @iOSXCUITFindBy(accessibility = "issuersScreenDescription")
    private WebElement addNewCardGuideMessage;

    @AndroidFindBy(accessibility = "issuerDescription-Mosip")
    @iOSXCUITFindBy(accessibility = "issuerDescription-Mosip")
    private WebElement issuerDescriptionMosip;

    @AndroidFindBy(accessibility = "issuerDescription-Mosip")
    @iOSXCUITFindBy(accessibility = "issuerDescription-Mosip")
    private WebElement issuerDescriptionEsignet;

    @AndroidFindBy(className = "android.widget.EditText")
    @iOSXCUITFindBy(accessibility = "issuerSearchBar")
    private WebElement issuerSearchBar;

    @AndroidFindBy(accessibility = "issuerHeading-StayProtected")
    @iOSXCUITFindBy(accessibility = "issuerHeading-StayProtected")
    private WebElement downloadViaSunbird;

    @AndroidFindBy(accessibility = "credentialTypeHeading-InsuranceCredential")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-InsuranceCredential")
    private WebElement credentialTypeHeadingInsuranceCredential;

    @AndroidFindBy(accessibility = "credentialTypeHeading-LandStatementCredential_VCDM1.0")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-LandStatementCredential_VCDM1.0")
    private WebElement credentialTypeHeadingLandStatementCredential;

    @AndroidFindBy(accessibility = "credentialTypeHeading-LandStatementCredential_VCDM2.0")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-LandStatementCredential_VCDM2.0")
    private WebElement credentialTypeHeadingLandStatementCredential2;

    @AndroidFindBy(accessibility = "credentialTypeHeading-RegistrationReceiptCredential_VCDM2.0")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-RegistrationReceiptCredential_VCDM2.0")
    private WebElement credentialTypeHeadingRegistrationReceiptCredential_VCDM2;

    @AndroidFindBy(accessibility = "credentialTypeHeading-RegistrationReceiptCredential_VCDM1.0")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-RegistrationReceiptCredential_VCDM1.0")
    private WebElement credentialTypeHeadingRegistrationReceiptCredentialVCDM1;

    @AndroidFindBy(accessibility = "credentialTypeValue")
    @iOSXCUITFindBy(accessibility = "credentialTypeValue")
    private WebElement credentialTypeValue;

    @AndroidFindBy(accessibility = "copilot-next-action")
    @iOSXCUITFindBy(accessibility = "copilot-next-action")
    private WebElement DoneButton;

    @AndroidFindBy(accessibility = "issuerHeading-MockMdl")
    @iOSXCUITFindBy(accessibility = "issuerHeading-MockMdl")
    private WebElement downloadViaMockCertify;

    @AndroidFindBy(accessibility = "credentialTypeHeading-DrivingLicenseCredential")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-DrivingLicenseCredential")
    private WebElement credentialTypeHeadingMockVerifiableCredential_mdoc;

    @AndroidFindBy(accessibility = "credentialTypeHeading-MockVerifiableCredential")
    @iOSXCUITFindBy(accessibility = "credentialTypeHeading-MockVerifiableCredential")
    private WebElement credentialTypeHeadingMockVerifiableCredential;

    @AndroidFindBy(xpath = "//*[@resource-id=\"issuerSearchBar\"]")
    @iOSXCUITFindBy(accessibility = "issuerSearchBar")
    private WebElement IssuerSearchBar;

    @AndroidFindBy(accessibility = "issuerHeading-Mock")
    @iOSXCUITFindBy(accessibility = "issuerHeading-Mock")
    private WebElement downloadViaMock;

    @AndroidFindBy(xpath = "//*[contains(@text,'CONTINUE')]")
    @iOSXCUITFindBy(xpath = "//*[contains(@text,'CONTINUE')]")
    private WebElement continuePopupButton;

    @AndroidFindBy(accessibility = "issuerHeading-Land")
    @iOSXCUITFindBy(accessibility = "issuerHeading-Land")
    private WebElement downloadViaLand;

    public AddNewCardPage(AppiumDriver driver) {
        super(driver);
    }

    private WebElement getDownloadViaUinElement() {
        String accessibilityId = InjiWalletConfigManager.getproperty("mosip.issuer");
        return driver.findElement(MobileBy.AccessibilityId(accessibilityId));
    }

    public String verifyLanguageForAddNewCardGuideMessage() {
        return getText(addNewCardGuideMessage, "Get text for guide message on Add New Card page");
    }

    public boolean isAddNewCardPageGuideMessageForEsignetDisplayed() {
        return isElementVisible(addNewCardGuideMessage, "Check if guide message for Esignet is visible");
    }

    public boolean isAddNewCardPageLoaded() {
        return isElementVisible(addNewCardHeader, "Verify if Add New Card header is visible");
    }

    public RetrieveIdPage clickOnDownloadViaUin() {
        scrollAndClickByAccessibilityId(mosipIssuer, "Click on 'Download via UIN'");
        scrollAndClickByAccessibilityId(mosipIssuerCredentialType, "Click on 'MOSIP Verifiable Credential' option");
        return new RetrieveIdPage(driver);
    }

    public void clickOnBack() {
        click(backButton, "Click on Back button");
    }

    public boolean isAddNewCardGuideMessageDisplayed() {
        return isElementVisible(addNewCardGuideMessage, "Verify guide message is visible on Add New Card page");
    }

    public boolean isDownloadViaUinDisplayed() {
        return isElementVisible(getDownloadViaUinElement(), "Verify 'Download via UIN' button is visible");
    }

    public boolean isDownloadViaUinDisplayedInHindi() {
        return isElementVisible(getDownloadViaUinElement(), "Verify 'Download via UIN' button is visible in Hindi");
    }

    public boolean isDownloadViaEsignetDisplayed() {
        return isElementVisible(getDownloadViaUinElement(), "Verify 'Download via Esignet' button is visible");
    }

    public boolean isDownloadViaEsignetDisplayedInHindi() {
        return isElementVisible(getDownloadViaUinElement(), "Verify 'Download via Esignet' button is visible in Hindi");
    }

    public boolean isDownloadViaEsignetDisplayedinFillpino() {
        return isElementVisible(getDownloadViaUinElement(), "Verify 'Download via Esignet' button is visible in Filipino");
    }

    public ESignetLoginPage clickOnDownloadViaEsignet() {
        scrollAndClickByAccessibilityId(mosipIssuer, "Click on 'Download via Esignet'");
        scrollAndClickByAccessibilityId(mosipIssuerCredentialType, "Click on 'MOSIP Verifiable Credential' option");
        return new ESignetLoginPage(driver);
    }

    public ESignetLoginPage clickOnDownloadViaLand() {
        click(downloadViaLand, "Clicking on download via Land registry");
        return new ESignetLoginPage(driver);
    }

    public void clickOnLandStatementCredential01() {
        click(credentialTypeHeadingLandStatementCredential, "Clicking on Land statement credential type");
    }

    public void clickOncredentialTypeHeadingLandStatementCredential2() {
        click(credentialTypeHeadingLandStatementCredential2, "Clicking on Land statement credential type");
    }

    public void clickOncredentialTypeHeadingRegistrationReceiptCredential_VCDM2() {
        click(credentialTypeHeadingRegistrationReceiptCredential_VCDM2, "Clicking on Land statement credential type");
    }

    public void clickOncredentialTypeHeadingRegistrationReceiptCredentialVCDM1() {
        click(credentialTypeHeadingRegistrationReceiptCredentialVCDM1, "Clicking on Land statement credential type");
    }

    public void clickOnMosipIssuer() {
        scrollAndClickByAccessibilityId(mosipIssuer, "Click on 'Download via Esignet'");
        new ESignetLoginPage(driver);
    }

    public void clickOnContinueButtonInSigninPopupIos() {
        click(continueButton, "Click on Continue button in iOS Sign-in popup");
    }

    public void clickOnCancelButtonInSigninPopupIos() {
        click(cancelButton, "Click on Cancel button in iOS Sign-in popup");
    }

    public void isBackButtonDisplayed() {
        isElementVisible(backButton, "Check if Back button is displayed");
    }

    public boolean isAddNewCardGuideMessageDisplayedInFillopin() {
        return isElementVisible(addNewCardGuideMessage, "Verify guide message is visible in Filipino");
    }

    public boolean isAddNewCardGuideMessageDisplayedInHindi() {
        return isElementVisible(addNewCardGuideMessage, "Verify guide message is visible in Hindi");
    }

    public boolean isIssuerDescriptionMosipDisplayed() {
        return isElementVisible(issuerDescriptionMosip, "Check if MOSIP issuer description is displayed");
    }

    public boolean isIssuerDescriptionEsignetDisplayed() {
        return isElementVisible(issuerDescriptionEsignet, "Check if Esignet issuer description is displayed");
    }

    public boolean isIssuerSearchBarDisplayed() {
        return isElementVisible(issuerSearchBar, "Verify Issuer search bar is visible");
    }

    public boolean isIssuerSearchBarDisplayedInFilipino() {
        return isElementVisible(issuerSearchBar, "Verify Issuer search bar is visible in Filipino");
    }

    public boolean isIssuerSearchBarDisplayedInHindi() {
        return isElementVisible(issuerSearchBar, "Verify Issuer search bar is visible in Hindi");
    }

    public void sendTextInIssuerSearchBar(String text) {
        clearAndSendKeys(issuerSearchBar, text, "Enter text in Issuer search bar: " + text);
    }

    public boolean isDownloadViaSunbirdDisplayed() {
        return isElementVisible(downloadViaSunbird, "Verify 'Download via Sunbird' option is visible");
    }

    public SunbirdLoginPage clickOnDownloadViaSunbird() {
//        click(continuePopupButton, "Click on Continue popup button");
        scrollAndClickByAccessibilityId(stayProtectedIssuer, "Click on 'Download via Sunbird'");
        return new SunbirdLoginPage(driver);
    }

    public void clickOnCredentialTypeHeadingInsuranceCredential() {
//        click(continuePopupButton, "Click on Continue popup button");
        scrollAndClickByAccessibilityId(stayProtectedIssuerCredentialType, 10, "Click on Insurance Credential type heading");
    }

    public void clickOnDoneButton() {
        click(DoneButton, "Click on Done button");
        IosUtil.scrollToElement(driver, 100, 800, 100, 200);
    }

    public MockCertifyLoginPage clickOnDownloadViaMockCertify() {
        clearAndSendKeys(IssuerSearchBar, "mock mobile", "Enter 'mock mobile' in Issuer search bar");
        click(downloadViaMockCertify, "Click on 'Download via Mock Certify'");
        click(credentialTypeHeadingMockVerifiableCredential_mdoc, "Click on Mock Verifiable Credential (mdoc)");
        return new MockCertifyLoginPage(driver);
    }

    public void clickOnDownloadViaMock() {
        clearAndSendKeys(IssuerSearchBar, "mock", "Enter 'mock' in Issuer search bar");
        click(downloadViaMock, "Click on 'Download via Mock'");
        click(credentialTypeHeadingMockVerifiableCredential, "Click on Mock Verifiable Credential");
    }

    public void ClickOnContinueButton() {
        click(continuePopupButton, "Clicking on continue button");
    }
}
