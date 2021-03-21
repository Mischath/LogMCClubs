package logmc.logmcclubs.entities;

import logmc.logmcclubs.Logmcclubs;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHolder {

    private HoconConfigurationLoader loader;
    private CommentedConfigurationNode node;

    public ConfigHolder(Path path, boolean asset) throws IOException {
        try {
            if (Files.notExists(path)) {
                if (asset) {
                    Logmcclubs.getContainer().getAsset(path.getFileName().toString()).get().copyToFile(path);
                } else {
                    Files.createFile(path);
                }
            }
            loader = HoconConfigurationLoader.builder().setPath(path).build();
            node = loader.load();
        } catch (IOException e) {
            Logmcclubs.getInstance().getLogger()
                    .error("Error loading config file! File:[" + path.getFileName().toString() + "]");
            throw e;
        }
    }

    public CommentedConfigurationNode getNode(Object... path) {
        return node.getNode(path);
    }

    public boolean save() {
        try {
            loader.save(node);
            return true;
        } catch (IOException e) {
            Logmcclubs.getInstance().getLogger().error("Unable to save config file! Error:");
            e.printStackTrace();
            return false;
        }
    }
}
