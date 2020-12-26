package com.info.inge.unlp.comandos;


import com.info.inge.unlp.*;
import com.info.inge.unlp.GuildAudioManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import discord4j.voice.VoiceConnection;

public class ComandoJoin implements Command{
    private  AudioProvider provider;
    @Override
    public void execute(MessageCreateEvent event) {
        final Member member = event.getMember().orElse(null);
        try {
            if (member != null) {
                final VoiceState voiceState = member.getVoiceState().block();
                if (voiceState != null) {
                    final VoiceChannel channel = voiceState.getChannel().block();
                    if (channel != null) {
                        // join returns a VoiceConnection which would be required if we were
                        // adding disconnection features, but for now we are just ignoring it.
                        provider= GuildAudioManager.of(channel.getGuildId()).getProvider();
                        channel.join(spec -> spec.setProvider(provider)).block();
                    }
                }else
                    throw new Exception();
            }
        }catch (Exception e){
            Bot.escribirMensaje((TextChannel) event.getMessage().getChannel().block(),"Tenes q estar adentro de un canal para hacer esto!!!!");
        }

    }

    @Override
    public String help() {
        return "Ingresa al canal que estas actualmente \n" +
                "Uso: ```"+ Bot.indicador_Comando+"join```";
    }

    @Override
    public String getName() {
        return "join";
    }
}

