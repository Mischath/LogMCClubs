package logmc.logmcclubs.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.UUID;

public class ClubKeys {
    public static Key<Value<UUID>> CLUB;

    public static DataRegistration<ClubData, ClubData.Immutable> CLUB_DATA_REGISTRATION;

    static {
        CLUB = Key.builder()
                .type(new TypeToken<Value<UUID>>() {})
                .id("club")
                .name("Club")
                .query(DataQuery.of("logmcscore", "Club"))
                .build();
    }
}
