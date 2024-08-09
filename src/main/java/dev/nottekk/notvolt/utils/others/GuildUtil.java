package dev.nottekk.notvolt.utils.others;

import dev.nottekk.notvolt.bot.BotWorker;
import dev.nottekk.notvolt.language.LanguageService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Map;

/**
 * Utility class used to handle Guild specific stuff that is being used multiple times.
 */
@Slf4j
public class GuildUtil {

    /**
     * Constructor should not be called, since it is a utility class that doesn't need an instance.
     *
     * @throws IllegalStateException it is a utility class.
     */
    private GuildUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Add a Role to the Member if Ree6 has enough power to do so.
     *
     * @param guild  the {@link Guild} Entity.
     * @param member the {@link Member} Entity.
     * @param role   the {@link Role} Entity.
     */
    private static void addRole(Guild guild, Member member, Role role) {
        if (guild.getSelfMember().canInteract(role) && guild.getSelfMember().canInteract(member)) {
            if (!member.getRoles().contains(role)) {
                guild.addRoleToMember(member, role).queue();
            }
        } else {
            log.error("[AutoRole] Failed to give a Role!");
            log.error("[AutoRole] Server: {} ({})", guild.getName(), guild.getId());
            if (guild.getOwner() != null)
                guild.getOwner().getUser().openPrivateChannel().queue(privateChannel ->
                        privateChannel.sendMessage(LanguageService.getByGuild(guild, guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES) ?
                                        "message.brs.autoRole.hierarchy"
                                        : "message.brs.autoRole.missingPermission", role.getName()))
                                .queue());
        }
    }

    /**
     * Get all roles that Ree6 can manage.
     * @param guild the Guild to get the roles from.
     * @return a List of Roles that Ree6 can manage.
     */
    public static List<Role> getManagableRoles(Guild guild) {
        return guild.getRoles().stream().filter(role -> guild.getSelfMember().canInteract(role) && !role.isManaged() && !role.isPublicRole()).toList();
    }

    /**
     * Checks if a specific user has supported Ree6 via Donations!
     * @param member the User of the current Guild to check.
     * @return true if the User has supported Ree6 via Donations, false if not.
     */
    public static boolean isSupporter(User member) {
        if (!member.getJDA().retrieveEntitlements().excludeEnded(true).skuIds(1165934495447384144L).complete().isEmpty()) {
            return true;
        }

        Guild ree6Guild = BotWorker.getShardManager().getGuildById(805149057004732457L);

        if (ree6Guild != null) {
            Member ree6Member = ree6Guild.getMemberById(member.getId());

            if (ree6Member != null) {
                return ree6Member.getRoles().stream()
                        .anyMatch(c -> c.getIdLong() == 910133809327009822L);
            }
        }
        return false;
    }
}
