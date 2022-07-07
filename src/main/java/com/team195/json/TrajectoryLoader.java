package com.team195.json;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrajectoryLoader {

    public static TrajectoryJson LoadTrajectory(Path trajectoryFilePath)
    {
        Gson gson = new Gson();
        TrajectoryJson trajectory = null;

        try
        {
            trajectory = gson.fromJson(new FileReader(String.valueOf(trajectoryFilePath)), TrajectoryJson.class);
        }
        catch (FileNotFoundException exception)
        {
            exception.printStackTrace();
        }

        return trajectory;
    }

    public static void SaveTrajectory(TrajectoryJson json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fw = new FileWriter("/Users/roberthilton/Desktop/trajectories/" + json.name + ".json");
            fw.write(gson.toJson(json, TrajectoryJson.class));
            fw.close();
        } catch (IOException exception) {

        }
    }

    public static ArrayList<TrajectoryJson> LoadAllTrajectoryJsons(String trajectoryDirectory)
    {
        ArrayList<TrajectoryJson> trajectoryJsons = new ArrayList<TrajectoryJson>();

        try (Stream<Path> paths = Files.walk(Paths.get(trajectoryDirectory))) {
            Stream<Path> filteredPaths = paths.filter(Files::isRegularFile);

            for (Path path: filteredPaths.collect(Collectors.toList()))
            {
                trajectoryJsons.add(LoadTrajectory(path));
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return trajectoryJsons;
    }
}

