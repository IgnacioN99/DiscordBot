package com.info.inge.unlp;
import com.info.inge.unlp.comandos.Sonido;

import com.info.inge.unlp.comandos.sonidos.Sounds;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.TextChannel;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    // Creates AudioPlayer instances and translates URLs to AudioTrack instances
    public final static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
     static String token ;
      static DiscordClient client ;
    public  static GatewayDiscordClient gateway;
    public static char indicador_Comando='>';
   public static Map<String, Command> comandos= new HashMap<>();

    public static void main(String[] args) {
        File arch=new File("./token");
        BufferedReader buffer;
            try {
                buffer=new BufferedReader(new FileReader(arch));
                token=buffer.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(token!=null) {
                client = DiscordClient.create(token);
                gateway = client.login().block();
                new Bot().agregarComandos();
                Sounds.encontrarSonidos();
                // This is an optimization strategy that Discord4J can utilize. It is not important to understand
                playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
                // Allow playerManager to parse remote sources like YouTube links
                AudioSourceManagers.registerRemoteSources(playerManager);
                playerManager.registerSourceManager(new LocalAudioSourceManager());


                try {
                    gateway.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> {
                        String content = event.getMessage().getContent();
                        String nick =event.getMember().orElse(null).getNicknameMention();
                        try {
                            if (content.equals(indicador_Comando + "help")) {
                                escribirMensaje((TextChannel) event.getMessage().getChannel().block(), nick+" Comandos disponibles```" + comandos.keySet().toString() + "```");

                            }
                            for (final Map.Entry<String, Command> entry : comandos.entrySet()) {

                                if (content.startsWith(indicador_Comando + "help" + " " + entry.getKey())) {
                                    escribirMensaje((TextChannel) event.getMessage().getChannel().block(),nick+" "+ entry.getValue().help());
                                    break;
                                }

                                if (content.startsWith(indicador_Comando + entry.getKey())) {
                                    entry.getValue().execute(event);
                                    break;
                                }
                            }
                        } catch (Throwable es) {
                            es.printStackTrace();
                        }

                    });

                    gateway.onDisconnect().block();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Recuerde crear un archivo sin extension con el nombre \"token\" con el token dentro");
            }
    }
    public static void escribirMensaje(TextChannel canal,String mensaje){
        try {
            canal.createMessage(mensaje).block();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void agregarComandos() {
        comandos.put("ping", new Command() {
            @Override
            public void execute(MessageCreateEvent event) {
                escribirMensaje((TextChannel) event.getMessage().getChannel().block(), "Pong");
            }

            @Override
            public String help() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        });
        comandos.put("s", new Sonido());
        comandos.put("random", new Command() {
            @Override
            public void execute(MessageCreateEvent event) {
                String [] aux= event.getMessage().getContent().split(" ");
                Thread t = new Thread(new prueba(Integer.parseInt(aux[aux.length - 1]), event));
                t.start();
            }

            @Override
            public String help() {
                return "Ingresa de forma aleatoria x veces cada n minutos\n" +
                        "uso ```"+indicador_Comando+"random <minutos> ```";
            }

            @Override
            public String getName() {
                return "random pepe";
            }
        }

    );
    }

    static class prueba implements Runnable{
        private final int tiempo;
        MessageCreateEvent event;
        public prueba(int tiempo,MessageCreateEvent event){
            this.tiempo=tiempo;
            this.event=event;
        }
        @Override
        public void run() {
            int rand= (int)(1+(Math.random()*100));
            Bot.escribirMensaje((TextChannel) event.getMessage().getChannel().block(),"el bot va a ingresar " + rand + " veces");

            for (int r = 0; r < 5; r++) {
                for (int i = 0; i < tiempo; i++) {
                    try {
                        System.out.println("ya va");
                        Thread.sleep(tiempo * 60 * 1000);
                        System.out.println("aca esta");
                        comandos.get("s").execute(event);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }

        }
    }


    }




