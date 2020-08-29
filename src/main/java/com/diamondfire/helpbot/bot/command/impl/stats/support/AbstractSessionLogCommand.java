package com.diamondfire.helpbot.bot.command.impl.stats.support;

import com.diamondfire.helpbot.bot.command.help.*;
import com.diamondfire.helpbot.bot.command.impl.stats.AbstractPlayerUUIDCommand;
import com.diamondfire.helpbot.bot.command.permissions.Permission;
import com.diamondfire.helpbot.bot.events.CommandEvent;
import com.diamondfire.helpbot.sys.database.SingleQueryBuilder;
import com.diamondfire.helpbot.sys.externalfile.ExternalFileUtil;
import com.diamondfire.helpbot.util.StringUtil;

import java.io.File;
import java.nio.file.*;
import java.util.*;

public abstract class AbstractSessionLogCommand extends AbstractPlayerUUIDCommand {

    abstract protected List<Session> getSessions(String player);

    @Override
    public Permission getPermission() {
        return Permission.EXPERT;
    }

    @Override
    protected void execute(CommandEvent event, String player) {
        List<Session> sessions = getSessions(player);
        StringBuilder builder = new StringBuilder();

        for (Session session : sessions) {
            builder.append(StringUtil.formatDate(session.getTaken()) + " ");
            builder.append(session.getSupportee() + " ");
            builder.append('(' + StringUtil.formatMilliTime(session.getDuration()) + ')');

            builder.append("\n");
        }

        try {
            File file = ExternalFileUtil.generateFile("session_log.txt");
            Files.writeString(file.toPath(), builder.toString(), StandardOpenOption.WRITE);

            event.getChannel().sendFile(file).queue();
        } catch (Exception e) {
            throw new IllegalStateException();
        }

    }

    static class Session {

        String supportee;
        long duration;
        Date taken;

        public Session(String supportee, long duration, Date taken) {
            this.supportee = supportee;
            this.duration = duration;
            this.taken = taken;
        }

        public String getSupportee() {
            return supportee;
        }

        public long getDuration() {
            return duration;
        }

        public Date getTaken() {
            return taken;
        }

    }

}
