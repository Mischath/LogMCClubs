package logmc.logmcclubs.services;

import logmc.logmcclubs.data.ClubData;
import logmc.logmcclubs.data.ClubsManager;
import logmc.logmcclubs.entities.Club;

import logmc.logmcclubs.utils.UserUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import com.google.inject.Singleton;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.*;
import java.util.stream.Collectors;

public final class ClubService {
    private Map<UUID, Club> idClubs = new HashMap<>();
    private Map<String, Club> nameClubs = new HashMap<>();

    public Optional<Club> getClub(UUID clubUUID) {
        return Optional.ofNullable(idClubs.get(clubUUID));
    }

    public Optional<Club> getClub(String clubName) { return  Optional.ofNullable(nameClubs.get(clubName.toLowerCase()));}

    public void setIdClubs(Map<UUID, Club> idClubs) {
        this.idClubs = idClubs;
    }

    public void setNameClubs(Map<String, Club> nameClubs) {
        this.nameClubs = nameClubs;
    }

    public Map<UUID, Club> getIdClubs() {
        return idClubs;
    }

    public Map<String, Club> getNameClubs() {
        return nameClubs;
    }

    public Collection<User> getClubMembers(Club club) {
        return club.getMembers().stream()
                .map(UserUtils::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

    }

    public Collection<Club> getClubList() {
        return idClubs.values();
    }

    public void addMember(Club club, User member) {
        club.addMember(member.getUniqueId());
        member.offer(new ClubData(club.getUniqueId()));
        saveClubs();
    }

    public void removeMember(Club club, User member) {
        club.removeMember(member.getUniqueId());
        member.remove(ClubData.class);
        saveClubs();
    }

    public void removeClub(Club club) {
        getClubMembers(club).forEach(member -> removeMember(club, member));
        idClubs.remove(club.getId());
        nameClubs.remove(club.getName().toLowerCase());
    }

    public void saveClubs() {
        ClubsManager.setNameClubs(nameClubs);
        ClubsManager.setIdClubs(idClubs);
        ClubsManager.save();
    }

    public Club createClub(User leader, String name, User... members) {
        Club club = new Club(leader.getUniqueId(), name, new HashSet<>());

        addMember(club, leader);
        for (User member : members) {
            addMember(club, member);
        }

        idClubs.put(club.getId(), club);
        nameClubs.put(club.getName().toLowerCase(), club);

        saveClubs();
        return club;
    }

    public void setClubLeader(Club club, UUID leader) {
        club.setLeader(leader);
        saveClubs();
    }

    public void setRandomClubMemberAsLeader(Club club) {
        UUID newLeader = (UUID)club.getMembers().toArray()[RandomUtils.nextInt(0, club.getMembers().size() - 1)];
        club.setLeader(newLeader);
        saveClubs();
    }
}
