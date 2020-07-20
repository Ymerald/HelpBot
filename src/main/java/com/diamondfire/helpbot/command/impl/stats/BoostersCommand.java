package com.diamondfire.helpbot.command.impl.stats;

import com.diamondfire.helpbot.command.argument.ArgumentSet;
import com.diamondfire.helpbot.command.help.*;
import com.diamondfire.helpbot.command.impl.Command;
import com.diamondfire.helpbot.command.permissions.Permission;
import com.diamondfire.helpbot.command.reply.PresetBuilder;
import com.diamondfire.helpbot.command.reply.feature.informative.*;
import com.diamondfire.helpbot.components.database.SingleQueryBuilder;
import com.diamondfire.helpbot.events.CommandEvent;
import com.diamondfire.helpbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;

public class BoostersCommand extends Command {

    @Override
    public String getName() {
        return "boosters";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"booster", "cpbooster", "boost", "boosts", "currentboosts", "cpboosts"};
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Gets current active boosters on all nodes.")
                .category(CommandCategory.STATS);
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet();
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    public void run(CommandEvent event) {
        PresetBuilder preset = new PresetBuilder()
                .withPreset(
                        new InformativeReply(InformativeReplyType.INFO, "Current Boosters", null)
                );
        EmbedBuilder embed = preset.getEmbed();

        new SingleQueryBuilder()
                .query("SELECT * FROM xp_boosters WHERE FROM_UNIXTIME(end_time * 0.001) > CURRENT_TIMESTAMP()")
                .onQuery((resultTable) -> {
                    String owner = resultTable.getString("owner_name");
                    int multiplier = resultTable.getInt("multiplier");
                    String durationName = StringUtil.formatMilliTime(resultTable.getLong("end_time") - System.currentTimeMillis());

                    embed.addField(String.format("%sx booster from %s ", multiplier, owner), String.format("Ends in: %s", durationName), false);
                }).onNotFound(() -> embed.addField("None!", "How sad... :(", false)).execute();
        event.reply(preset);

    }

}

