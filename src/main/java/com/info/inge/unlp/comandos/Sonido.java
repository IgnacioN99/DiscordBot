package com.info.inge.unlp.comandos;

import com.info.inge.unlp.Bot;
import com.info.inge.unlp.Command;
import com.info.inge.unlp.GuildAudioManager;
import com.info.inge.unlp.TrackScheduler;
import com.info.inge.unlp.comandos.sonidos.Sounds;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import java.io.File;
import java.util.*;

public class Sonido implements Command {
    public static boolean rep=false;
    public static Snowflake a;
    @Override
    public void execute(MessageCreateEvent event) {
        new ComandoJoin().execute(event);
        Sounds.encontrarSonidos();
        VoiceChannel v = event.getMember().orElse(null).getVoiceState().block().getChannel().block();
        a=event.getGuildId().get();

        String sonidoAReproducir=event.getMessage().getContent();
        String [] mensaje= event.getMessage().getContent().split(" ");
        for (Map.Entry<String, File> entry  : Sounds.getSonidos().entrySet()) {
                if(entry.getKey().contains(mensaje[mensaje.length-1].toLowerCase())){
                    sonidoAReproducir=entry.getValue().getAbsolutePath();
                    System.out.println(sonidoAReproducir);
                    break;
                }else {
                    sonidoAReproducir=null;
                }
        }
        if(sonidoAReproducir==null){

            File[] values = Sounds.getSonidos().values().toArray(new File[0]);
            sonidoAReproducir = values[(int) (0+Math.random()*values.length)].getAbsolutePath();
        }
        Bot.playerManager.loadItem(sonidoAReproducir, new AudioLoadResultHandler()  {
            final TrackScheduler scheduler =GuildAudioManager.of(v.getGuildId()).getScheduler();
            @Override
            public void trackLoaded(AudioTrack audioTrack) {

                    scheduler.play(audioTrack);
                    rep=true;

            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

            }

            @Override
            public void noMatches() {
                Bot.escribirMensaje((TextChannel) event.getMessage().getChannel().block(),event.getMember().orElse(null).getNicknameMention()+"no tengo ese sonido pedazo de bOLUDOOOOOOOOO");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public String help() {
        List<String> sonidos = new LinkedList<>();
        for (Map.Entry<String, File> entry  : Sounds.getSonidos().entrySet()) {
            sonidos.add(removeExtention(entry.getKey()));
        }
        return "Uso ```"+Bot.indicador_Comando+"s <Sonido a reproducir>```\n" +
                "Sonidos disponibles : `"+sonidos.toString()+"`";
    }

    @Override
    public String getName() {
        return "s";
    }

    public  String removeExtention(String filePath) {
        File f = new File(filePath); // if it's a directory, don't remove the extention
         if (f.isDirectory())
             return f.getName(); 
         String name = f.getName(); // if it is a hidden file 
         if (name.startsWith(".")) { // if there is no extn, do not rmove one... 
              if (name.lastIndexOf('.') == name.indexOf('.')) 
                  return name;
         } // if there is no extention, don't do anything
        if (!name.contains("."))
            return name; // Otherwise, remove the last 'extension type thing' 
        return name.substring(0, name.lastIndexOf('.'));
    }
    
}
