package logmc.logmcclubs.services;

import logmc.logmcclubs.data.ClubData;
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
    private Map<UUID, Club> clubs = new HashMap<>();

    public Optional<Club> getClub(UUID clubUUID) {
        return Optional.ofNullable(clubs.get(clubUUID));
    }

    public Collection<User> getClubMembers(Club club) {
        return club.getMembers().stream()
                .map(UserUtils::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

    }

    public void addMember(Club club, User member) {
        club.addMember(member.getUniqueId());
        member.offer(new ClubData(club.getUniqueId()));
    }

    public void removeMember(Club club, User member) {
        club.removeMember(member.getUniqueId());
        member.remove(ClubData.class);
    }

    public void removeClub(Club club) {
        getClubMembers(club).forEach(member -> removeMember(club, member));
        clubs.remove(club.getId());
    }

    public Club createClub(User leader, String name, User... members) {
        Club club = new Club(leader.getUniqueId(), name, new HashSet<>());

        addMember(club, leader);
        for (User member : members) {
            addMember(club, member);
        }

        clubs.put(club.getId(), club);

        return club;
    }

    public void setClubLeader(Club club, UUID leader) {
        club.setLeader(leader);
    }

    public void setRandomClubMemberAsLeader(Club club) {
        UUID newLeader = (UUID)club.getMembers().toArray()[RandomUtils.nextInt(0, club.getMembers().size() - 1)];
        club.setLeader(newLeader);
    }
}
