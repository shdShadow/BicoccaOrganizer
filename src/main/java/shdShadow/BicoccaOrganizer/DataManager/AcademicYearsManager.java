package shdShadow.BicoccaOrganizer.DataManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.common.reflect.TypeToInstanceMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import shdShadow.BicoccaOrganizer.CoursesWindow.AcademicYear;
import shdShadow.BicoccaOrganizer.Interfaces.IJsonIO;
import shdShadow.BicoccaOrganizer.util.Constants;
import shdShadow.BicoccaOrganizer.util.Shared;
import shdShadow.BicoccaOrganizer.util.UserCookies;

public class AcademicYearsManager implements IJsonIO<Void, List<AcademicYear>> {
    private Shared cond;

    public AcademicYearsManager(Shared condivisa) {
        this.cond = condivisa;
    }

    public Void saveToJson(List<AcademicYear> years) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fullpath = cond.getBaseDataPath();
        File academicYearsDirectory = new File(fullpath);
        if (!academicYearsDirectory.exists()) {
            // academicYearsDirectory.canWrite();
            academicYearsDirectory.mkdirs();
        }
        try (FileWriter writer = new FileWriter(fullpath + "years.json")) {
            gson.toJson(years, writer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    @Override
    public List<AcademicYear> loadFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(cond.getBaseDataPath() + Constants.COURSES_FILE)) {
            Type listType = new TypeToken<List<AcademicYear>>() {
            }.getType();
            return gson.fromJson(reader, listType);

        } catch (IOException ex) {
            return null;
        }
    }
}
