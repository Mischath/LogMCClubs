package logmc.logmcclubs.facades;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import logmc.logmcclubs.commands.ClubCommand;
import logmc.logmcclubs.data.ClubData;
import logmc.logmcclubs.entities.Club;
import logmc.logmcclubs.entities.SpongeIdentifiable;
import logmc.logmcclubs.exceptions.ClubCommandException;
import logmc.logmcclubs.services.ClubService;
import logmc.logmcclubs.utils.Question;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.projectile.ProjectileLauncher;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.*;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.format.TextColors.*;
import static org.spongepowered.api.text.format.TextStyles.BOLD;

public final class ClubFacade {
    @Inject
    private ClubService clubService;

    @Inject
    private ClubMessagingFacade clubMsg;

    public boolean isPlayerClubLeader(Player source, Club club) {
        return  source.getUniqueId().equals(club.getLeader());
    }

    public void createClub(Player source, String name) throws ClubCommandException {
        if (isPlayerInClub(source)){
            throw new ClubCommandException("You are already in a club!");
        }
        Club club = clubService.createClub(source, name);
        clubMsg.sendInfoToClub(club, GOLD, club.getName(), DARK_GREEN, " has been created.");
    }

    public void disbandClub(Player source) throws ClubCommandException {
        Club club = getPlayerClubOrThrow(source);

        if (isPlayerClubLeader(source, club)) {
            clubMsg.sendErrorToClub(club, "Your club has been disbanded.");
            clubService.removeClub(club);
        } else {
            throw ClubCommandException.notLeader();
        }
    }

    public void setClubLeader(Player source, Player target) throws ClubCommandException {
        if (source.getUniqueId().equals(target.getUniqueId())) {
            throw new ClubCommandException("You can't set yourself as leader!");
        }

        Club currentClub = getPlayerClubOrThrow(source);
        Club nextClub = getOtherPlayerClubOrThrow(target);

        if (isPlayerClubLeader(source, currentClub)) {
            if (currentClub.equals(nextClub)) {
                clubService.setClubLeader(currentClub, target.getUniqueId());
                clubMsg.sendInfoToClub(currentClub, GOLD, target.getName(), DARK_GREEN, " is now the leader of the club.");
            } else {
                throw ClubCommandException.notLeader();
            }
        }
    }

    public boolean isPlayerInClub(Player source) {
        Optional<Club> club = getPlayerClub(source);
        return club.isPresent();
    }

    public void leaveClub(Player source) throws ClubCommandException {
        Club club = getPlayerClubOrThrow(source);

        clubService.removeMember(club, source);
        clubMsg.info(source, "You have left the club.");

        if (club.getMembers().size() < 1) {
            clubService.removeClub(club);
        } else if (club.getLeader().equals(source.getUniqueId())) {
            for (UUID member : club.getMembers()) {
                Optional<Player> player = Sponge.getServer().getPlayer(member);

                if (player.isPresent()) {
                    clubService.setClubLeader(club, member);
                    clubMsg.sendErrorToClub(club, source.getName(), " has left the club. ", player.get().getName(), " is the new leader.");
                    return;
                }
            }
        } else {
            clubMsg.sendErrorToClub(club, source.getName(), " has left the club.");
        }
    }

    public void kickFromClub(Player source, User target) throws ClubCommandException {
        if (source.getUniqueId().equals(target.getUniqueId())) {
            leaveClub(source);
            return;
        }

        Club kickerClub = getPlayerClubOrThrow(source);
        Club kickeeClub = getOtherPlayerClubOrThrow(target);

        if (!kickeeClub.equals(kickerClub)) {
            throw ClubCommandException.notInClub(target);
        }

        if (isPlayerClubLeader(source, kickerClub)) {
            clubService.removeMember(kickerClub, target);
            clubMsg.sendErrorToClub(kickerClub, target.getName(), " has been kicked from the club.");
            if (target instanceof  MessageReceiver) {
                clubMsg.error((MessageReceiver)target, "You have been kicked from the club");
            }
        } else {
            throw ClubCommandException.notLeader();
        }
    }

    public void inviteToClub(Player source, Player target) throws ClubCommandException{
        Optional<Club> invClub = getPlayerClub(source);

        if (!Sponge.getServer().getPlayer(target.getUniqueId()).isPresent()) {
            throw new ClubCommandException("This player is not online or does not exist");
        }

        if (invClub.isPresent()) {
            Club club = invClub.get();

            if (club.getMembers().contains(target.getUniqueId())) {
                throw new ClubCommandException(target.getName(), " is already in your club.");
            }

            if (isPlayerClubLeader(source, club)) {
                invite(source, target, club);
            } else {
                throw ClubCommandException.notLeader();
            }
        } else {
            throw ClubCommandException.noClub();
        }
    }

    public void invite(Player source, Player target, Club club) {
        Question question = Question.of(clubMsg.formatInfo(GOLD, source.getName(), DARK_GREEN, " has invited you to ", club.getName(), "."))
                .addAnswer(Question.Answer.of(Text.of(GREEN, "Accept"), playerInvitee -> {
                    clubMsg.info(playerInvitee, "You have accepted ", GOLD, source.getName(), "'s", DARK_GREEN, " invite.");
                    clubService.addMember(club, target);
                    clubMsg.sendInfoToClub(club, GOLD, playerInvitee.getName(), DARK_GREEN, " has joined ", club.getName(), ".");
                }))
                .addAnswer(Question.Answer.of(Text.of(DARK_RED, "Reject"), playerInvitee -> {
                    clubMsg.error(playerInvitee, "You have rejected ", source.getName(), "'s", DARK_GREEN, " invite.");
                    clubMsg.sendErrorToClub(club, playerInvitee.getName(), " has declined the club invite.");
                }))
                .build();

        question.pollChat(target);

        clubMsg.sendInfoToClub(club, GOLD, target.getName(), DARK_GREEN, " has been invited to the club.");
    }

    public void printPlayerClub(Player source) throws ClubCommandException {
        Club club = getPlayerClubOrThrow(source);

        printPlayerClub(source, club);
    }

    public void printPlayerClub(Player source, Club club) throws ClubCommandException {

        PaginationList.Builder builder = PaginationList.builder()
                .title(Text.of(GOLD, club.getName()))
                .header(Text.of("Members:"))
                .padding(Text.of(DARK_GRAY, "="));

        List<Text> contents = new ArrayList<>();
        Text.Builder clubMembers = Text.builder();

        Sponge.getServer().getPlayer(club.getLeader()).ifPresent(player -> {
            clubMembers.append(Text.of(GOLD, BOLD, player.getName()));
        });

        getOnlineClubMembers(club).forEach(clubMember -> {
            if (!isPlayerClubLeader(clubMember, club)) {
                clubMembers.append(Text.of(DARK_GREEN, ", ", GOLD, TextStyles.RESET, clubMember.getName()));
            }
        });
        clubMembers.append(Text.of(DARK_GREEN, "."));
        contents.add(clubMembers.build());

        builder.contents(contents).sendTo(source);
    }

    public Club getPlayerClubOrThrow(Player source) throws ClubCommandException{
        ClubData clubData = source.get(ClubData.class).orElseThrow(ClubCommandException::noClub);
        return clubData.getClub().orElseThrow(ClubCommandException::noClub);
    }

    public Club getOtherPlayerClubOrThrow(User source) throws ClubCommandException{
        ClubData clubData = source.get(ClubData.class).orElseThrow(() -> ClubCommandException.notInClub(source));
        return clubData.getClub().orElseThrow(() -> ClubCommandException.notInClub(source));
    }

    public Optional<Club> getPlayerClub(Player source) {
        return source.get(ClubData.class).flatMap(ClubData::getClub);
    }

    public <T extends User> boolean arePlayersInSameClub(Player source, Player other) {
        Optional<Club> club1 = getPlayerClub(source);
        Optional<Club> club2 = getPlayerClub(other);
        return club1.isPresent() && club2.isPresent() && club1.get().equals(club2.get());
    }

    public Set<Player> getOnlineClubMembers(Club club) {
        return club.getMembers().stream()
                .map(uuid -> Sponge.getServer().getPlayer(uuid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}
