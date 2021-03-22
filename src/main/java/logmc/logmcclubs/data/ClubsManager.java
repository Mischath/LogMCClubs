package logmc.logmcclubs.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import logmc.logmcclubs.Logmcclubs;
import logmc.logmcclubs.entities.Club;
import logmc.logmcclubs.entities.ConfigHolder;
import logmc.logmcclubs.utils.TaskUtils;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ClubsManager {
    private static Path storDir = Logmcclubs.getInstance().getDirectory().resolve("storage");
    private static Path logsDir = storDir.resolve("logs");

    private static Map<UUID, Club> idClubs = new HashMap<>();
    private static Map<String, Club> nameClubs = new HashMap<>();

    public static void setIdClubs(Map<UUID, Club> idClubsInput) {
        idClubs = idClubsInput;
    }

    public static void setNameClubs(Map<String, Club> nameClubsInput) {
        nameClubs = nameClubsInput;
    }

    public static Map<UUID, Club> getIdClubs() {
        return idClubs;
    }

    public static Map<String, Club> getNameClubs() {
        return nameClubs;
    }

    private static void initializeNodes() {
        Gson gson = new Gson();

        try {
            Files.createDirectories(logsDir);

            File file = new File(String.valueOf(storDir.resolve("clubs.json")));
            if (!file.exists()) {
                file.createNewFile();
            }

            Type listType = new TypeToken<ArrayList<Club>>(){}.getType();
            List<Club> clubList = gson.fromJson(new FileReader(
                    String.valueOf(storDir.resolve("clubs.json"))), listType);
            if (clubList != null) {
                for (Club club : clubList) {
                    idClubs.put(club.getId(), club);
                    nameClubs.put(club.getName(), club);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readStorage() {
        initializeNodes();
    }

    public static void save() {
        Gson gson = new Gson();
        List<Club> clubList = new ArrayList<Club>(idClubs.values());

        try {
            FileWriter writer = new FileWriter(String.valueOf(storDir.resolve("clubs.json")));
            gson.toJson(clubList, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
