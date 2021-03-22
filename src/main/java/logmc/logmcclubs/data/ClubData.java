package logmc.logmcclubs.data;

import logmc.logmcclubs.entities.Club;
import org.spongepowered.api.Sponge;
import logmc.logmcclubs.Logmcclubs;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.ImmutableMappedData;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public class ClubData extends AbstractData<ClubData, ClubData.Immutable> {

    private UUID clubUUID;

    ClubData() {
        clubUUID = null;
        registerGettersAndSetters();
    }

    public ClubData(UUID clubUUID) {
        this.clubUUID = clubUUID;
        registerGettersAndSetters();
    }

    @Override
    public void registerGettersAndSetters() {
        if (ClubKeys.CLUB != null && clubUUID != null) {
            registerFieldGetter(ClubKeys.CLUB, this::getClubUUID);
            registerFieldSetter(ClubKeys.CLUB, this::setClubUUID);
            registerKeyValue(ClubKeys.CLUB, this::clubUUID);
        }
    }

    public Optional<Club> getClub() {
        return Logmcclubs.getInstance().getClubService().getClub(clubUUID);
    }

    public UUID getClubUUID() {
        return clubUUID;
    }

    public void setClubUUID(UUID clubUUID) {
        this.clubUUID = clubUUID;
    }

    public Value<UUID> clubUUID() {
        return Sponge.getRegistry().getValueFactory().createValue(ClubKeys.CLUB, clubUUID);
    }

    @Override
    public Optional<ClubData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(ClubData.class).ifPresent(that -> {
            ClubData data = overlap.merge(this, that);
            this.clubUUID = data.clubUUID;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<ClubData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ClubData> from(DataView container) {
        container.getString(ClubKeys.CLUB.getQuery()).ifPresent(v -> clubUUID = UUID.fromString(v));
        return Optional.of(this);
    }

    @Override
    public ClubData copy() {
        return new ClubData(clubUUID);
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(clubUUID);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(ClubKeys.CLUB.getQuery(), clubUUID.toString());
    }


    public static class Immutable extends AbstractImmutableData<Immutable, ClubData> {

        private UUID clubUUID;

        {
            registerGetters();
        }

        Immutable() {
            this.clubUUID = null;
        }

        Immutable(UUID clubUUID) {
            this.clubUUID = clubUUID;
        }

        @Override
        protected void registerGetters() {
            registerFieldGetter(ClubKeys.CLUB, this::getClubUUID);
            registerKeyValue(ClubKeys.CLUB, this::clubUUID);
        }

        public UUID getClubUUID() {
            return clubUUID;
        }

        public ImmutableValue<UUID> clubUUID() {
            return Sponge.getRegistry().getValueFactory().createValue(ClubKeys.CLUB, clubUUID).asImmutable();
        }

        @Override
        public ClubData asMutable() {
            return new ClubData(this.clubUUID);
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(ClubKeys.CLUB.getQuery(), this.clubUUID);
        }
    }

    public static class Builder extends AbstractDataBuilder<ClubData>
            implements DataManipulatorBuilder<ClubData, Immutable> {
        public Builder() {
            super(ClubData.class, 1);
        }

        @Override
        public ClubData create() {
            return new ClubData();
        }

        @Override
        public Optional<ClubData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ClubData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }
}
