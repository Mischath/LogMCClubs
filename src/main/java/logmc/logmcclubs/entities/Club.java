package logmc.logmcclubs.entities;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@ConfigSerializable
public class Club implements SpongeIdentifiable {

    @Setting(value="id")
    private UUID uuid;

    @Setting(value="name")
    private String name;

    @Setting(value="leaderId")
    private UUID leader;

    @Setting(value="memberIds")
    private Set<UUID> members;

    public Club(UUID leader, String name, Set<UUID> members) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.leader = leader;
        this.members = members;
    }

    public Club() { }

    @Nonnull
    @Override
    public UUID getId() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public void removeMember(UUID member) {
        members.remove(member);
    }

    public void addMember(UUID member) {
        members.add(member);
    }

    public void setMembers(Set<UUID> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club)o;
        return Objects.equals(uuid, club.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
