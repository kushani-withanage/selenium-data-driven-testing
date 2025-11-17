package DataDrivenTest;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;

public class LoginUsingDataProviderbyExcel {
    WebDriver driver;

    @BeforeMethod
    public void openPage(){
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @DataProvider(name = "loginData")
    public String[][] getExcel() throws IOException {
        FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "\\testData\\credentials.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet1 = workbook.getSheet("Sheet1");
        int row = sheet1.getLastRowNum();
        int column = sheet1.getRow(0).getLastCellNum();
        System.out.println("Row-"+row);
        System.out.println("Column-"+column);

        String[][] array=new String[row][column];
        for (int i = 1; i <= row; i++) {
            XSSFRow currentRow = sheet1.getRow(i);
            for (int j =0; j<column;j++){
                XSSFCell cell = currentRow.getCell(j);
                String data = cell.toString();
                array[i-1][j] = data;
            }
        }
        workbook.close();
        file.close();
        return array;
    }

    @Test(dataProvider = "loginData")
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
