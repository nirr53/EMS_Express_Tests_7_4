package EMS_Tests;


//package Vocanom_Tests;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.params.ClientPNames;
//import org.apache.http.client.protocol.ClientContext;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.HttpParams;
//import org.apache.http.protocol.BasicHttpContext;
//import org.eclipse.jetty.http.HttpMethod;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
// 
//import java.io.File;
//import java.io.IOException;
//import java.net.CookiePolicy;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.Set;
//import java.util.logging.FileHandler;
//import java.util.logging.Logger;
// 
//
//import com.google.common.annotations.Beta;
//import java.io.*;
//
//
//
//public class FileDownloader {
//
//    private WebDriver driver;
//    private String downloadPath = System.getProperty("java.io.tmpdir");
//
//    public FileDownloader(WebDriver driverObject) {
//        this.driver = driverObject;
//    }
//
//    /**
//     * Get the current location that files will be downloaded to.
//     *
//     * @return The filepath that the file will be downloaded to.
//     */
//    public String getDownloadPath() {
//        return this.downloadPath;
//    }
//
//    /**
//     * Set the path that files will be downloaded to.
//     *
//     * @param filePath The filepath that the file will be downloaded to.
//     */
//    public void setDownloadPath(String filePath) {
//        this.downloadPath = filePath;
//    }
//
//
//    /**
//     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
//     *
//     * @param seleniumCookieSet
//     * @return
//     */
//
//
//
//
//    public String fileDownloader(WebElement element) throws Exception {
//        return downloader(element, "href");
//    }
//
//    public String imageDownloader(WebElement element) throws Exception {
//        return downloader(element, "src");
//    }
//
//    public String downloader(WebElement element, String attribute) throws Exception {
//        //Assuming that getAttribute does some magic to return a fully qualified URL
//        String downloadLocation = element.getAttribute(attribute);
//        if (downloadLocation.trim().equals("")) {
//            throw new Exception("The element you have specified does not link to anything!");
//        }
//        URL downloadURL = new URL(downloadLocation);
//        HttpClient client = new HttpClient();
//        client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
//        //client.setHostConfiguration(mimicHostConfiguration(downloadURL.getHost(), downloadURL.getPort()));
//        client.setState(mimicCookieState(driver.manage().getCookies()));
//        HttpMethod getRequest = new GetMethod(downloadURL.getPath());
//        FileHandler downloadedFile = new FileHandler(downloadPath + downloadURL.getFile().replaceFirst("/|\\\\", ""), true);
//        try {
//            int status = client.executeMethod(getRequest);
//            System.out.println.info("HTTP Status {} when getting '{}'", status, downloadURL.toExternalForm());
//            BufferedInputStream in = new BufferedInputStream(getRequest.getResponseBodyAsStream());
//            int offset = 0;
//            int len = 4096;
//            int bytes = 0;
//            byte[] block = new byte[len];
//            while ((bytes = in.read(block, offset, len)) > -1) {
//                downloadedFile.getWritableFileOutputStream().write(block, 0, bytes);
//            }
//            downloadedFile.close();
//            in.close();
//            System.out.println("File downloaded to '{}'" + downloadedFile.getAbsoluteFile());
//        } catch (Exception Ex) {
//            System.out.println("Download failed: {}");
//            throw new Exception("Download failed!");
//        } finally {
//            getRequest.releaseConnection();
//        }
//        return downloadedFile.getAbsoluteFile();
//    }
//}