package shdShadow.BicoccaOrganizer.scrape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import shdShadow.BicoccaOrganizer.CoursesWindow.AcademicYear;
import shdShadow.BicoccaOrganizer.CoursesWindow.Course;

public class mainScrape {
    private Map<String, String> Cookies;
    public mainScrape(Map<String, String> Cookies) {
        this.Cookies = Cookies;
    }

    public List<AcademicYear> scrapeCourses(String url) {
        List<AcademicYear> years = new ArrayList<AcademicYear>();
        try {
            //Create the connection
            Connection.Response response = Jsoup.connect(url)
                    .cookies(Cookies)// 👈 This is key
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Referer", "https://elearning.unimib.it/") // optional but helpful
                    .timeout(15000)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .execute();
            //Get the html document
            Document doc = response.parse();
            Elements acadamicYearsName = doc.select("h5");
            acadamicYearsName.remove(0);
            for (int i = 0; i < acadamicYearsName.size();i ++){
                years.add(new AcademicYear(acadamicYearsName.get(i).childNode(0).nodeValue()));
            }
            acadamicYearsName.remove(0);
            //Get the academic year(s)
            Elements yearCourses = doc.select("div.courses");
            //remove the first beacause i dont need it
            yearCourses.remove(0);
            for (int i = 0;i < yearCourses.size(); i++) {
                //every single course is wrapped in a "a" tag
                Elements courses = yearCourses.get(i).select("a");
                //extract the Href link and the cours name                
                String href = "";
                String name = "";
                //every "a" tag has some child nodes. I only need this specific one
                for (Element c : courses){
                    href = c.attr("href");
                    name = c.selectFirst("span.card-title.course-fullname.text-truncate").nodeValue();
                    Course course = new Course(name, href);
                    years.get(i).addCourse(course);
                }
                
                
            }
        } catch (Exception e) {
            //TODO: Handle the exception properly
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return years;
    }
}
