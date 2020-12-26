package com.info.inge.unlp;
import com.info.inge.unlp.comandos.Sonido;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {
        private final List<AudioTrack> queue;

    public AudioPlayer getPlayer() {
        return player;
    }

    private final AudioPlayer player;

    private boolean sonido=false;
        public TrackScheduler(final AudioPlayer player) {
            // The queue may be modifed by different threads so guarantee memory safety
            // This does not, however, remove several race conditions currently present
            queue = Collections.synchronizedList(new LinkedList<>());
            this.player = player;
        }

        public List<AudioTrack> getQueue() {
            return queue;
        }

        public boolean play(final AudioTrack track) {
            return play(track, false);
        }

        public boolean play(final AudioTrack track, final boolean force) {
            final boolean playing = player.startTrack(track, !force);
            if (!playing) {
                queue.add(track);
            }

            return playing;
        }

        public boolean skip() {
            if(queue.isEmpty()) {
                player.stopTrack();
                Bot.gateway.getVoiceConnectionRegistry().disconnect(Sonido.a).block();
                return true;
            }
            return !queue.isEmpty() && play(queue.remove(0), true);
        }


        @Override
        public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason) {
            // Advance the player if the track completed naturally (FINISHED) or if the track cannot play (LOAD_FAILED)
            if (endReason.mayStartNext) {
                skip();
            }
        }
        public boolean vaciar(){
            queue.clear();
            player.stopTrack();
            return true;
        }
}