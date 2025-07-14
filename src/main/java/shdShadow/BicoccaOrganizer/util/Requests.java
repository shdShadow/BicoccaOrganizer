package shdShadow.BicoccaOrganizer.util;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Set;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Requests {
        public static UserCookies loginToServices(String username, String password) throws InterruptedException {
                // 1) Setup Geckodriver automatically
                // Auto-setup geckodriver
                // WebDriverManager.firefoxdriver().setup();

                // Headless + window size
                FirefoxOptions options = new FirefoxOptions();
                options.setHeadless(true);
                options.addArguments("--window-size=1920,1080");
                WebDriver driver = new FirefoxDriver(options);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                driver.get("https://elearning.unimib.it/login/index.php");
                // Option B: remove banner via JS
                ((JavascriptExecutor) driver).executeScript(
                                "var b = document.querySelector('.eupopup-container'); if(b) b.remove();");

                // === CLICK SSO LOGIN ===
                WebElement loginButton = wait.until(
                                ExpectedConditions.elementToBeClickable(By.className("auth-login")));
                loginButton.click();
                // capture pre-login base URL
                String firstUrl = driver.getCurrentUrl()
                                .substring(0, driver.getCurrentUrl().lastIndexOf("/"));
                // fill credentials
                WebElement user = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.name("j_username")));
                WebElement pass = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(By.name("j_password")));
                user.sendKeys(username);
                pass.sendKeys(password);
                // submit
                WebElement submit = wait.until(
                                ExpectedConditions.elementToBeClickable(By.name("_eventId_proceed")));
                submit.click();
                // check success
                new WebDriverWait(driver, Duration.ofSeconds(2))
                                .until(ExpectedConditions.not(
                                                ExpectedConditions.urlContains(firstUrl)));

                System.out.println("Login successful!");
                // after login/navigation…
                Set<Cookie> cookies = driver.manage().getCookies();
                UserCookies userCookie = new UserCookies();
                for (Cookie c : cookies) {
                        switch (c.getName()) {
                                case "Elearning-SAMLSessionID":
                                        userCookie.setElearningSamlSessionID(c.getValue());
                                        break;
                                case "MoodleSession":
                                        userCookie.setMoodleSession(c.getValue());
                                        break;
                                case "Elearning-SAMLAuthToken":
                                        userCookie.setElearningSamlAuthToken(c.getValue());
                                        break;
                                default:
                                        break;
                        }
                }
                // join all name=value pairs with “; ”
                // String cookieHeader = cookies.stream()
                // .map(c -> c.getName() + "=" + c.getValue())
                // .collect(Collectors.joining("; "));
                // System.out.println("Cookie: " + cookieHeader);{
                driver.quit();
                // this.cookies = cookies.stream()
                // .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
                return userCookie;

        }

        public static boolean areCookiesValid(UserCookies cookies) {
                try {
                        String testUrl = "https://elearning.unimib.it/my/";
                        HttpURLConnection conn = (HttpURLConnection) new URL(testUrl).openConnection();

                        conn.setRequestMethod("GET");
                        conn.setInstanceFollowRedirects(false); // We'll manually inspect the redirect
                        conn.setRequestProperty("Cookie", cookies.buildCookieHeader());

                        int status = conn.getResponseCode();
                        String redirectUrl = conn.getHeaderField("Location");

                        // Case: Redirect to the login page (which is the base URL with optional params)
                        if ((status == HttpURLConnection.HTTP_MOVED_TEMP ||
                                        status == HttpURLConnection.HTTP_MOVED_PERM ||
                                        status == 303) && redirectUrl != null) {

                                // Normalize the redirect target
                                String cleanedRedirectUrl = redirectUrl.split("\\?")[0]; // Remove any query params

                                if (cleanedRedirectUrl.equals("https://elearning.unimib.it/")) {
                                        System.out.println("Redirected to login → cookies are invalid.");
                                        return false;
                                }
                        }

                        // No redirect to login → cookies still valid
                        return true;
                } catch (IOException ex) {
                        // TODO: HANDLE THIS EXCEPTION PROPERLY
                }
                return false;
        }
}
