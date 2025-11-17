package DataDrivenTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginUsingDataProvider {

    WebDriver driver;

    @BeforeMethod
    public void openPage(){
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @DataProvider(name="logindata")
    public String[][] loginDataProvider(){
        String[][] data={
                {"Admin","admin123","valid"},
                {"AdminXXX","admin123XXX","invalid"},
                {"Admin","admin123XXX","invalid"},
                {"AdminXXX","admin123","invalid"},
        };

        return data;
    }


    @Test(dataProvider = "logindata")
    public void bothCorrectTesting(String uName,String passWord,String expectValidation){
        WebElement userName = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[1]/div/div[2]/input"));
        userName.sendKeys(uName);
        WebElement password = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[2]/div/div[2]/input"));
        password.sendKeys(passWord);
        WebElement loginBtn = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button"));
        loginBtn.click();

        boolean urlVerification=driver.getCurrentUrl().contains("dashboard");

        if(expectValidation.equals("valid")){
            Assert.assertTrue(urlVerification,"Expecting login success but not navigate to dashboard");
        }else{
            Assert.assertTrue(urlVerification,"Expecting login fail but navigate to dashboard");
        }

    }

    @AfterMethod
    public void quitBrowser(){
        driver.quit();
    }
}
