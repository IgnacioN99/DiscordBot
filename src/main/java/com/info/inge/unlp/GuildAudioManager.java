package com.info.inge.unlp;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.TextChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GuildAudioManager {

    public static Map<Snowflake, GuildAudioManager> getMANAGERS() {
        return MANAGERS;
    }

    private static final Map<Snowflake, GuildAudioManager> MANAGERS = new ConcurrentHashMap<>();

    public static GuildAudioManager of(final Snowflake id) {
        return MANAGERS.computeIfAbsent(id, ignored -> new GuildAudioManager());
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    private final AudioPlayer player;

    public LavaPlayerAudioProvider getProvider() {
        return provider;
    }

    private final TrackScheduler scheduler;

    public TrackScheduler getScheduler() {
        return scheduler;
    }

    private final LavaPlayerAudioProvider provider;

    private GuildAudioManager() {
        player = Bot.playerManager.createPlayer();
        scheduler = new TrackScheduler(player);
        provider = new LavaPlayerAudioProvider(player);
        player.addListener(scheduler);

    }

    // getters

}
